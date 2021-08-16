package com.github.bluecatlee.gs4d.common.utils.lock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

public class RedisLock {
    private static Logger log = LoggerFactory.getLogger(RedisLock.class);
    private StringRedisTemplate stringRedisTemplate;
    private String lockKey;
    private String value;
    private static final String PREFIX_KEY = "distributed.lock.redis_";
    private boolean locked;
    private int expireSecond;
    private int timeout;
    private static final int DEFAULT_RETRY_INTERVAL_MILLIS = 100;
    private int retryCount;
    private int retryIntervalMillisecond;

    /** @deprecated */
    @Deprecated
    public RedisLock(StringRedisTemplate stringRedisTemplate, String lockKey) {
        this.locked = false;
        this.expireSecond = 60;
        this.timeout = 0;
        this.retryCount = 0;
        this.retryIntervalMillisecond = 0;
        this.stringRedisTemplate = stringRedisTemplate;
        this.lockKey = PREFIX_KEY + lockKey;
    }

    public RedisLock(StringRedisTemplate stringRedisTemplate, String lockKey, int expireSecond) {
        this.locked = false;
        this.expireSecond = 60;
        this.timeout = 0;
        this.retryCount = 0;
        this.retryIntervalMillisecond = 0;
        this.stringRedisTemplate = stringRedisTemplate;
        this.lockKey = PREFIX_KEY + lockKey;
        this.expireSecond = expireSecond;
    }

    public RedisLock(StringRedisTemplate stringRedisTemplate, String lockKey, int expireSecond, int timeout) {
        this(stringRedisTemplate, lockKey, expireSecond);
        this.timeout = timeout;
    }

    public RedisLock(StringRedisTemplate stringRedisTemplate, String lockKey, int expireSecond, int retryCount, int retryIntervalMillisecond) {
        this(stringRedisTemplate, lockKey, expireSecond);
        this.retryCount = retryCount;
        this.retryIntervalMillisecond = retryIntervalMillisecond;
    }

    /** @deprecated */
    @Deprecated
    public boolean acquireLock(long expireSecond) {
        if (expireSecond <= 0L) {
            throw new RuntimeException("获取redids分布锁，过期秒数应大于零！");
        } else {
            this.expireSecond = (int)expireSecond;
            return this.acquireLock();
        }
    }

    /** @deprecated */
    @Deprecated
    public boolean acquireLock() {
        return this.lock();
    }

    public boolean lock() {
        log.debug("begin redisLock lock");
        long lockTimeout = this.getCurrentTimeMillisFromRedis() + (long)(this.expireSecond * 1000) + 1L;
        String strLockTimeout = String.valueOf(lockTimeout);
        if (this.setNX(this.lockKey, strLockTimeout, (long)this.expireSecond)) {
            this.locked = true;
            this.value = strLockTimeout;
            log.debug("setNX成功");
            log.debug("end redisLock lock");
            return true;
        } else {
            String strCurrentLockTimeout = (String)this.stringRedisTemplate.opsForValue().get(this.lockKey);
            log.debug("lockKey:{},strCurrentLockTimeout:{}", this.lockKey, strCurrentLockTimeout);
            if (strCurrentLockTimeout != null && Long.parseLong(strCurrentLockTimeout) < this.getCurrentTimeMillisFromRedis()) {
                log.debug("锁已过期！");
                String strOldLockTimeout = (String)this.stringRedisTemplate.opsForValue().getAndSet(this.lockKey, strLockTimeout);
                if (strOldLockTimeout != null && strOldLockTimeout.equals(strCurrentLockTimeout)) {
                    log.debug("重新抢到锁");
                    this.stringRedisTemplate.expire(this.lockKey, (long)(this.expireSecond * 1000), TimeUnit.MILLISECONDS);
                    this.value = strLockTimeout;
                    this.locked = true;
                    log.debug("end redisLock lock");
                    return true;
                }
            }

            log.debug("未抢到锁");
            log.debug("end redisLock lock");
            return false;
        }
    }

    public boolean tryLock() {
        if (this.retryCount > 0 && this.retryIntervalMillisecond > 0) {
            do {
                if (this.lock()) {
                    return true;
                }

                try {
                    Thread.sleep((long)this.retryIntervalMillisecond);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                --this.retryCount;
            } while(this.retryCount > 0);
        } else {
            do {
                if (this.lock()) {
                    return true;
                }

                this.timeout -= DEFAULT_RETRY_INTERVAL_MILLIS;
            } while(this.timeout > 0);
        }

        return false;
    }

    /** @deprecated */
    @Deprecated
    public void releaseLock() {
        this.unlock();
    }

    public void unlock() {
        log.debug("begin redisLock unlock");
        if (this.locked) {
            String strCurrentLockTimeout = (String)this.stringRedisTemplate.opsForValue().get(this.lockKey);
            if (strCurrentLockTimeout == null) {
                log.debug("锁已不存在了");
            } else {
                this.stringRedisTemplate.delete(this.lockKey);
            }

            this.locked = false;
        } else {
            log.debug("原来就没锁住");
        }

        log.debug("end redisLock unlock");
    }

    private boolean setNX(final String key, final String value, final long second) {
        return (Boolean)this.stringRedisTemplate.execute(new RedisCallback<Boolean>() {
            public Boolean doInRedis(RedisConnection connection) {
                byte[] keyBytes = RedisLock.this.stringRedisTemplate.getStringSerializer().serialize(key);
                boolean locked = connection.setNX(keyBytes, RedisLock.this.stringRedisTemplate.getStringSerializer().serialize(value));
                if (locked && second > 0L) {
                    connection.expire(keyBytes, second);
                }

                return locked;
            }
        });
    }

    public long getCurrentTimeMillisFromRedis() {
        return (Long)this.stringRedisTemplate.execute(new RedisCallback<Long>() {
            public Long doInRedis(RedisConnection redisConnection) throws DataAccessException {
                return redisConnection.time();
            }
        });
    }

    public String getLockKey() {
        return this.lockKey;
    }

    public boolean islocked() {
        return this.locked;
    }
}

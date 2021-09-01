package com.tranboot.client.core.txc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;

public final class ReentrantRedLock {
    private static final ThreadLocal<List<ReentrantRedLock>> holdedLock = new ThreadLocal();
    private ReentrantRedLock.RedisLock innerLock;

    public String getRedLockValue() {
        return this.innerLock.value;
    }

    private ReentrantRedLock(ReentrantRedLock.RedisLock innerLock) {
        this.innerLock = innerLock;
    }

    private ReentrantRedLock(StringRedisTemplate stringRedisTemplate, String lockKey, int expireSecond) {
        this.innerLock = new ReentrantRedLock.RedisLock(stringRedisTemplate, lockKey, expireSecond);
    }

    protected static ReentrantRedLock redisLock(StringRedisTemplate stringRedisTemplate, String lockKey, int expireSecond) {
        return new ReentrantRedLock(stringRedisTemplate, lockKey, expireSecond);
    }

    public boolean tryLock(long timeout, TimeUnit unit) {
        if (holdedLock.get() == null) {
            holdedLock.set(new ArrayList());
        }

        if (((List)holdedLock.get()).contains(this)) {
            this.innerLock.value = ((ReentrantRedLock)((List)holdedLock.get()).get(((List)holdedLock.get()).indexOf(this))).getRedLockValue();
            return true;
        } else if (this.innerLock.tryLock(timeout, unit)) {
            ((List)holdedLock.get()).add(this);
            return true;
        } else {
            return false;
        }
    }

    public void unlock() {
        this.innerLock.unlock();
        ((List)holdedLock.get()).remove(this);
    }

    public boolean equals(Object o) {
        if (!(o instanceof ReentrantRedLock)) {
            return false;
        } else {
            ReentrantRedLock that = (ReentrantRedLock)o;
            return this.innerLock.getLockKey().equals(that.innerLock.getLockKey());
        }
    }

    static final class RedisLock {
        private static Logger log = LoggerFactory.getLogger("RedisLock");
        private StringRedisTemplate stringRedisTemplate;
        private String lockKey;
        private String value;
        private static final String PREFIX_KEY = "txc_redlock_";
        private boolean locked;
        private int expireSecond;
        private static final int DEFAULT_RETRY_INTERVAL_MILLIS_SED = 2;

        private RedisLock(StringRedisTemplate stringRedisTemplate, String lockKey, int expireSecond) {
            this.locked = false;
            this.expireSecond = 60;
            this.stringRedisTemplate = stringRedisTemplate;
            this.lockKey = "txc_redlock_" + lockKey;
            this.expireSecond = expireSecond;
        }

        private boolean lock() {
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

        private boolean tryLock(long timeout, TimeUnit unit) {
            long nanosTimeout = unit.toNanos(timeout);
            long deadline = System.nanoTime() + nanosTimeout;

            while(!this.lock()) {
                try {
                    Thread.sleep((long)(2 * randInt(0, 6)));
                    nanosTimeout = deadline - System.nanoTime();
                } catch (InterruptedException var9) {
                    var9.printStackTrace();
                }

                if (nanosTimeout <= 0L) {
                    return false;
                }
            }

            return true;
        }

        private void unlock() {
            log.debug("begin redisLock unlock");
            if (this.locked) {
                String strCurrentLockTimeout = (String)this.stringRedisTemplate.opsForValue().get(this.lockKey);
                if (strCurrentLockTimeout == null) {
                    log.debug("锁已不存在了");
                } else {
                    Long currentTimeMillisFromRedis = this.getCurrentTimeMillisFromRedis();
                    if (currentTimeMillisFromRedis > Long.parseLong(strCurrentLockTimeout)) {
                        log.debug("锁已经到期了，不做任何处理。currentTimeMillisFromRedis:{},strCurrentLockTimeout:{}", currentTimeMillisFromRedis, strCurrentLockTimeout);
                    } else {
                        log.debug("锁未过期!");
                        if (this.value.equals(strCurrentLockTimeout)) {
                            log.debug("锁确定是自己的,可以删除。");
                            this.stringRedisTemplate.delete(this.lockKey);
                        } else {
                            log.debug("锁内容不是自己放的，当前不是自己的锁，不执行删除。");
                        }
                    }
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

        private long getCurrentTimeMillisFromRedis() {
            return (Long)this.stringRedisTemplate.execute(new RedisCallback<Long>() {
                public Long doInRedis(RedisConnection redisConnection) throws DataAccessException {
                    return redisConnection.time();
                }
            });
        }

        private String getLockKey() {
            return this.lockKey;
        }

        private static int randInt(int n, int m) {
            int w = m - n;
            return (int)Math.ceil(Math.random() * (double)w + (double)n);
        }
    }
}


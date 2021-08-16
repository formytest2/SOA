package com.github.bluecatlee.gs4d.sequence.task;

import com.github.bluecatlee.gs4d.common.utils.lock.RedisLock;
import com.github.bluecatlee.gs4d.sequence.dao.AutoSequenceDao;
import com.github.bluecatlee.gs4d.sequence.model.AutoSequence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * AutoSequence清除
 */
@Component
public class AutoSequenceClearListener {

    @Resource(name = "stringRedisTemplate")
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private AutoSequenceDao autoSequenceDao;

    protected static Logger logger = LoggerFactory.getLogger(AutoSequenceClearListener.class);

    public AutoSequenceClearListener() {
    }

    public void listenAutoSequenceClear() {
        List list = null;
        RedisLock redisLock = null;
        Boolean get = true;
        String keySuffix = "sequence_auto_clear";
        String key2 = "auto_clear_exist";

        try {
            redisLock = new RedisLock(this.stringRedisTemplate, keySuffix, 100, 30, 10);
            if (!redisLock.tryLock()) {
                get = false;
                logger.info("&&&&&&获取锁超时时间:另外一台服务器正在更新");
            } else {
                String val = (String)this.stringRedisTemplate.opsForValue().get(key2);
                if (val == null || val.trim().equals("")) {
                    list = this.autoSequenceDao.getClearAutoSeq();
                    if (list.size() <= 0) {
                        return;
                    }

                    this.autoSequenceDao.updateAutoCurrentNum();
                    Iterator iterator = list.iterator();

                    while(iterator.hasNext()) {
                        AutoSequence autoSequence = (AutoSequence)iterator.next();
                        String seqName = autoSequence.getSeqName().trim();
                        Long tenantNumId = autoSequence.getTenantNumId();
                        Long dataSign = autoSequence.getDataSign();
                        String cacheFlowCodeKey = seqName + tenantNumId + dataSign + "_auto_single";
                        if (tenantNumId == 0L && dataSign == 0L) {
                            cacheFlowCodeKey = seqName.trim() + "_auto_com";
                        }

                        this.stringRedisTemplate.opsForValue().set(cacheFlowCodeKey, "");
                        String cacheSequenceConfigKey = cacheFlowCodeKey + "_info_key";
                        this.stringRedisTemplate.opsForValue().set(cacheSequenceConfigKey, "");
                    }

                    this.stringRedisTemplate.opsForValue().set(key2, "exist", 1L, TimeUnit.HOURS);
                    logger.info("自增序列共更新：" + list.size() + "条");
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (redisLock != null && get) {
                redisLock.unlock();
                logger.info("Redis解锁时间：" + System.currentTimeMillis());
            }

        }

    }
}

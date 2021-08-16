package com.github.bluecatlee.gs4d.sequence.task;

import com.github.bluecatlee.gs4d.common.utils.lock.RedisLock;
import com.github.bluecatlee.gs4d.sequence.dao.SequenceDao;
import com.github.bluecatlee.gs4d.sequence.model.CreateSequence;
import com.github.bluecatlee.gs4d.sequence.service.SequenceTimeService;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 更新zookeeper存的sequenceTime和数据库sequenceTime
 */
@Component
public class UpdateSequenceTime {

    @Value("#{settings['zk.sequence.host.port']}")
    public String zkaddress;

    @Autowired
    private SequenceTimeService sequenceTimeService;

    @Autowired
    private SequenceDao sequenceDao;

    @Resource(name = "stringRedisTemplate")
    private StringRedisTemplate stringRedisTemplate;

    protected static Logger logger = LoggerFactory.getLogger(UpdateSequenceTime.class);

    public void update() {
        ZooKeeper zooKeeper = null;
        RedisLock redisLock = null;
        Boolean bool = Boolean.valueOf(true);
        String lockKey = "update_job_lock";
        try {
            redisLock = new RedisLock(this.stringRedisTemplate, lockKey, 100, 30, 10);
            if (!redisLock.tryLock()) {
                bool = Boolean.valueOf(false);
                logger.info("******获取锁超时时间：另一台服务器正在更新");
            } else {
                zooKeeper = new ZooKeeper(this.zkaddress, 50000, new Watcher() {
                    public void process(WatchedEvent watchedEvent) {
                        UpdateSequenceTime.logger.info("序列号开始更新zk节点");
                    }
                });
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Calendar calendar = Calendar.getInstance();
                logger.info("当前日期**************" + simpleDateFormat.format(calendar.getTime()));
                calendar.add(Calendar.DATE, 1);
                String newSequenceTime = simpleDateFormat.format(calendar.getTime());
                logger.info("增加一天的日期为****************:" + newSequenceTime);
                String currentSequenceTime = this.sequenceTimeService.getTime();
                if (!currentSequenceTime.equals(newSequenceTime)) {
                    if (Integer.valueOf(currentSequenceTime.replaceAll("-", "")).intValue() < Integer.valueOf(newSequenceTime.replaceAll("-", "")).intValue()) {
                        this.sequenceTimeService.updateTime(newSequenceTime, new Date());           // 更新数据库存的sequenceTime
                        this.sequenceDao.updateAllCurrentNum();                                     // 重置currentNum为1 (如果有seqNumStart， 重置为seqNumStart+1)
                        List<CreateSequence> list = this.sequenceDao.getSequenceWithCurrentNum();
                        if (list.size() > 0) {
                            for (CreateSequence createSequence : list) {
                                Long seqNumStart = createSequence.getSeqNumStart();
                                Long currentNum = Long.valueOf(1L);
                                if (seqNumStart.longValue() > 0L) {
                                    currentNum = createSequence.getSeqNumStart();
                                }
                                this.sequenceDao.updateCurrentNum(currentNum, createSequence.getSeries());
                            }
                        }
                        zooKeeper.setData("/seqNode/currentDayDate", newSequenceTime.getBytes(), -1);       // 更新zk存的sequenceTime
                        logger.info("序列号更新节点成功，本次更新的时间是 " + newSequenceTime);
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            try {
                if (zooKeeper != null) {
                    zooKeeper.close();
                    logger.info("ZK解锁时间：" + System.currentTimeMillis());
                }
                if (redisLock != null && bool.booleanValue()) {
                    redisLock.unlock();
                    logger.info("Redis解锁时间：" + System.currentTimeMillis());
                }
            } catch (InterruptedException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }
}


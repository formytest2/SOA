package com.github.bluecatlee.gs4d.transaction.service.impl;

import com.github.bluecatlee.gs4d.common.utils.lock.RedisLock;
import com.github.bluecatlee.gs4d.transaction.dao.TransactionLogDao;
import com.github.bluecatlee.gs4d.transaction.model.TRANSACTION_LOG;
import com.github.bluecatlee.gs4d.transaction.service.RollBackService;
import com.github.bluecatlee.gs4d.transaction.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

/**
 * 回滚失败的数据(transaction_state=3)定时重新回滚
 */
@Service("transactionCompensJobService")
public class TransactionCompensJobService {
    
    protected static Logger logger = LoggerFactory.getLogger(TransactionCompensJobService.class);
    
    @Autowired
    private TransactionLogDao transactionLogDao;

    @Autowired
    private RollBackService rollBackService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public void transactionCompensJob() {
        List<TRANSACTION_LOG> transactionLogList = null;
        RedisLock redisLock = null;
        Boolean unlocked = false;

        try {
            try {
                redisLock = new RedisLock(this.stringRedisTemplate, Constants.TransactionCompensJobLockKey, 500);
                if (!redisLock.lock()) {
                    unlocked = true;
                    logger.warn("分布式事务定时器补偿锁超时，其他机器正在补偿数据。");
                    return;
                }

                transactionLogList = this.transactionLogDao.getErrorTranstionLog();   // 查询回滚失败的事务日志
                if (transactionLogList.size() <= 0) {
                    return;
                }

                Iterator transactionLogListIterator = transactionLogList.iterator();

                while(transactionLogListIterator.hasNext()) {
                    TRANSACTION_LOG transactionLog = (TRANSACTION_LOG)transactionLogListIterator.next();
                    RedisLock lock = null;

                    try {
                        lock = new RedisLock(this.stringRedisTemplate, Constants.getTransactionLockKey(transactionLog.getTRANSACTION_ID()), 500);
                        if (!lock.lock()) {
                            logger.warn("分布式事务正在处理事务id为" + transactionLog.getTRANSACTION_ID() + "的数据");
                            return;
                        }

                        logger.info("发现分布式事务id为" + transactionLog.getTRANSACTION_ID() + "的数据存在回滚失败,开始补偿");
                        this.rollBackService.rollBackSql(transactionLog.getTRANSACTION_ID(), transactionLog.getTRANSACTION_ROLLBACK_FLAG(), true);
                    } catch (Exception e) {
                        logger.error("分布式事务消费失败" + e.getMessage(), e);
                    } finally {
                        if (lock != null) {
                            lock.unlock();
                        }

                    }
                }

                logger.debug("监听并补偿事务成功===============");
            } catch (Exception e) {
                logger.error("补偿10分钟前异常结束事务失败" + e.getMessage(), e);
            }

        } finally {
            if (redisLock != null && !unlocked) {
                redisLock.unlock();
            }

        }
    }
}


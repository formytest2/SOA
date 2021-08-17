package com.github.bluecatlee.gs4d.transaction.service.impl;

import com.github.bluecatlee.gs4d.common.utils.BatchUtil;
import com.github.bluecatlee.gs4d.common.utils.TransactionUtil;
import com.github.bluecatlee.gs4d.common.utils.lock.RedisLock;
import com.github.bluecatlee.gs4d.transaction.api.utils.TransactionApiUtil;
import com.github.bluecatlee.gs4d.transaction.dao.TransactionLogDao;
import com.github.bluecatlee.gs4d.transaction.dao.TransactionSqlLogDao;
import com.github.bluecatlee.gs4d.transaction.model.TRANSACTION_LOG;
import com.github.bluecatlee.gs4d.transaction.model.TRANSACTION_SQL_LOG;
import com.github.bluecatlee.gs4d.transaction.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.List;

/**
 * 定期删除事务日志
 *      用xxl-job等定时框架进行定时调用
 */
@Service("deleteSqlLogService")
public class DeleteSqlLogServiceImpl {
    
    private static Logger logger = LoggerFactory.getLogger(DeleteSqlLogServiceImpl.class);
    
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private TransactionLogDao transactionLogDao;

    @Autowired
    private TransactionSqlLogDao transactionSqlLogDao;

    @Resource(name = "transactionManager")
    private PlatformTransactionManager platformTransactionManager;

    @Value("#{settings['emailAddress']}")
    public String emailAddress;
    @Value("#{settings['emailDelayTime']}")
    private Integer emailDelayTime;

    public void deleteSqlLog() {
        RedisLock redisLock = null;

        try {
            redisLock = new RedisLock(this.stringRedisTemplate, Constants.TransactionDeleteJobLockKey, 500);
            if (redisLock.lock()) {
                Long count = this.transactionLogDao.getSuccessTranstionLogNum(); // 查询时间在今日之前的已成功核销的事务日志条数
                Long batchSize = 300L;
                Long batchCount = BatchUtil.batch(count, batchSize);

                for(Long i = 0L; i < batchCount; i = i + 1L) {
                    List<TRANSACTION_LOG> transactionLogList = this.transactionLogDao.getSuccessTranstionLog(i * batchSize, batchSize);
                    Iterator iterator = transactionLogList.iterator();

                    while(iterator.hasNext()) {
                        TRANSACTION_LOG transactionLog = (TRANSACTION_LOG)iterator.next();
                        boolean canDelete = true;
                        List<TRANSACTION_SQL_LOG> transactionSqlLogs = this.transactionSqlLogDao.queryTransactionSignByTransactionId(transactionLog.getTRANSACTION_ID());
                        Iterator transactionSqlLogsIterator = transactionSqlLogs.iterator();

                        while(transactionSqlLogsIterator.hasNext()) {
                            TRANSACTION_SQL_LOG transactionSqlLog = (TRANSACTION_SQL_LOG)transactionSqlLogsIterator.next();
                            if (!transactionSqlLog.getTRANSACTION_SIGN().equals("Y")) {
                                logger.error("发现了事务标识不正确的数据，分布式事务ID是" + transactionLog.getTRANSACTION_ID() + "sqllog表series是" + transactionSqlLog.getSERIES());
                                canDelete = false;
                            }
                        }

                        if (canDelete) {
                            TransactionStatus transactionStatus = this.platformTransactionManager.getTransaction(TransactionUtil.newTransactionDefinition());

                            try {
                                this.transactionSqlLogDao.delete(transactionLog.getTRANSACTION_ID());
                                this.transactionLogDao.delete(transactionLog.getTRANSACTION_ID());
                                this.stringRedisTemplate.delete(TransactionApiUtil.redisKeyStart + transactionLog.getTRANSACTION_ID());
                                this.platformTransactionManager.commit(transactionStatus);
                                logger.info("删除分布式事务ID" + transactionLog.getTRANSACTION_ID() + "成功。");
                            } catch (Exception e) {
                                logger.error("分布式事务删除日志时报错，理由是" + e.getMessage(), e);
                                this.platformTransactionManager.rollback(transactionStatus);
                            }
                        }
                    }
                }

                return;
            }

            logger.warn("分布式事务定时器删除锁超时，其他机器正在删除过期数据。");
        } catch (Exception e) {
            logger.error("分布式事务定时删除时报错，原因:" + e.getMessage());
            return;
        } finally {
            if (redisLock != null) {
                redisLock.unlock();
            }

        }

    }

}


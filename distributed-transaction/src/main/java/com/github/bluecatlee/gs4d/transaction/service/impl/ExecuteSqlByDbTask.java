package com.github.bluecatlee.gs4d.transaction.service.impl;

import com.github.bluecatlee.gs4d.common.datasource.DataSourceContextHolder;
import com.github.bluecatlee.gs4d.common.utils.TransactionUtil;
import com.github.bluecatlee.gs4d.transaction.api.utils.TransactionApiUtil;
import com.github.bluecatlee.gs4d.transaction.dao.TargetHandleDao;
import com.github.bluecatlee.gs4d.transaction.dao.TransactionLogDao;
import com.github.bluecatlee.gs4d.transaction.dao.TransactionSqlLogDao;
import com.github.bluecatlee.gs4d.transaction.model.TRANSACTION_SQL_LOG;
import com.github.bluecatlee.gs4d.transaction.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

@Service
public class ExecuteSqlByDbTask implements Callable {

    private static Logger logger = LoggerFactory.getLogger(ExecuteSqlByDbTask.class);

    private PlatformTransactionManager dynamicTransactionManager;
    private TargetHandleDao targetHandleDao;
    private TransactionSqlLogDao transactionSqlLogDao;
    public String emailAddress;
    private List<TRANSACTION_SQL_LOG> transactionSqlLogList;
    private TransactionLogDao transactionLogDao;
    private StringRedisTemplate stringRedisTemplate;

    public ExecuteSqlByDbTask(List<TRANSACTION_SQL_LOG> transactionSqlLogList, TransactionSqlLogDao transactionSqlLogDao, PlatformTransactionManager dynamicTransactionManager, TransactionLogDao transactionLogDao, TargetHandleDao targetHandleDao, StringRedisTemplate stringRedisTemplate) {
        this.transactionSqlLogList = transactionSqlLogList;
        this.transactionSqlLogDao = transactionSqlLogDao;
        this.dynamicTransactionManager = dynamicTransactionManager;
        this.targetHandleDao = targetHandleDao;
        this.transactionLogDao = transactionLogDao;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public Integer call() {
        Boolean var1 = true;
        Integer result = Constants.rollback_success;
        DataSourceContextHolder.clearDataSourceType();
        DataSourceContextHolder.setDataSourceType(((TRANSACTION_SQL_LOG)this.transactionSqlLogList.get(0)).getSOURCE_DB().toLowerCase());
        TransactionStatus transactionStatus = this.dynamicTransactionManager.getTransaction(TransactionUtil.newTransactionDefinition(30));
        Long series = null;
        String sql = null;
        Integer resultNum = 0;  // 影响行数
        Map<Long, Integer> series2ResultNumMap = new HashMap();

        try {
            Integer count = this.targetHandleDao.queryCountByTransactionKey(((TRANSACTION_SQL_LOG)this.transactionSqlLogList.get(0)).getTRANSACTION_ID() + "_" + ((TRANSACTION_SQL_LOG)this.transactionSqlLogList.get(0)).getTRANSACTION_DB_ID());
            TRANSACTION_SQL_LOG transactionSqlLog;
            Iterator transactionSqlLogListIterator;
            if (count > 0) {
                logger.warn("发现重复回滚sql，本次过滤，分布式事务ID是" + ((TRANSACTION_SQL_LOG)this.transactionSqlLogList.get(0)).getTRANSACTION_ID() + ",子事务ID是:" + ((TRANSACTION_SQL_LOG)this.transactionSqlLogList.get(0)).getTRANSACTION_DB_ID());
                transactionSqlLogListIterator = this.transactionSqlLogList.iterator();

                while(transactionSqlLogListIterator.hasNext()) {
                    transactionSqlLog = (TRANSACTION_SQL_LOG)transactionSqlLogListIterator.next();
                    this.transactionSqlLogDao.i(transactionSqlLog.getSERIES());
                }

                this.dynamicTransactionManager.commit(transactionStatus);
            }

            transactionSqlLogListIterator = this.transactionSqlLogList.iterator();

            while(true) {
                while(transactionSqlLogListIterator.hasNext()) {
                    transactionSqlLog = (TRANSACTION_SQL_LOG)transactionSqlLogListIterator.next();
                    series = transactionSqlLog.getSERIES();
                    if (transactionSqlLog.getSQL_STATUS() == Constants.sql_status_2) {
                        logger.debug("发现了transID为" + transactionSqlLog.getTRANSACTION_ID() + "sqlID为" + transactionSqlLog.getTRANSACTION_DB_ID() + "的数据，状态为2，直接忽略。");
                    } else {
                        Long bizRecordCount;
                        String bizRedisValue;
                        if (transactionSqlLog.getSQL_IS_OUT_TIME().equals("Y") && transactionSqlLog.getSQL_TYPE() != TransactionApiUtil.sqlDeleteType) {
                            // 非删除操作事务超时
                            bizRecordCount = this.targetHandleDao.queryCountBySqlKeyValueAndTxc(transactionSqlLog.getSQL_KEY_VALUE(), transactionSqlLog.getSQL_ID(), transactionSqlLog.getTABLE_NAME());
                            bizRedisValue = (String)this.stringRedisTemplate.opsForValue().get(transactionSqlLog.getBIZ_REDIS_KEY());
                            if (transactionSqlLog.getBIZ_REDIS_VALUE().equals(bizRedisValue)) {
                                this.stringRedisTemplate.delete(transactionSqlLog.getBIZ_REDIS_KEY());
                            }

                            if (bizRecordCount == 0L) {
                                logger.debug("发现了transID为" + transactionSqlLog.getTRANSACTION_ID() + "sqlID为" + transactionSqlLog.getTRANSACTION_DB_ID() + "的数据，超时且原数据没有update txc字段，直接忽略。");
                                continue;
                            }

                            logger.debug("发现了transID为" + transactionSqlLog.getTRANSACTION_ID() + "sqlID为" + transactionSqlLog.getTRANSACTION_DB_ID() + "的数据，超时且原数据更新了update txc字段，执行回滚。");
                        } else if (transactionSqlLog.getSQL_IS_OUT_TIME().equals("Y") && transactionSqlLog.getSQL_TYPE() == TransactionApiUtil.sqlDeleteType) {
                            // 删除操作事务超时
                            bizRecordCount = this.targetHandleDao.queryCountBySqlKeyValue(transactionSqlLog.getSQL_KEY_VALUE(), transactionSqlLog.getTABLE_NAME());
                            bizRedisValue = (String)this.stringRedisTemplate.opsForValue().get(transactionSqlLog.getBIZ_REDIS_KEY());
                            if (transactionSqlLog.getBIZ_REDIS_VALUE().equals(bizRedisValue)) {
                                this.stringRedisTemplate.delete(transactionSqlLog.getBIZ_REDIS_KEY());
                            }

                            if (bizRecordCount > 0L) {
                                logger.debug("发现了transID为" + transactionSqlLog.getTRANSACTION_ID() + "sqlID为" + transactionSqlLog.getTRANSACTION_DB_ID() + "的数据，原始delete语句执行失败，忽略回滚。");
                                continue;
                            }

                            logger.debug("发现了transID为" + transactionSqlLog.getTRANSACTION_ID() + "sqlID为" + transactionSqlLog.getTRANSACTION_DB_ID() + "的数据，原始delete语句执行成功，执行回滚。");
                        }

                        sql = transactionSqlLog.getSQL();
                        if (!sql.trim().toUpperCase().startsWith("INSERT") && !sql.contains("where") && !sql.contains("WHERE")) {
                            throw new Exception("当前sql语句没有包含where条件");
                        }

                        logger.debug("发现了transID为" + transactionSqlLog.getTRANSACTION_ID() + "sqlID为" + transactionSqlLog.getTRANSACTION_DB_ID() + "的数据，开始执行回滚。");
                        resultNum = this.targetHandleDao.execute(sql);
                        series2ResultNumMap.put(transactionSqlLog.getSERIES(), resultNum);
                    }
                }

                this.targetHandleDao.insert(((TRANSACTION_SQL_LOG)this.transactionSqlLogList.get(0)).getTRANSACTION_ID() + "_" + ((TRANSACTION_SQL_LOG)this.transactionSqlLogList.get(0)).getTRANSACTION_DB_ID());
                this.dynamicTransactionManager.commit(transactionStatus);
                break;
            }
        } catch (Exception e) {
            var1 = false;
            result = Constants.rollback_fail;
            String message = e.getMessage();
            logger.error("分布式事务回滚失败,目标数据库" + ((TRANSACTION_SQL_LOG)this.transactionSqlLogList.get(0)).getSOURCE_DB() + "分布式事务ID" + ((TRANSACTION_SQL_LOG)this.transactionSqlLogList.get(0)).getTRANSACTION_ID() + "本次事务ID" + ((TRANSACTION_SQL_LOG)this.transactionSqlLogList.get(0)).getTRANSACTION_DB_ID() + "原因" + message, e);
            this.dynamicTransactionManager.rollback(transactionStatus);
            this.transactionSqlLogDao.updateRollbackFailReason(series, message);
            this.transactionLogDao.updateByTransactionId(Constants.ap, "N", ((TRANSACTION_SQL_LOG)this.transactionSqlLogList.get(0)).getTRANSACTION_ID());
        }

        if (var1) {
            Iterator iterator = this.transactionSqlLogList.iterator();

            while(iterator.hasNext()) {
                TRANSACTION_SQL_LOG transactionSqlLog = (TRANSACTION_SQL_LOG)iterator.next();
                Integer var17 = (Integer)series2ResultNumMap.get(transactionSqlLog.getSERIES());
                if (var17 == null) {
                    this.transactionSqlLogDao.i(transactionSqlLog.getSERIES());
                } else {
                    this.transactionSqlLogDao.a(transactionSqlLog.getSERIES(), var17);
                }
            }
        }

        return result;
    }
}


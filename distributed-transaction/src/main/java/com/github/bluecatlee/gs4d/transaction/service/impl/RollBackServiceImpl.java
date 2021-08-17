package com.github.bluecatlee.gs4d.transaction.service.impl;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.Future;
import javax.annotation.Resource;

import com.github.bluecatlee.gs4d.transaction.dao.TargetHandleDao;
import com.github.bluecatlee.gs4d.transaction.dao.TransactionLogDao;
import com.github.bluecatlee.gs4d.transaction.dao.TransactionSqlLogDao;
import com.github.bluecatlee.gs4d.transaction.model.TRANSACTION_SQL_LOG;
import com.github.bluecatlee.gs4d.transaction.service.RollBackService;
import com.github.bluecatlee.gs4d.transaction.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

@Service
public class RollBackServiceImpl implements RollBackService {

    protected static Logger logger = LoggerFactory.getLogger(RollBackServiceImpl.class);

    @Resource(name = "dynamicTransactionManager")
    private PlatformTransactionManager dynamicTransactionManager;

    @Autowired
    private TransactionLogDao transactionLogDao;

    @Autowired
    private TransactionSqlLogDao transactionSqlLogDao;

    @Autowired
    private TargetHandleDao targetHandleDao;

    @Value("#{settings['emailAddress']}")
    public String emailAddress;
    @Value("#{settings['emailDelayTime']}")
    private Integer emailDelayTime;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 回滚sql
     * @param transactionId             分布式事务id
     * @param transactionRollbackFlag   事务回滚标识 0为并行回滚，1为串行回滚
     * @param wetherOutTimeJob          是否是超时事务 true:超时事务 false:回滚事务  ???? todo
     */
    public void rollBackSql(Long transactionId, String transactionRollbackFlag, Boolean wetherOutTimeJob) {
        Map<Long, List<TRANSACTION_SQL_LOG>> dbId2TransactionSqlLogListMap = new HashMap();  // dbId -> TransactionSqlLogList

        try {
            List<TRANSACTION_SQL_LOG> transactionSqlLogList = this.transactionSqlLogDao.queryByTransactionId(transactionId);
            if (transactionSqlLogList.isEmpty() && !wetherOutTimeJob) {
                return;
            }

            if (transactionSqlLogList.isEmpty() && wetherOutTimeJob) {
                this.transactionLogDao.updateByTransactionId(Constants.error_end, "Y", transactionId);
                return;
            }

            Iterator transactionSqlLogListIterator = transactionSqlLogList.iterator();

            while(transactionSqlLogListIterator.hasNext()) {
                TRANSACTION_SQL_LOG transactionSqlLog = (TRANSACTION_SQL_LOG)transactionSqlLogListIterator.next();
                if (dbId2TransactionSqlLogListMap.get(transactionSqlLog.getTRANSACTION_DB_ID()) == null) {
                    ArrayList list = new ArrayList();
                    list.add(transactionSqlLog);
                    dbId2TransactionSqlLogListMap.put(transactionSqlLog.getTRANSACTION_DB_ID(), list);
                } else {
                    ((List)dbId2TransactionSqlLogListMap.get(transactionSqlLog.getTRANSACTION_DB_ID())).add(transactionSqlLog);
                }
            }

            List<Future> futureList = new ArrayList();
            List<Integer> callResults = new ArrayList();
            Iterator iterator = dbId2TransactionSqlLogListMap.entrySet().iterator();

            while(iterator.hasNext()) {
                Entry entry = (Entry)iterator.next();
                ExecuteSqlByDbTask executeSqlByDbTask = new ExecuteSqlByDbTask((List)entry.getValue(), this.transactionSqlLogDao, this.dynamicTransactionManager, this.transactionLogDao, this.targetHandleDao, this.stringRedisTemplate);
                if ("0".equals(transactionRollbackFlag)) {  // 并行回滚 放到线程池中一起执行
                    Future future = MqConsumerService.threadPoolExecutor.submit(executeSqlByDbTask);
                    futureList.add(future);
                } else {
                    if (!"1".equals(transactionRollbackFlag)) {
                        throw new RuntimeException("分布式事务出现异常回滚标识，既非异步标识也非同步标识，错误的标识是" + transactionRollbackFlag);
                    }

                    Integer result = executeSqlByDbTask.call();  // 串行回滚 依次遍历执行
                    callResults.add(result);
                }
            }

            if ("0".equals(transactionRollbackFlag)) {
                label100: {
                    iterator = futureList.iterator();

                    Integer result;
                    do {
                        if (!iterator.hasNext()) {
                            break label100;
                        }

                        Future future = (Future)iterator.next();
                        result = (Integer)future.get();
                    } while(result != null && result == Constants.rollback_success);

                    return;
                }
            } else {
                label103: {
                    iterator = callResults.iterator();

                    Integer result;
                    do {
                        if (!iterator.hasNext()) {
                            break label103;
                        }

                        result = (Integer)iterator.next();
                    } while(result != null && result == Constants.rollback_success);

                    return;
                }
            }

            this.transactionLogDao.updateByTransactionId(Constants.error_end, "Y", transactionId);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

    }
}


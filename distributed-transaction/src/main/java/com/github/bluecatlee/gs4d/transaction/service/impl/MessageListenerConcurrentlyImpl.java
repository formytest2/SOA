package com.github.bluecatlee.gs4d.transaction.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.github.bluecatlee.gs4d.common.utils.TransactionUtil;
import com.github.bluecatlee.gs4d.common.utils.lock.RedisLock;
import com.github.bluecatlee.gs4d.sequence.utils.SeqGetUtil;
import com.github.bluecatlee.gs4d.transaction.api.model.MqTransactionModel;
import com.github.bluecatlee.gs4d.transaction.api.model.RedisTransactionModel;
import com.github.bluecatlee.gs4d.transaction.api.model.SqlParamModel;
import com.github.bluecatlee.gs4d.transaction.api.utils.TransactionApiUtil;
import com.github.bluecatlee.gs4d.transaction.dao.TransactionLogDao;
import com.github.bluecatlee.gs4d.transaction.dao.TransactionSqlLogDao;
import com.github.bluecatlee.gs4d.transaction.model.TRANSACTION_LOG;
import com.github.bluecatlee.gs4d.transaction.model.TRANSACTION_SQL_LOG;
import com.github.bluecatlee.gs4d.transaction.service.RollBackService;
import com.github.bluecatlee.gs4d.transaction.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class MessageListenerConcurrentlyImpl implements MessageListenerConcurrently {
    
    protected static Logger logger = LoggerFactory.getLogger(MessageListenerConcurrentlyImpl.class);
    
    private StringRedisTemplate stringRedisTemplate;
    private PlatformTransactionManager transactionManager;
    private TransactionSqlLogDao transactionSqlLogDao;
    private RollBackService rollBackService;
    private TransactionLogDao transactionLogDao;

    public MessageListenerConcurrentlyImpl(StringRedisTemplate stringRedisTemplate, PlatformTransactionManager platformTransactionManager, TransactionSqlLogDao transactionSqlLogDao, RollBackService rollBackService, TransactionLogDao transactionLogDao) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.transactionManager = platformTransactionManager;
        this.transactionSqlLogDao = transactionSqlLogDao;
        this.rollBackService = rollBackService;
        this.transactionLogDao = transactionLogDao;
    }

    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> messageExtList, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
        Integer count = 0;
        ConsumeConcurrentlyStatus consumeConcurrentlyStatus = ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        RedisLock lock = null;
        Boolean wetherOutTimeJob = true;    // 是否是超时事务
        Boolean unlocked = false;

        try {
            Iterator messageExtListIterator = messageExtList.iterator();

            while(messageExtListIterator.hasNext()) {
                MessageExt messageExt = (MessageExt)messageExtListIterator.next();
                String body = new String(messageExt.getBody());
                MqTransactionModel mqTransactionModel = (MqTransactionModel)JSONObject.parseObject(body, MqTransactionModel.class);
                wetherOutTimeJob = mqTransactionModel.getWetherOutTimeJob();
                if (!mqTransactionModel.getWetherOutTimeJob()) {
                    logger.info("分布式事务收到消息,处理回滚消息为" + body);
                }

                lock = new RedisLock(this.stringRedisTemplate, Constants.getTransactionLockKey(mqTransactionModel.getTransactionId()), 500);
                if (!lock.lock()) {
                    unlocked = true;
                    logger.warn("分布式事务正在处理事务id为" + mqTransactionModel.getTransactionId() + "的数据");
                    return consumeConcurrentlyStatus;
                }

                count = this.transactionSqlLogDao.queryCountByTransactionId(mqTransactionModel.getTransactionId());
                if (count <= 0) {
                    String redisKey = TransactionApiUtil.redisKeyStart + mqTransactionModel.getTransactionId();
                    HashOperations hashOperations = this.stringRedisTemplate.opsForHash();
                    Set keySet = hashOperations.keys(redisKey);
                    if (keySet.size() > 0) {
                        TransactionStatus transactionStatus = this.transactionManager.getTransaction(TransactionUtil.newTransactionDefinition());

                        try {
                            Iterator keySetIterator = keySet.iterator();

                            while(keySetIterator.hasNext()) {
                                String hashKey = (String)keySetIterator.next();
                                RedisTransactionModel redisTransactionModel = (RedisTransactionModel)JSONObject.parseObject((String)hashOperations.get(redisKey, hashKey), RedisTransactionModel.class);
                                Long transactionOutTimeSecond;
                                if (redisTransactionModel.getStatus() == null || redisTransactionModel.getStatus() == 0) {
                                    Iterator SqlParamModelListIterator = redisTransactionModel.getSqlParamModel().iterator();

                                    while(SqlParamModelListIterator.hasNext()) {
                                        SqlParamModel sqlParamModel = (SqlParamModel)SqlParamModelListIterator.next();
                                        transactionOutTimeSecond = redisTransactionModel.getTransactionOutTimeSecond();
                                        if (redisTransactionModel.getTransactionOutTimeSecond() == null) {
                                            transactionOutTimeSecond = 60000L;
                                        }

                                        if (System.currentTimeMillis() - sqlParamModel.getInsertRedisTime() < transactionOutTimeSecond) {
                                            throw new RuntimeException("本次发现主事务超时，但是子事务还没有超时的事务，暂时将不会回滚。主事务ID" + mqTransactionModel.getTransactionId() + ",数据库事务ID" + hashKey);
                                        }
                                    }
                                }

                                if (mqTransactionModel.getWetherOutTimeJob() != null && mqTransactionModel.getWetherOutTimeJob() && mqTransactionModel.getSystemNowTime().getTime() - redisTransactionModel.getTransactionStartDtme().getTime() < redisTransactionModel.getTransactionOutTimeSecond()) {
                                    this.transactionManager.rollback(transactionStatus);
                                    return consumeConcurrentlyStatus;
                                }

                                if (mqTransactionModel.getWetherOutTimeJob()) {
                                    logger.info("分布式事务定时器超时补偿消息为" + body);
                                    logger.debug("定时器时间触发当前数据库时间为" + (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date(mqTransactionModel.getSystemNowTime().getTime())) + ",事务开始时间为" + (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date(redisTransactionModel.getTransactionStartDtme().getTime())) + "超时毫秒数" + redisTransactionModel.getTransactionOutTimeSecond());
                                } else {
                                    logger.info("分布式事务处理回滚消息为" + body);
                                }

                                TRANSACTION_SQL_LOG transactionSqlLog = new TRANSACTION_SQL_LOG();
                                transactionSqlLog.setTRANSACTION_ID(redisTransactionModel.getTransactionId());
                                transactionSqlLog.setTRANSACTION_DB_ID(redisTransactionModel.getTransactionSqlId());
                                transactionSqlLog.setSOURCE_DB(redisTransactionModel.getSourceDb());
                                transactionSqlLog.setCOMMIT_SQL_DTME(redisTransactionModel.getTransactionStartDtme());
                                transactionSqlLog.setTRANSACTION_SIGN("N");
                                transactionSqlLog.setSQL_TIMEOUT(redisTransactionModel.getTransactionOutTimeSecond());

                                for(int i = 0; i < redisTransactionModel.getSqlParamModel().size(); ++i) {
                                    transactionOutTimeSecond = SeqGetUtil.getNoSubSequence(Constants.DISTRIBUTED_TRANSACTION_SEQUENCE);
                                    transactionSqlLog.setSERIES(transactionOutTimeSecond);
                                    transactionSqlLog.setSQL_STATUS(redisTransactionModel.getStatus());
                                    transactionSqlLog.setSQL(((SqlParamModel)redisTransactionModel.getSqlParamModel().get(i)).getSql());
                                    transactionSqlLog.setSQL_LEVEL(i);
                                    transactionSqlLog.setTABLE_NAME(((SqlParamModel)redisTransactionModel.getSqlParamModel().get(i)).getTable());
                                    if (redisTransactionModel.getStatus() != 0) {
                                        transactionSqlLog.setSQL_IS_OUT_TIME("N");
                                    } else {
                                        transactionSqlLog.setSQL_IS_OUT_TIME("Y");
                                    }

                                    transactionSqlLog.setSQL_KEY_VALUE(((SqlParamModel)redisTransactionModel.getSqlParamModel().get(i)).getTableKeyValueModels());
                                    transactionSqlLog.setSQL_ID(((SqlParamModel)redisTransactionModel.getSqlParamModel().get(i)).getTxc());
                                    transactionSqlLog.setBIZ_REDIS_KEY(((SqlParamModel)redisTransactionModel.getSqlParamModel().get(i)).getRedisLockKey());
                                    transactionSqlLog.setSQL_TYPE(((SqlParamModel)redisTransactionModel.getSqlParamModel().get(i)).getSqlType());
                                    transactionSqlLog.setBIZ_REDIS_VALUE(((SqlParamModel)redisTransactionModel.getSqlParamModel().get(i)).getRedisLockValue());
                                    this.transactionSqlLogDao.insert(transactionSqlLog);
                                }
                            }

                            this.transactionManager.commit(transactionStatus);
                        } catch (Exception e) {
                            logger.error("数据库插入失敗！" + e.getMessage(), e);
                            this.transactionManager.rollback(transactionStatus);
                            throw new RuntimeException("分布式事务把业务sql存入mysql时失败，原因" + e.getMessage());
                        }
                    }
                }

                List<TRANSACTION_LOG> transactionLogs = this.transactionLogDao.queryByTransactionId(mqTransactionModel.getTransactionId());
                if (transactionLogs.size() < 1) {
                    throw new RuntimeException("分布式事务回滚时，主表缺失分布式事务ID,分布式事务ID为" + mqTransactionModel.getTransactionId());
                }

                this.rollBackService.rollBackSql(mqTransactionModel.getTransactionId(), ((TRANSACTION_LOG)transactionLogs.get(0)).getTRANSACTION_ROLLBACK_FLAG(), wetherOutTimeJob);
            }

            return consumeConcurrentlyStatus;
        } catch (Exception e) {
            logger.error("分布式事务消费失败" + e.getMessage(), e);
            consumeConcurrentlyStatus = ConsumeConcurrentlyStatus.RECONSUME_LATER;
            return consumeConcurrentlyStatus;
        } finally {
            if (lock != null && !unlocked) {
                lock.unlock();
            }

        }
    }
}


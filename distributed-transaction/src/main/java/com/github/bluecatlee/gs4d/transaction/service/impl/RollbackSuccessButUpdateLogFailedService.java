package com.github.bluecatlee.gs4d.transaction.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.common.message.MessageExt;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Resource;

import com.github.bluecatlee.gs4d.common.utils.lock.RedisLock;
import com.github.bluecatlee.gs4d.transaction.api.model.MqTransactionModel;
import com.github.bluecatlee.gs4d.transaction.dao.TransactionLogDao;
import com.github.bluecatlee.gs4d.transaction.dao.TransactionSqlLogDao;
import com.github.bluecatlee.gs4d.transaction.model.TRANSACTION_LOG;
import com.github.bluecatlee.gs4d.transaction.service.RollBackService;
import com.github.bluecatlee.gs4d.transaction.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

@Service("rollbackSuccessButUpdateLogFailedService")
public class RollbackSuccessButUpdateLogFailedService {
    
    protected static Logger logger = LoggerFactory.getLogger(RollbackSuccessButUpdateLogFailedService.class);
    
    @Autowired
    private TransactionLogDao transactionLogDao;

    @Autowired
    private TransactionSqlLogDao transactionSqlLogDao;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Value("#{settings['emailAddress']}")
    public String emailAddress;
    @Value("#{settings['emailDelayTime']}")
    private Integer emailDelayTime;

    @Resource(name = "transactionManager")
    private PlatformTransactionManager transactionManager;

    @Resource
    private RollBackService rollBackService;

    public void rollbackSuccessButUpdateLogFailed() {
        List<TRANSACTION_LOG> transactionLogList = null;
        RedisLock redisLock = null;
        Boolean unlocked = false;

        try {
            redisLock = new RedisLock(this.stringRedisTemplate, Constants.TransactionRollBacSuccButUpdateLogErrLockKey, 500);
            if (!redisLock.lock()) {
                unlocked = true;
                logger.warn("分布式事务定时器超时锁超时，其他机器正在处理数据。");
                return;
            }

            logger.debug("开始定时处理超时事务任务");
            transactionLogList = this.transactionLogDao.getErrorTranstionLogToSendEmail();        // 查询已超时的事务日志
            if (transactionLogList.size() > 0) {
                Iterator transactionLogListIterator = transactionLogList.iterator();

                while(transactionLogListIterator.hasNext()) {
                    TRANSACTION_LOG transactionLog = (TRANSACTION_LOG)transactionLogListIterator.next();
                    Integer count = this.transactionSqlLogDao.queryUnCompletedCountByTransactionId(transactionLog.getTRANSACTION_ID());
                    if (count > 0) {
                        logger.info("发现分布式事务id为" + transactionLog.getTRANSACTION_ID() + "的数据存在业务回滚,但是日志更新失败,开始补偿");
                        MessageListenerConcurrentlyImpl messageListenerConcurrentlyImpl = new MessageListenerConcurrentlyImpl(this.stringRedisTemplate, this.transactionManager, this.transactionSqlLogDao, this.rollBackService, this.transactionLogDao);
                        List<MessageExt> messageExtList = new ArrayList();
                        MessageExt messageExt = new MessageExt();
                        MqTransactionModel mqTransactionModel = new MqTransactionModel();
                        mqTransactionModel.setTransactionId(transactionLog.getTRANSACTION_ID());
                        mqTransactionModel.setWetherOutTimeJob(true);
                        mqTransactionModel.setSystemNowTime(transactionLog.getSysTime());
                        messageExt.setBody(JSONObject.toJSONString(mqTransactionModel).getBytes());
                        messageExtList.add(messageExt);
                        messageListenerConcurrentlyImpl.consumeMessage(messageExtList, (ConsumeConcurrentlyContext)null);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("邮件发送失败！" + e.getMessage(), e);
        } finally {
            if (redisLock != null && !unlocked) {
                redisLock.unlock();
            }

        }

    }
}


package com.github.bluecatlee.gs4d.transaction.service.impl;

import com.alibaba.fastjson.JSONArray;
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

@Service("transactionOvertimeJobService")
public class TransactionOvertimeJobService {
    
    protected static Logger logger = LoggerFactory.getLogger(TransactionOvertimeJobService.class);
    
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

    public void transactionOvertime() {
        List timeoutTransactionLog = null;
        RedisLock lock = null;
        Boolean var3 = false;

        try {
            lock = new RedisLock(this.stringRedisTemplate, Constants.TransactionOvertimeJobLockKey, 500);
            if (lock.lock()) {
                logger.debug("开始定时处理超时事务任务");
                timeoutTransactionLog = this.transactionLogDao.getErrorTranstionLogToSendEmail();
                JSONArray var4 = new JSONArray();
                if (timeoutTransactionLog.size() > 0) {
                    Iterator iterator = timeoutTransactionLog.iterator();

                    while(iterator.hasNext()) {
                        TRANSACTION_LOG transactionLog = (TRANSACTION_LOG)iterator.next();
                        Integer count = this.transactionSqlLogDao.queryCountByTransactionId(transactionLog.getTRANSACTION_ID());
                        if (count == 0) {
                            logger.info("发现分布式事务id为" + transactionLog.getTRANSACTION_ID() + "的数据存在超时,开始补偿");
                            MessageListenerConcurrentlyImpl var8 = new MessageListenerConcurrentlyImpl(this.stringRedisTemplate, this.transactionManager, this.transactionSqlLogDao, this.rollBackService, this.transactionLogDao);
                            List<MessageExt> messageExtList = new ArrayList();
                            MessageExt messageExt = new MessageExt();
                            MqTransactionModel mqTransactionModel = new MqTransactionModel();
                            mqTransactionModel.setTransactionId(transactionLog.getTRANSACTION_ID());
                            mqTransactionModel.setWetherOutTimeJob(true);
                            mqTransactionModel.setSystemNowTime(transactionLog.getSysTime());
                            messageExt.setBody(JSONObject.toJSONString(mqTransactionModel).getBytes());
                            messageExtList.add(messageExt);
                            var8.consumeMessage(messageExtList, (ConsumeConcurrentlyContext)null);
                        } else {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("分布式事务ID", transactionLog.getTRANSACTION_ID());
                            jsonObject.put("IP地址", transactionLog.getIP_ADDRESS());
                            jsonObject.put("项目名称", transactionLog.getFROM_SYSTEM());
                            jsonObject.put("方法名称", transactionLog.getMETHOD_NAME());
                            var4.add(jsonObject);
                            this.transactionLogDao.updateByTransactionId(Constants.error_end, "Y", transactionLog.getTRANSACTION_ID());
                        }
                    }
                }

                return;
            }

            var3 = true;
            logger.warn("分布式事务定时器超时锁超时，其他机器正在处理数据。");
        } catch (Exception e) {
            logger.error("邮件发送失败！" + e.getMessage(), e);
            return;
        } finally {
            if (lock != null && !var3) {
                lock.unlock();
            }

        }

    }
}


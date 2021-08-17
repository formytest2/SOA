package com.github.bluecatlee.gs4d.transaction.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.github.bluecatlee.gs4d.transaction.api.model.MqTransactionModel;
import com.github.bluecatlee.gs4d.transaction.dao.TransactionLogDao;
import com.github.bluecatlee.gs4d.transaction.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class BI implements MessageListenerConcurrently {
    
    protected static Logger logger = LoggerFactory.getLogger(BI.class);

    private TransactionLogDao transactionLogDao;

    public BI(TransactionLogDao transactionLogDao) {
        this.transactionLogDao = transactionLogDao;
    }

    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> messageExtList, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
        ConsumeConcurrentlyStatus consumeConcurrentlyStatus = ConsumeConcurrentlyStatus.CONSUME_SUCCESS;

        try {
            Iterator iterator = messageExtList.iterator();

            while(iterator.hasNext()) {
                MessageExt messageExt = (MessageExt)iterator.next();
                logger.info("当前消息事务成功,ID为" + messageExt.getMsgId() + "消息Key为" + messageExt.getKeys() + "body为" + new String(messageExt.getBody()));
                String body = new String(messageExt.getBody());
                MqTransactionModel mqTransactionModel = (MqTransactionModel)JSONObject.parseObject(body, MqTransactionModel.class);
                Long transactionId = mqTransactionModel.getTransactionId();
                Date transactionCommmitDate = mqTransactionModel.getTransactionCommmitDate();
                this.transactionLogDao.updateByTransactionId(Constants.an, "Y", transactionId);
            }
        } catch (Exception e) {
            consumeConcurrentlyStatus = ConsumeConcurrentlyStatus.RECONSUME_LATER;
            logger.error("事务回滚更新日志表消费消息失败，rediskey为" + new String(((MessageExt)messageExtList.get(0)).getBody()) + "原因" + e.getMessage(), e);
        }

        return consumeConcurrentlyStatus;
    }
}

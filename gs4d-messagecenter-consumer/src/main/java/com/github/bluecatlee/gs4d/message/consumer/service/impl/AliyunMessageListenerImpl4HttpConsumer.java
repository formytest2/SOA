package com.github.bluecatlee.gs4d.message.consumer.service.impl;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import com.github.bluecatlee.gs4d.message.api.service.MessageCenterSendService;
import com.github.bluecatlee.gs4d.message.consumer.dao.SysRocketMqConsumeFailedlogDao;
import com.github.bluecatlee.gs4d.message.consumer.dao.SysRocketMqConsumeLogDao;
import com.github.bluecatlee.gs4d.message.consumer.dao.SysRocketMqSendLogDao;
import com.github.bluecatlee.gs4d.message.consumer.dao.SysRocketMqSendLogHistoryDao;
import com.github.bluecatlee.gs4d.message.consumer.model.PlatformMqHttpConsumer;
import com.github.bluecatlee.gs4d.message.consumer.model.PlatformMqTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.PlatformTransactionManager;

public class AliyunMessageListenerImpl4HttpConsumer extends AliyunMessageListenerSupport4HttpConsumer implements MessageListener {

    private static Logger logger = LoggerFactory.getLogger(MessageListenerConcurrentlyImpl4HttpConsumer.class);

    public AliyunMessageListenerImpl4HttpConsumer(PlatformMqTopic platformMqTopic, PlatformMqHttpConsumer platformMqHttpConsumer, MessageCenterSendService messageCenterSendService, SysRocketMqConsumeLogDao sysRocketMqConsumeLogDao, Integer dataSign, SysRocketMqSendLogDao sysRocketMqSendLogDao, Integer retries, SysRocketMqConsumeFailedlogDao sysRocketMqConsumeFailedlogDao, String consumerFiledToopic, String consumerFiledTag, SysRocketMqSendLogHistoryDao sysRocketMqSendLogHistoryDao, PlatformTransactionManager platformTransactionManager, Integer zkDataSign) {
        this.platformMqTopic = platformMqTopic;
        this.platformMqHttpConsumer = platformMqHttpConsumer;
        this.sysRocketMqConsumeLogDao = sysRocketMqConsumeLogDao;
        this.dataSign = dataSign;
        this.sysRocketMqSendLogDao = sysRocketMqSendLogDao;
        this.retries = retries;
        this.sysRocketMqConsumeFailedlogDao = sysRocketMqConsumeFailedlogDao;
        this.consumerFiledToopic = consumerFiledToopic;
        this.consumerFiledTag = consumerFiledTag;
        this.sysRocketMqSendLogHistoryDao = sysRocketMqSendLogHistoryDao;
        this.platformTransactionManager = platformTransactionManager;
        this.zkDataSign = zkDataSign;
    }

    public Action consume(Message message, ConsumeContext consumeContext) {
        Action action = Action.CommitMessage;
        Integer result = 0;

        try {
            result = this.doConsume(message);
            if (result != 0) {
                action = Action.ReconsumeLater;
            }
        } catch (Exception e) {
            result = -1;
            action = Action.ReconsumeLater;
            logger.error(e.getMessage(), e);
        }

        return action;
    }
}

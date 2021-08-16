package com.github.bluecatlee.gs4d.message.consumer.service.impl;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.order.ConsumeOrderContext;
import com.aliyun.openservices.ons.api.order.MessageOrderListener;
import com.aliyun.openservices.ons.api.order.OrderAction;
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

public class AliyunMessageOrderListenerImpl4HttpConsumer extends AliyunMessageListenerSupport4HttpConsumer implements MessageOrderListener {

    private static Logger logger = LoggerFactory.getLogger(MessageListenerConcurrentlyImpl4HttpConsumer.class);

    public AliyunMessageOrderListenerImpl4HttpConsumer(PlatformMqTopic platformMqTopic, PlatformMqHttpConsumer platformMqHttpConsumer, MessageCenterSendService messageCenterSendService, SysRocketMqConsumeLogDao sysRocketMqConsumeLogDao, Integer dataSign, SysRocketMqSendLogDao sysRocketMqSendLogDao, Integer retries, SysRocketMqConsumeFailedlogDao sysRocketMqConsumeFailedlogDao, String consumerFiledToopic, String consumerFiledTag, SysRocketMqSendLogHistoryDao sysRocketMqSendLogHistoryDao, PlatformTransactionManager platformTransactionManager, Integer zkDataSign) {
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

    public OrderAction consume(Message message, ConsumeOrderContext consumeOrderContext) {
        OrderAction orderAction = OrderAction.Success;
        Integer result = 0;

        try {
            result = this.doConsume(message);
            if (result != 0) {
                orderAction = OrderAction.Suspend;
            }
        } catch (Exception e) {
            result = -1;
            orderAction = OrderAction.Suspend;
            logger.error(e.getMessage(), e);
        }

        return orderAction;
    }
}

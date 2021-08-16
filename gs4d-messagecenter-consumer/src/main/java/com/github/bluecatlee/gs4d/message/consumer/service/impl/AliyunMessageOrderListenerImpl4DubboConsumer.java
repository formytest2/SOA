package com.github.bluecatlee.gs4d.message.consumer.service.impl;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.order.ConsumeOrderContext;
import com.aliyun.openservices.ons.api.order.MessageOrderListener;
import com.aliyun.openservices.ons.api.order.OrderAction;
import com.github.bluecatlee.gs4d.message.consumer.model.PlatformMqDubboConsumer;
import com.github.bluecatlee.gs4d.message.consumer.model.PlatformMqTopic;
import com.github.bluecatlee.gs4d.message.consumer.service.UpdateConsumerResultService;

public class AliyunMessageOrderListenerImpl4DubboConsumer extends AliyunMessageListenerSupport4DubboConsumer implements MessageOrderListener {

    public AliyunMessageOrderListenerImpl4DubboConsumer(PlatformMqTopic platformMqTopic, PlatformMqDubboConsumer platformMqDubboConsumer, Integer dataSign, Integer retries, Integer zkDataSign, UpdateConsumerResultService updateConsumerResultService) {
        this.platformMqTopic = platformMqTopic;
        this.platformMqDubboConsumer = platformMqDubboConsumer;
        this.dataSign = dataSign;
        this.retries = retries;
        this.zkDataSign = zkDataSign;
        this.updateConsumerResultService = updateConsumerResultService;
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

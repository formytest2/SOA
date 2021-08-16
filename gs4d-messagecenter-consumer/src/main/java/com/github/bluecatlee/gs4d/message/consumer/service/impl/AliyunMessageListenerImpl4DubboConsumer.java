package com.github.bluecatlee.gs4d.message.consumer.service.impl;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import com.github.bluecatlee.gs4d.message.consumer.model.PlatformMqDubboConsumer;
import com.github.bluecatlee.gs4d.message.consumer.model.PlatformMqTopic;
import com.github.bluecatlee.gs4d.message.consumer.service.UpdateConsumerResultService;

public class AliyunMessageListenerImpl4DubboConsumer extends AliyunMessageListenerSupport4DubboConsumer implements MessageListener {

    public AliyunMessageListenerImpl4DubboConsumer(PlatformMqTopic platformMqTopic, PlatformMqDubboConsumer platformMqDubboConsumer, Integer dataSign, Integer retries, Integer zkDataSign, UpdateConsumerResultService updateConsumerResultService) {
        this.platformMqTopic = platformMqTopic;
        this.platformMqDubboConsumer = platformMqDubboConsumer;
        this.dataSign = dataSign;
        this.retries = retries;
        this.zkDataSign = zkDataSign;
        this.updateConsumerResultService = updateConsumerResultService;
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

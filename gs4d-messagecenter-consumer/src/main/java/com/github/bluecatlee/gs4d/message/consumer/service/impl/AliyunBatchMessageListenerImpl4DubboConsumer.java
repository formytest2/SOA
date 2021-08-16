package com.github.bluecatlee.gs4d.message.consumer.service.impl;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.batch.BatchMessageListener;
import com.github.bluecatlee.gs4d.message.consumer.model.PlatformMqDubboConsumer;
import com.github.bluecatlee.gs4d.message.consumer.model.PlatformMqTopic;
import com.github.bluecatlee.gs4d.message.consumer.service.UpdateConsumerResultService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ExecutorService;

public class AliyunBatchMessageListenerImpl4DubboConsumer extends AliyunBatchMessageListenerSupport4DubboConsumer implements BatchMessageListener {
    
    private static Logger logger = LoggerFactory.getLogger(MessageListenerConcurrentlyImpl4DubboConsumer.class);
    
    public static ExecutorService executorService;

    public AliyunBatchMessageListenerImpl4DubboConsumer(PlatformMqTopic platformMqTopic, PlatformMqDubboConsumer platformMqDubboConsumer, Integer dataSign, Integer retries, Integer zkDataSign, UpdateConsumerResultService updateConsumerResultService) {
        this.platformMqTopic = platformMqTopic;
        this.platformMqDubboConsumer = platformMqDubboConsumer;
        this.dataSign = dataSign;
        this.retries = retries;
        this.zkDataSign = zkDataSign;
        this.updateConsumerResultService = updateConsumerResultService;
    }

    public Action consume(List<Message> messages, ConsumeContext consumeContext) {
        Action action = Action.CommitMessage;
        Integer result = 0;

        try {
            result = this.doConsume(messages);
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

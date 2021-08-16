package com.github.bluecatlee.gs4d.message.consumer.service;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.github.bluecatlee.gs4d.message.consumer.model.PlatformMqTopic;

import java.util.List;

public interface MqConsumerService {

    void initalAllConsumer();

    DefaultMQPushConsumer initalMqPushConsumerCore(PlatformMqTopic platformMqTopic, boolean flag) throws Exception;

    void initalConsumer(PlatformMqTopic platformMqTopic, List<DefaultMQPushConsumer> mqPushConsumerList) throws Exception;

}


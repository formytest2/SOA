package com.github.bluecatlee.gs4d.message.consumer.service;

import com.alibaba.rocketmq.client.exception.MQClientException;

public interface MessageSqlListenerService {

    void initMqSqlConsumer() throws MQClientException;

}


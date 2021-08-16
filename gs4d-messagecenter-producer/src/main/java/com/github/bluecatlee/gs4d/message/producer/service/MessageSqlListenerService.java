package com.github.bluecatlee.gs4d.message.producer.service;

import com.alibaba.rocketmq.client.exception.MQClientException;

public interface MessageSqlListenerService {

    void initMqSqlConsumer() throws MQClientException;

}


package com.github.bluecatlee.gs4d.message.consumer.service;

import com.github.bluecatlee.gs4d.message.consumer.model.PlatformMqDubboConsumer;

public interface MqDubboConsumerService {

    void initalAllDubboMethod();

    void initalSpecialDubboMethod(Long series);

    void initalDubboMethodCore(PlatformMqDubboConsumer platformMqDubboConsumer) throws Exception;

}


package com.github.bluecatlee.gs4d.message.producer.service;

import com.github.bluecatlee.gs4d.message.producer.model.PLATFORM_MQ_TOPIC;

public interface MqProductorBizInitService {

    void initalAllProductorBiz();

    void initalProductorBiz(PLATFORM_MQ_TOPIC platformMqTopic) throws Exception;

}


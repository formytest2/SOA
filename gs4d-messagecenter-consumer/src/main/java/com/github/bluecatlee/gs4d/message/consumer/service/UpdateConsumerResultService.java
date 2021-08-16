package com.github.bluecatlee.gs4d.message.consumer.service;

public interface UpdateConsumerResultService {

    Integer updateConsumerSuccessResult(Long series, String taskTarget, String messagePack, Long consumeTime, Integer stepId);

    Integer updateConsumerFailedResult(Long series, Integer retries, String taskTarget, String messagePack, String retryInterval, Integer retryTimes, String tag, Long consumeTime, Long tenantNumId, Long dataSign, Integer stepId);

}


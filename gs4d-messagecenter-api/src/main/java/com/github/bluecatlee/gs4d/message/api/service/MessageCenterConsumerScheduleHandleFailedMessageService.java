package com.github.bluecatlee.gs4d.message.api.service;

import com.github.bluecatlee.gs4d.message.api.request.ConsumerFailedMessageRetryRequest;
import com.github.bluecatlee.gs4d.message.api.request.ConsummerFailedMessageDeleteRequest;
import com.github.bluecatlee.gs4d.message.api.request.ConsummerFailedMessageHandleRequest;
import com.github.bluecatlee.gs4d.message.api.request.FailedMessageToSuccessDbRequest;
import com.github.bluecatlee.gs4d.message.api.response.ConsumerFailedMessageRetryResponse;
import com.github.bluecatlee.gs4d.message.api.response.ConsummerFailedMessageDeleteResponse;
import com.github.bluecatlee.gs4d.message.api.response.ConsummerFailedMessageHandleResponse;
import com.github.bluecatlee.gs4d.message.api.response.FailedMessageToSuccessDbInsertResponse;

public interface MessageCenterConsumerScheduleHandleFailedMessageService {

    ConsummerFailedMessageHandleResponse handleConsummerFailedMessage(ConsummerFailedMessageHandleRequest request);

    ConsumerFailedMessageRetryResponse retryConsumerFailedMessage(ConsumerFailedMessageRetryRequest request);

    FailedMessageToSuccessDbInsertResponse insertFailedMessageToSuccessDb(FailedMessageToSuccessDbRequest request);

    ConsummerFailedMessageDeleteResponse deleteConsummerFailedMessage(ConsummerFailedMessageDeleteRequest request);

    void sendMonitorLog(String topic, String tag, Long tenantNumId, Long dataSign, Long typeId, String responseDetail, String responseBody);
}


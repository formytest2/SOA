package com.github.bluecatlee.gs4d.message.api.service;

import com.github.bluecatlee.gs4d.message.api.request.*;
import com.github.bluecatlee.gs4d.message.api.response.*;

public interface MessageCenterProductorScheduleHandleFailedMessageService {

    NoDealTransMessageFindResponse findNoDealTransMessage(NoDealTransMessageFindRequest request);

    SendFailedMessageFindResponse findSendFailedMessage(SendFailedMessageFindRequest request);

    void sendMonitorLog(Long tenantNumId, Long dataSign, Long var, String detailMessage, String message);

    FailedMessageToSuccessDbInsertResponse insertFailedMessageToSuccessDb(FailedMessageToSuccessDbRequest request);

    UnConsumerMessageRetrytResponse retryUnConsumerMessage(UnConsumerMessageRetrytRequest request);

    TransationFailedLogFindResponse findTransationFailedLog(TransationFailedLogFindRequest request);

    NoDealTransFlowerMessageFindResponse findNoDealTransFlowerMessage(NoDealTransFlowerMessageFindRequest request);

    FlowerMessageDeleteResponse deleteFlowerMessage(FlowerMessageDeleteRequest request);

    RedisDelayMessageListenCheckResponse checkRedisDelayMessageListen(RedisDelayMessageListenCheckRequest request);

}


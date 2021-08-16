package com.github.bluecatlee.gs4d.message.api.service;

import com.github.bluecatlee.gs4d.message.api.request.*;
import com.github.bluecatlee.gs4d.message.api.response.*;

public interface MessageCenterSendService {

    PrepSimpleMessageResponse sendPrepSimpleMessage(PrepSimpleMessageRequest request);

    SimpleMessageRightNowSendResponse sendSimpleMessageRightNow(SimpleMessageRightNowSendRequest request);

    MsgConfirmSendResponse confirmSendMsg(MsgConfirmSendRequest request);

    MsgCancelSendResponse cancelSendMsg(MsgCancelSendRequest request);

    MsgRetrySendResponse retrySendMsg(MsgRetrySendRequest request);

    TopicCreateResponse createTopic(TopicCreateRequest request);

    SendMessRightNowResponse sendMessRightNow(SendMessRightNowRequest request);

    PrepMessSendResponse sendPrepMess(SendMessRightNowRequest request);

    PrepOTMSimpleMessageSendResponse sendPrepOTMSimpleMessage(PrepOTMSimpleMessageSendRequest request);

    OTMSimpleMessageRightNowSendResponse sendOTMSimpleMessageRightNow(OTMSimpleMessageRightNowSendRequest request);

    TransactionMessageConfirmResponse confirmTransactionMessage(TransactionMessageConfirmRequest request);

    TransactionMessageCancelResponse cancelTransactionMessage(TransactionMessageCancelRequest request);

    PrepOrderSimpleMessageResponse sendPrepOrderSimpleMessage(PrepOrderSimpleMessageRequest request);

    OrderSendMessageConfirmResponse confirmOrderSendMessage(OrderSendMessageConfirmRequest request);

    OrderSendMessageCancelResponse cancelOrderSendMessage(OrderSendMessageCancelRequest request);

    OrderMessageRightNowResponse sendOrderMessageRightNow(OrderMessageRightNowRequest request);

    JobCronMessageRightNowSendResponse sendJobCronMessageRightNow(JobCronMessageRightNowSendRequest request);

    JobCronMessageCancelResponse cancelJobCronMessage(JobCronMessageCancelRequest request);

    TransMessageRetryResponse retryTransMessage(TransMessageRetryRequest request);
}


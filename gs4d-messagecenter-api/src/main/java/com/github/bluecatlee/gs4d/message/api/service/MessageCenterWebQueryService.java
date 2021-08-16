package com.github.bluecatlee.gs4d.message.api.service;

import com.github.bluecatlee.gs4d.message.api.request.*;
import com.github.bluecatlee.gs4d.message.api.response.*;

public interface MessageCenterWebQueryService {

    SystemNameFindResponse findSystemName(SystemNameFindRequest request);

    MessageDetailResponse findMessageDetail(MessageDetailRequest request);

    MessageDetailResponse findHistoryMessageDetail(MessageDetailRequest request);

    TopicConfigResponse getTopicConfig(TopicConfigRequest request);

    AllTopicMessageResponse findAllTopicMessage(AllTopicMessageRequest request);

    ConsumerSeriesUpdateResponse updateConsumerSeries(ConsumerSeriesUpdateRequest request);

    HttpConsumerMessageGetResponse getHttpConsumerMessage(HttpConsumerMessageGetRequest request);

    DubboConsumerMessageGetResponse getDubboConsumerMessage(DubboConsumerMessageGetRequest request);

    PlatformMqTopicMessageUpdateResponse updatePlatformMqTopicMessage(PlatformMqTopicMessageUpdateRequest request);

    MesssageRetrySendResponse retrySendMessage(MesssageRetrySendRequest request);

    TopicBySystemNameFindResponse findTopicBySystemName(TopicBySystemNameFindRequest request);

    TagBySystemNameAndTopicFindResponse findTagBySystemNameAndTopic(TagBySystemNameAndTopicFindRequest request);

    HttpConfigByKeyResponse findHttpConfigByKey(HttpConfigByKeyRequest request);

    DubboConfigByKeyResponse findDubboConfigByKey(DubboConfigByKeyRequest request);

    TopicConfigDeleteResponse deleteTopicConfig(TopicConfigDeleteRequest request);

    TopicConfigAddResponse addTopicConfig(TopicConfigAddRequest request);

    AllSystemNameFindResponse findAllSystemName(AllSystemNameFindRequest request);

    AllFatherTopicFindResponse findAllFatherTopic(AllFatherTopicFindRequest request);

    RelationTopicFindResponse findRelationTopic(RelationTopicFindRequest request);

    RelationTopicDeleteResponse deleteRelationTopic(RelationTopicDeleteRequest request);

    RelationTopicAddResponse addRelationTopic(RelationTopicAddRequest request);

    FatherTopicAddResponse addFatherTopic(FatherTopicAddRequest request);

    FatherTopicDeleteResponse deleteFatherTopic(FatherTopicDeleteRequest request);

    AllSystemNameAndIdFindResponse findAllSystemNameAndId(AllSystemNameAndIdFindRequest request);

    TopicConfigBySystemIdFindResponse findTopicConfigBySystemId(TopicConfigBySystemIdFindRequest request);

    TopicConfigBySystemIdFindAndTopicResponse findTopicConfigBySystemIdAndTopic(TopicConfigBySystemIdAndTopicFindRequest request);

    RetryIntervalResponse getRetryInterval(RetryIntervalRequest request);

    MessageByBodySendResponse sendMessageByBody(MessageByBodySendRequest request);

    MessageDetailResponse findUnconsumeMessageDetail(MessageDetailRequest request);

    MessageRetrySendByTopicAndTagResponse retrySendMessageByTopicAndTag(MessageRetrySendByTopicAndTagRequest request);

    MqNameSrvGetRsponse getMqNameSrv(MqNameSrvGetRequest request);

    ConsumerIpsGetRsponse getConsumerIps(ConsumerIpsGetRequest request);

    HttpConsumerAddRsponse addHttpConsumer(HttpConsumerAddRequest request);

    DubboConsumerAddRsponse addDubboConsumer(DubboConsumerAddRequest request);

    DubboConsumerDeleteRsponse deleteDubboConsumer(DubboConsumerDeleteRequest request);

    HttpConsumerDeleteRsponse deleteHttpConsumer(HttpConsumerDeleteRequest request);

    HttpConsumerUpdateRsponse updateHttpConsumer(HttpConsumerUpdateRequest request);

    DubboConsumerUpdateRsponse updateDubboConsumer(DubboConsumerUpdateRequest request);

    HttpConsumerGetBySeriesResponse getHttpConsumerBySeries(HttpConsumerGetBySeriesRequest request);

    DubboConsumerGetBySeriesResponse getDubboConsumerBySeries(DubboConsumerGetBySeriesRequest request);

    ExArcDocSystemAddRsponse addExArcDocSystem(ExArcDocSystemAddRequest request);

    FatherTopicFindResponse findFatherTopicByTopic(FatherTopicFindRequest request);

    TopicByTopicTypeFindResponse findTopicByTopicType(TopicByTopicTypeFindRequest request);

    BeatHeartResponse heartBeat(BeatHeartRequest request);

    RemarkByTagGetResponse getRemarkByTag(RemarkByTagGetRequest request);

    TopicTagByTypeGetResponse getTopicTagByType(TopicTagByTypeGetRequest request);

    SimpleConfigByTopicAndTagGetResponse getSimpleConfigByTopicAndTag(SimpleConfigByTopicAndTagGetRequest request);

    MessageOnlineFlagUpdateResponse updateMessageOnlineFlag(MessageOnlineFlagUpdateRequest request);

    MessageDetailResponse findTransFailedMessageDetail(MessageDetailRequest request);

    TransFailedMessageRetryResponse retryTransFailedMessage(TransFailedMessageRetryRequest request);

    MessageDiffGetResponse getMessageDiff(MessageDiffGetRequest request);

    TopicRegistResponse registTopic(TopicRegistRequest request);

    MessageCountGetResponse getMessageCount(MessageCountGetRequest request);

}


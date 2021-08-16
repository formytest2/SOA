package com.github.bluecatlee.gs4d.message.api.service;

import com.github.bluecatlee.gs4d.message.api.request.JobListCronMessageRightNowSendRequest;
import com.github.bluecatlee.gs4d.message.api.request.JobMessageListRightNowSendRequest;
import com.github.bluecatlee.gs4d.message.api.request.PrepSimpleMessageListSendRequest;
import com.github.bluecatlee.gs4d.message.api.request.SimpleMessageListRightNowSendRequest;
import com.github.bluecatlee.gs4d.message.api.response.JobListCronMessageRightNowSendResponse;
import com.github.bluecatlee.gs4d.message.api.response.JobMessageListRightNowSendResponse;
import com.github.bluecatlee.gs4d.message.api.response.PrepSimpleMessageListSendResponse;
import com.github.bluecatlee.gs4d.message.api.response.SimpleMessageListRightNowSendResponse;

public interface MessageCenterSendListService {

    PrepSimpleMessageListSendResponse sendPrepSimpleMessageList(PrepSimpleMessageListSendRequest request);

    SimpleMessageListRightNowSendResponse sendSimpleMessageListRightNow(SimpleMessageListRightNowSendRequest request);

    JobMessageListRightNowSendResponse sendJobMessageListRightNow(JobMessageListRightNowSendRequest request);

    JobListCronMessageRightNowSendResponse sendJobListCronMessageRightNow(JobListCronMessageRightNowSendRequest request);

}


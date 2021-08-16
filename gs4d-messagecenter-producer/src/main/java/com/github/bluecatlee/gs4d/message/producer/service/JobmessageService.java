package com.github.bluecatlee.gs4d.message.producer.service;

import com.github.bluecatlee.gs4d.message.api.request.JobMessageByBodySendRequest;
import com.github.bluecatlee.gs4d.message.api.response.JobMessageByBodySendResponse;

public interface JobmessageService {

    JobMessageByBodySendResponse sendJobMessageByBody(JobMessageByBodySendRequest request);

    void initJobMessageToSpringJob();

}


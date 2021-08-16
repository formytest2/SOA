package com.github.bluecatlee.gs4d.message.api.request;

import com.github.bluecatlee.gs4d.common.bean.AbstractRequest;
import com.github.bluecatlee.gs4d.message.api.model.JobMessageModel;

public class JobCronMessageRightNowSendRequest extends AbstractRequest {
    private static final long serialVersionUID = -2194256027444828193L;
    private JobMessageModel jobMessageModel;

    public JobCronMessageRightNowSendRequest() {
    }

    public JobMessageModel getJobMessageModel() {
        return this.jobMessageModel;
    }

    public void setJobMessageModel(JobMessageModel jobMessageModel) {
        this.jobMessageModel = jobMessageModel;
    }
}


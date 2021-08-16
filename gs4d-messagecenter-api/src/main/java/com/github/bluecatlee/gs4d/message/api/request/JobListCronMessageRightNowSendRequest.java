package com.github.bluecatlee.gs4d.message.api.request;

import com.github.bluecatlee.gs4d.common.bean.AbstractRequest;
import com.github.bluecatlee.gs4d.message.api.model.JobMessageModel;

import java.util.List;

public class JobListCronMessageRightNowSendRequest extends AbstractRequest {
    private static final long serialVersionUID = 822404599084478697L;
    private List<JobMessageModel> jobMessageModels;

    public List<JobMessageModel> getJobMessageModels() {
        return this.jobMessageModels;
    }

    public void setJobMessageModels(List<JobMessageModel> jobMessageModels) {
        this.jobMessageModels = jobMessageModels;
    }
}


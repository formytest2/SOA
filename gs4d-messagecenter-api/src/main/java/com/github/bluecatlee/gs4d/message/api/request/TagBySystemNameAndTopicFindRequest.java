package com.github.bluecatlee.gs4d.message.api.request;

import com.github.bluecatlee.gs4d.common.bean.AbstractRequest;

import javax.validation.constraints.NotEmpty;

public class TagBySystemNameAndTopicFindRequest extends AbstractRequest {
    private static final long serialVersionUID = -3016139037493544642L;
    @NotEmpty(message = "topic不能为空！")
    private String topic;

    public String getTopic() {
        return this.topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}


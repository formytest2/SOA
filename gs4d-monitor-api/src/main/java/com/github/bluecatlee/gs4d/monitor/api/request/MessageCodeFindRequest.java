package com.github.bluecatlee.gs4d.monitor.api.request;

import com.github.bluecatlee.gs4d.common.bean.AbstractRequest;

import javax.validation.constraints.NotNull;

public class MessageCodeFindRequest extends AbstractRequest {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "topicId不能为空")
    private Long topicId;

    public Long getTopicId() {
        return this.topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }
}


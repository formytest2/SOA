package com.github.bluecatlee.gs4d.monitor.api.request;

import com.github.bluecatlee.gs4d.common.bean.AbstractRequest;

import javax.validation.constraints.NotNull;

public class MessageCodeUserFindRequest extends AbstractRequest {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "errorCodeId不能为空")
    private String errorCodeId;

    private Long topicId;

    public String getErrorCodeId() {
        return this.errorCodeId;
    }

    public void setErrorCodeId(String errorCodeId) {
        this.errorCodeId = errorCodeId;
    }

    public Long getTopicId() {
        return this.topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }
}


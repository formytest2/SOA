package com.github.bluecatlee.gs4d.message.api.request;

import com.github.bluecatlee.gs4d.common.bean.AbstractRequest;

import javax.validation.constraints.NotEmpty;

public class RetryIntervalRequest extends AbstractRequest {
    private static final long serialVersionUID = -6916581140124593958L;
    @NotEmpty(message = "retryInterval不能为空！")
    private String retryInterval;

    public String getRetryInterval() {
        return this.retryInterval;
    }

    public void setRetryInterval(String retryInterval) {
        this.retryInterval = retryInterval;
    }
}

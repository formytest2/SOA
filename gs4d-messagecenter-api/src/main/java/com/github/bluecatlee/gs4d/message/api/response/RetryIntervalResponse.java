package com.github.bluecatlee.gs4d.message.api.response;

import com.github.bluecatlee.gs4d.common.bean.MessagePack;

public class RetryIntervalResponse extends MessagePack {
    private static final long serialVersionUID = -8874092507833258313L;
    private String retryInterval;

    public String getRetryInterval() {
        return this.retryInterval;
    }

    public void setRetryInterval(String retryInterval) {
        this.retryInterval = retryInterval;
    }
}


package com.github.bluecatlee.gs4d.message.api.request;

import com.github.bluecatlee.gs4d.common.bean.AbstractRequest;

public class MessageOnlineFlagUpdateRequest extends AbstractRequest {
    private static final long serialVersionUID = 5633199416803235266L;
    private Long series;
    private String onlineFlag;

    public Long getSeries() {
        return this.series;
    }

    public void setSeries(Long series) {
        this.series = series;
    }

    public String getOnlineFlag() {
        return this.onlineFlag;
    }

    public void setOnlineFlag(String onlineFlag) {
        this.onlineFlag = onlineFlag;
    }
}


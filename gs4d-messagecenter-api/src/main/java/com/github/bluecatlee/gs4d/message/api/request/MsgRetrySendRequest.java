package com.github.bluecatlee.gs4d.message.api.request;

import com.github.bluecatlee.gs4d.common.bean.AbstractRequest;

public class MsgRetrySendRequest extends AbstractRequest {
    private static final long serialVersionUID = 734370097229017750L;
    private Long series;
    private Integer sendType = 2;

    public MsgRetrySendRequest() {
    }

    public Long getSeries() {
        return this.series;
    }

    public void setSeries(Long series) {
        this.series = series;
    }

    public Integer getSendType() {
        return this.sendType;
    }

    public void setSendType(Integer sendType) {
        this.sendType = sendType;
    }
}


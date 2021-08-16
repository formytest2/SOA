package com.github.bluecatlee.gs4d.message.api.response;

import com.github.bluecatlee.gs4d.common.bean.MessagePack;

import java.util.List;

public class JobMessageListRightNowSendResponse extends MessagePack {
    private static final long serialVersionUID = -3407408172386501776L;
    private List<Long> series;

    public JobMessageListRightNowSendResponse() {
    }

    public List<Long> getSeries() {
        return this.series;
    }

    public void setSeries(List<Long> series) {
        this.series = series;
    }
}


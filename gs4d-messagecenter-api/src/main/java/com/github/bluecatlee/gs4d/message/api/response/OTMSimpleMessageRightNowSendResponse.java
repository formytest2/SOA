package com.github.bluecatlee.gs4d.message.api.response;

import com.github.bluecatlee.gs4d.common.bean.MessagePack;

import java.util.List;

public class OTMSimpleMessageRightNowSendResponse extends MessagePack {
    private static final long serialVersionUID = 4241823204012154812L;
    private List<Long> series;

    public OTMSimpleMessageRightNowSendResponse() {
    }

    public List<Long> getSeries() {
        return this.series;
    }

    public void setSeries(List<Long> series) {
        this.series = series;
    }
}


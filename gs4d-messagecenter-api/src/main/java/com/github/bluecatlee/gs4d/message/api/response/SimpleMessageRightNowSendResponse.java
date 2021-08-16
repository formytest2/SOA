package com.github.bluecatlee.gs4d.message.api.response;

import com.github.bluecatlee.gs4d.common.bean.MessagePack;

public class SimpleMessageRightNowSendResponse extends MessagePack {
    private static final long serialVersionUID = -7417268227628725677L;
    private Long series;

    public SimpleMessageRightNowSendResponse() {
    }

    public Long getSeries() {
        return this.series;
    }

    public void setSeries(Long series) {
        this.series = series;
    }
}


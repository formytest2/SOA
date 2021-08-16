package com.github.bluecatlee.gs4d.message.api.response;

import com.github.bluecatlee.gs4d.common.bean.MessagePack;

public class JobCronMessageRightNowSendResponse extends MessagePack {
    private static final long serialVersionUID = -7614712528397901624L;
    private Long series;

    public JobCronMessageRightNowSendResponse() {
    }

    public Long getSeries() {
        return this.series;
    }

    public void setSeries(Long series) {
        this.series = series;
    }
}

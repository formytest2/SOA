package com.github.bluecatlee.gs4d.message.api.response;

import com.github.bluecatlee.gs4d.common.bean.MessagePack;

public class SendMessRightNowResponse extends MessagePack {

    private static final long serialVersionUID = 8647274031680098657L;
    private Long series;

    public Long getSeries() {
        return this.series;
    }

    public void setSeries(Long series) {
        this.series = series;
    }
}


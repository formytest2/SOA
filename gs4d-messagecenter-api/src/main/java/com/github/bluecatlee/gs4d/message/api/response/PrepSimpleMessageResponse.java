package com.github.bluecatlee.gs4d.message.api.response;

import com.github.bluecatlee.gs4d.common.bean.MessagePack;

public class PrepSimpleMessageResponse extends MessagePack {
    private static final long serialVersionUID = -1374597382923011758L;
    private Long series;

    public PrepSimpleMessageResponse() {
    }

    public Long getSeries() {
        return this.series;
    }

    public void setSeries(Long series) {
        this.series = series;
    }
}


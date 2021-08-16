package com.github.bluecatlee.gs4d.message.api.response;

import com.github.bluecatlee.gs4d.common.bean.MessagePack;

public class PrepMessSendResponse extends MessagePack {
    private static final long serialVersionUID = -2610202986440933139L;
    private Long series;

    public Long getSeries() {
        return this.series;
    }

    public void setSeries(Long series) {
        this.series = series;
    }
}


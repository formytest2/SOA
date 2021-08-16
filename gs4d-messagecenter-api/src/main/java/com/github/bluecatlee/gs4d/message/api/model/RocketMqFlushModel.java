package com.github.bluecatlee.gs4d.message.api.model;

import java.io.Serializable;

public class RocketMqFlushModel implements Serializable {
    private static final long serialVersionUID = -4578922609817265670L;
    private String flushFlag;
    private Long series;

    public String getFlushFlag() {
        return this.flushFlag;
    }

    public void setFlushFlag(String flushFlag) {
        this.flushFlag = flushFlag;
    }

    public Long getSeries() {
        return this.series;
    }

    public void setSeries(Long series) {
        this.series = series;
    }
}


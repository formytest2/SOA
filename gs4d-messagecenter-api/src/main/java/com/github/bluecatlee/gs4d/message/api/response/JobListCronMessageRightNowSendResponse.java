package com.github.bluecatlee.gs4d.message.api.response;

import com.github.bluecatlee.gs4d.common.bean.MessagePack;

import java.util.List;

public class JobListCronMessageRightNowSendResponse extends MessagePack {
    private static final long serialVersionUID = -7428611938499641786L;
    private List<Long> series;

    public List<Long> getSeries() {
        return this.series;
    }

    public void setSeries(List<Long> series) {
        this.series = series;
    }
}


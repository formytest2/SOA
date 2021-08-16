package com.github.bluecatlee.gs4d.message.api.response;

import com.github.bluecatlee.gs4d.common.bean.MessagePack;

import java.util.List;

public class MsgConfirmSendResponse extends MessagePack {
    private static final long serialVersionUID = -4995167843750551195L;
    private List<Long> series;

    public MsgConfirmSendResponse() {
    }

    public List<Long> getSeries() {
        return this.series;
    }

    public void setSeries(List<Long> series) {
        this.series = series;
    }
}

package com.github.bluecatlee.gs4d.message.api.request;

import com.github.bluecatlee.gs4d.common.bean.AbstractRequest;

import java.util.List;

public class MsgConfirmSendRequest extends AbstractRequest {
    private static final long serialVersionUID = -6473814955100549724L;
    private List<Long> series;

    public List<Long> getSeries() {
        return this.series;
    }

    public void setSeries(List<Long> series) {
        this.series = series;
    }
}

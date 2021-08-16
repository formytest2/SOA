package com.github.bluecatlee.gs4d.message.api.request;
import com.github.bluecatlee.gs4d.common.bean.AbstractRequest;

import java.util.List;

public class MsgCancelSendRequest extends AbstractRequest {
    private static final long serialVersionUID = 3382120229064951745L;
    private List<Long> series;

    public MsgCancelSendRequest() {
    }

    public List<Long> getSeries() {
        return this.series;
    }

    public void setSeries(List<Long> series) {
        this.series = series;
    }
}

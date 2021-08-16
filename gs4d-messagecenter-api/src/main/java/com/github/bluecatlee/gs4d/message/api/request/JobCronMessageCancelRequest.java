package com.github.bluecatlee.gs4d.message.api.request;

import com.github.bluecatlee.gs4d.common.bean.AbstractRequest;

public class JobCronMessageCancelRequest extends AbstractRequest {
    private static final long serialVersionUID = -3978068239643822421L;
    private Long series;

    public JobCronMessageCancelRequest() {
    }

    public Long getSeries() {
        return this.series;
    }

    public void setSeries(Long series) {
        this.series = series;
    }
}


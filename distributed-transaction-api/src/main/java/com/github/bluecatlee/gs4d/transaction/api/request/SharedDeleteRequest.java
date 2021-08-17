package com.github.bluecatlee.gs4d.transaction.api.request;

import com.github.bluecatlee.gs4d.common.bean.AbstractRequest;

public class SharedDeleteRequest extends AbstractRequest {
    private static final long serialVersionUID = -2867186861033514487L;

    private Long series;

    public Long getSeries() {
        return this.series;
    }

    public void setSeries(Long series) {
        this.series = series;
    }
}


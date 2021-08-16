package com.github.bluecatlee.gs4d.message.api.request;

import com.github.bluecatlee.gs4d.common.bean.AbstractRequest;

import javax.validation.constraints.NotNull;

public class TopicConfigDeleteRequest extends AbstractRequest {
    private static final long serialVersionUID = 4267389508279196755L;
    @NotNull(message = "series不能为空！")
    private Long series;

    public Long getSeries() {
        return this.series;
    }

    public void setSeries(Long series) {
        this.series = series;
    }
}

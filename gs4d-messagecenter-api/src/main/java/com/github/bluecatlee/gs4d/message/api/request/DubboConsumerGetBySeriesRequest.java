package com.github.bluecatlee.gs4d.message.api.request;

import com.github.bluecatlee.gs4d.common.bean.AbstractRequest;

import javax.validation.constraints.NotNull;

public class DubboConsumerGetBySeriesRequest extends AbstractRequest {
    private static final long serialVersionUID = 4541018222987457730L;
//    @ApiField(description = "消费者序号")
    @NotNull(message = "消费者序号不能为空!")
    private Long series;

    public Long getSeries() {
        return this.series;
    }

    public void setSeries(Long series) {
        this.series = series;
    }
}


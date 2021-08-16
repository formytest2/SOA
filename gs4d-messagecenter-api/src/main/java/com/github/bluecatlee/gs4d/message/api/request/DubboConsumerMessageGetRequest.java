package com.github.bluecatlee.gs4d.message.api.request;

import com.github.bluecatlee.gs4d.common.bean.AbstractRequest;

import javax.validation.constraints.NotNull;

public class DubboConsumerMessageGetRequest extends AbstractRequest {
    private static final long serialVersionUID = 1626745992876315755L;
//    @ApiField(description = "消费者序号")
    @NotNull(message = "消费者序号不能为空!")
    private Long consumerSeries;

    public Long getConsumerSeries() {
        return this.consumerSeries;
    }

    public void setConsumerSeries(Long consumerSeries) {
        this.consumerSeries = consumerSeries;
    }
}


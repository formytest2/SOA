package com.github.bluecatlee.gs4d.message.api.request;

import com.github.bluecatlee.gs4d.common.bean.AbstractRequest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ConsumerSeriesUpdateRequest extends AbstractRequest {
    private static final long serialVersionUID = 240349571693979758L;
    @NotBlank(message = "消息主题不能为空!")
    private String topic;
    @NotBlank(message = "不能为空!")
    private String tag;
    @NotNull(message = "消费者编号不能为空!")
    private Long consumerSeries;
    @NotBlank(message = "消费者类型不能为空!")
    private String consumerType;

    public String getTopic() {
        return this.topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTag() {
        return this.tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Long getConsumerSeries() {
        return this.consumerSeries;
    }

    public void setConsumerSeries(Long consumerSeries) {
        this.consumerSeries = consumerSeries;
    }

    public String getConsumerType() {
        return this.consumerType;
    }

    public void setConsumerType(String consumerType) {
        this.consumerType = consumerType;
    }
}


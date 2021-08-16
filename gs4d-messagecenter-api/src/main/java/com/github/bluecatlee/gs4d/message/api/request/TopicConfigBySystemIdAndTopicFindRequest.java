package com.github.bluecatlee.gs4d.message.api.request;

import com.github.bluecatlee.gs4d.common.bean.AbstractRequest;

import javax.validation.constraints.NotNull;

public class TopicConfigBySystemIdAndTopicFindRequest extends AbstractRequest {
    private static final long serialVersionUID = -6752834714435580605L;
    @NotNull(message = "系统ID不能为空！")
    private Long systemId;
    private Long topicSeries;

    public Long getSystemId() {
        return this.systemId;
    }

    public void setSystemId(Long systemId) {
        this.systemId = systemId;
    }

    public Long getTopicSeries() {
        return this.topicSeries;
    }

    public void setTopicSeries(Long topicSeries) {
        this.topicSeries = topicSeries;
    }
}


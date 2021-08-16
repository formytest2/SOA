package com.github.bluecatlee.gs4d.message.api.request;

import com.github.bluecatlee.gs4d.common.bean.AbstractRequest;

public class TopicByTopicTypeFindRequest extends AbstractRequest {
    private static final long serialVersionUID = 5936523926919265542L;
    private Long topicTopicType;

    public Long getTopicTopicType() {
        return this.topicTopicType;
    }

    public void setTopicTopicType(Long topicTopicType) {
        this.topicTopicType = topicTopicType;
    }
}


package com.github.bluecatlee.gs4d.message.api.request;

import com.github.bluecatlee.gs4d.common.bean.AbstractRequest;

public class TopicTagByTypeGetRequest extends AbstractRequest {
    private static final long serialVersionUID = -1780671506902031419L;
    private Long topicType;

    public Long getTopicType() {
        return this.topicType;
    }

    public void setTopicType(Long topicType) {
        this.topicType = topicType;
    }
}

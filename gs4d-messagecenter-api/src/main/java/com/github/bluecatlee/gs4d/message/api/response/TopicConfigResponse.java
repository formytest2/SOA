package com.github.bluecatlee.gs4d.message.api.response;

import com.github.bluecatlee.gs4d.common.bean.MessagePack;
import com.github.bluecatlee.gs4d.message.api.model.TopicConfigModel;

public class TopicConfigResponse extends MessagePack {
    private static final long serialVersionUID = 3002972851246483064L;
    private TopicConfigModel topicConfigModel;

    public TopicConfigModel getTopicConfigModel() {
        return this.topicConfigModel;
    }

    public void setTopicConfigModel(TopicConfigModel topicConfigModel) {
        this.topicConfigModel = topicConfigModel;
    }
}


package com.github.bluecatlee.gs4d.message.api.response;

import com.github.bluecatlee.gs4d.common.bean.MessagePack;
import com.github.bluecatlee.gs4d.message.api.model.TopicConfigModel;

import java.util.List;

public class TopicConfigBySystemIdFindResponse extends MessagePack {
    private static final long serialVersionUID = -682977096276022939L;
    private List<TopicConfigModel> topics;

    public List<TopicConfigModel> getTopics() {
        return this.topics;
    }

    public void setTopics(List<TopicConfigModel> topics) {
        this.topics = topics;
    }
}


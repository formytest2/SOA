package com.github.bluecatlee.gs4d.message.api.response;

import com.github.bluecatlee.gs4d.common.bean.MessagePack;
import com.github.bluecatlee.gs4d.message.api.model.TopicConfigModel;

import java.util.List;

public class TopicTagByTypeGetResponse extends MessagePack {
    private static final long serialVersionUID = -3827432877179648859L;
    private List<TopicConfigModel> topicList;

    public List<TopicConfigModel> getTopicList() {
        return this.topicList;
    }

    public void setTopicList(List<TopicConfigModel> topicList) {
        this.topicList = topicList;
    }
}


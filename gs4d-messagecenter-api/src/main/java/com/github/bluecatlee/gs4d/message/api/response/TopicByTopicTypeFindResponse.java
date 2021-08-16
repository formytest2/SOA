package com.github.bluecatlee.gs4d.message.api.response;

import com.github.bluecatlee.gs4d.common.bean.MessagePack;
import com.github.bluecatlee.gs4d.message.api.model.TopicConfigModel;

import java.util.List;

public class TopicByTopicTypeFindResponse extends MessagePack {
    private static final long serialVersionUID = 6185100756149877967L;
    List<TopicConfigModel> models;

    public List<TopicConfigModel> getModels() {
        return this.models;
    }

    public void setModels(List<TopicConfigModel> models) {
        this.models = models;
    }
}


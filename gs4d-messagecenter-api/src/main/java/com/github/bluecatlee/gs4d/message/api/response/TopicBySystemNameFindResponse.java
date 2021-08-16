package com.github.bluecatlee.gs4d.message.api.response;

import com.github.bluecatlee.gs4d.common.bean.MessagePack;

import java.util.List;

public class TopicBySystemNameFindResponse extends MessagePack {
    private static final long serialVersionUID = -3316244232323390250L;
    private List<String> topic;

    public List<String> getTopic() {
        return this.topic;
    }

    public void setTopic(List<String> topic) {
        this.topic = topic;
    }
}

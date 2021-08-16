package com.github.bluecatlee.gs4d.message.api.response;

import com.github.bluecatlee.gs4d.common.bean.MessagePack;

import java.util.List;

public class AllFatherTopicFindResponse extends MessagePack {
    private static final long serialVersionUID = 2676443343453796031L;
    private List<String> fatherTopics;

    public List<String> getFatherTopics() {
        return this.fatherTopics;
    }

    public void setFatherTopics(List<String> fatherTopics) {
        this.fatherTopics = fatherTopics;
    }
}

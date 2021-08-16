package com.github.bluecatlee.gs4d.message.api.response;

import com.github.bluecatlee.gs4d.common.bean.MessagePack;

import java.util.List;

public class TagBySystemNameAndTopicFindResponse extends MessagePack {
    private static final long serialVersionUID = -3740928950143679934L;
    private List<String> tag;

    public List<String> getTag() {
        return this.tag;
    }

    public void setTag(List<String> tag) {
        this.tag = tag;
    }
}


package com.github.bluecatlee.gs4d.message.api.request;

import com.github.bluecatlee.gs4d.common.bean.AbstractRequest;

import javax.validation.constraints.NotEmpty;

public class MessageCountGetRequest extends AbstractRequest {
    private static final long serialVersionUID = 3934007990854645339L;
    @NotEmpty(message = "topic不能为空！")
    private String topic;
    @NotEmpty(message = "tag名不能为空！")
    private String tag;
    @NotEmpty(message = "消息key不能为空！")
    private String messageKey;

    public String getTopic() {
        return this.topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTag() {
        return this.tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getMessageKey() {
        return this.messageKey;
    }

    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }
}


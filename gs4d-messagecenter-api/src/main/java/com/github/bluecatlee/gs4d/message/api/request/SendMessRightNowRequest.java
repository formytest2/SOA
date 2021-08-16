package com.github.bluecatlee.gs4d.message.api.request;

import com.github.bluecatlee.gs4d.common.bean.AbstractRequest;

public class SendMessRightNowRequest extends AbstractRequest {
    private static final long serialVersionUID = -4116401392524689899L;
    private String topic;
    private String tag;
    private String msgKey;
    private Object body;
    private Long userId = 0L;
    private String fromSystem;

    public SendMessRightNowRequest() {
    }

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

    public String getMsgKey() {
        return this.msgKey;
    }

    public void setMsgKey(String msgKey) {
        this.msgKey = msgKey;
    }

    public Object getBody() {
        return this.body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFromSystem() {
        return this.fromSystem;
    }

    public void setFromSystem(String fromSystem) {
        this.fromSystem = fromSystem;
    }
}


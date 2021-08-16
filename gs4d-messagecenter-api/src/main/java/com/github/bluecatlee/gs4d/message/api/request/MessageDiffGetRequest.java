package com.github.bluecatlee.gs4d.message.api.request;

import com.github.bluecatlee.gs4d.common.bean.AbstractRequest;

import javax.validation.constraints.NotBlank;

public class MessageDiffGetRequest extends AbstractRequest {
    private static final long serialVersionUID = -3530632127626914383L;
    @NotBlank(message = "topic不能为空")
    private String topic;
    @NotBlank(message = "tag不能为空")
    private String tag;

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
}

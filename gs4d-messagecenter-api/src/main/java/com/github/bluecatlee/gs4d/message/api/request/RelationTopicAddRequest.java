package com.github.bluecatlee.gs4d.message.api.request;

import com.github.bluecatlee.gs4d.common.bean.AbstractRequest;

import javax.validation.constraints.NotEmpty;

public class RelationTopicAddRequest extends AbstractRequest {
    private static final long serialVersionUID = -8730015347972367076L;
    @NotEmpty(message = "fathertopic不能为空！")
    private String fatherTopic;
    @NotEmpty(message = "topic不能为空！")
    private String topic;
    @NotEmpty(message = "tag不能为空！")
    private String tag;
    @NotEmpty(message = "系统名不能为空！")
    private String systemName;

    public String getFatherTopic() {
        return this.fatherTopic;
    }

    public void setFatherTopic(String fatherTopic) {
        this.fatherTopic = fatherTopic;
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

    public String getSystemName() {
        return this.systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }
}


package com.github.bluecatlee.gs4d.message.api.request;

import com.github.bluecatlee.gs4d.common.bean.AbstractRequest;

import javax.validation.constraints.NotEmpty;

public class FatherTopicFindRequest extends AbstractRequest {
    private static final long serialVersionUID = -8299163080805755995L;
    @NotEmpty(message = "fathertopic不能为空！")
    private String fatherTopic;

    public String getFatherTopic() {
        return this.fatherTopic;
    }

    public void setFatherTopic(String fatherTopic) {
        this.fatherTopic = fatherTopic;
    }
}

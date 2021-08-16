package com.github.bluecatlee.gs4d.message.api.request;

import com.github.bluecatlee.gs4d.common.bean.AbstractRequest;

import javax.validation.constraints.NotEmpty;

public class FatherTopicAddRequest extends AbstractRequest {
    private static final long serialVersionUID = 1L;
    @NotEmpty(message = "fathertopic不能为空！")
    private String fatherTopic;
    @NotEmpty(message = "remark不能为空！")
    private String remark;

    public String getFatherTopic() {
        return this.fatherTopic;
    }

    public void setFatherTopic(String fatherTopic) {
        this.fatherTopic = fatherTopic;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}

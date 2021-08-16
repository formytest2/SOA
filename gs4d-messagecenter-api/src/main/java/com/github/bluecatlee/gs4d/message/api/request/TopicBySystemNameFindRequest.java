package com.github.bluecatlee.gs4d.message.api.request;

import com.github.bluecatlee.gs4d.common.bean.AbstractRequest;

import javax.validation.constraints.NotEmpty;

public class TopicBySystemNameFindRequest extends AbstractRequest {
    private static final long serialVersionUID = 7024794214290413441L;
    @NotEmpty(message = "系统名不能为空！")
    private Long systemNumId;

    public Long getSystemNumId() {
        return this.systemNumId;
    }

    public void setSystemNumId(Long systemNumId) {
        this.systemNumId = systemNumId;
    }
}


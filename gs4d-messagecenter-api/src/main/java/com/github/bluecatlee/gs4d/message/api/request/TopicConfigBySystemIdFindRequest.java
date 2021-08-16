package com.github.bluecatlee.gs4d.message.api.request;

import com.github.bluecatlee.gs4d.common.bean.AbstractRequest;

import javax.validation.constraints.NotNull;

public class TopicConfigBySystemIdFindRequest extends AbstractRequest {
    private static final long serialVersionUID = -4417957816860923396L;
    @NotNull(message = "系统名不能为空！")
    private Long systemId;

    public Long getSystemId() {
        return this.systemId;
    }

    public void setSystemId(Long systemId) {
        this.systemId = systemId;
    }
}


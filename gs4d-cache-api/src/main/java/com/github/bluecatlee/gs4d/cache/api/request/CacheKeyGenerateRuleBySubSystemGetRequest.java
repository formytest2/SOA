package com.github.bluecatlee.gs4d.cache.api.request;

import com.github.bluecatlee.gs4d.common.bean.AbstractRequest;

import javax.validation.constraints.NotEmpty;

public class CacheKeyGenerateRuleBySubSystemGetRequest extends AbstractRequest {
    private static final long serialVersionUID = 1L;
//    @ApiField(description = "子系统名")
    @NotEmpty(message = "子系统名不能为空！")
    private String subSystem;

    public CacheKeyGenerateRuleBySubSystemGetRequest() {
    }

    public String getSubSystem() {
        return this.subSystem;
    }

    public void setSubSystem(String subSystem) {
        this.subSystem = subSystem;
    }
}


package com.github.bluecatlee.gs4d.cache.api.request;

import com.github.bluecatlee.gs4d.common.bean.AbstractRequest;

import javax.validation.constraints.NotEmpty;

public class CacheKeyGenerateRuleByMethodNameGetRequest extends AbstractRequest {
    private static final long serialVersionUID = 1L;
//    @ApiField(description = "方法名称")
    @NotEmpty(message = "方法名称不能为空！")
    private String methodName;

    public CacheKeyGenerateRuleByMethodNameGetRequest() {
    }

    public String getMethodName() {
        return this.methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
}

package com.github.bluecatlee.gs4d.cache.api.request;

import com.github.bluecatlee.gs4d.common.bean.AbstractRequest;

import javax.validation.constraints.NotEmpty;

public class CacheGetRequest extends AbstractRequest {
    private static final long serialVersionUID = 1L;
//    @ApiField(description = "方法名称")
    @NotEmpty(message = "方法名称不能为空！")
    private String methodName;
//    @ApiField(description = "入参")
    @NotEmpty(message = "入参不能为空！")
    private String params;

    public CacheGetRequest() {
    }

    public String getMethodName() {
        return this.methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getParams() {
        return this.params;
    }

    public void setParams(String params) {
        this.params = params;
    }
}

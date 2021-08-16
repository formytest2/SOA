package com.github.bluecatlee.gs4d.pay.caller.bean;

import com.github.bluecatlee.gs4d.common.bean.AbstractRequest;

public class ServiceMethodByMethodGetRequest extends AbstractRequest {
    private static final long serialVersionUID = -3772956376514441533L;
    private String method;

    public ServiceMethodByMethodGetRequest() {
    }

    public String getMethod() {
        return this.method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}

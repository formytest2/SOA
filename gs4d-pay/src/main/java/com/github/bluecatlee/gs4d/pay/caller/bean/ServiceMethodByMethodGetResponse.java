package com.github.bluecatlee.gs4d.pay.caller.bean;

import com.github.bluecatlee.gs4d.common.bean.MessagePack;

public class ServiceMethodByMethodGetResponse extends MessagePack {
    private static final long serialVersionUID = -8786609296789055714L;
    private ServiceMethod serviceMethod;

    public ServiceMethodByMethodGetResponse() {
    }

    public ServiceMethod getServiceMethod() {
        return this.serviceMethod;
    }

    public void setServiceMethod(ServiceMethod serviceMethod) {
        this.serviceMethod = serviceMethod;
    }
}

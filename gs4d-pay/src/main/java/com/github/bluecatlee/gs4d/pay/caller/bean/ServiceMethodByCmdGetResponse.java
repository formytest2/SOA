package com.github.bluecatlee.gs4d.pay.caller.bean;

import com.github.bluecatlee.gs4d.common.bean.MessagePack;

public class ServiceMethodByCmdGetResponse extends MessagePack {
    private static final long serialVersionUID = -767036911837472023L;
    private ServiceMethod serviceMethod;

    public ServiceMethodByCmdGetResponse() {
    }

    public ServiceMethod getServiceMethod() {
        return this.serviceMethod;
    }

    public void setServiceMethod(ServiceMethod serviceMethod) {
        this.serviceMethod = serviceMethod;
    }
}

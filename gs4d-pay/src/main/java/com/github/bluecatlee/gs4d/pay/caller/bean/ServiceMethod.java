package com.github.bluecatlee.gs4d.pay.caller.bean;

import java.io.Serializable;

public class ServiceMethod implements Serializable {
    private static final long serialVersionUID = 3438171223076548476L;
    private String cmd;
    private String method;
    private String serviceName;
    private String serviceMethod;
    private Long sessionSign;

    public ServiceMethod() {
    }

    public String getCmd() {
        return this.cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getMethod() {
        return this.method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getServiceName() {
        return this.serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceMethod() {
        return this.serviceMethod;
    }

    public void setServiceMethod(String serviceMethod) {
        this.serviceMethod = serviceMethod;
    }

    public Long getSessionSign() {
        return this.sessionSign;
    }

    public void setSessionSign(Long sessionSign) {
        this.sessionSign = sessionSign;
    }
}


package com.github.bluecatlee.gs4d.pay.caller.bean;

import com.github.bluecatlee.gs4d.common.bean.AbstractRequest;

public class ServiceMethodByCmdGetRequest extends AbstractRequest {
    private static final long serialVersionUID = -1432261998181838792L;
    private String cmd;

    public ServiceMethodByCmdGetRequest() {
    }

    public String getCmd() {
        return this.cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }
}


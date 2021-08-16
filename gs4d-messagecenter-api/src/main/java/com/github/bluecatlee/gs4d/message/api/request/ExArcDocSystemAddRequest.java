package com.github.bluecatlee.gs4d.message.api.request;

import com.github.bluecatlee.gs4d.common.bean.AbstractRequest;

public class ExArcDocSystemAddRequest extends AbstractRequest {
    private static final long serialVersionUID = 2448023711145688354L;
    private String systemName;

    public String getSystemName() {
        return this.systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }
}

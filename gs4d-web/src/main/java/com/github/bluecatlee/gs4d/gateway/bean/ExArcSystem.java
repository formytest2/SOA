package com.github.bluecatlee.gs4d.gateway.bean;

import java.io.Serializable;

public class ExArcSystem implements Serializable {
    private static final long serialVersionUID = -8410698096926642070L;

    private Integer sysNumId;
    private String sysName;

    public ExArcSystem() {
    }

    public Integer getSysNumId() {
        return this.sysNumId;
    }

    public void setSysNumId(Integer sysNumId) {
        this.sysNumId = sysNumId;
    }

    public String getSysName() {
        return this.sysName;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }
}

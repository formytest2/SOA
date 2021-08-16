package com.github.bluecatlee.gs4d.message.api.model;

import java.io.Serializable;

public class SystemModel implements Serializable {
    private static final long serialVersionUID = -5514536267033942749L;
    private Long systemNumId;
    private String systemName;

    public Long getSystemNumId() {
        return this.systemNumId;
    }

    public void setSystemNumId(Long systemNumId) {
        this.systemNumId = systemNumId;
    }

    public String getSystemName() {
        return this.systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }
}


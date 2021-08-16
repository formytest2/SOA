package com.github.bluecatlee.gs4d.gateway.bean;

import java.io.Serializable;

public class CommonCallTableInfoByFuncname implements Serializable {
    private static final long serialVersionUID = 260240949873999591L;

    private String cmd;
    private String sysNumId;
    private String requestSample;

    public CommonCallTableInfoByFuncname() {
    }

    public String getRequestSample() {
        return this.requestSample;
    }

    public void setRequestSample(String requestSample) {
        this.requestSample = requestSample;
    }

    public String getCmd() {
        return this.cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getSysNumId() {
        return this.sysNumId;
    }

    public void setSysNumId(String sysNumId) {
        this.sysNumId = sysNumId;
    }

}


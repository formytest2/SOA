package com.github.bluecatlee.gs4d.gateway.bean;

import java.io.Serializable;

public class CommonCallTableInfoByCmd implements Serializable {
    private static final long serialVersionUID = 4657712014530042425L;

    private String cmd;
    private String funcname;
    private String requestSample;
    private String remark;
    private String method;

    public CommonCallTableInfoByCmd() {
    }

    public String getMethod() {
        return this.method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCmd() {
        return this.cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getFuncname() {
        return this.funcname;
    }

    public void setFuncname(String funcname) {
        this.funcname = funcname;
    }

    public String getRequestSample() {
        return this.requestSample;
    }

    public void setRequestSample(String requestSample) {
        this.requestSample = requestSample;
    }
}

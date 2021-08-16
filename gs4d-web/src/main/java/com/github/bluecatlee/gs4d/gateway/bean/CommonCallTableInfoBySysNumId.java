package com.github.bluecatlee.gs4d.gateway.bean;

import java.io.Serializable;

public class CommonCallTableInfoBySysNumId implements Serializable {
    private static final long serialVersionUID = 5834874345901174854L;

    private String cmd;
    private String remark;
    private String funcname;
    private String method;

    public CommonCallTableInfoBySysNumId() {
    }

    public String getMethod() {
        return this.method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getCmd() {
        return this.cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getFuncname() {
        return this.funcname;
    }

    public void setFuncname(String funcname) {
        this.funcname = funcname;
    }
}

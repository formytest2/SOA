package com.github.bluecatlee.gs4d.export.request;


import com.github.bluecatlee.gs4d.common.bean.AbstractRequest;

public class CommonQueryGetRequest extends AbstractRequest {

    private static final long serialVersionUID = 1673356207290846918L;
    private String sqlId;

    public CommonQueryGetRequest() {
    }

    public String getSqlId() {
        return this.sqlId;
    }

    public void setSqlId(String sqlId) {
        this.sqlId = sqlId;
    }

}

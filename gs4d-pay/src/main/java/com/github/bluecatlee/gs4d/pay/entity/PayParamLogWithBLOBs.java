package com.github.bluecatlee.gs4d.pay.entity;

public class PayParamLogWithBLOBs extends PayParamLog {
    private String requestParams;

    private String responseParams;

    public String getRequestParams() {
        return requestParams;
    }

    public void setRequestParams(String requestParams) {
        this.requestParams = requestParams == null ? null : requestParams.trim();
    }

    public String getResponseParams() {
        return responseParams;
    }

    public void setResponseParams(String responseParams) {
        this.responseParams = responseParams == null ? null : responseParams.trim();
    }
}
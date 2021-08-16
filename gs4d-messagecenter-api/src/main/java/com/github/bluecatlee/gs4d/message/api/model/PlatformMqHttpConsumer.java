package com.github.bluecatlee.gs4d.message.api.model;

import java.io.Serializable;

public class PlatformMqHttpConsumer implements Serializable {
    private static final long serialVersionUID = -8015639404198105888L;
    private Long httpSeries;
    private String url;
    private String urlTest;
    private String paramName;
    private String httpUserName;
    private String httpRemark;
    private String httpHead;
    private String urlDevelop;

    public Long getHttpSeries() {
        return this.httpSeries;
    }

    public void setHttpSeries(Long httpSeries) {
        this.httpSeries = httpSeries;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlTest() {
        return this.urlTest;
    }

    public void setUrlTest(String urlTest) {
        this.urlTest = urlTest;
    }

    public String getParamName() {
        return this.paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getHttpUserName() {
        return this.httpUserName;
    }

    public void setHttpUserName(String httpUserName) {
        this.httpUserName = httpUserName;
    }

    public String getHttpRemark() {
        return this.httpRemark;
    }

    public void setHttpRemark(String httpRemark) {
        this.httpRemark = httpRemark;
    }

    public String getHttpHead() {
        return this.httpHead;
    }

    public void setHttpHead(String httpHead) {
        this.httpHead = httpHead;
    }

    public String getUrlDevelop() {
        return this.urlDevelop;
    }

    public void setUrlDevelop(String urlDevelop) {
        this.urlDevelop = urlDevelop;
    }
}


package com.github.bluecatlee.gs4d.message.api.response;

import com.github.bluecatlee.gs4d.common.bean.MessagePack;

public class HttpConsumerGetBySeriesResponse extends MessagePack {
    private static final long serialVersionUID = -5631544992756088031L;
    private Long series;
    private String url;
    private String developmentUrl;
    private String testUrl;
    private String paramName;
    private String createUserName;
    private String remark;
    private String httpHead;

    public Long getSeries() {
        return this.series;
    }

    public void setSeries(Long series) {
        this.series = series;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDevelopmentUrl() {
        return this.developmentUrl;
    }

    public void setDevelopmentUrl(String developmentUrl) {
        this.developmentUrl = developmentUrl;
    }

    public String getTestUrl() {
        return this.testUrl;
    }

    public void setTestUrl(String testUrl) {
        this.testUrl = testUrl;
    }

    public String getParamName() {
        return this.paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getCreateUserName() {
        return this.createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getHttpHead() {
        return this.httpHead;
    }

    public void setHttpHead(String httpHead) {
        this.httpHead = httpHead;
    }
}

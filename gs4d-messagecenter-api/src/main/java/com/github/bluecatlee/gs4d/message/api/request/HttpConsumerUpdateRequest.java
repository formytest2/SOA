package com.github.bluecatlee.gs4d.message.api.request;

import com.github.bluecatlee.gs4d.common.bean.AbstractRequest;

import javax.validation.constraints.NotNull;

public class HttpConsumerUpdateRequest extends AbstractRequest {
    private static final long serialVersionUID = -3254553810039813692L;
//    @ApiField(description = "消费者序号")
    @NotNull(message = "消费者序号不能为空!")
    private Long httpSeries;
    private String url;
    private String urlDevelop;
    private String urlTest;
    private String paramName;
    private String httpUserName;
    private String httpRemark;
    private String httpHead;

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

    public String getUrlDevelop() {
        return this.urlDevelop;
    }

    public void setUrlDevelop(String urlDevelop) {
        this.urlDevelop = urlDevelop;
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
}


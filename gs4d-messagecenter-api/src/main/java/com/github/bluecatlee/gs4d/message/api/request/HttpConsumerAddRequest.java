package com.github.bluecatlee.gs4d.message.api.request;

import com.github.bluecatlee.gs4d.common.bean.AbstractRequest;

import javax.validation.constraints.NotBlank;

public class HttpConsumerAddRequest extends AbstractRequest {
    private static final long serialVersionUID = 2635046036474751695L;
    @NotBlank(message = "url不能为空")
    private String url;
    @NotBlank(message = "urlDevelop不能为空")
    private String urlDevelop;
    @NotBlank(message = "urlTest不能为空")
    private String urlTest;
    private String paramName;
    @NotBlank(message = "创建人不能为空")
    private String httpUserName;
    @NotBlank(message = "备注不能为空")
    private String httpRemark;
    private String httpHead;

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


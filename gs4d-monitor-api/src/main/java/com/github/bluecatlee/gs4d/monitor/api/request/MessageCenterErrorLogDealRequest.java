package com.github.bluecatlee.gs4d.monitor.api.request;

import com.github.bluecatlee.gs4d.common.bean.AbstractRequest;

public class MessageCenterErrorLogDealRequest extends AbstractRequest {
    private static final long serialVersionUID = -1245413938560035741L;

    private Long series;

    private Long systemId;

    private String errorCode;

    private String requestParam;

    private String responseBody;

    private String systemName;

    private String topicName;

    public Long getSeries() {
        return this.series;
    }

    public void setSeries(Long series) {
        this.series = series;
    }

    public Long getSystemId() {
        return this.systemId;
    }

    public void setSystemId(Long systemId) {
        this.systemId = systemId;
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getRequestParam() {
        return this.requestParam;
    }

    public void setRequestParam(String requestParam) {
        this.requestParam = requestParam;
    }

    public String getResponseBody() {
        return this.responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public String getSystemName() {
        return this.systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getTopicName() {
        return this.topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }
}


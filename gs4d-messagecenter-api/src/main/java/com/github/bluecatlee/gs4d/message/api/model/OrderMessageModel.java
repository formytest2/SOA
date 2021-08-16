package com.github.bluecatlee.gs4d.message.api.model;

import java.io.Serializable;

public class OrderMessageModel implements Serializable {
    private static final long serialVersionUID = 3248720203494467685L;
    private String topic;
    private String tag;
    private Long systemId;
    private Long dataSign;
    private Long tenantNumId;
    private Long series;

    public String getTopic() {
        return this.topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTag() {
        return this.tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Long getSystemId() {
        return this.systemId;
    }

    public void setSystemId(Long systemId) {
        this.systemId = systemId;
    }

    public Long getDataSign() {
        return this.dataSign;
    }

    public void setDataSign(Long dataSign) {
        this.dataSign = dataSign;
    }

    public Long getTenantNumId() {
        return this.tenantNumId;
    }

    public void setTenantNumId(Long tenantNumId) {
        this.tenantNumId = tenantNumId;
    }

    public Long getSeries() {
        return this.series;
    }

    public void setSeries(Long series) {
        this.series = series;
    }
}

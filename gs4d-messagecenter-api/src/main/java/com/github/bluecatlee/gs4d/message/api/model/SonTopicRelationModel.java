package com.github.bluecatlee.gs4d.message.api.model;

import java.io.Serializable;

public class SonTopicRelationModel implements Serializable {
    private static final long serialVersionUID = 8693427629828880011L;
    private Long fatherSeries;
    private Long relationSeries;
    private String fatherTopic;
    private String remark;
    private String sonTopic;
    private String sonTag;
    private String tenantNumId;

    public String getFatherTopic() {
        return this.fatherTopic;
    }

    public void setFatherTopic(String fatherTopic) {
        this.fatherTopic = fatherTopic;
    }

    public String getSonTopic() {
        return this.sonTopic;
    }

    public void setSonTopic(String sonTopic) {
        this.sonTopic = sonTopic;
    }

    public String getSonTag() {
        return this.sonTag;
    }

    public void setSonTag(String sonTag) {
        this.sonTag = sonTag;
    }

    public String getTenantNumId() {
        return this.tenantNumId;
    }

    public void setTenantNumId(String tenantNumId) {
        this.tenantNumId = tenantNumId;
    }

    public Long getFatherSeries() {
        return this.fatherSeries;
    }

    public void setFatherSeries(Long fatherSeries) {
        this.fatherSeries = fatherSeries;
    }

    public Long getRelationSeries() {
        return this.relationSeries;
    }

    public void setRelationSeries(Long relationSeries) {
        this.relationSeries = relationSeries;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}

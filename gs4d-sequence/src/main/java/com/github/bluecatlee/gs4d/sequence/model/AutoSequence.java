package com.github.bluecatlee.gs4d.sequence.model;

import java.io.Serializable;

public class AutoSequence implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long series;

    private Long tenantNumId;

    private Long dataSign;

    private String seqName;

    public Long getSeries() {
        return this.series;
    }

    public void setSeries(Long series) {
        this.series = series;
    }

    public Long getTenantNumId() {
        return this.tenantNumId;
    }

    public void setTenantNumId(Long tenantNumId) {
        this.tenantNumId = tenantNumId;
    }

    public Long getDataSign() {
        return this.dataSign;
    }

    public void setDataSign(Long dataSign) {
        this.dataSign = dataSign;
    }

    public String getSeqName() {
        return this.seqName;
    }

    public void setSeqName(String seqName) {
        this.seqName = seqName;
    }
}


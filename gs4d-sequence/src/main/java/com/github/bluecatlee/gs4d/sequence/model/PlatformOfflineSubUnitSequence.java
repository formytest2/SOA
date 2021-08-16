package com.github.bluecatlee.gs4d.sequence.model;

import java.io.Serializable;
import java.util.Date;

public class PlatformOfflineSubUnitSequence implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long series;

    private String seqName;

    private Long startNum;

    private Long endNum;

    private Long subUnitNumId;

    private Date createDtme;

    public Long getSeries() {
        return this.series;
    }

    public void setSeries(Long series) {
        this.series = series;
    }

    public String getSeqName() {
        return this.seqName;
    }

    public void setSeqName(String seqName) {
        this.seqName = seqName;
    }

    public Long getStartNum() {
        return this.startNum;
    }

    public void setStartNum(Long startNum) {
        this.startNum = startNum;
    }

    public Long getEndNum() {
        return this.endNum;
    }

    public void setEndNum(Long endNum) {
        this.endNum = endNum;
    }

    public Long getSubUnitNumId() {
        return this.subUnitNumId;
    }

    public void setSubUnitNumId(Long subUnitNumId) {
        this.subUnitNumId = subUnitNumId;
    }

    public Date getCreateDtme() {
        return this.createDtme;
    }

    public void setCreateDtme(Date createDtme) {
        this.createDtme = createDtme;
    }
}

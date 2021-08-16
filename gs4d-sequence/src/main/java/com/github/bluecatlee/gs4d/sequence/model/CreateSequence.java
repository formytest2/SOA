package com.github.bluecatlee.gs4d.sequence.model;

import java.io.Serializable;
import java.util.Date;

public class CreateSequence implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long series;
    private String seqName;
    private String seqProject;
    private String seqPrefix;
    private String seqNum;
    private String seqVal;
    private Long currentNum;
    private Date CreateTime;
    private Integer currentSeqVal;
    private String squenceTime;
    private Long seqNumStart;
    private Long seqNumEnd;
    private Long disrupt;
    private Long isStoreLocal;
    private Integer proCurrentVal;

    public CreateSequence() {
    }

    public Long getIsStoreLocal() {
        return this.isStoreLocal;
    }

    public void setIsStoreLocal(Long isStoreLocal) {
        this.isStoreLocal = isStoreLocal;
    }

    public Long getDisrupt() {
        return this.disrupt;
    }

    public void setDisrupt(Long disrupt) {
        this.disrupt = disrupt;
    }

    public Integer getProCurrentVal() {
        return this.proCurrentVal;
    }

    public void setProCurrentVal(Integer proCurrentVal) {
        this.proCurrentVal = proCurrentVal;
    }

    public Long getSeqNumStart() {
        return this.seqNumStart;
    }

    public void setSeqNumStart(Long seqNumStart) {
        this.seqNumStart = seqNumStart;
    }

    public Long getSeqNumEnd() {
        return this.seqNumEnd;
    }

    public void setSeqNumEnd(Long seqNumEnd) {
        this.seqNumEnd = seqNumEnd;
    }

    public Long getCurrentNum() {
        return this.currentNum;
    }

    public void setCurrentNum(Long currentNum) {
        this.currentNum = currentNum;
    }

    public static long getSerialversionuid() {
        return 1L;
    }

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

    public String getSeqProject() {
        return this.seqProject;
    }

    public void setSeqProject(String seqProject) {
        this.seqProject = seqProject;
    }

    public String getSeqPrefix() {
        return this.seqPrefix;
    }

    public void setSeqPrefix(String seqPrefix) {
        this.seqPrefix = seqPrefix;
    }

    public String getSeqNum() {
        return this.seqNum;
    }

    public void setSeqNum(String seqNum) {
        this.seqNum = seqNum;
    }

    public String getSeqVal() {
        return this.seqVal;
    }

    public void setSeqVal(String seqVal) {
        this.seqVal = seqVal;
    }

    public Date getCreateTime() {
        return this.CreateTime;
    }

    public void setCreateTime(Date createTime) {
        this.CreateTime = createTime;
    }

    public Integer getCurrentSeqVal() {
        return this.currentSeqVal;
    }

    public void setCurrentSeqVal(Integer currentSeqVal) {
        this.currentSeqVal = currentSeqVal;
    }

    public String getSquenceTime() {
        return this.squenceTime;
    }

    public void setSquenceTime(String squenceTime) {
        this.squenceTime = squenceTime;
    }
}

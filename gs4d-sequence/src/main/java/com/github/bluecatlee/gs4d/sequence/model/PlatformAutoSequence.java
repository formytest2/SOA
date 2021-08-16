package com.github.bluecatlee.gs4d.sequence.model;

import java.io.Serializable;
import java.util.Date;

public class PlatformAutoSequence implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long series;

    private Long tenantNumId;

    private Long dataSign;

    private String seqName;

    private String seqProject;

    private String seqPrefix;

    private Long currentNum;

    private Integer isYear;

    private Integer isMonth;

    private Integer isDay;

    private Integer isFlowCode;

    private Date createTime;

    private String remark;

    private String spare;

    private Long initValue;

    private Long isClear;

    private Long flowCodeLength;

    private Integer cacheNum;

    public Integer getCacheNum() {
        return this.cacheNum;
    }

    public void setCacheNum(Integer cacheNum) {
        this.cacheNum = cacheNum;
    }

    public Long getFlowCodeLength() {
        return this.flowCodeLength;
    }

    public void setFlowCodeLength(Long flowCodeLength) {
        this.flowCodeLength = flowCodeLength;
    }

    public Long getInitValue() {
        return this.initValue;
    }

    public void setInitValue(Long initValue) {
        this.initValue = initValue;
    }

    public Long getIsClear() {
        return this.isClear;
    }

    public void setIsClear(Long isClear) {
        this.isClear = isClear;
    }

    public Integer getIsYear() {
        return this.isYear;
    }

    public void setIsYear(Integer isYear) {
        this.isYear = isYear;
    }

    public Integer getIsMonth() {
        return this.isMonth;
    }

    public void setIsMonth(Integer isMonth) {
        this.isMonth = isMonth;
    }

    public Integer getIsDay() {
        return this.isDay;
    }

    public void setIsDay(Integer isDay) {
        this.isDay = isDay;
    }

    public Integer getIsFlowCode() {
        return this.isFlowCode;
    }

    public void setIsFlowCode(Integer isFlowCode) {
        this.isFlowCode = isFlowCode;
    }

    public String getSpare() {
        return this.spare;
    }

    public void setSpare(String spare) {
        this.spare = spare;
    }

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

    public Long getCurrentNum() {
        return this.currentNum;
    }

    public void setCurrentNum(Long currentNum) {
        this.currentNum = currentNum;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}


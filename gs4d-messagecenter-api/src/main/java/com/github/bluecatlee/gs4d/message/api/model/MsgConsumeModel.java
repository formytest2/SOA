package com.github.bluecatlee.gs4d.message.api.model;

import java.io.Serializable;

public class MsgConsumeModel implements Serializable {
    private Long msgSeries;
    private String taskTarget;
    private String consumerSuccessDetail;
    private String nextretryTime;
    private Integer retryMax;
    private Integer retryTimes;
    private Long tenantNumId;
    private Long dataSign;
    private Integer stepId;
    private Long consumeTime;

    public Integer getStepId() {
        return this.stepId;
    }

    public void setStepId(Integer stepId) {
        this.stepId = stepId;
    }

    public Long getMsgSeries() {
        return this.msgSeries;
    }

    public void setMsgSeries(Long msgSeries) {
        this.msgSeries = msgSeries;
    }

    public String getTaskTarget() {
        return this.taskTarget;
    }

    public void setTaskTarget(String taskTarget) {
        this.taskTarget = taskTarget;
    }

    public String getConsumerSuccessDetail() {
        return this.consumerSuccessDetail;
    }

    public void setConsumerSuccessDetail(String consumerSuccessDetail) {
        this.consumerSuccessDetail = consumerSuccessDetail;
    }

    public String getNextretryTime() {
        return this.nextretryTime;
    }

    public void setNextretryTime(String nextretryTime) {
        this.nextretryTime = nextretryTime;
    }

    public Integer getRetryMax() {
        return this.retryMax;
    }

    public void setRetryMax(Integer retryMax) {
        this.retryMax = retryMax;
    }

    public Integer getRetryTimes() {
        return this.retryTimes;
    }

    public void setRetryTimes(Integer retryTimes) {
        this.retryTimes = retryTimes;
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

    public Long getConsumeTime() {
        return this.consumeTime;
    }

    public void setConsumeTime(Long consumeTime) {
        this.consumeTime = consumeTime;
    }
}


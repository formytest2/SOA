package com.github.bluecatlee.gs4d.transaction.model;

import java.io.Serializable;
import java.util.Date;

public class MqTransactionModel implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long transactionId;

    private Date transactionCommmitDate;

    private Boolean wetherOutTimeJob = Boolean.valueOf(false);

    private Date systemNowTime;

    public Long getTransactionId() {
        return this.transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public Date getTransactionCommmitDate() {
        return this.transactionCommmitDate;
    }

    public void setTransactionCommmitDate(Date transactionCommmitDate) {
        this.transactionCommmitDate = transactionCommmitDate;
    }

    public Boolean getWetherOutTimeJob() {
        return this.wetherOutTimeJob;
    }

    public void setWetherOutTimeJob(Boolean wetherOutTimeJob) {
        this.wetherOutTimeJob = wetherOutTimeJob;
    }

    public Date getSystemNowTime() {
        return this.systemNowTime;
    }

    public void setSystemNowTime(Date systemNowTime) {
        this.systemNowTime = systemNowTime;
    }

    public String toString() {
        return "MqTransactionModel [transactionId=" + this.transactionId + ", transactionCommmitDate=" + this.transactionCommmitDate + ", wetherOutTimeJob=" + this.wetherOutTimeJob + ", systemNowTime=" + this.systemNowTime + "]";
    }
}

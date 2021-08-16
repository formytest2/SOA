package com.github.bluecatlee.gs4d.transaction.model;

import java.io.Serializable;

public class TranscationWebModel implements Serializable {
    private Long transactionId;

    private String startDtme;

    private String endDtme;

    private String ipAddress;

    private String transactionState;

    private String transactionSign;

    private String fromSystem;

    private String methodName;

    private String rollbackStatus;

    public Long getTransactionId() {
        return this.transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public String getStartDtme() {
        return this.startDtme;
    }

    public void setStartDtme(String startDtme) {
        this.startDtme = startDtme;
    }

    public String getEndDtme() {
        return this.endDtme;
    }

    public void setEndDtme(String endDtme) {
        this.endDtme = endDtme;
    }

    public String getIpAddress() {
        return this.ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getTransactionState() {
        return this.transactionState;
    }

    public void setTransactionState(String transactionState) {
        this.transactionState = transactionState;
    }

    public String getTransactionSign() {
        return this.transactionSign;
    }

    public void setTransactionSign(String transactionSign) {
        this.transactionSign = transactionSign;
    }

    public String getFromSystem() {
        return this.fromSystem;
    }

    public void setFromSystem(String fromSystem) {
        this.fromSystem = fromSystem;
    }

    public String getMethodName() {
        return this.methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getRollbackStatus() {
        return this.rollbackStatus;
    }

    public void setRollbackStatus(String rollbackStatus) {
        this.rollbackStatus = rollbackStatus;
    }
}

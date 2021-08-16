package com.github.bluecatlee.gs4d.transaction.request;

import java.io.Serializable;

public class TransactionInitRequest implements Serializable {
    private static final long serialVersionUID = 2694488671923382674L;

    private String ipAddress;

    private String fromSystem;

    private String methodName;

    private Long transactionRollbackFlag;

    private Long transactionId;

    public String getIpAddress() {
        return this.ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
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

    public Long getTransactionRollbackFlag() {
        return this.transactionRollbackFlag;
    }

    public void setTransactionRollbackFlag(Long transactionRollbackFlag) {
        this.transactionRollbackFlag = transactionRollbackFlag;
    }

    public Long getTransactionId() {
        return this.transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }
}


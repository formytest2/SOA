package com.github.bluecatlee.gs4d.transaction.api.response;

import java.io.Serializable;

public class TransactionInitResponse implements Serializable {
    private static final long serialVersionUID = 2694488671923382674L;

    private Long transactionStartTime;

    private Long code = Long.valueOf(0L);

    private String message = "成功";

    private Long transactionId;

    public Long getTransactionStartTime() {
        return this.transactionStartTime;
    }

    public void setTransactionStartTime(Long transactionStartTime) {
        this.transactionStartTime = transactionStartTime;
    }

    public Long getCode() {
        return this.code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTransactionId() {
        return this.transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }
}

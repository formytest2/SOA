package com.github.bluecatlee.gs4d.message.api.request;

import java.io.Serializable;

public class TransactionMessageConfirmRequest implements Serializable {
    private static final long serialVersionUID = -2696451374948251000L;
    private Long transactionId;

    public TransactionMessageConfirmRequest() {
    }

    public Long getTransactionId() {
        return this.transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }
}


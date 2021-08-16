package com.github.bluecatlee.gs4d.transaction.model;

import java.io.Serializable;

public class TranscationStateListModel implements Serializable {
    private static final long serialVersionUID = -664826718776710362L;

    private Long transactionState;

    private String transactionStateName;

    public Long getTransactionState() {
        return this.transactionState;
    }

    public void setTransactionState(Long transactionState) {
        this.transactionState = transactionState;
    }

    public String getTransactionStateName() {
        return this.transactionStateName;
    }

    public void setTransactionStateName(String transactionStateName) {
        this.transactionStateName = transactionStateName;
    }
}


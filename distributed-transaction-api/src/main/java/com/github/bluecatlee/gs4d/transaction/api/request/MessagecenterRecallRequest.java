package com.github.bluecatlee.gs4d.transaction.api.request;

import java.io.Serializable;

public class MessagecenterRecallRequest implements Serializable {
    private static final long serialVersionUID = -6413921808448646225L;

    private Long transactionId;

    public Long getTransactionId() {
        return this.transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }
}


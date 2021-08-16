package com.github.bluecatlee.gs4d.message.api.request;

import java.io.Serializable;

public class TransactionMessageCancelRequest implements Serializable {
    private static final long serialVersionUID = -6095748725412494784L;
    private Long transactionId;

    public TransactionMessageCancelRequest() {
    }

    public Long getTransactionId() {
        return this.transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }
}


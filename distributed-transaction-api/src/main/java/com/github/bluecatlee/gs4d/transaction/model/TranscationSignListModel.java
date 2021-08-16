package com.github.bluecatlee.gs4d.transaction.model;

import java.io.Serializable;

public class TranscationSignListModel implements Serializable {
    private static final long serialVersionUID = -2387524928354131573L;

    private String transactionSign;

    private String transactionSignName;

    public String getTransactionSign() {
        return this.transactionSign;
    }

    public void setTransactionSign(String transactionSign) {
        this.transactionSign = transactionSign;
    }

    public String getTransactionSignName() {
        return this.transactionSignName;
    }

    public void setTransactionSignName(String transactionSignName) {
        this.transactionSignName = transactionSignName;
    }
}


package com.github.bluecatlee.gs4d.transaction.model;

public class TRANSACTION_ROLLBACK_FLAG {
    private Long SERIES;
    private String TRANSACTION_KEY;

    public Long getSERIES() {
        return this.SERIES;
    }

    public void setSERIES(Long SERIES) {
        this.SERIES = SERIES;
    }

    public String getTRANSACTION_KEY() {
        return this.TRANSACTION_KEY;
    }

    public void setTRANSACTION_KEY(String TRANSACTION_KEY) {
        this.TRANSACTION_KEY = TRANSACTION_KEY;
    }
}


package com.github.bluecatlee.gs4d.message.api.response;

import java.io.Serializable;

public class TransactionMessageCancelResponse implements Serializable {
    private static final long serialVersionUID = -1857400011960828018L;
    private Long code = 0L;
    private String message = "成功";

    public TransactionMessageCancelResponse() {
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
}


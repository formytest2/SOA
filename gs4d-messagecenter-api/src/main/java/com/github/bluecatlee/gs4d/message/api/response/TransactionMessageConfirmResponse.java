package com.github.bluecatlee.gs4d.message.api.response;

import java.io.Serializable;

public class TransactionMessageConfirmResponse implements Serializable {
    private static final long serialVersionUID = -6194287183592842191L;
    private Long code = 0L;
    private String message = "成功";

    public TransactionMessageConfirmResponse() {
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

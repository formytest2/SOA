package com.github.bluecatlee.gs4d.transaction.api.response;

import java.io.Serializable;

public class MessagecenterRecallResponse implements Serializable {
    private static final long serialVersionUID = 1591346215778423802L;

    private Long code = Long.valueOf(0L);

    private String message = "成功";

    private Long state;

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

    public Long getState() {
        return this.state;
    }

    public void setState(Long state) {
        this.state = state;
    }
}


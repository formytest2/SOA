package com.github.bluecatlee.gs4d.transaction.response;

import java.io.Serializable;

public class SharedColumnGetResponse implements Serializable {
    private static final long serialVersionUID = 7450799940831179192L;

    private String sharedColumnName;

    private Long code = Long.valueOf(0L);

    private String message = "成功";

    public String getSharedColumnName() {
        return this.sharedColumnName;
    }

    public void setSharedColumnName(String sharedColumnName) {
        this.sharedColumnName = sharedColumnName;
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


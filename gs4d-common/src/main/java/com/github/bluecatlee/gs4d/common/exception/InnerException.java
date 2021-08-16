package com.github.bluecatlee.gs4d.common.exception;

public class InnerException extends RuntimeException {

    private static final long serialVersionUID = -505163955190087963L;
    private long code;
    private String message;

    public InnerException(long code, String message) {
        this.code = code;
        this.message = message;
    }

    public long getCode() {
        return this.code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

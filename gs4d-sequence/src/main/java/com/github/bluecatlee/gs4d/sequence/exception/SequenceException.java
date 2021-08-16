package com.github.bluecatlee.gs4d.sequence.exception;

public class SequenceException extends RuntimeException {
    private static final long serialVersionUID = 58326433760774844L;
    private Long code;

    public SequenceException(String message) {
        super(message);
    }

    public long getCode() {
        return this.code;
    }

    public void setCode(long code) {
        this.code = code;
    }
}


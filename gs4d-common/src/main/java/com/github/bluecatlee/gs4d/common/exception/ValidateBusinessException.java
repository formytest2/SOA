package com.github.bluecatlee.gs4d.common.exception;

public class ValidateBusinessException extends RuntimeException {

    private static final long serialVersionUID = -9025397667493805518L;
    private long code;

    public ValidateBusinessException(String subSystem, ExceptionType et, String message) {
        super(message);
        if (!et.getCategory().equals(ExceptionTypeCategory.VALIDATE_BUSINESS_EXCEPTION)) {
        }

        this.code = et.getCode();
    }

    public ValidateBusinessException(String subSystem, AbstractExceptionType et, String message) {
        super(message);
        if (!et.getCategory().equals(ExceptionTypeCategory.VALIDATE_BUSINESS_EXCEPTION)) {
        }

        this.code = et.getCode();
    }

    public long getCode() {
        return this.code;
    }

    public void setCode(long code) {
        this.code = code;
    }
}


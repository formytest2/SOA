package com.github.bluecatlee.gs4d.common.exception;

public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 2367297598757939121L;
    private long code;

    public BusinessException(String subSystem, ExceptionType et, String message) {
        this(subSystem, et, message, false);
    }

    public BusinessException(String subSystem, AbstractExceptionType et, String message) {
        this(subSystem, et, message, false);
    }

    public BusinessException(String subSystem, AbstractExceptionType et, String message, boolean ignoreExceptionCategory) {
        super(message);
        if (!ignoreExceptionCategory && !et.getCategory().equals(ExceptionTypeCategory.BUSINESS_EXCEPTION)) {
        }

        this.code = et.getCode();
    }

    public BusinessException(String subSystem, ExceptionType et, String message, boolean ignoreExceptionCategory) {
        super(message);
        if (!ignoreExceptionCategory && !et.getCategory().equals(ExceptionTypeCategory.BUSINESS_EXCEPTION)) {
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


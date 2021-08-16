package com.github.bluecatlee.gs4d.common.exception;

public class ValidateClientException extends RuntimeException {

    private static final long serialVersionUID = 7760846410970627817L;
    private long code;

    public ValidateClientException(String subSystem, ExceptionType et, String message) {
        this(subSystem, et, message, false);
    }

    public ValidateClientException(String subSystem, AbstractExceptionType et, String message) {
        this(subSystem, et, message, false);
    }

    public ValidateClientException(String subSystem, ExceptionType et, String message, boolean ignoreExceptionCategory) {
        super(message);
        if (!ignoreExceptionCategory && !et.getCategory().equals(ExceptionTypeCategory.VALIDATE_CLIENT_EXCEPTION)) {
        }

        this.code = et.getCode();
    }

    // todo 异常类型通用化
    public ValidateClientException(String subSystem, AbstractExceptionType et, String message, boolean ignoreExceptionCategory) {
        super(message);
        if (!ignoreExceptionCategory && !et.getCategory().equals(ExceptionTypeCategory.VALIDATE_CLIENT_EXCEPTION)) {
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


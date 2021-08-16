package com.github.bluecatlee.gs4d.common.exception;

public class DatabaseOperateException extends RuntimeException {

    private static final long serialVersionUID = -8679781160435250655L;
    private long code;

    public DatabaseOperateException(String subSystem, ExceptionType et, String message) {
        super(message);
        if (!et.getCategory().equals(ExceptionTypeCategory.DATABASE_OPERATE_EXCEPTION)) {
        }

        this.code = et.getCode();
    }

    public DatabaseOperateException(String subSystem, AbstractExceptionType et, String message) {
        super(message);
        if (!et.getCategory().equals(ExceptionTypeCategory.DATABASE_OPERATE_EXCEPTION)) {
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


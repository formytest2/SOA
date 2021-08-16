package com.github.bluecatlee.gs4d.pay.exception;

import com.github.bluecatlee.gs4d.pay.constant.RespEnum;

public class BaseServiceException extends RuntimeException {

    private static final long serialVersionUID = 3660339012327408051L;

    protected String errorCode = "5000";

    public BaseServiceException() {
        super();
    }

    public BaseServiceException(RespEnum resp) {
        super(resp.getMsg());
        this.errorCode = resp.getStatus();
    }

    public BaseServiceException(RespEnum resp, String message) {
        super(message);
        this.errorCode = resp.getStatus();
    }

    public BaseServiceException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public BaseServiceException(String message) {
        super(message);
    }

    public BaseServiceException(Throwable cause) {
        super(cause);
    }

    public BaseServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseServiceException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String toString() {
        return "BaseServiceException{" + "message='" + this.getMessage() + '\'' + ", status=" + errorCode + '}';
    }

}
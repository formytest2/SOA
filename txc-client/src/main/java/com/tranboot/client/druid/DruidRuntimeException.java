package com.tranboot.client.druid;

public class DruidRuntimeException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public DruidRuntimeException() {
    }

    public DruidRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public DruidRuntimeException(String message) {
        super(message);
    }

    public DruidRuntimeException(Throwable cause) {
        super(cause);
    }
}

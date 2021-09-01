package com.tranboot.client.exception;

public class TxcNotSupportException extends RuntimeException {
    private static final long serialVersionUID = 3763827537077434132L;

    public TxcNotSupportException(String message) {
        super(message);
    }

    public TxcNotSupportException(Throwable cause, String message) {
        super(message, cause);
    }
}

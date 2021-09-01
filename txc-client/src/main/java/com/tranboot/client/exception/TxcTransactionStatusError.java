package com.tranboot.client.exception;

public class TxcTransactionStatusError extends RuntimeException {
    private static final long serialVersionUID = 9106036746676443390L;

    public TxcTransactionStatusError(String message) {
        super(message);
    }

    public TxcTransactionStatusError(Throwable cause, String message) {
        super(message, cause);
    }
}


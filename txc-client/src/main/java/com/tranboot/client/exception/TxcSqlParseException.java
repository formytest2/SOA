package com.tranboot.client.exception;

public class TxcSqlParseException extends RuntimeException {
    private static final long serialVersionUID = -6218065706560566991L;

    public TxcSqlParseException(String message) {
        super(message);
    }

    public TxcSqlParseException(Throwable cause, String message) {
        super(message, cause);
    }
}


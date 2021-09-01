package com.tranboot.client.druid.wall.violation;

import com.tranboot.client.druid.wall.Violation;

public class SyntaxErrorViolation implements Violation {
    private final Exception exception;
    private final String sql;

    public SyntaxErrorViolation(Exception exception, String sql) {
        this.exception = exception;
        this.sql = sql;
    }

    public String toString() {
        return this.sql;
    }

    public Exception getException() {
        return this.exception;
    }

    public String getSql() {
        return this.sql;
    }

    public String getMessage() {
        return this.exception == null ? "syntax error" : "syntax error: " + this.exception.getMessage();
    }

    public int getErrorCode() {
        return 1001;
    }
}

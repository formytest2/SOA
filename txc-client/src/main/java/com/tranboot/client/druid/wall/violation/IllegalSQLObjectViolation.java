package com.tranboot.client.druid.wall.violation;

import com.tranboot.client.druid.wall.Violation;

public class IllegalSQLObjectViolation implements Violation {
    private final String message;
    private final String sqlPart;
    private final int errorCode;

    public IllegalSQLObjectViolation(int errorCode, String message, String sqlPart) {
        this.errorCode = errorCode;
        this.message = message;
        this.sqlPart = sqlPart;
    }

    public String getSqlPart() {
        return this.sqlPart;
    }

    public String toString() {
        return this.sqlPart;
    }

    public String getMessage() {
        return this.message;
    }

    public int getErrorCode() {
        return this.errorCode;
    }
}

package com.tranboot.client.model;

public enum SQLType {
    INSERT("insert", 0),
    UPDATE("udpate", 1),
    DELETE("delete", 2),
    SELECT("select", 3);

    private String value;
    private int code;

    private SQLType(String type, int code) {
        this.value = type;
        this.code = code;
    }

    public String getType() {
        return this.value;
    }

    public int getCode() {
        return this.code;
    }

    public static SQLType type(String sqlType) {
        byte var2 = -1;
        switch(sqlType.hashCode()) {
            case -1335458389:
                if (sqlType.equals("delete")) {
                    var2 = 2;
                }
                break;
            case -1183792455:
                if (sqlType.equals("insert")) {
                    var2 = 1;
                }
                break;
            case -906021636:
                if (sqlType.equals("select")) {
                    var2 = 3;
                }
                break;
            case -838846263:
                if (sqlType.equals("update")) {
                    var2 = 0;
                }
        }

        switch(var2) {
            case 0:
                return UPDATE;
            case 1:
                return INSERT;
            case 2:
                return DELETE;
            case 3:
                return SELECT;
            default:
                return null;
        }
    }
}


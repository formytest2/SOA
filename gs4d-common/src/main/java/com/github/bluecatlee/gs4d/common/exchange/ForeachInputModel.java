package com.github.bluecatlee.gs4d.common.exchange;

public class ForeachInputModel {
    private String jsonAryName;
    private String contentType;
    public static final String OBJECT = "object";
    public static final String VALUE = "value";

    public ForeachInputModel() {
    }

    public String getJsonAryName() {
        return this.jsonAryName;
    }

    public void setJsonAryName(String jsonAryName) {
        this.jsonAryName = jsonAryName;
    }

    public String getContentType() {
        return this.contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}


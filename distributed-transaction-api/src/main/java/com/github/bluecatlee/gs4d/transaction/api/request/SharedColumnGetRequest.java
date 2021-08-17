package com.github.bluecatlee.gs4d.transaction.api.request;

import java.io.Serializable;

public class SharedColumnGetRequest implements Serializable {
    private static final long serialVersionUID = 8647745308898621011L;

    private String schema;

    private String table;

    public String getSchema() {
        return this.schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getTable() {
        return this.table;
    }

    public void setTable(String table) {
        this.table = table;
    }
}

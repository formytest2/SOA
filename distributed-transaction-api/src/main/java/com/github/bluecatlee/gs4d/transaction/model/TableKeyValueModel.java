package com.github.bluecatlee.gs4d.transaction.model;

import java.io.Serializable;

public class TableKeyValueModel implements Serializable {
    private String column;

    private String value;

    public String getColumn() {
        return this.column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String toString() {
        return "TableKeyValueModel [column=" + this.column + ", value=" + this.value + "]";
    }
}


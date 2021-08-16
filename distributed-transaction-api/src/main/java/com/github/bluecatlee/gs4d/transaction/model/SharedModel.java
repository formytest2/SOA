package com.github.bluecatlee.gs4d.transaction.model;

import java.io.Serializable;

public class SharedModel implements Serializable {
    private static final long serialVersionUID = 1903981000705601896L;

    private Long series;

    private String dbName;

    private String tableName;

    private String sharedColumn;

    public Long getSeries() {
        return this.series;
    }

    public void setSeries(Long series) {
        this.series = series;
    }

    public String getDbName() {
        return this.dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getTableName() {
        return this.tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getSharedColumn() {
        return this.sharedColumn;
    }

    public void setSharedColumn(String sharedColumn) {
        this.sharedColumn = sharedColumn;
    }
}

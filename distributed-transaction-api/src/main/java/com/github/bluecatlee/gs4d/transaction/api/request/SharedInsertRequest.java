package com.github.bluecatlee.gs4d.transaction.api.request;

import com.github.bluecatlee.gs4d.common.bean.AbstractRequest;

public class SharedInsertRequest extends AbstractRequest {
    private static final long serialVersionUID = -5153844388101559237L;

    private String dbName;

    private String tableName;

    private String sharedColumn;

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


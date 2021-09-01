package com.tranboot.client.model;

public class MysqlIndex {
    private String table;
    private int nonUnique;
    private String keyName;
    private int seqInIndex;
    private String columnName;
    private String indexType;

    public MysqlIndex() {
    }

    public String getTable() {
        return this.table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public int getNonUnique() {
        return this.nonUnique;
    }

    public void setNonUnique(int nonUnique) {
        this.nonUnique = nonUnique;
    }

    public String getKeyName() {
        return this.keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public int getSeqInIndex() {
        return this.seqInIndex;
    }

    public void setSeqInIndex(int seqInIndex) {
        this.seqInIndex = seqInIndex;
    }

    public String getColumnName() {
        return this.columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getIndexType() {
        return this.indexType;
    }

    public void setIndexType(String indexType) {
        this.indexType = indexType;
    }
}


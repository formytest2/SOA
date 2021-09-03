package com.tranboot.client.model;

/**
 * mysql表索引信息bean
 *      show index from {table};
 *      如果是联合索引，联合索引的每列字段都是一条记录。同一列可以存在多条记录，因为一个列可以在多个索引中
 */
public class MysqlIndex {
    private String table;       // 表名
    private int nonUnique;      // 是否是唯一索引 0是 1否
    private String keyName;     // 索引名 主键索引的名称为PRIMARY
    private int seqInIndex;     // 索引中的列序列号 联合索引的第一个列的序列为1，依次类推
    private String columnName;  // 列名
    private String indexType;   // 索引类型 一般都是BTREE

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


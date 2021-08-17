package com.github.bluecatlee.gs4d.transaction.api.model;

import java.io.Serializable;

public class SqlParamModel implements Serializable {
    private static final long serialVersionUID = -3880357275578832282L;

    private String sql;

    private String table;

    private String tableKeyValueModels;

    private String txc;

    private Long insertRedisTime;

    private String shard;

    private String redisLockKey;

    private String redisLockValue;

    private Integer transactionType;

    private Integer sqlType;

    public String getSql() {
        return this.sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Long getInsertRedisTime() {
        return this.insertRedisTime;
    }

    public void setInsertRedisTime(Long insertRedisTime) {
        this.insertRedisTime = insertRedisTime;
    }

    public String getTableKeyValueModels() {
        return this.tableKeyValueModels;
    }

    public void setTableKeyValueModels(String tableKeyValueModels) {
        this.tableKeyValueModels = tableKeyValueModels;
    }

    public String getRedisLockKey() {
        return this.redisLockKey;
    }

    public void setRedisLockKey(String redisLockKey) {
        this.redisLockKey = redisLockKey;
    }

    public String getTable() {
        return this.table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getTxc() {
        return this.txc;
    }

    public void setTxc(String txc) {
        this.txc = txc;
    }

    public Integer getTransactionType() {
        return this.transactionType;
    }

    public void setTransactionType(Integer transactionType) {
        this.transactionType = transactionType;
    }

    public Integer getSqlType() {
        return this.sqlType;
    }

    public void setSqlType(Integer sqlType) {
        this.sqlType = sqlType;
    }

    public String getRedisLockValue() {
        return this.redisLockValue;
    }

    public void setRedisLockValue(String redisLockValue) {
        this.redisLockValue = redisLockValue;
    }

    public String getShard() {
        return this.shard;
    }

    public void setShard(String shard) {
        this.shard = shard;
    }

    public String toString() {
        return "SqlParamModel [sql=" + this.sql + ", table=" + this.table + ", tableKeyValueModels=" + this.tableKeyValueModels + ", txc=" + this.txc + ", insertRedisTime=" + this.insertRedisTime + ", shard=" + this.shard + ", redisLockKey=" + this.redisLockKey + ", redisLockValue=" + this.redisLockValue + ", transactionType=" + this.transactionType + ", sqlType=" + this.sqlType + "]";
    }
}


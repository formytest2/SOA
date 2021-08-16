package com.github.bluecatlee.gs4d.transaction.model;

import java.io.Serializable;

public class TranscationDetailModel implements Serializable {
    private Long transactionId;

    private String rollbackDtme;

    private String sourceDb;

    private String tableName;

    private String sql;

    private String transactionSign;

    private String transactionErrorLog;

    private String sqlIsOutTime;

    private String sqlStatus;

    private Long sqlTimeOut;

    private Long resultNum;

    private Long dbId;

    private String dataFrom;

    public Long getTransactionId() {
        return this.transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public String getRollbackDtme() {
        return this.rollbackDtme;
    }

    public void setRollbackDtme(String rollbackDtme) {
        this.rollbackDtme = rollbackDtme;
    }

    public String getSourceDb() {
        return this.sourceDb;
    }

    public void setSourceDb(String sourceDb) {
        this.sourceDb = sourceDb;
    }

    public String getTableName() {
        return this.tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getSql() {
        return this.sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getTransactionSign() {
        return this.transactionSign;
    }

    public void setTransactionSign(String transactionSign) {
        this.transactionSign = transactionSign;
    }

    public String getTransactionErrorLog() {
        return this.transactionErrorLog;
    }

    public void setTransactionErrorLog(String transactionErrorLog) {
        this.transactionErrorLog = transactionErrorLog;
    }

    public String getSqlIsOutTime() {
        return this.sqlIsOutTime;
    }

    public void setSqlIsOutTime(String sqlIsOutTime) {
        this.sqlIsOutTime = sqlIsOutTime;
    }

    public String getSqlStatus() {
        return this.sqlStatus;
    }

    public void setSqlStatus(String sqlStatus) {
        this.sqlStatus = sqlStatus;
    }

    public Long getSqlTimeOut() {
        return this.sqlTimeOut;
    }

    public void setSqlTimeOut(Long sqlTimeOut) {
        this.sqlTimeOut = sqlTimeOut;
    }

    public Long getResultNum() {
        return this.resultNum;
    }

    public void setResultNum(Long resultNum) {
        this.resultNum = resultNum;
    }

    public Long getDbId() {
        return this.dbId;
    }

    public void setDbId(Long dbId) {
        this.dbId = dbId;
    }

    public String getDataFrom() {
        return this.dataFrom;
    }

    public void setDataFrom(String dataFrom) {
        this.dataFrom = dataFrom;
    }
}


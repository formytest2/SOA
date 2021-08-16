package com.github.bluecatlee.gs4d.transaction.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RedisTransactionModel implements Serializable {
    private static final long serialVersionUID = 1L;

    private Date transactionStartDtme;

    private String sourceDb;

    private Long transactionId;

    private Long transactionSqlId;

    private List<SqlParamModel> sqlParamModel = new ArrayList<>();

    private Long transactionOutTimeSecond;

    private Integer status;

    public Date getTransactionStartDtme() {
        return this.transactionStartDtme;
    }

    public void setTransactionStartDtme(Date transactionStartDtme) {
        this.transactionStartDtme = transactionStartDtme;
    }

    public String getSourceDb() {
        return this.sourceDb;
    }

    public void setSourceDb(String sourceDb) {
        this.sourceDb = sourceDb;
    }

    public Long getTransactionId() {
        return this.transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public Long getTransactionSqlId() {
        return this.transactionSqlId;
    }

    public void setTransactionSqlId(Long transactionSqlId) {
        this.transactionSqlId = transactionSqlId;
    }

    public List<SqlParamModel> getSqlParamModel() {
        return this.sqlParamModel;
    }

    public void setSqlParamModel(List<SqlParamModel> sqlParamModel) {
        this.sqlParamModel = sqlParamModel;
    }

    public Long getTransactionOutTimeSecond() {
        return this.transactionOutTimeSecond;
    }

    public void setTransactionOutTimeSecond(Long transactionOutTimeSecond) {
        this.transactionOutTimeSecond = transactionOutTimeSecond;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String toString() {
        return "RedisTransactionModel [transactionStartDtme=" + this.transactionStartDtme + ", sourceDb=" + this.sourceDb + ", transactionId=" + this.transactionId + ", transactionSqlId=" + this.transactionSqlId + ", sqlParamModel=" + this.sqlParamModel + ", transactionOutTimeSecond=" + this.transactionOutTimeSecond + ", status=" + this.status + "]";
    }
}


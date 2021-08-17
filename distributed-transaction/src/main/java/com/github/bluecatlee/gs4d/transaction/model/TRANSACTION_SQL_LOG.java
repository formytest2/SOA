package com.github.bluecatlee.gs4d.transaction.model;

import java.io.Serializable;
import java.util.Date;

public class TRANSACTION_SQL_LOG implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long SERIES;
    private Long TRANSACTION_ID;
    private Long TRANSACTION_DB_ID;
    private String SOURCE_DB;
    private String TABLE_NAME;
    private Date COMMIT_SQL_DTME;
    private Date ROLLBACK_SQL_DTME;
    private String SQL;
    private String SQL_PARAM;
    private String TRANSACTION_SIGN;
    private String TRANSACTION_ERROR_LOG;
    private Integer SQL_LEVEL;
    private String SQL_IS_OUT_TIME;
    private String SQL_KEY_VALUE;
    private String SQL_ID;
    private String BIZ_REDIS_KEY;
    private String BIZ_REDIS_VALUE;
    private Integer SQL_TYPE;
    private Integer SQL_STATUS;
    private Long SQL_TIMEOUT;
    private Long RESULT_NUM;

    public Long getTRANSACTION_ID() {
        return this.TRANSACTION_ID;
    }

    public void setTRANSACTION_ID(Long TRANSACTION_ID) {
        this.TRANSACTION_ID = TRANSACTION_ID;
    }

    public Long getTRANSACTION_DB_ID() {
        return this.TRANSACTION_DB_ID;
    }

    public void setTRANSACTION_DB_ID(Long TRANSACTION_DB_ID) {
        this.TRANSACTION_DB_ID = TRANSACTION_DB_ID;
    }

    public String getSOURCE_DB() {
        return this.SOURCE_DB;
    }

    public void setSOURCE_DB(String SOURCE_DB) {
        this.SOURCE_DB = SOURCE_DB;
    }

    public String getTABLE_NAME() {
        return this.TABLE_NAME;
    }

    public void setTABLE_NAME(String TABLE_NAME) {
        this.TABLE_NAME = TABLE_NAME;
    }

    public Date getCOMMIT_SQL_DTME() {
        return this.COMMIT_SQL_DTME;
    }

    public void setCOMMIT_SQL_DTME(Date COMMIT_SQL_DTME) {
        this.COMMIT_SQL_DTME = COMMIT_SQL_DTME;
    }

    public Date getROLLBACK_SQL_DTME() {
        return this.ROLLBACK_SQL_DTME;
    }

    public void setROLLBACK_SQL_DTME(Date ROLLBACK_SQL_DTME) {
        this.ROLLBACK_SQL_DTME = ROLLBACK_SQL_DTME;
    }

    public String getSQL() {
        return this.SQL;
    }

    public void setSQL(String SQL) {
        this.SQL = SQL;
    }

    public String getSQL_PARAM() {
        return this.SQL_PARAM;
    }

    public void setSQL_PARAM(String SQL_PARAM) {
        this.SQL_PARAM = SQL_PARAM;
    }

    public String getTRANSACTION_SIGN() {
        return this.TRANSACTION_SIGN;
    }

    public void setTRANSACTION_SIGN(String TRANSACTION_SIGN) {
        this.TRANSACTION_SIGN = TRANSACTION_SIGN;
    }

    public String getTRANSACTION_ERROR_LOG() {
        return this.TRANSACTION_ERROR_LOG;
    }

    public void setTRANSACTION_ERROR_LOG(String TRANSACTION_ERROR_LOG) {
        this.TRANSACTION_ERROR_LOG = TRANSACTION_ERROR_LOG;
    }

    public Long getSERIES() {
        return this.SERIES;
    }

    public void setSERIES(Long SERIES) {
        this.SERIES = SERIES;
    }

    public Integer getSQL_LEVEL() {
        return this.SQL_LEVEL;
    }

    public void setSQL_LEVEL(Integer SQL_LEVEL) {
        this.SQL_LEVEL = SQL_LEVEL;
    }

    public String getSQL_IS_OUT_TIME() {
        return this.SQL_IS_OUT_TIME;
    }

    public void setSQL_IS_OUT_TIME(String SQL_IS_OUT_TIME) {
        this.SQL_IS_OUT_TIME = SQL_IS_OUT_TIME;
    }

    public String getSQL_KEY_VALUE() {
        return this.SQL_KEY_VALUE;
    }

    public void setSQL_KEY_VALUE(String SQL_KEY_VALUE) {
        this.SQL_KEY_VALUE = SQL_KEY_VALUE;
    }

    public String getSQL_ID() {
        return this.SQL_ID;
    }

    public void setSQL_ID(String SQL_ID) {
        this.SQL_ID = SQL_ID;
    }

    public String getBIZ_REDIS_KEY() {
        return this.BIZ_REDIS_KEY;
    }

    public void setBIZ_REDIS_KEY(String BIZ_REDIS_KEY) {
        this.BIZ_REDIS_KEY = BIZ_REDIS_KEY;
    }

    public Integer getSQL_TYPE() {
        return this.SQL_TYPE;
    }

    public void setSQL_TYPE(Integer SQL_TYPE) {
        this.SQL_TYPE = SQL_TYPE;
    }

    public Integer getSQL_STATUS() {
        return this.SQL_STATUS;
    }

    public void setSQL_STATUS(Integer SQL_STATUS) {
        this.SQL_STATUS = SQL_STATUS;
    }

    public String getBIZ_REDIS_VALUE() {
        return this.BIZ_REDIS_VALUE;
    }

    public void setBIZ_REDIS_VALUE(String BIZ_REDIS_VALUE) {
        this.BIZ_REDIS_VALUE = BIZ_REDIS_VALUE;
    }

    public Long getSQL_TIMEOUT() {
        return this.SQL_TIMEOUT;
    }

    public void setSQL_TIMEOUT(Long SQL_TIMEOUT) {
        this.SQL_TIMEOUT = SQL_TIMEOUT;
    }

    public Long getRESULT_NUM() {
        return this.RESULT_NUM;
    }

    public void setRESULT_NUM(Long RESULT_NUM) {
        this.RESULT_NUM = RESULT_NUM;
    }
}


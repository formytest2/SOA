package com.github.bluecatlee.gs4d.export.entity;

import java.util.Date;

public class COMMON_QUERY {
    private Long SERIES;
    private String SQL_NAME;
    private String SQL_ID;
    private String SQL_CONTENT;
    private String PARAM_CONTENT;
    private String JDBC_NAME;
    private Date CREATE_DTME;
    private Date LAST_UPDTME;
    private String CREATE_USER_ID;
    private String LAST_UPDATE_USER_ID;
    private String CANCEL_SIGN;
    private Long TENANT_NUM_ID;
    private Long DATA_SIGN;
    private String DB_TYPE;

    public COMMON_QUERY() {
    }

    public Long getSERIES() {
        return this.SERIES;
    }

    public void setSERIES(Long SERIES) {
        this.SERIES = SERIES;
    }

    public String getSQL_NAME() {
        return this.SQL_NAME;
    }

    public void setSQL_NAME(String SQL_NAME) {
        this.SQL_NAME = SQL_NAME;
    }

    public String getSQL_ID() {
        return this.SQL_ID;
    }

    public void setSQL_ID(String SQL_ID) {
        this.SQL_ID = SQL_ID;
    }

    public String getSQL_CONTENT() {
        return this.SQL_CONTENT;
    }

    public void setSQL_CONTENT(String SQL_CONTENT) {
        this.SQL_CONTENT = SQL_CONTENT;
    }

    public String getPARAM_CONTENT() {
        return this.PARAM_CONTENT;
    }

    public void setPARAM_CONTENT(String PARAM_CONTENT) {
        this.PARAM_CONTENT = PARAM_CONTENT;
    }

    public String getJDBC_NAME() {
        return this.JDBC_NAME;
    }

    public void setJDBC_NAME(String JDBC_NAME) {
        this.JDBC_NAME = JDBC_NAME;
    }

    public Date getCREATE_DTME() {
        return this.CREATE_DTME;
    }

    public void setCREATE_DTME(Date CREATE_DTME) {
        this.CREATE_DTME = CREATE_DTME;
    }

    public Date getLAST_UPDTME() {
        return this.LAST_UPDTME;
    }

    public void setLAST_UPDTME(Date LAST_UPDTME) {
        this.LAST_UPDTME = LAST_UPDTME;
    }

    public String getCREATE_USER_ID() {
        return this.CREATE_USER_ID;
    }

    public void setCREATE_USER_ID(String CREATE_USER_ID) {
        this.CREATE_USER_ID = CREATE_USER_ID;
    }

    public String getLAST_UPDATE_USER_ID() {
        return this.LAST_UPDATE_USER_ID;
    }

    public void setLAST_UPDATE_USER_ID(String LAST_UPDATE_USER_ID) {
        this.LAST_UPDATE_USER_ID = LAST_UPDATE_USER_ID;
    }

    public String getCANCEL_SIGN() {
        return this.CANCEL_SIGN;
    }

    public void setCANCEL_SIGN(String CANCEL_SIGN) {
        this.CANCEL_SIGN = CANCEL_SIGN;
    }

    public Long getTENANT_NUM_ID() {
        return this.TENANT_NUM_ID;
    }

    public void setTENANT_NUM_ID(Long TENANT_NUM_ID) {
        this.TENANT_NUM_ID = TENANT_NUM_ID;
    }

    public Long getDATA_SIGN() {
        return this.DATA_SIGN;
    }

    public void setDATA_SIGN(Long DATA_SIGN) {
        this.DATA_SIGN = DATA_SIGN;
    }

    public String getDB_TYPE() {
        return this.DB_TYPE;
    }

    public void setDB_TYPE(String DB_TYPE) {
        this.DB_TYPE = DB_TYPE;
    }
}

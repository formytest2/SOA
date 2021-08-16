package com.github.bluecatlee.gs4d.cache.entity;

import java.util.Date;

public class EcCacheMethodSchemaDefine {
    private String SERIES;
    private Long TENANT_NUM_ID;
    private Long DATA_SIGN;
    private String SUB_SYSTEM;
    private String METHOD_NAME;
    private String SQL_CONTENT;
    private String DB;
    private String CACHE_METHOD;
    private String CACHE_MULTI_COL;
    private Long TTL;
    private Long LIST_SIGN;
    private Long ALLOW_LIST_EMPTY_SIGN;
    private Date CREATE_DTME;
    private Date LAST_UPDTME;
    private Long CREATE_USER_ID;
    private Long LAST_UPDATE_USER_ID;
    private String DESCRIPTION;

    public EcCacheMethodSchemaDefine() {
    }

    public String getSERIES() {
        return this.SERIES;
    }

    public void setSERIES(String SERIES) {
        this.SERIES = SERIES;
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

    public String getSUB_SYSTEM() {
        return this.SUB_SYSTEM;
    }

    public void setSUB_SYSTEM(String SUB_SYSTEM) {
        this.SUB_SYSTEM = SUB_SYSTEM;
    }

    public String getMETHOD_NAME() {
        return this.METHOD_NAME;
    }

    public void setMETHOD_NAME(String METHOD_NAME) {
        this.METHOD_NAME = METHOD_NAME;
    }

    public String getSQL_CONTENT() {
        return this.SQL_CONTENT;
    }

    public void setSQL_CONTENT(String SQL_CONTENT) {
        this.SQL_CONTENT = SQL_CONTENT;
    }

    public String getDB() {
        return this.DB;
    }

    public void setDB(String DB) {
        this.DB = DB;
    }

    public String getCACHE_METHOD() {
        return this.CACHE_METHOD;
    }

    public void setCACHE_METHOD(String CACHE_METHOD) {
        this.CACHE_METHOD = CACHE_METHOD;
    }

    public String getCACHE_MULTI_COL() {
        return this.CACHE_MULTI_COL;
    }

    public void setCACHE_MULTI_COL(String CACHE_MULTI_COL) {
        this.CACHE_MULTI_COL = CACHE_MULTI_COL;
    }

    public Long getTTL() {
        return this.TTL;
    }

    public void setTTL(Long TTL) {
        this.TTL = TTL;
    }

    public Long getLIST_SIGN() {
        return this.LIST_SIGN;
    }

    public void setLIST_SIGN(Long LIST_SIGN) {
        this.LIST_SIGN = LIST_SIGN;
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

    public Long getCREATE_USER_ID() {
        return this.CREATE_USER_ID;
    }

    public void setCREATE_USER_ID(Long CREATE_USER_ID) {
        this.CREATE_USER_ID = CREATE_USER_ID;
    }

    public Long getLAST_UPDATE_USER_ID() {
        return this.LAST_UPDATE_USER_ID;
    }

    public void setLAST_UPDATE_USER_ID(Long LAST_UPDATE_USER_ID) {
        this.LAST_UPDATE_USER_ID = LAST_UPDATE_USER_ID;
    }

    public String getDESCRIPTION() {
        return this.DESCRIPTION;
    }

    public void setDESCRIPTION(String DESCRIPTION) {
        this.DESCRIPTION = DESCRIPTION;
    }

    public Long getALLOW_LIST_EMPTY_SIGN() {
        return this.ALLOW_LIST_EMPTY_SIGN;
    }

    public void setALLOW_LIST_EMPTY_SIGN(Long ALLOW_LIST_EMPTY_SIGN) {
        this.ALLOW_LIST_EMPTY_SIGN = ALLOW_LIST_EMPTY_SIGN;
    }
}


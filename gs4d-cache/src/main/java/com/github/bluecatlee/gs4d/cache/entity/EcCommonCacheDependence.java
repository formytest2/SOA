package com.github.bluecatlee.gs4d.cache.entity;

import java.util.Date;

public class EcCommonCacheDependence {
    private String SERIES;
    private Long TENANT_NUM_ID;
    private Long DATA_SIGN;
    private String METHOD_NAME;
    private String DB;
    private String CACHE_KEY;
    private String PARAMS;
    private String TABLE_NAME;
    private String TABLE_SERIES;
    private Date CREATE_DTME;
    private Date LAST_UPDTME;
    private Long CREATE_USER_ID;
    private Long LAST_UPDATE_USER_ID;
    private String DUBBO_GROUP;

    public EcCommonCacheDependence() {
    }

    public String getDUBBO_GROUP() {
        return this.DUBBO_GROUP;
    }

    public void setDUBBO_GROUP(String DUBBO_GROUP) {
        this.DUBBO_GROUP = DUBBO_GROUP;
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

    public String getMETHOD_NAME() {
        return this.METHOD_NAME;
    }

    public void setMETHOD_NAME(String METHOD_NAME) {
        this.METHOD_NAME = METHOD_NAME;
    }

    public String getDB() {
        return this.DB;
    }

    public void setDB(String DB) {
        this.DB = DB;
    }

    public String getCACHE_KEY() {
        return this.CACHE_KEY;
    }

    public void setCACHE_KEY(String CACHE_KEY) {
        this.CACHE_KEY = CACHE_KEY;
    }

    public String getPARAMS() {
        return this.PARAMS;
    }

    public void setPARAMS(String PARAMS) {
        this.PARAMS = PARAMS;
    }

    public String getTABLE_NAME() {
        return this.TABLE_NAME;
    }

    public void setTABLE_NAME(String TABLE_NAME) {
        this.TABLE_NAME = TABLE_NAME;
    }

    public String getTABLE_SERIES() {
        return this.TABLE_SERIES;
    }

    public void setTABLE_SERIES(String TABLE_SERIES) {
        this.TABLE_SERIES = TABLE_SERIES;
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
}

package com.github.bluecatlee.gs4d.message.consumer.model;

import java.util.Date;

public class SysMailSendReq {
    private Long SERIES;
    private String SEND_MAIL;
    private String CC_MAIL;
    private String CONTENT;
    private String TITLE;
    private Long STATUS;
    private Date CREATEDTME;
    private Date LASTUPDTME;
    private Long TENANT_NUM_ID;
    private Long DATA_SIGN;

    public Long getSERIES() {
        return this.SERIES;
    }

    public void setSERIES(Long SERIES) {
        this.SERIES = SERIES;
    }

    public String getSEND_MAIL() {
        return this.SEND_MAIL;
    }

    public void setSEND_MAIL(String SEND_MAIL) {
        this.SEND_MAIL = SEND_MAIL;
    }

    public String getCC_MAIL() {
        return this.CC_MAIL;
    }

    public void setCC_MAIL(String CC_MAIL) {
        this.CC_MAIL = CC_MAIL;
    }

    public String getCONTENT() {
        return this.CONTENT;
    }

    public void setCONTENT(String CONTENT) {
        this.CONTENT = CONTENT;
    }

    public String getTITLE() {
        return this.TITLE;
    }

    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }

    public Long getSTATUS() {
        return this.STATUS;
    }

    public void setSTATUS(Long STATUS) {
        this.STATUS = STATUS;
    }

    public Date getCREATEDTME() {
        return this.CREATEDTME;
    }

    public void setCREATEDTME(Date CREATEDTME) {
        this.CREATEDTME = CREATEDTME;
    }

    public Date getLASTUPDTME() {
        return this.LASTUPDTME;
    }

    public void setLASTUPDTME(Date LASTUPDTME) {
        this.LASTUPDTME = LASTUPDTME;
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
}


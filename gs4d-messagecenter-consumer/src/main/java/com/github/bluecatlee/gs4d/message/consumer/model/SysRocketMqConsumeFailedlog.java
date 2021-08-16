package com.github.bluecatlee.gs4d.message.consumer.model;

import java.sql.Date;

public class SysRocketMqConsumeFailedlog {
    private Long MSG_SERIES;
    private Long TYPE_ID;
    private Date CREATE_DTME;
    private String ALREADY_DEAL;
    private Long TENANT_NUM_ID;
    private Long DATA_SIGN;

    public Long getMSG_SERIES() {
        return this.MSG_SERIES;
    }

    public void setMSG_SERIES(Long MSG_SERIES) {
        this.MSG_SERIES = MSG_SERIES;
    }

    public Long getTYPE_ID() {
        return this.TYPE_ID;
    }

    public void setTYPE_ID(Long TYPE_ID) {
        this.TYPE_ID = TYPE_ID;
    }

    public Date getCREATE_DTME() {
        return this.CREATE_DTME;
    }

    public void setCREATE_DTME(Date CREATE_DTME) {
        this.CREATE_DTME = CREATE_DTME;
    }

    public String getALREADY_DEAL() {
        return this.ALREADY_DEAL;
    }

    public void setALREADY_DEAL(String ALREADY_DEAL) {
        this.ALREADY_DEAL = ALREADY_DEAL;
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


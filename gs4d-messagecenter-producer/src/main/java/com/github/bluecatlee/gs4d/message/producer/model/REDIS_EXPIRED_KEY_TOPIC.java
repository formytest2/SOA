package com.github.bluecatlee.gs4d.message.producer.model;

import java.util.Date;

public class REDIS_EXPIRED_KEY_TOPIC {

    private Long SERIES;
    private String REDIS_KEY_HEAD;
    private String TOPIC;
    private String TAG;
    private String CANCELSIGN;
    private String REMARK;
    private String CREATE_USER;
    private Date CREATE_DTME;
    private Date UPDATE_DTME;
    private String EMAIL;
    private Long NOTICE_TIME_INTERVAL;

    public Long getSERIES() {
        return this.SERIES;
    }

    public void setSERIES(Long SERIES) {
        this.SERIES = SERIES;
    }

    public String getREDIS_KEY_HEAD() {
        return this.REDIS_KEY_HEAD;
    }

    public void setREDIS_KEY_HEAD(String REDIS_KEY_HEAD) {
        this.REDIS_KEY_HEAD = REDIS_KEY_HEAD;
    }

    public String getTOPIC() {
        return this.TOPIC;
    }

    public void setTOPIC(String TOPIC) {
        this.TOPIC = TOPIC;
    }

    public String getTAG() {
        return this.TAG;
    }

    public void setTAG(String TAG) {
        this.TAG = TAG;
    }

    public String getCANCELSIGN() {
        return this.CANCELSIGN;
    }

    public void setCANCELSIGN(String CANCELSIGN) {
        this.CANCELSIGN = CANCELSIGN;
    }

    public String getREMARK() {
        return this.REMARK;
    }

    public void setREMARK(String REMARK) {
        this.REMARK = REMARK;
    }

    public String getCREATE_USER() {
        return this.CREATE_USER;
    }

    public void setCREATE_USER(String CREATE_USER) {
        this.CREATE_USER = CREATE_USER;
    }

    public Date getCREATE_DTME() {
        return this.CREATE_DTME;
    }

    public void setCREATE_DTME(Date CREATE_DTME) {
        this.CREATE_DTME = CREATE_DTME;
    }

    public Date getUPDATE_DTME() {
        return this.UPDATE_DTME;
    }

    public void setUPDATE_DTME(Date UPDATE_DTME) {
        this.UPDATE_DTME = UPDATE_DTME;
    }

    public String getEMAIL() {
        return this.EMAIL;
    }

    public void setEMAIL(String EMAIL) {
        this.EMAIL = EMAIL;
    }

    public Long getNOTICE_TIME_INTERVAL() {
        return this.NOTICE_TIME_INTERVAL;
    }

    public void setNOTICE_TIME_INTERVAL(Long NOTICE_TIME_INTERVAL) {
        this.NOTICE_TIME_INTERVAL = NOTICE_TIME_INTERVAL;
    }
}

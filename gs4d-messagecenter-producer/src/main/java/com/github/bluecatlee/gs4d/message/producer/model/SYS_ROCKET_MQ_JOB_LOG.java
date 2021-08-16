package com.github.bluecatlee.gs4d.message.producer.model;

import java.util.Date;

public class SYS_ROCKET_MQ_JOB_LOG {

    private Long SERIES;
    private String MESSAGE_KEY = "";
    private String MESSAGE_DETAIL;
    private String MESSAGE_TOPIC = "";
    private String MESSAGE_TAG = "";
    private String CRON = "";
    private Date CREATE_DTME;
    private long LAST_UPDTME;
    private long TENANT_NUM_ID = 0L;
    private Long DATA_SIGN = Long.valueOf(0L);
    private String CLIENT_IP;
    private String CANCELSIGN;
    private String ERROR_LOG;
    private Long SEND_LOG_SERIES;

    public Long getSERIES() {
        return this.SERIES;
    }

    public void setSERIES(long SERIES) {
        this.SERIES = Long.valueOf(SERIES);
    }

    public String getMESSAGE_KEY() {
        return this.MESSAGE_KEY;
    }

    public void setMESSAGE_KEY(String MESSAGE_KEY) {
        this.MESSAGE_KEY = MESSAGE_KEY;
    }

    public String getMESSAGE_DETAIL() {
        return this.MESSAGE_DETAIL;
    }

    public void setMESSAGE_DETAIL(String MESSAGE_DETAIL) {
        this.MESSAGE_DETAIL = MESSAGE_DETAIL;
    }

    public String getMESSAGE_TOPIC() {
        return this.MESSAGE_TOPIC;
    }

    public void setMESSAGE_TOPIC(String MESSAGE_TOPIC) {
        this.MESSAGE_TOPIC = MESSAGE_TOPIC;
    }

    public String getMESSAGE_TAG() {
        return this.MESSAGE_TAG;
    }

    public void setMESSAGE_TAG(String MESSAGE_TAG) {
        this.MESSAGE_TAG = MESSAGE_TAG;
    }

    public String getCRON() {
        return this.CRON;
    }

    public void setCRON(String CRON) {
        this.CRON = CRON;
    }

    public Date getCREATE_DTME() {
        return this.CREATE_DTME;
    }

    public void setCREATE_DTME(Date CREATE_DTME) {
        this.CREATE_DTME = CREATE_DTME;
    }

    public long getLAST_UPDTME() {
        return this.LAST_UPDTME;
    }

    public void setLAST_UPDTME(long LAST_UPDTME) {
        this.LAST_UPDTME = LAST_UPDTME;
    }

    public long getTENANT_NUM_ID() {
        return this.TENANT_NUM_ID;
    }

    public void setTENANT_NUM_ID(long TENANT_NUM_ID) {
        this.TENANT_NUM_ID = TENANT_NUM_ID;
    }

    public Long getDATA_SIGN() {
        return this.DATA_SIGN;
    }

    public void setDATA_SIGN(Long DATA_SIGN) {
        this.DATA_SIGN = DATA_SIGN;
    }

    public String getCLIENT_IP() {
        return this.CLIENT_IP;
    }

    public void setCLIENT_IP(String CLIENT_IP) {
        this.CLIENT_IP = CLIENT_IP;
    }

    public String getCANCELSIGN() {
        return this.CANCELSIGN;
    }

    public void setCANCELSIGN(String CANCELSIGN) {
        this.CANCELSIGN = CANCELSIGN;
    }

    public String getERROR_LOG() {
        return this.ERROR_LOG;
    }

    public void setERROR_LOG(String ERROR_LOG) {
        this.ERROR_LOG = ERROR_LOG;
    }

    public Long getSEND_LOG_SERIES() {
        return this.SEND_LOG_SERIES;
    }

    public void setSEND_LOG_SERIES(Long SEND_LOG_SERIES) {
        this.SEND_LOG_SERIES = SEND_LOG_SERIES;
    }

    public void setSERIES(Long SERIES) {
        this.SERIES = SERIES;
    }
}


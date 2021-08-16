package com.github.bluecatlee.gs4d.message.producer.model;

import java.util.Date;

public class SYS_ORDER_MESSAGE_SEND_LOG {

    private Long SERIES;
    private String MESSAGE_KEY;
    private String MESSAGE_BODY;
    private String MESSAGE_TOPIC;
    private String MESSAGE_TAG;
    private Long TENANT_NUM_ID;
    private Long DATA_SIGN;
    private String FROM_SYSTEM;
    private Long ORDER_MESSAGE_GROUP_ID;
    private Integer ORDER_ID;
    private String CLIENT_IP;
    private Date CREATE_DTME;

    public Long getSERIES() {
        return this.SERIES;
    }

    public void setSERIES(Long SERIES) {
        this.SERIES = SERIES;
    }

    public String getMESSAGE_KEY() {
        return this.MESSAGE_KEY;
    }

    public void setMESSAGE_KEY(String MESSAGE_KEY) {
        this.MESSAGE_KEY = MESSAGE_KEY;
    }

    public String getMESSAGE_BODY() {
        return this.MESSAGE_BODY;
    }

    public void setMESSAGE_BODY(String MESSAGE_BODY) {
        this.MESSAGE_BODY = MESSAGE_BODY;
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

    public String getFROM_SYSTEM() {
        return this.FROM_SYSTEM;
    }

    public void setFROM_SYSTEM(String FROM_SYSTEM) {
        this.FROM_SYSTEM = FROM_SYSTEM;
    }

    public Long getORDER_MESSAGE_GROUP_ID() {
        return this.ORDER_MESSAGE_GROUP_ID;
    }

    public void setORDER_MESSAGE_GROUP_ID(Long ORDER_MESSAGE_GROUP_ID) {
        this.ORDER_MESSAGE_GROUP_ID = ORDER_MESSAGE_GROUP_ID;
    }

    public Integer getORDER_ID() {
        return this.ORDER_ID;
    }

    public void setORDER_ID(Integer ORDER_ID) {
        this.ORDER_ID = ORDER_ID;
    }

    public String getCLIENT_IP() {
        return this.CLIENT_IP;
    }

    public void setCLIENT_IP(String CLIENT_IP) {
        this.CLIENT_IP = CLIENT_IP;
    }

    public Date getCREATE_DTME() {
            return this.CREATE_DTME;
    }

    public void setCREATE_DTME(Date CREATE_DTME) {
        this.CREATE_DTME = CREATE_DTME;
    }
}

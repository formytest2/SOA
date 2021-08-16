package com.github.bluecatlee.gs4d.message.consumer.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PlatformMqDubboConsumer implements Serializable {
    private static final long serialVersionUID = 6205519264099514741L;

    static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private Long SERIES = 0L;
    private String SERVICE_NAME = null;
    private String METHOD_NAME = null;
    private String PARAM_ENTITY = null;
    private String CREATE_USER_NAME = null;
    private String CREATE_DATE_TIME;
    private String ZK_ADR_TEST;
    private String ZK_ADR_DEVELOP;
    private String ZK_ADR;
    private String DIRECT_ADR;
    private String VERSION;
    private Integer RETRIES;
    private String DUBBO_GROUP;
    private String REMARK;

    public PlatformMqDubboConsumer() {
        this.CREATE_DATE_TIME = dateFormat.format(new Date());
        this.ZK_ADR_TEST = null;
        this.ZK_ADR_DEVELOP = null;
        this.ZK_ADR = null;
        this.DIRECT_ADR = null;
        this.VERSION = null;
    }

    public String getVERSION() {
        return this.VERSION;
    }

    public void setVERSION(String VERSION) {
        this.VERSION = VERSION;
    }

    public void setSERIES(Long SERIES) {
        this.SERIES = SERIES;
    }

    public Long getSERIES() {
        return this.SERIES;
    }

    public void setSERVICE_NAME(String SERVICE_NAME) {
        this.SERVICE_NAME = SERVICE_NAME;
    }

    public String getSERVICE_NAME() {
        return this.SERVICE_NAME;
    }

    public void setMETHOD_NAME(String METHOD_NAME) {
        this.METHOD_NAME = METHOD_NAME;
    }

    public String getMETHOD_NAME() {
        return this.METHOD_NAME;
    }

    public void setPARAM_ENTITY(String PARAM_ENTITY) {
        this.PARAM_ENTITY = PARAM_ENTITY;
    }

    public String getPARAM_ENTITY() {
        return this.PARAM_ENTITY;
    }

    public void setCREATE_USER_NAME(String CREATE_USER_NAME) {
        this.CREATE_USER_NAME = CREATE_USER_NAME;
    }

    public String getCREATE_USER_NAME() {
        return this.CREATE_USER_NAME;
    }

    public void setCREATE_DATE_TIME(String CREATE_DATE_TIME) {
        this.CREATE_DATE_TIME = CREATE_DATE_TIME;
    }

    public String getCREATE_DATE_TIME() {
        return this.CREATE_DATE_TIME;
    }

    public void setZK_ADR_TEST(String ZK_ADR_TEST) {
        this.ZK_ADR_TEST = ZK_ADR_TEST;
    }

    public String getZK_ADR_DEVELOP() {
        return this.ZK_ADR_DEVELOP;
    }

    public void setZK_ADR_DEVELOP(String ZK_ADR_DEVELOP) {
        this.ZK_ADR_DEVELOP = ZK_ADR_DEVELOP;
    }

    public String getZK_ADR_TEST() {
        return this.ZK_ADR_TEST;
    }

    public void setZK_ADR(String ZK_ADR) {
        this.ZK_ADR = ZK_ADR;
    }

    public String getZK_ADR() {
        return this.ZK_ADR;
    }

    public String getDIRECT_ADR() {
        return this.DIRECT_ADR;
    }

    public void setDIRECT_ADR(String DIRECT_ADR) {
        this.DIRECT_ADR = DIRECT_ADR;
    }

    public Integer getRETRIES() {
        return this.RETRIES;
    }

    public void setRETRIES(Integer RETRIES) {
        this.RETRIES = RETRIES;
    }

    public String getDUBBO_GROUP() {
        return this.DUBBO_GROUP;
    }

    public void setDUBBO_GROUP(String DUBBO_GROUP) {
        this.DUBBO_GROUP = DUBBO_GROUP;
    }

    public String getREMARK() {
        return this.REMARK;
    }

    public void setREMARK(String REMARK) {
        this.REMARK = REMARK;
    }
}

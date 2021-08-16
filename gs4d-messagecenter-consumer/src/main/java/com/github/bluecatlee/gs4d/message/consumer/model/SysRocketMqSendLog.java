package com.github.bluecatlee.gs4d.message.consumer.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;

public class SysRocketMqSendLog implements Serializable {
    
    private static final long serialVersionUID = -3889635852067145114L;
    
    static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    private Long SERIES;
    private String MESSAGE_ID = "";
    private String MESSAGE_KEY = "";
    private String MESSAGE_TOPIC = "";
    private String MESSAGE_TAG = "";
    private String MESSAGE_BODY;
    private String MESSAGE_NAME_ADDR = "";
    private String PRODUCER_NAME = "";
    private String CREATE_DTME;
    private String LAST_UPDTME = "CURRENT_TIMESTAMP";
    private Long CREATE_USER_ID = 1L;
    private Long LAST_UPDATE_USER_ID = 1L;
    private String CANCELSIGN = "N";
    private String FAIL_DETAIL = "";
    private Long TENANT_NUM_ID = 0L;
    private Long DATA_SIGN = 0L;
    private Long WORKFLOW_ID = 0L;
    private Long INSTANCE_ID = 0L;
    private int STEP_ID = 0;
    private String FROM_SYSTEM = "";
    private int MSG_STATUS = 0;
    private Long ORDER_MESS_FLAG;
    private String RESPONSE_DETAIL;
    private Integer RETRY_TIMES;
    private String TASK_TARGET;
    private String CONSUMER_SUCCESS;
    private String CONSUMER_SUCCESS_TIME;
    private String NEXT_RETRY_INTERVAL;
    private String HAS_LOG = "Y";           // 默认记录日志
    private String CLIENT_IP = "";
    private String JOB_SEND_EMAIL = "N";
    private String NOW_TIME;
    private Long ORDER_SERIES;
    private String EXPRESS_FLAG = "N";

    public void setSERIES(Long SERIES) {
        this.SERIES = SERIES;
    }

    public Long getSERIES() {
        return this.SERIES;
    }

    public void setMESSAGE_ID(String MESSAGE_ID) {
        this.MESSAGE_ID = MESSAGE_ID;
    }

    public String getMESSAGE_ID() {
        return this.MESSAGE_ID;
    }

    public void setMESSAGE_KEY(String MESSAGE_KEY) {
        this.MESSAGE_KEY = MESSAGE_KEY;
    }

    public String getMESSAGE_KEY() {
        return this.MESSAGE_KEY;
    }

    public void setMESSAGE_TOPIC(String MESSAGE_TOPIC) {
        this.MESSAGE_TOPIC = MESSAGE_TOPIC;
    }

    public String getMESSAGE_TOPIC() {
        return this.MESSAGE_TOPIC;
    }

    public void setMESSAGE_TAG(String MESSAGE_TAG) {
        this.MESSAGE_TAG = MESSAGE_TAG;
    }

    public String getMESSAGE_TAG() {
        return this.MESSAGE_TAG;
    }

    public void setMESSAGE_BODY(String MESSAGE_BODY) {
        this.MESSAGE_BODY = MESSAGE_BODY;
    }

    public String getMESSAGE_BODY() {
        return this.MESSAGE_BODY;
    }

    public void setMESSAGE_NAME_ADDR(String MESSAGE_NAME_ADDR) {
        this.MESSAGE_NAME_ADDR = MESSAGE_NAME_ADDR;
    }

    public String getMESSAGE_NAME_ADDR() {
        return this.MESSAGE_NAME_ADDR;
    }

    public void setPRODUCER_NAME(String PRODUCER_NAME) {
        this.PRODUCER_NAME = PRODUCER_NAME;
    }

    public String getPRODUCER_NAME() {
        return this.PRODUCER_NAME;
    }

    public void setCREATE_DTME(String CREATE_DTME) {
        this.CREATE_DTME = CREATE_DTME;
    }

    public String getCREATE_DTME() {
        return this.CREATE_DTME;
    }

    public void setLAST_UPDTME(String LAST_UPDTME) {
        this.LAST_UPDTME = LAST_UPDTME;
    }

    public String getLAST_UPDTME() {
        return this.LAST_UPDTME;
    }

    public void setCREATE_USER_ID(Long CREATE_USER_ID) {
        this.CREATE_USER_ID = CREATE_USER_ID;
    }

    public Long getCREATE_USER_ID() {
        return this.CREATE_USER_ID;
    }

    public void setLAST_UPDATE_USER_ID(Long LAST_UPDATE_USER_ID) {
        this.LAST_UPDATE_USER_ID = LAST_UPDATE_USER_ID;
    }

    public Long getLAST_UPDATE_USER_ID() {
        return this.LAST_UPDATE_USER_ID;
    }

    public void setCANCELSIGN(String CANCELSIGN) {
        this.CANCELSIGN = CANCELSIGN;
    }

    public String getCANCELSIGN() {
        return this.CANCELSIGN;
    }

    public void setFAIL_DETAIL(String FAIL_DETAIL) {
        this.FAIL_DETAIL = FAIL_DETAIL;
    }

    public String getFAIL_DETAIL() {
        return this.FAIL_DETAIL;
    }

    public void setTENANT_NUM_ID(Long TENANT_NUM_ID) {
        this.TENANT_NUM_ID = TENANT_NUM_ID;
    }

    public Long getTENANT_NUM_ID() {
        return this.TENANT_NUM_ID;
    }

    public void setDATA_SIGN(Long DATA_SIGN) {
        this.DATA_SIGN = DATA_SIGN;
    }

    public Long getDATA_SIGN() {
        return this.DATA_SIGN;
    }

    public void setWORKFLOW_ID(Long WORKFLOW_ID) {
        this.WORKFLOW_ID = WORKFLOW_ID;
    }

    public Long getWORKFLOW_ID() {
        return this.WORKFLOW_ID;
    }

    public void setINSTANCE_ID(Long INSTANCE_ID) {
        this.INSTANCE_ID = INSTANCE_ID;
    }

    public Long getINSTANCE_ID() {
        return this.INSTANCE_ID;
    }

    public void setSTEP_ID(int STEP_ID) {
        this.STEP_ID = STEP_ID;
    }

    public int getSTEP_ID() {
        return this.STEP_ID;
    }

    public void setFROM_SYSTEM(String FROM_SYSTEM) {
        this.FROM_SYSTEM = FROM_SYSTEM;
    }

    public String getFROM_SYSTEM() {
        return this.FROM_SYSTEM;
    }

    public void setMSG_STATUS(int MSG_STATUS) {
        this.MSG_STATUS = MSG_STATUS;
    }

    public int getMSG_STATUS() {
        return this.MSG_STATUS;
    }

    public Long getORDER_MESS_FLAG() {
        return this.ORDER_MESS_FLAG;
    }

    public void setORDER_MESS_FLAG(Long ORDER_MESS_FLAG) {
        this.ORDER_MESS_FLAG = ORDER_MESS_FLAG;
    }

    public String getRESPONSE_DETAIL() {
        return this.RESPONSE_DETAIL;
    }

    public void setRESPONSE_DETAIL(String RESPONSE_DETAIL) {
        this.RESPONSE_DETAIL = RESPONSE_DETAIL;
    }

    public Integer getRETRY_TIMES() {
        return this.RETRY_TIMES;
    }

    public void setRETRY_TIMES(Integer RETRY_TIMES) {
        this.RETRY_TIMES = RETRY_TIMES;
    }

    public String getTASK_TARGET() {
        return this.TASK_TARGET;
    }

    public void setTASK_TARGET(String TASK_TARGET) {
        this.TASK_TARGET = TASK_TARGET;
    }

    public String getCONSUMER_SUCCESS() {
        return this.CONSUMER_SUCCESS;
    }

    public void setCONSUMER_SUCCESS(String CONSUMER_SUCCESS) {
        this.CONSUMER_SUCCESS = CONSUMER_SUCCESS;
    }

    public String getCONSUMER_SUCCESS_TIME() {
        return this.CONSUMER_SUCCESS_TIME;
    }

    public void setCONSUMER_SUCCESS_TIME(String CONSUMER_SUCCESS_TIME) {
        this.CONSUMER_SUCCESS_TIME = CONSUMER_SUCCESS_TIME;
    }

    public String getNEXT_RETRY_INTERVAL() {
        return this.NEXT_RETRY_INTERVAL;
    }

    public void setNEXT_RETRY_INTERVAL(String NEXT_RETRY_INTERVAL) {
        this.NEXT_RETRY_INTERVAL = NEXT_RETRY_INTERVAL;
    }

    public String getHAS_LOG() {
        return this.HAS_LOG;
    }

    public void setHAS_LOG(String HAS_LOG) {
        this.HAS_LOG = HAS_LOG;
    }

    public String getCLIENT_IP() {
        return this.CLIENT_IP;
    }

    public void setCLIENT_IP(String CLIENT_IP) {
        this.CLIENT_IP = CLIENT_IP;
    }

    public String getJOB_SEND_EMAIL() {
        return this.JOB_SEND_EMAIL;
    }

    public void setJOB_SEND_EMAIL(String JOB_SEND_EMAIL) {
        this.JOB_SEND_EMAIL = JOB_SEND_EMAIL;
    }

    public String getNOW_TIME() {
        return this.NOW_TIME;
    }

    public void setNOW_TIME(String NOW_TIME) {
        this.NOW_TIME = NOW_TIME;
    }

    public Long getORDER_SERIES() {
        return this.ORDER_SERIES;
    }

    public void setORDER_SERIES(Long ORDER_SERIES) {
        this.ORDER_SERIES = ORDER_SERIES;
    }

    public String getEXPRESS_FLAG() {
        return this.EXPRESS_FLAG;
    }

    public void setEXPRESS_FLAG(String EXPRESS_FLAG) {
        this.EXPRESS_FLAG = EXPRESS_FLAG;
    }
}

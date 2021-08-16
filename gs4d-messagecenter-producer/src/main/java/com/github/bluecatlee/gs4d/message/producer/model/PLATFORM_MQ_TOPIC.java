package com.github.bluecatlee.gs4d.message.producer.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PLATFORM_MQ_TOPIC {
    
    static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Long SERIES = Long.valueOf(0L);
    private Long MSG_ID = Long.valueOf(0L);
    private String TOPIC_ALIAS;
    private String TOPIC = null;
    private String TAG_ALIAS;
    private String TAG = "0";
    private String FROM_SYSTEM = null;
    private String CREATE_DATE_TIME;
    private String CREATE_USER_NAME;
    private String REMARK;
    private int CONSUMER_TYPE;
    private Long CONSUMER_SERIES;
    private int CONSUMER_INSTANCE_COUNT;
    private Long CONSUMER_INTERVAL;
    private Integer RETRIES;
    private Integer RETRIES_TEST;
    private Integer RETRIES_DEVELOP;
    private String TASK_TARGET;
    private String IS_DISTINCT;
    private Integer MESS_BATCH_NUM;
    private Integer CONSUMER_THREAD_MIN;
    private Integer CONSUMER_THREAD_MAX;
    private Long TENANT_NUM_ID;
    private String WETHER_ORDER_MESS;
    private String MESS_TRANS_GROUP;
    private String RETRY_INTERVAL;
    private String CANCELSIGN;
    private Long SYSTEM_NUM_ID;
    private String WETHER_INSERTDB;
    private String CORRECT_CODES;
    private Integer MQ_QUEUE;
    private String WETHER_HANDLE_FAILEDMESS;
    private Integer RETRY_MAX;
    private Integer ZK_DATA_SIGN;
    private String MQ_NAMESRV;
    private String CONSUMER_IP;
    private String DATASOURCE_NAME;

    public PLATFORM_MQ_TOPIC() {
        this.CREATE_DATE_TIME = formatter.format(new Date());
        this.CREATE_USER_NAME = null;
        this.REMARK = null;
        this.CONSUMER_TYPE = 0;
        this.CONSUMER_SERIES = Long.valueOf(0L);
        this.CONSUMER_INSTANCE_COUNT = 1;
        this.CONSUMER_INTERVAL = Long.valueOf(0L);
    }

    public void setSERIES(Long SERIES) {
        this.SERIES = SERIES;
    }

    public Long getSERIES() {
        return this.SERIES;
    }

    public void setMSG_ID(Long MSG_ID) {
        this.MSG_ID = MSG_ID;
    }

    public Long getMSG_ID() {
        return this.MSG_ID;
    }

    public void setTOPIC(String TOPIC) {
            this.TOPIC = TOPIC;
    }

    public String getTOPIC() {
        return this.TOPIC;
    }

    public void setTAG(String TAG) {
        this.TAG = TAG;
    }

    public String getTAG() {
        return this.TAG;
    }

    public void setFROM_SYSTEM(String FROM_SYSTEM) {
        this.FROM_SYSTEM = FROM_SYSTEM;
    }

    public String getFROM_SYSTEM() {
        return this.FROM_SYSTEM;
    }

    public void setCREATE_DATE_TIME(String CREATE_DATE_TIME) {
        this.CREATE_DATE_TIME = CREATE_DATE_TIME;
    }

    public String getCREATE_DATE_TIME() {
        return this.CREATE_DATE_TIME;
    }

    public void setCREATE_USER_NAME(String CREATE_USER_NAME) {
        this.CREATE_USER_NAME = CREATE_USER_NAME;
    }

    public String getCREATE_USER_NAME() {
        return this.CREATE_USER_NAME;
    }

    public void setREMARK(String REMARK) {
        this.REMARK = REMARK;
    }

    public String getREMARK() {
        return this.REMARK;
    }

    public void setCONSUMER_TYPE(int CONSUMER_TYPE) {
        this.CONSUMER_TYPE = CONSUMER_TYPE;
    }

    public int getCONSUMER_TYPE() {
        return this.CONSUMER_TYPE;
    }

    public void setCONSUMER_SERIES(Long CONSUMER_SERIES) {
        this.CONSUMER_SERIES = CONSUMER_SERIES;
    }

    public Long getCONSUMER_SERIES() {
        return this.CONSUMER_SERIES;
    }

    public void setCONSUMER_INSTANCE_COUNT(int CONSUMER_INSTANCE_COUNT) {
        this.CONSUMER_INSTANCE_COUNT = CONSUMER_INSTANCE_COUNT;
    }

    public int getCONSUMER_INSTANCE_COUNT() {
        return this.CONSUMER_INSTANCE_COUNT;
    }

    public Long getCONSUMER_INTERVAL() {
        return this.CONSUMER_INTERVAL;
    }

    public void setCONSUMER_INTERVAL(Long CONSUMER_INTERVAL) {
        this.CONSUMER_INTERVAL = CONSUMER_INTERVAL;
    }

    public Integer getRETRIES() {
        return this.RETRIES;
    }

    public void setRETRIES(Integer RETRIES) {
        this.RETRIES = RETRIES;
    }

    public Integer getRETRIES_TEST() {
        return this.RETRIES_TEST;
    }

    public void setRETRIES_TEST(Integer RETRIES_TEST) {
        this.RETRIES_TEST = RETRIES_TEST;
    }

    public Integer getRETRIES_DEVELOP() {
        return this.RETRIES_DEVELOP;
    }

    public void setRETRIES_DEVELOP(Integer RETRIES_DEVELOP) {
        this.RETRIES_DEVELOP = RETRIES_DEVELOP;
    }

    public String getTASK_TARGET() {
        return this.TASK_TARGET;
    }

    public void setTASK_TARGET(String TASK_TARGET) {
        this.TASK_TARGET = TASK_TARGET;
    }

    public String getIS_DISTINCT() {
        return this.IS_DISTINCT;
    }

    public void setIS_DISTINCT(String IS_DISTINCT) {
        this.IS_DISTINCT = IS_DISTINCT;
    }

    public Integer getMESS_BATCH_NUM() {
        return this.MESS_BATCH_NUM;
    }

    public void setMESS_BATCH_NUM(Integer MESS_BATCH_NUM) {
        this.MESS_BATCH_NUM = MESS_BATCH_NUM;
    }

    public Integer getCONSUMER_THREAD_MIN() {
        return this.CONSUMER_THREAD_MIN;
    }

    public void setCONSUMER_THREAD_MIN(Integer CONSUMER_THREAD_MIN) {
        this.CONSUMER_THREAD_MIN = CONSUMER_THREAD_MIN;
    }

    public Integer getCONSUMER_THREAD_MAX() {
        return this.CONSUMER_THREAD_MAX;
    }

    public void setCONSUMER_THREAD_MAX(Integer CONSUMER_THREAD_MAX) {
        this.CONSUMER_THREAD_MAX = CONSUMER_THREAD_MAX;
    }

    public Long getTENANT_NUM_ID() {
        return this.TENANT_NUM_ID;
    }

    public void setTENANT_NUM_ID(Long TENANT_NUM_ID) {
        this.TENANT_NUM_ID = TENANT_NUM_ID;
    }

    public String getWETHER_ORDER_MESS() {
        return this.WETHER_ORDER_MESS;
    }

    public void setWETHER_ORDER_MESS(String WETHER_ORDER_MESS) {
        this.WETHER_ORDER_MESS = WETHER_ORDER_MESS;
    }

    public String getMESS_TRANS_GROUP() {
        return this.MESS_TRANS_GROUP;
    }

    public void setMESS_TRANS_GROUP(String MESS_TRANS_GROUP) {
        this.MESS_TRANS_GROUP = MESS_TRANS_GROUP;
    }

    public String getRETRY_INTERVAL() {
        return this.RETRY_INTERVAL;
    }

    public void setRETRY_INTERVAL(String RETRY_INTERVAL) {
        this.RETRY_INTERVAL = RETRY_INTERVAL;
    }

    public String getTOPIC_ALIAS() {
        return this.TOPIC_ALIAS;
    }

    public void setTOPIC_ALIAS(String TOPIC_ALIAS) {
        this.TOPIC_ALIAS = TOPIC_ALIAS;
    }

    public String getTAG_ALIAS() {
        return this.TAG_ALIAS;
    }

    public void setTAG_ALIAS(String TAG_ALIAS) {
        this.TAG_ALIAS = TAG_ALIAS;
    }

    public String getCANCELSIGN() {
        return this.CANCELSIGN;
    }

    public void setCANCELSIGN(String CANCELSIGN) {
        this.CANCELSIGN = CANCELSIGN;
    }

    public Long getSYSTEM_NUM_ID() {
        return this.SYSTEM_NUM_ID;
    }

    public void setSYSTEM_NUM_ID(Long SYSTEM_NUM_ID) {
        this.SYSTEM_NUM_ID = SYSTEM_NUM_ID;
    }

    public String getWETHER_INSERTDB() {
        return this.WETHER_INSERTDB;
    }

    public void setWETHER_INSERTDB(String WETHER_INSERTDB) {
        this.WETHER_INSERTDB = WETHER_INSERTDB;
    }

    public String getCORRECT_CODES() {
        return this.CORRECT_CODES;
    }

    public void setCORRECT_CODES(String CORRECT_CODES) {
        this.CORRECT_CODES = CORRECT_CODES;
    }

    public Integer getMQ_QUEUE() {
        return this.MQ_QUEUE;
    }

    public void setMQ_QUEUE(Integer MQ_QUEUE) {
        this.MQ_QUEUE = MQ_QUEUE;
    }

    public String getWETHER_HANDLE_FAILEDMESS() {
        return this.WETHER_HANDLE_FAILEDMESS;
    }

    public void setWETHER_HANDLE_FAILEDMESS(String WETHER_HANDLE_FAILEDMESS) {
        this.WETHER_HANDLE_FAILEDMESS = WETHER_HANDLE_FAILEDMESS;
    }

    public Integer getRETRY_MAX() {
        return this.RETRY_MAX;
    }

    public void setRETRY_MAX(Integer RETRY_MAX) {
        this.RETRY_MAX = RETRY_MAX;
    }

    public Integer getZK_DATA_SIGN() {
        return this.ZK_DATA_SIGN;
    }

    public void setZK_DATA_SIGN(Integer ZK_DATA_SIGN) {
        this.ZK_DATA_SIGN = ZK_DATA_SIGN;
    }

    public String getMQ_NAMESRV() {
        return this.MQ_NAMESRV;
    }

    public void setMQ_NAMESRV(String MQ_NAMESRV) {
        this.MQ_NAMESRV = MQ_NAMESRV;
    }

    public String getCONSUMER_IP() {
        return this.CONSUMER_IP;
    }

    public void setCONSUMER_IP(String CONSUMER_IP) {
        this.CONSUMER_IP = CONSUMER_IP;
    }

    public String getDATASOURCE_NAME() {
        return this.DATASOURCE_NAME;
    }

    public void setDATASOURCE_NAME(String DATASOURCE_NAME) {
        this.DATASOURCE_NAME = DATASOURCE_NAME;
    }
}

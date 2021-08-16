package com.github.bluecatlee.gs4d.message.consumer.model;

public class SysMessageTransDubbo {
    private Long SYSTEM_NUM_ID;
    private String DUBBO_GROUP;
    private String ZK_ADR;
    private String ZK_ADR_TEST;
    private String ZK_ADR_DEVELOPMENT;

    public Long getSYSTEM_NUM_ID() {
        return this.SYSTEM_NUM_ID;
    }

    public void setSYSTEM_NUM_ID(Long SYSTEM_NUM_ID) {
        this.SYSTEM_NUM_ID = SYSTEM_NUM_ID;
    }

    public String getDUBBO_GROUP() {
        return this.DUBBO_GROUP;
    }

    public void setDUBBO_GROUP(String DUBBO_GROUP) {
        this.DUBBO_GROUP = DUBBO_GROUP;
    }

    public String getZK_ADR() {
        return this.ZK_ADR;
    }

    public void setZK_ADR(String ZK_ADR) {
        this.ZK_ADR = ZK_ADR;
    }

    public String getZK_ADR_TEST() {
        return this.ZK_ADR_TEST;
    }

    public void setZK_ADR_TEST(String ZK_ADR_TEST) {
        this.ZK_ADR_TEST = ZK_ADR_TEST;
    }

    public String getZK_ADR_DEVELOPMENT() {
        return this.ZK_ADR_DEVELOPMENT;
    }

    public void setZK_ADR_DEVELOPMENT(String ZK_ADR_DEVELOPMENT) {
        this.ZK_ADR_DEVELOPMENT = ZK_ADR_DEVELOPMENT;
    }
}

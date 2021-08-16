package com.github.bluecatlee.gs4d.message.consumer.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SysRocketMqConsumeLog {
    private Long SERIES;
    private Long MESSAGE_SERIES;
    private String CONSUMER_GROUP;
    private String IS_SUCCESS;
    private String CREATE_DTME;
    private Long CREATE_USER_ID;
    private String FAIL_DETAIL;
    private Long TENANT_NUM_ID = 0L;
    private Long INSTANCE_ID = 0L;
    private Long DATA_SIGN = 0L;
    private Long IS_REALSUCCESS = 0L;
    private Long ConsumerTime;

    public Long getSERIES() {
        return this.SERIES;
    }

    public void setSERIES(Long SERIES) {
        this.SERIES = SERIES;
    }

    public Long getMESSAGE_SERIES() {
        return this.MESSAGE_SERIES;
    }

    public void setMESSAGE_SERIES(Long MESSAGE_SERIES) {
        this.MESSAGE_SERIES = MESSAGE_SERIES;
    }

    public void setCONSUMER_GROUP(String CONSUMER_GROUP) {
        this.CONSUMER_GROUP = CONSUMER_GROUP;
    }

    public String getCONSUMER_GROUP() {
        return this.CONSUMER_GROUP;
    }

    public void setIS_SUCCESS(String IS_SUCCESS) {
        this.IS_SUCCESS = IS_SUCCESS;
    }

    public String getIS_SUCCESS() {
        return this.IS_SUCCESS;
    }

    public void setCREATE_DTME(String CREATE_DTME) {
        this.CREATE_DTME = CREATE_DTME;
    }

    public String getCREATE_DTME() {
        return this.CREATE_DTME;
    }

    public Long getCREATE_USER_ID() {
        return this.CREATE_USER_ID;
    }

    public void setCREATE_USER_ID(Long CREATE_USER_ID) {
        this.CREATE_USER_ID = CREATE_USER_ID;
    }

    public void setFAIL_DETAIL(String FAIL_DETAIL) {
        this.FAIL_DETAIL = FAIL_DETAIL;
    }

    public String getFAIL_DETAIL() {
        return this.FAIL_DETAIL;
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

    public Long getIS_REALSUCCESS() {
        return this.IS_REALSUCCESS;
    }

    public void setIS_REALSUCCESS(Long IS_REALSUCCESS) {
        this.IS_REALSUCCESS = IS_REALSUCCESS;
    }

    public Long getINSTANCE_ID() {
        return this.INSTANCE_ID;
    }

    public void setINSTANCE_ID(Long INSTANCE_ID) {
        this.INSTANCE_ID = INSTANCE_ID;
    }

    public static SysRocketMqConsumeLog map2bean(Map map) {
        SysRocketMqConsumeLog sysRocketMqConsumeLog = new SysRocketMqConsumeLog();
        if (map.containsKey("SERIES")) {
            sysRocketMqConsumeLog.setSERIES(Long.valueOf(String.valueOf(map.get("SERIES"))));
        }

        if (map.containsKey("MESSAGE_SERIES")) {
            sysRocketMqConsumeLog.setMESSAGE_SERIES(Long.valueOf(String.valueOf(map.get("MESSAGE_SERIES"))));
        }

        if (map.containsKey("CONSUMER_GROUP")) {
            sysRocketMqConsumeLog.setCONSUMER_GROUP(String.valueOf(map.get("CONSUMER_GROUP")));
        }

        if (map.containsKey("IS_SUCCESS")) {
            sysRocketMqConsumeLog.setIS_SUCCESS(String.valueOf(map.get("IS_SUCCESS")));
        }

        if (map.containsKey("CREATE_DTME")) {
            sysRocketMqConsumeLog.setCREATE_DTME(String.valueOf(map.get("CREATE_DTME")));
        }

        if (map.containsKey("CREATE_USER_ID")) {
            sysRocketMqConsumeLog.setCREATE_USER_ID(Long.valueOf(String.valueOf(map.get("CREATE_USER_ID"))));
        }

        if (map.containsKey("FAIL_DETAIL")) {
            sysRocketMqConsumeLog.setFAIL_DETAIL(String.valueOf(map.get("FAIL_DETAIL")));
        }

        if (map.containsKey("TENANT_NUM_ID")) {
            sysRocketMqConsumeLog.setTENANT_NUM_ID(Long.valueOf(String.valueOf(map.get("TENANT_NUM_ID"))));
        }

        if (map.containsKey("DATA_SIGN")) {
            sysRocketMqConsumeLog.setDATA_SIGN(Long.valueOf(String.valueOf(map.get("DATA_SIGN"))));
        }

        return sysRocketMqConsumeLog;
    }

    public static List<SysRocketMqConsumeLog> map2bean(List mapList) {
        ArrayList beanList = new ArrayList();
        Iterator iterator = mapList.iterator();

        while(iterator.hasNext()) {
            Object obj = iterator.next();
            Map map = (Map)obj;
            beanList.add(map2bean(map));
        }

        return beanList;
    }

    public Long getConsumerTime() {
        return this.ConsumerTime;
    }

    public void setConsumerTime(Long ConsumerTime) {
        this.ConsumerTime = ConsumerTime;
    }
}

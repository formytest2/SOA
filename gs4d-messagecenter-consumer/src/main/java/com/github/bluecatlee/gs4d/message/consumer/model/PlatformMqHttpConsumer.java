package com.github.bluecatlee.gs4d.message.consumer.model;

import java.text.SimpleDateFormat;
import java.util.*;

public class PlatformMqHttpConsumer {
    
    static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Long SERIES = 0L;
    private String PARAM_NAME = null;
    private String CREATE_USER_NAME = null;
    private String CREATE_DATE_TIME;
    private String URL_TEST;
    private String URL;
    private String URL_DEVELOP;
    private String HTTP_HEAD;
    private String Remark;

    public PlatformMqHttpConsumer() {
        this.CREATE_DATE_TIME = dateFormat.format(new Date());
        this.URL_TEST = null;
        this.URL = null;
        this.URL_DEVELOP = null;
        this.HTTP_HEAD = null;
    }

    public void setSERIES(Long SERIES) {
        this.SERIES = SERIES;
    }

    public Long getSERIES() {
        return this.SERIES;
    }

    public String getURL_DEVELOP() {
        return this.URL_DEVELOP;
    }

    public void setURL_DEVELOP(String URL_DEVELOP) {
        this.URL_DEVELOP = URL_DEVELOP;
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

    public static PlatformMqHttpConsumer map2bean(Map map) {
        PlatformMqHttpConsumer platformMqHttpConsumer = new PlatformMqHttpConsumer();
        if (map.containsKey("SERIES")) {
            platformMqHttpConsumer.setSERIES((Long)((Long)map.get("SERIES")));
        }

        if (map.containsKey("PARAM_NAME")) {
            platformMqHttpConsumer.setPARAM_NAME((String)((String)map.get("PARAM_NAME")));
        }

        if (map.containsKey("CREATE_USER_NAME")) {
            platformMqHttpConsumer.setCREATE_USER_NAME((String)((String)map.get("CREATE_USER_NAME")));
        }

        if (map.containsKey("CREATE_DATE_TIME")) {
            platformMqHttpConsumer.setCREATE_DATE_TIME((String)((String)map.get("CREATE_DATE_TIME")));
        }

        if (map.containsKey("URL_TEST")) {
            platformMqHttpConsumer.setURL_TEST((String)((String)map.get("URL_TEST")));
        }

        if (map.containsKey("URL_DEVELOP")) {
            platformMqHttpConsumer.setURL_TEST((String)((String)map.get("URL_DEVELOP")));
        }

        if (map.containsKey("URL")) {
            platformMqHttpConsumer.setURL((String)((String)map.get("URL")));
        }

        return platformMqHttpConsumer;
    }

    public static List<PlatformMqHttpConsumer> map2bean(List mapList) {
        ArrayList beanList = new ArrayList();
        Iterator iterator = mapList.iterator();

        while(iterator.hasNext()) {
            Object obj = iterator.next();
            Map map = (Map)obj;
            beanList.add(map2bean(map));
        }

        return beanList;
    }

    public String getPARAM_NAME() {
        return this.PARAM_NAME;
    }

    public void setPARAM_NAME(String PARAM_NAME) {
        this.PARAM_NAME = PARAM_NAME;
    }

    public String getURL_TEST() {
        return this.URL_TEST;
    }

    public void setURL_TEST(String URL_TEST) {
        this.URL_TEST = URL_TEST;
    }

    public String getURL() {
        return this.URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getHTTP_HEAD() {
        return this.HTTP_HEAD;
    }

    public void setHTTP_HEAD(String HTTP_HEAD) {
        this.HTTP_HEAD = HTTP_HEAD;
    }

    public String getRemark() {
        return this.Remark;
    }

    public void setRemark(String Remark) {
        this.Remark = Remark;
    }
}

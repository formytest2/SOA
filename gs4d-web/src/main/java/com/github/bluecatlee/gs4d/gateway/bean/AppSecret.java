package com.github.bluecatlee.gs4d.gateway.bean;

import java.io.Serializable;

public class AppSecret implements Serializable {

    private static final long serialVersionUID = -7776853331069184462L;

    private Long series;
    private Long tenantNumId;
    private Long dataSign;
    private String appKey;
    private String appSecret;
    private Long expiryTime;

    public AppSecret() {
    }

    public Long getExpiryTime() {
        return this.expiryTime;
    }

    public void setExpiryTime(Long expiryTime) {
        this.expiryTime = expiryTime;
    }

    public Long getSeries() {
        return this.series;
    }

    public void setSeries(Long series) {
        this.series = series;
    }

    public Long getTenantNumId() {
        return this.tenantNumId;
    }

    public void setTenantNumId(Long tenantNumId) {
        this.tenantNumId = tenantNumId;
    }

    public void setDataSign(Long dataSign) {
        this.dataSign = dataSign;
    }

    public String getAppKey() {
        return this.appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppSecret() {
        return this.appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public Long getDataSign() {
        return this.dataSign;
    }
}

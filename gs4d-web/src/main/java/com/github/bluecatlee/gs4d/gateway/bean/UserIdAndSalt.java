package com.github.bluecatlee.gs4d.gateway.bean;

import java.io.Serializable;
import java.util.Date;

public class UserIdAndSalt implements Serializable {

    private static final long serialVersionUID = 5777877875483930137L;

    private Long userId;

    private String salt;

    private boolean expireSign;

    private Date expireDtme;

    private Long tenantNumId;

    private Long dataSign;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public boolean isExpireSign() {
        return expireSign;
    }

    public void setExpireSign(boolean expireSign) {
        this.expireSign = expireSign;
    }

    public Date getExpireDtme() {
        return expireDtme;
    }

    public void setExpireDtme(Date expireDtme) {
        this.expireDtme = expireDtme;
    }

    public Long getTenantNumId() {
        return tenantNumId;
    }

    public void setTenantNumId(Long tenantNumId) {
        this.tenantNumId = tenantNumId;
    }

    public Long getDataSign() {
        return dataSign;
    }

    public void setDataSign(Long dataSign) {
        this.dataSign = dataSign;
    }
}

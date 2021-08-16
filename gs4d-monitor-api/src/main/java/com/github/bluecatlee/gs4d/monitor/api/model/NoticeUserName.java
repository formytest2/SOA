package com.github.bluecatlee.gs4d.monitor.api.model;

public class NoticeUserName extends BaseEntity {
    private Long userId;

    private String userName;

    private Long errorCodeId;

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getErrorCodeId() {
        return this.errorCodeId;
    }

    public void setErrorCodeId(Long errorCodeId) {
        this.errorCodeId = errorCodeId;
    }

    public Long getSeries() {
        return null;
    }
}

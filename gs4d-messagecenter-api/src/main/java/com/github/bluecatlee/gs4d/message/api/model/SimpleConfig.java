package com.github.bluecatlee.gs4d.message.api.model;

import java.io.Serializable;

public class SimpleConfig implements Serializable {
    private static final long serialVersionUID = -3157074139008825782L;
    private Long series;
    private String topic;
    private String tag;
    private String systemName;
    private String remark;
    private String createUser;
    private String onlineFlag;
    private Long retryTimes;
    private Long consumerType;

    public Long getSeries() {
        return this.series;
    }

    public void setSeries(Long series) {
        this.series = series;
    }

    public String getTopic() {
        return this.topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTag() {
        return this.tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getSystemName() {
        return this.systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreateUser() {
        return this.createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getOnlineFlag() {
        return this.onlineFlag;
    }

    public void setOnlineFlag(String onlineFlag) {
        this.onlineFlag = onlineFlag;
    }

    public Long getRetryTimes() {
        return this.retryTimes;
    }

    public void setRetryTimes(Long retryTimes) {
        this.retryTimes = retryTimes;
    }

    public Long getConsumerType() {
        return this.consumerType;
    }

    public void setConsumerType(Long consumerType) {
        this.consumerType = consumerType;
    }
}


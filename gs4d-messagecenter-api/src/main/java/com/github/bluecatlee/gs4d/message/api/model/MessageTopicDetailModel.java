package com.github.bluecatlee.gs4d.message.api.model;

import java.io.Serializable;

public class MessageTopicDetailModel implements Serializable {

    private static final long serialVersionUID = 5960554801779598340L;
    private String topic;
    private String tag;
    private String businessRemark;
    private Long messageNum;
    private Long messageConfirmNum;
    private Long messageNoConfirmNum;
    private Long messageCounsumerSuccessNum;
    private Long messageCounsumerFailedNum;
    private Long messageNoCounsumerNum;
    private Long messageCancelNum;
    private Long messageIsTryingNow;
    private String averageConsumerTime;
    private Integer retryTime;
    private String createTime;
    private String updateTime;

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

    public String getBusinessRemark() {
        return this.businessRemark;
    }

    public void setBusinessRemark(String businessRemark) {
        this.businessRemark = businessRemark;
    }

    public Long getMessageNum() {
        return this.messageNum;
    }

    public void setMessageNum(Long messageNum) {
        this.messageNum = messageNum;
    }

    public Long getMessageConfirmNum() {
        return this.messageConfirmNum;
    }

    public void setMessageConfirmNum(Long messageConfirmNum) {
        this.messageConfirmNum = messageConfirmNum;
    }

    public Long getMessageNoConfirmNum() {
        return this.messageNoConfirmNum;
    }

    public void setMessageNoConfirmNum(Long messageNoConfirmNum) {
        this.messageNoConfirmNum = messageNoConfirmNum;
    }

    public Long getMessageCounsumerSuccessNum() {
        return this.messageCounsumerSuccessNum;
    }

    public void setMessageCounsumerSuccessNum(Long messageCounsumerSuccessNum) {
        this.messageCounsumerSuccessNum = messageCounsumerSuccessNum;
    }

    public Long getMessageCounsumerFailedNum() {
        return this.messageCounsumerFailedNum;
    }

    public void setMessageCounsumerFailedNum(Long messageCounsumerFailedNum) {
        this.messageCounsumerFailedNum = messageCounsumerFailedNum;
    }

    public Long getMessageNoCounsumerNum() {
        return this.messageNoCounsumerNum;
    }

    public void setMessageNoCounsumerNum(Long messageNoCounsumerNum) {
        this.messageNoCounsumerNum = messageNoCounsumerNum;
    }

    public String getAverageConsumerTime() {
        return this.averageConsumerTime;
    }

    public void setAverageConsumerTime(String averageConsumerTime) {
        this.averageConsumerTime = averageConsumerTime;
    }

    public Integer getRetryTime() {
        return this.retryTime;
    }

    public void setRetryTime(Integer retryTime) {
        this.retryTime = retryTime;
    }

    public Long getMessageIsTryingNow() {
        return this.messageIsTryingNow;
    }

    public void setMessageIsTryingNow(Long messageIsTryingNow) {
        this.messageIsTryingNow = messageIsTryingNow;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public Long getMessageCancelNum() {
        return this.messageCancelNum;
    }

    public void setMessageCancelNum(Long messageCancelNum) {
        this.messageCancelNum = messageCancelNum;
    }
}


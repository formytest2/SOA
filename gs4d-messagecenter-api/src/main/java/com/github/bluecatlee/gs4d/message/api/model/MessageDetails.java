package com.github.bluecatlee.gs4d.message.api.model;

import java.io.Serializable;

public class MessageDetails implements Serializable {

    private static final long serialVersionUID = 4938394502069152247L;
    private String series;
    private String messageId;
    private String messageKey;
    private String createTime;
    private String messageBody;
    private String messageIsConsumer;
    private String messageConsumerResult;
    private Long retryTimes;
    private String consumerSuccessTime;
    private Long consumerTime;
    private String clientIp;
    private String topic;
    private String tag;
    private String remark;
    private String sendType;
    private String unConsumeReason;

    public String getSeries() {
        return this.series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getMessageId() {
        return this.messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessageKey() {
        return this.messageKey;
    }

    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getMessageBody() {
        return this.messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public String getMessageIsConsumer() {
        return this.messageIsConsumer;
    }

    public void setMessageIsConsumer(String messageIsConsumer) {
        this.messageIsConsumer = messageIsConsumer;
    }

    public String getMessageConsumerResult() {
        return this.messageConsumerResult;
    }

    public void setMessageConsumerResult(String messageConsumerResult) {
        this.messageConsumerResult = messageConsumerResult;
    }

    public Long getRetryTimes() {
        return this.retryTimes;
    }

    public void setRetryTimes(Long retryTimes) {
        this.retryTimes = retryTimes;
    }

    public String getConsumerSuccessTime() {
        return this.consumerSuccessTime;
    }

    public void setConsumerSuccessTime(String consumerSuccessTime) {
        this.consumerSuccessTime = consumerSuccessTime;
    }

    public String getClientIp() {
        return this.clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
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

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSendType() {
        return this.sendType;
    }

    public void setSendType(String sendType) {
        this.sendType = sendType;
    }

    public String getUnConsumeReason() {
        return this.unConsumeReason;
    }

    public void setUnConsumeReason(String unConsumeReason) {
        this.unConsumeReason = unConsumeReason;
    }

    public Long getConsumerTime() {
        return this.consumerTime;
    }

    public void setConsumerTime(Long consumerTime) {
        this.consumerTime = consumerTime;
    }
}


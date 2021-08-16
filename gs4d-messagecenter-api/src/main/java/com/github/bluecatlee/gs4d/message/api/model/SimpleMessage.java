package com.github.bluecatlee.gs4d.message.api.model;

import java.io.Serializable;
import java.util.Date;

public class SimpleMessage implements Serializable {
    private static final long serialVersionUID = 9054634473193563696L;
    private String topic;
    private String tag;
    private String msgKey = "";
    private String body;
    private Long dataSign;
    private Long userId = 0L;
    private Long fromSystem;
    private Long tenantNumId;
    private String clientIp;        // 消息发起方ip
    private Date jobTargetTime;
    private Long transactionId;     // 分布式事务id
    private Long workFlowId;

    public SimpleMessage() {}

    public SimpleMessage(String topic, String tag, String msgKey, String body, Long systemId, Long dataSign, Long tenantNumId) {
        this.topic = topic;
        this.tag = tag;
        this.msgKey = msgKey;
        this.body = body;
        this.fromSystem = systemId;
        this.dataSign = dataSign;
        this.tenantNumId = tenantNumId;
    }

    public SimpleMessage(String topic, String tag, String msgKey, String body, Long systemId, Long dataSign, Long tenantNumId, String clientIP) {
        this.topic = topic;
        this.tag = tag;
        this.msgKey = msgKey;
        this.body = body;
        this.fromSystem = systemId;
        this.dataSign = dataSign;
        this.tenantNumId = tenantNumId;
        this.clientIp = clientIP;
    }

    public SimpleMessage(String topic, String tag, String msgKey, String body, Long systemId, Long dataSign) {
        this.topic = topic;
        this.tag = tag;
        this.msgKey = msgKey;
        this.body = body;
        this.fromSystem = systemId;
        this.dataSign = dataSign;
    }

    public SimpleMessage(String topic, String tag, String msgKey, String body, Long systemId, Long dataSign, Long tenantNumId, Date jobTargetTime) {
        this.topic = topic;
        this.tag = tag;
        this.msgKey = msgKey;
        this.body = body;
        this.fromSystem = systemId;
        this.dataSign = dataSign;
        this.tenantNumId = tenantNumId;
        this.jobTargetTime = jobTargetTime;
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

    public String getMsgKey() {
        return this.msgKey;
    }

    public void setMsgKey(String msgKey) {
        this.msgKey = msgKey;
    }

    public String getBody() {
        return this.body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Long getFromSystem() {
        return this.fromSystem;
    }

    public void setFromSystem(Long fromSystem) {
        this.fromSystem = fromSystem;
    }

    public Long getTenantNumId() {
        return this.tenantNumId;
    }

    public void setTenantNumId(Long tenantNumId) {
        this.tenantNumId = tenantNumId;
    }

    public Long getDataSign() {
        return this.dataSign;
    }

    public void setDataSign(Long dataSign) {
        this.dataSign = dataSign;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getClientIp() {
        return this.clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public Date getJobTargetTime() {
        return this.jobTargetTime;
    }

    public void setJobTargetTime(Date jobTargetTime) {
        this.jobTargetTime = jobTargetTime;
    }

    public Long getTransactionId() {
        return this.transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public Long getWorkFlowId() {
        return this.workFlowId;
    }

    public void setWorkFlowId(Long workFlowId) {
        this.workFlowId = workFlowId;
    }
}


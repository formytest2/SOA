package com.github.bluecatlee.gs4d.pay.entity;

import java.util.Date;

public class PayParamLog {
    private Long series;

    private String outTradeNo;

    private String channel;

    private Date requestDate;

    private Date responseDate;

    private String responseCode;

    private String responseMsg;

    private String threadId;

    private String ip;

    private Long tenantNumId;

    private Short platType;

    private String operatType;

    private String payAppKey;

    public Long getSeries() {
        return series;
    }

    public void setSeries(Long series) {
        this.series = series;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo == null ? null : outTradeNo.trim();
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel == null ? null : channel.trim();
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public Date getResponseDate() {
        return responseDate;
    }

    public void setResponseDate(Date responseDate) {
        this.responseDate = responseDate;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode == null ? null : responseCode.trim();
    }

    public String getResponseMsg() {
        return responseMsg;
    }

    public void setResponseMsg(String responseMsg) {
        this.responseMsg = responseMsg == null ? null : responseMsg.trim();
    }

    public String getThreadId() {
        return threadId;
    }

    public void setThreadId(String threadId) {
        this.threadId = threadId == null ? null : threadId.trim();
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    public Long getTenantNumId() {
        return tenantNumId;
    }

    public void setTenantNumId(Long tenantNumId) {
        this.tenantNumId = tenantNumId;
    }

    public Short getPlatType() {
        return platType;
    }

    public void setPlatType(Short platType) {
        this.platType = platType;
    }

    public String getOperatType() {
        return operatType;
    }

    public void setOperatType(String operatType) {
        this.operatType = operatType == null ? null : operatType.trim();
    }

    public String getPayAppKey() {
        return payAppKey;
    }

    public void setPayAppKey(String payAppKey) {
        this.payAppKey = payAppKey == null ? null : payAppKey.trim();
    }
}
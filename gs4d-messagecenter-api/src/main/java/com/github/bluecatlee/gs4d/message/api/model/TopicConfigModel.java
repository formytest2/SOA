package com.github.bluecatlee.gs4d.message.api.model;

import java.io.Serializable;
import java.util.List;

public class TopicConfigModel implements Serializable {
    private static final long serialVersionUID = -6159720510841762575L;
    private String series;
    private String topic;
    private String tag;
    private String consumerInfo;
    private String consumerType;
    private Long consumerSeries;
    private String createUser;
    private String createTime;
    private Integer retryTimes;
    private Integer retryTestTimes;
    private Integer retryDevelopTimes;
    private String consumerInterval;
    private String wetherOrderMessage;
    private String wetherListenBinLogTopic;
    private String consumerPullDelay;
    private Long consumerThreadNum;
    private Long messBatchNum;
    private String remark;
    private String systemName;
    private String retryInterval;
    private String taskTarget;
    private Long systemNumId;
    private String wetherInsertdb;
    private String correctCodes;
    private Long mqQueue;
    private String wetherHandleFailedmess;
    private Long retryMax;
    private String zkDataSign;
    private String mqNamesrv;
    private String consumerIp;
    private String dubboService;
    private String dubboMethod;
    private String dubboParam;
    private String dubboGroup;
    private String dubboDirUrl;
    private String zkAddress;
    private String zkTestAddress;
    private String zkDevelopAddress;
    private String dubboRemark;
    private String dubboCreatePerson;
    private Long dubboSeries;
    private Long httpSeries;
    private String url;
    private String urlTest;
    private String urlDevelop;
    private String paramName;
    private String httpUserName;
    private String httpRemark;
    private String httpHead;
    private List<Long> users;

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

    public String getConsumerInfo() {
        return this.consumerInfo;
    }

    public void setConsumerInfo(String consumerInfo) {
        this.consumerInfo = consumerInfo;
    }

    public String getConsumerType() {
        return this.consumerType;
    }

    public void setConsumerType(String consumerType) {
        this.consumerType = consumerType;
    }

    public Long getConsumerSeries() {
        return this.consumerSeries;
    }

    public void setConsumerSeries(Long consumerSeries) {
        this.consumerSeries = consumerSeries;
    }

    public String getCreateUser() {
        return this.createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Integer getRetryTimes() {
        return this.retryTimes;
    }

    public void setRetryTimes(Integer retryTimes) {
        this.retryTimes = retryTimes;
    }

    public Long getConsumerThreadNum() {
        return this.consumerThreadNum;
    }

    public void setConsumerThreadNum(Long consumerThreadNum) {
        this.consumerThreadNum = consumerThreadNum;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSystemName() {
        return this.systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getSeries() {
        return this.series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getRetryInterval() {
        return this.retryInterval;
    }

    public void setRetryInterval(String retryInterval) {
        this.retryInterval = retryInterval;
    }

    public String getTaskTarget() {
        return this.taskTarget;
    }

    public void setTaskTarget(String taskTarget) {
        this.taskTarget = taskTarget;
    }

    public Long getSystemNumId() {
        return this.systemNumId;
    }

    public void setSystemNumId(Long systemNumId) {
        this.systemNumId = systemNumId;
    }

    public String getWetherInsertdb() {
        return this.wetherInsertdb;
    }

    public void setWetherInsertdb(String wetherInsertdb) {
        this.wetherInsertdb = wetherInsertdb;
    }

    public String getCorrectCodes() {
        return this.correctCodes;
    }

    public void setCorrectCodes(String correctCodes) {
        this.correctCodes = correctCodes;
    }

    public Long getMqQueue() {
        return this.mqQueue;
    }

    public void setMqQueue(Long mqQueue) {
        this.mqQueue = mqQueue;
    }

    public String getWetherHandleFailedmess() {
        return this.wetherHandleFailedmess;
    }

    public void setWetherHandleFailedmess(String wetherHandleFailedmess) {
        this.wetherHandleFailedmess = wetherHandleFailedmess;
    }

    public Long getRetryMax() {
        return this.retryMax;
    }

    public void setRetryMax(Long retryMax) {
        this.retryMax = retryMax;
    }

    public String getZkDataSign() {
        return this.zkDataSign;
    }

    public void setZkDataSign(String zkDataSign) {
        this.zkDataSign = zkDataSign;
    }

    public String getMqNamesrv() {
        return this.mqNamesrv;
    }

    public void setMqNamesrv(String mqNamesrv) {
        this.mqNamesrv = mqNamesrv;
    }

    public String getConsumerIp() {
        return this.consumerIp;
    }

    public void setConsumerIp(String consumerIp) {
        this.consumerIp = consumerIp;
    }

    public Integer getRetryTestTimes() {
        return this.retryTestTimes;
    }

    public void setRetryTestTimes(Integer retryTestTimes) {
        this.retryTestTimes = retryTestTimes;
    }

    public Integer getRetryDevelopTimes() {
        return this.retryDevelopTimes;
    }

    public void setRetryDevelopTimes(Integer retryDevelopTimes) {
        this.retryDevelopTimes = retryDevelopTimes;
    }

    public String getDubboService() {
        return this.dubboService;
    }

    public void setDubboService(String dubboService) {
        this.dubboService = dubboService;
    }

    public String getDubboMethod() {
        return this.dubboMethod;
    }

    public void setDubboMethod(String dubboMethod) {
        this.dubboMethod = dubboMethod;
    }

    public String getDubboParam() {
        return this.dubboParam;
    }

    public void setDubboParam(String dubboParam) {
        this.dubboParam = dubboParam;
    }

    public String getDubboGroup() {
        return this.dubboGroup;
    }

    public void setDubboGroup(String dubboGroup) {
        this.dubboGroup = dubboGroup;
    }

    public String getZkAddress() {
        return this.zkAddress;
    }

    public void setZkAddress(String zkAddress) {
        this.zkAddress = zkAddress;
    }

    public String getZkTestAddress() {
        return this.zkTestAddress;
    }

    public void setZkTestAddress(String zkTestAddress) {
        this.zkTestAddress = zkTestAddress;
    }

    public String getZkDevelopAddress() {
        return this.zkDevelopAddress;
    }

    public void setZkDevelopAddress(String zkDevelopAddress) {
        this.zkDevelopAddress = zkDevelopAddress;
    }

    public String getDubboRemark() {
        return this.dubboRemark;
    }

    public void setDubboRemark(String dubboRemark) {
        this.dubboRemark = dubboRemark;
    }

    public String getDubboCreatePerson() {
        return this.dubboCreatePerson;
    }

    public void setDubboCreatePerson(String dubboCreatePerson) {
        this.dubboCreatePerson = dubboCreatePerson;
    }

    public Long getDubboSeries() {
        return this.dubboSeries;
    }

    public void setDubboSeries(Long dubboSeries) {
        this.dubboSeries = dubboSeries;
    }

    public Long getHttpSeries() {
        return this.httpSeries;
    }

    public void setHttpSeries(Long httpSeries) {
        this.httpSeries = httpSeries;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlTest() {
        return this.urlTest;
    }

    public void setUrlTest(String urlTest) {
        this.urlTest = urlTest;
    }

    public String getUrlDevelop() {
        return this.urlDevelop;
    }

    public void setUrlDevelop(String urlDevelop) {
        this.urlDevelop = urlDevelop;
    }

    public String getParamName() {
        return this.paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getHttpUserName() {
        return this.httpUserName;
    }

    public void setHttpUserName(String httpUserName) {
        this.httpUserName = httpUserName;
    }

    public String getHttpRemark() {
        return this.httpRemark;
    }

    public void setHttpRemark(String httpRemark) {
        this.httpRemark = httpRemark;
    }

    public String getHttpHead() {
        return this.httpHead;
    }

    public void setHttpHead(String httpHead) {
        this.httpHead = httpHead;
    }

    public String getDubboDirUrl() {
        return this.dubboDirUrl;
    }

    public void setDubboDirUrl(String dubboDirUrl) {
        this.dubboDirUrl = dubboDirUrl;
    }

    public String getConsumerInterval() {
        return this.consumerInterval;
    }

    public void setConsumerInterval(String consumerInterval) {
        this.consumerInterval = consumerInterval;
    }

    public String getWetherOrderMessage() {
        return this.wetherOrderMessage;
    }

    public void setWetherOrderMessage(String wetherOrderMessage) {
        this.wetherOrderMessage = wetherOrderMessage;
    }

    public String getConsumerPullDelay() {
        return this.consumerPullDelay;
    }

    public void setConsumerPullDelay(String consumerPullDelay) {
        this.consumerPullDelay = consumerPullDelay;
    }

    public String getWetherListenBinLogTopic() {
        return this.wetherListenBinLogTopic;
    }

    public void setWetherListenBinLogTopic(String wetherListenBinLogTopic) {
        this.wetherListenBinLogTopic = wetherListenBinLogTopic;
    }

    public Long getMessBatchNum() {
        return this.messBatchNum;
    }

    public void setMessBatchNum(Long messBatchNum) {
        this.messBatchNum = messBatchNum;
    }

    public List<Long> getUsers() {
        return this.users;
    }

    public void setUsers(List<Long> users) {
        this.users = users;
    }
}

package com.github.bluecatlee.gs4d.message.api.request;

import com.github.bluecatlee.gs4d.common.bean.AbstractRequest;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

public class TopicConfigAddRequest extends AbstractRequest {
    private static final long serialVersionUID = -648531022888240209L;
    @NotEmpty(message = "topic不能为空！")
    private String topic;
    @NotEmpty(message = "tag不能为空！")
    private String tag;
    @NotNull(message = "系统名不能为空")
    private Long systemNumId;
    @NotEmpty(message = "创建人不能为空！")
    private String createUserName;
    @NotEmpty(message = "备注不能为空！")
    private String remark;
    private String wetherListenBinLogTopic;
    private String consumerInterval;
    private Long retries;
    private Long retriesTest;
    private Long retriesDevelopment;
    private Long taskTarget;
    private String isDistinct;
    private Long messBatchNum;
    private Long consumerNum;
    private String wetherOrderMess;
    private String retryInterval;
    private String wetherInsertdb;
    private String correctCodes;
    private Long mqQueue;
    private String wetherHandleFailedmess;
    private Long retryMax;
    private String zkDataSign;
    private Long consumerPullDelay;
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

    public Long getSystemNumId() {
        return this.systemNumId;
    }

    public void setSystemNumId(Long systemNumId) {
        this.systemNumId = systemNumId;
    }

    public String getCreateUserName() {
        return this.createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getWetherListenBinLogTopic() {
        return this.wetherListenBinLogTopic;
    }

    public void setWetherListenBinLogTopic(String wetherListenBinLogTopic) {
        this.wetherListenBinLogTopic = wetherListenBinLogTopic;
    }

    public String getConsumerInterval() {
        return this.consumerInterval;
    }

    public void setConsumerInterval(String consumerInterval) {
        this.consumerInterval = consumerInterval;
    }

    public Long getRetries() {
        return this.retries;
    }

    public void setRetries(Long retries) {
        this.retries = retries;
    }

    public Long getRetriesTest() {
        return this.retriesTest;
    }

    public void setRetriesTest(Long retriesTest) {
        this.retriesTest = retriesTest;
    }

    public Long getRetriesDevelopment() {
        return this.retriesDevelopment;
    }

    public void setRetriesDevelopment(Long retriesDevelopment) {
        this.retriesDevelopment = retriesDevelopment;
    }

    public Long getTaskTarget() {
        return this.taskTarget;
    }

    public void setTaskTarget(Long taskTarget) {
        this.taskTarget = taskTarget;
    }

    public String getIsDistinct() {
        return this.isDistinct;
    }

    public void setIsDistinct(String isDistinct) {
        this.isDistinct = isDistinct;
    }

    public Long getMessBatchNum() {
        return this.messBatchNum;
    }

    public void setMessBatchNum(Long messBatchNum) {
        this.messBatchNum = messBatchNum;
    }

    public Long getConsumerNum() {
        return this.consumerNum;
    }

    public void setConsumerNum(Long consumerNum) {
        this.consumerNum = consumerNum;
    }

    public String getWetherOrderMess() {
        return this.wetherOrderMess;
    }

    public void setWetherOrderMess(String wetherOrderMess) {
        this.wetherOrderMess = wetherOrderMess;
    }

    public String getRetryInterval() {
        return this.retryInterval;
    }

    public void setRetryInterval(String retryInterval) {
        this.retryInterval = retryInterval;
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

    public Long getConsumerPullDelay() {
        return this.consumerPullDelay;
    }

    public void setConsumerPullDelay(Long consumerPullDelay) {
        this.consumerPullDelay = consumerPullDelay;
    }

    public List<Long> getUsers() {
        return this.users;
    }

    public void setUsers(List<Long> users) {
        this.users = users;
    }
}


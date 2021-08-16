package com.github.bluecatlee.gs4d.message.api.request;

import com.github.bluecatlee.gs4d.common.bean.AbstractRequest;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

public class PlatformMqTopicMessageUpdateRequest extends AbstractRequest {
    private static final long serialVersionUID = -7827976771038401756L;
    @NotNull(message = "series不能为空！")
    private Long series;
    @NotEmpty(message = "topic不能为空！")
    private String topic;
    @NotEmpty(message = "tag不能为空！")
    private String tag;
    @NotNull(message = "systemNumId不能为空！")
    private Long systemNumId;
    @NotEmpty(message = "创建人不能为空！")
    private String createUser;
    @NotEmpty(message = "备注不能为空！")
    private String remark;
    private String wetherListenBinLogTopic;
    private String consumerInterval;
    private Long retryTimes;
    private Long retryTestTimes;
    private Long retryDevelopTimes;
    private Long taskTarget;
    private String isDistinct;
    private Long messBatchNum;
    private Long consumerThreadNum;
    private String wetherOrderMessage;
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

    public String getCreateUser() {
        return this.createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
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

    public Long getRetryTimes() {
        return this.retryTimes;
    }

    public void setRetryTimes(Long retryTimes) {
        this.retryTimes = retryTimes;
    }

    public Long getRetryTestTimes() {
        return this.retryTestTimes;
    }

    public void setRetryTestTimes(Long retryTestTimes) {
        this.retryTestTimes = retryTestTimes;
    }

    public Long getRetryDevelopTimes() {
        return this.retryDevelopTimes;
    }

    public void setRetryDevelopTimes(Long retryDevelopTimes) {
        this.retryDevelopTimes = retryDevelopTimes;
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

    public Long getConsumerThreadNum() {
        return this.consumerThreadNum;
    }

    public void setConsumerThreadNum(Long consumerThreadNum) {
        this.consumerThreadNum = consumerThreadNum;
    }

    public String getWetherOrderMessage() {
        return this.wetherOrderMessage;
    }

    public void setWetherOrderMessage(String wetherOrderMessage) {
        this.wetherOrderMessage = wetherOrderMessage;
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

    public Long getSeries() {
        return this.series;
    }

    public void setSeries(Long series) {
        this.series = series;
    }
}

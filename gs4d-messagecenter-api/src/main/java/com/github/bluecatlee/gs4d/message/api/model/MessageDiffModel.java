package com.github.bluecatlee.gs4d.message.api.model;

import java.io.Serializable;

public class MessageDiffModel implements Serializable {
    private static final long serialVersionUID = 8437885928936254732L;
    private String topic;
    private String tag;
    private String brokerName;
    private Integer queueId;
    private Long diff;
    private String lastTime;

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

    public String getBrokerName() {
        return this.brokerName;
    }

    public void setBrokerName(String brokerName) {
        this.brokerName = brokerName;
    }

    public Integer getQueueId() {
        return this.queueId;
    }

    public void setQueueId(Integer queueId) {
        this.queueId = queueId;
    }

    public Long getDiff() {
        return this.diff;
    }

    public void setDiff(Long diff) {
        this.diff = diff;
    }

    public String getLastTime() {
        return this.lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }
}

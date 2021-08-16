package com.github.bluecatlee.gs4d.message.api.model;

import java.io.Serializable;

public class MessageConsumerFailedModel implements Serializable {

    private String topic;
    private String tag;
    private Integer consumerFailedNum;
    private String remark;

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

    public Integer getConsumerFailedNum() {
        return this.consumerFailedNum;
    }

    public void setConsumerFailedNum(Integer consumerFailedNum) {
        this.consumerFailedNum = consumerFailedNum;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}

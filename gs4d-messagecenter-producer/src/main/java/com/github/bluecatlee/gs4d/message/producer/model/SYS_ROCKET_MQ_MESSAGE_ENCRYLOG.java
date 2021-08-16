package com.github.bluecatlee.gs4d.message.producer.model;

public class SYS_ROCKET_MQ_MESSAGE_ENCRYLOG {

    private String msgmd;
    private Long msgSeries;
    private Long dataSign;
    private String remark;
    private String topic;
    private String tag;

    public String getMsgmd() {
        return this.msgmd;
    }

    public void setMsgmd(String msgmd) {
        this.msgmd = msgmd;
    }

    public Long getMsgSeries() {
        return this.msgSeries;
    }

    public void setMsgSeries(Long msgSeries) {
        this.msgSeries = msgSeries;
    }

    public Long getDataSign() {
        return this.dataSign;
    }

    public void setDataSign(Long dataSign) {
        this.dataSign = dataSign;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
}

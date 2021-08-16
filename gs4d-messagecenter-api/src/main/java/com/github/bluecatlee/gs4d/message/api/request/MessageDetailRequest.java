package com.github.bluecatlee.gs4d.message.api.request;

import com.github.bluecatlee.gs4d.common.bean.AbstractRequest;

import java.util.Date;

public class MessageDetailRequest extends AbstractRequest {
    private static final long serialVersionUID = -2405766970877322506L;
    private Date createTime;
    private Date endTime;
    private String topic;
    private String tag;
    private Long series;
    private Long pageNo;
    private Long pageCount;
    private String messageKey;
    private String consumerSuccess;
    private Long retryTimes;

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getEndTime() {
        return this.endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
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

    public Long getSeries() {
        return this.series;
    }

    public void setSeries(Long series) {
        this.series = series;
    }

    public Long getPageNo() {
        return this.pageNo;
    }

    public void setPageNo(Long pageNo) {
        this.pageNo = pageNo;
    }

    public Long getPageCount() {
        return this.pageCount;
    }

    public void setPageCount(Long pageCount) {
        this.pageCount = pageCount;
    }

    public String getMessageKey() {
        return this.messageKey;
    }

    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

    public String getConsumerSuccess() {
        return this.consumerSuccess;
    }

    public void setConsumerSuccess(String consumerSuccess) {
        this.consumerSuccess = consumerSuccess;
    }

    public Long getRetryTimes() {
        return this.retryTimes;
    }

    public void setRetryTimes(Long retryTimes) {
        this.retryTimes = retryTimes;
    }
}


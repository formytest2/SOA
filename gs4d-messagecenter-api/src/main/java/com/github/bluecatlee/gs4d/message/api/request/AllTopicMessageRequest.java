package com.github.bluecatlee.gs4d.message.api.request;

import com.github.bluecatlee.gs4d.common.bean.AbstractRequest;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class AllTopicMessageRequest extends AbstractRequest {
    private static final long serialVersionUID = 1L;
    private String topic;
    private String tag;
    @NotNull(message = "开始时间！")
    private Date createTime;
    @NotNull(message = "结束时间！")
    private Date endTime;
    @NotNull(message = "页码不能为空！")
    private Integer pageNo;
    @NotNull(message = "一页展示数不能为空！")
    private Integer pageCount;

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

    public Integer getPageNo() {
        return this.pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageCount() {
        return this.pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }
}


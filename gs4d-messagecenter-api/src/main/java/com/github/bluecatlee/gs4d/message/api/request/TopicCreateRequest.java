package com.github.bluecatlee.gs4d.message.api.request;

import com.github.bluecatlee.gs4d.common.bean.AbstractRequest;

public class TopicCreateRequest extends AbstractRequest {
    private static final long serialVersionUID = 6449420228054228629L;
    private String topic;
    private Long rowNum;
    private String nameSrv;

    public String getTopic() {
        return this.topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Long getRowNum() {
        return this.rowNum;
    }

    public void setRowNum(Long rowNum) {
        this.rowNum = rowNum;
    }

    public String getNameSrv() {
        return this.nameSrv;
    }

    public void setNameSrv(String nameSrv) {
        this.nameSrv = nameSrv;
    }
}


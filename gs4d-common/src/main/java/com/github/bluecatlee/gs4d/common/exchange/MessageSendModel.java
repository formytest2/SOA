package com.github.bluecatlee.gs4d.common.exchange;

import java.util.List;

public class MessageSendModel {
    private String topic;
    private String tag;
    private String messageKey;
    private List<AddParamModel> addParam;
    private List<ForeachInputModel> foreachInput;
    private long importSystemId;

    public MessageSendModel() {
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

    public String getMessageKey() {
        return this.messageKey;
    }

    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

    public List<AddParamModel> getAddParam() {
        return this.addParam;
    }

    public void setAddParam(List<AddParamModel> addParam) {
        this.addParam = addParam;
    }

    public List<ForeachInputModel> getForeachInput() {
        return this.foreachInput;
    }

    public void setForeachInput(List<ForeachInputModel> foreachInput) {
        this.foreachInput = foreachInput;
    }

    public long getImportSystemId() {
        return this.importSystemId;
    }

    public void setImportSystemId(long importSystemId) {
        this.importSystemId = importSystemId;
    }
}


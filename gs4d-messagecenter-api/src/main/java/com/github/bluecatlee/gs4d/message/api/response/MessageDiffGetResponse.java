package com.github.bluecatlee.gs4d.message.api.response;

import com.github.bluecatlee.gs4d.common.bean.MessagePack;
import com.github.bluecatlee.gs4d.message.api.model.MessageDiffModel;

import java.util.List;

public class MessageDiffGetResponse extends MessagePack {
    private List<MessageDiffModel> messageDiffModel;
    private Long totalDiff;
    private String consumerInfo;

    public List<MessageDiffModel> getMessageDiffModel() {
        return this.messageDiffModel;
    }

    public void setMessageDiffModel(List<MessageDiffModel> messageDiffModel) {
        this.messageDiffModel = messageDiffModel;
    }

    public Long getTotalDiff() {
        return this.totalDiff;
    }

    public void setTotalDiff(Long totalDiff) {
        this.totalDiff = totalDiff;
    }

    public String getConsumerInfo() {
        return this.consumerInfo;
    }

    public void setConsumerInfo(String consumerInfo) {
        this.consumerInfo = consumerInfo;
    }
}


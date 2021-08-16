package com.github.bluecatlee.gs4d.message.api.response;

import com.github.bluecatlee.gs4d.common.bean.MessagePack;
import com.github.bluecatlee.gs4d.message.api.model.MessageDetails;

import java.util.List;

public class MessageDetailResponse extends MessagePack {
    private static final long serialVersionUID = -7245983147613552038L;
    private List<MessageDetails> messageList;
    private Long total;

    public List<MessageDetails> getMessageList() {
        return this.messageList;
    }

    public void setMessageList(List<MessageDetails> messageList) {
        this.messageList = messageList;
    }

    public Long getTotal() {
        return this.total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}


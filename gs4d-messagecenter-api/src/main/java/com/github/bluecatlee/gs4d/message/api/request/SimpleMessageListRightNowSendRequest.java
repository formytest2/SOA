package com.github.bluecatlee.gs4d.message.api.request;

import com.github.bluecatlee.gs4d.common.bean.AbstractRequest;
import com.github.bluecatlee.gs4d.message.api.model.SimpleMessage;

import java.util.List;

public class SimpleMessageListRightNowSendRequest extends AbstractRequest {
    private static final long serialVersionUID = -821101562265285319L;
    private List<SimpleMessage> simpleMessageList;

    public SimpleMessageListRightNowSendRequest() {
    }

    public List<SimpleMessage> getSimpleMessageList() {
        return this.simpleMessageList;
    }

    public void setSimpleMessageList(List<SimpleMessage> simpleMessageList) {
        this.simpleMessageList = simpleMessageList;
    }
}


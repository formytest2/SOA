package com.github.bluecatlee.gs4d.message.api.request;

import com.github.bluecatlee.gs4d.common.bean.AbstractRequest;
import com.github.bluecatlee.gs4d.message.api.model.SimpleMessage;

import java.util.List;

public class OrderMessageRightNowRequest extends AbstractRequest {
    private static final long serialVersionUID = -44876610938571087L;
    private List<SimpleMessage> simpleMessage;

    public OrderMessageRightNowRequest() {
    }

    public List<SimpleMessage> getSimpleMessage() {
        return this.simpleMessage;
    }

    public void setSimpleMessage(List<SimpleMessage> simpleMessage) {
        this.simpleMessage = simpleMessage;
    }
}


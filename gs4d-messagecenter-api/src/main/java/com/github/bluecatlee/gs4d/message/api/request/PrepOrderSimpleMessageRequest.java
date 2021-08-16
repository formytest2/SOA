package com.github.bluecatlee.gs4d.message.api.request;

import com.github.bluecatlee.gs4d.common.bean.AbstractRequest;
import com.github.bluecatlee.gs4d.message.api.model.SimpleMessage;

import java.util.List;

public class PrepOrderSimpleMessageRequest extends AbstractRequest {
    private static final long serialVersionUID = -44876610938571087L;
    private List<SimpleMessage> simpleMessage;

    public PrepOrderSimpleMessageRequest() {
    }

    public List<SimpleMessage> getSimpleMessage() {
        return this.simpleMessage;
    }

    public void setSimpleMessage(List<SimpleMessage> simpleMessage) {
        this.simpleMessage = simpleMessage;
    }
}

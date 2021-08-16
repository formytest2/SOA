package com.github.bluecatlee.gs4d.message.api.request;

import com.github.bluecatlee.gs4d.common.bean.AbstractRequest;
import com.github.bluecatlee.gs4d.message.api.model.SimpleMessage;

public class OTMSimpleMessageRightNowSendRequest extends AbstractRequest {
    private static final long serialVersionUID = -3175851988905227087L;
    private SimpleMessage simpleMessage;

    public OTMSimpleMessageRightNowSendRequest() {
    }

    public SimpleMessage getSimpleMessage() {
        return this.simpleMessage;
    }

    public void setSimpleMessage(SimpleMessage simpleMessage) {
        this.simpleMessage = simpleMessage;
    }
}


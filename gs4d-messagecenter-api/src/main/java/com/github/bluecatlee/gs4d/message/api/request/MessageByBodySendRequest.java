package com.github.bluecatlee.gs4d.message.api.request;

import com.github.bluecatlee.gs4d.common.bean.AbstractRequest;
import com.github.bluecatlee.gs4d.message.api.model.SimpleObjectMessage;

public class MessageByBodySendRequest extends AbstractRequest {
    private static final long serialVersionUID = 7595036588549469977L;
    private SimpleObjectMessage message;

    public SimpleObjectMessage getMessage() {
        return this.message;
    }

    public void setMessage(SimpleObjectMessage message) {
        this.message = message;
    }
}


package com.github.bluecatlee.gs4d.message.api.request;


import com.github.bluecatlee.gs4d.common.bean.AbstractRequest;
import com.github.bluecatlee.gs4d.message.api.model.SimpleMessage;

public class PrepSimpleMessageRequest extends AbstractRequest {
    private static final long serialVersionUID = -3175851988905227087L;
    private SimpleMessage simpleMessage;
    private Long orderMessId;

    public PrepSimpleMessageRequest() {
    }

    public SimpleMessage getSimpleMessage() {
        return this.simpleMessage;
    }

    public void setSimpleMessage(SimpleMessage simpleMessage) {
        this.simpleMessage = simpleMessage;
    }

    public Long getOrderMessId() {
        return this.orderMessId;
    }

    public void setOrderMessId(Long orderMessId) {
        this.orderMessId = orderMessId;
    }
}


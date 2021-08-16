package com.github.bluecatlee.gs4d.message.api.response;

import com.github.bluecatlee.gs4d.common.bean.MessagePack;

public class OrderMessageRightNowResponse extends MessagePack {
    private static final long serialVersionUID = -2490137363718129790L;
    private Long orderMessageGroupId;

    public OrderMessageRightNowResponse() {
    }

    public Long getOrderMessageGroupId() {
        return this.orderMessageGroupId;
    }

    public void setOrderMessageGroupId(Long orderMessageGroupId) {
        this.orderMessageGroupId = orderMessageGroupId;
    }
}


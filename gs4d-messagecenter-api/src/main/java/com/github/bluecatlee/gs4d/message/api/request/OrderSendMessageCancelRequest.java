package com.github.bluecatlee.gs4d.message.api.request;

import com.github.bluecatlee.gs4d.common.bean.AbstractRequest;

public class OrderSendMessageCancelRequest extends AbstractRequest {
    private static final long serialVersionUID = -7906514617032476009L;
    private Long orderMessageGroupId;

    public OrderSendMessageCancelRequest() {
    }

    public Long getOrderMessageGroupId() {
        return this.orderMessageGroupId;
    }

    public void setOrderMessageGroupId(Long orderMessageGroupId) {
        this.orderMessageGroupId = orderMessageGroupId;
    }
}

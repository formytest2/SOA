package com.github.bluecatlee.gs4d.message.api.request;

import com.github.bluecatlee.gs4d.common.bean.AbstractRequest;

public class OrderSendMessageConfirmRequest extends AbstractRequest {
    private static final long serialVersionUID = -2845807430348334291L;
    private Long orderMessageGroupId;

    public OrderSendMessageConfirmRequest() {
    }

    public Long getOrderMessageGroupId() {
        return this.orderMessageGroupId;
    }

    public void setOrderMessageGroupId(Long orderMessageGroupId) {
        this.orderMessageGroupId = orderMessageGroupId;
    }
}

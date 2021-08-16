package com.github.bluecatlee.gs4d.message.api.response;

import com.github.bluecatlee.gs4d.common.bean.MessagePack;
import com.github.bluecatlee.gs4d.message.api.model.OrderMessageModel;

import java.util.List;

public class PrepOrderSimpleMessageResponse extends MessagePack {
    private static final long serialVersionUID = 2297654900648987316L;
    private Long orderMessageGroupId;
    private List<OrderMessageModel> orderMessageModel;

    public PrepOrderSimpleMessageResponse() {
    }

    public Long getOrderMessageGroupId() {
        return this.orderMessageGroupId;
    }

    public void setOrderMessageGroupId(Long orderMessageGroupId) {
        this.orderMessageGroupId = orderMessageGroupId;
    }

    public List<OrderMessageModel> getOrderMessageModel() {
        return this.orderMessageModel;
    }

    public void setOrderMessageModel(List<OrderMessageModel> orderMessageModel) {
        this.orderMessageModel = orderMessageModel;
    }
}


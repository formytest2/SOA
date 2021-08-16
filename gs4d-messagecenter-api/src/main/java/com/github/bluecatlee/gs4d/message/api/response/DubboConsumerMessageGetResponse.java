package com.github.bluecatlee.gs4d.message.api.response;

import com.github.bluecatlee.gs4d.common.bean.MessagePack;
import com.github.bluecatlee.gs4d.message.api.model.PlatformMqDubboConsumer;

import java.util.List;

public class DubboConsumerMessageGetResponse extends MessagePack {
    private static final long serialVersionUID = 8645289043059326932L;
//    @ApiField(description = "监控人集合")
    private List<PlatformMqDubboConsumer> dubboList;

    public List<PlatformMqDubboConsumer> getDubboList() {
        return this.dubboList;
    }

    public void setDubboList(List<PlatformMqDubboConsumer> dubboList) {
        this.dubboList = dubboList;
    }
}


package com.github.bluecatlee.gs4d.message.api.response;

import com.github.bluecatlee.gs4d.common.bean.MessagePack;
import com.github.bluecatlee.gs4d.message.api.model.PlatformMqDubboConsumer;

import java.util.List;

public class DubboConfigByKeyResponse extends MessagePack {
    private static final long serialVersionUID = 4089558063207796664L;
//    @ApiField(description = "监控人集合")
    private List<PlatformMqDubboConsumer> dubboList;

    public List<PlatformMqDubboConsumer> getDubboList() {
        return this.dubboList;
    }

    public void setDubboList(List<PlatformMqDubboConsumer> dubboList) {
        this.dubboList = dubboList;
    }
}


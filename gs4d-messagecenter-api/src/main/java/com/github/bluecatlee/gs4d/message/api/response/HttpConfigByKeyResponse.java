package com.github.bluecatlee.gs4d.message.api.response;

import com.github.bluecatlee.gs4d.common.bean.MessagePack;
import com.github.bluecatlee.gs4d.message.api.model.PlatformMqHttpConsumer;

import java.util.List;

public class HttpConfigByKeyResponse extends MessagePack {
    private static final long serialVersionUID = 4089558063207796664L;
    private List<PlatformMqHttpConsumer> httpList;

    public List<PlatformMqHttpConsumer> getHttpList() {
        return this.httpList;
    }

    public void setHttpList(List<PlatformMqHttpConsumer> httpList) {
        this.httpList = httpList;
    }
}


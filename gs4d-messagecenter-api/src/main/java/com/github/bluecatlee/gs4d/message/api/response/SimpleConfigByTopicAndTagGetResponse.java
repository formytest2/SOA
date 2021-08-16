package com.github.bluecatlee.gs4d.message.api.response;

import com.github.bluecatlee.gs4d.common.bean.MessagePack;
import com.github.bluecatlee.gs4d.message.api.model.SimpleConfig;

import java.util.List;

public class SimpleConfigByTopicAndTagGetResponse extends MessagePack {
    private static final long serialVersionUID = -7183723296676195209L;
    private List<SimpleConfig> simpleConfig;
    private Long total;

    public List<SimpleConfig> getSimpleConfig() {
        return this.simpleConfig;
    }

    public void setSimpleConfig(List<SimpleConfig> simpleConfig) {
        this.simpleConfig = simpleConfig;
    }

    public Long getTotal() {
        return this.total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}

package com.github.bluecatlee.gs4d.message.api.response;

import com.github.bluecatlee.gs4d.common.bean.MessagePack;

import java.util.List;

public class ConsumerIpsGetRsponse extends MessagePack {
    private static final long serialVersionUID = -5593433567713992562L;
    private List<String> consumerIps;
    private String consumerRemark;

    public List<String> getConsumerIps() {
        return this.consumerIps;
    }

    public void setConsumerIps(List<String> consumerIps) {
        this.consumerIps = consumerIps;
    }

    public String getConsumerRemark() {
        return this.consumerRemark;
    }

    public void setConsumerRemark(String consumerRemark) {
        this.consumerRemark = consumerRemark;
    }
}


package com.github.bluecatlee.gs4d.message.api.request;

import com.github.bluecatlee.gs4d.common.bean.AbstractRequest;

public class AllFatherTopicFindRequest extends AbstractRequest {
    private static final long serialVersionUID = -3379740043130808011L;
    private String fatherTopicKey;

    public String getFatherTopicKey() {
        return this.fatherTopicKey;
    }

    public void setFatherTopicKey(String fatherTopicKey) {
        this.fatherTopicKey = fatherTopicKey;
    }
}


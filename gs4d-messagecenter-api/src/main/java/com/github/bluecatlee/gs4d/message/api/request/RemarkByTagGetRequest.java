package com.github.bluecatlee.gs4d.message.api.request;

import com.github.bluecatlee.gs4d.common.bean.AbstractRequest;

public class RemarkByTagGetRequest extends AbstractRequest {
    private static final long serialVersionUID = -26474468657844047L;
    private String tag;

    public String getTag() {
        return this.tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}


package com.github.bluecatlee.gs4d.message.api.request;


import com.github.bluecatlee.gs4d.common.bean.AbstractRequest;

import javax.validation.constraints.NotEmpty;

public class MesssageRetrySendRequest extends AbstractRequest {
    private static final long serialVersionUID = -3139955947981685362L;
    @NotEmpty(message = "序列号不能为空！")
    private String seriesList;

    public String getSeriesList() {
        return this.seriesList;
    }

    public void setSeriesList(String seriesList) {
        this.seriesList = seriesList;
    }
}


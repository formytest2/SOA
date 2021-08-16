package com.github.bluecatlee.gs4d.message.api.response;

import com.github.bluecatlee.gs4d.common.bean.MessagePack;

public class MessageCountGetResponse extends MessagePack {
    private static final long serialVersionUID = 7941240227460936098L;
    private Long total;

    public Long getTotal() {
        return this.total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}


package com.github.bluecatlee.gs4d.message.api.request;

import com.github.bluecatlee.gs4d.common.bean.AbstractRequest;

public class FailedMessageToSuccessDbRequest extends AbstractRequest {
    private static final long serialVersionUID = -4219989447274071033L;
    private String messageCreateDtme;

    public String getMessageCreateDtme() {
        return this.messageCreateDtme;
    }

    public void setMessageCreateDtme(String messageCreateDtme) {
        this.messageCreateDtme = messageCreateDtme;
    }
}


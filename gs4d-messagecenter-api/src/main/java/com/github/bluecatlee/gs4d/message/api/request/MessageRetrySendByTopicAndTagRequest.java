package com.github.bluecatlee.gs4d.message.api.request;

import com.github.bluecatlee.gs4d.common.bean.AbstractRequest;

import javax.validation.constraints.NotBlank;

public class MessageRetrySendByTopicAndTagRequest extends AbstractRequest {
    private static final long serialVersionUID = 1L;
    @NotBlank(message = "消息主题不能为空")
    private String messageTopic;
    @NotBlank(message = "消息主题tag不能为空")
    private String messageTag;
    @NotBlank(message = "重发消息日期不能为空")
    private String messageDtme;

    public String getMessageTopic() {
        return this.messageTopic;
    }

    public void setMessageTopic(String messageTopic) {
        this.messageTopic = messageTopic;
    }

    public String getMessageTag() {
        return this.messageTag;
    }

    public void setMessageTag(String messageTag) {
        this.messageTag = messageTag;
    }

    public String getMessageDtme() {
        return this.messageDtme;
    }

    public void setMessageDtme(String messageDtme) {
        this.messageDtme = messageDtme;
    }
}


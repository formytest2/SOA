package com.github.bluecatlee.gs4d.message.api.response;
import com.github.bluecatlee.gs4d.common.bean.MessagePack;
import com.github.bluecatlee.gs4d.message.api.model.MessageTopicDetailModel;

import java.util.List;

public class AllTopicMessageResponse extends MessagePack {
    private static final long serialVersionUID = 1L;
    private List<MessageTopicDetailModel> messageLists;
    private Integer total;

    public List<MessageTopicDetailModel> getMessageLists() {
        return this.messageLists;
    }

    public void setMessageLists(List<MessageTopicDetailModel> messageLists) {
        this.messageLists = messageLists;
    }

    public Integer getTotal() {
        return this.total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}

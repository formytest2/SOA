package com.github.bluecatlee.gs4d.transaction.api.response;

import com.github.bluecatlee.gs4d.common.bean.MessagePack;
import com.github.bluecatlee.gs4d.transaction.api.model.TranscationWebModel;

import java.util.List;

public class TransactionGetResponse extends MessagePack {
    private static final long serialVersionUID = -7915363445156914174L;

    private Long total;

    private List<TranscationWebModel> models;

    public Long getTotal() {
        return this.total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<TranscationWebModel> getModels() {
        return this.models;
    }

    public void setModels(List<TranscationWebModel> models) {
        this.models = models;
    }
}


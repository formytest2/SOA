package com.github.bluecatlee.gs4d.transaction.response;

import com.github.bluecatlee.gs4d.common.bean.MessagePack;
import com.github.bluecatlee.gs4d.transaction.model.TranscationDetailModel;

import java.util.List;

public class TransactionDetailGetResponse extends MessagePack {
    private static final long serialVersionUID = -3044700730949207028L;

    private Long total;

    private List<TranscationDetailModel> models;

    public Long getTotal() {
        return this.total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<TranscationDetailModel> getModels() {
        return this.models;
    }

    public void setModels(List<TranscationDetailModel> models) {
        this.models = models;
    }
}


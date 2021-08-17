package com.github.bluecatlee.gs4d.transaction.api.response;

import com.github.bluecatlee.gs4d.common.bean.MessagePack;
import com.github.bluecatlee.gs4d.transaction.api.model.TranscationSignListModel;

import java.util.List;

public class TransactionSignListGetResponse extends MessagePack {
    private static final long serialVersionUID = -4490010394066588900L;

    private List<TranscationSignListModel> transcationSignListModel;

    public List<TranscationSignListModel> getTranscationSignListModel() {
        return this.transcationSignListModel;
    }

    public void setTranscationSignListModel(List<TranscationSignListModel> transcationSignListModel) {
        this.transcationSignListModel = transcationSignListModel;
    }
}

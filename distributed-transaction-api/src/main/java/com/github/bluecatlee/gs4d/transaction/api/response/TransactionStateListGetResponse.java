package com.github.bluecatlee.gs4d.transaction.api.response;

import com.github.bluecatlee.gs4d.common.bean.MessagePack;
import com.github.bluecatlee.gs4d.transaction.api.model.TranscationStateListModel;

import java.util.List;

public class TransactionStateListGetResponse extends MessagePack {
    private static final long serialVersionUID = 5766292474557573708L;

    private List<TranscationStateListModel> transcationStateListModel;

    public List<TranscationStateListModel> getTranscationStateListModel() {
        return this.transcationStateListModel;
    }

    public void setTranscationStateListModel(List<TranscationStateListModel> transcationStateListModel) {
        this.transcationStateListModel = transcationStateListModel;
    }
}


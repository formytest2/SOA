package com.github.bluecatlee.gs4d.transaction.api.response;

import com.github.bluecatlee.gs4d.common.bean.MessagePack;
import com.github.bluecatlee.gs4d.transaction.api.model.SharedModel;

import java.util.List;

public class SharedGetResponse extends MessagePack {
    private static final long serialVersionUID = -7542775100525814996L;

    private Long total;

    private List<SharedModel> sharedModels;

    public Long getTotal() {
        return this.total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<SharedModel> getSharedModels() {
        return this.sharedModels;
    }

    public void setSharedModels(List<SharedModel> sharedModels) {
        this.sharedModels = sharedModels;
    }
}


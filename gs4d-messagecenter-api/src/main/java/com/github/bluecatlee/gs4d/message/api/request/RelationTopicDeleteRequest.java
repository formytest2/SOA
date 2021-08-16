package com.github.bluecatlee.gs4d.message.api.request;

import com.github.bluecatlee.gs4d.common.bean.AbstractRequest;

import javax.validation.constraints.NotNull;

public class RelationTopicDeleteRequest extends AbstractRequest {
    private static final long serialVersionUID = 8462621051376857808L;
    @NotNull(message = "关联序列不能为空")
    private Long relationSeries;

    public Long getRelationSeries() {
        return this.relationSeries;
    }

    public void setRelationSeries(Long relationSeries) {
        this.relationSeries = relationSeries;
    }
}


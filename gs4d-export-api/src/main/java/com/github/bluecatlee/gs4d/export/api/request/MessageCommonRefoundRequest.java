package com.github.bluecatlee.gs4d.export.api.request;

import com.github.bluecatlee.gs4d.common.bean.AbstractRequest;

import javax.validation.constraints.NotNull;

public class MessageCommonRefoundRequest extends AbstractRequest {
    private static final long serialVersionUID = 6372105042019006613L;

//    @ApiField(description = "系统编号")
    @NotNull(message = "系统编号不可为空")
    private Long sysNumId;

//    @ApiField(description = "消息序号")
    @NotNull(message = "消息序号不可为空")
    private String msgSeries;

//    @ApiField(description = "同一系统下的数据其他数据源")
    private String datasouceName;

    public Long getSysNumId() {
        return this.sysNumId;
    }

    public void setSysNumId(Long sysNumId) {
        this.sysNumId = sysNumId;
    }

    public String getMsgSeries() {
        return this.msgSeries;
    }

    public void setMsgSeries(String msgSeries) {
        this.msgSeries = msgSeries;
    }

    public String getDatasouceName() {
        return this.datasouceName;
    }

    public void setDatasouceName(String datasouceName) {
        this.datasouceName = datasouceName;
    }
}


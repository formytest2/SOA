package com.github.bluecatlee.gs4d.common.bean;

public class AbstractEmployeeSessionRequest extends AbstractRequest {

    private static final long serialVersionUID = -1722861982593347078L;

    //    @ApiField(description = "操作人员编号")
    private Long userNumId;

    public AbstractEmployeeSessionRequest() {
    }

    public Long getUserNumId() {
        if (this.userNumId == null) {
            this.userNumId = 0L;
        }

        return this.userNumId;
    }

    public void setUserNumId(Long userNumId) {
        this.userNumId = userNumId;
    }

}


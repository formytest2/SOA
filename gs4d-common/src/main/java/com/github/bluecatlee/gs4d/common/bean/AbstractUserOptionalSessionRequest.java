package com.github.bluecatlee.gs4d.common.bean;

/**
 * 用户请求(客户编号不能可选)
 */
public class AbstractUserOptionalSessionRequest extends AbstractRequest {

    private static final long serialVersionUID = 3781733049804108331L;

    //    @ApiField(description = "客户编号")
    private Long usrNumId;

    public AbstractUserOptionalSessionRequest() {
    }

    public Long getUsrNumId() {
        return this.usrNumId;
    }

    public void setUsrNumId(Long usrNumId) {
        this.usrNumId = usrNumId;
    }
}
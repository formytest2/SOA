package com.github.bluecatlee.gs4d.common.model;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class SycDetailInfo implements Serializable {
    private static final long serialVersionUID = 5048463697217032596L;
    private Long tenantNumId;
    private Long dataSign;
//    @ApiField(
//            description = "sql语句和参数"
//    )
    @NotNull(message = "sql语句和参数不可为空!")
    private SqlAndParamters sp;

    public SycDetailInfo() {
    }

    public Long getTenantNumId() {
        return this.tenantNumId;
    }

    public void setTenantNumId(Long tenantNumId) {
        this.tenantNumId = tenantNumId;
    }

    public Long getDataSign() {
        return this.dataSign;
    }

    public void setDataSign(Long dataSign) {
        this.dataSign = dataSign;
    }

    public SqlAndParamters getSp() {
        return this.sp;
    }

    public void setSp(SqlAndParamters sp) {
        this.sp = sp;
    }
}


package com.github.bluecatlee.gs4d.monitor.api.request;

import com.github.bluecatlee.gs4d.common.bean.AbstractRequest;
import javax.validation.constraints.NotBlank;

public class SystemLiveDetectRequest extends AbstractRequest {
    private static final long serialVersionUID = 1L;

//    @ApiField(description = "子系统名称")
    @NotBlank(message = "子系统名称不能为空")
    private String subSystem;

    public String getSubSystem() {
        return this.subSystem;
    }

    public void setSubSystem(String subSystem) {
        this.subSystem = subSystem;
    }
}

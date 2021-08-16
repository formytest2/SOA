package com.github.bluecatlee.gs4d.export.api.request;

import com.github.bluecatlee.gs4d.common.bean.AbstractRequest;
import lombok.Data;
import net.sf.json.JSONObject;

import javax.validation.constraints.NotNull;

@Data
public class DataExportRequest extends AbstractRequest {
    private static final long serialVersionUID = -3120661649802101457L;

    @NotNull(message = "导出参数值orgInputJson不能为空")
    private JSONObject orgInputJson;
    @NotNull(message = "导出系统任务作业号sysTaskNumId不能为空")
    private Long sysTaskNumId;
    private Long isNotify = 0L;

}


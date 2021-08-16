package com.github.bluecatlee.gs4d.export.api.request;

import com.github.bluecatlee.gs4d.common.bean.AbstractRequest;
import lombok.Data;

@Data
public class DataCheckRequest extends AbstractRequest {
    private static final long serialVersionUID = -3873369794711812064L;
    private String configDtl;
}

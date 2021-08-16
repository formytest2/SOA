package com.github.bluecatlee.gs4d.export.api.request;

import com.github.bluecatlee.gs4d.common.bean.AbstractRequest;
import lombok.Data;

import java.util.Map;

@Data
public class CommonExcuteBySqlIdRequest extends AbstractRequest {
    private static final long serialVersionUID = -6003447135281706180L;
    private String dataSourceName;
    private String sqlId;
    private Map<String, Object> inputParam;
    private Long SubRuerySign = 1L; // 1：非子查询 2：子查询
    private int count = -1;
    private Long pageNum;       // 页码
    private Long pageSize;      // 每页数量

}


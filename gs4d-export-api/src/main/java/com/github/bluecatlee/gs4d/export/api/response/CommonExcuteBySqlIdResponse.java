package com.github.bluecatlee.gs4d.export.api.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.bluecatlee.gs4d.common.bean.MessagePack;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class CommonExcuteBySqlIdResponse extends MessagePack {

    private static final long serialVersionUID = -9120059174670487232L;

    private long pageCount;     // 总页数
    private long recordCount;   // 数据行数
    private List<Map<String, Object>> results;  // 数据行
    private String sqlName;
    @JsonIgnore
    private String sql;         // sql_content
    @JsonIgnore
    private Object[] arg;       // 入参转换后的参数数组
    private String sqlFlag;

}


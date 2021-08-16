package com.github.bluecatlee.gs4d.cache.api.request;

import com.github.bluecatlee.gs4d.common.bean.AbstractRequest;

import javax.validation.constraints.NotNull;

public class CommonCacheRefreshRequest extends AbstractRequest {
    @NotNull(message = "数据库名不能为空！")
    private String database;
    @NotNull(message = "表名不能为空！")
    private String tableName;
    @NotNull(message = "行号不能为空！")
    private Long tableSeries;
    @NotNull(message = "操作类型不能为空！")
    private Integer optType;

    public CommonCacheRefreshRequest() {
    }

    public String getDatabase() {
        return this.database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getTableName() {
        return this.tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Long getTableSeries() {
        return this.tableSeries;
    }

    public void setTableSeries(Long tableSeries) {
        this.tableSeries = tableSeries;
    }

    public Integer getOptType() {
        return this.optType;
    }

    public void setOptType(Integer optType) {
        this.optType = optType;
    }
}

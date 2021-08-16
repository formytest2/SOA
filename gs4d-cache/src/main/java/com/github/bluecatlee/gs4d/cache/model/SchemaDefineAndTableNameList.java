package com.github.bluecatlee.gs4d.cache.model;

import com.github.bluecatlee.gs4d.cache.entity.EcCacheMethodSchemaDefine;

import java.util.List;

public class SchemaDefineAndTableNameList {
    private EcCacheMethodSchemaDefine schemaDefine;
    private List<String> tableNameList;

    public EcCacheMethodSchemaDefine getSchemaDefine() {
        return this.schemaDefine;
    }

    public void setSchemaDefine(EcCacheMethodSchemaDefine ecCacheMethodSchemaDefine) {
        this.schemaDefine = ecCacheMethodSchemaDefine;
    }

    public List<String> getTableNameList() {
        return this.tableNameList;
    }

    public void setTableNameList(List<String> tableNameList) {
        this.tableNameList = tableNameList;
    }
}

package com.tranboot.client.model.dbsync;

import java.util.Map;
import java.util.Set;

public class SqlMapper {
    private String targetDb;
    private String sourceDb;
    private String targetDbType;
    private String sourceDbType;
    private String targetTable;
    private String sourceTable;
    private String sourceKeyField;
    private String targetKeyField;
    private Map<String, String> fieldMapper;
    private Set<String> excludeFields;

    public SqlMapper() {
    }

    public boolean needExclude(String field) {
        return this.excludeFields.contains(field.toLowerCase());
    }

    public String mapField(String field) {
        return this.fieldMapper.get(field.toLowerCase()) == null ? field : (String)this.fieldMapper.get(field.toLowerCase());
    }

    public String getTargetDb() {
        return this.targetDb;
    }

    public void setTargetDb(String targetDb) {
        this.targetDb = targetDb;
    }

    public String getSourceDb() {
        return this.sourceDb;
    }

    public void setSourceDb(String sourceDb) {
        this.sourceDb = sourceDb;
    }

    public String getTargetTable() {
        return this.targetTable;
    }

    public void setTargetTable(String targetTable) {
        this.targetTable = targetTable;
    }

    public String getSourceTable() {
        return this.sourceTable;
    }

    public void setSourceTable(String sourceTable) {
        this.sourceTable = sourceTable;
    }

    public String getSourceKeyField() {
        return this.sourceKeyField;
    }

    public void setSourceKeyField(String sourceKeyField) {
        this.sourceKeyField = sourceKeyField;
    }

    public String getTargetKeyField() {
        return this.targetKeyField;
    }

    public void setTargetKeyField(String targetKeyField) {
        this.targetKeyField = targetKeyField;
    }

    public Map<String, String> getFieldMapper() {
        return this.fieldMapper;
    }

    public void setFieldMapper(Map<String, String> fieldMapper) {
        this.fieldMapper = fieldMapper;
    }

    public Set<String> getExcludeFields() {
        return this.excludeFields;
    }

    public void setExcludeFields(Set<String> excludeFields) {
        this.excludeFields = excludeFields;
    }

    public String getTargetDbType() {
        return this.targetDbType;
    }

    public void setTargetDbType(String targetDbType) {
        this.targetDbType = targetDbType;
    }

    public String getSourceDbType() {
        return this.sourceDbType;
    }

    public void setSourceDbType(String sourceDbType) {
        this.sourceDbType = sourceDbType;
    }

    public String toString() {
        return "SqlMapper [" + this.targetDb + "->" + this.sourceDb + ", " + this.targetTable + "->" + this.sourceTable + this.sourceKeyField + "->" + this.targetKeyField + "]";
    }
}


package com.github.bluecatlee.gs4d.exchange.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class CommonQuery implements Serializable {

    private static final long serialVersionUID = 5182957677633744995L;
    public static final String SQL_FLAG_INSERT = "I";
    public static final String SQL_FLAG_UPDATE = "U";
    public static final String SQL_FLAG_SELECT = "S";
    private String series;
    private String sqlName;
    private String sqlId;
    private String sqlContent;
    private String paramContent;
    private String jdbcName;
    private Long createUserId;
    private String cancelSign;
    private Long tenantNumId;
    private Long dataSign;
    private String dbType;
    private String annotatePrefix;
    public Map<String, String> paramMap = new HashMap();
    private Long pageSize;
    private String sqlFlag;
    private String subSqlId;
    private String noDataException;     // Y表示如果没查到数据则抛异常
    private String noDataExceptionMsg;
    private Long cacheSign;             // 是否缓存 0不使用 1直接缓存，2缓存服务
    private String methodName;          // 缓存方法名(这部分未实现)
    private Long cacheLiveTime;         // 缓存存活时间 s
    private String returnHandleContent; // 需要把值转成字符串的字段
    private Long subQuerySign = 1L;     // 1.非子查询 2.子查询
    private String excelColumn;

    public CommonQuery() {
    }

    public String getExcelColumn() {
        return this.excelColumn;
    }

    public void setExcelColumn(String excelColumn) {
        this.excelColumn = excelColumn;
    }

    public String getSeries() {
        return this.series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getSqlName() {
        return this.sqlName;
    }

    public void setSqlName(String sqlName) {
        this.sqlName = sqlName;
    }

    public String getSqlId() {
        return this.sqlId;
    }

    public void setSqlId(String sqlId) {
        this.sqlId = sqlId;
    }

    public String getSqlContent() {
        return this.sqlContent;
    }

    public void setSqlContent(String sqlContent) {
        this.sqlContent = sqlContent;
        if (sqlContent.trim().toLowerCase().startsWith("insert")) {
            this.sqlFlag = SQL_FLAG_INSERT;
        } else if (sqlContent.trim().toLowerCase().startsWith("update")) {
            this.sqlFlag = SQL_FLAG_UPDATE;
        } else if (sqlContent.trim().toLowerCase().startsWith("delete")) {
            this.sqlFlag = SQL_FLAG_UPDATE;
        } else if (sqlContent.trim().toLowerCase().startsWith("select")) {
            this.sqlFlag = SQL_FLAG_SELECT;
        } else if (sqlContent.trim().toLowerCase().startsWith("merge into")) {
            this.sqlFlag = SQL_FLAG_INSERT;
        }

    }

    public String getParamContent() {
        return this.paramContent;
    }

    public void setParamContent(String paramContent) {
        this.paramContent = paramContent;
    }

    public String getJdbcName() {
        return this.jdbcName;
    }

    public void setJdbcName(String jdbcName) {
        this.jdbcName = jdbcName;
    }

    public Long getCreateUserId() {
        return this.createUserId;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    public String getCancelSign() {
        return this.cancelSign;
    }

    public void setCancelSign(String cancelSign) {
        this.cancelSign = cancelSign;
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

    public String getDbType() {
        return this.dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    public String getSqlFlag() {
        return this.sqlFlag;
    }

    public void setSqlFlag(String sqlFlag) {
        this.sqlFlag = sqlFlag;
    }

    public Long getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(Long pageSize) {
        this.pageSize = pageSize;
    }

    public String getAnnotatePrefix() {
        return this.annotatePrefix;
    }

    public void setAnnotatePrefix(String annotatePrefix) {
        this.annotatePrefix = annotatePrefix;
    }

    public String getSubSqlId() {
        return this.subSqlId;
    }

    public void setSubSqlId(String subSqlId) {
        this.subSqlId = subSqlId;
    }

    public String getNoDataException() {
        return this.noDataException;
    }

    public void setNoDataException(String noDataException) {
        this.noDataException = noDataException;
    }

    public String getNoDataExceptionMsg() {
        return this.noDataExceptionMsg;
    }

    public void setNoDataExceptionMsg(String noDataExceptionMsg) {
        this.noDataExceptionMsg = noDataExceptionMsg;
    }

    public Long getCacheSign() {
        return this.cacheSign;
    }

    public void setCacheSign(Long cacheSign) {
        this.cacheSign = cacheSign;
    }

    public String getMethodName() {
        return this.methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Long getCacheLiveTime() {
        return this.cacheLiveTime;
    }

    public void setCacheLiveTime(Long cacheLiveTime) {
        this.cacheLiveTime = cacheLiveTime;
    }

    public String getReturnHandleContent() {
        return this.returnHandleContent;
    }

    public void setReturnHandleContent(String returnHandleContent) {
        this.returnHandleContent = returnHandleContent;
    }

    public Long getSubQuerySign() {
        return this.subQuerySign;
    }

    public void setSubQuerySign(Long subQuerySign) {
        this.subQuerySign = subQuerySign;
    }
}


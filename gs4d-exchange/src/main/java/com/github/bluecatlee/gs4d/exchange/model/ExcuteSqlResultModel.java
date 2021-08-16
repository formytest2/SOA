package com.github.bluecatlee.gs4d.exchange.model;

import net.sf.json.JSONObject;

import java.util.List;

public class ExcuteSqlResultModel {

    private long pageCount; // 总页数

    private long recordCount; // 数据行数(通过select count查询得到) 或者 写操作返回的影响行数

    private List<JSONObject> data; // 返回数据

    private long count;     // 返回的数据行数 即data.size()

    private String sql;

    private Object[] arg = new Object[0];

    private String sqlFlag; // sql标识 S I U

    public long getPageCount() {
        return this.pageCount;
    }

    public void setPageCount(long pageCount) {
        this.pageCount = pageCount;
    }

    public long getRecordCount() {
        return this.recordCount;
    }

    public void setRecordCount(long recordCount) {
        this.recordCount = recordCount;
    }

    public List<JSONObject> getData() {
        return this.data;
    }

    public void setData(List<JSONObject> data) {
        this.data = data;
    }

    public String getSql() {
        return this.sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Object[] getArg() {
        return this.arg;
    }

    public void setArg(Object[] arg) {
        this.arg = arg;
    }

    public String getSqlFlag() {
        return this.sqlFlag;
    }

    public void setSqlFlag(String sqlFlag) {
        this.sqlFlag = sqlFlag;
    }

    public long getCount() {
        return this.count;
    }

    public void setCount(long count) {
        this.count = count;
    }

}


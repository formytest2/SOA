package com.github.bluecatlee.gs4d.export.model;


import java.util.List;

public class BatchExcuteSqlModel {

    private String sql;
    private List<Object[]> argList;
    private String noDataUpdateSql;
    private List<Object[]> noDataUpdateArgList;
    private boolean hasNoDataUpdate = false;        // ?

    public BatchExcuteSqlModel() {
    }

    public String getSql() {
        return this.sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public List<Object[]> getArgList() {
        return this.argList;
    }

    public void setArgList(List<Object[]> argList) {
        this.argList = argList;
    }

    public String getNoDataUpdateSql() {
        return this.noDataUpdateSql;
    }

    public void setNoDataUpdateSql(String noDataUpdateSql) {
        this.noDataUpdateSql = noDataUpdateSql;
    }

    public List<Object[]> getNoDataUpdateArgList() {
        return this.noDataUpdateArgList;
    }

    public void setNoDataUpdateArgList(List<Object[]> noDataUpdateArgList) {
        this.noDataUpdateArgList = noDataUpdateArgList;
    }

    public boolean hasNoDataUpdate() {
        return this.hasNoDataUpdate;
    }

    public void setHasNoDataUpdate(boolean hasNoDataUpdate) {
        this.hasNoDataUpdate = hasNoDataUpdate;
    }
}

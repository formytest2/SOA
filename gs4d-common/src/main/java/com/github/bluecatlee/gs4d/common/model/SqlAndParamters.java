package com.github.bluecatlee.gs4d.common.model;

import java.io.Serializable;
import java.util.List;

public class SqlAndParamters implements Serializable {

    private String sql;
    private List<Parameter> paramterList;
    private List<List<Parameter>> list;

    public SqlAndParamters() {
    }

    public String getSql() {
        return this.sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public List<List<Parameter>> getList() {
        return this.list;
    }

    public void setList(List<List<Parameter>> list) {
        this.list = list;
    }

    public List<Parameter> getParamterList() {
        return this.paramterList;
    }

    public void setParamterList(List<Parameter> paramterList) {
        this.paramterList = paramterList;
    }
}


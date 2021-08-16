package com.github.bluecatlee.gs4d.common.datasource;

public class DataSourceInstance {

    private String dataSourceName;
    private String dbType;

    public DataSourceInstance() {
    }

    public String getDataSourceName() {
        return this.dataSourceName;
    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    public String getDbType() {
        return this.dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

}

package com.github.bluecatlee.gs4d.common.datasource;

public class DataSourceContextHolder {

    private static final ThreadLocal<String> contextHolder = new ThreadLocal();

    public DataSourceContextHolder() {
    }

    public static String getDataSourceType() {
        return (String)contextHolder.get();
    }

    public static void clearDataSourceType() {
        contextHolder.remove();
    }

    public static void setDataSourceType(String dataSourceType) {
        contextHolder.set(dataSourceType);
    }

}

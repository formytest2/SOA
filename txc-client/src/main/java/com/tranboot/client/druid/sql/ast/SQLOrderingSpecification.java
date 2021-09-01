package com.tranboot.client.druid.sql.ast;

public enum SQLOrderingSpecification {
    ASC("ASC"),
    DESC("DESC");

    public final String name;
    public final String name_lcase;

    private SQLOrderingSpecification(String name) {
        this.name = name;
        this.name_lcase = name.toLowerCase();
    }
}

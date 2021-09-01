package com.tranboot.client.sqlast;

public interface SqlInsertStatementBuilder {
    SqlInsertStatementBuilder insert(String var1);

    SqlInsertStatementBuilder column(String... var1);
}


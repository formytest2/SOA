package com.tranboot.client.druid.sql.visitor;

public interface PrintableVisitor extends SQLASTVisitor {
    boolean isUppCase();

    void print(char var1);

    void print(String var1);
}


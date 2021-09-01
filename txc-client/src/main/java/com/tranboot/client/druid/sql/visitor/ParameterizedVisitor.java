package com.tranboot.client.druid.sql.visitor;

public interface ParameterizedVisitor extends PrintableVisitor {
    int getReplaceCount();

    void incrementReplaceCunt();
}


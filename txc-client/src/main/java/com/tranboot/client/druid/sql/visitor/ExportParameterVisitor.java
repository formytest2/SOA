package com.tranboot.client.druid.sql.visitor;

import java.util.List;

public interface ExportParameterVisitor extends SQLASTVisitor {
    boolean isParameterizedMergeInList();

    void setParameterizedMergeInList(boolean var1);

    List<Object> getParameters();
}

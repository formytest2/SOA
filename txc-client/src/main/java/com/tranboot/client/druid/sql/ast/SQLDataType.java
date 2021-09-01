package com.tranboot.client.druid.sql.ast;

import java.util.List;

public interface SQLDataType extends SQLObject {
    String getName();

    void setName(String var1);

    List<SQLExpr> getArguments();
}

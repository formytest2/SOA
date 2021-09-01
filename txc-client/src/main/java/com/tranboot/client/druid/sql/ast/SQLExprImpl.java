package com.tranboot.client.druid.sql.ast;

public abstract class SQLExprImpl extends SQLObjectImpl implements SQLExpr {
    public SQLExprImpl() {
    }

    public abstract boolean equals(Object var1);

    public abstract int hashCode();
}


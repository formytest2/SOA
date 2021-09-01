package com.tranboot.client.druid.sql.ast;

import com.tranboot.client.druid.sql.visitor.SQLASTVisitor;

public class SQLSubPartitionByHash extends SQLSubPartitionBy {
    protected SQLExpr expr;
    private boolean key;

    public SQLSubPartitionByHash() {
    }

    public SQLExpr getExpr() {
        return this.expr;
    }

    public void setExpr(SQLExpr expr) {
        if (expr != null) {
            expr.setParent(this);
        }

        this.expr = expr;
    }

    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.expr);
            this.acceptChild(visitor, this.subPartitionsCount);
        }

        visitor.endVisit(this);
    }

    public boolean isKey() {
        return this.key;
    }

    public void setKey(boolean key) {
        this.key = key;
    }
}

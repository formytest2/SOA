package com.tranboot.client.druid.sql.ast;

import com.tranboot.client.druid.sql.visitor.SQLASTVisitor;

public class SQLPartitionByHash extends SQLPartitionBy {
    protected SQLExpr expr;
    protected boolean key;

    public SQLPartitionByHash() {
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

    public boolean isKey() {
        return this.key;
    }

    public void setKey(boolean key) {
        this.key = key;
    }

    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.expr);
            this.acceptChild(visitor, this.partitionsCount);
            this.acceptChild(visitor, this.getPartitions());
            this.acceptChild(visitor, this.subPartitionBy);
        }

        visitor.endVisit(this);
    }
}

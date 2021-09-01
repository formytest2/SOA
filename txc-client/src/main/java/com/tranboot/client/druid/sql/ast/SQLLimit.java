package com.tranboot.client.druid.sql.ast;

import com.tranboot.client.druid.sql.visitor.SQLASTVisitor;

public class SQLLimit extends SQLObjectImpl {
    private SQLExpr rowCount;
    private SQLExpr offset;

    public SQLLimit() {
    }

    public SQLLimit(SQLExpr rowCount) {
        this.setRowCount(rowCount);
    }

    public SQLExpr getRowCount() {
        return this.rowCount;
    }

    public void setRowCount(SQLExpr rowCount) {
        if (rowCount != null) {
            rowCount.setParent(this);
        }

        this.rowCount = rowCount;
    }

    public SQLExpr getOffset() {
        return this.offset;
    }

    public void setOffset(SQLExpr offset) {
        if (offset != null) {
            offset.setParent(this);
        }

        this.offset = offset;
    }

    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.offset);
            this.acceptChild(visitor, this.rowCount);
        }

        visitor.endVisit(this);
    }
}

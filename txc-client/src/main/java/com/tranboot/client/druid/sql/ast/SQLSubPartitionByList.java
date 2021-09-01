package com.tranboot.client.druid.sql.ast;

import com.tranboot.client.druid.sql.visitor.SQLASTVisitor;

public class SQLSubPartitionByList extends SQLSubPartitionBy {
    protected SQLName column;

    public SQLSubPartitionByList() {
    }

    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.column);
            this.acceptChild(visitor, this.subPartitionsCount);
        }

        visitor.endVisit(this);
    }

    public SQLName getColumn() {
        return this.column;
    }

    public void setColumn(SQLName column) {
        if (column != null) {
            column.setParent(this);
        }

        this.column = column;
    }
}

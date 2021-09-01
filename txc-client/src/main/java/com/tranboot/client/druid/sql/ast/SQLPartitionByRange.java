package com.tranboot.client.druid.sql.ast;

import com.tranboot.client.druid.sql.visitor.SQLASTVisitor;
import java.util.ArrayList;
import java.util.List;

public class SQLPartitionByRange extends SQLPartitionBy {
    protected List<SQLName> columns = new ArrayList();
    protected SQLExpr interval;
    protected SQLExpr expr;

    public SQLPartitionByRange() {
    }

    public List<SQLName> getColumns() {
        return this.columns;
    }

    public void addColumn(SQLName column) {
        if (column != null) {
            column.setParent(this);
        }

        this.columns.add(column);
    }

    public SQLExpr getInterval() {
        return this.interval;
    }

    public void setInterval(SQLExpr interval) {
        if (interval != null) {
            interval.setParent(this);
        }

        this.interval = interval;
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
            this.acceptChild(visitor, this.columns);
            this.acceptChild(visitor, this.expr);
            this.acceptChild(visitor, this.interval);
            this.acceptChild(visitor, this.storeIn);
            this.acceptChild(visitor, this.partitions);
        }

        visitor.endVisit(this);
    }
}

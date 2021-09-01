package com.tranboot.client.druid.sql.ast;

import com.tranboot.client.druid.sql.visitor.SQLASTVisitor;
import java.util.ArrayList;
import java.util.List;

public class SQLPartitionByList extends SQLPartitionBy {
    protected SQLExpr expr;
    protected List<SQLName> columns = new ArrayList();

    public SQLPartitionByList() {
    }

    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.expr);
            this.acceptChild(visitor, this.columns);
            this.acceptChild(visitor, this.partitionsCount);
            this.acceptChild(visitor, this.getPartitions());
            this.acceptChild(visitor, this.subPartitionBy);
        }

        visitor.endVisit(this);
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

    public List<SQLName> getColumns() {
        return this.columns;
    }

    public void addColumn(SQLName column) {
        if (column != null) {
            column.setParent(this);
        }

        this.columns.add(column);
    }
}

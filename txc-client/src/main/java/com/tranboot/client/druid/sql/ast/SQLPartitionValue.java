package com.tranboot.client.druid.sql.ast;

import com.tranboot.client.druid.sql.visitor.SQLASTVisitor;
import java.util.ArrayList;
import java.util.List;

public class SQLPartitionValue extends SQLObjectImpl {
    protected SQLPartitionValue.Operator operator;
    protected final List<SQLExpr> items = new ArrayList();

    public SQLPartitionValue(SQLPartitionValue.Operator operator) {
        this.operator = operator;
    }

    public List<SQLExpr> getItems() {
        return this.items;
    }

    public void addItem(SQLExpr item) {
        if (item != null) {
            item.setParent(this);
        }

        this.items.add(item);
    }

    public SQLPartitionValue.Operator getOperator() {
        return this.operator;
    }

    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.getItems());
        }

        visitor.endVisit(this);
    }

    public static enum Operator {
        LessThan,
        In,
        List;

        private Operator() {
        }
    }
}

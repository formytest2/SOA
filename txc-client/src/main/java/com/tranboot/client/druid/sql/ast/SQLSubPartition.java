package com.tranboot.client.druid.sql.ast;

import com.tranboot.client.druid.sql.visitor.SQLASTVisitor;

public class SQLSubPartition extends SQLObjectImpl {
    protected SQLName name;
    protected SQLPartitionValue values;

    public SQLSubPartition() {
    }

    public SQLName getName() {
        return this.name;
    }

    public void setName(SQLName name) {
        if (name != null) {
            name.setParent(this);
        }

        this.name = name;
    }

    public SQLPartitionValue getValues() {
        return this.values;
    }

    public void setValues(SQLPartitionValue values) {
        if (values != null) {
            values.setParent(this);
        }

        this.values = values;
    }

    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.name);
            this.acceptChild(visitor, this.values);
        }

        visitor.endVisit(this);
    }
}

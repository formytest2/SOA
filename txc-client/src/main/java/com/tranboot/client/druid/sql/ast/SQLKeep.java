package com.tranboot.client.druid.sql.ast;

import com.tranboot.client.druid.sql.visitor.SQLASTVisitor;

public class SQLKeep extends SQLObjectImpl {
    protected SQLKeep.DenseRank denseRank;
    protected SQLOrderBy orderBy;

    public SQLKeep() {
    }

    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.orderBy);
        }

        visitor.endVisit(this);
    }

    public SQLKeep.DenseRank getDenseRank() {
        return this.denseRank;
    }

    public void setDenseRank(SQLKeep.DenseRank denseRank) {
        this.denseRank = denseRank;
    }

    public SQLOrderBy getOrderBy() {
        return this.orderBy;
    }

    public void setOrderBy(SQLOrderBy orderBy) {
        if (orderBy != null) {
            orderBy.setParent(this);
        }

        this.orderBy = orderBy;
    }

    public static enum DenseRank {
        FIRST,
        LAST;

        private DenseRank() {
        }
    }
}

package com.tranboot.client.druid.sql.ast;

import com.tranboot.client.druid.sql.ast.statement.SQLSelectOrderByItem;
import com.tranboot.client.druid.sql.visitor.SQLASTVisitor;
import java.util.ArrayList;
import java.util.List;

public class SQLOrderBy extends SQLObjectImpl {
    protected final List<SQLSelectOrderByItem> items = new ArrayList();
    private boolean sibings;

    public SQLOrderBy() {
    }

    public SQLOrderBy(SQLExpr expr) {
        SQLSelectOrderByItem item = new SQLSelectOrderByItem(expr);
        this.addItem(item);
    }

    public void addItem(SQLSelectOrderByItem item) {
        if (item != null) {
            item.setParent(this);
        }

        this.items.add(item);
    }

    public List<SQLSelectOrderByItem> getItems() {
        return this.items;
    }

    public boolean isSibings() {
        return this.sibings;
    }

    public void setSibings(boolean sibings) {
        this.sibings = sibings;
    }

    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.items);
        }

        visitor.endVisit(this);
    }

    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = 31 * result + (this.items == null ? 0 : this.items.hashCode());
        result = 31 * result + (this.sibings ? 1231 : 1237);
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (this.getClass() != obj.getClass()) {
            return false;
        } else {
            SQLOrderBy other = (SQLOrderBy)obj;
            if (this.items == null) {
                if (other.items != null) {
                    return false;
                }
            } else if (!this.items.equals(other.items)) {
                return false;
            }

            return this.sibings == other.sibings;
        }
    }

    public void addItem(SQLExpr expr, SQLOrderingSpecification type) {
        SQLSelectOrderByItem item = this.createItem();
        item.setExpr(expr);
        item.setType(type);
        this.addItem(item);
    }

    protected SQLSelectOrderByItem createItem() {
        return new SQLSelectOrderByItem();
    }
}

package com.tranboot.client.druid.sql.ast.expr;

import com.tranboot.client.druid.sql.ast.SQLExprImpl;
import com.tranboot.client.druid.sql.visitor.SQLASTVisitor;

public class SQLVariantRefExpr extends SQLExprImpl {
    private String name;
    private boolean global = false;
    private int index = -1;

    public SQLVariantRefExpr(String name) {
        this.name = name;
    }

    public SQLVariantRefExpr(String name, boolean global) {
        this.name = name;
        this.global = global;
    }

    public SQLVariantRefExpr() {
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void output(StringBuffer buf) {
        buf.append(this.name);
    }

    protected void accept0(SQLASTVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }

    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = 31 * result + (this.name == null ? 0 : this.name.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (!(obj instanceof SQLVariantRefExpr)) {
            return false;
        } else {
            SQLVariantRefExpr other = (SQLVariantRefExpr)obj;
            if (this.name == null) {
                if (other.name != null) {
                    return false;
                }
            } else if (!this.name.equals(other.name)) {
                return false;
            }

            return true;
        }
    }

    public boolean isGlobal() {
        return this.global;
    }

    public void setGlobal(boolean global) {
        this.global = global;
    }
}


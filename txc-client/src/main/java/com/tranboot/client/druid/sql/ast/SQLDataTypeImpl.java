package com.tranboot.client.druid.sql.ast;

import com.tranboot.client.druid.sql.visitor.SQLASTVisitor;
import java.util.ArrayList;
import java.util.List;

public class SQLDataTypeImpl extends SQLObjectImpl implements SQLDataType {
    protected String name;
    protected final List<SQLExpr> arguments = new ArrayList();

    public SQLDataTypeImpl() {
    }

    public SQLDataTypeImpl(String name) {
        this.name = name;
    }

    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.arguments);
        }

        visitor.endVisit(this);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SQLExpr> getArguments() {
        return this.arguments;
    }

    public void addArgument(SQLExpr argument) {
        if (argument != null) {
            argument.setParent(this);
        }

        this.arguments.add(argument);
    }

    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = 31 * result + this.arguments.hashCode();
        result = 31 * result + (this.name == null ? 0 : this.name.hashCode());
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
            SQLDataTypeImpl other = (SQLDataTypeImpl)obj;
            if (!this.arguments.equals(other.arguments)) {
                return false;
            } else {
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
    }
}

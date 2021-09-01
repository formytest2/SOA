package com.tranboot.client.druid.sql.ast;

import com.tranboot.client.druid.sql.ast.statement.SQLTableElement;
import com.tranboot.client.druid.sql.visitor.SQLASTVisitor;
import java.util.ArrayList;
import java.util.List;

public class SQLDeclareItem extends SQLObjectImpl {
    protected SQLDeclareItem.Type type;
    protected SQLExpr name;
    protected SQLDataType dataType;
    protected SQLExpr value;
    protected List<SQLTableElement> tableElementList = new ArrayList();

    public SQLDeclareItem() {
    }

    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.name);
            this.acceptChild(visitor, this.dataType);
            this.acceptChild(visitor, this.value);
            this.acceptChild(visitor, this.tableElementList);
        }

        visitor.endVisit(this);
    }

    public SQLExpr getName() {
        return this.name;
    }

    public void setName(SQLExpr name) {
        this.name = name;
    }

    public SQLDataType getDataType() {
        return this.dataType;
    }

    public void setDataType(SQLDataType dataType) {
        this.dataType = dataType;
    }

    public SQLExpr getValue() {
        return this.value;
    }

    public void setValue(SQLExpr value) {
        this.value = value;
    }

    public List<SQLTableElement> getTableElementList() {
        return this.tableElementList;
    }

    public void setTableElementList(List<SQLTableElement> tableElementList) {
        this.tableElementList = tableElementList;
    }

    public SQLDeclareItem.Type getType() {
        return this.type;
    }

    public void setType(SQLDeclareItem.Type type) {
        this.type = type;
    }

    public static enum Type {
        TABLE,
        LOCAL,
        CURSOR;

        private Type() {
        }
    }
}

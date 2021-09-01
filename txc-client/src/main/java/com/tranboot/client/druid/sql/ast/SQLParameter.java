package com.tranboot.client.druid.sql.ast;

import com.tranboot.client.druid.sql.visitor.SQLASTVisitor;

public class SQLParameter extends SQLObjectImpl {
    private SQLExpr name;
    private SQLDataType dataType;
    private SQLExpr defaultValue;
    private SQLParameter.ParameterType paramType;

    public SQLParameter() {
    }

    public SQLExpr getDefaultValue() {
        return this.defaultValue;
    }

    public void setDefaultValue(SQLExpr deaultValue) {
        this.defaultValue = deaultValue;
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

    public SQLParameter.ParameterType getParamType() {
        return this.paramType;
    }

    public void setParamType(SQLParameter.ParameterType paramType) {
        this.paramType = paramType;
    }

    public void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.name);
            this.acceptChild(visitor, this.dataType);
            this.acceptChild(visitor, this.defaultValue);
        }

        visitor.endVisit(this);
    }

    public static enum ParameterType {
        DEFAULT,
        IN,
        OUT,
        INOUT;

        private ParameterType() {
        }
    }
}

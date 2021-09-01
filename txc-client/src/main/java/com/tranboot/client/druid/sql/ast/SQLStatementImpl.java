package com.tranboot.client.druid.sql.ast;

import com.tranboot.client.druid.sql.SQLUtils;
import com.tranboot.client.druid.sql.visitor.SQLASTVisitor;

public abstract class SQLStatementImpl extends SQLObjectImpl implements SQLStatement {
    private String dbType;

    public SQLStatementImpl() {
    }

    public SQLStatementImpl(String dbType) {
        this.dbType = dbType;
    }

    public String getDbType() {
        return this.dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    public String toString() {
        return SQLUtils.toSQLString(this, this.dbType);
    }

    protected void accept0(SQLASTVisitor visitor) {
        throw new UnsupportedOperationException(this.getClass().getName());
    }
}

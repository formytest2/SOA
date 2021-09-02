package com.tranboot.client.sqlast;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.expr.SQLVariantRefExpr;
import com.alibaba.druid.sql.ast.statement.SQLInsertStatement;
import com.alibaba.druid.sql.builder.impl.SQLBuilderImpl;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlInsertStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleInsertStatement;

import java.util.List;

public class SqlInsertStatementBuilderImpl extends SQLBuilderImpl implements SqlInsertStatementBuilder {
    private SQLInsertStatement stmt;
    private String dbType;

    public SqlInsertStatementBuilderImpl(String dbType) {
        this.dbType = dbType;
    }

    public SqlInsertStatementBuilderImpl(String sql, String dbType) {
        List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, dbType);
        if (stmtList.size() == 0) {
            throw new IllegalArgumentException("not support empty-statement :" + sql);
        } else if (stmtList.size() > 1) {
            throw new IllegalArgumentException("not support multi-statement :" + sql);
        } else {
            SQLInsertStatement stmt = (SQLInsertStatement)stmtList.get(0);
            this.stmt = stmt;
            this.dbType = dbType;
        }
    }

    public SqlInsertStatementBuilderImpl(SQLInsertStatement stmt, String dbType) {
        this.stmt = stmt;
        this.dbType = dbType;
    }

    public SqlInsertStatementBuilder insert(String table) {
        SQLInsertStatement statement = this.getSQLInsertStatement();
        statement.setTableName(new SQLIdentifierExpr(table));
        statement.setTableSource(new SQLIdentifierExpr(table));
        return this;
    }

    public SQLInsertStatement getSQLInsertStatement() {
        if (this.stmt == null) {
            this.stmt = this.createSQLInsertStatement();
        }

        return this.stmt;
    }

    public SQLInsertStatement createSQLInsertStatement() {
        if ("mysql".equals(this.dbType)) {
            return new MySqlInsertStatement();
        } else {
            return (SQLInsertStatement)("oracle".equals(this.dbType) ? new OracleInsertStatement() : new SQLInsertStatement());
        }
    }

    public SqlInsertStatementBuilder column(String... columns) {
        SQLInsertStatement statement = this.getSQLInsertStatement();
        if (statement.getColumns() != null && statement.getColumns().size() > 0) {
            throw new IllegalStateException("不能多次调用column");
        } else {
            String[] var3 = columns;
            int var4 = columns.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                String column = var3[var5];
                SQLIdentifierExpr field = new SQLIdentifierExpr(column);
                field.setParent(statement);
                statement.addColumn(field);
            }

            this.value(columns);
            return this;
        }
    }

    private SqlInsertStatementBuilder value(String... columns) {
        SQLInsertStatement statement = this.getSQLInsertStatement();
        SQLInsertStatement.ValuesClause valuesClause = new SQLInsertStatement.ValuesClause();
        valuesClause.setParent(statement);

        for(int i = 0; i < columns.length; ++i) {
            SQLVariantRefExpr ref = new SQLVariantRefExpr("?");
            ref.setIndex(i);
            valuesClause.addValue(ref);
        }

        statement.setValues(valuesClause);
        return this;
    }
}


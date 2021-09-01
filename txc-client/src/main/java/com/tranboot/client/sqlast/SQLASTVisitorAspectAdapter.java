package com.tranboot.client.sqlast;

import com.tranboot.client.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLInListExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLLiteralExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLVariantRefExpr;
import com.tranboot.client.druid.sql.ast.statement.SQLDeleteStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLExprTableSource;
import com.tranboot.client.druid.sql.ast.statement.SQLInsertStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLUpdateSetItem;
import com.tranboot.client.druid.sql.ast.statement.SQLUpdateStatement;

public class SQLASTVisitorAspectAdapter implements SQLASTVisitorAspect {
    public SQLASTVisitorAspectAdapter() {
    }

    public SQLExprTableSource tableAspect(SQLExprTableSource table) {
        return table;
    }

    public SQLUpdateSetItem updateItemAspect(SQLUpdateSetItem updateItem) {
        return updateItem;
    }

    public String insertColumnAspect(String columnName, SQLInsertStatement parent) {
        return columnName;
    }

    public String updateColumnAspect(String columnName) {
        return columnName;
    }

    public String whereColumnAspect(String columnName, SQLVariantRefExpr right) {
        return columnName;
    }

    public void insertEnterPoint(SQLInsertStatement insertStatement) {
    }

    public void updateEnterPoint(SQLUpdateStatement updateStatement) {
    }

    public String whereColumnAspect(String columnName, SQLLiteralExpr right) {
        return columnName;
    }

    public void whereEnterPoint(SQLBinaryOpExpr x) {
    }

    public void deleteEnterPoint(SQLDeleteStatement deleteStatement) {
    }

    public String whereColumnAspect(String columnName, SQLInListExpr in) {
        return columnName;
    }

    public void whereBinaryOpAspect(SQLVariantRefExpr right) {
    }

    public void whereExitPoint(SQLBinaryOpExpr x) {
    }

    public void variantRefAspect(SQLVariantRefExpr x) {
    }
}


package com.tranboot.client.sqlast;

import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.alibaba.druid.sql.ast.expr.SQLInListExpr;
import com.alibaba.druid.sql.ast.expr.SQLLiteralExpr;
import com.alibaba.druid.sql.ast.expr.SQLVariantRefExpr;
import com.alibaba.druid.sql.ast.statement.*;

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


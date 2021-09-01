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

public interface SQLASTVisitorAspect {
    SQLExprTableSource tableAspect(SQLExprTableSource var1);

    SQLUpdateSetItem updateItemAspect(SQLUpdateSetItem var1);

    String insertColumnAspect(String var1, SQLInsertStatement var2);

    String updateColumnAspect(String var1);

    String whereColumnAspect(String var1, SQLVariantRefExpr var2);

    String whereColumnAspect(String var1, SQLLiteralExpr var2);

    void whereBinaryOpAspect(SQLVariantRefExpr var1);

    String whereColumnAspect(String var1, SQLInListExpr var2);

    void whereEnterPoint(SQLBinaryOpExpr var1);

    void whereExitPoint(SQLBinaryOpExpr var1);

    void insertEnterPoint(SQLInsertStatement var1);

    void updateEnterPoint(SQLUpdateStatement var1);

    void deleteEnterPoint(SQLDeleteStatement var1);

    void variantRefAspect(SQLVariantRefExpr var1);
}


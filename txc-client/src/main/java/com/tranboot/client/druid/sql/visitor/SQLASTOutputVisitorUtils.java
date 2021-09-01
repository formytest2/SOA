package com.tranboot.client.druid.sql.visitor;

import com.tranboot.client.druid.sql.ast.expr.SQLCharExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLIntegerExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLNumberExpr;

public class SQLASTOutputVisitorUtils {
    public SQLASTOutputVisitorUtils() {
    }

    public static boolean visit(PrintableVisitor visitor, SQLIntegerExpr x) {
        visitor.print(x.getNumber().toString());
        return false;
    }

    public static boolean visit(PrintableVisitor visitor, SQLNumberExpr x) {
        visitor.print(x.getNumber().toString());
        return false;
    }

    public static boolean visit(PrintableVisitor visitor, SQLCharExpr x) {
        visitor.print('\'');
        String text = x.getText();
        text = text.replaceAll("'", "''");
        text = text.replaceAll("\\\\", "\\\\");
        visitor.print(text);
        visitor.print('\'');
        return false;
    }
}

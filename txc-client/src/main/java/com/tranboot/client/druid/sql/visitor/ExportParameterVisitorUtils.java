package com.tranboot.client.druid.sql.visitor;

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.expr.SQLBetweenExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLBooleanExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLCharExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLLiteralExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLNumericLiteralExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLVariantRefExpr;
import com.tranboot.client.druid.sql.dialect.mysql.visitor.MySqlExportParameterVisitor;
import com.tranboot.client.druid.sql.dialect.oracle.visitor.OracleExportParameterVisitor;
import java.util.ArrayList;
import java.util.List;

public final class ExportParameterVisitorUtils {
    private ExportParameterVisitorUtils() {
    }

    public static ExportParameterVisitor createExportParameterVisitor(Appendable out, String dbType) {
        if ("mysql".equals(dbType)) {
            return new MySqlExportParameterVisitor(out);
        } else if (!"oracle".equals(dbType) && !"AliOracle".equals(dbType)) {
            if ("mariadb".equals(dbType)) {
                return new MySqlExportParameterVisitor(out);
            } else {
                return (ExportParameterVisitor)("h2".equals(dbType) ? new MySqlExportParameterVisitor(out) : new ExportParameterizedOutputVisitor(out));
            }
        } else {
            return new OracleExportParameterVisitor(out);
        }
    }

    public static boolean exportParamterAndAccept(List<Object> parameters, List<SQLExpr> list) {
        int i = 0;

        for(int size = list.size(); i < size; ++i) {
            SQLExpr param = (SQLExpr)list.get(i);
            SQLExpr result = exportParameter(parameters, param);
            if (result != param) {
                list.set(i, result);
            }
        }

        return false;
    }

    public static SQLExpr exportParameter(List<Object> parameters, SQLExpr param) {
        Object value = null;
        boolean replace = false;
        if (param instanceof SQLCharExpr) {
            value = ((SQLCharExpr)param).getText();
            replace = true;
        }

        if (param instanceof SQLBooleanExpr) {
            value = ((SQLBooleanExpr)param).getValue();
            replace = true;
        }

        if (param instanceof SQLNumericLiteralExpr) {
            value = ((SQLNumericLiteralExpr)param).getNumber();
            replace = true;
        }

        if (!replace) {
            return param;
        } else {
            SQLObject parent = param.getParent();
            if (parent != null) {
                List<SQLObject> mergedList = (List)parent.getAttribute("parameterized.mergedList");
                if (mergedList != null) {
                    List<Object> mergedListParams = new ArrayList(mergedList.size() + 1);

                    for(int i = 0; i < mergedList.size(); ++i) {
                        SQLObject item = (SQLObject)mergedList.get(i);
                        if (item instanceof SQLBinaryOpExpr) {
                            SQLBinaryOpExpr binaryOpItem = (SQLBinaryOpExpr)item;
                            exportParameter(mergedListParams, (SQLExpr)binaryOpItem.getRight());
                        }
                    }

                    if (mergedListParams.size() > 0) {
                        mergedListParams.add(0, value);
                        value = mergedListParams;
                    }
                }
            }

            parameters.add(value);
            return new SQLVariantRefExpr("?");
        }
    }

    public static void exportParameter(List<Object> parameters, SQLBinaryOpExpr x) {
        if (!(x.getLeft() instanceof SQLLiteralExpr) || !(x.getRight() instanceof SQLLiteralExpr) || !x.getOperator().isRelational()) {
            SQLExpr rightResult = exportParameter(parameters, x.getLeft());
            if (rightResult != x.getLeft()) {
                x.setLeft(rightResult);
            }

            rightResult = exportParameter(parameters, x.getRight());
            if (rightResult != x.getRight()) {
                x.setRight(rightResult);
            }

        }
    }

    public static void exportParameter(List<Object> parameters, SQLBetweenExpr x) {
        SQLExpr result = exportParameter(parameters, x.getBeginExpr());
        if (result != x.getBeginExpr()) {
            x.setBeginExpr(result);
        }

        result = exportParameter(parameters, x.getEndExpr());
        if (result != x.getBeginExpr()) {
            x.setEndExpr(result);
        }

    }
}

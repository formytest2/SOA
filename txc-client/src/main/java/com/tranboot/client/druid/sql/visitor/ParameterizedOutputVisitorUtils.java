package com.tranboot.client.druid.sql.visitor;

import com.tranboot.client.druid.sql.ast.SQLDataType;
import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.SQLStatement;
import com.tranboot.client.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLBinaryOperator;
import com.tranboot.client.druid.sql.ast.expr.SQLIntegerExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLLiteralExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLVariantRefExpr;
import com.tranboot.client.druid.sql.ast.statement.SQLColumnDefinition;
import com.tranboot.client.druid.sql.ast.statement.SQLSelectOrderByItem;
import com.tranboot.client.druid.sql.dialect.mysql.visitor.MySqlOutputVisitor;
import com.tranboot.client.druid.sql.dialect.oracle.visitor.OracleParameterizedOutputVisitor;
import com.tranboot.client.druid.sql.parser.SQLParserUtils;
import com.tranboot.client.druid.sql.parser.SQLStatementParser;
import java.util.ArrayList;
import java.util.List;

public class ParameterizedOutputVisitorUtils {
    public static final String ATTR_PARAMS_SKIP = "druid.parameterized.skip";
    public static final String ATTR_MERGED = "parameterized.mergedList";

    public ParameterizedOutputVisitorUtils() {
    }

    public static String parameterize(String sql, String dbType) {
        SQLStatementParser parser = SQLParserUtils.createSQLStatementParser(sql, dbType);
        List<SQLStatement> statementList = parser.parseStatementList();
        if (statementList.size() == 0) {
            return sql;
        } else {
            StringBuilder out = new StringBuilder(sql.length());
            ParameterizedVisitor visitor = createParameterizedOutputVisitor(out, dbType);

            for(int i = 0; i < statementList.size(); ++i) {
                if (i > 0) {
                    out.append(";\n");
                }

                SQLStatement stmt = (SQLStatement)statementList.get(i);
                if (stmt.hasBeforeComment()) {
                    stmt.getBeforeCommentsDirect().clear();
                }

                stmt.accept(visitor);
            }

            if (visitor.getReplaceCount() == 0 && parser.getLexer().getCommentCount() == 0) {
                return sql;
            } else {
                return out.toString();
            }
        }
    }

    public static String parameterize(List<SQLStatement> statementList, String dbType) {
        StringBuilder out = new StringBuilder();
        ParameterizedVisitor visitor = createParameterizedOutputVisitor(out, dbType);

        for(int i = 0; i < statementList.size(); ++i) {
            if (i > 0) {
                out.append(";\n");
            }

            SQLStatement stmt = (SQLStatement)statementList.get(i);
            if (stmt.hasBeforeComment()) {
                stmt.getBeforeCommentsDirect().clear();
            }

            stmt.accept(visitor);
        }

        return out.toString();
    }

    public static ParameterizedVisitor createParameterizedOutputVisitor(Appendable out, String dbType) {
        if (!"oracle".equals(dbType) && !"AliOracle".equals(dbType)) {
            return (ParameterizedVisitor)(!"mysql".equals(dbType) && !"mariadb".equals(dbType) && !"h2".equals(dbType) ? new SQLASTOutputVisitor(out, true) : new MySqlOutputVisitor(out, true));
        } else {
            return new OracleParameterizedOutputVisitor(out);
        }
    }

    public static boolean checkParameterize(SQLObject x) {
        if (Boolean.TRUE.equals(x.getAttribute("druid.parameterized.skip"))) {
            return false;
        } else {
            SQLObject parent = x.getParent();
            return !(parent instanceof SQLDataType) && !(parent instanceof SQLColumnDefinition) && !(parent instanceof SQLSelectOrderByItem);
        }
    }

    public static boolean visit(ParameterizedVisitor v, SQLVariantRefExpr x) {
        v.print('?');
        v.incrementReplaceCunt();
        if (v instanceof ExportParameterVisitor) {
            ExportParameterVisitorUtils.exportParameter(((ExportParameterVisitor)v).getParameters(), x);
        }

        return false;
    }

    static void putMergedArribute(SQLObject object, SQLObject item) {
        List<SQLObject> mergedList = (List)object.getAttribute("parameterized.mergedList");
        if (mergedList == null) {
            mergedList = new ArrayList();
            object.putAttribute("parameterized.mergedList", mergedList);
        }

        ((List)mergedList).add(item);
    }

    public static SQLBinaryOpExpr merge(ParameterizedVisitor v, SQLBinaryOpExpr x) {
        SQLExpr left = x.getLeft();
        SQLExpr right = x.getRight();
        SQLObject parent = x.getParent();
        if (left instanceof SQLLiteralExpr && right instanceof SQLLiteralExpr) {
            if (x.getOperator() == SQLBinaryOperator.Equality || x.getOperator() == SQLBinaryOperator.NotEqual) {
                if (left instanceof SQLIntegerExpr && right instanceof SQLIntegerExpr) {
                    if (((SQLIntegerExpr)left).getNumber().intValue() < 100) {
                        left.putAttribute("druid.parameterized.skip", true);
                    }

                    if (((SQLIntegerExpr)right).getNumber().intValue() < 100) {
                        right.putAttribute("druid.parameterized.skip", true);
                    }
                } else {
                    left.putAttribute("druid.parameterized.skip", true);
                    right.putAttribute("druid.parameterized.skip", true);
                }
            }

            return x;
        } else {
            SQLBinaryOpExpr leftBinary;
            while(x.getRight() instanceof SQLBinaryOpExpr) {
                if (x.getLeft() instanceof SQLBinaryOpExpr) {
                    leftBinary = (SQLBinaryOpExpr)x.getLeft();
                    if (leftBinary.getRight().equals(x.getRight())) {
                        x = leftBinary;
                        v.incrementReplaceCunt();
                        continue;
                    }
                }

                leftBinary = merge(v, (SQLBinaryOpExpr)x.getRight());
                if (leftBinary != x.getRight()) {
                    x = new SQLBinaryOpExpr(x.getLeft(), x.getOperator(), leftBinary);
                    v.incrementReplaceCunt();
                }

                x.setParent(parent);
                break;
            }

            SQLBinaryOpExpr rightBinary;
            if (x.getLeft() instanceof SQLBinaryOpExpr) {
                leftBinary = merge(v, (SQLBinaryOpExpr)x.getLeft());
                if (leftBinary != x.getLeft()) {
                    rightBinary = new SQLBinaryOpExpr(leftBinary, x.getOperator(), x.getRight());
                    rightBinary.setParent(parent);
                    x = rightBinary;
                    v.incrementReplaceCunt();
                }
            }

            if (x.getOperator() == SQLBinaryOperator.BooleanOr && x.getLeft() instanceof SQLBinaryOpExpr && x.getRight() instanceof SQLBinaryOpExpr) {
                leftBinary = (SQLBinaryOpExpr)x.getLeft();
                rightBinary = (SQLBinaryOpExpr)x.getRight();
                if (mergeEqual(leftBinary, rightBinary)) {
                    v.incrementReplaceCunt();
                    leftBinary.setParent(x.getParent());
                    putMergedArribute(leftBinary, rightBinary);
                    return leftBinary;
                }

                if (isLiteralExpr(leftBinary.getLeft()) && leftBinary.getOperator() == SQLBinaryOperator.BooleanOr && mergeEqual(leftBinary.getRight(), x.getRight())) {
                    v.incrementReplaceCunt();
                    putMergedArribute(leftBinary, rightBinary);
                    return leftBinary;
                }
            }

            return x;
        }
    }

    private static boolean mergeEqual(SQLExpr a, SQLExpr b) {
        if (!(a instanceof SQLBinaryOpExpr)) {
            return false;
        } else if (!(b instanceof SQLBinaryOpExpr)) {
            return false;
        } else {
            SQLBinaryOpExpr binaryA = (SQLBinaryOpExpr)a;
            SQLBinaryOpExpr binaryB = (SQLBinaryOpExpr)b;
            if (binaryA.getOperator() != SQLBinaryOperator.Equality) {
                return false;
            } else if (binaryB.getOperator() != SQLBinaryOperator.Equality) {
                return false;
            } else if (!(binaryA.getRight() instanceof SQLLiteralExpr) && !(binaryA.getRight() instanceof SQLVariantRefExpr)) {
                return false;
            } else {
                return !(binaryB.getRight() instanceof SQLLiteralExpr) && !(binaryB.getRight() instanceof SQLVariantRefExpr) ? false : binaryA.getLeft().toString().equals(binaryB.getLeft().toString());
            }
        }
    }

    private static boolean isLiteralExpr(SQLExpr expr) {
        if (expr instanceof SQLLiteralExpr) {
            return true;
        } else if (!(expr instanceof SQLBinaryOpExpr)) {
            return false;
        } else {
            SQLBinaryOpExpr binary = (SQLBinaryOpExpr)expr;
            return isLiteralExpr(binary.getLeft()) && isLiteralExpr(binary.getRight());
        }
    }
}


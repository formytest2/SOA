package com.tranboot.client.druid.sql;

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLLimit;
import com.tranboot.client.druid.sql.ast.SQLOrderBy;
import com.tranboot.client.druid.sql.ast.SQLStatement;
import com.tranboot.client.druid.sql.ast.expr.SQLAggregateExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLAggregateOption;
import com.tranboot.client.druid.sql.ast.expr.SQLAllColumnExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLBinaryOperator;
import com.tranboot.client.druid.sql.ast.expr.SQLIdentifierExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLNumberExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLNumericLiteralExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLPropertyExpr;
import com.tranboot.client.druid.sql.ast.statement.SQLSelect;
import com.tranboot.client.druid.sql.ast.statement.SQLSelectItem;
import com.tranboot.client.druid.sql.ast.statement.SQLSelectQuery;
import com.tranboot.client.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.tranboot.client.druid.sql.ast.statement.SQLSelectStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLSubqueryTableSource;
import com.tranboot.client.druid.sql.ast.statement.SQLUnionQuery;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleSelectQueryBlock;
import java.util.List;

public class PagerUtils {
    public PagerUtils() {
    }

    public static String count(String sql, String dbType) {
        List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, dbType);
        if (stmtList.size() != 1) {
            throw new IllegalArgumentException("sql not support count : " + sql);
        } else {
            SQLStatement stmt = (SQLStatement)stmtList.get(0);
            if (!(stmt instanceof SQLSelectStatement)) {
                throw new IllegalArgumentException("sql not support count : " + sql);
            } else {
                SQLSelectStatement selectStmt = (SQLSelectStatement)stmt;
                return count(selectStmt.getSelect(), dbType);
            }
        }
    }

    public static String limit(String sql, String dbType, int offset, int count) {
        List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, dbType);
        if (stmtList.size() != 1) {
            throw new IllegalArgumentException("sql not support count : " + sql);
        } else {
            SQLStatement stmt = (SQLStatement)stmtList.get(0);
            if (!(stmt instanceof SQLSelectStatement)) {
                throw new IllegalArgumentException("sql not support count : " + sql);
            } else {
                SQLSelectStatement selectStmt = (SQLSelectStatement)stmt;
                return limit(selectStmt.getSelect(), dbType, offset, count);
            }
        }
    }

    public static String limit(SQLSelect select, String dbType, int offset, int count) {
        SQLSelectQuery query = select.getQuery();
        if ("oracle".equals(dbType)) {
            return limitOracle(select, dbType, offset, count);
        } else if (query instanceof SQLSelectQueryBlock) {
            return limitQueryBlock(select, dbType, offset, count);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    private static String limitQueryBlock(SQLSelect select, String dbType, int offset, int count) {
        SQLSelectQueryBlock queryBlock = (SQLSelectQueryBlock)select.getQuery();
        if (!"mysql".equals(dbType) && !"mariadb".equals(dbType) && !"h2".equals(dbType)) {
            throw new UnsupportedOperationException();
        } else {
            return limitMySqlQueryBlock((MySqlSelectQueryBlock)queryBlock, dbType, offset, count);
        }
    }

    private static String limitOracle(SQLSelect select, String dbType, int offset, int count) {
        SQLSelectQuery query = select.getQuery();
        OracleSelectQueryBlock queryBlock;
        if (query instanceof SQLSelectQueryBlock) {
            queryBlock = (OracleSelectQueryBlock)query;
            if (queryBlock.getGroupBy() == null && select.getOrderBy() == null && offset <= 0) {
                SQLExpr condition = new SQLBinaryOpExpr(new SQLIdentifierExpr("ROWNUM"), SQLBinaryOperator.LessThanOrEqual, new SQLNumberExpr(count), "oracle");
                if (queryBlock.getWhere() == null) {
                    queryBlock.setWhere(condition);
                } else {
                    queryBlock.setWhere(new SQLBinaryOpExpr(queryBlock.getWhere(), SQLBinaryOperator.BooleanAnd, condition, "oracle"));
                }

                return SQLUtils.toSQLString(select, dbType);
            }
        }

        queryBlock = new OracleSelectQueryBlock();
        queryBlock.getSelectList().add(new SQLSelectItem(new SQLPropertyExpr(new SQLIdentifierExpr("XX"), "*")));
        queryBlock.getSelectList().add(new SQLSelectItem(new SQLIdentifierExpr("ROWNUM"), "RN"));
        queryBlock.setFrom(new SQLSubqueryTableSource(select, "XX"));
        queryBlock.setWhere(new SQLBinaryOpExpr(new SQLIdentifierExpr("ROWNUM"), SQLBinaryOperator.LessThanOrEqual, new SQLNumberExpr(count + offset), "oracle"));
        if (offset <= 0) {
            return SQLUtils.toSQLString(queryBlock, dbType);
        } else {
            OracleSelectQueryBlock offsetQueryBlock = new OracleSelectQueryBlock();
            offsetQueryBlock.getSelectList().add(new SQLSelectItem(new SQLAllColumnExpr()));
            offsetQueryBlock.setFrom(new SQLSubqueryTableSource(new SQLSelect(queryBlock), "XXX"));
            offsetQueryBlock.setWhere(new SQLBinaryOpExpr(new SQLIdentifierExpr("RN"), SQLBinaryOperator.GreaterThan, new SQLNumberExpr(offset), "oracle"));
            return SQLUtils.toSQLString(offsetQueryBlock, dbType);
        }
    }

    private static String limitMySqlQueryBlock(MySqlSelectQueryBlock queryBlock, String dbType, int offset, int count) {
        if (queryBlock.getLimit() != null) {
            throw new IllegalArgumentException("limit already exists.");
        } else {
            SQLLimit limit = new SQLLimit();
            if (offset > 0) {
                limit.setOffset(new SQLNumberExpr(offset));
            }

            limit.setRowCount(new SQLNumberExpr(count));
            queryBlock.setLimit(limit);
            return SQLUtils.toSQLString(queryBlock, dbType);
        }
    }

    private static String count(SQLSelect select, String dbType) {
        if (select.getOrderBy() != null) {
            select.setOrderBy((SQLOrderBy)null);
        }

        SQLSelectQuery query = select.getQuery();
        clearOrderBy(query);
        if (query instanceof SQLSelectQueryBlock) {
            SQLSelectItem countItem = createCountItem(dbType);
            SQLSelectQueryBlock queryBlock = (SQLSelectQueryBlock)query;
            if (queryBlock.getGroupBy() != null && queryBlock.getGroupBy().getItems().size() > 0) {
                return createCountUseSubQuery(select, dbType);
            } else {
                int option = queryBlock.getDistionOption();
                if (option == 2 && queryBlock.getSelectList().size() == 1) {
                    SQLSelectItem firstItem = (SQLSelectItem)queryBlock.getSelectList().get(0);
                    SQLAggregateExpr exp = new SQLAggregateExpr("COUNT", SQLAggregateOption.DISTINCT);
                    exp.addArgument(firstItem.getExpr());
                    firstItem.setExpr(exp);
                    queryBlock.setDistionOption(0);
                } else {
                    queryBlock.getSelectList().clear();
                    queryBlock.getSelectList().add(countItem);
                }

                return SQLUtils.toSQLString(select, dbType);
            }
        } else if (query instanceof SQLUnionQuery) {
            return createCountUseSubQuery(select, dbType);
        } else {
            throw new IllegalStateException();
        }
    }

    private static String createCountUseSubQuery(SQLSelect select, String dbType) {
        SQLSelectQueryBlock countSelectQuery = createQueryBlock(dbType);
        SQLSelectItem countItem = createCountItem(dbType);
        countSelectQuery.getSelectList().add(countItem);
        SQLSubqueryTableSource fromSubquery = new SQLSubqueryTableSource(select);
        fromSubquery.setAlias("ALIAS_COUNT");
        countSelectQuery.setFrom(fromSubquery);
        SQLSelect countSelect = new SQLSelect(countSelectQuery);
        SQLSelectStatement countStmt = new SQLSelectStatement(countSelect);
        return SQLUtils.toSQLString(countStmt, dbType);
    }

    private static SQLSelectQueryBlock createQueryBlock(String dbType) {
        if ("mysql".equals(dbType)) {
            return new MySqlSelectQueryBlock();
        } else if ("mariadb".equals(dbType)) {
            return new MySqlSelectQueryBlock();
        } else if ("h2".equals(dbType)) {
            return new MySqlSelectQueryBlock();
        } else {
            return (SQLSelectQueryBlock)("oracle".equals(dbType) ? new OracleSelectQueryBlock() : new SQLSelectQueryBlock());
        }
    }

    private static SQLSelectItem createCountItem(String dbType) {
        SQLAggregateExpr countExpr = new SQLAggregateExpr("COUNT");
        countExpr.addArgument(new SQLAllColumnExpr());
        SQLSelectItem countItem = new SQLSelectItem(countExpr);
        return countItem;
    }

    private static void clearOrderBy(SQLSelectQuery query) {
        if (query instanceof SQLSelectQueryBlock) {
            SQLSelectQueryBlock queryBlock = (SQLSelectQueryBlock)query;
            if (queryBlock instanceof MySqlSelectQueryBlock) {
                MySqlSelectQueryBlock mysqlQueryBlock = (MySqlSelectQueryBlock)queryBlock;
                if (mysqlQueryBlock.getOrderBy() != null) {
                    mysqlQueryBlock.setOrderBy((SQLOrderBy)null);
                }
            }

        } else {
            if (query instanceof SQLUnionQuery) {
                SQLUnionQuery union = (SQLUnionQuery)query;
                if (union.getOrderBy() != null) {
                    union.setOrderBy((SQLOrderBy)null);
                }

                clearOrderBy(union.getLeft());
                clearOrderBy(union.getRight());
            }

        }
    }

    public static int getLimit(String sql, String dbType) {
        List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, dbType);
        if (stmtList.size() != 1) {
            return -1;
        } else {
            SQLStatement stmt = (SQLStatement)stmtList.get(0);
            if (stmt instanceof SQLSelectStatement) {
                SQLSelectStatement selectStmt = (SQLSelectStatement)stmt;
                SQLSelectQuery query = selectStmt.getSelect().getQuery();
                if (query instanceof SQLSelectQueryBlock) {
                    if (query instanceof MySqlSelectQueryBlock) {
                        SQLLimit limit = ((MySqlSelectQueryBlock)query).getLimit();
                        if (limit == null) {
                            return -1;
                        }

                        SQLExpr rowCountExpr = limit.getRowCount();
                        if (rowCountExpr instanceof SQLNumericLiteralExpr) {
                            int rowCount = ((SQLNumericLiteralExpr)rowCountExpr).getNumber().intValue();
                            return rowCount;
                        }

                        return 2147483647;
                    }

                    return -1;
                }
            }

            return -1;
        }
    }
}

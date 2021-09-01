package com.tranboot.client.druid.wall.spi;

import com.tranboot.client.druid.sql.SQLUtils;
import com.tranboot.client.druid.sql.ast.SQLCommentHint;
import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLLimit;
import com.tranboot.client.druid.sql.ast.SQLName;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.SQLOrderBy;
import com.tranboot.client.druid.sql.ast.SQLStatement;
import com.tranboot.client.druid.sql.ast.expr.SQLAggregateExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLAllColumnExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLBetweenExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLBinaryOperator;
import com.tranboot.client.druid.sql.ast.expr.SQLBooleanExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLCaseExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLCharExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLExistsExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLIdentifierExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLInListExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLInSubQueryExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLIntegerExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLLiteralExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLMethodInvokeExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLNCharExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLNotExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLNumberExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLNumericLiteralExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLPropertyExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLQueryExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLUnaryExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLValuableExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLVariantRefExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLCaseExpr.Item;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLBlockStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLCallStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLCreateIndexStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLCreateSequenceStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLCreateTableStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLCreateTriggerStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLCreateViewStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLDeleteStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLDropIndexStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLDropProcedureStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLDropSequenceStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLDropTableStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLDropTriggerStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLDropViewStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLExprTableSource;
import com.tranboot.client.druid.sql.ast.statement.SQLInsertInto;
import com.tranboot.client.druid.sql.ast.statement.SQLInsertStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLJoinTableSource;
import com.tranboot.client.druid.sql.ast.statement.SQLMergeStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLRollbackStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLSelect;
import com.tranboot.client.druid.sql.ast.statement.SQLSelectGroupByClause;
import com.tranboot.client.druid.sql.ast.statement.SQLSelectItem;
import com.tranboot.client.druid.sql.ast.statement.SQLSelectQuery;
import com.tranboot.client.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.tranboot.client.druid.sql.ast.statement.SQLSelectStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLSetStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLShowTablesStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLStartTransactionStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLSubqueryTableSource;
import com.tranboot.client.druid.sql.ast.statement.SQLTableSource;
import com.tranboot.client.druid.sql.ast.statement.SQLTruncateStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLUnionOperator;
import com.tranboot.client.druid.sql.ast.statement.SQLUnionQuery;
import com.tranboot.client.druid.sql.ast.statement.SQLUpdateSetItem;
import com.tranboot.client.druid.sql.ast.statement.SQLUpdateStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLUseStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLInsertStatement.ValuesClause;
import com.tranboot.client.druid.sql.dialect.mysql.ast.expr.MySqlOrderingExpr;
import com.tranboot.client.druid.sql.dialect.mysql.ast.expr.MySqlOutFileExpr;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlCommitStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlDeleteStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlDescribeStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlHintStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlInsertStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlLockTableStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlRenameTableStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlReplaceStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlSetCharSetStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlSetNamesStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlShowGrantsStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlShowStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlUpdateStatement;
import com.tranboot.client.druid.sql.dialect.mysql.parser.MySqlStatementParser;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleMultiInsertStatement;
import com.tranboot.client.druid.sql.parser.SQLStatementParser;
import com.tranboot.client.druid.sql.visitor.ExportParameterVisitor;
import com.tranboot.client.druid.sql.visitor.SQLEvalVisitor;
import com.tranboot.client.druid.sql.visitor.SQLEvalVisitorUtils;
import com.tranboot.client.druid.sql.visitor.functions.Nil;
import com.tranboot.client.druid.util.JdbcUtils;
import com.tranboot.client.druid.util.ServletPathMatcher;
import com.tranboot.client.druid.util.StringUtils;
import com.tranboot.client.druid.wall.WallConfig;
import com.tranboot.client.druid.wall.WallContext;
import com.tranboot.client.druid.wall.WallProvider;
import com.tranboot.client.druid.wall.WallSqlTableStat;
import com.tranboot.client.druid.wall.WallVisitor;
import com.tranboot.client.druid.wall.WallConfig.TenantCallBack;
import com.tranboot.client.druid.wall.WallConfig.TenantCallBack.StatementType;
import com.tranboot.client.druid.wall.violation.IllegalSQLObjectViolation;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WallVisitorUtils {
    private static final Logger LOG = LoggerFactory.getLogger(WallVisitorUtils.class);
    public static final String HAS_TRUE_LIKE = "hasTrueLike";
    public static final String[] whiteHints = new String[]{"LOCAL", "TEMPORARY", "SQL_NO_CACHE", "SQL_CACHE", "HIGH_PRIORITY", "LOW_PRIORITY", "STRAIGHT_JOIN", "SQL_BUFFER_RESULT", "SQL_BIG_RESULT", "SQL_SMALL_RESULT", "DELAYED"};
    private static ThreadLocal<WallVisitorUtils.WallConditionContext> wallConditionContextLocal = new ThreadLocal();
    private static ThreadLocal<WallVisitorUtils.WallTopStatementContext> wallTopStatementContextLocal = new ThreadLocal();

    public WallVisitorUtils() {
    }

    public static void check(WallVisitor visitor, SQLInListExpr x) {
    }

    public static boolean check(WallVisitor visitor, SQLBinaryOpExpr x) {
        List groupList;
        if (x.getOperator() != SQLBinaryOperator.BooleanOr && x.getOperator() != SQLBinaryOperator.BooleanAnd) {
            if (x.getOperator() == SQLBinaryOperator.Add || x.getOperator() == SQLBinaryOperator.Concat) {
                groupList = SQLUtils.split(x);
                if (groupList.size() >= 4) {
                    int chrCount = 0;

                    for(int i = 0; i < groupList.size(); ++i) {
                        SQLExpr item = (SQLExpr)groupList.get(i);
                        if (item instanceof SQLMethodInvokeExpr) {
                            SQLMethodInvokeExpr methodExpr = (SQLMethodInvokeExpr)item;
                            String methodName = methodExpr.getMethodName().toLowerCase();
                            if (("chr".equals(methodName) || "char".equals(methodName)) && methodExpr.getParameters().get(0) instanceof SQLLiteralExpr) {
                                ++chrCount;
                            }
                        } else if (item instanceof SQLCharExpr && ((SQLCharExpr)item).getText().length() > 5) {
                            chrCount = 0;
                            continue;
                        }

                        if (chrCount >= 4) {
                            addViolation(visitor, 2112, "evil concat", x);
                            break;
                        }
                    }
                }
            }

            return true;
        } else {
            groupList = SQLUtils.split(x);
            Iterator var3 = groupList.iterator();

            while(var3.hasNext()) {
                SQLExpr item = (SQLExpr)var3.next();
                item.accept(visitor);
            }

            return false;
        }
    }

    public static void check(WallVisitor visitor, SQLCreateTableStatement x) {
        String tableName = x.getName().getSimpleName();
        WallContext context = WallContext.current();
        if (context != null) {
            WallSqlTableStat tableStat = context.getTableStat(tableName);
            if (tableStat != null) {
                tableStat.incrementCreateCount();
            }
        }

    }

    public static void check(WallVisitor visitor, SQLAlterTableStatement x) {
        String tableName = x.getName().getSimpleName();
        WallContext context = WallContext.current();
        if (context != null) {
            WallSqlTableStat tableStat = context.getTableStat(tableName);
            if (tableStat != null) {
                tableStat.incrementAlterCount();
            }
        }

    }

    public static void check(WallVisitor visitor, SQLDropTableStatement x) {
        Iterator var2 = x.getTableSources().iterator();

        while(var2.hasNext()) {
            SQLTableSource item = (SQLTableSource)var2.next();
            if (item instanceof SQLExprTableSource) {
                SQLExpr expr = ((SQLExprTableSource)item).getExpr();
                String tableName = ((SQLName)expr).getSimpleName();
                WallContext context = WallContext.current();
                if (context != null) {
                    WallSqlTableStat tableStat = context.getTableStat(tableName);
                    if (tableStat != null) {
                        tableStat.incrementDropCount();
                    }
                }
            }
        }

    }

    public static void check(WallVisitor visitor, SQLSelectItem x) {
        SQLExpr expr = x.getExpr();
        if (expr instanceof SQLVariantRefExpr && !isTopSelectItem(expr) && "@".equals(((SQLVariantRefExpr)expr).getName())) {
            addViolation(visitor, 2111, "@ not allow", x);
        }

        if (!visitor.getConfig().isSelectAllColumnAllow()) {
            if (expr instanceof SQLAllColumnExpr && x.getParent() instanceof SQLSelectQueryBlock) {
                SQLSelectQueryBlock queryBlock = (SQLSelectQueryBlock)x.getParent();
                SQLTableSource from = queryBlock.getFrom();
                if (from instanceof SQLExprTableSource) {
                    addViolation(visitor, 1002, "'SELECT *' not allow", x);
                }
            }

        }
    }

    public static void check(WallVisitor visitor, SQLPropertyExpr x) {
        checkSchema(visitor, x.getOwner());
    }

    public static void checkInsert(WallVisitor visitor, SQLInsertInto x) {
        checkReadOnly(visitor, x.getTableSource());
        if (!visitor.getConfig().isInsertAllow()) {
            addViolation(visitor, 1004, "insert not allow", x);
        }

        checkInsertForMultiTenant(visitor, x);
    }

    public static void checkSelelct(WallVisitor visitor, SQLSelectQueryBlock x) {
        Iterator var2 = x.getSelectList().iterator();

        while(var2.hasNext()) {
            SQLSelectItem item = (SQLSelectItem)var2.next();
            item.setParent(x);
        }

        if (x.getInto() != null) {
            checkReadOnly(visitor, x.getInto());
        }

        if (!visitor.getConfig().isSelectIntoAllow() && x.getInto() != null) {
            addViolation(visitor, 1003, "select into not allow", x);
        } else {
            if (x.getFrom() != null) {
                x.getFrom().setParent(x);
            }

            SQLExpr where = x.getWhere();
            if (where != null) {
                where.setParent(x);
                checkCondition(visitor, x.getWhere());
                Object whereValue = getConditionValue(visitor, where, visitor.getConfig().isSelectWhereAlwayTrueCheck());
                if (Boolean.TRUE == whereValue && visitor.getConfig().isSelectWhereAlwayTrueCheck() && visitor.isSqlEndOfComment() && !isSimpleConstExpr(where)) {
                    addViolation(visitor, 2100, "select alway true condition not allow", x);
                }
            }

            checkSelectForMultiTenant(visitor, x);
        }
    }

    public static void checkHaving(WallVisitor visitor, SQLExpr x) {
        if (x != null) {
            if (Boolean.TRUE == getConditionValue(visitor, x, visitor.getConfig().isSelectHavingAlwayTrueCheck()) && visitor.getConfig().isSelectHavingAlwayTrueCheck() && visitor.isSqlEndOfComment() && !isSimpleConstExpr(x)) {
                addViolation(visitor, 2100, "having alway true condition not allow", x);
            }

        }
    }

    public static void checkDelete(WallVisitor visitor, SQLDeleteStatement x) {
        checkReadOnly(visitor, x.getTableSource());
        WallConfig config = visitor.getConfig();
        if (!config.isDeleteAllow()) {
            addViolation(visitor, 1004, "delete not allow", x);
        } else {
            boolean hasUsing = false;
            if (x instanceof MySqlDeleteStatement) {
                hasUsing = ((MySqlDeleteStatement)x).getUsing() != null;
            }

            boolean isJoinTableSource = x.getTableSource() instanceof SQLJoinTableSource;
            if (x.getWhere() == null && !hasUsing && !isJoinTableSource) {
                WallContext context = WallContext.current();
                if (context != null) {
                    context.incrementDeleteNoneConditionWarnings();
                }

                if (config.isDeleteWhereNoneCheck()) {
                    addViolation(visitor, 2104, "delete none condition not allow", x);
                    return;
                }
            }

            SQLExpr where = x.getWhere();
            if (where != null) {
                checkCondition(visitor, where);
                if (Boolean.TRUE == getConditionValue(visitor, where, config.isDeleteWhereAlwayTrueCheck()) && config.isDeleteWhereAlwayTrueCheck() && visitor.isSqlEndOfComment() && !isSimpleConstExpr(where)) {
                    addViolation(visitor, 2100, "delete alway true condition not allow", x);
                }
            }

        }
    }

    private static boolean isSimpleConstExpr(SQLExpr sqlExpr) {
        List<SQLExpr> parts = getParts(sqlExpr);
        if (parts.isEmpty()) {
            return false;
        } else {
            Iterator var2 = parts.iterator();

            boolean isSimpleConstExpr;
            do {
                if (!var2.hasNext()) {
                    return true;
                }

                SQLExpr part = (SQLExpr)var2.next();
                if (isFirst(part)) {
                    Object evalValue = part.getAttribute("eval.value");
                    if (evalValue == null) {
                        if (part instanceof SQLBooleanExpr) {
                            evalValue = ((SQLBooleanExpr)part).getValue();
                        } else if (part instanceof SQLNumericLiteralExpr) {
                            evalValue = ((SQLNumericLiteralExpr)part).getNumber();
                        } else if (part instanceof SQLCharExpr) {
                            evalValue = ((SQLCharExpr)part).getText();
                        } else if (part instanceof SQLNCharExpr) {
                            evalValue = ((SQLNCharExpr)part).getText();
                        }
                    }

                    Boolean result = SQLEvalVisitorUtils.castToBoolean(evalValue);
                    if (result != null && result) {
                        return true;
                    }
                }

                isSimpleConstExpr = false;
                if (part != sqlExpr && !(part instanceof SQLLiteralExpr)) {
                    if (part instanceof SQLBinaryOpExpr) {
                        SQLBinaryOpExpr binaryOpExpr = (SQLBinaryOpExpr)part;
                        if ((binaryOpExpr.getOperator() == SQLBinaryOperator.Equality || binaryOpExpr.getOperator() == SQLBinaryOperator.NotEqual || binaryOpExpr.getOperator() == SQLBinaryOperator.GreaterThan) && binaryOpExpr.getLeft() instanceof SQLIntegerExpr && binaryOpExpr.getRight() instanceof SQLIntegerExpr) {
                            isSimpleConstExpr = true;
                        }
                    }
                } else {
                    isSimpleConstExpr = true;
                }
            } while(isSimpleConstExpr);

            return false;
        }
    }

    private static void checkCondition(WallVisitor visitor, SQLExpr x) {
        if (x != null) {
            if (visitor.getConfig().isMustParameterized()) {
                ExportParameterVisitor exportParameterVisitor = visitor.getProvider().createExportParameterVisitor();
                x.accept(exportParameterVisitor);
                if (exportParameterVisitor.getParameters().size() > 0) {
                    addViolation(visitor, 2200, "sql must parameterized", x);
                }
            }

        }
    }

    private static void checkJoinSelectForMultiTenant(WallVisitor visitor, SQLJoinTableSource join, SQLSelectQueryBlock x) {
        TenantCallBack tenantCallBack = visitor.getConfig().getTenantCallBack();
        String tenantTablePattern = visitor.getConfig().getTenantTablePattern();
        if (tenantCallBack != null || tenantTablePattern != null && tenantTablePattern.length() != 0) {
            SQLTableSource right = join.getRight();
            if (right instanceof SQLExprTableSource) {
                SQLExpr tableExpr = ((SQLExprTableSource)right).getExpr();
                if (tableExpr instanceof SQLIdentifierExpr) {
                    String tableName = ((SQLIdentifierExpr)tableExpr).getName();
                    String alias = null;
                    String tenantColumn = null;
                    if (tenantCallBack != null) {
                        tenantColumn = tenantCallBack.getTenantColumn(StatementType.SELECT, tableName);
                    }

                    if (StringUtils.isEmpty(tenantColumn) && ServletPathMatcher.getInstance().matches(tenantTablePattern, tableName)) {
                        tenantColumn = visitor.getConfig().getTenantColumn();
                    }

                    if (!StringUtils.isEmpty(tenantColumn)) {
                        alias = right.getAlias();
                        if (alias == null) {
                            alias = tableName;
                        }

                        SQLExpr item = null;
                        if (alias != null) {
                            item = new SQLPropertyExpr(new SQLIdentifierExpr(alias), tenantColumn);
                        } else {
                            item = new SQLIdentifierExpr(tenantColumn);
                        }

                        SQLSelectItem selectItem = new SQLSelectItem((SQLExpr)item);
                        x.getSelectList().add(selectItem);
                        visitor.setSqlModified(true);
                    }
                }
            }

        }
    }

    private static boolean isSelectStatmentForMultiTenant(SQLSelectQueryBlock queryBlock) {
        SQLObject parent;
        for(parent = queryBlock.getParent(); parent != null && parent instanceof SQLUnionQuery; parent = parent.getParent()) {
        }

        if (!(parent instanceof SQLSelect)) {
            return false;
        } else {
            parent = ((SQLSelect)parent).getParent();
            return parent instanceof SQLSelectStatement;
        }
    }

    private static void checkSelectForMultiTenant(WallVisitor visitor, SQLSelectQueryBlock x) {
        TenantCallBack tenantCallBack = visitor.getConfig().getTenantCallBack();
        String tenantTablePattern = visitor.getConfig().getTenantTablePattern();
        if (tenantCallBack != null || tenantTablePattern != null && tenantTablePattern.length() != 0) {
            if (x == null) {
                throw new IllegalStateException("x is null");
            } else if (isSelectStatmentForMultiTenant(x)) {
                SQLTableSource tableSource = x.getFrom();
                String alias = null;
                String matchTableName = null;
                String tenantColumn = null;
                SQLExpr item;
                if (tableSource instanceof SQLExprTableSource) {
                    item = ((SQLExprTableSource)tableSource).getExpr();
                    if (item instanceof SQLIdentifierExpr) {
                        String tableName = ((SQLIdentifierExpr)item).getName();
                        if (tenantCallBack != null) {
                            tenantColumn = tenantCallBack.getTenantColumn(StatementType.SELECT, tableName);
                        }

                        if (StringUtils.isEmpty(tenantColumn) && ServletPathMatcher.getInstance().matches(tenantTablePattern, tableName)) {
                            tenantColumn = visitor.getConfig().getTenantColumn();
                        }

                        if (!StringUtils.isEmpty(tenantColumn)) {
                            matchTableName = tableName;
                            alias = tableSource.getAlias();
                        }
                    }
                } else if (tableSource instanceof SQLJoinTableSource) {
                    SQLJoinTableSource join = (SQLJoinTableSource)tableSource;
                    if (join.getLeft() instanceof SQLExprTableSource) {
                        SQLExpr tableExpr = ((SQLExprTableSource)join.getLeft()).getExpr();
                        if (tableExpr instanceof SQLIdentifierExpr) {
                            String tableName = ((SQLIdentifierExpr)tableExpr).getName();
                            if (tenantCallBack != null) {
                                tenantColumn = tenantCallBack.getTenantColumn(StatementType.SELECT, tableName);
                            }

                            if (StringUtils.isEmpty(tenantColumn) && ServletPathMatcher.getInstance().matches(tenantTablePattern, tableName)) {
                                tenantColumn = visitor.getConfig().getTenantColumn();
                            }

                            if (!StringUtils.isEmpty(tenantColumn)) {
                                matchTableName = tableName;
                                alias = join.getLeft().getAlias();
                                if (alias == null) {
                                    alias = tableName;
                                }
                            }
                        }

                        checkJoinSelectForMultiTenant(visitor, join, x);
                    } else {
                        checkJoinSelectForMultiTenant(visitor, join, x);
                    }
                }

                if (matchTableName != null) {
                    item = null;
                    if (alias != null) {
                        item = new SQLPropertyExpr(new SQLIdentifierExpr(alias), tenantColumn);
                    } else {
                        item = new SQLIdentifierExpr(tenantColumn);
                    }

                    SQLSelectItem selectItem = new SQLSelectItem((SQLExpr)item);
                    x.getSelectList().add(selectItem);
                    visitor.setSqlModified(true);
                }
            }
        }
    }

    private static void checkUpdateForMultiTenant(WallVisitor visitor, SQLUpdateStatement x) {
        TenantCallBack tenantCallBack = visitor.getConfig().getTenantCallBack();
        String tenantTablePattern = visitor.getConfig().getTenantTablePattern();
        if (tenantCallBack != null || tenantTablePattern != null && tenantTablePattern.length() != 0) {
            if (x == null) {
                throw new IllegalStateException("x is null");
            } else {
                SQLTableSource tableSource = x.getTableSource();
                String alias = null;
                String matchTableName = null;
                String tenantColumn = null;
                SQLExpr item;
                if (tableSource instanceof SQLExprTableSource) {
                    item = ((SQLExprTableSource)tableSource).getExpr();
                    if (item instanceof SQLIdentifierExpr) {
                        String tableName = ((SQLIdentifierExpr)item).getName();
                        if (tenantCallBack != null) {
                            tenantColumn = tenantCallBack.getTenantColumn(StatementType.UPDATE, tableName);
                        }

                        if (StringUtils.isEmpty(tenantColumn) && ServletPathMatcher.getInstance().matches(tenantTablePattern, tableName)) {
                            tenantColumn = visitor.getConfig().getTenantColumn();
                        }

                        if (!StringUtils.isEmpty(tenantColumn)) {
                            matchTableName = tableName;
                            alias = tableSource.getAlias();
                        }
                    }
                }

                if (matchTableName != null) {
                    item = null;
                    if (alias != null) {
                        item = new SQLPropertyExpr(new SQLIdentifierExpr(alias), tenantColumn);
                    } else {
                        item = new SQLIdentifierExpr(tenantColumn);
                    }

                    SQLExpr value = generateTenantValue(visitor, alias, StatementType.UPDATE, matchTableName);
                    SQLUpdateSetItem updateSetItem = new SQLUpdateSetItem();
                    updateSetItem.setColumn((SQLExpr)item);
                    updateSetItem.setValue(value);
                    x.addItem(updateSetItem);
                    visitor.setSqlModified(true);
                }
            }
        }
    }

    private static void checkInsertForMultiTenant(WallVisitor visitor, SQLInsertInto x) {
        TenantCallBack tenantCallBack = visitor.getConfig().getTenantCallBack();
        String tenantTablePattern = visitor.getConfig().getTenantTablePattern();
        if (tenantCallBack != null || tenantTablePattern != null && tenantTablePattern.length() != 0) {
            if (x == null) {
                throw new IllegalStateException("x is null");
            } else {
                SQLExprTableSource tableSource = x.getTableSource();
                String alias = null;
                String matchTableName = null;
                String tenantColumn = null;
                SQLExpr tableExpr = tableSource.getExpr();
                String tableName;
                if (tableExpr instanceof SQLIdentifierExpr) {
                    tableName = ((SQLIdentifierExpr)tableExpr).getName();
                    if (tenantCallBack != null) {
                        tenantColumn = tenantCallBack.getTenantColumn(StatementType.INSERT, tableName);
                    }

                    if (StringUtils.isEmpty(tenantColumn) && ServletPathMatcher.getInstance().matches(tenantTablePattern, tableName)) {
                        tenantColumn = visitor.getConfig().getTenantColumn();
                    }

                    if (!StringUtils.isEmpty(tenantColumn)) {
                        matchTableName = tableName;
                        alias = tableSource.getAlias();
                    }
                }

                if (matchTableName != null) {
                    tableName = null;
                    SQLExpr item;
                    if (alias != null) {
                        item = new SQLPropertyExpr(new SQLIdentifierExpr(alias), tenantColumn);
                    } else {
                        item = new SQLIdentifierExpr(tenantColumn);
                    }

                    SQLExpr value = generateTenantValue(visitor, alias, StatementType.INSERT, matchTableName);
                    x.getColumns().add(item);
                    List<ValuesClause> valuesClauses = null;
                    ValuesClause valuesClause = null;
                    if (x instanceof MySqlInsertStatement) {
                        valuesClauses = ((MySqlInsertStatement)x).getValuesList();
                    } else {
                        valuesClause = x.getValues();
                    }

                    if (valuesClauses != null && valuesClauses.size() > 0) {
                        Iterator var13 = valuesClauses.iterator();

                        while(var13.hasNext()) {
                            ValuesClause clause = (ValuesClause)var13.next();
                            clause.addValue(value);
                        }
                    }

                    if (valuesClause != null) {
                        valuesClause.addValue(value);
                    }

                    SQLSelect select = x.getQuery();
                    if (select != null) {
                        List<SQLSelectQueryBlock> queryBlocks = splitSQLSelectQuery(select.getQuery());
                        Iterator var15 = queryBlocks.iterator();

                        while(var15.hasNext()) {
                            SQLSelectQueryBlock queryBlock = (SQLSelectQueryBlock)var15.next();
                            queryBlock.getSelectList().add(new SQLSelectItem(value));
                        }
                    }

                    visitor.setSqlModified(true);
                }
            }
        }
    }

    private static List<SQLSelectQueryBlock> splitSQLSelectQuery(SQLSelectQuery x) {
        List<SQLSelectQueryBlock> groupList = new ArrayList();
        Stack<SQLSelectQuery> stack = new Stack();
        stack.push(x);

        do {
            SQLSelectQuery query = (SQLSelectQuery)stack.pop();
            if (query instanceof SQLSelectQueryBlock) {
                groupList.add((SQLSelectQueryBlock)query);
            } else if (query instanceof SQLUnionQuery) {
                SQLUnionQuery unionQuery = (SQLUnionQuery)query;
                stack.push(unionQuery.getLeft());
                stack.push(unionQuery.getRight());
            }
        } while(!stack.empty());

        return groupList;
    }

    private static SQLExpr generateTenantValue(WallVisitor visitor, String alias, StatementType statementType, String tableName) {
        TenantCallBack callBack = visitor.getConfig().getTenantCallBack();
        if (callBack != null) {
            WallProvider.setTenantValue(callBack.getTenantValue(statementType, tableName));
        }

        Object tenantValue = WallProvider.getTenantValue();
        Object value;
        if (tenantValue instanceof Number) {
            value = new SQLNumberExpr((Number)tenantValue);
        } else {
            if (!(tenantValue instanceof String)) {
                throw new IllegalStateException("tenant value not support type " + tenantValue);
            }

            value = new SQLCharExpr((String)tenantValue);
        }

        return (SQLExpr)value;
    }

    public static void checkReadOnly(WallVisitor visitor, SQLTableSource tableSource) {
        if (tableSource instanceof SQLExprTableSource) {
            String tableName = null;
            SQLExpr tableNameExpr = ((SQLExprTableSource)tableSource).getExpr();
            if (tableNameExpr instanceof SQLName) {
                tableName = ((SQLName)tableNameExpr).getSimpleName();
            }

            boolean readOnlyValid = visitor.getProvider().checkReadOnlyTable(tableName);
            if (!readOnlyValid) {
                addViolation(visitor, 4000, "table readonly : " + tableName, tableSource);
            }
        } else if (tableSource instanceof SQLJoinTableSource) {
            SQLJoinTableSource join = (SQLJoinTableSource)tableSource;
            checkReadOnly(visitor, join.getLeft());
            checkReadOnly(visitor, join.getRight());
        }

    }

    public static void checkUpdate(WallVisitor visitor, SQLUpdateStatement x) {
        checkReadOnly(visitor, x.getTableSource());
        WallConfig config = visitor.getConfig();
        if (!config.isUpdateAllow()) {
            addViolation(visitor, 1006, "update not allow", x);
        } else {
            SQLExpr where = x.getWhere();
            if (where == null) {
                WallContext context = WallContext.current();
                if (context != null) {
                    context.incrementUpdateNoneConditionWarnings();
                }

                if (config.isUpdateWhereNoneCheck()) {
                    if (!(x instanceof MySqlUpdateStatement)) {
                        addViolation(visitor, 2104, "update none condition not allow", x);
                        return;
                    }

                    MySqlUpdateStatement mysqlUpdate = (MySqlUpdateStatement)x;
                    if (mysqlUpdate.getLimit() == null) {
                        addViolation(visitor, 2104, "update none condition not allow", x);
                        return;
                    }
                }
            } else {
                where.setParent(x);
                checkCondition(visitor, where);
                if (Boolean.TRUE == getConditionValue(visitor, where, config.isUpdateWhereAlayTrueCheck()) && config.isUpdateWhereAlayTrueCheck() && visitor.isSqlEndOfComment() && !isSimpleConstExpr(where)) {
                    addViolation(visitor, 2100, "update alway true condition not allow", x);
                }
            }

            checkUpdateForMultiTenant(visitor, x);
        }
    }

    public static Object getValue(WallVisitor visitor, SQLBinaryOpExpr x) {
        List groupList;
        if (x.getOperator() == SQLBinaryOperator.BooleanOr) {
            groupList = SQLUtils.split(x);
            boolean allFalse = true;

            for(int i = groupList.size() - 1; i >= 0; --i) {
                SQLExpr item = (SQLExpr)groupList.get(i);
                Object result = getValue(visitor, item);
                Boolean booleanVal = SQLEvalVisitorUtils.castToBoolean(result);
                if (Boolean.TRUE == booleanVal) {
                    WallVisitorUtils.WallConditionContext wallContext = getWallConditionContext();
                    if (wallContext != null && !isFirst(item)) {
                        wallContext.setPartAlwayTrue(true);
                    }

                    return true;
                }

                if (Boolean.FALSE != booleanVal) {
                    allFalse = false;
                }
            }

            if (allFalse) {
                return false;
            } else {
                return null;
            }
        } else if (x.getOperator() == SQLBinaryOperator.BooleanAnd) {
            groupList = SQLUtils.split(x);
            int dalConst = 0;
            Boolean allTrue = Boolean.TRUE;

            for(int i = groupList.size() - 1; i >= 0; --i) {
                SQLExpr item = (SQLExpr)groupList.get(i);
                Object result = getValue(visitor, item);
                Boolean booleanVal = SQLEvalVisitorUtils.castToBoolean(result);
                WallVisitorUtils.WallConditionContext wallContext;
                if (Boolean.TRUE == booleanVal) {
                    wallContext = getWallConditionContext();
                    if (wallContext != null && !isFirst(item)) {
                        wallContext.setPartAlwayTrue(true);
                    }

                    ++dalConst;
                } else if (Boolean.FALSE == booleanVal) {
                    wallContext = getWallConditionContext();
                    if (wallContext != null && !isFirst(item)) {
                        wallContext.setPartAlwayFalse(true);
                    }

                    allTrue = Boolean.FALSE;
                    ++dalConst;
                } else {
                    if (allTrue != Boolean.FALSE) {
                        allTrue = null;
                    }

                    dalConst = 0;
                }

                if (dalConst == 2 && visitor != null && !visitor.getConfig().isConditionDoubleConstAllow()) {
                    addViolation(visitor, 2107, "double const condition", x);
                }
            }

            if (Boolean.TRUE == allTrue) {
                return true;
            } else if (Boolean.FALSE == allTrue) {
                return false;
            } else {
                return null;
            }
        } else {
            boolean checkCondition = visitor != null && (!visitor.getConfig().isConstArithmeticAllow() || !visitor.getConfig().isConditionOpBitwseAllow() || !visitor.getConfig().isConditionOpXorAllow());
            if (x.getLeft() instanceof SQLName) {
                if (x.getRight() instanceof SQLName) {
                    if (x.getLeft().toString().equalsIgnoreCase(x.getRight().toString())) {
                        switch(x.getOperator()) {
                            case Equality:
                            case Like:
                                return Boolean.TRUE;
                            case NotEqual:
                            case GreaterThan:
                            case GreaterThanOrEqual:
                            case LessThan:
                            case LessThanOrEqual:
                            case LessThanOrGreater:
                            case NotLike:
                                return Boolean.FALSE;
                        }
                    }
                } else if (!checkCondition) {
                    switch(x.getOperator()) {
                        case Equality:
                        case NotEqual:
                        case GreaterThan:
                        case GreaterThanOrEqual:
                        case LessThan:
                        case LessThanOrEqual:
                        case LessThanOrGreater:
                            return null;
                        case Like:
                    }
                }
            }

            Object leftResult;
            Object rightResult;
            if (x.getLeft() instanceof SQLValuableExpr && x.getRight() instanceof SQLValuableExpr) {
                leftResult = ((SQLValuableExpr)x.getLeft()).getValue();
                rightResult = ((SQLValuableExpr)x.getRight()).getValue();
                boolean evalValue;
                if (x.getOperator() == SQLBinaryOperator.Equality) {
                    evalValue = SQLEvalVisitorUtils.eq(leftResult, rightResult);
                    x.putAttribute("eval.value", evalValue);
                    return evalValue;
                }

                if (x.getOperator() == SQLBinaryOperator.NotEqual) {
                    evalValue = SQLEvalVisitorUtils.eq(leftResult, rightResult);
                    x.putAttribute("eval.value", !evalValue);
                    return !evalValue;
                }
            }

            leftResult = getValue(visitor, x.getLeft());
            rightResult = getValue(visitor, x.getRight());
            if (x.getOperator() == SQLBinaryOperator.Like && leftResult instanceof String && leftResult.equals(rightResult)) {
                addViolation(visitor, 2108, "same const like", x);
            }

            if (x.getOperator() == SQLBinaryOperator.Like || x.getOperator() == SQLBinaryOperator.NotLike) {
                WallContext context = WallContext.current();
                if (context != null && (rightResult instanceof Number || leftResult instanceof Number)) {
                    context.incrementLikeNumberWarnings();
                }
            }

            String dbType = null;
            WallContext wallContext = WallContext.current();
            if (wallContext != null) {
                dbType = wallContext.getDbType();
            }

            return eval(visitor, dbType, x, Collections.emptyList());
        }
    }

    public static SQLExpr getFirst(SQLExpr x) {
        if (x instanceof SQLBinaryOpExpr) {
            SQLBinaryOpExpr binary = (SQLBinaryOpExpr)x;
            if (binary.getOperator() == SQLBinaryOperator.BooleanAnd || binary.getOperator() == SQLBinaryOperator.BooleanOr) {
                return getFirst(((SQLBinaryOpExpr)x).getLeft());
            }
        }

        return x;
    }

    public static List<SQLExpr> getParts(SQLExpr x) {
        List<SQLExpr> exprs = new ArrayList();
        ((List)exprs).add(x);

        while(true) {
            List<SQLExpr> tmp = partExpr((List)exprs);
            if (tmp.size() == ((List)exprs).size()) {
                return (List)exprs;
            }

            exprs = tmp;
        }
    }

    public static List<SQLExpr> partExpr(List<SQLExpr> exprs) {
        List<SQLExpr> partList = new ArrayList();
        Iterator var2 = exprs.iterator();

        while(true) {
            while(var2.hasNext()) {
                SQLExpr x = (SQLExpr)var2.next();
                if (x instanceof SQLBinaryOpExpr) {
                    SQLBinaryOpExpr binary = (SQLBinaryOpExpr)x;
                    if (binary.getOperator() == SQLBinaryOperator.BooleanAnd || binary.getOperator() == SQLBinaryOperator.BooleanOr) {
                        partList.add(((SQLBinaryOpExpr)x).getLeft());
                        partList.add(((SQLBinaryOpExpr)x).getRight());
                        continue;
                    }
                }

                partList.add(x);
            }

            return partList;
        }
    }

    public static boolean isFirst(SQLObject x) {
        if (x == null) {
            return true;
        } else {
            while(true) {
                SQLObject parent = x.getParent();
                if (!(parent instanceof SQLExpr)) {
                    return true;
                }

                if (parent instanceof SQLBinaryOpExpr) {
                    SQLBinaryOpExpr binaryExpr = (SQLBinaryOpExpr)parent;
                    if (x == binaryExpr.getRight()) {
                        return false;
                    }
                }

                x = parent;
            }
        }
    }

    private static boolean hasWhere(SQLSelectQuery selectQuery) {
        if (selectQuery instanceof SQLSelectQueryBlock) {
            return ((SQLSelectQueryBlock)selectQuery).getWhere() != null;
        } else if (!(selectQuery instanceof SQLUnionQuery)) {
            return false;
        } else {
            SQLUnionQuery union = (SQLUnionQuery)selectQuery;
            return hasWhere(union.getLeft()) || hasWhere(union.getRight());
        }
    }

    public static boolean checkSqlExpr(SQLExpr x) {
        if (x == null) {
            return false;
        } else {
            Object obj = x;

            while(true) {
                SQLObject parent = ((SQLObject)obj).getParent();
                if (parent == null) {
                    return false;
                }

                if (parent instanceof SQLSelectGroupByClause) {
                    return true;
                }

                if (parent instanceof SQLOrderBy) {
                    return true;
                }

                if (parent instanceof SQLLimit) {
                    return true;
                }

                if (parent instanceof MySqlOrderingExpr) {
                    return true;
                }

                obj = parent;
            }
        }
    }

    public static boolean isWhereOrHaving(SQLObject x) {
        if (x == null) {
            return false;
        } else {
            while(true) {
                SQLObject parent = x.getParent();
                if (parent == null) {
                    return false;
                }

                if (parent instanceof SQLJoinTableSource) {
                    SQLJoinTableSource joinTableSource = (SQLJoinTableSource)parent;
                    if (joinTableSource.getCondition() == x) {
                        return true;
                    }
                }

                if (parent instanceof SQLUnionQuery) {
                    SQLUnionQuery union = (SQLUnionQuery)parent;
                    if (union.getRight() == x && hasWhere(union.getLeft())) {
                        return true;
                    }
                }

                if (parent instanceof SQLSelectQueryBlock) {
                    SQLSelectQueryBlock query = (SQLSelectQueryBlock)parent;
                    if (query.getWhere() == x) {
                        return true;
                    }
                }

                if (parent instanceof SQLDeleteStatement) {
                    SQLDeleteStatement delete = (SQLDeleteStatement)parent;
                    if (delete.getWhere() == x) {
                        return true;
                    }

                    return false;
                }

                if (parent instanceof SQLUpdateStatement) {
                    SQLUpdateStatement update = (SQLUpdateStatement)parent;
                    if (update.getWhere() == x) {
                        return true;
                    }

                    return false;
                }

                if (parent instanceof SQLSelectGroupByClause) {
                    SQLSelectGroupByClause groupBy = (SQLSelectGroupByClause)parent;
                    if (x == groupBy.getHaving()) {
                        return true;
                    }

                    return false;
                }

                x = parent;
            }
        }
    }

    public static WallVisitorUtils.WallConditionContext getWallConditionContext() {
        return (WallVisitorUtils.WallConditionContext)wallConditionContextLocal.get();
    }

    public static WallVisitorUtils.WallTopStatementContext getWallTopStatementContext() {
        return (WallVisitorUtils.WallTopStatementContext)wallTopStatementContextLocal.get();
    }

    public static void clearWallTopStatementContext() {
        wallTopStatementContextLocal.set(null);
    }

    public static void initWallTopStatementContext() {
        wallTopStatementContextLocal.set(new WallVisitorUtils.WallTopStatementContext());
    }

    public static Object getConditionValue(WallVisitor visitor, SQLExpr x, boolean alwayTrueCheck) {
        WallVisitorUtils.WallConditionContext old = (WallVisitorUtils.WallConditionContext)wallConditionContextLocal.get();

        Object var7;
        try {
            wallConditionContextLocal.set(new WallVisitorUtils.WallConditionContext());
            Object value = getValue(visitor, x);
            WallVisitorUtils.WallConditionContext current = (WallVisitorUtils.WallConditionContext)wallConditionContextLocal.get();
            WallContext context = WallContext.current();
            if (context != null && (current.hasPartAlwayTrue() || Boolean.TRUE == value) && !isFirst(x)) {
                context.incrementWarnings();
            }

            if (current.hasPartAlwayTrue() && !visitor.getConfig().isConditionAndAlwayTrueAllow()) {
                addViolation(visitor, 2100, "part alway true condition not allow", x);
            }

            if (current.hasPartAlwayFalse() && !visitor.getConfig().isConditionAndAlwayFalseAllow()) {
                addViolation(visitor, 2113, "part alway false condition not allow", x);
            }

            if (current.hasConstArithmetic() && !visitor.getConfig().isConstArithmeticAllow()) {
                addViolation(visitor, 2101, "const arithmetic not allow", x);
            }

            if (current.hasXor() && !visitor.getConfig().isConditionOpXorAllow()) {
                addViolation(visitor, 2102, "xor not allow", x);
            }

            if (current.hasBitwise() && !visitor.getConfig().isConditionOpBitwseAllow()) {
                addViolation(visitor, 2103, "bitwise operator not allow", x);
            }

            var7 = value;
        } finally {
            wallConditionContextLocal.set(old);
        }

        return var7;
    }

    public static Object getValueFromAttributes(WallVisitor visitor, SQLObject sqlObject) {
        if (sqlObject == null) {
            return null;
        } else {
            return visitor != null && visitor.getConfig().isConditionLikeTrueAllow() && sqlObject.getAttributes().containsKey("hasTrueLike") ? null : sqlObject.getAttribute("eval.value");
        }
    }

    public static Object getValue(SQLExpr x) {
        return getValue((WallVisitor)null, (SQLExpr)x);
    }

    public static Object getValue(WallVisitor visitor, SQLExpr x) {
        if (x != null && x.getAttributes().containsKey("eval.value")) {
            return getValueFromAttributes(visitor, x);
        } else if (x instanceof SQLBinaryOpExpr) {
            return getValue(visitor, (SQLBinaryOpExpr)x);
        } else if (x instanceof SQLBooleanExpr) {
            return ((SQLBooleanExpr)x).getValue();
        } else if (x instanceof SQLNumericLiteralExpr) {
            return ((SQLNumericLiteralExpr)x).getNumber();
        } else if (x instanceof SQLCharExpr) {
            return ((SQLCharExpr)x).getText();
        } else if (x instanceof SQLNCharExpr) {
            return ((SQLNCharExpr)x).getText();
        } else {
            if (x instanceof SQLNotExpr) {
                Object result = getValue(visitor, ((SQLNotExpr)x).getExpr());
                if (result instanceof Boolean) {
                    return !(Boolean)result;
                }
            }

            SQLCaseExpr caseExpr;
            if (x instanceof SQLQueryExpr) {
                if (isSimpleCountTableSource(visitor, ((SQLQueryExpr)x).getSubQuery())) {
                    return 1;
                }

                if (isSimpleCaseTableSource(visitor, ((SQLQueryExpr)x).getSubQuery())) {
                    SQLSelectQueryBlock queryBlock = (SQLSelectQueryBlock)((SQLQueryExpr)x).getSubQuery().getQuery();
                    caseExpr = (SQLCaseExpr)((SQLSelectItem)queryBlock.getSelectList().get(0)).getExpr();
                    Object result = getValue(caseExpr);
                    if (visitor != null && !visitor.getConfig().isCaseConditionConstAllow()) {
                        boolean leftIsName = false;
                        if (x.getParent() instanceof SQLBinaryOpExpr) {
                            SQLExpr left = ((SQLBinaryOpExpr)x.getParent()).getLeft();
                            if (left instanceof SQLName) {
                                leftIsName = true;
                            }
                        }

                        if (!leftIsName && result != null) {
                            addViolation(visitor, 2109, "const case condition", caseExpr);
                        }
                    }

                    return result;
                }
            }

            String dbType = null;
            if (visitor != null) {
                dbType = visitor.getDbType();
            }

            if (!(x instanceof SQLMethodInvokeExpr) && !(x instanceof SQLBetweenExpr) && !(x instanceof SQLInListExpr) && !(x instanceof SQLUnaryExpr)) {
                if (x instanceof SQLCaseExpr) {
                    if (visitor != null && !visitor.getConfig().isCaseConditionConstAllow()) {
                        caseExpr = (SQLCaseExpr)x;
                        boolean leftIsName = false;
                        if (caseExpr.getParent() instanceof SQLBinaryOpExpr) {
                            SQLExpr left = ((SQLBinaryOpExpr)caseExpr.getParent()).getLeft();
                            if (left instanceof SQLName) {
                                leftIsName = true;
                            }
                        }

                        if (!leftIsName && caseExpr.getValueExpr() == null && caseExpr.getItems().size() > 0) {
                            Item item = (Item)caseExpr.getItems().get(0);
                            Object conditionVal = getValue(visitor, item.getConditionExpr());
                            Object itemVal = getValue(visitor, item.getValueExpr());
                            if (conditionVal instanceof Boolean && itemVal != null) {
                                addViolation(visitor, 2109, "const case condition", caseExpr);
                            }
                        }
                    }

                    return eval(visitor, dbType, x, Collections.emptyList());
                } else {
                    return null;
                }
            } else {
                return eval(visitor, dbType, x, Collections.emptyList());
            }
        }
    }

    public static Object eval(WallVisitor wallVisitor, String dbType, SQLObject sqlObject, List<Object> parameters) {
        SQLEvalVisitor visitor = SQLEvalVisitorUtils.createEvalVisitor(dbType);
        visitor.setParameters(parameters);
        visitor.registerFunction("rand", Nil.instance);
        visitor.registerFunction("sin", Nil.instance);
        visitor.registerFunction("cos", Nil.instance);
        visitor.registerFunction("asin", Nil.instance);
        visitor.registerFunction("acos", Nil.instance);
        sqlObject.accept(visitor);
        return sqlObject instanceof SQLNumericLiteralExpr ? ((SQLNumericLiteralExpr)sqlObject).getNumber() : getValueFromAttributes(wallVisitor, sqlObject);
    }

    public static boolean isSimpleCountTableSource(WallVisitor visitor, SQLTableSource tableSource) {
        if (!(tableSource instanceof SQLSubqueryTableSource)) {
            return false;
        } else {
            SQLSubqueryTableSource subQuery = (SQLSubqueryTableSource)tableSource;
            return isSimpleCountTableSource(visitor, subQuery.getSelect());
        }
    }

    public static boolean isSimpleCountTableSource(WallVisitor visitor, SQLSelect select) {
        SQLSelectQuery query = select.getQuery();
        if (query instanceof SQLSelectQueryBlock) {
            SQLSelectQueryBlock queryBlock = (SQLSelectQueryBlock)query;
            boolean allawTrueWhere = false;
            if (queryBlock.getWhere() == null) {
                allawTrueWhere = true;
            } else {
                Object whereValue = getValue(visitor, queryBlock.getWhere());
                if (whereValue == Boolean.TRUE) {
                    allawTrueWhere = true;
                } else if (whereValue == Boolean.FALSE) {
                    return false;
                }
            }

            boolean simpleCount = false;
            if (queryBlock.getSelectList().size() == 1) {
                SQLExpr selectItemExpr = ((SQLSelectItem)queryBlock.getSelectList().get(0)).getExpr();
                if (selectItemExpr instanceof SQLAggregateExpr && ((SQLAggregateExpr)selectItemExpr).getMethodName().equalsIgnoreCase("COUNT")) {
                    simpleCount = true;
                }
            }

            if (allawTrueWhere && simpleCount) {
                return true;
            }
        }

        return false;
    }

    public static boolean isSimpleCaseTableSource(WallVisitor visitor, SQLSelect select) {
        SQLSelectQuery query = select.getQuery();
        if (query instanceof SQLSelectQueryBlock) {
            SQLSelectQueryBlock queryBlock = (SQLSelectQueryBlock)query;
            boolean allawTrueWhere = false;
            if (queryBlock.getWhere() == null) {
                allawTrueWhere = true;
            } else {
                Object whereValue = getValue(visitor, queryBlock.getWhere());
                if (whereValue == Boolean.TRUE) {
                    allawTrueWhere = true;
                } else if (whereValue == Boolean.FALSE) {
                    return false;
                }
            }

            boolean simpleCase = false;
            if (queryBlock.getSelectList().size() == 1) {
                SQLExpr selectItemExpr = ((SQLSelectItem)queryBlock.getSelectList().get(0)).getExpr();
                if (selectItemExpr instanceof SQLCaseExpr) {
                    simpleCase = true;
                }
            }

            if (allawTrueWhere && simpleCase) {
                return true;
            }
        }

        return false;
    }

    public static void checkFunctionInTableSource(WallVisitor visitor, SQLMethodInvokeExpr x) {
        WallVisitorUtils.WallTopStatementContext topStatementContext = (WallVisitorUtils.WallTopStatementContext)wallTopStatementContextLocal.get();
        if (topStatementContext == null || !topStatementContext.fromSysSchema && !topStatementContext.fromSysTable) {
            checkSchema(visitor, x.getOwner());
            String methodName = x.getMethodName().toLowerCase();
            if (!visitor.getProvider().checkDenyTable(methodName) && (isTopStatementWithTableSource(x) || isFirstSelectTableSource(x)) && topStatementContext != null) {
                topStatementContext.setFromSysSchema(Boolean.TRUE);
                clearViolation(visitor);
            }

        }
    }

    public static void checkFunction(WallVisitor visitor, SQLMethodInvokeExpr x) {
        WallVisitorUtils.WallTopStatementContext topStatementContext = (WallVisitorUtils.WallTopStatementContext)wallTopStatementContextLocal.get();
        if (topStatementContext == null || !topStatementContext.fromSysSchema && !topStatementContext.fromSysTable) {
            checkSchema(visitor, x.getOwner());
            if (visitor.getConfig().isFunctionCheck()) {
                String methodName = x.getMethodName().toLowerCase();
                WallContext context = WallContext.current();
                if (context != null) {
                    context.incrementFunctionInvoke(methodName);
                }

                if (!visitor.getProvider().checkDenyFunction(methodName)) {
                    boolean isTopNoneFrom = isTopNoneFromSelect(visitor, x);
                    if (isTopNoneFrom) {
                        return;
                    }

                    if (isTopFromDenySchema(visitor, x)) {
                        return;
                    }

                    boolean isShow = x.getParent() instanceof MySqlShowGrantsStatement;
                    if (isShow) {
                        return;
                    }

                    if (isWhereOrHaving(x) || checkSqlExpr(x)) {
                        addViolation(visitor, 2001, "deny function : " + methodName, x);
                    }
                }

            }
        }
    }

    public static SQLSelectQueryBlock getQueryBlock(SQLObject x) {
        if (x == null) {
            return null;
        } else if (x instanceof SQLSelectQueryBlock) {
            return (SQLSelectQueryBlock)x;
        } else {
            SQLObject parent = x.getParent();
            if (parent instanceof SQLExpr) {
                return getQueryBlock(parent);
            } else if (parent instanceof SQLSelectItem) {
                return getQueryBlock(parent);
            } else {
                return parent instanceof SQLSelectQueryBlock ? (SQLSelectQueryBlock)parent : null;
            }
        }
    }

    public static boolean isTopNoneFromSelect(WallVisitor visitor, SQLObject x) {
        while(x.getParent() instanceof SQLExpr || x.getParent() instanceof Item) {
            x = x.getParent();
        }

        if (!(x.getParent() instanceof SQLSelectItem)) {
            return false;
        } else {
            SQLSelectItem item = (SQLSelectItem)x.getParent();
            if (!(item.getParent() instanceof SQLSelectQueryBlock)) {
                return false;
            } else {
                SQLSelectQueryBlock queryBlock = (SQLSelectQueryBlock)item.getParent();
                if (!queryBlockFromIsNull(visitor, queryBlock)) {
                    return false;
                } else if (!(queryBlock.getParent() instanceof SQLSelect)) {
                    return false;
                } else {
                    SQLSelect select = (SQLSelect)queryBlock.getParent();
                    if (!(select.getParent() instanceof SQLSelectStatement)) {
                        return false;
                    } else {
                        SQLSelectStatement stmt = (SQLSelectStatement)select.getParent();
                        return stmt.getParent() == null;
                    }
                }
            }
        }
    }

    private static boolean isTopFromDenySchema(WallVisitor visitor, SQLMethodInvokeExpr x) {
        SQLObject parent;
        for(parent = x.getParent(); parent instanceof SQLExpr || parent instanceof Item || parent instanceof SQLSelectItem; parent = parent.getParent()) {
        }

        if (parent instanceof SQLSelectQueryBlock) {
            SQLSelectQueryBlock queryBlock = (SQLSelectQueryBlock)parent;
            if (!(queryBlock.getParent() instanceof SQLSelect)) {
                return false;
            } else {
                SQLSelect select = (SQLSelect)queryBlock.getParent();
                if (!(select.getParent() instanceof SQLSelectStatement)) {
                    return false;
                } else {
                    SQLSelectStatement stmt = (SQLSelectStatement)select.getParent();
                    if (stmt.getParent() != null) {
                        return false;
                    } else {
                        SQLTableSource from = queryBlock.getFrom();
                        if (from instanceof SQLExprTableSource) {
                            SQLExpr fromExpr = ((SQLExprTableSource)from).getExpr();
                            if (fromExpr instanceof SQLName) {
                                String fromTableName = fromExpr.toString();
                                return visitor.isDenyTable(fromTableName);
                            }
                        }

                        return false;
                    }
                }
            }
        } else {
            return false;
        }
    }

    private static boolean checkSchema(WallVisitor visitor, SQLExpr x) {
        WallVisitorUtils.WallTopStatementContext topStatementContext = (WallVisitorUtils.WallTopStatementContext)wallTopStatementContextLocal.get();
        if (topStatementContext == null || !topStatementContext.fromSysSchema && !topStatementContext.fromSysTable) {
            if (x instanceof SQLName) {
                String owner = ((SQLName)x).getSimpleName();
                owner = form(owner);
                if (isInTableSource(x) && !visitor.getProvider().checkDenySchema(owner)) {
                    if (!isTopStatementWithTableSource(x) && !isFirstSelectTableSource(x) && !isFirstInSubQuery(x)) {
                        SQLObject parent;
                        for(parent = x.getParent(); parent != null && !(parent instanceof SQLStatement); parent = parent.getParent()) {
                        }

                        boolean sameToTopSelectSchema = false;
                        if (parent instanceof SQLSelectStatement) {
                            SQLSelectStatement selectStmt = (SQLSelectStatement)parent;
                            SQLSelectQuery query = selectStmt.getSelect().getQuery();
                            if (query instanceof SQLSelectQueryBlock) {
                                SQLSelectQueryBlock queryBlock = (SQLSelectQueryBlock)query;

                                SQLTableSource from;
                                for(from = queryBlock.getFrom(); from instanceof SQLJoinTableSource; from = ((SQLJoinTableSource)from).getLeft()) {
                                }

                                if (from instanceof SQLExprTableSource) {
                                    SQLExpr expr = ((SQLExprTableSource)from).getExpr();
                                    if (expr instanceof SQLPropertyExpr) {
                                        SQLExpr schemaExpr = ((SQLPropertyExpr)expr).getOwner();
                                        if (schemaExpr instanceof SQLIdentifierExpr) {
                                            String schema = ((SQLIdentifierExpr)schemaExpr).getName();
                                            schema = form(schema);
                                            if (schema.equalsIgnoreCase(owner)) {
                                                sameToTopSelectSchema = true;
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        if (!sameToTopSelectSchema) {
                            addViolation(visitor, 2002, "deny schema : " + owner, x);
                        }
                    } else if (topStatementContext != null) {
                        topStatementContext.setFromSysSchema(Boolean.TRUE);
                        clearViolation(visitor);
                    }

                    return true;
                }

                if (visitor.getConfig().isDenyObjects(owner)) {
                    addViolation(visitor, 2005, "deny object : " + owner, x);
                    return true;
                }
            }

            return x instanceof SQLPropertyExpr ? checkSchema(visitor, ((SQLPropertyExpr)x).getOwner()) : true;
        } else {
            return true;
        }
    }

    private static boolean isInTableSource(SQLObject x) {
        while(x instanceof SQLExpr) {
            x = x.getParent();
        }

        if (x instanceof SQLExprTableSource) {
            return true;
        } else {
            return false;
        }
    }

    private static boolean isFirstInSubQuery(SQLObject x) {
        while(x instanceof SQLExpr) {
            x = x.getParent();
        }

        if (!(x instanceof SQLExprTableSource)) {
            return false;
        } else {
            SQLSelect sqlSelect = null;

            SQLObject parent;
            for(parent = x.getParent(); parent != null; parent = parent.getParent()) {
                if (parent instanceof SQLSelect) {
                    sqlSelect = (SQLSelect)parent;
                    break;
                }
            }

            if (sqlSelect == null) {
                return false;
            } else {
                parent = sqlSelect.getParent();
                if (parent instanceof SQLInSubQueryExpr && isFirst(parent)) {
                    SQLInSubQueryExpr sqlInSubQueryExpr = (SQLInSubQueryExpr)parent;
                    if (!(sqlInSubQueryExpr.getParent() instanceof SQLSelectQueryBlock)) {
                        return false;
                    } else {
                        SQLSelectQueryBlock queryBlock = (SQLSelectQueryBlock)sqlInSubQueryExpr.getParent();
                        if (!(queryBlock.getParent() instanceof SQLSelect)) {
                            return false;
                        } else {
                            SQLSelect select = (SQLSelect)queryBlock.getParent();
                            if (!(select.getParent() instanceof SQLSelectStatement)) {
                                return false;
                            } else {
                                SQLSelectStatement stmt = (SQLSelectStatement)select.getParent();
                                return stmt.getParent() == null;
                            }
                        }
                    }
                } else {
                    return false;
                }
            }
        }
    }

    private static boolean isFirstSelectTableSource(SQLObject x) {
        while(x instanceof SQLExpr) {
            x = x.getParent();
        }

        if (!(x instanceof SQLExprTableSource)) {
            return false;
        } else {
            SQLSelectQueryBlock queryBlock = null;

            SQLObject parent;
            for(parent = x.getParent(); parent != null; parent = parent.getParent()) {
                if (parent instanceof SQLSelectQueryBlock) {
                    queryBlock = (SQLSelectQueryBlock)parent;
                    break;
                }
            }

            if (queryBlock == null) {
                return false;
            } else {
                boolean isWhereQueryExpr = false;
                boolean isSelectItem = false;

                do {
                    x = parent;
                    parent = parent.getParent();
                    if (parent instanceof SQLUnionQuery) {
                        SQLUnionQuery union = (SQLUnionQuery)parent;
                        if (union.getRight() == x && hasTableSource(union.getLeft())) {
                            return false;
                        }
                    } else if (!(parent instanceof SQLQueryExpr) && !(parent instanceof SQLInSubQueryExpr) && !(parent instanceof SQLExistsExpr)) {
                        if (parent instanceof SQLSelectItem) {
                            isSelectItem = true;
                        } else if ((isWhereQueryExpr || isSelectItem) && parent instanceof SQLSelectQueryBlock && hasTableSource((SQLSelectQuery)((SQLSelectQueryBlock)parent))) {
                            return false;
                        }
                    } else {
                        isWhereQueryExpr = isWhereOrHaving(parent);
                    }
                } while(parent != null);

                return true;
            }
        }
    }

    private static boolean hasTableSource(SQLSelectQuery x) {
        if (!(x instanceof SQLUnionQuery)) {
            return x instanceof SQLSelectQueryBlock ? hasTableSource(((SQLSelectQueryBlock)x).getFrom()) : false;
        } else {
            SQLUnionQuery union = (SQLUnionQuery)x;
            return hasTableSource(union.getLeft()) || hasTableSource(union.getRight());
        }
    }

    private static boolean hasTableSource(SQLTableSource x) {
        if (x == null) {
            return false;
        } else if (x instanceof SQLExprTableSource) {
            SQLExpr fromExpr = ((SQLExprTableSource)x).getExpr();
            if (fromExpr instanceof SQLName) {
                String name = fromExpr.toString();
                name = form(name);
                if (name.equalsIgnoreCase("DUAL")) {
                    return false;
                }
            }

            return true;
        } else if (!(x instanceof SQLJoinTableSource)) {
            return x instanceof SQLSubqueryTableSource ? hasTableSource(((SQLSubqueryTableSource)x).getSelect().getQuery()) : false;
        } else {
            SQLJoinTableSource join = (SQLJoinTableSource)x;
            return hasTableSource(join.getLeft()) || hasTableSource(join.getRight());
        }
    }

    private static boolean isTopStatementWithTableSource(SQLObject x) {
        while(x instanceof SQLExpr) {
            x = x.getParent();
        }

        if (x instanceof SQLExprTableSource) {
            x = x.getParent();
            if (x instanceof SQLStatement) {
                x = x.getParent();
                if (x == null) {
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean isTopSelectItem(SQLObject x) {
        while(x.getParent() instanceof SQLExpr || x.getParent() instanceof Item) {
            x = x.getParent();
        }

        if (!(x.getParent() instanceof SQLSelectItem)) {
            return false;
        } else {
            SQLSelectItem item = (SQLSelectItem)x.getParent();
            return isTopSelectStatement(item.getParent());
        }
    }

    private static boolean isTopSelectStatement(SQLObject x) {
        if (!(x instanceof SQLSelectQueryBlock)) {
            return false;
        } else {
            SQLSelectQueryBlock queryBlock = (SQLSelectQueryBlock)x;
            if (!(queryBlock.getParent() instanceof SQLSelect)) {
                return false;
            } else {
                SQLSelect select = (SQLSelect)queryBlock.getParent();
                if (!(select.getParent() instanceof SQLSelectStatement)) {
                    return false;
                } else {
                    SQLSelectStatement stmt = (SQLSelectStatement)select.getParent();
                    return stmt.getParent() == null;
                }
            }
        }
    }

    public static boolean isTopSelectOutFile(MySqlOutFileExpr x) {
        if (!(x.getParent() instanceof SQLExprTableSource)) {
            return false;
        } else {
            SQLExprTableSource tableSource = (SQLExprTableSource)x.getParent();
            return isTopSelectStatement(tableSource.getParent());
        }
    }

    public static boolean check(WallVisitor visitor, SQLExprTableSource x) {
        WallVisitorUtils.WallTopStatementContext topStatementContext = (WallVisitorUtils.WallTopStatementContext)wallTopStatementContextLocal.get();
        SQLExpr expr = x.getExpr();
        if (expr instanceof SQLPropertyExpr) {
            boolean checkResult = checkSchema(visitor, ((SQLPropertyExpr)expr).getOwner());
            if (!checkResult) {
                return false;
            }
        }

        if (expr instanceof SQLName) {
            String tableName = ((SQLName)expr).getSimpleName();
            WallContext context = WallContext.current();
            if (context != null) {
                WallSqlTableStat tableStat = context.getTableStat(tableName);
                if (tableStat != null) {
                    SQLObject parent;
                    for(parent = x.getParent(); parent instanceof SQLTableSource; parent = parent.getParent()) {
                    }

                    if (parent instanceof SQLSelectQueryBlock) {
                        SQLSelectQueryBlock queryBlock = (SQLSelectQueryBlock)parent;
                        if (x == queryBlock.getInto()) {
                            tableStat.incrementSelectIntoCount();
                        } else {
                            tableStat.incrementSelectCount();
                        }
                    } else if (parent instanceof SQLTruncateStatement) {
                        tableStat.incrementTruncateCount();
                    } else if (parent instanceof SQLInsertStatement) {
                        tableStat.incrementInsertCount();
                    } else if (parent instanceof SQLDeleteStatement) {
                        tableStat.incrementDeleteCount();
                    } else if (parent instanceof SQLUpdateStatement) {
                        tableStat.incrementUpdateCount();
                    } else if (parent instanceof MySqlReplaceStatement) {
                        tableStat.incrementReplaceCount();
                    }
                }
            }

            if (topStatementContext != null && (topStatementContext.fromSysSchema || topStatementContext.fromSysTable)) {
                return true;
            }

            if (visitor.isDenyTable(tableName) && (topStatementContext == null || !topStatementContext.fromPermitTable())) {
                if (!isTopStatementWithTableSource(x) && !isFirstSelectTableSource(x)) {
                    boolean isTopNoneFrom = isTopNoneFromSelect(visitor, x);
                    if (isTopNoneFrom) {
                        return false;
                    } else {
                        addViolation(visitor, 2004, "deny table : " + tableName, x);
                        return false;
                    }
                } else {
                    if (topStatementContext != null) {
                        topStatementContext.setFromSysTable(Boolean.TRUE);
                        clearViolation(visitor);
                    }

                    return false;
                }
            }

            if (visitor.getConfig().getPermitTables().contains(tableName) && isFirstSelectTableSource(x)) {
                if (topStatementContext != null) {
                    topStatementContext.setFromPermitTable(Boolean.TRUE);
                }

                return false;
            }
        }

        return true;
    }

    private static void addViolation(WallVisitor visitor, int errorCode, String message, SQLObject x) {
        visitor.addViolation(new IllegalSQLObjectViolation(errorCode, message, visitor.toSQL(x)));
    }

    private static void clearViolation(WallVisitor visitor) {
        visitor.getViolations().clear();
    }

    public static void checkUnion(WallVisitor visitor, SQLUnionQuery x) {
        if (x.getOperator() == SQLUnionOperator.MINUS && !visitor.getConfig().isMinusAllow()) {
            addViolation(visitor, 1008, "minus not allow", x);
        } else if (x.getOperator() == SQLUnionOperator.INTERSECT && !visitor.getConfig().isIntersectAllow()) {
            addViolation(visitor, 1008, "intersect not allow", x);
        } else {
            if (!queryBlockFromIsNull(visitor, x.getLeft()) && queryBlockFromIsNull(visitor, x.getRight())) {
                boolean isTopUpdateStatement = false;
                boolean isTopInsertStatement = false;
                SQLObject selectParent = x.getParent();

                while(true) {
                    if (!(selectParent instanceof SQLSelectQuery) && !(selectParent instanceof SQLJoinTableSource) && !(selectParent instanceof SQLSubqueryTableSource) && !(selectParent instanceof SQLSelect)) {
                        if (selectParent instanceof SQLUpdateStatement) {
                            isTopUpdateStatement = true;
                        }

                        if (selectParent instanceof SQLInsertStatement) {
                            isTopInsertStatement = true;
                        }

                        if (isTopUpdateStatement || isTopInsertStatement) {
                            return;
                        }

                        if (x.getLeft() instanceof SQLSelectQueryBlock) {
                            SQLSelectQueryBlock left = (SQLSelectQueryBlock)x.getLeft();
                            SQLTableSource tableSource = left.getFrom();
                            if (left.getWhere() == null && tableSource != null && tableSource instanceof SQLExprTableSource) {
                                return;
                            }
                        }

                        WallContext context = WallContext.current();
                        if (context != null) {
                            context.incrementUnionWarnings();
                        }

                        if ((x.getOperator() == SQLUnionOperator.UNION || x.getOperator() == SQLUnionOperator.UNION_ALL || x.getOperator() == SQLUnionOperator.DISTINCT) && visitor.getConfig().isSelectUnionCheck() && visitor.isSqlEndOfComment() || x.getOperator() == SQLUnionOperator.MINUS && visitor.getConfig().isSelectMinusCheck() || x.getOperator() == SQLUnionOperator.INTERSECT && visitor.getConfig().isSelectIntersectCheck() || x.getOperator() == SQLUnionOperator.EXCEPT && visitor.getConfig().isSelectExceptCheck()) {
                            addViolation(visitor, 5000, x.getOperator().toString() + " query not contains 'from clause'", x);
                        }
                        break;
                    }

                    selectParent = selectParent.getParent();
                }
            }

        }
    }

    public static boolean queryBlockFromIsNull(WallVisitor visitor, SQLSelectQuery query) {
        return queryBlockFromIsNull(visitor, query, true);
    }

    public static boolean queryBlockFromIsNull(WallVisitor visitor, SQLSelectQuery query, boolean checkSelectConst) {
        if (query instanceof SQLSelectQueryBlock) {
            SQLSelectQueryBlock queryBlock = (SQLSelectQueryBlock)query;
            SQLTableSource from = queryBlock.getFrom();
            if (queryBlock.getSelectList().size() < 1) {
                return false;
            }

            if (from == null) {
                boolean itemIsConst = true;
                boolean itemHasAlias = false;
                Iterator var14 = queryBlock.getSelectList().iterator();

                while(var14.hasNext()) {
                    SQLSelectItem item = (SQLSelectItem)var14.next();
                    if (!(item.getExpr() instanceof SQLIdentifierExpr) && !(item.getExpr() instanceof SQLPropertyExpr)) {
                        if (item.getAlias() == null) {
                            continue;
                        }

                        itemHasAlias = true;
                        break;
                    }

                    itemIsConst = false;
                    break;
                }

                if (itemIsConst && !itemHasAlias) {
                    return true;
                }

                return false;
            }

            SQLExpr where;
            if (from instanceof SQLExprTableSource) {
                where = ((SQLExprTableSource)from).getExpr();
                if (where instanceof SQLName) {
                    String name = where.toString();
                    name = form(name);
                    if (name.equalsIgnoreCase("DUAL")) {
                        return true;
                    }
                }
            }

            if (queryBlock.getSelectList().size() == 1 && ((SQLSelectItem)queryBlock.getSelectList().get(0)).getExpr() instanceof SQLAllColumnExpr && from instanceof SQLSubqueryTableSource) {
                SQLSelectQuery subQuery = ((SQLSubqueryTableSource)from).getSelect().getQuery();
                if (queryBlockFromIsNull(visitor, subQuery)) {
                    return true;
                }
            }

            if (checkSelectConst) {
                where = queryBlock.getWhere();
                if (where != null) {
                    Object whereValue = getValue(visitor, where);
                    if (Boolean.TRUE == whereValue) {
                        boolean allIsConst = true;
                        Iterator var8 = queryBlock.getSelectList().iterator();

                        while(var8.hasNext()) {
                            SQLSelectItem item = (SQLSelectItem)var8.next();
                            if (getValue(visitor, item.getExpr()) == null) {
                                allIsConst = false;
                                break;
                            }
                        }

                        if (allIsConst) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    public static String form(String name) {
        if (name.startsWith("\"") && name.endsWith("\"")) {
            name = name.substring(1, name.length() - 1);
        }

        if (name.startsWith("'") && name.endsWith("'")) {
            name = name.substring(1, name.length() - 1);
        }

        if (name.startsWith("`") && name.endsWith("`")) {
            name = name.substring(1, name.length() - 1);
        }

        name = name.toLowerCase();
        return name;
    }

    public static void loadResource(Set<String> names, String resource) {
        try {
            boolean hasResource = false;
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            if (classLoader != null) {
                Enumeration e = Thread.currentThread().getContextClassLoader().getResources(resource);

                while(e.hasMoreElements()) {
                    URL url = (URL)e.nextElement();
                    InputStream in = null;

                    try {
                        in = url.openStream();
                        readFromInputStream(names, in);
                        hasResource = true;
                    } finally {
                        JdbcUtils.close(in);
                    }
                }
            }

            if (!hasResource) {
                if (!resource.startsWith("/")) {
                    resource = "/" + resource;
                }

                InputStream in = null;

                try {
                    in = WallVisitorUtils.class.getResourceAsStream(resource);
                    if (in != null) {
                        readFromInputStream(names, in);
                    }
                } finally {
                    JdbcUtils.close(in);
                }
            }
        } catch (IOException var17) {
            LOG.error("load oracle deny tables errror", var17);
        }

    }

    private static void readFromInputStream(Set<String> names, InputStream in) throws IOException {
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new InputStreamReader(in));

            while(true) {
                String line = reader.readLine();
                if (line == null) {
                    return;
                }

                line = line.trim();
                if (line.length() > 0) {
                    line = line.toLowerCase();
                    names.add(line);
                }
            }
        } finally {
            JdbcUtils.close(reader);
        }
    }

    public static void preVisitCheck(WallVisitor visitor, SQLObject x) {
        WallConfig config = visitor.getProvider().getConfig();
        if (x instanceof SQLStatement) {
            boolean allow = false;
            short errorCode;
            String denyMessage;
            if (x instanceof SQLInsertStatement) {
                allow = config.isInsertAllow();
                denyMessage = "insert not allow";
                errorCode = 1004;
            } else if (x instanceof SQLSelectStatement) {
                allow = true;
                denyMessage = "select not allow";
                errorCode = 1002;
            } else if (x instanceof SQLDeleteStatement) {
                allow = config.isDeleteAllow();
                denyMessage = "delete not allow";
                errorCode = 1005;
            } else if (x instanceof SQLUpdateStatement) {
                allow = config.isUpdateAllow();
                denyMessage = "update not allow";
                errorCode = 1006;
            } else if (x instanceof OracleMultiInsertStatement) {
                allow = true;
                denyMessage = "multi-insert not allow";
                errorCode = 1004;
            } else if (x instanceof SQLMergeStatement) {
                allow = config.isMergeAllow();
                denyMessage = "merge not allow";
                errorCode = 1009;
            } else if (x instanceof SQLCallStatement) {
                allow = config.isCallAllow();
                denyMessage = "call not allow";
                errorCode = 1300;
            } else if (x instanceof SQLTruncateStatement) {
                allow = config.isTruncateAllow();
                denyMessage = "truncate not allow";
                errorCode = 1100;
            } else if (!(x instanceof SQLCreateTableStatement) && !(x instanceof SQLCreateIndexStatement) && !(x instanceof SQLCreateViewStatement) && !(x instanceof SQLCreateTriggerStatement) && !(x instanceof SQLCreateSequenceStatement)) {
                if (x instanceof SQLAlterTableStatement) {
                    allow = config.isAlterTableAllow();
                    denyMessage = "alter table not allow";
                    errorCode = 1102;
                } else if (!(x instanceof SQLDropTableStatement) && !(x instanceof SQLDropIndexStatement) && !(x instanceof SQLDropViewStatement) && !(x instanceof SQLDropTriggerStatement) && !(x instanceof SQLDropSequenceStatement) && !(x instanceof SQLDropProcedureStatement)) {
                    if (!(x instanceof MySqlSetCharSetStatement) && !(x instanceof MySqlSetNamesStatement) && !(x instanceof SQLSetStatement)) {
                        if (x instanceof MySqlReplaceStatement) {
                            allow = config.isReplaceAllow();
                            denyMessage = "replace not allow";
                            errorCode = 1010;
                        } else if (x instanceof MySqlDescribeStatement) {
                            allow = config.isDescribeAllow();
                            denyMessage = "describe not allow";
                            errorCode = 1201;
                        } else if (!(x instanceof MySqlShowStatement) && !(x instanceof SQLShowTablesStatement)) {
                            if (x instanceof MySqlCommitStatement) {
                                allow = config.isCommitAllow();
                                denyMessage = "commit not allow";
                                errorCode = 1301;
                            } else if (x instanceof SQLRollbackStatement) {
                                allow = config.isRollbackAllow();
                                denyMessage = "rollback not allow";
                                errorCode = 1302;
                            } else if (x instanceof SQLUseStatement) {
                                allow = config.isUseAllow();
                                denyMessage = "use not allow";
                                errorCode = 1203;
                            } else if (x instanceof MySqlRenameTableStatement) {
                                allow = config.isRenameTableAllow();
                                denyMessage = "rename table not allow";
                                errorCode = 1105;
                            } else if (x instanceof MySqlHintStatement) {
                                allow = config.isHintAllow();
                                denyMessage = "hint not allow";
                                errorCode = 1400;
                            } else if (x instanceof MySqlLockTableStatement) {
                                allow = config.isLockTableAllow();
                                denyMessage = "lock table not allow";
                                errorCode = 1106;
                            } else if (x instanceof SQLStartTransactionStatement) {
                                allow = config.isStartTransactionAllow();
                                denyMessage = "start transaction not allow";
                                errorCode = 1303;
                            } else if (x instanceof SQLBlockStatement) {
                                allow = config.isBlockAllow();
                                denyMessage = "block statement not allow";
                                errorCode = 1304;
                            } else {
                                allow = config.isNoneBaseStatementAllow();
                                errorCode = 1999;
                                denyMessage = x.getClass() + " not allow";
                            }
                        } else {
                            allow = config.isShowAllow();
                            denyMessage = "show not allow";
                            errorCode = 1202;
                        }
                    } else {
                        allow = config.isSetAllow();
                        denyMessage = "set not allow";
                        errorCode = 1200;
                    }
                } else {
                    allow = config.isDropTableAllow();
                    denyMessage = "drop table not allow";
                    errorCode = 1103;
                }
            } else {
                allow = config.isCreateTableAllow();
                denyMessage = "create table not allow";
                errorCode = 1101;
            }

            if (!allow) {
                addViolation(visitor, errorCode, denyMessage, x);
            }

        }
    }

    public static void check(WallVisitor visitor, SQLCommentHint x) {
        if (!visitor.getConfig().isHintAllow()) {
            addViolation(visitor, 2110, "hint not allow", x);
        } else {
            String text = x.getText();
            text = text.trim();
            if (text.startsWith("!")) {
                text = text.substring(1);
            }

            if (text.length() != 0) {
                int pos;
                for(pos = 0; pos < text.length(); ++pos) {
                    char ch = text.charAt(pos);
                    if (ch < '0' || ch > '9') {
                        break;
                    }
                }

                if (pos == 5) {
                    text = text.substring(5);
                    text = text.trim();
                }

                text = text.toUpperCase();
                boolean isWhite = false;
                String[] var5 = whiteHints;
                int var6 = var5.length;

                for(int var7 = 0; var7 < var6; ++var7) {
                    String hint = var5[var7];
                    if (text.equals(hint)) {
                        isWhite = true;
                        break;
                    }
                }

                if (!isWhite && (text.startsWith("FORCE INDEX") || text.startsWith("IGNORE INDEX"))) {
                    isWhite = true;
                }

                if (!isWhite && text.startsWith("SET")) {
                    SQLStatementParser parser = new MySqlStatementParser(text);
                    List<SQLStatement> statementList = parser.parseStatementList();
                    if (statementList != null && statementList.size() > 0) {
                        SQLStatement statement = (SQLStatement)statementList.get(0);
                        if (statement instanceof SQLSetStatement || statement instanceof MySqlSetCharSetStatement || statement instanceof MySqlSetNamesStatement) {
                            isWhite = true;
                        }
                    }
                }

                if (!isWhite) {
                    addViolation(visitor, 2110, "hint not allow", x);
                }

            }
        }
    }

    public static class WallConditionContext {
        private boolean partAlwayTrue = false;
        private boolean partAlwayFalse = false;
        private boolean constArithmetic = false;
        private boolean xor = false;
        private boolean bitwise = false;

        public WallConditionContext() {
        }

        public boolean hasPartAlwayTrue() {
            return this.partAlwayTrue;
        }

        public void setPartAlwayTrue(boolean partAllowTrue) {
            this.partAlwayTrue = partAllowTrue;
        }

        public boolean hasPartAlwayFalse() {
            return this.partAlwayFalse;
        }

        public void setPartAlwayFalse(boolean partAlwayFalse) {
            this.partAlwayFalse = partAlwayFalse;
        }

        public boolean hasConstArithmetic() {
            return this.constArithmetic;
        }

        public void setConstArithmetic(boolean constArithmetic) {
            this.constArithmetic = constArithmetic;
        }

        public boolean hasXor() {
            return this.xor;
        }

        public void setXor(boolean xor) {
            this.xor = xor;
        }

        public boolean hasBitwise() {
            return this.bitwise;
        }

        public void setBitwise(boolean bitwise) {
            this.bitwise = bitwise;
        }
    }

    public static class WallTopStatementContext {
        private boolean fromSysTable = false;
        private boolean fromSysSchema = false;
        private boolean fromPermitTable = false;

        public WallTopStatementContext() {
        }

        public boolean fromSysTable() {
            return this.fromSysTable;
        }

        public void setFromSysTable(boolean fromSysTable) {
            this.fromSysTable = fromSysTable;
        }

        public boolean fromSysSchema() {
            return this.fromSysSchema;
        }

        public void setFromSysSchema(boolean fromSysSchema) {
            this.fromSysSchema = fromSysSchema;
        }

        public boolean fromPermitTable() {
            return this.fromPermitTable;
        }

        public void setFromPermitTable(boolean fromPermitTable) {
            this.fromPermitTable = fromPermitTable;
        }
    }
}

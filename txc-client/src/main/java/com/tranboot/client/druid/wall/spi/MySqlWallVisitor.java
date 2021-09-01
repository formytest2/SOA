package com.tranboot.client.druid.wall.spi;

import com.tranboot.client.druid.sql.SQLUtils;
import com.tranboot.client.druid.sql.ast.SQLCommentHint;
import com.tranboot.client.druid.sql.ast.SQLLimit;
import com.tranboot.client.druid.sql.ast.SQLName;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLInListExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLMethodInvokeExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLNumericLiteralExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLPropertyExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLVariantRefExpr;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLAssignItem;
import com.tranboot.client.druid.sql.ast.statement.SQLCallStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLCreateTableStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLCreateTriggerStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLDeleteStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLDropTableStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLExprTableSource;
import com.tranboot.client.druid.sql.ast.statement.SQLInsertStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLSelectGroupByClause;
import com.tranboot.client.druid.sql.ast.statement.SQLSelectItem;
import com.tranboot.client.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.tranboot.client.druid.sql.ast.statement.SQLSelectStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLSetStatement;
import com.tranboot.client.druid.sql.ast.statement.SQLUnionQuery;
import com.tranboot.client.druid.sql.ast.statement.SQLUpdateStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.expr.MySqlOutFileExpr;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlCreateTableStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlDeleteStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlInsertStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlReplaceStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlShowCreateTableStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlUnionQuery;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlUpdateStatement;
import com.tranboot.client.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.tranboot.client.druid.sql.dialect.mysql.visitor.MySqlASTVisitorAdapter;
import com.tranboot.client.druid.wall.Violation;
import com.tranboot.client.druid.wall.WallConfig;
import com.tranboot.client.druid.wall.WallContext;
import com.tranboot.client.druid.wall.WallProvider;
import com.tranboot.client.druid.wall.WallSqlTableStat;
import com.tranboot.client.druid.wall.WallVisitor;
import com.tranboot.client.druid.wall.spi.WallVisitorUtils.WallTopStatementContext;
import com.tranboot.client.druid.wall.violation.IllegalSQLObjectViolation;
import java.util.ArrayList;
import java.util.List;

public class MySqlWallVisitor extends MySqlASTVisitorAdapter implements WallVisitor, MySqlASTVisitor {
    private final WallConfig config;
    private final WallProvider provider;
    private final List<Violation> violations = new ArrayList();
    private boolean sqlModified = false;
    private boolean sqlEndOfComment = false;

    public MySqlWallVisitor(WallProvider provider) {
        this.config = provider.getConfig();
        this.provider = provider;
    }

    public String getDbType() {
        return "mysql";
    }

    public boolean isSqlModified() {
        return this.sqlModified;
    }

    public void setSqlModified(boolean sqlModified) {
        this.sqlModified = sqlModified;
    }

    public WallProvider getProvider() {
        return this.provider;
    }

    public WallConfig getConfig() {
        return this.config;
    }

    public void addViolation(Violation violation) {
        this.violations.add(violation);
    }

    public List<Violation> getViolations() {
        return this.violations;
    }

    public boolean visit(SQLInListExpr x) {
        WallVisitorUtils.check(this, x);
        return true;
    }

    public boolean visit(SQLBinaryOpExpr x) {
        return WallVisitorUtils.check(this, x);
    }

    public boolean visit(SQLSelectQueryBlock x) {
        WallVisitorUtils.checkSelelct(this, x);
        return true;
    }

    public boolean visit(MySqlSelectQueryBlock x) {
        WallVisitorUtils.checkSelelct(this, x);
        return true;
    }

    public boolean visit(SQLSelectGroupByClause x) {
        WallVisitorUtils.checkHaving(this, x.getHaving());
        return true;
    }

    public boolean visit(MySqlDeleteStatement x) {
        WallVisitorUtils.checkReadOnly(this, x.getFrom());
        return this.visit((SQLDeleteStatement)x);
    }

    public boolean visit(SQLDeleteStatement x) {
        WallVisitorUtils.checkDelete(this, x);
        return true;
    }

    public boolean visit(MySqlUpdateStatement x) {
        return this.visit((SQLUpdateStatement)x);
    }

    public boolean visit(SQLUpdateStatement x) {
        WallVisitorUtils.initWallTopStatementContext();
        WallVisitorUtils.checkUpdate(this, x);
        return true;
    }

    public void endVisit(SQLUpdateStatement x) {
        WallVisitorUtils.clearWallTopStatementContext();
    }

    public boolean visit(MySqlInsertStatement x) {
        return this.visit((SQLInsertStatement)x);
    }

    public boolean visit(SQLInsertStatement x) {
        WallVisitorUtils.initWallTopStatementContext();
        WallVisitorUtils.checkInsert(this, x);
        return true;
    }

    public void endVisit(SQLInsertStatement x) {
        WallVisitorUtils.clearWallTopStatementContext();
    }

    public boolean visit(SQLSelectStatement x) {
        if (!this.config.isSelelctAllow()) {
            this.getViolations().add(new IllegalSQLObjectViolation(1002, "select not allow", this.toSQL(x)));
            return false;
        } else {
            WallVisitorUtils.initWallTopStatementContext();
            return true;
        }
    }

    public void endVisit(SQLSelectStatement x) {
        WallVisitorUtils.clearWallTopStatementContext();
    }

    public boolean visit(SQLLimit x) {
        if (x.getRowCount() instanceof SQLNumericLiteralExpr) {
            WallContext context = WallContext.current();
            int rowCount = ((SQLNumericLiteralExpr)x.getRowCount()).getNumber().intValue();
            if (rowCount == 0) {
                if (context != null) {
                    context.incrementWarnings();
                }

                if (!this.provider.getConfig().isLimitZeroAllow()) {
                    this.getViolations().add(new IllegalSQLObjectViolation(2200, "limit row 0", this.toSQL(x)));
                }
            }
        }

        return true;
    }

    public boolean visit(SQLPropertyExpr x) {
        if (x.getOwner() instanceof SQLVariantRefExpr) {
            SQLVariantRefExpr varExpr = (SQLVariantRefExpr)x.getOwner();
            SQLObject parent = x.getParent();
            String varName = varExpr.getName();
            if (varName.equalsIgnoreCase("@@session") || varName.equalsIgnoreCase("@@global")) {
                if (!(parent instanceof SQLSelectItem) && !(parent instanceof SQLAssignItem)) {
                    this.violations.add(new IllegalSQLObjectViolation(2003, "variable in condition not allow", this.toSQL(x)));
                    return false;
                }

                if (!this.checkVar(x.getParent(), x.getName())) {
                    boolean isTop = WallVisitorUtils.isTopNoneFromSelect(this, x);
                    if (!isTop) {
                        boolean allow = true;
                        if (this.isDeny(varName) && (WallVisitorUtils.isWhereOrHaving(x) || WallVisitorUtils.checkSqlExpr(varExpr))) {
                            allow = false;
                        }

                        if (!allow) {
                            this.violations.add(new IllegalSQLObjectViolation(2003, "variable not allow : " + x.getName(), this.toSQL(x)));
                        }
                    }
                }

                return false;
            }
        }

        WallVisitorUtils.check(this, x);
        return true;
    }

    public boolean checkVar(SQLObject parent, String varName) {
        if (varName == null) {
            return false;
        } else if (varName.equals("?")) {
            return true;
        } else if (!this.config.isVariantCheck()) {
            return true;
        } else {
            if (varName.startsWith("@@")) {
                if (!(parent instanceof SQLSelectItem) && !(parent instanceof SQLAssignItem)) {
                    return false;
                }

                varName = varName.substring(2);
            }

            return this.config.getPermitVariants().contains(varName);
        }
    }

    public boolean isDeny(String varName) {
        if (varName.startsWith("@@")) {
            varName = varName.substring(2);
        }

        varName = varName.toLowerCase();
        return this.config.getDenyVariants().contains(varName);
    }

    public boolean visit(SQLVariantRefExpr x) {
        String varName = x.getName();
        if (varName == null) {
            return false;
        } else {
            if (varName.startsWith("@@") && !this.checkVar(x.getParent(), x.getName())) {
                WallTopStatementContext topStatementContext = WallVisitorUtils.getWallTopStatementContext();
                if (topStatementContext != null && (topStatementContext.fromSysSchema() || topStatementContext.fromSysTable())) {
                    return false;
                }

                boolean isTop = WallVisitorUtils.isTopNoneFromSelect(this, x);
                if (!isTop) {
                    boolean allow = true;
                    if (this.isDeny(varName) && (WallVisitorUtils.isWhereOrHaving(x) || WallVisitorUtils.checkSqlExpr(x))) {
                        allow = false;
                    }

                    if (!allow) {
                        this.violations.add(new IllegalSQLObjectViolation(2003, "variable not allow : " + x.getName(), this.toSQL(x)));
                    }
                }
            }

            return false;
        }
    }

    public boolean visit(SQLMethodInvokeExpr x) {
        WallVisitorUtils.checkFunction(this, x);
        return true;
    }

    public boolean visit(SQLExprTableSource x) {
        WallVisitorUtils.check(this, x);
        return !(x.getExpr() instanceof SQLName);
    }

    public boolean visit(MySqlOutFileExpr x) {
        if (!this.config.isSelectIntoOutfileAllow() && !WallVisitorUtils.isTopSelectOutFile(x)) {
            this.violations.add(new IllegalSQLObjectViolation(3000, "into out file not allow", this.toSQL(x)));
        }

        return true;
    }

    public boolean visit(SQLUnionQuery x) {
        WallVisitorUtils.checkUnion(this, x);
        return true;
    }

    public boolean visit(MySqlUnionQuery x) {
        WallVisitorUtils.checkUnion(this, x);
        return true;
    }

    public String toSQL(SQLObject obj) {
        return SQLUtils.toMySqlString(obj);
    }

    public boolean isDenyTable(String name) {
        if (!this.config.isTableCheck()) {
            return false;
        } else {
            return !this.provider.checkDenyTable(name);
        }
    }

    public void preVisit(SQLObject x) {
        WallVisitorUtils.preVisitCheck(this, x);
    }

    public boolean visit(SQLSelectItem x) {
        WallVisitorUtils.check(this, x);
        return true;
    }

    public boolean visit(SQLCreateTableStatement x) {
        WallVisitorUtils.check(this, x);
        return false;
    }

    public boolean visit(MySqlCreateTableStatement x) {
        WallVisitorUtils.check(this, x);
        return true;
    }

    public boolean visit(SQLAlterTableStatement x) {
        WallVisitorUtils.check(this, x);
        return true;
    }

    public boolean visit(SQLDropTableStatement x) {
        WallVisitorUtils.check(this, x);
        return true;
    }

    public boolean visit(SQLSetStatement x) {
        return false;
    }

    public boolean visit(MySqlReplaceStatement x) {
        return true;
    }

    public boolean visit(SQLCallStatement x) {
        return false;
    }

    public boolean visit(SQLCommentHint x) {
        WallVisitorUtils.check(this, x);
        return true;
    }

    public boolean visit(MySqlShowCreateTableStatement x) {
        String tableName = ((SQLName)x.getName()).getSimpleName();
        WallContext context = WallContext.current();
        if (context != null) {
            WallSqlTableStat tableStat = context.getTableStat(tableName);
            if (tableStat != null) {
                tableStat.incrementShowCount();
            }
        }

        return false;
    }

    public boolean visit(SQLCreateTriggerStatement x) {
        return false;
    }

    public boolean isSqlEndOfComment() {
        return this.sqlEndOfComment;
    }

    public void setSqlEndOfComment(boolean sqlEndOfComment) {
        this.sqlEndOfComment = sqlEndOfComment;
    }
}

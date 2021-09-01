package com.tranboot.client.druid.wall.spi;

import com.tranboot.client.druid.sql.SQLUtils;
import com.tranboot.client.druid.sql.ast.SQLName;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLIdentifierExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLInListExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLMethodInvokeExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLPropertyExpr;
import com.tranboot.client.druid.sql.ast.statement.SQLAlterTableStatement;
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
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleCreateTableStatement;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleDeleteStatement;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleInsertStatement;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleMultiInsertStatement;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleSelectQueryBlock;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleSelectTableReference;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleUpdateStatement;
import com.tranboot.client.druid.sql.dialect.oracle.ast.stmt.OracleMultiInsertStatement.InsertIntoClause;
import com.tranboot.client.druid.sql.dialect.oracle.visitor.OracleASTVisitorAdapter;
import com.tranboot.client.druid.wall.Violation;
import com.tranboot.client.druid.wall.WallConfig;
import com.tranboot.client.druid.wall.WallProvider;
import com.tranboot.client.druid.wall.WallVisitor;
import com.tranboot.client.druid.wall.violation.IllegalSQLObjectViolation;
import java.util.ArrayList;
import java.util.List;

public class OracleWallVisitor extends OracleASTVisitorAdapter implements WallVisitor {
    private final WallConfig config;
    private final WallProvider provider;
    private final List<Violation> violations = new ArrayList();
    private boolean sqlModified = false;
    private boolean sqlEndOfComment = false;

    public OracleWallVisitor(WallProvider provider) {
        this.config = provider.getConfig();
        this.provider = provider;
    }

    public String getDbType() {
        return "oracle";
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

    public boolean visit(SQLIdentifierExpr x) {
        String name = x.getName();
        name = WallVisitorUtils.form(name);
        if (this.config.isVariantCheck() && this.config.getDenyVariants().contains(name)) {
            this.getViolations().add(new IllegalSQLObjectViolation(2003, "variable not allow : " + name, this.toSQL(x)));
        }

        return true;
    }

    public boolean visit(SQLPropertyExpr x) {
        WallVisitorUtils.check(this, x);
        return true;
    }

    public boolean visit(SQLInListExpr x) {
        WallVisitorUtils.check(this, x);
        return true;
    }

    public boolean visit(SQLBinaryOpExpr x) {
        return WallVisitorUtils.check(this, x);
    }

    public boolean visit(SQLMethodInvokeExpr x) {
        WallVisitorUtils.checkFunction(this, x);
        return true;
    }

    public boolean visit(OracleSelectTableReference x) {
        return WallVisitorUtils.check(this, x);
    }

    public boolean visit(SQLExprTableSource x) {
        WallVisitorUtils.check(this, x);
        return !(x.getExpr() instanceof SQLName);
    }

    public boolean visit(SQLSelectGroupByClause x) {
        WallVisitorUtils.checkHaving(this, x.getHaving());
        return true;
    }

    public boolean visit(SQLSelectQueryBlock x) {
        WallVisitorUtils.checkSelelct(this, x);
        return true;
    }

    public boolean visit(OracleSelectQueryBlock x) {
        WallVisitorUtils.checkSelelct(this, x);
        return true;
    }

    public boolean visit(SQLUnionQuery x) {
        WallVisitorUtils.checkUnion(this, x);
        return true;
    }

    public String toSQL(SQLObject obj) {
        return SQLUtils.toOracleString(obj);
    }

    public boolean isDenyTable(String name) {
        if (!this.config.isTableCheck()) {
            return false;
        } else {
            name = WallVisitorUtils.form(name);
            if (!name.startsWith("v$") && !name.startsWith("v_$")) {
                return !this.provider.checkDenyTable(name);
            } else {
                return true;
            }
        }
    }

    public void preVisit(SQLObject x) {
        WallVisitorUtils.preVisitCheck(this, x);
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

    public boolean visit(OracleInsertStatement x) {
        return this.visit((SQLInsertStatement)x);
    }

    public boolean visit(SQLInsertStatement x) {
        WallVisitorUtils.initWallTopStatementContext();
        WallVisitorUtils.checkInsert(this, x);
        return true;
    }

    public void endVisit(OracleInsertStatement x) {
        this.endVisit((SQLInsertStatement)x);
    }

    public void endVisit(SQLInsertStatement x) {
        WallVisitorUtils.clearWallTopStatementContext();
    }

    public boolean visit(InsertIntoClause x) {
        WallVisitorUtils.checkInsert(this, x);
        return true;
    }

    public boolean visit(OracleMultiInsertStatement x) {
        if (!this.config.isInsertAllow()) {
            this.getViolations().add(new IllegalSQLObjectViolation(1004, "insert not allow", this.toSQL(x)));
            return false;
        } else {
            WallVisitorUtils.initWallTopStatementContext();
            return true;
        }
    }

    public void endVisit(OracleMultiInsertStatement x) {
        WallVisitorUtils.clearWallTopStatementContext();
    }

    public boolean visit(OracleDeleteStatement x) {
        return this.visit((SQLDeleteStatement)x);
    }

    public boolean visit(SQLDeleteStatement x) {
        WallVisitorUtils.checkDelete(this, x);
        return true;
    }

    public void endVisit(OracleDeleteStatement x) {
        this.endVisit((SQLDeleteStatement)x);
    }

    public void endVisit(SQLDeleteStatement x) {
        WallVisitorUtils.clearWallTopStatementContext();
    }

    public boolean visit(OracleUpdateStatement x) {
        return this.visit((SQLUpdateStatement)x);
    }

    public boolean visit(SQLUpdateStatement x) {
        WallVisitorUtils.initWallTopStatementContext();
        WallVisitorUtils.checkUpdate(this, x);
        return true;
    }

    public boolean visit(SQLSelectItem x) {
        WallVisitorUtils.check(this, x);
        return true;
    }

    public boolean visit(SQLCreateTableStatement x) {
        WallVisitorUtils.check(this, x);
        return true;
    }

    public boolean visit(OracleCreateTableStatement x) {
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

    public boolean visit(SQLCallStatement x) {
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

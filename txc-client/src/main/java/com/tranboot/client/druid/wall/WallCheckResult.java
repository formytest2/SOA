package com.tranboot.client.druid.wall;

import com.tranboot.client.druid.sql.ast.SQLStatement;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class WallCheckResult {
    private final List<SQLStatement> statementList;
    private final Map<String, WallSqlTableStat> tableStats;
    private final List<Violation> violations;
    private final Map<String, WallSqlFunctionStat> functionStats;
    private final boolean syntaxError;
    private final WallSqlStat sqlStat;
    private String sql;

    public WallCheckResult() {
        this((WallSqlStat)null);
    }

    public WallCheckResult(WallSqlStat sqlStat, List<SQLStatement> stmtList) {
        if (sqlStat != null) {
            this.tableStats = sqlStat.getTableStats();
            this.violations = sqlStat.getViolations();
            this.functionStats = sqlStat.getFunctionStats();
            this.statementList = stmtList;
            this.syntaxError = sqlStat.isSyntaxError();
        } else {
            this.tableStats = Collections.emptyMap();
            this.violations = Collections.emptyList();
            this.functionStats = Collections.emptyMap();
            this.statementList = stmtList;
            this.syntaxError = false;
        }

        this.sqlStat = sqlStat;
    }

    public WallCheckResult(WallSqlStat sqlStat) {
        this(sqlStat, Collections.emptyList());
    }

    public WallCheckResult(WallSqlStat sqlStat, List<Violation> violations, Map<String, WallSqlTableStat> tableStats, Map<String, WallSqlFunctionStat> functionStats, List<SQLStatement> statementList, boolean syntaxError) {
        this.sqlStat = sqlStat;
        this.tableStats = tableStats;
        this.violations = violations;
        this.functionStats = functionStats;
        this.statementList = statementList;
        this.syntaxError = syntaxError;
    }

    public String getSql() {
        return this.sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public List<Violation> getViolations() {
        return this.violations;
    }

    public List<SQLStatement> getStatementList() {
        return this.statementList;
    }

    public Map<String, WallSqlTableStat> getTableStats() {
        return this.tableStats;
    }

    public Map<String, WallSqlFunctionStat> getFunctionStats() {
        return this.functionStats;
    }

    public boolean isSyntaxError() {
        return this.syntaxError;
    }

    public WallSqlStat getSqlStat() {
        return this.sqlStat;
    }
}

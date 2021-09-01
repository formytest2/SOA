package com.tranboot.client.druid.wall;

import com.tranboot.client.druid.util.JdbcSqlStatUtils;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;

public class WallSqlStat {
    private volatile long executeCount;
    private volatile long executeErrorCount;
    private volatile long fetchRowCount;
    private volatile long updateCount;
    static final AtomicLongFieldUpdater<WallSqlStat> executeCountUpdater = AtomicLongFieldUpdater.newUpdater(WallSqlStat.class, "executeCount");
    static final AtomicLongFieldUpdater<WallSqlStat> executeErrorCountUpdater = AtomicLongFieldUpdater.newUpdater(WallSqlStat.class, "executeErrorCount");
    static final AtomicLongFieldUpdater<WallSqlStat> fetchRowCountUpdater = AtomicLongFieldUpdater.newUpdater(WallSqlStat.class, "fetchRowCount");
    static final AtomicLongFieldUpdater<WallSqlStat> updateCountUpdater = AtomicLongFieldUpdater.newUpdater(WallSqlStat.class, "updateCount");
    private final Map<String, WallSqlTableStat> tableStats;
    private final List<Violation> violations;
    private final Map<String, WallSqlFunctionStat> functionStats;
    private final boolean syntaxError;
    private String sample;
    private long sqlHash;

    public WallSqlStat(Map<String, WallSqlTableStat> tableStats, Map<String, WallSqlFunctionStat> functionStats, boolean syntaxError) {
        this(tableStats, functionStats, Collections.emptyList(), syntaxError);
    }

    public WallSqlStat(Map<String, WallSqlTableStat> tableStats, Map<String, WallSqlFunctionStat> functionStats, List<Violation> violations, boolean syntaxError) {
        this.violations = violations;
        this.tableStats = tableStats;
        this.functionStats = functionStats;
        this.syntaxError = syntaxError;
    }

    public long getSqlHash() {
        return this.sqlHash;
    }

    public void setSqlHash(long sqlHash) {
        this.sqlHash = sqlHash;
    }

    public String getSample() {
        return this.sample;
    }

    public void setSample(String sample) {
        this.sample = sample;
    }

    public long incrementAndGetExecuteCount() {
        return executeCountUpdater.incrementAndGet(this);
    }

    public long incrementAndGetExecuteErrorCount() {
        return executeErrorCountUpdater.incrementAndGet(this);
    }

    public long getExecuteCount() {
        return this.executeCount;
    }

    public long getExecuteErrorCount() {
        return this.executeErrorCount;
    }

    public long addAndFetchRowCount(long delta) {
        return fetchRowCountUpdater.addAndGet(this, delta);
    }

    public long getEffectRowCount() {
        return this.fetchRowCount;
    }

    public long getUpdateCount() {
        return this.updateCount;
    }

    public void addUpdateCount(long delta) {
        updateCountUpdater.addAndGet(this, delta);
    }

    public Map<String, WallSqlTableStat> getTableStats() {
        return this.tableStats;
    }

    public Map<String, WallSqlFunctionStat> getFunctionStats() {
        return this.functionStats;
    }

    public List<Violation> getViolations() {
        return this.violations;
    }

    public boolean isSyntaxError() {
        return this.syntaxError;
    }

    public WallSqlStatValue getStatValue(boolean reset) {
        WallSqlStatValue statValue = new WallSqlStatValue();
        statValue.setExecuteCount(JdbcSqlStatUtils.get(this, executeCountUpdater, reset));
        statValue.setExecuteErrorCount(JdbcSqlStatUtils.get(this, executeErrorCountUpdater, reset));
        statValue.setFetchRowCount(JdbcSqlStatUtils.get(this, fetchRowCountUpdater, reset));
        statValue.setUpdateCount(JdbcSqlStatUtils.get(this, updateCountUpdater, reset));
        statValue.setSyntaxError(this.syntaxError);
        statValue.setSqlSample(this.sample);
        if (this.violations.size() > 0) {
            String violationMessage = ((Violation)this.violations.get(0)).getMessage();
            statValue.setViolationMessage(violationMessage);
        }

        return statValue;
    }
}

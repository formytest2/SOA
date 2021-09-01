package com.tranboot.client.druid.wall;

import java.util.HashMap;
import java.util.Map;

public class WallContext {
    private static final ThreadLocal<WallContext> contextLocal = new ThreadLocal();
    private WallSqlStat sqlStat;
    private Map<String, WallSqlTableStat> tableStats;
    private Map<String, WallSqlFunctionStat> functionStats;
    private final String dbType;
    private int commentCount;
    private int warnings = 0;
    private int unionWarnings = 0;
    private int updateNoneConditionWarnings = 0;
    private int deleteNoneConditionWarnings = 0;
    private int likeNumberWarnings = 0;

    public WallContext(String dbType) {
        this.dbType = dbType;
    }

    public void incrementFunctionInvoke(String tableName) {
        if (this.functionStats == null) {
            this.functionStats = new HashMap();
        }

        String lowerCaseName = tableName.toLowerCase();
        WallSqlFunctionStat stat = (WallSqlFunctionStat)this.functionStats.get(lowerCaseName);
        if (stat == null) {
            if (this.functionStats.size() > 100) {
                return;
            }

            stat = new WallSqlFunctionStat();
            this.functionStats.put(tableName, stat);
        }

        stat.incrementInvokeCount();
    }

    public WallSqlTableStat getTableStat(String tableName) {
        if (this.tableStats == null) {
            this.tableStats = new HashMap(2);
        }

        String lowerCaseName = tableName.toLowerCase();
        WallSqlTableStat stat = (WallSqlTableStat)this.tableStats.get(lowerCaseName);
        if (stat == null) {
            if (this.tableStats.size() > 100) {
                return null;
            }

            stat = new WallSqlTableStat();
            this.tableStats.put(tableName, stat);
        }

        return stat;
    }

    public static WallContext createIfNotExists(String dbType) {
        WallContext context = (WallContext)contextLocal.get();
        if (context == null) {
            context = new WallContext(dbType);
            contextLocal.set(context);
        }

        return context;
    }

    public static WallContext create(String dbType) {
        WallContext context = new WallContext(dbType);
        contextLocal.set(context);
        return context;
    }

    public static WallContext current() {
        return (WallContext)contextLocal.get();
    }

    public static void clearContext() {
        contextLocal.remove();
    }

    public static void setContext(WallContext context) {
        contextLocal.set(context);
    }

    public WallSqlStat getSqlStat() {
        return this.sqlStat;
    }

    public void setSqlStat(WallSqlStat sqlStat) {
        this.sqlStat = sqlStat;
    }

    public Map<String, WallSqlTableStat> getTableStats() {
        return this.tableStats;
    }

    public Map<String, WallSqlFunctionStat> getFunctionStats() {
        return this.functionStats;
    }

    public String getDbType() {
        return this.dbType;
    }

    public int getCommentCount() {
        return this.commentCount;
    }

    public void incrementCommentCount() {
        if (this.commentCount == 0) {
            ++this.warnings;
        }

        ++this.commentCount;
    }

    public int getWarnings() {
        return this.warnings;
    }

    public void incrementWarnings() {
        ++this.warnings;
    }

    public int getLikeNumberWarnings() {
        return this.likeNumberWarnings;
    }

    public void incrementLikeNumberWarnings() {
        if (this.likeNumberWarnings == 0) {
            ++this.warnings;
        }

        ++this.likeNumberWarnings;
    }

    public int getUnionWarnings() {
        return this.unionWarnings;
    }

    public void incrementUnionWarnings() {
        if (this.unionWarnings == 0) {
            this.incrementWarnings();
        }

        ++this.unionWarnings;
    }

    public int getUpdateNoneConditionWarnings() {
        return this.updateNoneConditionWarnings;
    }

    public void incrementUpdateNoneConditionWarnings() {
        ++this.updateNoneConditionWarnings;
    }

    public int getDeleteNoneConditionWarnings() {
        return this.deleteNoneConditionWarnings;
    }

    public void incrementDeleteNoneConditionWarnings() {
        ++this.deleteNoneConditionWarnings;
    }
}

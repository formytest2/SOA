package com.tranboot.client.druid.wall;

import com.tranboot.client.druid.sql.SQLUtils;
import com.tranboot.client.druid.sql.ast.SQLStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlHintStatement;
import com.tranboot.client.druid.sql.parser.NotAllowCommentException;
import com.tranboot.client.druid.sql.parser.ParserException;
import com.tranboot.client.druid.sql.parser.SQLStatementParser;
import com.tranboot.client.druid.sql.parser.Token;
import com.tranboot.client.druid.sql.parser.Lexer.CommentHandler;
import com.tranboot.client.druid.sql.visitor.ExportParameterVisitor;
import com.tranboot.client.druid.sql.visitor.ParameterizedOutputVisitorUtils;
import com.tranboot.client.druid.util.JdbcSqlStatUtils;
import com.tranboot.client.druid.util.LRUCache;
import com.tranboot.client.druid.util.Utils;
import com.tranboot.client.druid.wall.spi.WallVisitorUtils;
import com.tranboot.client.druid.wall.violation.IllegalSQLObjectViolation;
import com.tranboot.client.druid.wall.violation.SyntaxErrorViolation;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class WallProvider {
    private String name;
    private final Map<String, Object> attributes = new ConcurrentHashMap(1, 0.75F, 1);
    private boolean whiteListEnable = true;
    private LRUCache<String, WallSqlStat> whiteList;
    private int MAX_SQL_LENGTH = 8192;
    private int whiteSqlMaxSize = 1000;
    private boolean blackListEnable = true;
    private LRUCache<String, WallSqlStat> blackList;
    private LRUCache<String, WallSqlStat> blackMergedList;
    private int blackSqlMaxSize = 200;
    protected final WallConfig config;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private static final ThreadLocal<Boolean> privileged = new ThreadLocal();
    private final ConcurrentMap<String, WallFunctionStat> functionStats = new ConcurrentHashMap(16, 0.75F, 1);
    private final ConcurrentMap<String, WallTableStat> tableStats = new ConcurrentHashMap(16, 0.75F, 1);
    public final WallDenyStat commentDeniedStat = new WallDenyStat();
    protected String dbType = null;
    protected final AtomicLong checkCount = new AtomicLong();
    protected final AtomicLong hardCheckCount = new AtomicLong();
    protected final AtomicLong whiteListHitCount = new AtomicLong();
    protected final AtomicLong blackListHitCount = new AtomicLong();
    protected final AtomicLong syntaxErrorCount = new AtomicLong();
    protected final AtomicLong violationCount = new AtomicLong();
    protected final AtomicLong violationEffectRowCount = new AtomicLong();
    private static final ThreadLocal<Object> tenantValueLocal = new ThreadLocal();

    public WallProvider(WallConfig config) {
        this.config = config;
    }

    public WallProvider(WallConfig config, String dbType) {
        this.config = config;
        this.dbType = dbType;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    public void reset() {
        this.checkCount.set(0L);
        this.hardCheckCount.set(0L);
        this.violationCount.set(0L);
        this.whiteListHitCount.set(0L);
        this.blackListHitCount.set(0L);
        this.clearWhiteList();
        this.clearBlackList();
        this.functionStats.clear();
        this.tableStats.clear();
    }

    public ConcurrentMap<String, WallTableStat> getTableStats() {
        return this.tableStats;
    }

    public ConcurrentMap<String, WallFunctionStat> getFunctionStats() {
        return this.functionStats;
    }

    public WallSqlStat getSqlStat(String sql) {
        WallSqlStat sqlStat = this.getWhiteSql(sql);
        if (sqlStat == null) {
            sqlStat = this.getBlackSql(sql);
        }

        return sqlStat;
    }

    public WallTableStat getTableStat(String tableName) {
        String lowerCaseName = tableName.toLowerCase();
        if (lowerCaseName.startsWith("`") && lowerCaseName.endsWith("`")) {
            lowerCaseName = lowerCaseName.substring(1, lowerCaseName.length() - 1);
        }

        return this.getTableStatWithLowerName(lowerCaseName);
    }

    public void addUpdateCount(WallSqlStat sqlStat, long updateCount) {
        sqlStat.addUpdateCount(updateCount);
        Map<String, WallSqlTableStat> sqlTableStats = sqlStat.getTableStats();
        if (sqlTableStats != null) {
            Iterator var5 = sqlTableStats.entrySet().iterator();

            while(var5.hasNext()) {
                Entry<String, WallSqlTableStat> entry = (Entry)var5.next();
                String tableName = (String)entry.getKey();
                WallTableStat tableStat = this.getTableStat(tableName);
                if (tableStat != null) {
                    WallSqlTableStat sqlTableStat = (WallSqlTableStat)entry.getValue();
                    if (sqlTableStat.getDeleteCount() > 0) {
                        tableStat.addDeleteDataCount(updateCount);
                    } else if (sqlTableStat.getUpdateCount() > 0) {
                        tableStat.addUpdateDataCount(updateCount);
                    } else if (sqlTableStat.getInsertCount() > 0) {
                        tableStat.addInsertDataCount(updateCount);
                    }
                }
            }

        }
    }

    public void addFetchRowCount(WallSqlStat sqlStat, long fetchRowCount) {
        sqlStat.addAndFetchRowCount(fetchRowCount);
        Map<String, WallSqlTableStat> sqlTableStats = sqlStat.getTableStats();
        if (sqlTableStats != null) {
            Iterator var5 = sqlTableStats.entrySet().iterator();

            while(var5.hasNext()) {
                Entry<String, WallSqlTableStat> entry = (Entry)var5.next();
                String tableName = (String)entry.getKey();
                WallTableStat tableStat = this.getTableStat(tableName);
                if (tableStat != null) {
                    WallSqlTableStat sqlTableStat = (WallSqlTableStat)entry.getValue();
                    if (sqlTableStat.getSelectCount() > 0) {
                        tableStat.addFetchRowCount(fetchRowCount);
                    }
                }
            }

        }
    }

    public WallTableStat getTableStatWithLowerName(String lowerCaseName) {
        WallTableStat stat = (WallTableStat)this.tableStats.get(lowerCaseName);
        if (stat == null) {
            if (this.tableStats.size() > 10000) {
                return null;
            }

            this.tableStats.putIfAbsent(lowerCaseName, new WallTableStat());
            stat = (WallTableStat)this.tableStats.get(lowerCaseName);
        }

        return stat;
    }

    public WallFunctionStat getFunctionStat(String functionName) {
        String lowerCaseName = functionName.toLowerCase();
        return this.getFunctionStatWithLowerName(lowerCaseName);
    }

    public WallFunctionStat getFunctionStatWithLowerName(String lowerCaseName) {
        WallFunctionStat stat = (WallFunctionStat)this.functionStats.get(lowerCaseName);
        if (stat == null) {
            if (this.functionStats.size() > 10000) {
                return null;
            }

            this.functionStats.putIfAbsent(lowerCaseName, new WallFunctionStat());
            stat = (WallFunctionStat)this.functionStats.get(lowerCaseName);
        }

        return stat;
    }

    public WallConfig getConfig() {
        return this.config;
    }

    public WallSqlStat addWhiteSql(String sql, Map<String, WallSqlTableStat> tableStats, Map<String, WallSqlFunctionStat> functionStats, boolean syntaxError) {
        if (!this.whiteListEnable) {
            WallSqlStat stat = new WallSqlStat(tableStats, functionStats, syntaxError);
            return stat;
        } else {
            String mergedSql;
            WallSqlStat newStat;
            try {
                mergedSql = ParameterizedOutputVisitorUtils.parameterize(sql, this.dbType);
            } catch (Exception var24) {
                newStat = new WallSqlStat(tableStats, functionStats, syntaxError);
                newStat.incrementAndGetExecuteCount();
                return newStat;
            }

            WallSqlStat mergedStat;
            if (mergedSql != sql) {
                this.lock.readLock().lock();

                try {
                    if (this.whiteList == null) {
                        this.whiteList = new LRUCache(this.whiteSqlMaxSize);
                    }

                    mergedStat = (WallSqlStat)this.whiteList.get(mergedSql);
                } finally {
                    this.lock.readLock().unlock();
                }

                if (mergedStat == null) {
                    newStat = new WallSqlStat(tableStats, functionStats, syntaxError);
                    newStat.setSample(sql);
                    this.lock.writeLock().lock();

                    try {
                        mergedStat = (WallSqlStat)this.whiteList.get(mergedSql);
                        if (mergedStat == null) {
                            this.whiteList.put(mergedSql, newStat);
                            mergedStat = newStat;
                        }
                    } finally {
                        this.lock.writeLock().unlock();
                    }
                }

                mergedStat.incrementAndGetExecuteCount();
                return mergedStat;
            } else {
                this.lock.writeLock().lock();

                try {
                    if (this.whiteList == null) {
                        this.whiteList = new LRUCache(this.whiteSqlMaxSize);
                    }

                    mergedStat = (WallSqlStat)this.whiteList.get(sql);
                    if (mergedStat == null) {
                        mergedStat = new WallSqlStat(tableStats, functionStats, syntaxError);
                        this.whiteList.put(sql, mergedStat);
                        mergedStat.setSample(sql);
                        mergedStat.incrementAndGetExecuteCount();
                    }

                    newStat = mergedStat;
                } finally {
                    this.lock.writeLock().unlock();
                }

                return newStat;
            }
        }
    }

    public WallSqlStat addBlackSql(String sql, Map<String, WallSqlTableStat> tableStats, Map<String, WallSqlFunctionStat> functionStats, List<Violation> violations, boolean syntaxError) {
        if (!this.blackListEnable) {
            return new WallSqlStat(tableStats, functionStats, violations, syntaxError);
        } else {
            String mergedSql;
            try {
                mergedSql = ParameterizedOutputVisitorUtils.parameterize(sql, this.dbType);
            } catch (Exception var12) {
                mergedSql = sql;
            }

            this.lock.writeLock().lock();

            WallSqlStat var8;
            try {
                if (this.blackList == null) {
                    this.blackList = new LRUCache(this.blackSqlMaxSize);
                }

                if (this.blackMergedList == null) {
                    this.blackMergedList = new LRUCache(this.blackSqlMaxSize);
                }

                WallSqlStat wallStat = (WallSqlStat)this.blackList.get(sql);
                if (wallStat == null) {
                    wallStat = (WallSqlStat)this.blackMergedList.get(mergedSql);
                    if (wallStat == null) {
                        wallStat = new WallSqlStat(tableStats, functionStats, violations, syntaxError);
                        this.blackMergedList.put(mergedSql, wallStat);
                        wallStat.setSample(sql);
                    }

                    wallStat.incrementAndGetExecuteCount();
                    this.blackList.put(sql, wallStat);
                }

                var8 = wallStat;
            } finally {
                this.lock.writeLock().unlock();
            }

            return var8;
        }
    }

    public Set<String> getWhiteList() {
        Set<String> hashSet = new HashSet();
        this.lock.readLock().lock();

        try {
            if (this.whiteList != null) {
                hashSet.addAll(this.whiteList.keySet());
            }
        } finally {
            this.lock.readLock().unlock();
        }

        return Collections.unmodifiableSet(hashSet);
    }

    public Set<String> getSqlList() {
        Set<String> hashSet = new HashSet();
        this.lock.readLock().lock();

        try {
            if (this.whiteList != null) {
                hashSet.addAll(this.whiteList.keySet());
            }

            if (this.blackMergedList != null) {
                hashSet.addAll(this.blackMergedList.keySet());
            }
        } finally {
            this.lock.readLock().unlock();
        }

        return Collections.unmodifiableSet(hashSet);
    }

    public Set<String> getBlackList() {
        Set<String> hashSet = new HashSet();
        this.lock.readLock().lock();

        try {
            if (this.blackList != null) {
                hashSet.addAll(this.blackList.keySet());
            }
        } finally {
            this.lock.readLock().unlock();
        }

        return Collections.unmodifiableSet(hashSet);
    }

    public void clearCache() {
        this.lock.writeLock().lock();

        try {
            if (this.whiteList != null) {
                this.whiteList = null;
            }

            if (this.blackList != null) {
                this.blackList = null;
            }

            if (this.blackMergedList != null) {
                this.blackMergedList = null;
            }
        } finally {
            this.lock.writeLock().unlock();
        }

    }

    public void clearWhiteList() {
        this.lock.writeLock().lock();

        try {
            if (this.whiteList != null) {
                this.whiteList = null;
            }
        } finally {
            this.lock.writeLock().unlock();
        }

    }

    public void clearBlackList() {
        this.lock.writeLock().lock();

        try {
            if (this.blackList != null) {
                this.blackList = null;
            }
        } finally {
            this.lock.writeLock().unlock();
        }

    }

    public WallSqlStat getWhiteSql(String sql) {
        String mergedSql;
        WallSqlStat stat = null;
        this.lock.readLock().lock();
        try {
             if (this.whiteList == null) {
                return null;
             }
             stat = (WallSqlStat)this.whiteList.get(sql);
        } finally {
             this.lock.readLock().unlock();
        }

        if (stat != null) {
            return stat;
        }

        try {
             mergedSql = ParameterizedOutputVisitorUtils.parameterize(sql, this.dbType);
        } catch (Exception ex) {

             return null;
        }

        this.lock.readLock().lock();
        try {
            stat = (WallSqlStat)this.whiteList.get(mergedSql);
        } finally {
            this.lock.readLock().unlock();
        }
        return stat;
    }

    public WallSqlStat getBlackSql(String sql) {
        this.lock.readLock().lock();

        WallSqlStat var2;
        try {
            if (this.blackList != null) {
                var2 = (WallSqlStat)this.blackList.get(sql);
                return var2;
            }

            var2 = null;
        } finally {
            this.lock.readLock().unlock();
        }

        return var2;
    }

    public boolean whiteContains(String sql) {
        return this.getWhiteSql(sql) != null;
    }

    public abstract SQLStatementParser createParser(String var1);

    public abstract WallVisitor createWallVisitor();

    public abstract ExportParameterVisitor createExportParameterVisitor();

    public boolean checkValid(String sql) {
        WallContext originalContext = WallContext.current();

        boolean var4;
        try {
            WallContext.create(this.dbType);
            WallCheckResult result = this.checkInternal(sql);
            var4 = result.getViolations().isEmpty();
        } finally {
            if (originalContext == null) {
                WallContext.clearContext();
            }

        }

        return var4;
    }

    public void incrementCommentDeniedCount() {
        this.commentDeniedStat.incrementAndGetDenyCount();
    }

    public boolean checkDenyFunction(String functionName) {
        if (functionName == null) {
            return true;
        } else {
            functionName = functionName.toLowerCase();
            return !this.getConfig().getDenyFunctions().contains(functionName);
        }
    }

    public boolean checkDenySchema(String schemaName) {
        if (schemaName == null) {
            return true;
        } else if (!this.config.isSchemaCheck()) {
            return true;
        } else {
            schemaName = schemaName.toLowerCase();
            return !this.getConfig().getDenySchemas().contains(schemaName);
        }
    }

    public boolean checkDenyTable(String tableName) {
        if (tableName == null) {
            return true;
        } else {
            tableName = WallVisitorUtils.form(tableName);
            return !this.getConfig().getDenyTables().contains(tableName);
        }
    }

    public boolean checkReadOnlyTable(String tableName) {
        if (tableName == null) {
            return true;
        } else {
            tableName = WallVisitorUtils.form(tableName);
            return !this.getConfig().isReadOnly(tableName);
        }
    }

    public WallDenyStat getCommentDenyStat() {
        return this.commentDeniedStat;
    }

    public WallCheckResult check(String sql) {
        WallContext originalContext = WallContext.current();

        WallCheckResult var3;
        try {
            WallContext.createIfNotExists(this.dbType);
            var3 = this.checkInternal(sql);
        } finally {
            if (originalContext == null) {
                WallContext.clearContext();
            }

        }

        return var3;
    }

    private WallCheckResult checkInternal(String sql) {
        this.checkCount.incrementAndGet();
        WallContext context = WallContext.current();
        if (this.config.isDoPrivilegedAllow() && ispPrivileged()) {
            WallCheckResult checkResult = new WallCheckResult();
            checkResult.setSql(sql);
            return checkResult;
        } else {
            boolean mulltiTenant = this.config.getTenantTablePattern() != null && this.config.getTenantTablePattern().length() > 0;
            if (!mulltiTenant) {
                WallCheckResult checkResult = this.checkWhiteAndBlackList(sql);
                if (checkResult != null) {
                    checkResult.setSql(sql);
                    return checkResult;
                }
            }

            this.hardCheckCount.incrementAndGet();
            List<Violation> violations = new ArrayList();
            List<SQLStatement> statementList = new ArrayList();
            boolean syntaxError = false;
            boolean endOfComment = false;

            try {
                SQLStatementParser parser = this.createParser(sql);
                parser.getLexer().setCommentHandler(WallProvider.WallCommentHandler.instance);
                if (!this.config.isCommentAllow()) {
                    parser.getLexer().setAllowComment(false);
                }

                if (!this.config.isCompleteInsertValuesCheck()) {
                    parser.setParseCompleteValues(false);
                    parser.setParseValuesSize(this.config.getInsertValuesCheckSize());
                }

                parser.parseStatementList(statementList);
                Token lastToken = parser.getLexer().token();
                if (lastToken != Token.EOF && this.config.isStrictSyntaxCheck()) {
                    violations.add(new IllegalSQLObjectViolation(1001, "not terminal sql, token " + lastToken, sql));
                }

                endOfComment = parser.getLexer().isEndOfComment();
            } catch (NotAllowCommentException var15) {
                violations.add(new IllegalSQLObjectViolation(1104, "comment not allow", sql));
                this.incrementCommentDeniedCount();
            } catch (ParserException var16) {
                this.syntaxErrorCount.incrementAndGet();
                syntaxError = true;
                if (this.config.isStrictSyntaxCheck()) {
                    violations.add(new SyntaxErrorViolation(var16, sql));
                }
            } catch (Exception var17) {
                if (this.config.isStrictSyntaxCheck()) {
                    violations.add(new SyntaxErrorViolation(var17, sql));
                }
            }

            if (statementList.size() > 1 && !this.config.isMultiStatementAllow()) {
                violations.add(new IllegalSQLObjectViolation(2201, "multi-statement not allow", sql));
            }

            WallVisitor visitor = this.createWallVisitor();
            visitor.setSqlEndOfComment(endOfComment);
            if (statementList.size() > 0) {
                boolean lastIsHint = false;

                for(int i = 0; i < statementList.size(); ++i) {
                    SQLStatement stmt = (SQLStatement)statementList.get(i);
                    if ((i == 0 || lastIsHint) && stmt instanceof MySqlHintStatement) {
                        lastIsHint = true;
                    } else {
                        try {
                            stmt.accept(visitor);
                        } catch (ParserException var14) {
                            violations.add(new SyntaxErrorViolation(var14, sql));
                        }
                    }
                }
            }

            if (visitor.getViolations().size() > 0) {
                violations.addAll(visitor.getViolations());
            }

            WallSqlStat sqlStat = null;
            if (violations.size() > 0) {
                this.violationCount.incrementAndGet();
                if (sql.length() < this.MAX_SQL_LENGTH) {
                    sqlStat = this.addBlackSql(sql, context.getTableStats(), context.getFunctionStats(), violations, syntaxError);
                }
            } else if (sql.length() < this.MAX_SQL_LENGTH) {
                sqlStat = this.addWhiteSql(sql, context.getTableStats(), context.getFunctionStats(), syntaxError);
            }

            Map<String, WallSqlTableStat> tableStats = null;
            Map<String, WallSqlFunctionStat> functionStats = null;
            if (context != null) {
                tableStats = context.getTableStats();
                functionStats = context.getFunctionStats();
                this.recordStats(tableStats, functionStats);
            }

            WallCheckResult result;
            if (sqlStat != null) {
                context.setSqlStat(sqlStat);
                result = new WallCheckResult(sqlStat, statementList);
            } else {
                result = new WallCheckResult((WallSqlStat)null, violations, tableStats, functionStats, statementList, syntaxError);
            }

            String resultSql;
            if (visitor.isSqlModified()) {
                resultSql = SQLUtils.toSQLString(statementList, this.dbType);
            } else {
                resultSql = sql;
            }

            result.setSql(resultSql);
            return result;
        }
    }

    private WallCheckResult checkWhiteAndBlackList(String sql) {
        WallSqlStat sqlStat;
        if (this.blackListEnable) {
            sqlStat = this.getBlackSql(sql);
            if (sqlStat != null) {
                this.blackListHitCount.incrementAndGet();
                this.violationCount.incrementAndGet();
                if (sqlStat.isSyntaxError()) {
                    this.syntaxErrorCount.incrementAndGet();
                }

                sqlStat.incrementAndGetExecuteCount();
                this.recordStats(sqlStat.getTableStats(), sqlStat.getFunctionStats());
                return new WallCheckResult(sqlStat);
            }
        }

        if (this.whiteListEnable) {
            sqlStat = this.getWhiteSql(sql);
            if (sqlStat != null) {
                this.whiteListHitCount.incrementAndGet();
                sqlStat.incrementAndGetExecuteCount();
                if (sqlStat.isSyntaxError()) {
                    this.syntaxErrorCount.incrementAndGet();
                }

                this.recordStats(sqlStat.getTableStats(), sqlStat.getFunctionStats());
                WallContext context = WallContext.current();
                if (context != null) {
                    context.setSqlStat(sqlStat);
                }

                return new WallCheckResult(sqlStat);
            }
        }

        return null;
    }

    void recordStats(Map<String, WallSqlTableStat> tableStats, Map<String, WallSqlFunctionStat> functionStats) {
        Iterator var3;
        Entry entry;
        String tableName;
        if (tableStats != null) {
            var3 = tableStats.entrySet().iterator();

            while(var3.hasNext()) {
                entry = (Entry)var3.next();
                tableName = (String)entry.getKey();
                WallSqlTableStat sqlTableStat = (WallSqlTableStat)entry.getValue();
                WallTableStat tableStat = this.getTableStat(tableName);
                if (tableStat != null) {
                    tableStat.addSqlTableStat(sqlTableStat);
                }
            }
        }

        if (functionStats != null) {
            var3 = functionStats.entrySet().iterator();

            while(var3.hasNext()) {
                entry = (Entry)var3.next();
                tableName = (String)entry.getKey();
                WallSqlFunctionStat sqlTableStat = (WallSqlFunctionStat)entry.getValue();
                WallFunctionStat functionStat = this.getFunctionStatWithLowerName(tableName);
                if (functionStat != null) {
                    functionStat.addSqlFunctionStat(sqlTableStat);
                }
            }
        }

    }

    public static boolean ispPrivileged() {
        Boolean value = (Boolean)privileged.get();
        return value == null ? false : value;
    }

    public static <T> T doPrivileged(PrivilegedAction<T> action) {
        Boolean original = (Boolean)privileged.get();
        privileged.set(Boolean.TRUE);

        try {
            return action.run();
        } finally {
            privileged.set(original);
        }

    }

    public static void setTenantValue(Object value) {
        tenantValueLocal.set(value);
    }

    public static Object getTenantValue() {
        return tenantValueLocal.get();
    }

    public long getWhiteListHitCount() {
        return this.whiteListHitCount.get();
    }

    public long getBlackListHitCount() {
        return this.blackListHitCount.get();
    }

    public long getSyntaxErrorCount() {
        return this.syntaxErrorCount.get();
    }

    public long getCheckCount() {
        return this.checkCount.get();
    }

    public long getViolationCount() {
        return this.violationCount.get();
    }

    public long getHardCheckCount() {
        return this.hardCheckCount.get();
    }

    public long getViolationEffectRowCount() {
        return this.violationEffectRowCount.get();
    }

    public void addViolationEffectRowCount(long rowCount) {
        this.violationEffectRowCount.addAndGet(rowCount);
    }

    public WallProviderStatValue getStatValue(boolean reset) {
        WallProviderStatValue statValue = new WallProviderStatValue();
        statValue.setName(this.name);
        statValue.setCheckCount(JdbcSqlStatUtils.get(this.checkCount, reset));
        statValue.setHardCheckCount(JdbcSqlStatUtils.get(this.hardCheckCount, reset));
        statValue.setViolationCount(JdbcSqlStatUtils.get(this.violationCount, reset));
        statValue.setViolationEffectRowCount(JdbcSqlStatUtils.get(this.violationEffectRowCount, reset));
        statValue.setBlackListHitCount(JdbcSqlStatUtils.get(this.blackListHitCount, reset));
        statValue.setWhiteListHitCount(JdbcSqlStatUtils.get(this.whiteListHitCount, reset));
        statValue.setSyntaxErrorCount(JdbcSqlStatUtils.get(this.syntaxErrorCount, reset));
        Iterator var3 = this.tableStats.entrySet().iterator();

        Entry entry;
        String functionName;
        while(var3.hasNext()) {
            entry = (Entry)var3.next();
            functionName = (String)entry.getKey();
            WallTableStat tableStat = (WallTableStat)entry.getValue();
            WallTableStatValue tableStatValue = tableStat.getStatValue(reset);
            if (tableStatValue.getTotalExecuteCount() != 0L) {
                tableStatValue.setName(functionName);
                statValue.getTables().add(tableStatValue);
            }
        }

        var3 = this.functionStats.entrySet().iterator();

        while(var3.hasNext()) {
            entry = (Entry)var3.next();
            functionName = (String)entry.getKey();
            WallFunctionStat functionStat = (WallFunctionStat)entry.getValue();
            WallFunctionStatValue functionStatValue = functionStat.getStatValue(reset);
            if (functionStatValue.getInvokeCount() != 0L) {
                functionStatValue.setName(functionName);
                statValue.getFunctions().add(functionStatValue);
            }
        }

        Lock lock = reset ? this.lock.writeLock() : this.lock.readLock();
        ((Lock)lock).lock();

        try {
            WallSqlStatValue sqlStatValue;
            Iterator var15;
            String sql;
            WallSqlStat sqlStat;
            if (this.whiteList != null) {
                var15 = this.whiteList.entrySet().iterator();

                while(var15.hasNext()) {
                    entry = (Entry)var15.next();
                    sql = (String)entry.getKey();
                    sqlStat = (WallSqlStat)entry.getValue();
                    sqlStatValue = sqlStat.getStatValue(reset);
                    if (sqlStatValue.getExecuteCount() != 0L) {
                        sqlStatValue.setSql(sql);
                        long sqlHash = sqlStat.getSqlHash();
                        if (sqlHash == 0L) {
                            sqlHash = Utils.murmurhash2_64(sql);
                            sqlStat.setSqlHash(sqlHash);
                        }

                        sqlStatValue.setSqlHash(sqlHash);
                        statValue.getWhiteList().add(sqlStatValue);
                    }
                }
            }

            if (this.blackMergedList != null) {
                var15 = this.blackMergedList.entrySet().iterator();

                while(var15.hasNext()) {
                    entry = (Entry)var15.next();
                    sql = (String)entry.getKey();
                    sqlStat = (WallSqlStat)entry.getValue();
                    sqlStatValue = sqlStat.getStatValue(reset);
                    if (sqlStatValue.getExecuteCount() != 0L) {
                        sqlStatValue.setSql(sql);
                        statValue.getBlackList().add(sqlStatValue);
                    }
                }
            }
        } finally {
            ((Lock)lock).unlock();
        }

        return statValue;
    }

    public Map<String, Object> getStatsMap() {
        return this.getStatValue(false).toMap();
    }

    public boolean isWhiteListEnable() {
        return this.whiteListEnable;
    }

    public void setWhiteListEnable(boolean whiteListEnable) {
        this.whiteListEnable = whiteListEnable;
    }

    public boolean isBlackListEnable() {
        return this.blackListEnable;
    }

    public void setBlackListEnable(boolean blackListEnable) {
        this.blackListEnable = blackListEnable;
    }

    public static class WallCommentHandler implements CommentHandler {
        public static final WallProvider.WallCommentHandler instance = new WallProvider.WallCommentHandler();

        public WallCommentHandler() {
        }

        public boolean handle(Token lastToken, String comment) {
            if (lastToken == null) {
                return false;
            } else {
                switch(lastToken) {
                    case SELECT:
                    case INSERT:
                    case DELETE:
                    case UPDATE:
                    case TRUNCATE:
                    case SET:
                    case CREATE:
                    case ALTER:
                    case DROP:
                    case SHOW:
                    case REPLACE:
                        return true;
                    default:
                        WallContext context = WallContext.current();
                        if (context != null) {
                            context.incrementCommentCount();
                        }

                        return false;
                }
            }
        }
    }
}

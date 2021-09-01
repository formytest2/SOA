package com.tranboot.client.utils;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Meter;
import com.codahale.metrics.Metric;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.codahale.metrics.jmx.JmxReporter;
import com.tranboot.client.core.txc.TxcDataSourceTransactionManagerInterceptor;
import com.tranboot.client.core.txc.TxcDubboFilter;
import com.tranboot.client.core.txc.TxcMethodInterceptor;
import com.tranboot.client.model.txc.ManualRollbackTxcSQL;
import com.tranboot.client.model.txc.SqlParserTxcSQL;
import com.tranboot.client.model.txc.TxcSqlParserProcessor;
import com.tranboot.client.service.txc.TxcRedisService;
import com.tranboot.client.service.txc.impl.TxcManualRollbackSQLServiceMysqlImpl;

public class MetricsReporter {
    static final MetricRegistry metrics = new MetricRegistry();
    static final JmxReporter reporter;
    public static final Counter txcTimeoutCounter;
    public static final Meter throughput;
    public static final Counter redLockFailCount;
    public static final Timer redLockTimer;
    public static final Timer redUnlockTimer;
    public static final Timer hput;
    public static final Timer time;
    public static final Timer commitTimer;
    public static final Timer rollbackTimer;
    public static final Timer beginTimer;
    public static final Timer sendCommitMessage;
    public static final Timer sendRollbackMessage;
    public static final Timer txcSqlCacheTimer;
    public static final Timer dbsyncCacheTimer;
    public static final Timer schemaCacheTimer;
    public static final Timer txcSQLTransformTimer;
    public static final Timer parserProcessorTimer;
    public static final Timer rollbackSqlTimer;
    public static final Timer query;
    public static final Timer render;
    public static final Timer deal;
    public static final Timer manualRollbackSqlTimer;
    public static final Timer insertTxcLog;
    public static final Timer queryManualRollbackSql;
    public static final Timer invokeTimer;

    public MetricsReporter() {
    }

    public static Timer timer(String name) {
        return metrics.timer(name);
    }

    public static <T extends Metric> T register(String name, T metric) {
        return metrics.register(name, metric);
    }

    static {
        reporter = JmxReporter.forRegistry(metrics).build();
        txcTimeoutCounter = (Counter)register("txcTimeoutCounter", new Counter());
        throughput = (Meter)register("qps", new Meter());
        redLockFailCount = (Counter)register("redLockFailCount", new Counter());
        redLockTimer = timer("redlock");
        redUnlockTimer = timer("redUnlock");
        hput = timer(MetricRegistry.name(TxcRedisService.class, new String[]{"hput"}));
        time = timer(MetricRegistry.name(TxcRedisService.class, new String[]{"redis_time"}));
        commitTimer = timer(MetricRegistry.name(TxcDataSourceTransactionManagerInterceptor.class, new String[]{"doCommit"}));
        rollbackTimer = timer(MetricRegistry.name(TxcDataSourceTransactionManagerInterceptor.class, new String[]{"doRollback"}));
        beginTimer = timer(MetricRegistry.name(TxcDataSourceTransactionManagerInterceptor.class, new String[]{"doBegin"}));
        sendCommitMessage = timer(MetricRegistry.name(TxcMethodInterceptor.class, new String[]{"sendCommitMessage"}));
        sendRollbackMessage = timer(MetricRegistry.name(TxcMethodInterceptor.class, new String[]{"sendRollbackMessage"}));
        txcSqlCacheTimer = timer(MetricRegistry.name(LRUCache.class, new String[]{"getTxcSql"}));
        dbsyncCacheTimer = timer(MetricRegistry.name(LRUCache.class, new String[]{"getDbsyncSqlTransformResult"}));
        schemaCacheTimer = timer(MetricRegistry.name(LRUCache.class, new String[]{"getTableSchema"}));
        txcSQLTransformTimer = timer(MetricRegistry.name(LRUCache.class, new String[]{"getTxcSqlTransform"}));
        parserProcessorTimer = timer(MetricRegistry.name(TxcSqlParserProcessor.class, new String[]{"parse"}));
        rollbackSqlTimer = timer(MetricRegistry.name(SqlParserTxcSQL.class, new String[]{"rollbackSql"}));
        query = timer(MetricRegistry.name(SqlParserTxcSQL.class, new String[]{"query"}));
        render = timer(MetricRegistry.name(SqlParserTxcSQL.class, new String[]{"render"}));
        deal = timer(MetricRegistry.name(SqlParserTxcSQL.class, new String[]{"deal"}));
        manualRollbackSqlTimer = timer(MetricRegistry.name(ManualRollbackTxcSQL.class, new String[]{"rollbackSql"}));
        insertTxcLog = timer(MetricRegistry.name(TxcMethodInterceptor.class, new String[]{"insertTransactionLog"}));
        queryManualRollbackSql = timer(MetricRegistry.name(TxcManualRollbackSQLServiceMysqlImpl.class, new String[]{"queryManualRollbackSql"}));
        invokeTimer = timer(MetricRegistry.name(TxcDubboFilter.class, new String[]{"invoke"}));
        reporter.start();
    }
}

package com.tranboot.client.utils;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer.Context;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.tranboot.client.exception.TxcTransactionException;
import com.tranboot.client.model.TableSchema;
import com.tranboot.client.model.dbsync.SqlTransformResult;
import com.tranboot.client.model.txc.TxcSQL;
import com.tranboot.client.model.txc.TxcSQLTransformer;
import com.tranboot.client.model.txc.TxcSQLTransformer.TxcSQLTransform;
import com.tranboot.client.service.TableSchemaCacheLoader;
import java.util.concurrent.Callable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LRUCache {
    private static final Logger logger = LoggerFactory.getLogger(LRUCache.class);
    private static final Logger monitor = LoggerFactory.getLogger("LRUmonitor");
    private static final Cache<String, TxcSQL> txcSqlCache = CacheBuilder.newBuilder().concurrencyLevel(10).initialCapacity(100).maximumSize(200L).removalListener(new RemovalListener<String, TxcSQL>() {
        public void onRemoval(RemovalNotification<String, TxcSQL> notification) {
            LRUCache.logger.info((String)notification.getKey() + " txc 缓存过期，将被移除");
        }
    }).recordStats().build();                                                               // sql -> TxcSQL
    private static final Cache<String, TableSchema> tableSchemaCache;                       // tableName -> TableSchema
    private static final Cache<String, SqlTransformResult> dbsyncSqlTransformCache;         // sql -> SqlTransformResult
    private static final Cache<String, TxcSQLTransform> txcSQLTransformCache;               // sql -> TxcSQLTransform

    public LRUCache() {
    }

    public static final void cacheDbsyncSqlTransformResult(String sql, SqlTransformResult result) {
        dbsyncSqlTransformCache.put(sql, result);
    }

    public static final TxcSQL getTxcSql(String sql, Callable<TxcSQL> loader) {
        Context context = MetricsReporter.txcSqlCacheTimer.time();

        TxcSQL ts;
        try {
            TxcSQL txcSql = (TxcSQL)txcSqlCache.get(sql, loader);
            monitor.info(System.lineSeparator() + "原语句:" + sql + System.lineSeparator() + txcSql.toString());
            ts = txcSql;
        } catch (Exception e) {
            throw new TxcTransactionException(e, "获取TxcSQL失败");
        } finally {
            context.stop();
        }

        return ts;
    }

    public static final SqlTransformResult getDbsyncSqlTransformResult(String sql) {
        Context context = MetricsReporter.dbsyncCacheTimer.time();

        SqlTransformResult sqlTransformResult;
        try {
            sqlTransformResult = (SqlTransformResult)dbsyncSqlTransformCache.getIfPresent(sql);
        } finally {
            context.stop();
        }

        return sqlTransformResult;
    }

    public static final TxcSQLTransform getTxcTransformedSql(final String sql) {
        Context context = MetricsReporter.txcSQLTransformTimer.time();

        TxcSQLTransform txcSQLTransform;
        try {
            txcSQLTransform = (TxcSQLTransform)txcSQLTransformCache.get(sql, new Callable<TxcSQLTransform>() {
                public TxcSQLTransform call() throws Exception {
                    TxcSQLTransformer transformer = new TxcSQLTransformer();
                    TxcSQLTransform transform = transformer.transform(sql);
                    return transform;
                }
            });
        } catch (Exception e) {
            throw new TxcTransactionException(e, "获取TxcTransformedSql失败");
        } finally {
            context.stop();
        }

        return txcSQLTransform;
    }

    public static final boolean tableSchemaCacheContain(String tableName) {
        return tableSchemaCache.getIfPresent(tableName) != null;
    }

    public static final TableSchema getTableSchema(String tableName, TableSchemaCacheLoader loader) {
        Context context = MetricsReporter.schemaCacheTimer.time();

        TableSchema tableSchema;
        try {
            TableSchema schema = (TableSchema)tableSchemaCache.get(tableName, loader);
            logger.info("TableSchema:{}", schema);
            monitor.info(schema.toString());
            tableSchema = schema;
        } catch (Exception e) {
            throw new TxcTransactionException(e, "获取TableSchema失败");
        } finally {
            context.stop();
        }

        return tableSchema;
    }

    static {
        MetricsReporter.register(MetricRegistry.name(LRUCache.class, new String[]{"txcSqlCache"}), new LRUCache.CacheStatusMetric(txcSqlCache));
        tableSchemaCache = CacheBuilder.newBuilder().concurrencyLevel(10).initialCapacity(50).maximumSize(100L).recordStats().removalListener(new RemovalListener<String, TableSchema>() {
            public void onRemoval(RemovalNotification<String, TableSchema> notification) {
                LRUCache.logger.info((String)notification.getKey() + " schema 缓存过期,将被移除");
            }
        }).build();
        MetricsReporter.register(MetricRegistry.name(LRUCache.class, new String[]{"tableSchemaCache"}), new LRUCache.CacheStatusMetric(tableSchemaCache));
        dbsyncSqlTransformCache = CacheBuilder.newBuilder().concurrencyLevel(10).initialCapacity(100).maximumSize(200L).recordStats().removalListener(new RemovalListener<String, SqlTransformResult>() {
            public void onRemoval(RemovalNotification<String, SqlTransformResult> notification) {
                LRUCache.logger.info((String)notification.getKey() + " dbsync sqltransformresult 缓存过期,将被移除");
            }
        }).build();
        MetricsReporter.register(MetricRegistry.name(LRUCache.class, new String[]{"dbsyncSqlTransformCache"}), new LRUCache.CacheStatusMetric(dbsyncSqlTransformCache));
        txcSQLTransformCache = CacheBuilder.newBuilder().concurrencyLevel(10).initialCapacity(100).maximumSize(200L).recordStats().removalListener(new RemovalListener<String, TxcSQLTransform>() {
            public void onRemoval(RemovalNotification<String, TxcSQLTransform> notification) {
                LRUCache.logger.info((String)notification.getKey() + " txcsqltransform 缓存过期,将被移除");
            }
        }).build();
        MetricsReporter.register(MetricRegistry.name(LRUCache.class, new String[]{"txcSQLTransformCache"}), new LRUCache.CacheStatusMetric(txcSQLTransformCache));
    }

    static class CacheStatusMetric implements Gauge<String> {
        Cache cache;

        public CacheStatusMetric(Cache cache) {
            this.cache = cache;
        }

        public String getValue() {
            return this.cache.stats().toString();
        }
    }
}


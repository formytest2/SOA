package com.tranboot.client.core.txc;

import com.alibaba.fastjson.JSON;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.codahale.metrics.Timer.Context;
import com.github.bluecatlee.gs4d.transaction.api.model.SqlParamModel;
import com.tranboot.client.exception.TxcDBException;
import com.tranboot.client.exception.TxcTransactionTimeoutException;
import com.tranboot.client.model.DBType;
import com.tranboot.client.model.txc.BranchRollbackMessage;
import com.tranboot.client.model.txc.TxcSQL;
import com.tranboot.client.model.txc.TxcSQL.RollbackSqlInfo;
import com.tranboot.client.service.txc.TxcRedisService;
import com.tranboot.client.spring.ContextUtils;
import com.tranboot.client.utils.BranchIdGenerator;
import com.tranboot.client.utils.MetricsReporter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.sql.DataSource;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 抽象JdbcTemplate拦截器
 */
public abstract class AbstractJdbcTemplateInterceptor implements MethodInterceptor {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    protected final Timer txcTimer = MetricsReporter.timer(MetricRegistry.name(this.getClass(), new String[]{"txc"}));
    protected final Timer processTimer = MetricsReporter.timer(MetricRegistry.name(this.getClass(), new String[]{"process"}));
    protected final Timer invokeTimer = MetricsReporter.timer(MetricRegistry.name(this.getClass(), new String[]{"invoke"}));
    protected final String datasource;
    protected final DataSource ds;
    protected final TxcRedisService txcRedisService;
    protected final DBType dbType;

    public AbstractJdbcTemplateInterceptor(String datasource, DataSource ds, TxcRedisService txcRedisService, DBType dbType) {
        this.datasource = datasource;
        this.ds = ds;
        this.txcRedisService = txcRedisService;
        this.dbType = dbType;
    }

    public Object invoke(MethodInvocation invocation) throws Throwable {
        Context invokeContext = this.invokeTimer.time();

        Object result;
        try {
            Object ret;
            if (!TxcContext.inTxcTransaction()) {
                ret = invocation.proceed();
                MetricsReporter.throughput.mark();
                return ret;
            }

            if (!TxcRollbackSqlManagerV2.inTransaction(this.ds)) {
                BranchRollbackMessage message = new BranchRollbackMessage();
                message.setXid(TxcContext.getCurrentXid());
                long branchId = BranchIdGenerator.branchId();
                message.setDataSource(this.datasource);
                message.setBranchId(branchId);
                message.setStatus(TxcRedisService.INIT_STATUS);
                message.setServerIp(ContextUtils.getServerIp());
                message.setTransactionStartDate(new Date(TxcContext.getTxcStart()));
                message.setTransactionOutTimeSecond((long)TxcContext.getTxcTimeout() * 1000L);
                Context context = this.txcTimer.time();
                ArrayList locks = new ArrayList();
                boolean locked = false;

                Object res;
                try {
                    locked = true;
                    this.logger.debug(this.datasource + "不存在本地事务");
                    long redisTime = this.txcRedisService.getCurrentTimeMillisFromRedis();
                    if (TxcContext.getTxcStart() + (long)(TxcContext.getTxcTimeout() * 1000) < redisTime) {
                        throw new TxcTransactionTimeoutException(String.format("分布式事务%d超时: 系统时间:%d 事务开始时间:%d 超时设置:%d", TxcContext.getCurrentXid(), redisTime, TxcContext.getTxcStart(), TxcContext.getTxcTimeout() * 1000));
                    }

                    ret = this.txc(invocation, message, 0, locks);
                    MetricsReporter.throughput.mark();
                    message.setStatus(TxcRedisService.COMMITTABLE_STATUS);
                    this.txcRedisService.hput(TxcRedisService.buildTransactionModel(message));
                    res = ret;
                    locked = false;
                } catch (TxcDBException e) {
                    message.setStatus(TxcRedisService.ROLLBACKABLE_STATUS);
                    this.txcRedisService.hput(TxcRedisService.buildTransactionModel(message));
                    throw e;
                } finally {
                    if (locked) {
                        Iterator locksIterator = locks.iterator();

                        while(locksIterator.hasNext()) {
                            ReentrantRedLock lock = (ReentrantRedLock)locksIterator.next();
                            lock.unlock();
                        }

                        context.stop();
                    }
                }

                Iterator locksIterator = locks.iterator();

                while(locksIterator.hasNext()) {
                    ReentrantRedLock lock = (ReentrantRedLock)locksIterator.next();
                    lock.unlock();
                }

                context.stop();
                return res;
            }

            Context context = this.txcTimer.time();

            try {
                this.logger.debug(this.datasource + "存在本地事务");
                ret = this.txc(invocation, TxcRollbackSqlManagerV2.get(this.ds), 1, (List)null);
                MetricsReporter.throughput.mark();
                result = ret;
            } catch (Throwable throwable) {
                throw throwable;
            } finally {
                context.stop();
            }
        } finally {
            invokeContext.stop();
        }

        return result;
    }

    public abstract Object txc(MethodInvocation invocation, BranchRollbackMessage message, int transactionType, List<ReentrantRedLock> locks) throws TxcDBException;

    protected static Object[] addArgs(Object[] args, Object... additionArgs) {
        return ArrayUtils.addAll(args, additionArgs);
    }

    protected ReentrantRedLock lock(String key, int txcTimeout, List<ReentrantRedLock> locks) {
        if (TxcRollbackSqlManagerV2.inTransaction(this.ds)) {
            return TxcRollbackSqlManagerV2.redlockInTransaction(this.ds, key, txcTimeout);
        } else {
            ReentrantRedLock lock = TxcRollbackSqlManagerV2.redlock(key, txcTimeout);
            locks.add(lock);
            return lock;
        }
    }

    protected SqlParamModel redisSqlModel(RollbackSqlInfo rollbacksql, TxcSQL txcSQL, int transactionType, String txc, String lockKey, String lockValue) {
        SqlParamModel model = new SqlParamModel();
        model.setTxc(txc);
        model.setSql(rollbacksql.rollbackSql);
        model.setTable(txcSQL.getTableName());
        model.setTransactionType(transactionType);
        model.setSqlType(txcSQL.getSqlType().getCode());
        model.setInsertRedisTime(System.currentTimeMillis());
        model.setTableKeyValueModels(JSON.toJSONString(rollbacksql.primaryKVPair));
        model.setRedisLockKey(lockKey);
        model.setRedisLockValue(lockValue);
        model.setShard(rollbacksql.shardValue);
        return model;
    }
}


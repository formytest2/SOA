package com.tranboot.client.core.txc;

import com.codahale.metrics.Timer.Context;
import com.tranboot.client.exception.TxcTransactionStatusError;
import com.tranboot.client.model.txc.BranchRollbackInfo;
import com.tranboot.client.model.txc.BranchRollbackMessage;
import com.tranboot.client.spring.ContextUtils;
import com.tranboot.client.utils.MetricsReporter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

public final class TxcRollbackSqlManagerV2 {
    private static final Logger logger = LoggerFactory.getLogger(TxcRollbackSqlManagerV2.class);
    private static final Logger log = LoggerFactory.getLogger("txcRollbackSqlManager");
    protected static final ThreadLocal<Map<Object, BranchRollbackMessage>> _txcResource = new ThreadLocal();
    protected static final ThreadLocal<Map<Object, List<ReentrantRedLock>>> _redLocks = new ThreadLocal();
    protected static final int TXC_LOCK_WIAT_TIMEOUT = 60000;

    public TxcRollbackSqlManagerV2() {
    }

    protected static final String txc(BranchRollbackMessage message) {
        String txc = String.format("%s-%s", String.valueOf(message.getXid()), String.valueOf(message.getBranchId()));
        return txc;
    }

    protected static BranchRollbackMessage beginTransaction(DataSource ds, long branchId, long xid, String serverIp) {
        if (_txcResource.get() == null) {
            _txcResource.set(new HashMap());
        }

        if (_redLocks.get() == null) {
            _redLocks.set(new HashMap());
        }

        if (((Map)_txcResource.get()).containsKey(ds) || ((Map)_redLocks.get()).containsKey(ds)) {
            logger.error("分布式事务状态错误 Expected Status is NULL,But Not NULL");
            releaseResource(ds);
        }

        BranchRollbackMessage message = new BranchRollbackMessage();
        message.setBranchId(branchId);
        message.setXid(xid);
        message.setServerIp(serverIp);
        message.setStatus(0);
        message.setTransactionStartDate(new Date(TxcContext.getTxcStart()));
        message.setTransactionOutTimeSecond((long)TxcContext.getTxcTimeout() * 1000L);
        ((Map)_txcResource.get()).put(ds, message);
        ((Map)_redLocks.get()).put(ds, new ArrayList());
        return message;
    }

    protected static boolean inTransaction(DataSource ds) {
        if (_txcResource.get() == null) {
            return false;
        } else {
            return ((Map)_txcResource.get()).containsKey(ds);
        }
    }

    protected static BranchRollbackMessage get(DataSource ds) {
        if (_txcResource.get() != null && ((Map)_txcResource.get()).containsKey(ds)) {
            return (BranchRollbackMessage)((Map)_txcResource.get()).get(ds);
        } else {
            logger.error("分布式事务状态错误");
            throw new TxcTransactionStatusError("分布式事务状态错误 Expected txcResource is NOT NULL,But NULL");
        }
    }

    protected static ReentrantRedLock redlockInTransaction(DataSource ds, String key, int txcTimeout) {
        ReentrantRedLock reentrantRedLock = ReentrantRedLock.redisLock((StringRedisTemplate)ContextUtils.getBean(StringRedisTemplate.class), key, txcTimeout);
        Context context = MetricsReporter.redLockTimer.time();

        label62: {
            ReentrantRedLock var5;
            try {
                if (!reentrantRedLock.tryLock((long)(txcTimeout * 1000), TimeUnit.MILLISECONDS)) {
                    break label62;
                }

                if (_redLocks.get() == null || !((Map)_redLocks.get()).containsKey(ds)) {
                    context.stop();
                    reentrantRedLock.unlock();
                    logger.error("分布式事务状态错误");
                    throw new TxcTransactionStatusError("分布式事务状态错误 Expected RedLock is NOT NULL,But NULL");
                }

                if (!((List)((Map)_redLocks.get()).get(ds)).contains(reentrantRedLock)) {
                    ((List)((Map)_redLocks.get()).get(ds)).add(reentrantRedLock);
                }

                var5 = reentrantRedLock;
            } finally {
                context.stop();
            }

            return var5;
        }

        MetricsReporter.redLockFailCount.inc();
        throw new TxcTransactionStatusError(String.format("分布式事务锁【%s】获取超时!", key));
    }

    protected static ReentrantRedLock redlock(String key, int txcTimeout) {
        ReentrantRedLock reentrantRedLock = ReentrantRedLock.redisLock((StringRedisTemplate)ContextUtils.getBean(StringRedisTemplate.class), key, txcTimeout);
        Context context = MetricsReporter.redLockTimer.time();

        try {
            if (reentrantRedLock.tryLock((long)(txcTimeout * 1000), TimeUnit.MILLISECONDS)) {
                ReentrantRedLock var4 = reentrantRedLock;
                return var4;
            }
        } finally {
            context.stop();
        }

        MetricsReporter.redLockFailCount.inc();
        throw new TxcTransactionStatusError(String.format("分布式事务锁【%s】获取超时!", key));
    }

    protected static void releaseResource(DataSource ds) {
        log.debug("开始释放分布式事务上下文...");
        if (_txcResource.get() != null) {
            ((Map)_txcResource.get()).remove(ds);
        }

        if (_redLocks.get() != null) {
            List<ReentrantRedLock> locks = (List)((Map)_redLocks.get()).get(ds);
            locks.forEach(new Consumer<ReentrantRedLock>() {
                public void accept(ReentrantRedLock t) {
                    Context context = MetricsReporter.redUnlockTimer.time();

                    try {
                        t.unlock();
                    } finally {
                        context.stop();
                    }

                }
            });
            ((Map)_redLocks.get()).remove(ds);
        }

        log.debug("释放分布式事务上下文完成");
    }

    protected static void addBranchRollbackInfo(DataSource ds, String datasource, List<BranchRollbackInfo> info) {
        if (_txcResource.get() != null && ((Map)_txcResource.get()).containsKey(ds)) {
            BranchRollbackMessage message = (BranchRollbackMessage)((Map)_txcResource.get()).get(ds);
            message.addBranchRollbackInfo(datasource, info);
        } else {
            logger.error("分布式事务状态错误");
            throw new TxcTransactionStatusError("分布式事务状态错误 Expected Status is NOT NULL,But NULL");
        }
    }

    protected static void addBranchRollbackInfo(DataSource ds, String datasource, BranchRollbackInfo info) {
        if (_txcResource.get() != null && ((Map)_txcResource.get()).containsKey(ds)) {
            BranchRollbackMessage message = (BranchRollbackMessage)((Map)_txcResource.get()).get(ds);
            message.addBranchRollbackInfo(datasource, info);
        } else {
            logger.error("分布式事务状态错误");
            throw new TxcTransactionStatusError("分布式事务状态错误 Expected Status is NOT NULL,But NULL");
        }
    }
}

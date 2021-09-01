package com.tranboot.client.core.txc;

import com.codahale.metrics.Timer.Context;
import com.github.bluecatlee.gs4d.transaction.api.request.TransactionInitRequest;
import com.github.bluecatlee.gs4d.transaction.api.response.TransactionInitResponse;
import com.github.bluecatlee.gs4d.transaction.api.service.TransactionInitService;
import com.tranboot.client.exception.TxcMQException;
import com.tranboot.client.exception.TxcTransactionException;
import com.tranboot.client.service.txc.TxcMqService;
import com.tranboot.client.spring.ContextUtils;
import com.tranboot.client.utils.MetricsReporter;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import javax.sql.DataSource;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TxcMethodInterceptor implements MethodInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(TxcMethodInterceptor.class);
    protected static final TxcTransaction defaultTxcTransaction = new DefaultTxcTransaction();
    protected static final AtomicReference<String> insertSql = new AtomicReference();
    protected static final String dbtimeSql = "select SYSDATE() as dbtime";
    Map<Object, TxcTransaction> _context = new HashMap();

    public TxcMethodInterceptor(List<TxcMethodContext> contexts) {
        Iterator iterator = contexts.iterator();

        while(iterator.hasNext()) {
            TxcMethodContext local = (TxcMethodContext)iterator.next();
            this._context.put(methodToStr(local.getLocalMethod()), local.getTxcTransaction());
        }

    }

    private static String methodToStr(Method method) {
        StringBuilder localStringBuilder = new StringBuilder();
        String str = method.getName();
        Class<?>[] arrayOfClass1 = method.getParameterTypes();
        localStringBuilder.append(str);
        localStringBuilder.append("(");
        int i = 0;
        Class[] parameterClasses = arrayOfClass1;
        int parameterLen = arrayOfClass1.length;

        for(int j = 0; j < parameterLen; ++j) {
            Class<?> localClass = parameterClasses[j];
            localStringBuilder.append(localClass.getName());
            ++i;
            if (i < arrayOfClass1.length) {
                localStringBuilder.append(",");
            }
        }

        localStringBuilder.append(")");
        return localStringBuilder.toString();
    }

    public TxcTransaction findTxcTransaction(MethodInvocation invocation) {
        TxcTransaction localTxcTransaction = (TxcTransaction)this._context.get(invocation.getMethod());
        if (localTxcTransaction == null) {
            synchronized(this) {
                localTxcTransaction = (TxcTransaction)this._context.get(invocation.getMethod());
                if (localTxcTransaction == null) {
                    String str = methodToStr(invocation.getMethod());
                    localTxcTransaction = (TxcTransaction)this._context.get(str);
                    if (localTxcTransaction == null) {
                        localTxcTransaction = defaultTxcTransaction;
                    }

                    Map<Object, TxcTransaction> localHashMap = new HashMap();
                    localHashMap.putAll(this._context);
                    localHashMap.remove(str);
                    localHashMap.put(invocation.getMethod(), localTxcTransaction);
                    this._context = localHashMap;
                }
            }
        }

        return localTxcTransaction;
    }

    /**
     * 新增分布式事务
     *      即插入数据到transaction_log表 并返回生成的分布式事务id及事务开始时间等
     * @param ip_address
     * @param systemName
     * @param methodName
     * @param rollbackMode
     * @return
     */
    protected static TransactionInitResponse insertTransactionLogViaDubbo(String ip_address, String systemName, String methodName, int rollbackMode) {
        TransactionInitRequest request = new TransactionInitRequest();
        request.setFromSystem(systemName);
        request.setIpAddress(ip_address);
        request.setMethodName(methodName);
        request.setTransactionRollbackFlag(Long.parseLong(rollbackMode + ""));
        TransactionInitService dubboService = (TransactionInitService)ContextUtils.getBean(TransactionInitService.class);
        TransactionInitResponse rep = dubboService.initTransaction(request);
        if (rep.getCode() == 0L && rep.getTransactionId() != null && rep.getTransactionStartTime() != null) {
            logger.debug(String.format("插入分布式事务数据库记录完成【%s】", rep.getTransactionId() + ""));
            return rep;
        } else {
            throw new TxcTransactionException("插入分布式事务数据库记录失败! " + rep.getMessage());
        }
    }

    /** @deprecated */
    @Deprecated
    protected static long insertTransactionLog(long xid, String ip_address, String systemName, String methodName, int rollbackMode) {
        Connection con = null;
        Context context = MetricsReporter.insertTxcLog.time();

        long currentTimeMillis;
        try {
            DataSource platformDs = (DataSource)ContextUtils.getBean("platformDataSource", DataSource.class);
            con = platformDs.getConnection();
            PreparedStatement ps = con.prepareStatement(dbtimeSql);
            ResultSet rs = ps.executeQuery();
            rs.next();
            Timestamp dbtime = rs.getTimestamp("dbtime");
            ps.close();
            if (insertSql.get() == null) {
                StringBuilder tmp = new StringBuilder();
                tmp.append("insert into transaction_log");
                tmp.append("(");
                tmp.append("transaction_id,");
                tmp.append("start_dtme,");
                tmp.append("ip_address,");
                tmp.append("transaction_state,");
                tmp.append("from_system,");
                tmp.append("method_name,");
                tmp.append("transaction_rollback_flag,");
                tmp.append("transaction_sign");
                tmp.append(")");
                tmp.append("values (");
                tmp.append("?,").append("?,").append("?,").append("?,").append("?,").append("?,").append("?,").append("?");
                tmp.append(")");
                insertSql.compareAndSet(null, tmp.toString());
            }

            ps = con.prepareStatement((String)insertSql.get());
            ps.setLong(1, xid);
            ps.setTimestamp(2, dbtime);
            ps.setString(3, ip_address);
            ps.setInt(4, 0);
            ps.setString(5, systemName);
            ps.setString(6, methodName);
            ps.setInt(7, rollbackMode);
            ps.setString(8, "N");
            int row = ps.executeUpdate();
            if (row != 1) {
                logger.error("txc事务表插入失败");
                throw new TxcTransactionException("txc事务表插入失败");
            }

            logger.debug("txc事务表插入完成,xid={}", xid);
            currentTimeMillis = dbtime.getTime();
        } catch (Exception e) {
            logger.error("txc事务表插入失败", e);
            throw new TxcTransactionException(e, "txc事务表插入失败");
        } finally {
            try {
                if (con != null) {
                    con.close();
                }

                context.stop();
            } catch (SQLException e) {
                logger.error("手动释放连接失败", e);
                context.stop();
                throw new TxcTransactionException(e, "释放连接失败");
            }
        }

        return currentTimeMillis;
    }

    /**
     * 发送txc提交消息
     * @param xid
     * @throws TxcMQException
     */
    protected static void sendCommitMessage(long xid) throws TxcMQException {
        Context context = MetricsReporter.sendCommitMessage.time();
        TxcMqService mqService = (TxcMqService)ContextUtils.getBean(TxcMqService.class);

        try {
            mqService.sendCommit(xid, new Date());
        } catch (Exception e) {
            logger.error("发送txc提交消息失败。", e);
            throw new TxcMQException(e, "发送txc提交消息失败。【注意:后台默认会执行定时回滚任务，数据会被回滚掉，请捕获该异常，通知上游业务失败】");
        } finally {
            context.stop();
        }

    }

    /**
     * 发送txc回滚消息
     * @param xid
     */
    protected static void sendRollbackMessage(long xid) {
        Context context = MetricsReporter.sendRollbackMessage.time();
        TxcMqService mqService = (TxcMqService)ContextUtils.getBean(TxcMqService.class);

        try {
            mqService.sendRollback(xid, new Date());
        } catch (Exception e) {
            logger.error("发送txc回滚消息失败。【注意:后台默认会执行定时回滚任务，数据会被回滚掉】", e);
        } finally {
            context.stop();
        }

    }

    public Object invoke(MethodInvocation invocation) throws Throwable {
        TxcTransaction localTxcTransaction = this.findTxcTransaction(invocation);
        if (localTxcTransaction != defaultTxcTransaction) {
            int rollbackMode = localTxcTransaction.rollbackMode().getMode();
            int timeout = localTxcTransaction.timeout();
            String name = invocation.getMethod().getName();
            if (TxcContext.getCurrentXid() != null) {
                throw new TxcTransactionException(String.format("方法【%s】已经存在分布式事务", name));
            } else {
                TransactionInitResponse rep = insertTransactionLogViaDubbo(ContextUtils.getServerIp(), ContextUtils.getSystemName(), name, rollbackMode);
                TxcContext.bind(rep.getTransactionId(), rep.getTransactionStartTime(), timeout);
                Object result = null;

                try {
                    result = invocation.proceed();
                    TxcContext.commitTxc();
                    return result;
                } catch (RuntimeException e) {
                    TxcContext.rollbackTxc();
                    throw e;
                }
            }
        } else {
            return invocation.proceed();
        }
    }
}

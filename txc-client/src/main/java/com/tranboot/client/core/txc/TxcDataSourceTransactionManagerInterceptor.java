package com.tranboot.client.core.txc;

import com.codahale.metrics.Timer.Context;
import com.tranboot.client.exception.TxcRedisException;
import com.tranboot.client.exception.TxcTransactionTimeoutException;
import com.tranboot.client.model.txc.BranchRollbackMessage;
import com.tranboot.client.service.txc.TxcRedisService;
import com.tranboot.client.spring.ContextUtils;
import com.tranboot.client.utils.BranchIdGenerator;
import com.tranboot.client.utils.MetricsReporter;
import java.lang.reflect.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.AopInvocationException;
import org.springframework.aop.RawTargetAccess;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

public class TxcDataSourceTransactionManagerInterceptor implements MethodInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(TxcDataSourceTransactionManagerInterceptor.class);
    private TxcRedisService txcRedisService;
    private DataSourceTransactionManager target;

    public TxcDataSourceTransactionManagerInterceptor(DataSourceTransactionManager target, TxcRedisService txcRedisService) {
        this.target = target;
        this.txcRedisService = txcRedisService;
    }

    public Object invoke(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        String name = method.getName();
        Context context;
        Object retVal;
        long redisTime;
        Object var48;
        if ("doBegin".equals(name)) {
            context = MetricsReporter.beginTimer.time();

            Object var11;
            try {
                if (TxcContext.getCurrentXid() == null) {
                    retVal = methodProxy.invoke(this.target, args);
                    var48 = processReturnType(proxy, this.target, method, retVal);
                    return var48;
                }

                logger.debug("txc分支事务开启...");
                redisTime = BranchIdGenerator.branchId();
                BranchRollbackMessage message = TxcRollbackSqlManagerV2.beginTransaction(this.target.getDataSource(), redisTime, TxcContext.getCurrentXid(), ContextUtils.getServerIp());
                this.txcRedisService.hput(TxcRedisService.buildTransactionModel(message));
                retVal = methodProxy.invoke(this.target, args);
                logger.debug("txc分支事务开启成功");
                var11 = processReturnType(proxy, this.target, method, retVal);
            } finally {
                context.stop();
            }

            return var11;
        } else {
            BranchRollbackMessage message;
            if ("doCommit".equals(name)) {
                context = MetricsReporter.commitTimer.time();
                if (TxcContext.getCurrentXid() != null) {
                    logger.debug("txc分支事务提交...");

                    try {
                        redisTime = this.txcRedisService.getCurrentTimeMillisFromRedis();
                        if (redisTime > TxcContext.getTxcStart() + (long)(TxcContext.getTxcTimeout() * 1000)) {
                            logger.error("分布式事务超时,将自动rollback本地事务.");
                            throw new TxcTransactionTimeoutException(String.format("分布式事务%d超时: 系统时间:%d 事务开始时间:%d 超时设置:%d", TxcContext.getCurrentXid(), redisTime, TxcContext.getTxcStart(), TxcContext.getTxcTimeout() * 1000));
                        }
                    } catch (TxcRedisException var40) {
                        throw new TxcTransactionTimeoutException("获取redis 系统时间出错!");
                    }
                }

                try {
                    retVal = methodProxy.invoke(this.target, args);
                    retVal = processReturnType(proxy, this.target, method, retVal);
                    if (TxcContext.getCurrentXid() != null) {
                        try {
                            message = TxcRollbackSqlManagerV2.get(this.target.getDataSource());
                            logger.debug("更新reids状态位:{}", message);
                            message.setStatus(1);
                            this.txcRedisService.hput(TxcRedisService.buildTransactionModel(message));
                            logger.debug("更新reids状态位成功,txc分支事务提交成功");
                        } catch (Exception var39) {
                            logger.warn("更新分支事务redis状态出错！【本地事务已经提交成功，不影响数据】", var39);
                        }
                    }

                    var48 = retVal;
                } catch (Exception var43) {
                    logger.error("本地分支事务commit失败", var43);
                    throw var43;
                } finally {
                    if (TxcContext.getCurrentXid() != null) {
                        TxcRollbackSqlManagerV2.releaseResource(this.target.getDataSource());
                    }

                    context.stop();
                }

                return var48;
            } else if ("doRollback".equals(name)) {
                context = MetricsReporter.rollbackTimer.time();

                try {
                    retVal = methodProxy.invoke(this.target, args);
                    if (TxcContext.getCurrentXid() != null) {
                        try {
                            logger.debug("txc分支事务回滚...");
                            message = TxcRollbackSqlManagerV2.get(this.target.getDataSource());
                            message.setStatus(2);
                            logger.debug("更新reids状态位:{}", message);
                            this.txcRedisService.hput(TxcRedisService.buildTransactionModel(message));
                            logger.debug("更新reids状态位成功,txc分支事务回滚成功");
                        } catch (Exception var41) {
                            logger.warn("更新分支事务redis状态出错！【本地事务已经回滚成功，不影响数据】", var41);
                        }
                    }

                    var48 = processReturnType(proxy, this.target, method, retVal);
                } catch (Exception var45) {
                    logger.error("本地分支事务rollback失败！", var45);
                    throw var45;
                } finally {
                    if (TxcContext.getCurrentXid() != null) {
                        TxcRollbackSqlManagerV2.releaseResource(this.target.getDataSource());
                    }

                    context.stop();
                }

                return var48;
            } else {
                return null;
            }
        }
    }

    @Override
    public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        if (!"doCommit".equals(method.getName()) && !"doRollback".equals(method.getName()) && !"doBegin".equals(method.getName())) {
            Object retVal = methodProxy.invoke(this.target, args);
            return processReturnType(proxy, this.target, method, retVal);
        } else {
            return this.invoke(proxy, method, args, methodProxy);
        }
    }

    private static Object processReturnType(Object proxy, Object target, Method method, Object retVal) {
        if (retVal != null && retVal == target && !RawTargetAccess.class.isAssignableFrom(method.getDeclaringClass())) {
            retVal = proxy;
        }

        Class<?> returnType = method.getReturnType();
        if (retVal == null && returnType != Void.TYPE && returnType.isPrimitive()) {
            throw new AopInvocationException("Null return value from advice does not match primitive return type for: " + method);
        } else {
            return retVal;
        }
    }
}


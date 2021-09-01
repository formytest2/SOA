package com.tranboot.client.core.txc;

import com.github.bluecatlee.gs4d.transaction.api.response.TransactionInitResponse;
import com.tranboot.client.exception.TxcMQException;
import com.tranboot.client.exception.TxcRedisException;
import com.tranboot.client.exception.TxcTransactionException;
import com.tranboot.client.service.txc.TxcRedisService;
import com.tranboot.client.spring.ContextUtils;

/**
 * 基于消息队列的分布式事务上下文
 */
public final class TxcContext {

    protected static final String TXC_XID = "TXC_XID";
    protected static final String TXC_START = "TXC_START";
    protected static final String TXC_TIMEOUT = "TXC_TIMEOUT";

    /**
     * 绑定
     *      即把分布式事务id、事务开始时间、超时时间绑定到线程局部变量中
     * @param xid
     * @param txcStart
     * @param timeout
     */
    protected static void bind(Long xid, Long txcStart, int timeout) {
        TxcContextOperator.setContextParam(TXC_XID, xid);
        TxcContextOperator.setContextParam(TXC_START, txcStart);
        TxcContextOperator.setContextParam(TXC_TIMEOUT, timeout);
    }

    /**
     * 解绑
     *      即清除线程局部变量的内容
     */
    public static void unbind() {
        TxcContextOperator.removeContext();
    }

    /**
     * 获取当前的分布式事务id
     * @return
     */
    public static Long getCurrentXid() {
        return (Long)TxcContextOperator.getContextParam(TXC_XID);
    }

    /**
     * 获取当前的分布式事务的开始时间
     * @return
     */
    public static Long getTxcStart() {
        return (Long)TxcContextOperator.getContextParam(TXC_START);
    }

    /**
     * 获取当前的分布式事务的超时时间
     * @return
     */
    public static Integer getTxcTimeout() {
        return (Integer)TxcContextOperator.getContextParam(TXC_TIMEOUT);
    }

    /**
     * 判断当前是否存在分布式事务
     * @return
     */
    protected static boolean inTxcTransaction() {
        return getCurrentXid() != null;
    }

    /**
     * 开启分布式事务
     * @param methodName
     * @param rollbackMode  回滚模式
     * @see com.tranboot.client.model.txc.TxcRollbackMode
     * @param timeout
     * @return
     */
    public static Long beginTxc(String methodName, int rollbackMode, int timeout) {
        if (getCurrentXid() != null) {
            throw new TxcTransactionException(String.format("方法【%s】已经存在分布式事务", methodName));
        } else {
            TransactionInitResponse rep = TxcMethodInterceptor.insertTransactionLogViaDubbo(ContextUtils.getServerIp(), ContextUtils.getSystemName(), methodName, rollbackMode); // 新增分布式事务

            try {
                bind(rep.getTransactionId(), ((TxcRedisService)ContextUtils.getBean(TxcRedisService.class)).getCurrentTimeMillisFromRedis(), timeout); // 为避免机器时间差异 这里获取redis的时间
            } catch (TxcRedisException e) {
                throw new TxcTransactionException("获取分布式事务开始时间失败!");
            }

            return rep.getTransactionId();
        }
    }

    public static Long beginTxc(String methodName, int rollbackMode) {
        return beginTxc(methodName, rollbackMode, 10);
    }

    /**
     * 提交分布式事务
     * @return
     * @throws TxcMQException
     */
    public static boolean commitTxc() throws TxcMQException {
        Long xid = getCurrentXid();
        if (xid == null) {
            throw new TxcTransactionException("不存在分布式事务");
        } else {
            TxcMethodInterceptor.sendCommitMessage(xid);
            unbind();
            return true;
        }
    }

    /**
     * 回滚分布式事务
     * @return
     */
    public static boolean rollbackTxc() {
        Long xid = getCurrentXid();
        if (xid == null) {
            throw new TxcTransactionException("不存在分布式事务");
        } else {
            TxcMethodInterceptor.sendRollbackMessage(xid);
            unbind();
            return true;
        }
    }
}


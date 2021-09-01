package com.tranboot.client.core.txc;

import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.RpcException;
import com.codahale.metrics.Timer.Context;
import com.tranboot.client.utils.MetricsReporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 基于dubbo-filter机制的分布式事务拦截器
 */
public class TxcDubboFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(TxcDubboFilter.class);

    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        Context context = MetricsReporter.invokeTimer.time();
        boolean isProvider = false;

        Result result;
        try {
            String xid = RpcContext.getContext().getAttachment("xid");
            String txcStart = RpcContext.getContext().getAttachment("txc_start");
            String txcTimeout = RpcContext.getContext().getAttachment("txc_timeout");
            if (xid != null && TxcContext.getCurrentXid() == null) {
                isProvider = true;
                logger.debug("dubbo 服务调用存在txc上下文，开始自动绑定");
                TxcContext.bind(Long.parseLong(xid), Long.parseLong(txcStart), Integer.parseInt(txcTimeout));
            }

            if (xid == null && TxcContext.getCurrentXid() != null) {
                RpcContext.getContext().setAttachment("xid", String.valueOf(TxcContext.getCurrentXid()));
                RpcContext.getContext().setAttachment("txc_start", String.valueOf(TxcContext.getTxcStart()));
                RpcContext.getContext().setAttachment("txc_timeout", String.valueOf(TxcContext.getTxcTimeout()));
            }

            result = invoker.invoke(invocation);
        } catch (RpcException e) {
            throw e;
        } finally {
            if (isProvider) {
                TxcContext.unbind();
            }

            context.stop();
        }

        return result;
    }
}


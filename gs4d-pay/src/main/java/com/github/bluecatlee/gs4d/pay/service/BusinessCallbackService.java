package com.github.bluecatlee.gs4d.pay.service;

import org.springframework.lang.Nullable;
import org.springframework.scheduling.annotation.Async;

/**
 * 业务回调服务
 */
public interface BusinessCallbackService {

    /**
     * 执行业务回调 （需要确保支付完成才能调用此方法）
     * @param outTradeNo
     */
    @Async
    public void businessCallBack(String outTradeNo);

    /**
     * 查询需要进行业务回调的数据
     * @param outTradeNos
     * @return
     */
    Object queryNeedCallBackTask(@Nullable String outTradeNos);

}

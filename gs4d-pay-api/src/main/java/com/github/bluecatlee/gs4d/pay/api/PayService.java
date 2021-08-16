package com.github.bluecatlee.gs4d.pay.api;

/**
 * 通用支付服务
 */
public interface PayService {

    /**
     * 统一入口
     * @param paramMap
     * @param requestMethod
     * @return
     * @throws Exception
     */
    String pay(String paramMap, String requestMethod) throws Exception;

    /**
     * 定时自动修补数据 (异步操作可能存在数据未及时更新的情况 需要保证数据变更成功)
     *      这里就是修补支付或退款结果的数据，注意不能与异步回调的逻辑冲突。
     * @return
     * @throws Exception
     */
    String repair() throws Exception;

}


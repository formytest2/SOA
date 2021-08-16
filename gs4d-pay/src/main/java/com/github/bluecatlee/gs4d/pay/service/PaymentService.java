package com.github.bluecatlee.gs4d.pay.service;

import com.github.bluecatlee.gs4d.pay.bean.BaseRequest;
import com.github.bluecatlee.gs4d.pay.bean.BaseResponse;

import java.util.Map;

public interface PaymentService<T extends BaseRequest, U extends BaseResponse> {

    /**
     * 参数预处理
     * @param req
     * @param res
     * @param requestMethod
     * @throws Exception
     */
    void checkInputParams(T req, U res, String requestMethod) throws Exception;

    /**
     * 支付
     * @param req
     * @param res
     * @return
     * @throws Exception
     */
    BaseResponse pay(T req, U res) throws Exception;

    /**
     * 支付后处理
     * @param req
     * @param res
     * @param id
     * @return
     * @throws Exception
     */
    Map afterPay(T req, String res, long id) throws Exception;

    /**
     * 退款处理
     * @param req
     * @param res
     * @return
     * @throws Exception
     */
    BaseResponse refund(T req, U res) throws Exception;

    /**
     * 退款后处理
     * @param req
     * @param res
     * @param id
     * @return
     * @throws Exception
     */
    Map afterRefund(T req, String res, long id) throws Exception;

    /**
     * 查询支付结果
     * @param req
     * @param res
     * @return
     * @throws Exception
     */
    BaseResponse queryPayResult(T req, U res) throws Exception;

    /**
     * 查询退款结果
     * @param req
     * @param res
     * @return
     * @throws Exception
     */
    BaseResponse queryRefundResult(T req, U res) throws Exception;

//    /**
//     * 轮询支付结果
//     * @param payOrder 支付单
//     * @return
//     */
//    PayPollingRespVO payPolling(PayOrder payOrder);

//    /**
//     * 延时结算
//     * @param payOrder 支付单
//     * @return
//     */
//    CommonRespVO confirmSettle(PayOrder payOrder);

    /**
     * 服务器回调处理
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    default BaseResponse callbackNotify(BaseRequest request, BaseResponse response) throws Exception {
        return null;
    }

}

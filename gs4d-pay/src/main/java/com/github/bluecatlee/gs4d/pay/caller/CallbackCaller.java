package com.github.bluecatlee.gs4d.pay.caller;

import com.github.bluecatlee.gs4d.pay.bean.BaseResponse;
import com.github.bluecatlee.gs4d.pay.entity.PayOrderInfo;

/**
 * 业务回调调用器 自行实现
 */
public interface CallbackCaller {

    BaseResponse call(PayOrderInfo orderInfo, String businessCallback) throws Exception;

}

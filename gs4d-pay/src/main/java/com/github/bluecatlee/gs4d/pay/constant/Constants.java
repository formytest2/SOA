package com.github.bluecatlee.gs4d.pay.constant;

public class Constants {

//    public final static String APP_VERSION = "1.0.1";


    public final static String REDIS_KEY_STATUS_PROCESSING = "REDIS_KEY_STATUS_PROCESSING";

    public final static String REDIS_KEY_STATUS_COMPLETED = "REDIS_KEY_STATUS_COMPLETED";

    public final static long REDIS_KEY_STATUS_PROCESSING_TIMEOUT = 60;

    public final static long REDIS_KEY_STATUS_COMPLETED_TIMEOUT = 300;

//    //app商城 查询订单成功标识
//    public static final String APP_CALLBACK_TRADE_SUCCESS = "TRADE_SUCCESS";
//
//    //app商城 查询订单成功标识(退款)
//    public static final String APP_CALLBACK_REFUND_SUCCESS = "REFUND_SUCCESS";


//    public final static int SHARD_COUNT = 4;

    public static final String CCB_PAY_TYPE = "86"; // 建行龙支付类型
    public static final String DCEP_PAY_TYPE = "96";// 建行数币支付类型
    public static final String ABC_PAY_TYPE = "97";// 农行支付类型

}

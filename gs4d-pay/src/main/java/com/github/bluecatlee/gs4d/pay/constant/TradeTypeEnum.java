package com.github.bluecatlee.gs4d.pay.constant;

/**
 * 交易类型枚举
 */
public enum TradeTypeEnum {
    PAY("1", "支付"),
    REFUND("2","退款"),
    QUERY("3","查询"),
    ;

    private final String status;
    private final String message;

    TradeTypeEnum(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public String getMsg() {
        return message;
    }
}

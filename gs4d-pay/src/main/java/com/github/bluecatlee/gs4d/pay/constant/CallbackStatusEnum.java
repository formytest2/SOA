package com.github.bluecatlee.gs4d.pay.constant;

/**
 * 业务回调状态枚举
 */
public enum CallbackStatusEnum {
    NEW("0","未处理"),
    SUCCESS("1","已处理"),
    ERROR("-1","处理失败");

    private final String status;
    private final String message;

    CallbackStatusEnum(String status, String message) {
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

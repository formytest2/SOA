package com.github.bluecatlee.gs4d.pay.constant;

/**
 * 请求方法枚举
 */
public enum RequestMethodEnum {

    CANCEL("cancel", "取消"),
    QUERY("query", "查询"),
    NOTIFY("notify", "通知"),
    CREATE("create", "创建"),
    QUERYR("queryr", "退款查询");

    private final String method;
    private final String message;

    RequestMethodEnum(String method, String message) {
        this.method = method;
        this.message = message;
    }

    public String getMethod() {
        return method;
    }

    public String getMsg() {
        return message;
    }

     public static RequestMethodEnum getType(String dataTypeCode){
        for(RequestMethodEnum enums:RequestMethodEnum.values()){
            if(enums.method.equals(dataTypeCode)){
                return enums;
            }
        }
        return null;
    }

}

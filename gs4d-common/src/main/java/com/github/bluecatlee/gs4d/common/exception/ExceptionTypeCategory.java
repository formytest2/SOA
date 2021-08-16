package com.github.bluecatlee.gs4d.common.exception;

public enum ExceptionTypeCategory {

    // 通用异常
    GENERAL_EXCEPTION,

    // 客户端校验异常
    VALIDATE_CLIENT_EXCEPTION,

    // 业务校验异常
    VALIDATE_BUSINESS_EXCEPTION,

    // 数据库异常
    DATABASE_OPERATE_EXCEPTION,

    // 业务异常
    BUSINESS_EXCEPTION;

    /**
     * 枚举类型的构造默认是私有的
     *      扩展：jvm保证枚举的每个实例只被初始化一次，可以利用这个特性实现单例。
     */
    private ExceptionTypeCategory() {
    }

}

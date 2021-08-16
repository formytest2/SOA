package com.github.bluecatlee.gs4d.cache.exception;

import com.github.bluecatlee.gs4d.common.exception.AbstractExceptionType;
import com.github.bluecatlee.gs4d.common.exception.ExceptionTypeCategory;

public class CacheExceptionType extends AbstractExceptionType {

    public static CacheExceptionType DOE30042;

    static {
        DOE30042 = new CacheExceptionType(-30042L, ExceptionTypeCategory.DATABASE_OPERATE_EXCEPTION, CacheExceptionType.SubSystem.CACHE_SYSTEM, "缓存系统操作数据库异常");
    }

    protected CacheExceptionType(long code, ExceptionTypeCategory category, String subSystem, String description) {
        super(code, category, subSystem, description);
    }

    public static class SubSystem {
        public static String CACHE_SYSTEM = "cache";

        public SubSystem() {
        }
    }

}

package com.github.bluecatlee.gs4d.monitor.api.exception;

import com.github.bluecatlee.gs4d.common.exception.AbstractExceptionType;
import com.github.bluecatlee.gs4d.common.exception.ExceptionTypeCategory;

public class CodeMonitorExceptionType extends AbstractExceptionType {
    private Long systemId = Long.valueOf(20L);

    protected CodeMonitorExceptionType(long code, ExceptionTypeCategory category, String description) {
        super(code, category, description);
        lookup.put(Long.valueOf(code), this);
    }

    public static CodeMonitorExceptionType VCE120101 = new CodeMonitorExceptionType(-120101L, ExceptionTypeCategory.VALIDATE_CLIENT_EXCEPTION, "客户端输入验证异常");

    public static CodeMonitorExceptionType VBE120201 = new CodeMonitorExceptionType(-120201L, ExceptionTypeCategory.VALIDATE_BUSINESS_EXCEPTION, "业务验证异常");

    public static CodeMonitorExceptionType DOE120301 = new CodeMonitorExceptionType(-120301L, ExceptionTypeCategory.DATABASE_OPERATE_EXCEPTION, "数据库操作验证异常");

    public static CodeMonitorExceptionType BSE120401 = new CodeMonitorExceptionType(-120401L, ExceptionTypeCategory.BUSINESS_EXCEPTION, "联系人不存在请维护platform_error_user_watch和platform_error_notice_user表");

    public static CodeMonitorExceptionType BSE120402 = new CodeMonitorExceptionType(-120402L, ExceptionTypeCategory.BUSINESS_EXCEPTION, "发送邮件异常");

    public static CodeMonitorExceptionType BSE120403 = new CodeMonitorExceptionType(-120403L, ExceptionTypeCategory.BUSINESS_EXCEPTION, "未能找到指定code");

    public static CodeMonitorExceptionType BSE120404 = new CodeMonitorExceptionType(-120404L, ExceptionTypeCategory.BUSINESS_EXCEPTION, "系统编号在数据表platform_system_default_error_user中不存在");

    public static CodeMonitorExceptionType BSE120405 = new CodeMonitorExceptionType(-120405L, ExceptionTypeCategory.BUSINESS_EXCEPTION, "用户编号在platform_error_notice_user表查询不到");

    public static CodeMonitorExceptionType INSTANCE = VCE120101;

    public AbstractExceptionType getAbstractExceptionTypeByCode(long code) {
        return (AbstractExceptionType)lookup.get(Long.valueOf(code));
    }
}


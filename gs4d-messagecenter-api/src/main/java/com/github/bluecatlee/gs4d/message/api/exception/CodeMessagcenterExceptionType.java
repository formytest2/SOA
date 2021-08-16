package com.github.bluecatlee.gs4d.message.api.exception;

import com.github.bluecatlee.gs4d.common.exception.AbstractExceptionType;
import com.github.bluecatlee.gs4d.common.exception.ExceptionTypeCategory;

public class CodeMessagcenterExceptionType extends AbstractExceptionType {
    private Long systemId = 19L;
    public static CodeMessagcenterExceptionType VCE119101;
    public static CodeMessagcenterExceptionType VBE119201;
    public static CodeMessagcenterExceptionType DOE119301;
    public static CodeMessagcenterExceptionType BSE119401;
    public static CodeMessagcenterExceptionType BSE119402;

    protected CodeMessagcenterExceptionType(long code, ExceptionTypeCategory category, String description) {
        super(code, category, description);
        lookup.put(code, this);
    }

    public AbstractExceptionType getAbstractExceptionTypeByCode(long code) {
        return (AbstractExceptionType)lookup.get(code);
    }

    static {
        VCE119101 = new CodeMessagcenterExceptionType(-119101L, ExceptionTypeCategory.VALIDATE_CLIENT_EXCEPTION, "系统id为空,请重新输入");
        VBE119201 = new CodeMessagcenterExceptionType(-119201L, ExceptionTypeCategory.VALIDATE_BUSINESS_EXCEPTION, "业务验证异常");
        DOE119301 = new CodeMessagcenterExceptionType(-119301L, ExceptionTypeCategory.DATABASE_OPERATE_EXCEPTION, "数据库操作验证异常");
        BSE119401 = new CodeMessagcenterExceptionType(-119401L, ExceptionTypeCategory.BUSINESS_EXCEPTION, "一个系统id都没有找到请去维护系统id");
        BSE119402 = new CodeMessagcenterExceptionType(-119402L, ExceptionTypeCategory.BUSINESS_EXCEPTION, "当前topic和tag系统中已经存在！");
    }
}


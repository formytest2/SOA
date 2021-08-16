package com.github.bluecatlee.gs4d.common.exception;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public enum ExceptionType {
    VCE10000(-10000L, ExceptionTypeCategory.VALIDATE_CLIENT_EXCEPTION, SubSystem.SUB_SYSTEM_MANAGE, "通用客户端校验异常"),
    VCE10001(-10001L, ExceptionTypeCategory.VALIDATE_CLIENT_EXCEPTION, SubSystem.SUB_SYSTEM_MANAGE, "后台管理系统客户端验证异常"),
    VCE10007(-10007L, ExceptionTypeCategory.VALIDATE_CLIENT_EXCEPTION, SubSystem.SUB_SYSTEM_BASEINFO, "基础资料客户端验证异常"),
    VCE10027(-10027L, ExceptionTypeCategory.VALIDATE_CLIENT_EXCEPTION, SubSystem.SUB_SYSTEM_MANAGE, "后台管理系统客户端验证异常"),
    VCE10030(-10030L, ExceptionTypeCategory.VALIDATE_CLIENT_EXCEPTION, SubSystem.SUB_SYSTEM_CACHE, "缓存系统客户端验证异常"),
    VCE10031(-10031L, ExceptionTypeCategory.VALIDATE_CLIENT_EXCEPTION, SubSystem.SUB_SYSTEM_MSSSAGECENTER, "消息中心客户端验证异常"),
    VCE10033(-10033L, ExceptionTypeCategory.VALIDATE_CLIENT_EXCEPTION, SubSystem.SUB_SYSTEM_EXPORT, "数据导出参数验证异常"),
    VCE10034(-10034L, ExceptionTypeCategory.VALIDATE_CLIENT_EXCEPTION, SubSystem.SUB_SYSTEM_IMPORT, "数据导入参数验证异常"),

    VBE20007(-20007L, ExceptionTypeCategory.VALIDATE_BUSINESS_EXCEPTION, SubSystem.SUB_SYSTEM_BASEINFO, "基础资料业务验证异常"),
    VBE20030(-20030L, ExceptionTypeCategory.VALIDATE_BUSINESS_EXCEPTION, SubSystem.SUB_SYSTEM_CACHE, "缓存服务验证异常"),
    VBE20031(-20031L, ExceptionTypeCategory.VALIDATE_BUSINESS_EXCEPTION, SubSystem.SUB_SYSTEM_MSSSAGECENTER, "消息中心验证异常"),

    VBE20033(-20033L, ExceptionTypeCategory.VALIDATE_BUSINESS_EXCEPTION, SubSystem.SUB_SYSTEM_EXPORT, "数据同步导出服务验证异常"),

    DOE30045(-30045L, ExceptionTypeCategory.DATABASE_OPERATE_EXCEPTION, SubSystem.SUB_SYSTEM_MSSSAGECENTER, "消息中心WEB操作数据库异常"),
    DOE30051(-30051L, ExceptionTypeCategory.DATABASE_OPERATE_EXCEPTION, SubSystem.SUB_SYSTEM_EXPORT, "数据同步导出系统操作数据库异常"),
    DOE30052(-30052L, ExceptionTypeCategory.DATABASE_OPERATE_EXCEPTION, SubSystem.SUB_SYSTEM_IMPORT, "数据同步导入系统操作数据库异常"),
    DOE30054(-30054L, ExceptionTypeCategory.DATABASE_OPERATE_EXCEPTION, SubSystem.SUB_SYSTEM_MSSSAGECENTER, "消息中心获取日志表实体异常"),

    BE40071(-40071L, ExceptionTypeCategory.BUSINESS_EXCEPTION, SubSystem.SUB_SYSTEM_CACHE, "数据库中无指定资料，获取缓存失败！"),
    BE40072(-40072L, ExceptionTypeCategory.BUSINESS_EXCEPTION, SubSystem.SUB_SYSTEM_CACHE, "数据库中存在多比指定资料，获取缓存失败！"),
    BE40073(-40073L, ExceptionTypeCategory.BUSINESS_EXCEPTION, SubSystem.SUB_SYSTEM_CACHE, "从本地内存缓存中获取数据源信息出错！"),
    BE40093(-40093L, ExceptionTypeCategory.BUSINESS_EXCEPTION, SubSystem.SUB_SYSTEM_EXPORT, "同步数据导出参数类型错误"),
    BE40101(-40101L, ExceptionTypeCategory.BUSINESS_EXCEPTION, SubSystem.SUB_SYSTEM_MSSSAGECENTER, "消息中心消费异常！"),
    BE40144(-40144L, ExceptionTypeCategory.BUSINESS_EXCEPTION, SubSystem.SUB_SYSTEM_BASEINFO, "探测系统时发现系统停止！"),
    BE40160(-40160L, ExceptionTypeCategory.BUSINESS_EXCEPTION, SubSystem.SUB_SYSTEM_IMPORT, "事务消息回查消息序号查询失败!"),
    BE40117(-40117L, ExceptionTypeCategory.BUSINESS_EXCEPTION, SubSystem.SUB_SYSTEM_MSSSAGECENTER, "消息中心消费端业务执行异常"),

    BE40202(-40202L, ExceptionTypeCategory.BUSINESS_EXCEPTION, SubSystem.SUB_SYSTEM_EXPORT, "通用查询子查询查到多笔纪录"),
    BE40203(-40203L, ExceptionTypeCategory.BUSINESS_EXCEPTION, SubSystem.SUB_SYSTEM_EXPORT, "通用查询没有查询到纪录"),



    ;


    /**
     * 异常码
     */
    private long code;
    /**
     * 子系统名称
     */
    private String subSystem;
    /**
     * 异常信息
     */
    private String description;
    /**
     * 异常类型分类
     */
    private ExceptionTypeCategory category;

    private static final Map<Long, ExceptionType> lookup = new HashMap();

    private ExceptionType(long code, ExceptionTypeCategory category, String subSystem, String description) {
        this.code = code;
        this.category = category;
        this.subSystem = subSystem;
        this.description = description;
    }

    public long getCode() {
        return this.code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getSubSystem() {
        return this.subSystem;
    }

    public void setSubSystem(String subSystem) {
        this.subSystem = subSystem;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ExceptionTypeCategory getCategory() {
        return this.category;
    }

    public void setCategory(ExceptionTypeCategory category) {
        this.category = category;
    }

    public static ExceptionType getExceptionTypeByCode(long code) {
        return (ExceptionType)lookup.get(code);
    }

    static {
        Iterator iterator = EnumSet.allOf(ExceptionType.class).iterator();

        while(iterator.hasNext()) {
            ExceptionType et = (ExceptionType)iterator.next();
            lookup.put(et.getCode(), et);
        }

    }

    /**
     * 定义各子系统名称
     */
    private static class SubSystem {
        public static String SUB_SYSTEM_MANAGE = "manage";
        public static String SUB_SYSTEM_BASEINFO = "baseinfo";
        public static String SUB_SYSTEM_CACHE = "cache";
        public static String SUB_SYSTEM_EXPORT = "export";
        public static String SUB_SYSTEM_IMPORT = "import";
        public static String SUB_SYSTEM_MSSSAGECENTER = "messagecenter";

        private SubSystem() {
        }
    }
}


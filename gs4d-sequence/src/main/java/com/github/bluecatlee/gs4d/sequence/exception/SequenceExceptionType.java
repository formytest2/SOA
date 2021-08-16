package com.github.bluecatlee.gs4d.sequence.exception;

import com.github.bluecatlee.gs4d.common.exception.AbstractExceptionType;
import com.github.bluecatlee.gs4d.common.exception.ExceptionTypeCategory;

import java.util.HashMap;
import java.util.Map;

public class SequenceExceptionType extends AbstractExceptionType {
    public static SequenceExceptionType VCE10001;
    public static SequenceExceptionType INSTANCE;
    private static final Map<Long, AbstractExceptionType> lookup;

    private SequenceExceptionType(long code, ExceptionTypeCategory category, String subSystem, String description) {
        super(code, category, subSystem, description);
    }

    public AbstractExceptionType getAbstractExceptionTypeByCode(long code) {
        AbstractExceptionType et = (AbstractExceptionType)lookup.get(code);
        if (et == null) {
            throw new RuntimeException("不存在异常编号:" + code);
        } else {
            return et;
        }
    }

    static {
        VCE10001 = new SequenceExceptionType(-10001L, ExceptionTypeCategory.VALIDATE_CLIENT_EXCEPTION, SequenceExceptionType.SubSystem.SEQUENCE_CLIENT, "没有找到对应配置!");
        INSTANCE = VCE10001;
        lookup = new HashMap();
        lookup.put(VCE10001.getCode(), VCE10001);
    }

    private static class SubSystem {
        public static String SEQUENCE_CLIENT = "sequence";

        private SubSystem() {
        }
    }
}


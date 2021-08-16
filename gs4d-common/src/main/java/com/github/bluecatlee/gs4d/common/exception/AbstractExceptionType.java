package com.github.bluecatlee.gs4d.common.exception;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractExceptionType {

    private long code;
    protected String subSystem;
    private String description;
    private ExceptionTypeCategory category;
    public static final Map<Long, AbstractExceptionType> lookup = new HashMap();

    protected AbstractExceptionType(long code, ExceptionTypeCategory category, String subSystem, String description) {
        this.code = code;
        this.subSystem = subSystem;
        this.description = description;
        this.category = category;
        lookup.put(code, this);         // 子类实例化的时候 这个this指向的是子类
    }

    protected AbstractExceptionType(long code, ExceptionTypeCategory category, String description) {
        this.category = category;
        this.code = code;
        this.description = description;
        lookup.put(code, this);
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

    public AbstractExceptionType getAbstractExceptionTypeByCode(long code) {
        return lookup.get(code);
    }

}


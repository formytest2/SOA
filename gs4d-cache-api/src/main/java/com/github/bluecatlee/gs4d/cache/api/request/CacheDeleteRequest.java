package com.github.bluecatlee.gs4d.cache.api.request;

import com.github.bluecatlee.gs4d.common.bean.AbstractRequest;

import javax.validation.constraints.NotEmpty;

public class CacheDeleteRequest extends AbstractRequest {
    private static final long serialVersionUID = 1L;
    @NotEmpty(message = "缓存键不可为空")
    private String cacheKey;
    private Object[] keyValues;

    public CacheDeleteRequest() {
    }

    public String getCacheKey() {
        return this.cacheKey;
    }

    public void setCacheKey(String cacheKey) {
        this.cacheKey = cacheKey;
    }

    public Object[] getKeyValues() {
        return this.keyValues;
    }

    public void setKeyValues(Object[] keyValues) {
        this.keyValues = keyValues;
    }
}

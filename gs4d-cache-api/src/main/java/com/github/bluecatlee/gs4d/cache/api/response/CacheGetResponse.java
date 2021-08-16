package com.github.bluecatlee.gs4d.cache.api.response;


import com.github.bluecatlee.gs4d.common.bean.MessagePack;

public class CacheGetResponse extends MessagePack {

    private static final long serialVersionUID = 1L;
//    @ApiField(description = "缓存结果")
    private String cacheResult;

    public CacheGetResponse() {
    }

    public String getCacheResult() {
        return this.cacheResult;
    }

    public void setCacheResult(String cacheResult) {
        this.cacheResult = cacheResult;
    }
}

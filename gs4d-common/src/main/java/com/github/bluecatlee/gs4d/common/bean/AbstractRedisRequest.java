package com.github.bluecatlee.gs4d.common.bean;


import javax.validation.constraints.NotNull;

public class AbstractRedisRequest extends AbstractRequest {
    private static final long serialVersionUID = 1756482171631052295L;
    @NotNull(message = "redis的key值不能为空!")
    private String redisKey;

    public AbstractRedisRequest() {
    }

    public String getRedisKey() {
        return this.redisKey;
    }

    public void setRedisKey(String redisKey) {
        this.redisKey = redisKey;
    }
}


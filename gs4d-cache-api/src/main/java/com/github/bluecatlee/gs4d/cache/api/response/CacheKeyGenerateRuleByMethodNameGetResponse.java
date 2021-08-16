package com.github.bluecatlee.gs4d.cache.api.response;

import com.github.bluecatlee.gs4d.cache.api.model.CacheKeyGenerateRule;
import com.github.bluecatlee.gs4d.common.bean.MessagePack;

public class CacheKeyGenerateRuleByMethodNameGetResponse extends MessagePack {
    private static final long serialVersionUID = 1L;
//    @ApiField(description = "缓存主键生成规则")
    private CacheKeyGenerateRule rule;

    public CacheKeyGenerateRuleByMethodNameGetResponse() {
    }

    public CacheKeyGenerateRule getRule() {
        return this.rule;
    }

    public void setRule(CacheKeyGenerateRule rule) {
        this.rule = rule;
    }
}


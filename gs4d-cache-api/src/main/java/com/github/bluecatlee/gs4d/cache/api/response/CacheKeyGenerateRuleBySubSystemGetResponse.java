package com.github.bluecatlee.gs4d.cache.api.response;


import com.github.bluecatlee.gs4d.cache.api.model.CacheKeyGenerateRule;
import com.github.bluecatlee.gs4d.common.bean.MessagePack;

import java.util.List;

public class CacheKeyGenerateRuleBySubSystemGetResponse extends MessagePack {
    private static final long serialVersionUID = 1L;
//    @ApiField(description = "缓存主键规则列表")
    private List<CacheKeyGenerateRule> rules;

    public CacheKeyGenerateRuleBySubSystemGetResponse() {
    }

    public List<CacheKeyGenerateRule> getRules() {
        return this.rules;
    }

    public void setRules(List<CacheKeyGenerateRule> rules) {
        this.rules = rules;
    }
}

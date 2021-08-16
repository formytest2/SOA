package com.github.bluecatlee.gs4d.cache.api.service;


import com.github.bluecatlee.gs4d.cache.api.request.CacheDeleteRequest;
import com.github.bluecatlee.gs4d.cache.api.request.CacheGetRequest;
import com.github.bluecatlee.gs4d.cache.api.request.CacheKeyGenerateRuleByMethodNameGetRequest;
import com.github.bluecatlee.gs4d.cache.api.request.CacheKeyGenerateRuleBySubSystemGetRequest;
import com.github.bluecatlee.gs4d.cache.api.response.CacheDeleteResponse;
import com.github.bluecatlee.gs4d.cache.api.response.CacheGetResponse;
import com.github.bluecatlee.gs4d.cache.api.response.CacheKeyGenerateRuleByMethodNameGetResponse;
import com.github.bluecatlee.gs4d.cache.api.response.CacheKeyGenerateRuleBySubSystemGetResponse;

public interface CacheStoreService {

    CacheGetResponse getCache(CacheGetRequest request);

    CacheKeyGenerateRuleBySubSystemGetResponse getCacheKeyGenerateRuleBySubSystem(CacheKeyGenerateRuleBySubSystemGetRequest request);

    CacheKeyGenerateRuleByMethodNameGetResponse getCacheKeyGenerateRuleByMethodName(CacheKeyGenerateRuleByMethodNameGetRequest request);

    CacheDeleteResponse deleteCache(CacheDeleteRequest request);

}

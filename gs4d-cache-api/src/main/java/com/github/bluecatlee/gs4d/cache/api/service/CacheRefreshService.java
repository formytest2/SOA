package com.github.bluecatlee.gs4d.cache.api.service;

import com.github.bluecatlee.gs4d.cache.api.request.CommonCacheRefreshRequest;
import com.github.bluecatlee.gs4d.cache.api.response.CommonCacheRefreshResponse;

public interface CacheRefreshService {
    CommonCacheRefreshResponse refreshCommonCache(CommonCacheRefreshRequest request);
}


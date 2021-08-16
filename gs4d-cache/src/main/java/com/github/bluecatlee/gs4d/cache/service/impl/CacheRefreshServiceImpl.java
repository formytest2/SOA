package com.github.bluecatlee.gs4d.cache.service.impl;

import com.fasterxml.jackson.databind.PropertyNamingStrategy.LowerCaseStrategy;
import com.github.bluecatlee.gs4d.cache.constant.Constants;
import com.github.bluecatlee.gs4d.cache.dao.EcCacheMethodSchemaDefineDao;
import com.github.bluecatlee.gs4d.cache.dao.EcCommonCacheDependenceDao;
import com.github.bluecatlee.gs4d.cache.dao.MdmsSConfigDao;
import com.github.bluecatlee.gs4d.cache.entity.EcCacheMethodSchemaDefine;
import com.github.bluecatlee.gs4d.cache.entity.EcCommonCacheDependence;
import com.github.bluecatlee.gs4d.cache.api.request.CacheDeleteRequest;
import com.github.bluecatlee.gs4d.cache.api.request.CacheGetRequest;
import com.github.bluecatlee.gs4d.cache.api.request.CommonCacheRefreshRequest;
import com.github.bluecatlee.gs4d.cache.api.response.CacheDeleteResponse;
import com.github.bluecatlee.gs4d.cache.api.response.CommonCacheRefreshResponse;
import com.github.bluecatlee.gs4d.cache.api.service.CacheRefreshService;
import com.github.bluecatlee.gs4d.cache.api.service.CacheStoreService;
import com.github.bluecatlee.gs4d.common.exception.ExceptionType;
import com.github.bluecatlee.gs4d.common.exception.ValidateBusinessException;
import com.github.bluecatlee.gs4d.common.utils.ExceptionUtil;
import com.github.bluecatlee.gs4d.common.utils.MyJsonMapper;
import com.github.bluecatlee.gs4d.common.utils.TransactionUtil;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service("cacheRefreshService")
public class CacheRefreshServiceImpl implements CacheRefreshService {

    private static final Logger logger = LoggerFactory.getLogger(CacheRefreshServiceImpl.class);

    private static MyJsonMapper nonEmptyMapper = MyJsonMapper.nonEmptyMapper();
//    private static MyJsonMapper nonDefaultMapper = MyJsonMapper.nonDefaultMapper();

    public static Cache<String, Object> cache;

    static {
        nonEmptyMapper.getMapper().setPropertyNamingStrategy(LowerCaseStrategy.SNAKE_CASE);
        cache = CacheBuilder.newBuilder().maximumSize(200000L).expireAfterWrite(1L, TimeUnit.DAYS).build();
    }

    @Resource(name = "stringRedisTemplate")
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private EcCacheMethodSchemaDefineDao ecCacheMethodSchemaDefineDao;

    @Resource
    private CacheStoreService cacheStoreService;

    @Resource
    private CacheStoreService ecCacheStoreService;

    @Resource(name = "commonTransactionManager")
    private PlatformTransactionManager transactionManager;

    @Resource
    private EcCommonCacheDependenceDao ecCommonCacheDependenceDao;

//    @Value("${refresh.cache.special.group:}")
//    private String refreshCacheSpecialGroup;

    @Resource
    private MdmsSConfigDao mdmsSConfigDao;

    public CommonCacheRefreshResponse refreshCommonCache(CommonCacheRefreshRequest request) {
        if (logger.isDebugEnabled()) {
            logger.debug("begin refreshCommonCache request:{}", nonEmptyMapper.toJson(request));
        }

        CommonCacheRefreshResponse response = new CommonCacheRefreshResponse();

        try {
            request.validate(Constants.SUB_SYSTEM, ExceptionType.VCE10030);
            Long tenantNumId = request.getTenantNumId();
            Long dataSign = request.getDataSign();
            String datasource = this.getDb(tenantNumId, dataSign, request.getDatabase(), this.getCacheDbKey(request.getDatabase()));
            List<EcCommonCacheDependence> commonCacheDependenceList = this.ecCommonCacheDependenceDao.queryByDbAndTableNameAndTableSeries(tenantNumId, dataSign, datasource, request.getTableName(), request.getTableSeries());
            if (CollectionUtils.isNotEmpty(commonCacheDependenceList)) {
                commonCacheDependenceList.stream().forEach((commonCacheDependence) -> {
                    TransactionStatus transactionStatus = this.transactionManager.getTransaction(TransactionUtil.newTransactionDefinition());

                    try {
                        this.ecCommonCacheDependenceDao.deleteByMethodNameAndCacheKeyAndSeries(commonCacheDependence.getTENANT_NUM_ID(), commonCacheDependence.getDATA_SIGN(), commonCacheDependence.getMETHOD_NAME(), commonCacheDependence.getCACHE_KEY(), commonCacheDependence.getSERIES());
                        if (StringUtils.isNotEmpty(commonCacheDependence.getDUBBO_GROUP())) {
                            CacheDeleteRequest cacheDeleteRequest = new CacheDeleteRequest();
                            cacheDeleteRequest.setTenantNumId(tenantNumId);
                            cacheDeleteRequest.setDataSign(dataSign);
                            cacheDeleteRequest.setCacheKey(commonCacheDependence.getCACHE_KEY());
                            CacheDeleteResponse cacheDeleteResponse = this.ecCacheStoreService.deleteCache(cacheDeleteRequest);
                            ExceptionUtil.checkDubboException(cacheDeleteResponse);
                        } else {
                            this.stringRedisTemplate.delete(commonCacheDependence.getCACHE_KEY());
                        }

                        EcCacheMethodSchemaDefine cacheMethodSchemaDefine = this.ecCacheMethodSchemaDefineDao.queryCacheMethodSchemaDefine(commonCacheDependence.getMETHOD_NAME());
                        if (cacheMethodSchemaDefine.getLIST_SIGN().equals(1L)) {
                            List<String> seriesList = this.ecCommonCacheDependenceDao.querySeriesByMethodNameAndCacheKey(commonCacheDependence.getTENANT_NUM_ID(), commonCacheDependence.getDATA_SIGN(), commonCacheDependence.getMETHOD_NAME(), commonCacheDependence.getCACHE_KEY());
                            if (CollectionUtils.isNotEmpty(seriesList)) {
                                Iterator iterator = seriesList.iterator();

                                while(iterator.hasNext()) {
                                    String series = (String)iterator.next();
                                    this.ecCommonCacheDependenceDao.deleteByMethodNameAndCacheKeyAndSeries(commonCacheDependence.getTENANT_NUM_ID(), commonCacheDependence.getDATA_SIGN(), commonCacheDependence.getMETHOD_NAME(), commonCacheDependence.getCACHE_KEY(), series);
                                }
                            }
                        }

                        if (request.getOptType().equals(2)) {  // 重新加载
                            CacheGetRequest cacheGetRequest = new CacheGetRequest();
                            cacheGetRequest.setTenantNumId(commonCacheDependence.getTENANT_NUM_ID());
                            cacheGetRequest.setDataSign(commonCacheDependence.getDATA_SIGN());
                            cacheGetRequest.setMethodName(commonCacheDependence.getMETHOD_NAME());
                            cacheGetRequest.setParams(commonCacheDependence.getPARAMS());
//                            if (StringUtils.isNotEmpty(commonCacheDependence.getDUBBO_GROUP())) {
//                                this.ecCacheStoreService.getCache(cacheGetRequest);
//                            } else {
                                this.cacheStoreService.getCache(cacheGetRequest);
//                            }
                        }

                        this.transactionManager.commit(transactionStatus);
                    } catch (Exception e) {
                        this.transactionManager.rollback(transactionStatus);
                        logger.info("清理缓存数据异常！{}", e.getMessage());
                    }

                });
            }
        } catch (Exception e) {
            ExceptionUtil.processException(e, response);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("end refreshCommonCache response:{}", response.toLowerCaseJson());
        }

        return response;
    }

    /**
     * 根据数据库名(真实数据库名，非逻辑库)获取配置的数据源名
     * @param tenantNumId
     * @param dataSign
     * @param database
     * @param cacheKey
     * @return
     */
    private String getDb(Long tenantNumId, Long dataSign, String database, String cacheKey) {
        try {
            return (String)cache.get(cacheKey, () -> {
                String datasource = this.mdmsSConfigDao.getConfigValue(0L, 0L, "refresh_common_cache_" + database);
                if (StringUtils.isBlank(datasource)) {
                    throw new ValidateBusinessException(Constants.SUB_SYSTEM, ExceptionType.BE40073, "从本地内存缓存中获取数据源信息出错!database:" + database);
                } else {
                    return datasource;
                }
            });
        } catch (ExecutionException e) {
            throw new ValidateBusinessException(Constants.SUB_SYSTEM, ExceptionType.BE40073, e.getMessage());
        }
    }

    private String getCacheDbKey(String database) {
        return "refresh_db_name_" + database;
    }

}


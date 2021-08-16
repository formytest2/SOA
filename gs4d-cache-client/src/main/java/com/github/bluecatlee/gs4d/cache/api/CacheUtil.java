package com.github.bluecatlee.gs4d.cache.api;

import com.github.bluecatlee.gs4d.cache.api.model.CacheKeyGenerateRule;
import com.github.bluecatlee.gs4d.cache.api.request.CacheGetRequest;
import com.github.bluecatlee.gs4d.cache.api.request.CacheKeyGenerateRuleByMethodNameGetRequest;
import com.github.bluecatlee.gs4d.cache.api.request.CacheKeyGenerateRuleBySubSystemGetRequest;
import com.github.bluecatlee.gs4d.cache.api.response.CacheGetResponse;
import com.github.bluecatlee.gs4d.cache.api.response.CacheKeyGenerateRuleByMethodNameGetResponse;
import com.github.bluecatlee.gs4d.cache.api.response.CacheKeyGenerateRuleBySubSystemGetResponse;
import com.github.bluecatlee.gs4d.cache.api.service.CacheStoreService;
import com.github.bluecatlee.gs4d.common.bean.AbstractRequest;
import com.github.bluecatlee.gs4d.common.bean.MessagePack;
import com.github.bluecatlee.gs4d.common.exception.ExceptionType;
import com.github.bluecatlee.gs4d.common.exception.ValidateBusinessException;
import com.github.bluecatlee.gs4d.common.utils.ExceptionUtil;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.gson.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;

/**
 * todo 缓存工具类应该由各自服务各自初始化 (可以将该工具类单独做到一个api包中)
 */
@Component
@Lazy(false)
@Scope("singleton")
public class CacheUtil implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger log = LoggerFactory.getLogger(CacheUtil.class);
    private static Gson gson = (new GsonBuilder()).create();
    public static Cache<String, CacheKeyGenerateRule> cache = CacheBuilder.newBuilder().maximumSize(1000L).build();

    private static final String SUB_SYSTEM = "cache";
    
    @Value("${current.subsystem}")
    private String currentSubSystem;

    public CacheUtil() {
    }

    public static class CacheUtilInnerClass {
        public static StringRedisTemplate stringRedisTemplate;
        public static CacheStoreService cacheStoreService;

        public CacheUtilInnerClass() {
        }
    }

    @Resource(name = "stringRedisTemplate")
    public void setStringRedisTemplate(StringRedisTemplate stringRedisTemplate) {
        CacheUtil.CacheUtilInnerClass.stringRedisTemplate = stringRedisTemplate;
    }

    @Resource
    public void setCacheStoreService(CacheStoreService cacheStoreService) {
        CacheUtil.CacheUtilInnerClass.cacheStoreService = cacheStoreService;
    }

    /**
     * 监听处理ContextRefreshedEvent
     *      spring容器初始化完成后 直接将缓存键规则加载到本地内存中
     * @param event
     */
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null) {
            log.debug("begin getCacheKeyGenerateRuleBySubSystem ");
            CacheKeyGenerateRuleBySubSystemGetRequest request = new CacheKeyGenerateRuleBySubSystemGetRequest();
            request.setTenantNumId(0L);
            request.setDataSign(0L);
            request.setSubSystem(this.currentSubSystem);
            CacheKeyGenerateRuleBySubSystemGetResponse response = CacheUtil.CacheUtilInnerClass.cacheStoreService.getCacheKeyGenerateRuleBySubSystem(request);
            ExceptionUtil.checkDubboException(response);
            Iterator iterator = response.getRules().iterator();

            CacheKeyGenerateRule rule;
            String key;
            while(iterator.hasNext()) {
                rule = (CacheKeyGenerateRule)iterator.next();
                key = "0_" + rule.getMethodName();
                cache.put(key, rule);
            }

            request = new CacheKeyGenerateRuleBySubSystemGetRequest();
            request.setTenantNumId(0L);
            request.setDataSign(1L);
            request.setSubSystem(this.currentSubSystem);
            response = CacheUtil.CacheUtilInnerClass.cacheStoreService.getCacheKeyGenerateRuleBySubSystem(request);
            ExceptionUtil.checkDubboException(response);
            iterator = response.getRules().iterator();

            while(iterator.hasNext()) {
                rule = (CacheKeyGenerateRule)iterator.next();
                key = "1_" + rule.getMethodName();
                cache.put(key, rule);
            }

            log.debug("end getCacheKeyGenerateRuleBySubSystem ");
        }

    }

    /**
     * 根据缓存方法名和参数获取缓存值
     * @param methodName
     * @param paramsRequest
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getCache(String methodName, AbstractRequest paramsRequest, Class<T> clazz) {
        return getCache(methodName, paramsRequest, clazz, true);
    }

    /**
     * 根据缓存方法名和参数获取缓存值
     * @param methodName
     * @param paramsRequest
     * @param clazz
     * @param useCache  是否使用缓存
     * @param <T>
     * @return
     */
    public static <T> T getCache(String methodName, AbstractRequest paramsRequest, Class<T> clazz, boolean useCache) {
        String cacheResult = getCacheResult(methodName, paramsRequest, useCache);
        return getObjectFromCamelJson(cacheResult, clazz);
    }

    public static <T> T getCache(Long dataSign, String methodName, String camelJson, Class<T> clazz, boolean useCache) {
        String cacheResult = getCacheResult(dataSign, methodName, camelJson, useCache);
        return getObjectFromCamelJson(cacheResult, clazz);
    }

    /**
     * 根据缓存方法名和参数获取列表类型的缓存值
     * @param methodName
     * @param paramsRequest
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> getListCache(String methodName, AbstractRequest paramsRequest, Class<T> clazz) {
        return getListCache(methodName, paramsRequest, clazz, true);
    }

    /**
     * 根据缓存方法名和参数获取列表类型的缓存值
     * @param methodName
     * @param paramsRequest
     * @param clazz
     * @param useCache  是否使用缓存
     * @param <T>
     * @return
     */
    public static <T> List<T> getListCache(String methodName, AbstractRequest paramsRequest, Class<T> clazz, boolean useCache) {
        String cacheResult = getCacheResult(methodName, paramsRequest, useCache);
        List<T> list = new ArrayList();
        JsonArray array = (new JsonParser()).parse(cacheResult).getAsJsonArray();
        Iterator iterator = array.iterator();

        while(iterator.hasNext()) {
            JsonElement elem = (JsonElement)iterator.next();
            list.add(gson.fromJson(elem, clazz));
        }

        return list;
    }

    // //////////////////////////////////////////////////////////////////////////

    /**
     * 缓存结果字符串转成bean
     * @param cacheResult
     * @param clazz
     * @param <T>
     * @return
     */
    private static <T> T getObjectFromCamelJson(String cacheResult, Class<T> clazz) {
        Map map;
        Iterator iterator;
        Entry entry;
        if (clazz.equals(String.class)) {
            map = (Map)gson.fromJson(cacheResult, Map.class);
            Assert.isTrue(!map.isEmpty());
            iterator = map.entrySet().iterator();
            if (iterator.hasNext()) {
                entry = (Entry)iterator.next();
                return (T)entry.getValue();
            }
        } else if (clazz.equals(Long.class)) {
            map = (Map)gson.fromJson(cacheResult, Map.class);
            Assert.isTrue(!map.isEmpty());
            iterator = map.entrySet().iterator();
            if (iterator.hasNext()) {
                entry = (Entry)iterator.next();
                Long value = ((Number) entry.getValue()).longValue();
                return (T)value;
            }
        }

        T obj = gson.fromJson(cacheResult, clazz);
        if (MessagePack.class.isAssignableFrom(clazz)) {
            Field field = ReflectionUtils.findField(clazz, "code");
            field.setAccessible(true);
            ReflectionUtils.setField(field, obj, 0L);
            field = ReflectionUtils.findField(clazz, "message");
            field.setAccessible(true);
            ReflectionUtils.setField(field, obj, "成功");
        }

        return obj;
    }

    private static String getCacheResult(String methodName, AbstractRequest paramsRequest, boolean useCache) {
        String cacheResult;
        if (useCache) {
            cacheResult = getCacheResultFromCache(methodName, paramsRequest);
        } else {
            if (log.isDebugEnabled()) {
                log.debug("getCache from db,methodName:{}", methodName);
            }

            cacheResult = getCacheResultFromDb(methodName, paramsRequest);
        }

        if (log.isDebugEnabled()) {
            log.debug("cacheResult:{}", cacheResult);
        }

        return cacheResult;
    }

    private static String getCacheResult(Long dataSign, String methodName, String camelJson, boolean useCache) {
        String cacheResult;
        if (useCache) {
            cacheResult = getCacheResultFromCache(dataSign, methodName, camelJson);
        } else {
            if (log.isDebugEnabled()) {
                log.debug("getCache from db,methodName:{}", methodName);
            }

            cacheResult = getCacheResultFromDb(dataSign, methodName, camelJson);
        }

        if (log.isDebugEnabled()) {
            log.debug("cacheResult:{}", cacheResult);
        }

        return cacheResult;
    }

    /**
     * 从缓存中获取
     * @param methodName
     * @param paramsRequest
     * @return
     */
    private static String getCacheResultFromCache(String methodName, AbstractRequest paramsRequest) {
        CacheKeyGenerateRule rule = getCacheKeyGenerateRule(paramsRequest.getDataSign(), methodName);
        String[] arrCacheKey = rule.getCacheMultiCol().split("#");
        StringBuilder sb = new StringBuilder(rule.getCacheMethod());
        String[] arr = arrCacheKey;
        int length = arrCacheKey.length;

        for(int i = 0; i < length; ++i) {
            String cacheCol = arr[i];

            Object obj;
            try {
                obj = getFieldValueInAllSuper(paramsRequest, cacheCol);
            } catch (IllegalAccessException | NoSuchFieldException e) {
                throw new ValidateBusinessException(SUB_SYSTEM, ExceptionType.VBE20030, "方法:" + methodName + ",生成缓存的主键所需栏位:" + cacheCol + ",类:" + paramsRequest.getClass().getName() + "无此属性!");
            }

            if (obj == null) {
                throw new ValidateBusinessException(SUB_SYSTEM, ExceptionType.VBE20030, "方法:" + methodName + ",生成缓存的主键所需栏位:" + cacheCol + "对象的值为null！");
            }

            sb.append("_").append(obj.toString());
        }

        String cacheKey = sb.toString();
        return doGetFromCache(cacheKey, methodName, paramsRequest);
    }

    /**
     * 从缓存中获取
     * @param dataSign
     * @param methodName
     * @param camelJson
     * @return
     */
    private static String getCacheResultFromCache(Long dataSign, String methodName, String camelJson) {
        CacheKeyGenerateRule rule = getCacheKeyGenerateRule(dataSign, methodName);
        Map<String, Object> map = (Map)gson.fromJson(camelJson, Map.class);
        String[] arrCacheKey = rule.getCacheMultiCol().split("#");
        StringBuilder sb = new StringBuilder(rule.getCacheMethod());
        String[] arr = arrCacheKey;
        int length = arrCacheKey.length;

        for(int i = 0; i < length; ++i) {
            String cacheCol = arr[i];
            Object obj = map.get(cacheCol);
            if (obj == null) {
                throw new ValidateBusinessException(SUB_SYSTEM, ExceptionType.VBE20030, "方法:" + methodName + ",生成缓存的主键所需栏位:" + cacheCol + "对象的值为null！");
            }

            sb.append("_").append(obj.toString());
        }

        String cacheKey = sb.toString();
        return doGetFromCache(dataSign, cacheKey, methodName, camelJson);
    }

    /**
     * 获取缓存key的生成规则
     * @param dataSign
     * @param methodName
     * @return
     */
    private static CacheKeyGenerateRule getCacheKeyGenerateRule(final Long dataSign, final String methodName) {
        try {
            String key = dataSign + "_" + methodName;
            if (CacheUtil.CacheUtilInnerClass.cacheStoreService == null) {
                throw new ValidateBusinessException(SUB_SYSTEM, ExceptionType.VBE20030, "缓存初始化还未完成,请稍等...");
            } else {
                CacheKeyGenerateRule rule = (CacheKeyGenerateRule)cache.get(key, new Callable<CacheKeyGenerateRule>() {
                    public CacheKeyGenerateRule call() throws Exception {
                        CacheKeyGenerateRuleByMethodNameGetRequest request = new CacheKeyGenerateRuleByMethodNameGetRequest();
                        request.setTenantNumId(0L);
                        request.setDataSign(dataSign);
                        request.setMethodName(methodName);
                        CacheKeyGenerateRuleByMethodNameGetResponse response = CacheUtil.CacheUtilInnerClass.cacheStoreService.getCacheKeyGenerateRuleByMethodName(request);
                        ExceptionUtil.checkDubboException(response);
                        return response.getRule();
                    }
                });
                return rule;
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ValidateBusinessException(SUB_SYSTEM, ExceptionType.VBE20030, "未找方法:" + methodName + "缓存的主键生成规则");
        }
    }

    private static String doGetFromCache(Long dataSign, String cacheKey, String methodName, String camelJson) {
        String cacheResult = (String)CacheUtil.CacheUtilInnerClass.stringRedisTemplate.opsForValue().get(cacheKey);
        if (cacheResult != null) {
            return cacheResult;
        } else {
            // 从缓存中获取 取不到值 再从数据库中获取
            cacheResult = getCacheResultFromDb(dataSign, methodName, camelJson);
            return cacheResult;
        }
    }

    /**
     * 从数据库读取值
     * @param dataSign
     * @param methodName
     * @param camelJson
     * @return
     */
    private static String getCacheResultFromDb(Long dataSign, String methodName, String camelJson) {
        CacheGetRequest request = new CacheGetRequest();
        request.setTenantNumId(0L);
        request.setDataSign(dataSign);
        request.setMethodName(methodName);
        request.setParams(camelJson);
        CacheGetResponse response = CacheUtil.CacheUtilInnerClass.cacheStoreService.getCache(request);
        ExceptionUtil.checkDubboException(response);
        String cacheResult = response.getCacheResult();
        return cacheResult;
    }

    private static String doGetFromCache(String cacheKey, String methodName, AbstractRequest paramsRequest) {
        String camelJson = gson.toJson(paramsRequest);
        return doGetFromCache(paramsRequest.getDataSign(), cacheKey, methodName, camelJson);
    }

    /**
     * 从数据库读取值
     * @param methodName
     * @param paramsRequest
     * @return
     */
    private static String getCacheResultFromDb(String methodName, AbstractRequest paramsRequest) {
        String camelJson = gson.toJson(paramsRequest);
        return getCacheResultFromDb(paramsRequest.getDataSign(), methodName, camelJson);
    }

    /**
     * 获取指定对象的指定字段的值
     * @param object
     * @param propertyName
     * @return
     * @throws IllegalAccessException
     * @throws NoSuchFieldException
     */
    private static Object getFieldValueInAllSuper(Object object, String propertyName) throws IllegalAccessException, NoSuchFieldException {
        Class<?> claszz = object.getClass();
        Field field = null;

        do {
            try {
                field = claszz.getDeclaredField(propertyName);
            } catch (NoSuchFieldException e) {
                field = null;
            }

            claszz = claszz.getSuperclass();
        } while(field == null && claszz != null);

        if (field == null) {
            throw new NoSuchFieldException("类:" + object.getClass().getName() + "无指定的属性:" + propertyName);
        } else {
            field.setAccessible(true);
            return field.get(object);
        }
    }

}


package com.github.bluecatlee.gs4d.cache.service.impl;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.alibaba.druid.stat.TableStat.Name;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.rpc.RpcContext;
import com.fasterxml.jackson.databind.PropertyNamingStrategy.LowerCaseStrategy;
import com.github.bluecatlee.gs4d.cache.constant.Constants;
import com.github.bluecatlee.gs4d.cache.dao.EcCacheMethodSchemaDefineDao;
import com.github.bluecatlee.gs4d.cache.dao.EcCommonCacheDependenceDao;
import com.github.bluecatlee.gs4d.cache.entity.EcCacheMethodSchemaDefine;
import com.github.bluecatlee.gs4d.cache.entity.EcCommonCacheDependence;
import com.github.bluecatlee.gs4d.cache.api.model.CacheKeyGenerateRule;
import com.github.bluecatlee.gs4d.cache.model.SchemaDefineAndTableNameList;
import com.github.bluecatlee.gs4d.cache.api.request.CacheDeleteRequest;
import com.github.bluecatlee.gs4d.cache.api.request.CacheGetRequest;
import com.github.bluecatlee.gs4d.cache.api.request.CacheKeyGenerateRuleByMethodNameGetRequest;
import com.github.bluecatlee.gs4d.cache.api.request.CacheKeyGenerateRuleBySubSystemGetRequest;
import com.github.bluecatlee.gs4d.cache.api.response.CacheDeleteResponse;
import com.github.bluecatlee.gs4d.cache.api.response.CacheGetResponse;
import com.github.bluecatlee.gs4d.cache.api.response.CacheKeyGenerateRuleByMethodNameGetResponse;
import com.github.bluecatlee.gs4d.cache.api.response.CacheKeyGenerateRuleBySubSystemGetResponse;
import com.github.bluecatlee.gs4d.cache.service.CacheCommonService;
import com.github.bluecatlee.gs4d.cache.api.service.CacheStoreService;
import com.github.bluecatlee.gs4d.common.exception.BusinessException;
import com.github.bluecatlee.gs4d.common.exception.ExceptionType;
import com.github.bluecatlee.gs4d.common.exception.ValidateBusinessException;
import com.github.bluecatlee.gs4d.common.exception.ValidateClientException;
import com.github.bluecatlee.gs4d.common.utils.*;
import com.github.bluecatlee.gs4d.common.utils.lock.RedisLock;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service("cacheStoreService")
public class CacheStoreServiceImpl implements CacheStoreService {
    private static final Logger logger = LoggerFactory.getLogger(CacheStoreServiceImpl.class);
    private static MyJsonMapper nonEmptyMapper = MyJsonMapper.nonEmptyMapper();
    private static MyJsonMapper nonDefaultMapper = MyJsonMapper.nonDefaultMapper();

    public static ThreadPoolExecutor executor;

    static {
        executor = new ThreadPoolExecutor(10, 30, 60L, TimeUnit.SECONDS, new LinkedBlockingDeque(500), new ThreadPoolExecutor.DiscardPolicy());
    }

    static {
        nonEmptyMapper.getMapper().setPropertyNamingStrategy(LowerCaseStrategy.SNAKE_CASE);
    }

    @Value("${schema.define.primary.key.cache.sign:true}")
    private boolean schemaDefinePrimaryKeyCacheSign;
    @Value("${special.group.name:}")
    private String specialGroupName;

    @Resource(name = "stringRedisTemplate")
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private EcCacheMethodSchemaDefineDao ecCacheMethodSchemaDefineDao;
    @Resource
    private EcCommonCacheDependenceDao ecCommonCacheDependenceDao;

    @Resource
    private CacheCommonService cacheCommonService;

    public CacheStoreServiceImpl() {
    }

    public CacheGetResponse getCache(CacheGetRequest request) {
        if (logger.isDebugEnabled()) {
            logger.debug("begin getCache request:{}", nonEmptyMapper.toJson(request));
        }

        CacheGetResponse response = new CacheGetResponse();

        try {
            request.validate(Constants.SUB_SYSTEM, ExceptionType.VCE10030);
            String result = this.getCache(request.getDataSign(), request.getMethodName(), request.getParams());
            response.setCacheResult(result);
        } catch (Exception e) {
            if (e instanceof BusinessException && ExceptionType.BE40071.getCode() == ((BusinessException)e).getCode()) {
                logger.error("????????????????????????????????????????????????,message:{}", ((BusinessException)e).getMessage());
                response.setCode(ExceptionType.BE40071.getCode());
                response.setMessage(((BusinessException)e).getMessage());
            } else {
                ExceptionUtil.processException(e, response);
            }
        }

        if (logger.isDebugEnabled()) {
            logger.debug("end getCache response:{}", response.toLowerCaseJson());
        }

        return response;
    }

    private String getCache(final Long dataSign, final String methodName, String params) {
        final EcCacheMethodSchemaDefine ecCacheMethodSchemaDefine;
        List tableNameList;
        if (this.schemaDefinePrimaryKeyCacheSign) {
            String prefix = Constants.SCHEMA_PREFIX;
            String schemaDefineAndTableNameListKey = prefix + "_" + methodName;
            String value = (String)this.stringRedisTemplate.opsForValue().get(schemaDefineAndTableNameListKey);
            SchemaDefineAndTableNameList schemaDefineAndTableNameList;
            if (value != null) {
                schemaDefineAndTableNameList = (SchemaDefineAndTableNameList)nonEmptyMapper.fromJson(value, SchemaDefineAndTableNameList.class);
                ecCacheMethodSchemaDefine = schemaDefineAndTableNameList.getSchemaDefine();
                tableNameList = schemaDefineAndTableNameList.getTableNameList();
            } else {
                ecCacheMethodSchemaDefine = this.ecCacheMethodSchemaDefineDao.queryCacheMethodSchemaDefine(methodName);
                tableNameList = this.parseTableNames(ecCacheMethodSchemaDefine.getMETHOD_NAME(), ecCacheMethodSchemaDefine.getSQL_CONTENT());
                schemaDefineAndTableNameList = new SchemaDefineAndTableNameList();
                schemaDefineAndTableNameList.setSchemaDefine(ecCacheMethodSchemaDefine);
                schemaDefineAndTableNameList.setTableNameList(tableNameList);
                value = nonEmptyMapper.toJson(schemaDefineAndTableNameList);
                this.stringRedisTemplate.opsForValue().set(schemaDefineAndTableNameListKey, value);
                this.stringRedisTemplate.expire(schemaDefineAndTableNameListKey, 1L, TimeUnit.DAYS);
                if (logger.isDebugEnabled()) {
                    logger.debug("get cache schema define and table name list from db");
                }
            }
        } else {
            ecCacheMethodSchemaDefine = this.ecCacheMethodSchemaDefineDao.queryCacheMethodSchemaDefine(methodName);
            tableNameList = this.parseTableNames(ecCacheMethodSchemaDefine.getMETHOD_NAME(), ecCacheMethodSchemaDefine.getSQL_CONTENT());
        }

        Map paramMap = (Map)nonDefaultMapper.fromJson(params, Map.class);
        if (!paramMap.containsKey("dataSign")) {
            throw new ValidateClientException(Constants.SUB_SYSTEM, ExceptionType.VCE10030, "???????????????????????????!");
        } else {
            Long dataSignParam = ((Number)paramMap.get("dataSign")).longValue();
            if (!dataSign.equals(dataSignParam)) {
                throw new ValidateClientException(Constants.SUB_SYSTEM, ExceptionType.VCE10030, "??????????????????????????????!");
            } else {
                String[] cacheMultiCols = ecCacheMethodSchemaDefine.getCACHE_MULTI_COL().split("#");
                StringBuilder stringBuilder = new StringBuilder(ecCacheMethodSchemaDefine.getCACHE_METHOD());
                String[] cacheMultiColArr = cacheMultiCols;
                int cacheMultiColCount = cacheMultiCols.length;

                for(int i = 0; i < cacheMultiColCount; ++i) {
                    String col = cacheMultiColArr[i];
                    Object colValue = paramMap.get(col);
                    if (colValue == null) {
                        throw new ValidateBusinessException(Constants.SUB_SYSTEM, ExceptionType.VBE20030, "??????????????????????????????,?????????:" + methodName + ",??????:" + col);
                    }

                    stringBuilder.append("_").append(colValue.toString());
                }

                final String cacheKey = stringBuilder.toString();
                JdbcTemplate jdbcTemplate = this.cacheCommonService.getJdbcTemplate(ecCacheMethodSchemaDefine.getDB());
                NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
                List<Map<String, Object>> results = namedParameterJdbcTemplate.queryForList(ecCacheMethodSchemaDefine.getSQL_CONTENT(), paramMap);
                String result;
                if (results.isEmpty()) {
                    if (ecCacheMethodSchemaDefine.getLIST_SIGN().equals(1L) && ecCacheMethodSchemaDefine.getALLOW_LIST_EMPTY_SIGN().equals(1L) && ecCacheMethodSchemaDefine.getTTL() > 0L) {
                        // ????????????list?????????????????? ????????????[]
                        result = "[]";
                        this.stringRedisTemplate.opsForValue().set(cacheKey, result);
                        this.stringRedisTemplate.expire(cacheKey, ecCacheMethodSchemaDefine.getTTL(), TimeUnit.SECONDS);
                        return result;
                    } else if (StringUtil.isNullOrBlank(ecCacheMethodSchemaDefine.getDESCRIPTION())) {
                        throw new BusinessException(Constants.SUB_SYSTEM, ExceptionType.BE40071, "??????????????????????????????,??????????????????????????????,?????????:" + methodName + ",??????:" + params);
                    } else {
                        throw new BusinessException(Constants.SUB_SYSTEM, ExceptionType.BE40071, ecCacheMethodSchemaDefine.getDESCRIPTION() + "??????,?????????:" + methodName + ",??????:" + params);
                    }
                } else if (!ecCacheMethodSchemaDefine.getLIST_SIGN().equals(1L) && results.size() > 1) {
                    if (StringUtil.isNullOrBlank(ecCacheMethodSchemaDefine.getDESCRIPTION())) {
                        throw new BusinessException(Constants.SUB_SYSTEM, ExceptionType.BE40072, "??????????????????????????????,?????????????????????????????????,?????????:" + methodName + ",??????:" + params);
                    } else {
                        throw new BusinessException(Constants.SUB_SYSTEM, ExceptionType.BE40072, "??????????????????????????????,?????????????????????????????????,?????????:" + methodName + ecCacheMethodSchemaDefine.getDESCRIPTION() + ",??????:" + params);
                    }
                } else {
                    final Long tenantNumId;
                    if (paramMap.containsKey("tenantNumId")) {
                        tenantNumId = ((Number)paramMap.get("tenantNumId")).longValue();
                    } else {
                        tenantNumId = 0L;
                    }

                    final List<EcCommonCacheDependence> commonCacheDependenceList = new ArrayList();

                    List multiColumnResMapList = new ArrayList(results.size()); // ?????????????????????????????????list (???????????????????????????????????????)

                    boolean singleColumnList = false;                       // ?????????????????? ??????list
                    List columnValues = new ArrayList(results.size());      // ????????????????????????list (?????????????????????????????????)

//                    boolean var20 = false;
                    Iterator resultsIterator = results.iterator();

                    while(resultsIterator.hasNext()) {
                        Map<String, Object> resMap = (Map)resultsIterator.next();

                        HashMap paramMap_ = new HashMap();
                        Iterator paramMapKeyIterator = paramMap.keySet().iterator();

                        while(paramMapKeyIterator.hasNext()) {
                            String paramMapKey = (String)paramMapKeyIterator.next();                    // ?????? (??????????????????????????????????????????)
                            String paramMapKey_ = CamelUnderlineUtil.camelToUnderline(paramMapKey);     // ???????????????
                            paramMap_.put(paramMapKey_, paramMap.get(paramMapKey));
                        }
                        paramMap_.putAll(resMap);   // ?????????????????????????????????map???  ??????????????????map??????key???????????????????????????
                                                    // ???????????????????????????map  ??????????????????????????????????????????(series??????)

                        boolean haveReferenceTables = tableNameList.size() > 1;                      // ???????????????????????????
                        Set<String> referenceTableSeriesSet = new HashSet(tableNameList.size());     // ????????????series???????????????????????? ?????????????????? ?????????????????????????????????
                        Iterator tableNameListIterator = tableNameList.iterator();

                        String columnName;        // ??????
                        Object columnValue;       // ??????
                        String columnValueStr;    // ??????????????????

                        // ?????????????????????
                        while(tableNameListIterator.hasNext()) {
                            String tableName = (String)tableNameListIterator.next();
                            if (haveReferenceTables) {
                                columnName = tableName + "_series";
                            } else {
                                columnName = "series";
                            }

                            referenceTableSeriesSet.add(columnName);
                            columnValue = paramMap_.get(columnName);
                            if (columnValue == null) {
                                logger.info("????????????????????????????????????????????????,???:" + tableName + ",????????????:" + columnName + "methodName:" + methodName + "params:" + params);
                            } else {
                                columnValueStr = columnValue.toString();
                                EcCommonCacheDependence commonCacheDependence = new EcCommonCacheDependence();
                                commonCacheDependence.setTENANT_NUM_ID(tenantNumId);
                                commonCacheDependence.setDATA_SIGN(dataSignParam);
                                commonCacheDependence.setMETHOD_NAME(methodName);
                                commonCacheDependence.setDB(ecCacheMethodSchemaDefine.getDB());
                                commonCacheDependence.setCACHE_KEY(cacheKey);
                                commonCacheDependence.setPARAMS(params);
                                commonCacheDependence.setTABLE_NAME(tableName);
                                commonCacheDependence.setTABLE_SERIES(columnValueStr);
                                commonCacheDependence.setDATA_SIGN(dataSign);
                                commonCacheDependence.setCREATE_USER_ID(1L);
                                commonCacheDependence.setLAST_UPDATE_USER_ID(1L);
                                commonCacheDependence.setDUBBO_GROUP(this.getCurrentThreadDubboGroup());
                                commonCacheDependenceList.add(commonCacheDependence);
                            }
                        }

                        HashMap resMapCamel = new HashMap(resMap.size());           // ????????????????????????????????????map ?????????????????????
                        Iterator resMapKeyIterator = resMap.keySet().iterator();

                        while(resMapKeyIterator.hasNext()) {
                            columnName = (String)resMapKeyIterator.next();
                            if (!referenceTableSeriesSet.contains(columnName)) {
                                // ????????????
                                columnValue = resMap.get(columnName);
                                String columnNameCamel = CamelUnderlineUtil.underlineToCamel(columnName);
                                resMapCamel.put(columnNameCamel, columnValue);
                            }
                        }

//                        if (!var20) {
                            if (ecCacheMethodSchemaDefine.getLIST_SIGN().equals(1L) && resMapCamel.size() == 1) {
                                singleColumnList = true;
                            }

//                            var20 = true;
//                        }

                        if (singleColumnList) {
                            resMapKeyIterator = resMapCamel.entrySet().iterator();
                            if (resMapKeyIterator.hasNext()) {
                                Entry<String, Object> entry = (Entry)resMapKeyIterator.next();
                                columnValue = entry.getValue();
                                columnValues.add(columnValue);
                            }
                        } else {
                            multiColumnResMapList.add(resMapCamel);
                        }
                    }

                    final String dubboGroup = this.getCurrentThreadDubboGroup();
                    executor.execute(new Runnable() {
                        public void run() {
                            CacheStoreServiceImpl.this.refresh(methodName, cacheKey, tenantNumId, dataSign, ecCacheMethodSchemaDefine, commonCacheDependenceList, dubboGroup);
                        }
                    });

                    if (singleColumnList) {
                        result = nonEmptyMapper.toJson(columnValues);
                    } else if (ecCacheMethodSchemaDefine.getLIST_SIGN().equals(1L)) {
                        result = nonEmptyMapper.toJson(multiColumnResMapList);
                    } else {
                        result = nonEmptyMapper.toJson(multiColumnResMapList.get(0));
                    }

                    this.stringRedisTemplate.opsForValue().set(cacheKey, result);
                    if (ecCacheMethodSchemaDefine.getTTL() > 0L) {
                        this.stringRedisTemplate.expire(cacheKey, ecCacheMethodSchemaDefine.getTTL(), TimeUnit.SECONDS);
                    }

                    return result;
                }
            }
        }
    }

    /**
     * ?????????????????????dubbo??????
     * @return
     */
    private String getCurrentThreadDubboGroup() {
        URL url = RpcContext.getContext().getUrl();
        Map map = url.toMap();
        String group = (String)map.get("group");
        if (group == null || group.trim().equals("")) {
            group = "";
        }

        if (logger.isDebugEnabled()) {
            logger.debug("???dubbo URL????????????dubbo??????group:{}", group);
        }

        return group;
    }

    public boolean refresh(String methodName, String cacheKey, Long tenantNumId, Long dataSign, EcCacheMethodSchemaDefine ecCacheMethodSchemaDefine, List<EcCommonCacheDependence> commonCacheDependenceList, String dubboGroup) {
        long startRefreshTime = System.currentTimeMillis();
        RedisLock redisLock = null;
        boolean result = true;

        try {
            String lockKey = methodName + "_" + cacheKey;
            redisLock = new RedisLock(this.stringRedisTemplate, lockKey, 120);
            if (!redisLock.lock()) {
                throw new BusinessException(Constants.SUB_SYSTEM, ExceptionType.VBE20030, "distributedLockKey???" + lockKey + "???????????????...");
            }

            List<String> seriesList = this.ecCommonCacheDependenceDao.querySeriesByMethodNameAndCacheKeyAndDubboGroup(tenantNumId, dataSign, methodName, cacheKey, dubboGroup);
            if (CollectionUtils.isNotEmpty(seriesList)) {
                Iterator iterator = seriesList.iterator();

                while(iterator.hasNext()) {
                    String series = (String)iterator.next();
                    this.ecCommonCacheDependenceDao.deleteByMethodNameAndCacheKeyAndSeries(tenantNumId, dataSign, methodName, cacheKey, series);
                }
            }

            if (System.currentTimeMillis() - startRefreshTime > 1000L) {
                logger.info("dealCacheMethodDependence ???????????? cacheKey:{},methodName:{},cost:{}", new Object[]{cacheKey, methodName, System.currentTimeMillis() - startRefreshTime});
            }

            long startInsertTime = System.currentTimeMillis();
            this.ecCommonCacheDependenceDao.batchInsert(commonCacheDependenceList);
            if (System.currentTimeMillis() - startInsertTime > 1000L) {
                logger.info("dealCacheMethodDependence ?????????????????? ec_common_cache_dependence cacheKey:{},methodName:{},cost:{}", new Object[]{cacheKey, methodName, System.currentTimeMillis() - startInsertTime});
            }
        } catch (Exception e) {
            logger.error("dealCacheMethodDependence cacheKey:{},methodName:{},msg:{}???", new Object[]{cacheKey, methodName, e});
            result = false;
        } finally {
            if (redisLock != null) {
                redisLock.unlock();
            }

        }

        return result;
    }

    /**
     * ??????????????????????????????key???????????????
     * @param request
     * @return
     */
    public CacheKeyGenerateRuleBySubSystemGetResponse getCacheKeyGenerateRuleBySubSystem(CacheKeyGenerateRuleBySubSystemGetRequest request) {
        if (logger.isDebugEnabled()) {
            logger.debug("begin getCacheKeyGenerateRuleBySubSystem request:{}", nonEmptyMapper.toJson(request));
        }

        CacheKeyGenerateRuleBySubSystemGetResponse response = new CacheKeyGenerateRuleBySubSystemGetResponse();

        try {
            request.validate(Constants.SUB_SYSTEM, ExceptionType.VCE10030);
            List results = this.ecCacheMethodSchemaDefineDao.queryCacheMethodSchemaDefinesBySubSystem(request.getSubSystem());
            List list = new ArrayList(results.size());
            Iterator iterator = results.iterator();

            while(iterator.hasNext()) {
                EcCacheMethodSchemaDefine ecCacheMethodSchemaDefine = (EcCacheMethodSchemaDefine)iterator.next();
                CacheKeyGenerateRule rule = new CacheKeyGenerateRule();
                rule.setCacheMethod(ecCacheMethodSchemaDefine.getCACHE_METHOD());
                rule.setCacheMultiCol(ecCacheMethodSchemaDefine.getCACHE_MULTI_COL());
                rule.setMethodName(ecCacheMethodSchemaDefine.getMETHOD_NAME());
                list.add(rule);
            }

            response.setRules(list);
        } catch (Exception e) {
            ExceptionUtil.processException(e, response);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("end getCacheKeyGenerateRuleBySubSystem response:{}", response.toLowerCaseJson());
        }

        return response;
    }

    /**
     * ???????????????????????????key???????????????
     * @param request
     * @return
     */
    public CacheKeyGenerateRuleByMethodNameGetResponse getCacheKeyGenerateRuleByMethodName(CacheKeyGenerateRuleByMethodNameGetRequest request) {
        if (logger.isDebugEnabled()) {
            logger.debug("begin getCacheKeyGenerateRuleByMethodName request:{}", nonEmptyMapper.toJson(request));
        }

        CacheKeyGenerateRuleByMethodNameGetResponse response = new CacheKeyGenerateRuleByMethodNameGetResponse();

        try {
            request.validate(Constants.SUB_SYSTEM, ExceptionType.VCE10030);
            EcCacheMethodSchemaDefine cacheMethodSchemaDefine = this.ecCacheMethodSchemaDefineDao.queryCacheMethodSchemaDefine(request.getMethodName());
            CacheKeyGenerateRule rule = new CacheKeyGenerateRule();
            rule.setCacheMethod(cacheMethodSchemaDefine.getCACHE_METHOD());
            rule.setCacheMultiCol(cacheMethodSchemaDefine.getCACHE_MULTI_COL());
            rule.setMethodName(cacheMethodSchemaDefine.getMETHOD_NAME());
            response.setRule(rule);
        } catch (Exception e) {
            ExceptionUtil.processException(e, response);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("end getCacheKeyGenerateRuleByMethodName response:{}", response.toLowerCaseJson());
        }

        return response;
    }

    /**
     * ????????????
     * @param request
     * @return
     */
    public CacheDeleteResponse deleteCache(CacheDeleteRequest request) {
        if (logger.isDebugEnabled()) {
            logger.debug("begin deleteCache request:{}", nonEmptyMapper.toJson(request));
        }

        CacheDeleteResponse response = new CacheDeleteResponse();

        try {
            request.validate(Constants.SUB_SYSTEM, ExceptionType.VCE10030);
            String cacheKey = request.getCacheKey();
            Object[] keyValues = request.getKeyValues();
            if (keyValues != null) {
                Object[] arr = keyValues;
                int length = keyValues.length;

                for(int i = 0; i < length; ++i) {
                    Object suffix = arr[i];
                    cacheKey = cacheKey + "_" + suffix;
                }
            }

            Set keySet = this.stringRedisTemplate.keys(cacheKey);
            this.stringRedisTemplate.delete(keySet);
        } catch (Exception e) {
            ExceptionUtil.processException(e, response);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("end deleteCache response:{}", response.toLowerCaseJson());
        }

        return response;
    }

    /**
     * ???sql??????????????????
     * @param methodName
     * @param sqlContent
     * @return
     */
    private List<String> parseTableNames(String methodName, String sqlContent) {
        List sqlStatements = SQLUtils.parseStatements(sqlContent, "mysql");
        if (sqlStatements.isEmpty()) {
            throw new ValidateBusinessException(Constants.SUB_SYSTEM, ExceptionType.VBE20030, "?????????????????????sql????????????????????????,?????????:" + methodName + "sql??????:" + sqlContent);
        } else if (sqlStatements.size() > 1) {
            throw new ValidateBusinessException(Constants.SUB_SYSTEM, ExceptionType.VBE20030, "???????????????sql??????????????????????????????????????????sql????????????,?????????:" + methodName + "sql??????:" + sqlContent);
        } else {
            MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
            ((SQLStatement)sqlStatements.get(0)).accept(visitor);
            return (List)visitor.getTables().keySet().stream().map(Name::getName).collect(Collectors.toList());
        }
    }

}

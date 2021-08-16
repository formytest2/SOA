package com.github.bluecatlee.gs4d.export.service.impl;

import com.github.bluecatlee.gs4d.cache.api.CacheUtil;
import com.github.bluecatlee.gs4d.common.datasource.DataSourceContextHolder;
import com.github.bluecatlee.gs4d.common.exception.DatabaseOperateException;
import com.github.bluecatlee.gs4d.common.exception.ExceptionType;
import com.github.bluecatlee.gs4d.common.utils.CamelUnderlineUtil;
import com.github.bluecatlee.gs4d.common.utils.StringUtil;
import com.github.bluecatlee.gs4d.exchange.model.CommonQuery;
import com.github.bluecatlee.gs4d.exchange.model.ExcuteSqlResultModel;
import com.github.bluecatlee.gs4d.exchange.utils.CommUtil;
import com.github.bluecatlee.gs4d.exchange.utils.JsonObjectUtil;
import com.github.bluecatlee.gs4d.export.constant.Constants;
import com.github.bluecatlee.gs4d.export.model.BatchExcuteSqlModel;
import com.github.bluecatlee.gs4d.export.request.CommonQueryGetRequest;
import com.github.bluecatlee.gs4d.export.service.CommonJsonQueryService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service("exportCommonJsonQueryService")
public class CommonJsonQueryServiceImpl implements CommonJsonQueryService {

    private static Logger logger = LoggerFactory.getLogger(CommonJsonQueryServiceImpl.class);

    @Resource(name = "dynamicJdbcTemplate")
    private JdbcTemplate dynamicJdbcTemplate;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Value("#{settings['offline.sign']}")
    private Boolean offlineSign;

    private static Gson cP = (new GsonBuilder()).create();

    public ExcuteSqlResultModel excuteSqlById(Long currentPage, String dataSource, CommonQuery commonQuery, JSONObject inputParam, Long tenantNumId, Long dataSign) {
        if (StringUtil.isNullOrBlankTrim(dataSource)) {
            dataSource = commonQuery.getJdbcName();
        }

        if (!commonQuery.getCacheSign().equals(0L) && StringUtil.isNullOrBlank(commonQuery.getMethodName())) {
            commonQuery.setMethodName(commonQuery.getSqlId());
        }

        ExcuteSqlResultModel excuteSqlResultModel = new ExcuteSqlResultModel();
        ArrayList<JSONObject> list = null;
        if (commonQuery.getCacheSign().equals(2L)) {    // 使用缓存服务
            list = new ArrayList();
            JSONObject jsonObject = CacheUtil.getCache(commonQuery.getDataSign(), commonQuery.getMethodName(), CamelUnderlineUtil.underlineToCamel(cP.toJson(inputParam)), JSONObject.class, true);
            list.add(jsonObject);
            excuteSqlResultModel.setData(list);
            excuteSqlResultModel.setRecordCount((long)list.size());
            excuteSqlResultModel.setCount((long)list.size());
            return excuteSqlResultModel;
        } else {
            DataSourceContextHolder.setDataSourceType(dataSource);
            String paramContent = commonQuery.getParamContent();
            JSONArray jsonArray = JSONArray.fromObject(paramContent);
            Map paramMap = CommUtil.getParamsWithMap(commonQuery, jsonArray, inputParam);
            String sqlContent = commonQuery.getSqlContent();
            sqlContent = CommUtil.sqlHandler(sqlContent, paramMap);
            Long pageSize = commonQuery.getPageSize();
            excuteSqlResultModel.setSqlFlag(commonQuery.getSqlFlag());
            if (currentPage > 0L && pageSize > 0L && CommonQuery.SQL_FLAG_SELECT.equals(commonQuery.getSqlFlag())) {    // 分页查询 注意currentPage是从1开始的
                long count = 0L;         // 数据行数
                long pageCount = 1L;     // 总页数
                String selectCountSql = "select count(1) CT from (" + sqlContent + ") t";
                List results = this.query(commonQuery, selectCountSql, paramMap, excuteSqlResultModel, false);
                Map result = (Map)results.get(0);  // 查count只会有一条
                count = Long.parseLong(result.get("CT").toString());
                pageCount = (count - 1L) / pageSize + 1L;
                excuteSqlResultModel.setRecordCount(count);
                excuteSqlResultModel.setPageCount(pageCount);
                if (count == 0L) {
                    excuteSqlResultModel.setData(results);
                    return excuteSqlResultModel;    // 无数据 直接返回
                }

                long start = (currentPage - 1L) * pageSize + 1L;
                long end = currentPage * pageSize;
                if ("MYSQL".equals(commonQuery.getDbType().toUpperCase())) {
                    sqlContent = sqlContent + " limit " + (start - 1L) + "," + pageSize;  // limit是从0开始的
                } else {  // Oracle
                    sqlContent = "select * from (select a.*, rownum as rnum from (" + sqlContent + ") a where rownum <= " + end + ") where rnum >= " + start;
                }
            } else {
                excuteSqlResultModel.setPageCount(1L);  // 查询全部 则总页数为1
            }

            String annotatePrefix = commonQuery.getAnnotatePrefix();
            if (StringUtil.isAllNotNullOrBlank(new String[]{annotatePrefix}) && !this.offlineSign) {  // 离线环境是单库 不需要annotatePrefix
                sqlContent = annotatePrefix + sqlContent;
            }

            if (CommonQuery.SQL_FLAG_SELECT.equals(commonQuery.getSqlFlag())) {     // 查询
                List results = this.query(commonQuery, sqlContent, paramMap, excuteSqlResultModel, true);
                excuteSqlResultModel.setData(results);
                excuteSqlResultModel.setCount((long)results.size());
                if (currentPage <= 0L || pageSize <= 0L) {
                    excuteSqlResultModel.setRecordCount((long)results.size());
                }
            } else {
                int count = this.update(commonQuery, sqlContent, paramMap, excuteSqlResultModel);
                excuteSqlResultModel.setRecordCount((long)count);
            }

            if (excuteSqlResultModel.getRecordCount() == 0L) {
                excuteSqlResultModel.setPageCount(0L);
            }

            if (StringUtil.isAllNotNullOrBlank(new String[]{commonQuery.getReturnHandleContent()})) {
                String[] returnHandleContents = commonQuery.getReturnHandleContent().split(",");    // 需要把值转成字符串的字段

                for(int i = 0; i < returnHandleContents.length; ++i) {
                    String field = returnHandleContents[i];

                    for(int j = 0; j < excuteSqlResultModel.getData().size(); ++j) {
                        JSONObject jsonObject = (JSONObject)excuteSqlResultModel.getData().get(j);
                        Object value = jsonObject.get(field);
                        jsonObject.replace(field, value.toString());            // 值转成字符串
                    }
                }
            }

            return excuteSqlResultModel;
        }
    }

    private List query(CommonQuery commonQuery, String sqlContent, Map<String, Object> paramMap, ExcuteSqlResultModel excuteSqlResultModel, boolean useCache) {
        ArrayList valueList = new ArrayList();
        Object[] arr = paramMap.values().toArray();
        Object[] paramValues = arr;
        int paramCount = arr.length;

        for(int i = 0; i < paramCount; ++i) {
            Object paramValue = paramValues[i];
            if (paramValue != null) {
                valueList.add(paramValue);
            }
        }
        paramValues = valueList.toArray();

        sqlContent = sqlContent.replaceAll("[\\[\\]]*", "");
        excuteSqlResultModel.setArg(paramValues);
        excuteSqlResultModel.setSql(sqlContent);
        String[] selectFields = null;
        String key;

        if (commonQuery.getSubQuerySign().equals(2L)) { // 如果是子查询
            String sqlContentOrigin = sqlContent;
            sqlContent = sqlContent.toLowerCase();
            key = sqlContent.substring(sqlContent.indexOf("select") + 7, sqlContent.indexOf("from")); // 后面会去除空格
            selectFields = key.split(",");
            sqlContent = sqlContentOrigin;
        }

        List list;
        String resultJsonStr;
        try {
            if (commonQuery.getCacheSign() == null) {
                commonQuery.setCacheSign(0L);           // 默认不使用缓存
            }

            if (commonQuery.getCacheSign().equals(1L) && useCache) {
                if (logger.isDebugEnabled()) {
                    logger.info("use cache");
                }

                // todo 优先根据methodName查缓存(结合缓存服务)
                // 如果使用这种方式 注意缓存服务的参数列表与导出服务的参数列表得一致
//                String methodName = commonQuery.getMethodName();
//                CacheUtil.getCache()

                key = commonQuery.getSqlId();
                Object[] paramValues1 = paramValues;
                int paramCount1 = paramValues.length;

                for(int i = 0; i < paramCount1; ++i) {
                    Object paramValue1 = paramValues1[i];
                    key = key + "_" + paramValue1;
                }

                resultJsonStr = this.stringRedisTemplate.opsForValue().get(key);
                if (resultJsonStr == null) {
                    list = this.dynamicJdbcTemplate.queryForList(sqlContent, paramValues);
                    if (!list.isEmpty()) {
                        resultJsonStr = cP.toJson(list);
                        this.stringRedisTemplate.opsForValue().set(key, resultJsonStr, commonQuery.getCacheLiveTime(), TimeUnit.SECONDS);
                    }
                } else {
                    list = cP.fromJson(resultJsonStr, List.class);
                }
            } else {
                try {
                    list = this.dynamicJdbcTemplate.queryForList(sqlContent, paramValues);  // sql查询
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                    throw throwable;
                }
            }
        } catch (Exception e) {
            throw new DatabaseOperateException(Constants.SUB_SYSTEM, ExceptionType.DOE30051, "sqlId为" + commonQuery.getSqlId() + "执行sql语句异常" + e.getMessage() + ",入参为:" + JSONArray.fromObject(paramValues).toString());
        }

        if (!list.isEmpty()) {
            JSONArray jsonArray = JSONArray.fromObject(list, JsonObjectUtil.getJsonConfig());
            resultJsonStr = jsonArray.toString().replace(":null", ":\"\"");
            jsonArray = JSONArray.fromObject(resultJsonStr);
            list.clear();
            list = (List)JSONArray.toCollection(jsonArray, JSONObject.class);
        }

        if (list.isEmpty() && commonQuery.getSubQuerySign().equals(2L)) {
            HashMap map = null;

            for(int i = 0; i < selectFields.length; ++i) {
                map = new HashMap();
//                if (commonQuery.getSqlId().contains("APP-MESSAGE-CENTER")) {        // 特殊处理
//                    if (selectFields[i].indexOf(" as ") != -1) {
//                        map.put(selectFields[i].substring(selectFields[i].indexOf(" as ") + 3).trim(), (Object)null);
//                    } else {
//                        map.put(selectFields[i].trim(), (Object)null);
//                    }
//                } else {
                    if (selectFields[i].indexOf(" as ") != -1) {
                        map.put(selectFields[i].substring(selectFields[i].indexOf(" as ") + 3).trim(), "");     // 字段名转成别名
                    } else {
                        map.put(selectFields[i].trim(), "");
                    }
//                }
            }

            list.add(map);
        }

        return list;
    }

    private int update(CommonQuery commonQuery, String sqlContent, Map<String, Object> paramMap, ExcuteSqlResultModel excuteSqlResultModel) {
        ArrayList valueList = new ArrayList();
        Object[] arr = paramMap.values().toArray();
        Object[] paramValues = arr;
        int paramCount = arr.length;

        for(int i = 0; i < paramCount; ++i) {
            Object paramValue = paramValues[i];
            if (paramValue != null) {
                valueList.add(paramValue);
            }
        }

        paramValues = valueList.toArray();
        sqlContent = sqlContent.replaceAll("[\\[\\]]*", "");

        try {
            int count = this.dynamicJdbcTemplate.update(sqlContent, paramValues);
            return count;
        } catch (Exception e) {
            excuteSqlResultModel.setArg(paramValues);
            excuteSqlResultModel.setSql(sqlContent);
            throw new DatabaseOperateException(Constants.SUB_SYSTEM, ExceptionType.DOE30051, "sqlId为" + commonQuery.getSqlId() + "执行sql语句异常" + e.getMessage() + ",sql语句为:" + sqlContent + "入参为:" + JSONArray.fromObject(paramValues).toString());
        }
    }

//    public List excuteSqlById(String dataSource, CommonQuery commonQuery, List<Long> list, Long tenantNumId, Long dataSign) {
//        String sqlContent = commonQuery.getSqlContent();
//        sqlContent = sqlContent.replace("D.RESERVED_NO = ?", "");
//        sqlContent = sqlContent.replace("WM_BL_SHIP_DTL", " unitepos.WM_BL_SHIP_DTL ");
//        sqlContent = sqlContent.replace("WM_BL_SHIP_CONTAINER_HDR", " unitepos.WM_BL_SHIP_CONTAINER_HDR ");
//        sqlContent = sqlContent.replace("SD_BL_SO_TML_HDR", " unitepos.SD_BL_SO_TML_HDR ");
//        sqlContent = sqlContent.replace("SD_BL_SO_TML_DTL", " unitepos.SD_BL_SO_TML_DTL ");
//        sqlContent = sqlContent + "D.series in(select ITEMID from unitepos.abc)";
//        sqlContent = "select (select b.id from  unitepos.abc b where b.itemid=a.series) ZT_RESERVED_NO, a.* from (" + sqlContent + ") a ";
//        DataSourceContextHolder.clearDataSourceType();
//        DataSourceContextHolder.setDataSourceType(dataSource);
//        List results = this.dynamicJdbcTemplate.queryForList(sqlContent);
//        return results;
//    }

//    public List<Long> getSeries(String dataSource) {
//        DataSourceContextHolder.clearDataSourceType();
//        DataSourceContextHolder.setDataSourceType(dataSource);
//        List results = this.dynamicJdbcTemplate.queryForList("select ITEMID from unitepos.abc", Long.class);
//        return results;
//    }

    public CommonQuery getCommonQuery(Long tenantNumId, Long dataSign, String sqlId) {
        CommonQueryGetRequest commonQueryGetRequest = new CommonQueryGetRequest();
        commonQueryGetRequest.setTenantNumId(tenantNumId);
        commonQueryGetRequest.setDataSign(dataSign);
        commonQueryGetRequest.setSqlId(sqlId);
        CommonQuery commonQuery = CacheUtil.getCache("getCommonQueryBySqlId", commonQueryGetRequest, CommonQuery.class);
        return commonQuery;
    }

    public ExcuteSqlResultModel parseSqlById(Long currentPage, String dataSource, CommonQuery commonQuery, JSONObject inputParam, Long tenantNumId, Long dataSign) {
        ExcuteSqlResultModel excuteSqlResultModel = new ExcuteSqlResultModel();
        String sqlContent = commonQuery.getSqlContent();
        String paramContent = commonQuery.getParamContent();
        JSONArray ja = JSONArray.fromObject(paramContent);
        Map paramMap = CommUtil.getParamsWithMap(commonQuery, ja, inputParam);
        sqlContent = CommUtil.sqlHandler(sqlContent, paramMap);
        sqlContent = sqlContent.replaceAll("[\\[\\]]*", "");
        Long pageSize = commonQuery.getPageSize();
        excuteSqlResultModel.setSql(sqlContent);
        ArrayList valueList = new ArrayList();
        Object[] arr = paramMap.values().toArray();
        Object[] paramValues = arr;
        int paramCount = arr.length;

        for(int i = 0; i < paramCount; ++i) {
            Object paramValue = paramValues[i];
            valueList.add(paramValue);
        }

        paramValues = valueList.toArray();
        excuteSqlResultModel.setArg(paramValues);
        return excuteSqlResultModel;
    }

    public void batchExcuteSql(List<BatchExcuteSqlModel> list, String dataSource) {
        try {
            int size = list.size();

            for(int n = 0; n < size; ++n) {
                BatchExcuteSqlModel item = list.get(n);
                String sql = item.getSql();
                final List argList = item.getArgList();
                final List noDataUpdateArgList = item.getNoDataUpdateArgList();
                if (!StringUtil.isAllNullOrBlank(new String[]{sql}) && argList != null) {
                    int count;
                    if (item.hasNoDataUpdate()) {
                        for(int j = argList.size() - 1; j >= 0; --j) {
                            count = this.dynamicJdbcTemplate.update(sql, (Object[])argList.get(j));
                            if (count <= 0) {
                                this.dynamicJdbcTemplate.update(item.getNoDataUpdateSql(), (Object[])noDataUpdateArgList.get(j));
                            }
                        }
                    } else {
                        int[] bactchCount = this.dynamicJdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                            public void setValues(PreparedStatement ps, int i) throws SQLException {
                                Object[] args = (Object[])argList.get(i);

                                for(int j = 1; j <= args.length; ++j) {
                                    Object arg = args[j - 1];
                                    ps.setObject(j, arg);
                                }

                            }

                            public int getBatchSize() {
                                return argList.size();
                            }
                        });
                        if (item.hasNoDataUpdate() && noDataUpdateArgList != null && !noDataUpdateArgList.isEmpty()) {
                            for(count = bactchCount.length - 1; count >= 0; --count) {
                                int c = bactchCount[count];
                                if (c > 0) {
                                    noDataUpdateArgList.remove(count);
                                }
                            }

                            if (!noDataUpdateArgList.isEmpty()) {
                                sql = item.getNoDataUpdateSql();
                                this.dynamicJdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                                        Object[] args = (Object[])noDataUpdateArgList.get(i);

                                        for(int j = 1; j <= args.length; ++j) {
                                            Object arg = args[j - 1];
                                            ps.setObject(j, arg);
                                        }

                                    }

                                    public int getBatchSize() {
                                        return noDataUpdateArgList.size();
                                    }
                                });
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new DatabaseOperateException(Constants.SUB_SYSTEM, ExceptionType.DOE30052, "通用导入插入数据异常，异常信息：" + e.getMessage());
        }
    }
}


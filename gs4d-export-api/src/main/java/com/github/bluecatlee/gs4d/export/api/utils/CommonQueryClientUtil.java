package com.github.bluecatlee.gs4d.export.api.utils;

import com.github.bluecatlee.gs4d.common.exception.ExceptionType;
import com.github.bluecatlee.gs4d.common.exception.ValidateBusinessException;
import com.github.bluecatlee.gs4d.common.utils.ExceptionUtil;
import com.github.bluecatlee.gs4d.common.utils.MapBeanConvertUtil;
import com.github.bluecatlee.gs4d.export.api.response.CommonExcuteBySqlIdResponse;
import com.github.bluecatlee.gs4d.export.api.request.CommonExcuteBySqlIdRequest;
import com.github.bluecatlee.gs4d.export.api.service.ExportDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Lazy(false)
@Scope("singleton")
public class CommonQueryClientUtil {

    private static String SUB_SYSTEM = "export";
    private static ExportDataService innerExportDataService;

    public CommonQueryClientUtil() {
    }

    @Autowired(required = false)
    public void setExportDataService(ExportDataService exportDataService) {
        innerExportDataService = exportDataService;
    }

    public static <T> T commonQueryForObject(Long tenantNumId, Long dataSign, String sqlId, String paramKey, Object paramValue, Class<T> clazz) {
        return commonQueryForObject(tenantNumId, dataSign, sqlId, new String[]{paramKey}, new Object[]{paramValue}, clazz);
    }

    public static <T> T commonQueryForObject(Long tenantNumId, Long dataSign, String sqlId, String paramKey1, Object paramValue1, String paramKey2, Object paramValue2, Class<T> clazz) {
        return commonQueryForObject(tenantNumId, dataSign, sqlId, new String[]{paramKey1, paramKey2}, new Object[]{paramValue1, paramValue2}, clazz);
    }

    public static <T> T commonQueryForObject(Long tenantNumId, Long dataSign, String sqlId, String paramKey1, Object paramValue1, String paramKey2, Object paramValue2, String paramKey3, Object paramValue3, Class<T> clazz) {
        return commonQueryForObject(tenantNumId, dataSign, sqlId, new String[]{paramKey1, paramKey2, paramKey3}, new Object[]{paramValue1, paramValue2, paramValue3}, clazz);
    }

    public static <T> T commonQueryForObject(Long tenantNumId, Long dataSign, String sqlId, String paramKey1, Object paramValue1, String paramKey2, Object paramValue2, String paramKey3, Object paramValue3, String paramKey4, Object paramValue4, Class<T> clazz) {
        return commonQueryForObject(tenantNumId, dataSign, sqlId, new String[]{paramKey1, paramKey2, paramKey3, paramKey4}, new Object[]{paramValue1, paramValue2, paramValue3, paramValue4}, clazz);
    }

    public static <T> T commonQueryForObject(Long tenantNumId, Long dataSign, String sqlId, String paramKey1, Object paramValue1, String paramKey2, Object paramValue2, String paramKey3, Object paramValue3, String paramKey4, Object paramValue4, String paramKey5, Object paramValue5, Class<T> clazz) {
        return commonQueryForObject(tenantNumId, dataSign, sqlId, new String[]{paramKey1, paramKey2, paramKey3, paramKey4, paramKey5}, new Object[]{paramValue1, paramValue2, paramValue3, paramValue4, paramValue5}, clazz);
    }

    public static <T> T commonQueryForObject(Long tenantNumId, Long dataSign, String sqlId, String[] arrKey, Object[] arrValue, Class<T> clazz) {
        CommonExcuteBySqlIdResponse response = commonQuery(tenantNumId, dataSign, sqlId, arrKey, arrValue);
        List<Map<String, Object>> list = response.getResults();
        return list != null && !list.isEmpty() ? (T)MapBeanConvertUtil.map2Bean(clazz, (Map)list.get(0)) : null;
    }

    public static <T> List<T> commonQueryForList(Long tenantNumId, Long dataSign, String sqlId, String paramKey, Object paramValue, Class<T> clz) {
        return commonQueryForList(tenantNumId, dataSign, sqlId, new String[]{paramKey}, new Object[]{paramValue}, clz);
    }

    public static <T> List<T> commonQueryForList(Long tenantNumId, Long dataSign, String sqlId, String paramKey1, Object paramValue1, String paramKey2, Object paramValue2, Class<T> clz) {
        return commonQueryForList(tenantNumId, dataSign, sqlId, new String[]{paramKey1, paramKey2}, new Object[]{paramValue1, paramValue2}, clz);
    }

    public static <T> List<T> commonQueryForList(Long tenantNumId, Long dataSign, String sqlId, String paramKey1, Object paramValue1, String paramKey2, Object paramValue2, String paramKey3, Object paramValue3, Class<T> clz) {
        return commonQueryForList(tenantNumId, dataSign, sqlId, new String[]{paramKey1, paramKey2, paramKey3}, new Object[]{paramValue1, paramValue2, paramValue3}, clz);
    }

    public static <T> List<T> commonQueryForList(Long tenantNumId, Long dataSign, String sqlId, String paramKey1, Object paramValue1, String paramKey2, Object paramValue2, String paramKey3, Object paramValue3, String paramKey4, Object paramValue4, Class<T> clz) {
        return commonQueryForList(tenantNumId, dataSign, sqlId, new String[]{paramKey1, paramKey2, paramKey3, paramKey4}, new Object[]{paramValue1, paramValue2, paramValue3, paramValue4}, clz);
    }

    public static <T> List<T> commonQueryForList(Long tenantNumId, Long dataSign, String sqlId, String paramKey1, Object paramValue1, String paramKey2, Object paramValue2, String paramKey3, Object paramValue3, String paramKey4, Object paramValue4, String paramKey5, Object paramValue5, Class<T> clz) {
        return commonQueryForList(tenantNumId, dataSign, sqlId, new String[]{paramKey1, paramKey2, paramKey3, paramKey4, paramKey5}, new Object[]{paramValue1, paramValue2, paramValue3, paramValue4, paramValue5}, clz);
    }

    public static <T> List<T> commonQueryForList(Long tenantNumId, Long dataSign, String sqlId, String[] arrKey, Object[] arrValue, Class<T> clz) {
        CommonExcuteBySqlIdResponse response = commonQuery(tenantNumId, dataSign, sqlId, arrKey, arrValue);
        List<Map<String, Object>> list = response.getResults();
        return list != null && !list.isEmpty() ? MapBeanConvertUtil.map2BeanForList(clz, list) : null;
    }

    public static <T> List<T> commonQueryForPageList(Long tenantNumId, Long dataSign, String sqlId, Long pageNum, Long pageSize, String[] arrKey, Object[] arrValue, Class<T> clz) {
        CommonExcuteBySqlIdResponse response = pageCommonQuery(tenantNumId, dataSign, sqlId, pageNum, pageSize, arrKey, arrValue);
        List<Map<String, Object>> list = response.getResults();
        return list != null && !list.isEmpty() ? MapBeanConvertUtil.map2BeanForList(clz, list) : null;
    }

    public static Map<String, Object> commonQueryForMap(Long tenantNumId, Long dataSign, String sqlId, String paramKey, Object paramValue) {
        return commonQueryForMap(tenantNumId, dataSign, sqlId, new String[]{paramKey}, new Object[]{paramValue});
    }

    public static Map<String, Object> commonQueryForMap(Long tenantNumId, Long dataSign, String sqlId, String paramKey1, Object paramValue1, String paramKey2, Object paramValue2) {
        return commonQueryForMap(tenantNumId, dataSign, sqlId, new String[]{paramKey1, paramKey2}, new Object[]{paramValue1, paramValue2});
    }

    public static Map<String, Object> commonQueryForMap(Long tenantNumId, Long dataSign, String sqlId, String paramKey1, Object paramValue1, String paramKey2, Object paramValue2, String paramKey3, Object paramValue3) {
        return commonQueryForMap(tenantNumId, dataSign, sqlId, new String[]{paramKey1, paramKey2, paramKey3}, new Object[]{paramValue1, paramValue2, paramValue3});
    }

    public static Map<String, Object> commonQueryForMap(Long tenantNumId, Long dataSign, String sqlId, String paramKey1, Object paramValue1, String paramKey2, Object paramValue2, String paramKey3, Object paramValue3, String paramKey4, Object paramValue4) {
        return commonQueryForMap(tenantNumId, dataSign, sqlId, new String[]{paramKey1, paramKey2, paramKey3, paramKey4}, new Object[]{paramValue1, paramValue2, paramValue3, paramValue4});
    }

    public static Map<String, Object> commonQueryForMap(Long tenantNumId, Long dataSign, String sqlId, String paramKey1, Object paramValue1, String paramKey2, Object paramValue2, String paramKey3, Object paramValue3, String paramKey4, Object paramValue4, String paramKey5, Object paramValue5) {
        return commonQueryForMap(tenantNumId, dataSign, sqlId, new String[]{paramKey1, paramKey2, paramKey3, paramKey4, paramKey5}, new Object[]{paramValue1, paramValue2, paramValue3, paramValue4, paramValue5});
    }

    public static Map<String, Object> commonQueryForMap(Long tenantNumId, Long dataSign, String sqlId, String[] arrKey, Object[] arrValue) {
        CommonExcuteBySqlIdResponse response = commonQuery(tenantNumId, dataSign, sqlId, arrKey, arrValue);
        List<Map<String, Object>> list = response.getResults();
        return list != null && !list.isEmpty() ? (Map)list.get(0) : null;
    }

    public static CommonExcuteBySqlIdResponse commonQuery(Long tenantNumId, Long dataSign, String sqlId, String[] arrKey, Object[] arrValue) {
        if (innerExportDataService == null) {
            throw new ValidateBusinessException(SUB_SYSTEM, ExceptionType.VBE20033, "未设置导出服务！");
        } else {
            CommonExcuteBySqlIdRequest request = new CommonExcuteBySqlIdRequest();
            request.setTenantNumId(tenantNumId);
            request.setDataSign(dataSign);
            request.setSqlId(sqlId);
            if (arrKey.length != arrValue.length) {
                throw new ValidateBusinessException(SUB_SYSTEM, ExceptionType.VBE20033, "通用查询参数键值对数量不匹配！");
            } else {
                Map<String, Object> map = new HashMap();

                for(int i = 0; i < arrKey.length; ++i) {
                    map.put(arrKey[i], arrValue[i]);
                }

                request.setInputParam(map);
                CommonExcuteBySqlIdResponse response = innerExportDataService.commonExcuteBySqlId(request);
                if (response.getCode() == ExceptionType.BE40203.getCode()) {
                    response.setMessage(response.getSqlName() + "没有查询到纪录");
                }

                if (response.getCode() == ExceptionType.BE40202.getCode()) {
                    response.setMessage(response.getSqlName() + "子查询查到了多条纪录");
                }

                ExceptionUtil.checkDubboException(response);
                return response;
            }
        }
    }

    public static CommonExcuteBySqlIdResponse pageCommonQuery(Long tenantNumId, Long dataSign, String sqlId, Long pageNum, Long pageSize, String[] arrKey, Object[] arrValue) {
        if (innerExportDataService == null) {
            throw new ValidateBusinessException(SUB_SYSTEM, ExceptionType.VBE20033, "未设置导出服务！");
        } else {
            CommonExcuteBySqlIdRequest request = new CommonExcuteBySqlIdRequest();
            request.setTenantNumId(tenantNumId);
            request.setDataSign(dataSign);
            request.setSqlId(sqlId);
            request.setPageNum(pageNum);
            request.setPageSize(pageSize);
            if (arrKey.length != arrValue.length) {
                throw new ValidateBusinessException(SUB_SYSTEM, ExceptionType.VBE20033, "通用查询参数键值对数量不匹配！");
            } else {
                Map<String, Object> map = new HashMap();

                for(int i = 0; i < arrKey.length; ++i) {
                    map.put(arrKey[i], arrValue[i]);
                }

                request.setInputParam(map);
                CommonExcuteBySqlIdResponse response = innerExportDataService.commonExcuteBySqlId(request);
                if (response.getCode() == ExceptionType.BE40203.getCode()) {
                    response.setMessage(response.getSqlName() + "没有查询到纪录");
                }

                if (response.getCode() == ExceptionType.BE40202.getCode()) {
                    response.setMessage(response.getSqlName() + "子查询查到了多条纪录");
                }

                ExceptionUtil.checkDubboException(response);
                return response;
            }
        }
    }
}


package com.github.bluecatlee.gs4d.export.service;

import com.github.bluecatlee.gs4d.exchange.model.CommonQuery;
import com.github.bluecatlee.gs4d.exchange.model.ExcuteSqlResultModel;
import com.github.bluecatlee.gs4d.export.model.BatchExcuteSqlModel;
import net.sf.json.JSONObject;

import java.util.List;

public interface CommonJsonQueryService {

    ExcuteSqlResultModel excuteSqlById(Long currentPage, String dataSource, CommonQuery commonQuery, JSONObject inputParam, Long tenantNumId, Long dataSign);

//    List excuteSqlById(String dataSource, CommonQuery commonQuery, List<Long> list, Long tenantNumId, Long dataSign);

//    List<Long> getSeries(String dataSource);

    CommonQuery getCommonQuery(Long tenantNumId, Long dataSign, String sqlId);

    ExcuteSqlResultModel parseSqlById(Long currentPage, String dataSource, CommonQuery commonQuery, JSONObject inputParam, Long tenantNumId, Long dataSign);

    void batchExcuteSql(List<BatchExcuteSqlModel> list, String dataSource);

}

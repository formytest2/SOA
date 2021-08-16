package com.github.bluecatlee.gs4d.export.dao;

import com.github.bluecatlee.gs4d.common.datasource.DataSourceContextHolder;
import com.github.bluecatlee.gs4d.common.exception.DatabaseOperateException;
import com.github.bluecatlee.gs4d.common.exception.ExceptionType;
import com.github.bluecatlee.gs4d.common.utils.StringUtil;
import com.github.bluecatlee.gs4d.exchange.model.CommonQuery;
import com.github.bluecatlee.gs4d.export.constant.Constants;
import com.github.bluecatlee.gs4d.export.entity.COMMON_QUERY;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;

@Repository("commonQueryDao")
public class CommonQueryDao {

    @Resource(name = "myJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    public static final String a = "SELECT series, sql_name AS sqlName, sql_id AS sqlId, sql_content AS sqlContent, param_content AS paramContent, jdbc_name AS jdbcName, create_user_id AS createUserId, 'N' as cancelSign, tenant_num_id AS tenantNumId, data_sign AS dataSign, db_type AS dbType,annotate_prefix AS annotatePrefix,sub_sql_id,no_data_exception,cache_sign,method_name,cache_live_time,return_handle_content FROM common_query WHERE sql_id = ?  and data_sign = ?";
    public static final String b = "SELECT series, sql_name AS sqlName, sql_id AS sqlId, sql_content AS sqlContent, param_content AS paramContent, jdbc_name AS jdbcName, create_user_id AS createUserId, 'N' as cancelSign, tenant_num_id AS tenantNumId, data_sign AS dataSign, db_type AS dbType,annotate_prefix AS annotatePrefix,sub_sql_id,no_data_exception,cache_sign,method_name,cache_live_time,excel_column FROM common_query WHERE sql_id = ? and tenant_num_id = ? and data_sign = ?";

    public CommonQueryDao() {
    }

    public CommonQuery getModelWithTenant(String sqlId, Long tenantNumId, Long dataSign) {
        CommonQuery commonQuery = (CommonQuery)this.jdbcTemplate.queryForObject("SELECT series, sql_name AS sqlName, sql_id AS sqlId, sql_content AS sqlContent, param_content AS paramContent, jdbc_name AS jdbcName, create_user_id AS createUserId, 'N' as cancelSign, tenant_num_id AS tenantNumId, data_sign AS dataSign, db_type AS dbType,annotate_prefix AS annotatePrefix,sub_sql_id,no_data_exception,cache_sign,method_name,cache_live_time,excel_column FROM common_query WHERE sql_id = ? and tenant_num_id = ? and data_sign = ?",
                new Object[]{sqlId, tenantNumId, dataSign}, new BeanPropertyRowMapper(CommonQuery.class));
        DataSourceContextHolder.clearDataSourceType();
        return commonQuery;
    }

    public void updateModelBySeries(COMMON_QUERY entity) {
        StringBuffer sb = new StringBuffer();
        sb.append("UPDATE common_query  set ");
        ArrayList valueList = new ArrayList();
        if (!StringUtil.isNullOrBlankTrim(entity.getSQL_ID())) {
            sb.append("sql_id = ?,");
            valueList.add(entity.getSQL_ID());
        }

        if (!StringUtil.isNullOrBlankTrim(entity.getSQL_NAME())) {
            sb.append("sql_name = ?,");
            valueList.add(entity.getSQL_NAME());
        }

        if (!StringUtil.isNullOrBlankTrim(entity.getSQL_CONTENT())) {
            sb.append("sql_content = ?,");
            valueList.add(entity.getSQL_CONTENT());
        }

        if (!StringUtil.isNullOrBlankTrim(entity.getPARAM_CONTENT())) {
            sb.append("param_content = ?,");
            valueList.add(entity.getSQL_CONTENT());
        }

        if (!StringUtil.isNullOrBlankTrim(entity.getJDBC_NAME())) {
            sb.append("jdbc_name = ?,");
            valueList.add(entity.getJDBC_NAME());
        }

        if (!StringUtil.isNullOrBlankTrim(entity.getDB_TYPE())) {
            sb.append("db_type = ?,");
            valueList.add(entity.getDB_TYPE());
        }

        if (sb.length() <= 0) {
            throw new DatabaseOperateException(Constants.SUB_SYSTEM, ExceptionType.DOE30051, "更新表common_query的属性不可以为空！");
        } else {
            sb.delete(sb.length() - 1, sb.length());
            if (!StringUtil.isNullOrBlankTrim(entity.getSERIES())) {
                sb.append("  where series = ?");
                valueList.add(entity.getSERIES());
                if (this.jdbcTemplate.update(sb.toString(), valueList.toArray()) <= 0) {
                    throw new DatabaseOperateException(Constants.SUB_SYSTEM, ExceptionType.DOE30051, "更新表common_query失败！");
                }
            } else {
                throw new DatabaseOperateException(Constants.SUB_SYSTEM, ExceptionType.DOE30051, "更新表common_query的序列号不可以为空！");
            }
        }
    }

    public CommonQuery getModelNoTenant(String sqlId, Long dataSign) {
        CommonQuery commonQuery = (CommonQuery)this.jdbcTemplate.queryForObject("SELECT series, sql_name AS sqlName, sql_id AS sqlId, sql_content AS sqlContent, param_content AS paramContent, jdbc_name AS jdbcName, create_user_id AS createUserId, 'N' as cancelSign, tenant_num_id AS tenantNumId, data_sign AS dataSign, db_type AS dbType,annotate_prefix AS annotatePrefix,sub_sql_id,no_data_exception,cache_sign,method_name,cache_live_time,return_handle_content FROM common_query WHERE sql_id = ?  and data_sign = ?",
                new Object[]{sqlId, dataSign}, new BeanPropertyRowMapper(CommonQuery.class));
        if (commonQuery == null) {
            throw new DatabaseOperateException(Constants.SUB_SYSTEM, ExceptionType.DOE30051, "在通用查询配置没有sql" + sqlId + "对应的记录!");
        } else {
            DataSourceContextHolder.clearDataSourceType();
            return commonQuery;
        }
    }

    public CommonQuery getCommonQueryBySqlId(String sqlId, Long dataSign) {
        CommonQuery commonQuery = (CommonQuery)this.jdbcTemplate.queryForObject("SELECT series, sql_name AS sqlName, sql_id AS sqlId, sql_content AS sqlContent, param_content AS paramContent, jdbc_name AS jdbcName, create_user_id AS createUserId, 'N' as cancelSign, tenant_num_id AS tenantNumId, data_sign AS dataSign, db_type AS dbType,annotate_prefix AS annotatePrefix,sub_sql_id,no_data_exception,cache_sign,method_name,cache_live_time,return_handle_content FROM common_query WHERE sql_id = ?  and data_sign = ?",
                new Object[]{sqlId, dataSign}, new BeanPropertyRowMapper(CommonQuery.class));
        if (commonQuery == null) {
            throw new DatabaseOperateException(Constants.SUB_SYSTEM, ExceptionType.DOE30051, "没有找到" + sqlId + "对应的sql数据");
        } else {
            DataSourceContextHolder.clearDataSourceType();
            return commonQuery;
        }
    }
}

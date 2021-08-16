package com.github.bluecatlee.gs4d.cache.dao;

import com.github.bluecatlee.gs4d.cache.constant.Constants;
import com.github.bluecatlee.gs4d.cache.entity.EcCommonCacheDependence;
import com.github.bluecatlee.gs4d.cache.exception.CacheExceptionType;
import com.github.bluecatlee.gs4d.common.exception.DatabaseOperateException;
import com.github.bluecatlee.gs4d.common.utils.MyJdbcTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class EcCommonCacheDependenceDao {

    @Value("${db.annotate.prefix}")
    private String dbAnnotatePrefix;

    @Resource(name = "commonJdbcTemplate")
    private MyJdbcTemplate jdbcTemplate;

    public EcCommonCacheDependenceDao() {
    }

    public void batchInsert(final List<EcCommonCacheDependence> list) {
        String sql = "insert into ec_common_cache_dependence(series,tenant_num_id,data_sign,method_name,db,cache_key,params,`table_name`,table_series,create_dtme,last_updtme,create_user_id,last_update_user_id,dubbo_group) values (?,?,?,?,?,?,?,?,?,now(),now(),?,?,?)";
        int[] n = this.jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            public int getBatchSize() {
                return list.size();
            }

            public void setValues(PreparedStatement ps, int index) throws SQLException {
                EcCommonCacheDependence model = (EcCommonCacheDependence)list.get(index);
                int i = 1;
                ps.setString(i++, model.getSERIES());
                ps.setLong(i++, model.getTENANT_NUM_ID());
                ps.setLong(i++, model.getDATA_SIGN());
                ps.setString(i++, model.getMETHOD_NAME());
                ps.setString(i++, model.getDB());
                ps.setString(i++, model.getCACHE_KEY());
                ps.setString(i++, model.getPARAMS());
                ps.setString(i++, model.getTABLE_NAME());
                ps.setString(i++, model.getTABLE_SERIES());
                ps.setLong(i++, model.getCREATE_USER_ID());
                ps.setLong(i++, model.getLAST_UPDATE_USER_ID());
                ps.setString(i++, model.getDUBBO_GROUP());
            }
        });

        if (n.length != list.size()) {
            throw new DatabaseOperateException(Constants.SUB_SYSTEM, CacheExceptionType.DOE30042, "批量插入缓存方法值关联表记录失败!");
        }
    }

    public List<EcCommonCacheDependence> queryByDbAndTableNameAndTableSeries(Long tenantNumId, Long dataSign, String db, String tableName, Long tableSeries) {
        String sql = "select series,tenant_num_id,data_sign,method_name,db,cache_key,params,table_name,table_series,create_dtme,last_updtme,create_user_id,last_update_user_id,dubbo_group from ec_common_cache_dependence where data_sign=? and table_name=? and table_series=? and db=? ";
        if (!tenantNumId.equals(0L)) {
            sql = sql + String.format(" and tenant_num_id=%s  ", tenantNumId);
        }

        return this.jdbcTemplate.query(sql, new Object[]{dataSign, tableName, tableSeries, db}, new BeanPropertyRowMapper(EcCommonCacheDependence.class));
    }

    public List<String> querySeriesByMethodNameAndCacheKeyAndDubboGroup(Long tenantNumId, Long dataSign, String methodName, String cacheKey, String dubboGroup) {
        String sql = this.dbAnnotatePrefix + "select series from ec_common_cache_dependence where tenant_num_id =? and data_sign =? and cache_key=? and method_name=? and dubbo_group=? ";
        List list = this.jdbcTemplate.queryForList(sql, new Object[]{tenantNumId, dataSign, cacheKey, methodName, dubboGroup}, String.class);
        return list;
    }

    public List<String> querySeriesByMethodNameAndCacheKey(Long tenantNumId, Long dataSign, String methodName, String cacheKey) {
        String sql = this.dbAnnotatePrefix + "select series from ec_common_cache_dependence where tenant_num_id =? and data_sign =? and cache_key=? and method_name=? ";
        List n = this.jdbcTemplate.queryForList(sql, new Object[]{tenantNumId, dataSign, cacheKey, methodName}, String.class);
        return n;
    }

    public void deleteByMethodNameAndCacheKey(Long tenantNumId, Long dataSign, String methodName, String cacheKey) {
        String sql = "delete from ec_common_cache_dependence where tenant_num_id =? and data_sign =? and cache_key=? and method_name=? ";
        int n = this.jdbcTemplate.update(sql, new Object[]{tenantNumId, dataSign, cacheKey, methodName});
        if (n < 1) {
            throw new DatabaseOperateException(Constants.SUB_SYSTEM, CacheExceptionType.DOE30042, "删除已过期缓存方法值关联表记录失败!方法名:" + methodName + ",缓存键值:" + cacheKey);
        }
    }

    public void deleteByMethodNameAndCacheKeyAndSeries(Long tenantNumId, Long dataSign, String methodName, String cacheKey, String series) {
        String sql = "delete from ec_common_cache_dependence where tenant_num_id =? and data_sign =? and cache_key=? and method_name=? and series=? ";
        int n = this.jdbcTemplate.update(sql, new Object[]{tenantNumId, dataSign, cacheKey, methodName, series});
        if (n < 0) {
            throw new DatabaseOperateException(Constants.SUB_SYSTEM, CacheExceptionType.DOE30042, "删除已过期缓存方法值关联表记录失败!方法名:" + methodName + ",缓存键值:" + cacheKey);
        }
    }
}


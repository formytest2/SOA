package com.github.bluecatlee.gs4d.cache.dao;

import com.github.bluecatlee.gs4d.cache.constant.Constants;
import com.github.bluecatlee.gs4d.cache.entity.EcCacheMethodSchemaDefine;
import com.github.bluecatlee.gs4d.cache.exception.CacheExceptionType;
import com.github.bluecatlee.gs4d.common.exception.DatabaseOperateException;
import com.github.bluecatlee.gs4d.common.utils.MyJdbcTemplate;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class EcCacheMethodSchemaDefineDao {

    @Resource(name = "commonJdbcTemplate")
    private MyJdbcTemplate jdbcTemplate;

    public EcCacheMethodSchemaDefineDao() {
    }

    public EcCacheMethodSchemaDefine queryCacheMethodSchemaDefine(String methodName) {
        String sql = "select series,tenant_num_id,data_sign,sub_system,method_name,sql_content,db,cache_method,cache_multi_col,ttl,list_sign,allow_list_empty_sign,create_dtme,last_updtme,create_user_id,last_update_user_id,description from ec_cache_method_schema_define where tenant_num_id=0 and data_sign= 0 and method_name= ? ";
        EcCacheMethodSchemaDefine ecCacheMethodSchemaDefine = (EcCacheMethodSchemaDefine)this.jdbcTemplate.queryForObject(sql, new Object[]{methodName}, new BeanPropertyRowMapper(EcCacheMethodSchemaDefine.class));
        if (ecCacheMethodSchemaDefine == null) {
            throw new DatabaseOperateException(Constants.SUB_SYSTEM, CacheExceptionType.DOE30042, "获取缓存对应sql定义失败,方法名:" + methodName);
        } else {
            return ecCacheMethodSchemaDefine;
        }
    }

    public List<EcCacheMethodSchemaDefine> queryCacheMethodSchemaDefinesBySubSystem(String subSystem) {
        String sql = "select series,tenant_num_id,data_sign,sub_system,method_name,sql_content,db,cache_method,cache_multi_col,ttl,list_sign,create_dtme,last_updtme,create_user_id,last_update_user_id from ec_cache_method_schema_define where tenant_num_id=0 and data_sign= 0 and sub_system= ? ";
        List list = this.jdbcTemplate.query(sql, new Object[]{subSystem}, new BeanPropertyRowMapper(EcCacheMethodSchemaDefine.class));
        return list;
    }

}

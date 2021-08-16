package com.github.bluecatlee.gs4d.cache.dao;

import javax.annotation.Resource;

import com.github.bluecatlee.gs4d.common.utils.MyJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class MdmsSConfigDao {

    @Resource(name = "masterJdbcTemplate")
    private MyJdbcTemplate jdbcTemplate;

    public String getConfigValue(Long tenantNumId, Long dataSign, String configName) {
        String sql = "select config_value from mdms_s_config where tenant_num_id=? and data_sign=? and config_name=? limit 1 ";
        return (String)this.jdbcTemplate.queryForObject(sql, new Object[]{tenantNumId, dataSign, configName}, String.class);
    }
}


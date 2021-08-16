package com.github.bluecatlee.gs4d.cache.service.impl;

import com.github.bluecatlee.gs4d.cache.service.CacheCommonService;
import com.github.bluecatlee.gs4d.common.datasource.DataSourceContextHolder;
import com.github.bluecatlee.gs4d.common.datasource.DynamicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CacheCommonServiceImpl implements CacheCommonService {

    @Resource(name = "dataSource")
    DynamicDataSource dataSource;

    public JdbcTemplate getJdbcTemplate(String dataSourceType) {
        DataSourceContextHolder.setDataSourceType(dataSourceType);
        JdbcTemplate jdbcTemplate = new JdbcTemplate(this.dataSource);
        return jdbcTemplate;
    }

}

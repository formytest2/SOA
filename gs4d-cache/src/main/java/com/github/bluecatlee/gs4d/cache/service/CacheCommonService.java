package com.github.bluecatlee.gs4d.cache.service;

import org.springframework.jdbc.core.JdbcTemplate;

public interface CacheCommonService {

    JdbcTemplate getJdbcTemplate(String dataSourceType);

}

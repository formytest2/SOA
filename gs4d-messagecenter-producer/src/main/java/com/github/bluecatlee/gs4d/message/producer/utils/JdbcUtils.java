package com.github.bluecatlee.gs4d.message.producer.utils;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;

public class JdbcUtils {

    private List<JdbcTemplate> jdbcTemplates;

    public List<JdbcTemplate> getJdbcTemplates() {
        return this.jdbcTemplates;
    }

    public void setJdbcTemplates(List<JdbcTemplate> jdbcTemplates) {
        this.jdbcTemplates = jdbcTemplates;
    }

}

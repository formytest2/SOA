package com.github.bluecatlee.gs4d.common.utils;

import java.util.List;
import java.util.Map;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;

public class MyJdbcTemplate extends JdbcTemplate {

    private String dbAnnotatePrefix = "";

    public String getDbAnnotatePrefix() {
        return this.dbAnnotatePrefix;
    }

    public void setDbAnnotatePrefix(String dbAnnotatePrefix) {
        this.dbAnnotatePrefix = dbAnnotatePrefix;
    }

    public <T> T queryForObject(String sql, Object[] args, Class<T> requiredType) throws DataAccessException {
        sql = this.replaceSql(sql);
        return this.queryForObject(sql, args, this.getSingleColumnRowMapper(requiredType));
    }

    public <T> T queryForObject(String sql, Object[] args, RowMapper<T> rowMapper) throws DataAccessException {
        sql = this.replaceSql(sql);
        List<T> results = (List)super.query(sql, args, new RowMapperResultSetExtractor(rowMapper, 1));
        return IDataAccessUtil.requiredSingleResult(results);
    }

    public <T> List<T> queryForList(String sql, Object[] args, Class<T> elementType) throws DataAccessException {
        sql = this.replaceSql(sql);
        return super.queryForList(sql, args, elementType);
    }

    public SqlRowSet queryForRowSet(String sql, Object... args) throws DataAccessException {
        sql = this.replaceSql(sql);
        return super.queryForRowSet(sql, args);
    }

    public List<Map<String, Object>> queryForList(String sql, Object... args) throws DataAccessException {
        sql = this.replaceSql(sql);
        return super.queryForList(sql, args);
    }

    public <T> List<T> query(String sql, Object[] args, RowMapper<T> rowMapper) throws DataAccessException {
        sql = this.replaceSql(sql);
        return super.query(sql, args, rowMapper);
    }

    public int update(String sql, Object... args) throws DataAccessException {
        sql = this.replaceSql(sql);
        return super.update(sql, args);
    }

    public int[] batchUpdate(String sql, List<Object[]> batchArgs) throws DataAccessException {
        sql = this.replaceSql(sql);
        return super.batchUpdate(sql, batchArgs);
    }

    public String replaceSql(String sql) {
        return this.dbAnnotatePrefix + sql;
    }

}


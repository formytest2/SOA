package com.tranboot.client.service;

import com.tranboot.client.exception.TxcNotSupportException;
import com.tranboot.client.model.MysqlIndex;
import com.tranboot.client.model.TableSchema;
import com.tranboot.client.service.txc.TxcShardSettingReader;
import com.tranboot.client.spring.ContextUtils;
import com.tranboot.client.utils.LRUCache;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

public class TableSchemaCacheLoader implements Callable<TableSchema> {
    private JdbcTemplate jdbcTemplate;
    private String tableName;
    private String datasource;
    private static final String sql = "select * from %s limit 0,1";
    private static final String _sql = "select table_schema,column_name from information_schema.columns where table_name='%s'";
    private static final String showindex = "show index from %s";

    public TableSchemaCacheLoader(JdbcTemplate jdbcTemplate, String tableName, String datasource) {
        this.jdbcTemplate = jdbcTemplate;
        this.tableName = tableName;
        this.datasource = datasource;
    }

    private TableSchema loadSchema() {
        final TableSchema schema = new TableSchema();
        schema.setTableName(this.tableName);
        schema.setDbName(this.datasource);
        List<MysqlIndex> indexs = (List)this.jdbcTemplate.query(String.format("show index from %s", this.tableName), new ResultSetExtractor<List<MysqlIndex>>() {
            public List<MysqlIndex> extractData(ResultSet rs) throws SQLException, DataAccessException {
                ArrayList indexs = new ArrayList();

                while(rs.next()) {
                    MysqlIndex index = new MysqlIndex();
                    index.setTable(rs.getString("Table"));
                    index.setNonUnique(rs.getInt("Non_unique"));
                    index.setKeyName(rs.getString("Key_name"));
                    index.setColumnName(rs.getString("Column_name"));
                    index.setIndexType(rs.getString("Index_type"));
                    index.setSeqInIndex(rs.getInt("Seq_in_index"));
                    indexs.add(index);
                }

                return indexs;
            }
        });
        Map<String, List<String>> tmp = new HashMap();
        Iterator var4 = indexs.iterator();

        while(var4.hasNext()) {
            MysqlIndex index = (MysqlIndex)var4.next();
            if (index.getKeyName().equals("PRIMARY")) {
                schema.primaryKey(index.getColumnName().toLowerCase());
            } else if (index.getNonUnique() == 0) {
                String keyname = index.getKeyName();
                if (tmp.containsKey(keyname)) {
                    ((List)tmp.get(keyname)).add(index.getColumnName().toLowerCase());
                } else {
                    tmp.put(keyname, new ArrayList());
                }
            }
        }

        schema.setUniqKeyStrs(tmp.values());
        if ((schema.getPrimaryKeyStrs() == null || schema.getPrimaryKeyStrs().size() <= 0) && (schema.getUniqKeyStrs() == null || schema.getUniqKeyStrs().size() <= 0)) {
            throw new TxcNotSupportException(String.format("表%s没有解析到主键或唯一索引，不能进行分布式事务操作", this.tableName));
        } else {
            new ArrayList();
            List<Map<String, String>> allfileds = (List)this.jdbcTemplate.query(String.format("select table_schema,column_name from information_schema.columns where table_name='%s'", this.tableName), new ResultSetExtractor<List<Map<String, String>>>() {
                public List<Map<String, String>> extractData(ResultSet rs) throws SQLException, DataAccessException {
                    ArrayList result = new ArrayList();

                    while(rs.next()) {
                        Map<String, String> field = new HashMap();
                        field.put("table_schema", rs.getString("table_schema").toLowerCase());
                        field.put("column_name", rs.getString("column_name").toLowerCase());
                        result.add(field);
                    }

                    return result;
                }
            });
            if (allfileds != null && allfileds.size() > 0) {
                Map<String, List<Map<String, String>>> grouprs = (Map)allfileds.stream().collect(Collectors.groupingBy(new Function<Map<String, String>, String>() {
                    public String apply(Map<String, String> t) {
                        return (String)t.get("table_schema");
                    }
                }));
                List<String> fileds = (List)((List)((Entry)grouprs.entrySet().iterator().next()).getValue()).stream().map(new Function<Map<String, String>, String>() {
                    public String apply(Map<String, String> t) {
                        return (String)t.get("column_name");
                    }
                }).collect(Collectors.toList());
                schema.setColumns(fileds);
            } else {
                this.jdbcTemplate.query(String.format("select * from %s limit 0,1", this.tableName), new ResultSetExtractor<Map<String, Object>>() {
                    public Map<String, Object> extractData(ResultSet rs) throws SQLException, DataAccessException {
                        ResultSetMetaData rsm = rs.getMetaData();

                        try {
                            if (Class.forName("com.alibaba.druid.proxy.jdbc.ResultSetMetaDataProxyImpl").isInstance(rsm)) {
                                Method getResultSetMetaDataRaw = Class.forName("com.alibaba.druid.proxy.jdbc.ResultSetMetaDataProxyImpl").getMethod("getResultSetMetaDataRaw");
                                rsm = (ResultSetMetaData)getResultSetMetaDataRaw.invoke(rsm);
                            }
                        } catch (Exception var7) {
                            throw new TxcNotSupportException(var7, String.format("获取表%s table_schema 时出现异常", TableSchemaCacheLoader.this.tableName));
                        }

                        try {
                            if (!LRUCache.tableSchemaCacheContain(TableSchemaCacheLoader.this.tableName)) {
                                schema.setTableName(TableSchemaCacheLoader.this.tableName);
                                Field f_fields = rsm.getClass().getDeclaredField("fields");
                                f_fields.setAccessible(true);
                                com.mysql.jdbc.Field[] fields = (com.mysql.jdbc.Field[])((com.mysql.jdbc.Field[])f_fields.get(rsm));
                                List<String> fs = (List)Arrays.asList(fields).stream().map(new Function<com.mysql.jdbc.Field, String>() {
                                    public String apply(com.mysql.jdbc.Field t) {
                                        try {
                                            return t.getName().toLowerCase();
                                        } catch (Exception var3) {
                                            throw new TxcNotSupportException(var3, String.format("获取表%s 列失败", TableSchemaCacheLoader.this.tableName));
                                        }
                                    }
                                }).collect(Collectors.toList());
                                schema.setColumns(fs);
                            }

                            return null;
                        } catch (SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException var6) {
                            throw new TxcNotSupportException(var6, String.format("获取表%s table_schema 时出现异常", TableSchemaCacheLoader.this.tableName));
                        }
                    }
                });
                if (schema.getColumns() == null) {
                    throw new TxcNotSupportException(String.format("表%s没有解析到列，不能进行分布式事务操作", this.tableName));
                }
            }

            String shardField = ((TxcShardSettingReader)ContextUtils.getBean(TxcShardSettingReader.class)).shardFiled(this.datasource, this.tableName);
            schema.setShardField(shardField);
            return schema;
        }
    }

    public TableSchema call() throws Exception {
        return this.loadSchema();
    }
}

package com.tranboot.client.model.txc;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.codahale.metrics.Timer.Context;
import com.tranboot.client.exception.TxcQueryException;
import com.tranboot.client.exception.TxcSqlParseException;
import com.tranboot.client.model.SQLType;
import com.tranboot.client.model.txc.SQLParamExtractorPipeline.PrimaryKeyExtractor;
import com.tranboot.client.model.txc.TxcSQL.RollbackSqlInfo;
import com.tranboot.client.sqlast.MySQLRewriteVistorAop;
import com.tranboot.client.sqlast.SQLASTVisitorAspectAdapter;
import com.tranboot.client.utils.MetricsReporter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

public class SqlParserTxcSQL implements TxcSQL {
    protected SQLStatement querySql;
    protected SQLStatement rollbackSql;
    protected String where;
    protected SQLType sqlType;
    protected String tableName;
    protected PrimaryKeyExtractor primaryKeyExtractor;
    protected SQLParamExtractorPipeline pipeline;

    public SqlParserTxcSQL() {
    }

    private void deal(Object[] args) {
        Context context = MetricsReporter.deal.time();

        try {
            this.pipeline.start(args);
        } finally {
            context.stop();
        }

    }

    private List<Map<String, Object>> query(JdbcTemplate jdbcTemplate) {
        Context context = MetricsReporter.query.time();
        String sql = this.querySql();

        List var5;
        try {
            List<Map<String, Object>> rows = jdbcTemplate.query(sql, new ColumnMapRowMapper());
            var5 = rows;
        } catch (Exception var9) {
            throw new TxcQueryException(var9, String.format("%s 查询失败", sql));
        } finally {
            context.stop();
        }

        return var5;
    }

    private void render(Map<String, Object> row) {
        Context context = MetricsReporter.render.time();

        try {
            this.pipeline.setParam("query_row", row);
            this.pipeline.render();
        } finally {
            context.stop();
        }

    }

    private RollbackSqlInfo rollbackSql() {
        StringBuilder sbuilder = new StringBuilder();
        this.rollbackSql.accept(new MySQLRewriteVistorAop(this.pipeline.getFinalArgs(), sbuilder, new SQLASTVisitorAspectAdapter()));
        String sql = sbuilder.toString();
        return new RollbackSqlInfo(sql, this.primaryKeyExtractor.render(), this.primaryKeyExtractor.shard());
    }

    private String querySql() {
        try {
            StringBuilder sbuilder = new StringBuilder();
            this.querySql.accept(new MySQLRewriteVistorAop(this.pipeline.getFinalArgs(), sbuilder, new SQLASTVisitorAspectAdapter()));
            String var2 = sbuilder.toString();
            return var2;
        } catch (Exception var6) {
            throw new TxcSqlParseException(var6, String.format("%s 生成查询语句失败", this.querySql.toString()));
        } finally {
            ;
        }
    }

    public String getTableName() {
        return this.tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public SQLType getSqlType() {
        return this.sqlType;
    }

    public void setSqlType(SQLType sqlType) {
        this.sqlType = sqlType;
    }

    public List<RollbackSqlInfo> rollbackSql(Object[] args, JdbcTemplate jdbc) {
        Context context = MetricsReporter.rollbackSqlTimer.time();

        try {
            List<RollbackSqlInfo> rollbackSqls = new ArrayList();
            this.deal(args);
            if (this.querySql == null) {
                this.render((Map)null);
                rollbackSqls.add(this.rollbackSql());
            } else {
                List rows;
                if (this.sqlType == SQLType.INSERT) {
                    rows = Collections.singletonList(this.rollbackSql());
                    return rows;
                }

                rows = this.query(jdbc);
                Iterator var6 = rows.iterator();

                while(var6.hasNext()) {
                    Map<String, Object> row = (Map)var6.next();
                    this.render(row);
                    rollbackSqls.add(this.rollbackSql());
                }
            }

            List var13 = rollbackSqls;
            return var13;
        } catch (Exception var11) {
            throw new TxcSqlParseException(var11, String.format("%s 生成回滚语句失败", this.rollbackSql.toString()));
        } finally {
            context.stop();
        }
    }

    public String toString() {
        StringBuilder sbuilder = new StringBuilder();
        sbuilder.append("生成方式:").append("自动").append(System.lineSeparator());
        sbuilder.append("查询语句:").append(this.querySql == null ? "无需前置查询" : SQLUtils.toMySqlString(this.querySql, new SQLUtils.FormatOption(false, false, false))).append(System.lineSeparator());
        sbuilder.append("回滚语句:").append(SQLUtils.toMySqlString(this.rollbackSql, new SQLUtils.FormatOption(false, false, false))).append(System.lineSeparator());
        sbuilder.append("主键抽取器:").append(this.primaryKeyExtractor.toString()).append(System.lineSeparator());
        sbuilder.append("处理拓扑:").append(System.lineSeparator()).append(this.pipeline.toString()).append(System.lineSeparator());
        return sbuilder.toString();
    }
}


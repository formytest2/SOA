package com.tranboot.client.model.txc;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.tranboot.client.model.DBType;
import com.tranboot.client.service.txc.TxcManualRollbackSqlService;
import com.tranboot.client.spring.ContextUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;

public class TxcSqlProcessorWraper implements TxcSqlProcessor {
    private String sql;
    private TxcSqlProcessor processor;
    private DBType dbType;
    private JdbcTemplate jdbc;
    private String datasource;

    public TxcSqlProcessorWraper(String sql, JdbcTemplate jdbc, DBType dbType, String datasource) {
        this.sql = sql;
        this.jdbc = jdbc;
        this.dbType = dbType;
        this.datasource = datasource;
    }

    public TxcSQL parse() {
        if (this.manual()) {
            logger.debug("{} 通过手动配置生成回滚语句。", this.sql);
            this.processor = new TxcSqlManualProcessor(this.sql);
        } else {
            this.processor = new TxcSqlParserProcessor(this.jdbc, (SQLStatement) SQLUtils.parseStatements(this.cleanSql(this.sql), this.dbType == DBType.ORACLE ? "oracle" : "mysql").get(0), this.datasource);
        }

        return this.processor.parse();
    }

    public boolean manual() {
        return ((TxcManualRollbackSqlService)ContextUtils.getBean(TxcManualRollbackSqlService.class)).map(this.sql) != null;
    }

    public boolean auto() {
        return ((TxcManualRollbackSqlService)ContextUtils.getBean(TxcManualRollbackSqlService.class)).map(this.sql) == null;
    }

    protected String cleanSql(String sql) {
        return sql.indexOf("mycat") > -1 ? StringUtils.substringAfterLast(sql, "*/") : sql;
    }
}


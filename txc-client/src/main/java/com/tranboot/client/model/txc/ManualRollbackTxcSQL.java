package com.tranboot.client.model.txc;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.codahale.metrics.Timer.Context;
import com.tranboot.client.exception.TxcSqlParseException;
import com.tranboot.client.model.SQLType;
import com.tranboot.client.model.txc.SQLParamExtractorPipeline.KeyValuePair;
import com.tranboot.client.sqlast.MySQLRewriteVistorAop;
import com.tranboot.client.sqlast.SQLASTVisitorAspectAdapter;
import com.tranboot.client.utils.MetricsReporter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ManualRollbackTxcSQL implements TxcSQL {
    public final String originalSql;
    public final String rollbackSql;
    public final int[] indexs;
    public final SQLStatement rollbackStatement;
    public final SQLType sqlType;
    public final String table;
    public final String[] primaryKey;
    public final int[] primaryValueIndex;
    public final Integer shardValueIndex;

    public ManualRollbackTxcSQL(String originalSql, String rollbackSql, int[] indexs, String table, SQLType sqlType, String[] primaryKey, int[] primaryValueIndex, int shardValueIndex) {
        this.originalSql = originalSql;
        this.rollbackSql = rollbackSql;
        this.indexs = indexs;
        this.rollbackStatement = (SQLStatement) SQLUtils.parseStatements(rollbackSql, "mysql").get(0);
        this.sqlType = sqlType;
        this.table = table;
        this.primaryKey = primaryKey;
        this.primaryValueIndex = primaryValueIndex;
        this.shardValueIndex = shardValueIndex;
    }

    public List<RollbackSqlInfo> rollbackSql(Object[] args, JdbcTemplate jdbcTemplate) {
        Context context = MetricsReporter.manualRollbackSqlTimer.time();
        Object[] params = null;
        if (this.indexs != null) {
            params = new Object[this.indexs.length];

            for(int i = 0; i < this.indexs.length; ++i) {
                params[i] = args[this.indexs[i]];
            }
        }

        List rollbackSqlInfoList;
        try {
            StringBuilder sbuilder = new StringBuilder();
            this.rollbackStatement.accept(new MySQLRewriteVistorAop((List)(params == null ? new ArrayList() : Arrays.asList(params)), sbuilder, new SQLASTVisitorAspectAdapter()));
            String rollbackSql = sbuilder.toString();
            List<KeyValuePair> primaryKVPair = new ArrayList();
            if (this.primaryKey != null && this.primaryKey.length > 0) {
                for(int i = 0; i < this.primaryKey.length; ++i) {
                    primaryKVPair.add(new KeyValuePair(this.primaryKey[i], args[this.primaryValueIndex[i]]));
                }
            }

            RollbackSqlInfo rollbackInfo = new RollbackSqlInfo(rollbackSql, primaryKVPair, this.shardValueIndex == null ? null : args[this.shardValueIndex].toString());
            rollbackSqlInfoList = Collections.singletonList(rollbackInfo);
        } catch (Exception e) {
            throw new TxcSqlParseException(e, String.format("%s 生成回滚语句失败", this.rollbackSql));
        } finally {
            context.stop();
        }

        return rollbackSqlInfoList;
    }

    public SQLType getSqlType() {
        return this.sqlType;
    }

    public String getTableName() {
        return this.table;
    }

    public String toString() {
        StringBuilder sbuilder = new StringBuilder();
        sbuilder.append("生成方式:").append("手动配置").append(System.lineSeparator());
        sbuilder.append("源语句:").append(this.originalSql).append(System.lineSeparator());
        sbuilder.append("回滚语句:").append(this.rollbackSql).append(System.lineSeparator());
        sbuilder.append("参数下标:").append(this.indexs == null ? "" : Arrays.toString(this.indexs)).append(System.lineSeparator());
        sbuilder.append("主键字段:").append(this.primaryKey).append(System.lineSeparator());
        sbuilder.append("主键参数下标:").append(this.primaryValueIndex).append(System.lineSeparator());
        return sbuilder.toString();
    }
}


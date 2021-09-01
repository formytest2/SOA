package com.tranboot.client.model.txc;

import com.tranboot.client.model.SQLType;
import com.tranboot.client.service.txc.TxcManualRollbackSqlService;
import com.tranboot.client.spring.ContextUtils;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

public class TxcSqlManualProcessor implements TxcSqlProcessor {
    private String sql;

    public TxcSqlManualProcessor(String sql) {
        this.sql = sql;
    }

    public TxcSQL parse() {
        Map<String, Object> rollbackSql = ((TxcManualRollbackSqlService)ContextUtils.getBean(TxcManualRollbackSqlService.class)).map(this.sql);
        String rollback = rollbackSql.get("rollback_sql").toString();
        String indexs = rollbackSql.get("indexs").toString();
        int[] _indexs = null;
        if (StringUtils.isNotBlank(indexs)) {
            String[] indexArr = indexs.split(",");
            _indexs = new int[indexArr.length];

            for(int i = 0; i < indexArr.length; ++i) {
                _indexs[i] = Integer.parseInt(indexArr[i]);
            }
        }

        String table = rollbackSql.get("table").toString();
        String sqltype = rollbackSql.get("sql_type").toString();
        String originalSql = rollbackSql.get("original_sql").toString();
        String primaryKey = rollbackSql.get("primary_key").toString();
        String primaryValueIndexs = rollbackSql.get("primary_value_index").toString();
        String shardValueIndex = rollbackSql.get("shard_value_index") == null ? null : rollbackSql.get("shard_value_index").toString();
        String[] pvArr = primaryValueIndexs.split(",");
        int[] pkvIndexs = new int[pvArr.length];

        for(int i = 0; i < pvArr.length; ++i) {
            pkvIndexs[i] = Integer.parseInt(pvArr[i]);
        }

        return new ManualRollbackTxcSQL(originalSql, rollback, _indexs, table, SQLType.type(sqltype), primaryKey.split(","), pkvIndexs, shardValueIndex == null ? null : Integer.parseInt(shardValueIndex));
    }

    public boolean manual() {
        return true;
    }

    public boolean auto() {
        return false;
    }
}


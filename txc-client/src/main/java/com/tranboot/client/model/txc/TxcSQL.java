package com.tranboot.client.model.txc;

import com.tranboot.client.model.SQLType;
import com.tranboot.client.model.txc.SQLParamExtractorPipeline.KeyValuePair;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;

public interface TxcSQL {
    List<TxcSQL.RollbackSqlInfo> rollbackSql(Object[] var1, JdbcTemplate var2);

    SQLType getSqlType();

    String getTableName();

    public static class RollbackSqlInfo {
        public final String rollbackSql;
        public final List<KeyValuePair> primaryKVPair;
        public final String shardValue;

        public RollbackSqlInfo(String rollbackSql, List<KeyValuePair> primaryKVPair, String shardValue) {
            this.rollbackSql = rollbackSql;
            this.primaryKVPair = primaryKVPair;
            this.shardValue = shardValue;
        }

        public String pkv() {
            StringBuilder sbuilder = new StringBuilder();
            Iterator iterator = this.primaryKVPair.iterator();

            while(iterator.hasNext()) {
                KeyValuePair kv = (KeyValuePair)iterator.next();
                sbuilder.append(kv.column).append("[").append(kv.value.toString()).append("]").append("-");
            }

            return StringUtils.substringBeforeLast(sbuilder.toString(), "-");
        }
    }
}


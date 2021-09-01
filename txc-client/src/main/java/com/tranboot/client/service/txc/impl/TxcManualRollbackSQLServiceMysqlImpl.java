package com.tranboot.client.service.txc.impl;

import com.codahale.metrics.Timer.Context;
import com.tranboot.client.exception.TxcTransactionException;
import com.tranboot.client.service.txc.TxcManualRollbackSqlService;
import com.tranboot.client.spring.ContextUtils;
import com.tranboot.client.utils.MetricsReporter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;

/** @deprecated */
@Deprecated
public class TxcManualRollbackSQLServiceMysqlImpl extends TxcManualRollbackSqlService {
    int systemId;

    public TxcManualRollbackSQLServiceMysqlImpl(int systemId) {
        this.systemId = systemId;
        this.init();
    }

    public void init() {
        this.queryManualRollbackSql();
    }

    private void queryManualRollbackSql() {
        this.logger.debug("开始从数据库中读取并加载数据库缓存 {}", this.systemId);
        Connection con = null;
        Context context = MetricsReporter.queryManualRollbackSql.time();

        try {
            String sql = "select * from txc_manual_rollback_sql where systemId=?";
            DataSource platformDs = (DataSource)ContextUtils.getBean("platformDataSource", DataSource.class);
            con = platformDs.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, this.systemId);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                Map<String, Object> row = new HashMap();
                row.put("original_sql", rs.getString("original_sql"));
                row.put("rollback_sql", rs.getString("rollback_sql"));
                row.put("system_id", rs.getInt("system_id"));
                row.put("indexs", rs.getString("indexs"));
                row.put("table", rs.getString("table"));
                row.put("sql_type", rs.getString("sql_type").trim().toLowerCase());
                row.put("primary_key", rs.getString("primary_key"));
                row.put("primary_value_index", rs.getString("primary_value_index"));
                row.put("shard_value_index", rs.getString("shard_value_index"));
                this.dealRow(row);
            }

            this.logger.debug("加载数据库缓存结束,共读取到{}条数据", this.rollbackCache.size());
        } catch (Exception var15) {
            throw new TxcTransactionException(var15, "txc_manual_rollback_sql查询失败");
        } finally {
            try {
                if (con != null) {
                    con.close();
                }

                context.stop();
            } catch (SQLException var14) {
                this.logger.error("手动释放连接失败", var14);
                context.stop();
                throw new TxcTransactionException(var14, "释放连接失败");
            }
        }
    }

    private void dealRow(Map<String, Object> row) {
        this.rollbackCache.put(row.get("original_sql").toString(), row);
    }
}

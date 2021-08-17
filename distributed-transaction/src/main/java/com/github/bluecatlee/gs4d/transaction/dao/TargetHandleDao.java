package com.github.bluecatlee.gs4d.transaction.dao;

import com.alibaba.fastjson.JSONObject;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Resource;

import com.github.bluecatlee.gs4d.common.utils.MyJdbcTemplate;
import com.github.bluecatlee.gs4d.sequence.utils.SeqGetUtil;
import com.github.bluecatlee.gs4d.transaction.api.model.TableKeyValueModel;
import com.github.bluecatlee.gs4d.transaction.api.utils.TransactionApiUtil;
import com.github.bluecatlee.gs4d.transaction.utils.Constants;
import org.springframework.stereotype.Repository;

@Repository
public class TargetHandleDao {

    @Resource(name = "dynamicJdbcTemplate")
    private MyJdbcTemplate dynamicJdbcTemplate;

    public Integer execute(String sql) {
        Integer i = this.dynamicJdbcTemplate.update(sql);
        return i;
    }

    public Long queryCountBySqlKeyValueAndTxc(String sqlKeyValue, String sqlId, String tableName) {
        List kvList = JSONObject.parseArray(sqlKeyValue, TableKeyValueModel.class);
        StringBuilder condition = new StringBuilder();
        Iterator iterator = kvList.iterator();

        while(iterator.hasNext()) {
            TableKeyValueModel tableKeyValueModel = (TableKeyValueModel)iterator.next();
            condition.append(tableKeyValueModel.getColumn() + "='" + tableKeyValueModel.getValue() + "' and ");
        }

        StringBuilder sb = new StringBuilder();
        sb.append("select count(*) from ").append(tableName).append(" where ").append(condition.toString()).append(TransactionApiUtil.txc).append("='").append(sqlId).append("'");
        String sql = sb.toString();
        Long count = (Long)this.dynamicJdbcTemplate.queryForObject(sql, Long.class);
        return count;
    }

    public Long queryCountBySqlKeyValue(String sqlKeyValue, String tableName) {
        List kvList = JSONObject.parseArray(sqlKeyValue, TableKeyValueModel.class);
        StringBuilder condition = new StringBuilder();
        Iterator iterator = kvList.iterator();

        while(iterator.hasNext()) {
            TableKeyValueModel tableKeyValueModel = (TableKeyValueModel)iterator.next();
            condition.append(tableKeyValueModel.getColumn() + "=" + tableKeyValueModel.getValue() + " and");
        }

        String conditionS = condition.substring(0, condition.length() - 3);
        StringBuilder sb = new StringBuilder();
        sb.append("select count(*) from ").append(tableName).append(" where ").append(conditionS);
        String sql = sb.toString();
        Long count = (Long)this.dynamicJdbcTemplate.queryForObject(sql, Long.class);
        return count;
    }

    public void insert(String transactionKey) {
        StringBuilder sb = new StringBuilder();
        sb.append("insert into transaction_rollback_flag(series,transaction_key)values(?,?)");
        String sql = sb.toString();
        this.dynamicJdbcTemplate.update(sql, new Object[]{SeqGetUtil.getNoSubSequence(Constants.TRANSACTION_ROLLBACK_FLAG_SERIES), transactionKey});
    }

    public int queryCountByTransactionKey(String transactionKey) {
        StringBuilder sb = new StringBuilder();
        sb.append("select count(*) from transaction_rollback_flag where transaction_key=?");
        String sql = sb.toString();
        return (Integer)this.dynamicJdbcTemplate.queryForObject(sql, new Object[]{transactionKey}, Integer.class);
    }
}

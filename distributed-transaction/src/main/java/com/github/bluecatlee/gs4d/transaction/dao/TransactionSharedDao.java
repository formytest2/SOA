package com.github.bluecatlee.gs4d.transaction.dao;

import com.github.bluecatlee.gs4d.sequence.utils.SeqGetUtil;
import com.github.bluecatlee.gs4d.transaction.model.TRANSACTION_SHARED;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class TransactionSharedDao {

    @Resource(name = "jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Value("${mycatGoMaster}")
    private String mycatGoMaster;

    public List<TRANSACTION_SHARED> querySharedColumn(String dbName, String tableName) {
        String sql = this.mycatGoMaster + "select shared_column from transaction_shared where db_name=? and table_name=? and cancelsign='N' ";
        List list = this.jdbcTemplate.query(sql, new Object[]{dbName, tableName}, new BeanPropertyRowMapper(TRANSACTION_SHARED.class));
        return list;
    }

    public List<TRANSACTION_SHARED> b(Long var1, Long var2) {
        String var3 = this.mycatGoMaster + "select series,db_name,table_name,shared_column from transaction_shared where  cancelsign='N' limit ?,? ";
        List var4 = this.jdbcTemplate.query(var3, new Object[]{var1, var2}, new BeanPropertyRowMapper(TRANSACTION_SHARED.class));
        return var4;
    }

    public Long getTranstionSharedCount() {
        String var1 = "select count(*) from transaction_shared where  cancelsign='N'";
        Long var2 = (Long)this.jdbcTemplate.queryForObject(var1, Long.class);
        return var2;
    }

    public void b(String var1, String var2, String var3) {
        Long var4 = SeqGetUtil.getNoSubSequence("transaction_shared_series");
        String var5 = "insert transaction_shared(series,db_name,table_name,shared_column,CREATE_TIME,UPDATE_TIME,cancelsign)values(?,?,?,?,sysdate(),sysdate(),'N')";
        this.jdbcTemplate.update(var5, new Object[]{var4, var3, var2, var1});
    }

    public void c(Long var1) {
        String var2 = "update transaction_shared set cancelsign='Y' where series=?";
        this.jdbcTemplate.update(var2, new Object[]{var1});
    }
}


package com.github.bluecatlee.gs4d.transaction.dao;

import java.util.List;
import javax.annotation.Resource;

import com.github.bluecatlee.gs4d.common.exception.DatabaseOperateException;
import com.github.bluecatlee.gs4d.common.exception.ExceptionType;
import com.github.bluecatlee.gs4d.transaction.model.TRANSACTION_LOG;
import com.github.bluecatlee.gs4d.transaction.utils.Constants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TransactionLogDao {

    @Resource(name = "jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Value("${mycatGoMaster}")
    private String mycatGoMaster;

    public void updateByTransactionId(Integer transactionState, String transactionSign, Long transactionId) {
        String sql = "update transaction_log set end_dtme=sysdate(),transaction_state=?,transaction_sign=? where transaction_id=?";
        int i = this.jdbcTemplate.update(sql, new Object[]{transactionState, transactionSign, transactionId});
        if (i < 1) {
            throw new DatabaseOperateException(Constants.SUB_SYSTEM, ExceptionType.DOE30084, "分布式事务服务端更新事务表失败，transactionId:" + transactionId);
        }
    }

    public Long getSuccessTranstionLogNum() {
        String sql = this.mycatGoMaster + "select count(*) from transaction_log where  transaction_sign='Y' and end_dtme<date_add(now(), interval - 1 day) limit 100000 ";
        Long count = (Long)this.jdbcTemplate.queryForObject(sql, Long.class);
        return count;
    }

    public List<TRANSACTION_LOG> getSuccessTranstionLog(Long offset, Long rows) {
        String sql = this.mycatGoMaster + "select transaction_id from transaction_log where transaction_sign='Y' and end_dtme<date_add(now(), interval - 1 day) limit ?,? ";
        List list = this.jdbcTemplate.query(sql, new Object[]{offset, rows}, new BeanPropertyRowMapper(TRANSACTION_LOG.class));
        return list;
    }

    public void delete(Long transactionId) {
        String sql = "delete from transaction_log where transaction_id=?";
        this.jdbcTemplate.update(sql, new Object[]{transactionId});
    }

    public List<TRANSACTION_LOG> queryByTransactionId(Long transactionId) {
        String sql = this.mycatGoMaster + "select transaction_rollback_flag,TRANSACTION_STATE from transaction_log where transaction_id=? ";
        List list = this.jdbcTemplate.query(sql, new Object[]{transactionId}, new BeanPropertyRowMapper(TRANSACTION_LOG.class));
        return list;
    }

    public List<TRANSACTION_LOG> getErrorTranstionLog() {
        String sql = this.mycatGoMaster + "select transaction_id,Transaction_rollback_flag from transaction_log where transaction_state=3 and transaction_sign='N' and start_dtme<DATE_SUB(now(),INTERVAL 10 MINUTE) ";
        List list = this.jdbcTemplate.query(sql, new BeanPropertyRowMapper(TRANSACTION_LOG.class));
        return list;
    }

    public List<TRANSACTION_LOG> getErrorTranstionLogToSendEmail() {
        String sql = this.mycatGoMaster + "select transaction_id,ip_address,from_system,method_name,sysdate() sysTime from transaction_log where transaction_state=0 and transaction_sign='N' and start_dtme<DATE_SUB(now(),INTERVAL 1 MINUTE) limit 200";
        List list = this.jdbcTemplate.query(sql, new BeanPropertyRowMapper(TRANSACTION_LOG.class));
        return list;
    }

    public void insert(TRANSACTION_LOG entity) {
        String sql = "insert into TRANSACTION_LOG(transaction_id,start_dtme,ip_address,transaction_state,transaction_sign,from_system,method_name,transaction_rollback_flag) values(?,sysdate(),?,0,'N',?,?,?)";
        this.jdbcTemplate.update(sql, new Object[]{entity.getTRANSACTION_ID(), entity.getIP_ADDRESS(), entity.getFROM_SYSTEM(), entity.getMETHOD_NAME(), entity.getTRANSACTION_ROLLBACK_FLAG()});
    }

    public Long getNowTime() {
        String sql = this.mycatGoMaster + "select  sysdate() sysTime from transaction_log limit 0,1";
        List list = this.jdbcTemplate.query(sql, new BeanPropertyRowMapper(TRANSACTION_LOG.class));
        return ((TRANSACTION_LOG)list.get(0)).getSysTime().getTime();
    }

    public List<TRANSACTION_LOG> d(String var1) {
        List var2 = this.jdbcTemplate.query(var1, new BeanPropertyRowMapper(TRANSACTION_LOG.class));
        return var2;
    }

    public Long e(String var1) {
        Long var2 = (Long)this.jdbcTemplate.queryForObject(var1, Long.class);
        return var2;
    }
}


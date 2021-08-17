package com.github.bluecatlee.gs4d.transaction.dao;

import com.github.bluecatlee.gs4d.common.exception.DatabaseOperateException;
import com.github.bluecatlee.gs4d.common.exception.ExceptionType;
import com.github.bluecatlee.gs4d.transaction.model.TRANSACTION_SQL_LOG;
import com.github.bluecatlee.gs4d.transaction.utils.Constants;
import com.github.bluecatlee.gs4d.transaction.utils.SqlUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class TransactionSqlLogDao {

    @Resource(name = "jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Value("${mycatGoMaster}")
    private String mycatGoMaster;

    public void insert(TRANSACTION_SQL_LOG entity) {
        String sql = "insert into transaction_sql_log(series,transaction_id,transaction_db_id,source_db,table_name,commit_sql_dtme,rollback_sql_dtme,`sql`,sql_param,transaction_sign,sql_level,sql_is_out_time,sql_type,sql_key_value,sql_id,biz_redis_key,sql_status,biz_redis_value,sql_timeout)";
        Object[] args = new Object[]{entity.getSERIES(), entity.getTRANSACTION_ID(), entity.getTRANSACTION_DB_ID(), entity.getSOURCE_DB(), entity.getTABLE_NAME(), entity.getCOMMIT_SQL_DTME(), entity.getROLLBACK_SQL_DTME(), entity.getSQL(), entity.getSQL_PARAM(), entity.getTRANSACTION_SIGN(), entity.getSQL_LEVEL(), entity.getSQL_IS_OUT_TIME(), entity.getSQL_TYPE(), entity.getSQL_KEY_VALUE(), entity.getSQL_ID(), entity.getBIZ_REDIS_KEY(), entity.getSQL_STATUS(), entity.getBIZ_REDIS_VALUE(), entity.getSQL_TIMEOUT()};
        sql = sql + SqlUtil.assembleInsertValuesSql(args.length);
        if (this.jdbcTemplate.update(sql, args) < 1) {
            throw new DatabaseOperateException(Constants.SUB_SYSTEM, ExceptionType.DOE30084, "transaction_sql_log 表插入失败");
        }
    }

    public List<TRANSACTION_SQL_LOG> queryByTransactionId(Long transactionId) {
        String sql = this.mycatGoMaster + "select series,transaction_id,transaction_db_id,source_db,`sql`,sql_is_out_time,sql_key_value,sql_id,biz_redis_key,sql_type,table_name,sql_status,biz_redis_value from transaction_sql_log where TRANSACTION_ID = ? and TRANSACTION_SIGN = 'N' order by transaction_db_id,sql_level desc";
        return this.jdbcTemplate.query(sql, new Object[]{transactionId}, new BeanPropertyRowMapper(TRANSACTION_SQL_LOG.class));
    }

    public void updateRollbackFailReason(Long series, String transactionErrorLog) {
        String sql = this.mycatGoMaster + "update transaction_sql_log set rollback_sql_dtme=sysdate(),transaction_error_log=?,transaction_sign='N' where series=?";
        int i = this.jdbcTemplate.update(sql, new Object[]{transactionErrorLog, series});
        if (i < 1) {
            throw new DatabaseOperateException(Constants.SUB_SYSTEM, ExceptionType.DOE30084, "分布式事务服务端更新事务sql表失败，series:" + series);
        }
    }

    public List<TRANSACTION_SQL_LOG> queryTransactionSignByTransactionId(Long transactionId) {
        String sql = this.mycatGoMaster + "select transaction_sign,series from transaction_sql_log where transaction_id=?";
        List list = this.jdbcTemplate.query(sql, new Object[]{transactionId}, new BeanPropertyRowMapper(TRANSACTION_SQL_LOG.class));
        return list;
    }

    public void delete(Long transactionId) {
        String sql = "delete from transaction_sql_log where transaction_id=?";
        this.jdbcTemplate.update(sql, new Object[]{transactionId});
    }

    public Integer queryCountByTransactionId(Long transactionId) {
        String sql = this.mycatGoMaster + "select count(1) from transaction_sql_log where TRANSACTION_ID = ?";
        return (Integer)this.jdbcTemplate.queryForObject(sql, new Object[]{transactionId}, Integer.class);
    }

    public Integer queryUnCompletedCountByTransactionId(Long transactionId) {
        String sql = this.mycatGoMaster + "select count(1) from transaction_sql_log where TRANSACTION_ID = ? and TRANSACTION_SIGN='N' ";
        return (Integer)this.jdbcTemplate.queryForObject(sql, new Object[]{transactionId}, Integer.class);
    }

    public void i(Long series) {
        String sql = this.mycatGoMaster + "update transaction_sql_log set rollback_sql_dtme=sysdate(),transaction_error_log='事务提交成功',transaction_sign='Y' where SERIES=?";
        int i = this.jdbcTemplate.update(sql, new Object[]{series});
        if (i < 1) {
            throw new DatabaseOperateException(Constants.SUB_SYSTEM, ExceptionType.DOE30084, "分布式事务服务端更新事务sql表失败，series:" + series);
        }
    }

    public void a(Long series, Integer resultNum) {
        String var3 = this.mycatGoMaster + "update transaction_sql_log set rollback_sql_dtme=sysdate(),transaction_error_log='事务提交成功',transaction_sign='Y',result_num=? where SERIES=?";
        int var4 = this.jdbcTemplate.update(var3, new Object[]{resultNum, series});
        if (var4 < 1) {
            throw new DatabaseOperateException(Constants.SUB_SYSTEM, ExceptionType.DOE30084, "分布式事务服务端更新事务sql表失败，series:" + series);
        }
    }

    public List<TRANSACTION_SQL_LOG> f(String var1) {
        List var2 = this.jdbcTemplate.query(var1, new BeanPropertyRowMapper(TRANSACTION_SQL_LOG.class));
        return var2;
    }

    public Long g(String var1) {
        return (Long)this.jdbcTemplate.queryForObject(var1, Long.class);
    }
}

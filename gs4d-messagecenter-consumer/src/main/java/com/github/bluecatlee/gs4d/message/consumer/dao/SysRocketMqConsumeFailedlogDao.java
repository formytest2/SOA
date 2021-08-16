package com.github.bluecatlee.gs4d.message.consumer.dao;

import com.github.bluecatlee.gs4d.message.consumer.model.SysRocketMqConsumeFailedlog;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Service
public class SysRocketMqConsumeFailedlogDao {

    @Resource(name = "jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    private static final String insertSql = "INSERT INTO SYS_ROCKET_MQ_CONSUMER_FAILEDLOG(MSG_SERIES,TYPE_ID,ALREADY_DEAL,TENANT_NUM_ID,DATA_SIGN) values (?,?,'N',?,?)";
    private static final String queryUnDealSql = "SELECT MSG_SERIES,TYPE_ID,TENANT_NUM_ID,DATA_SIGN FROM SYS_ROCKET_MQ_CONSUMER_FAILEDLOG WHERE ALREADY_DEAL='N' and TENANT_NUM_ID=? and DATA_SIGN=? LIMIT ?";
    private static final String updateSql = " UPDATE SYS_ROCKET_MQ_CONSUMER_FAILEDLOG SET ALREADY_DEAL=? WHERE MSG_SERIES=? AND TYPE_ID=? AND  TENANT_NUM_ID=? AND DATA_SIGN=? ";
    private static final String deleteSql = " DELETE FROM SYS_ROCKET_MQ_CONSUMER_FAILEDLOG where ALREADY_DEAL='Y' and TENANT_NUM_ID=? and DATA_SIGN=? ";

    public void insert(Long msgSeries, Long typeId, Long tenantNumId, Long dataSign) {
        Object[] args = new Object[]{msgSeries, typeId, tenantNumId, dataSign};
        if (this.jdbcTemplate.update(insertSql, args) <= 0) {
            throw new RuntimeException("插入消费失败日志失败!");
        }
    }

    public List<SysRocketMqConsumeFailedlog> queryUnDeal(Integer rows, Long tenantNumId, Long dataSign) {
        List list = this.jdbcTemplate.query(queryUnDealSql, new Object[]{tenantNumId, dataSign, rows}, new BeanPropertyRowMapper(SysRocketMqConsumeFailedlog.class));
        return list;
    }

    public void batchUpdate(final List<SysRocketMqConsumeFailedlog> list) {
        this.jdbcTemplate.batchUpdate(updateSql, new BatchPreparedStatementSetter() {
            public int getBatchSize() {
                return list.size();
            }

            public void setValues(PreparedStatement ps, int i) throws SQLException {
                SysRocketMqConsumeFailedlog entity = (SysRocketMqConsumeFailedlog)list.get(i);
                int index = 1;
                ps.setString(index++, "Y");
                ps.setLong(index++, entity.getMSG_SERIES());
                ps.setLong(index++, entity.getTYPE_ID());
                ps.setLong(index++, entity.getTENANT_NUM_ID());
                ps.setLong(index++, entity.getDATA_SIGN());
            }
        });
    }

    public void delete(Long tenantNumId, Long dataSign) {
        this.jdbcTemplate.update(deleteSql, new Object[]{tenantNumId, dataSign});
    }
}


package com.github.bluecatlee.gs4d.message.producer.dao;

import com.github.bluecatlee.gs4d.message.producer.model.SYS_ROCKET_MQ_SEND_LOG;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Service
public class SysTransationFailedLogDao {

    @Resource(name = "jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    private static final String insertSql = "INSERT INTO SYS_TRANSATION_FAILED_LOG(SERIES,MESSAGE_ID,MESSAGE_KEY,MESSAGE_TOPIC,MESSAGE_TAG,MESSAGE_BODY,MESSAGE_NAME_ADDR,PRODUCER_NAME,CREATE_USER_ID,LAST_UPDATE_USER_ID,CANCELSIGN,FAIL_DETAIL,TENANT_NUM_ID,DATA_SIGN,WORKFLOW_ID,INSTANCE_ID,STEP_ID,FROM_SYSTEM,MSG_STATUS,CREATE_DTME,LAST_UPDTME,CONSUMER_SUCCESS,RETRY_TIMES,CONSUMER_SUCCESS_TIME,TASK_TARGET,ORDER_MESS_FLAG,RESPONSE_DETAIL,CLIENT_IP) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,sysdate(),?,?,?,?)";
    private static final String insertSql2 = "INSERT INTO SYS_TRANSATION_FAILED_LOG(SERIES,MESSAGE_ID,MESSAGE_KEY,MESSAGE_TOPIC,MESSAGE_TAG,MESSAGE_BODY,MESSAGE_NAME_ADDR,PRODUCER_NAME,CREATE_USER_ID,LAST_UPDATE_USER_ID,CANCELSIGN,FAIL_DETAIL,TENANT_NUM_ID,DATA_SIGN,WORKFLOW_ID,INSTANCE_ID,STEP_ID,FROM_SYSTEM,MSG_STATUS,CREATE_DTME,CLIENT_IP,CONSUMER_SUCCESS_TIME,ORDER_MESS_FLAG) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,sysdate(),?,?,?)";
    private static final String queryByStepIdSql = "SELECT COUNT(1) FROM SYS_TRANSATION_FAILED_LOG WHERE TENANT_NUM_ID=? AND DATA_SIGN=? AND STEP_ID=?";
    private static final String countSql = "select count(*) from SYS_TRANSATION_FAILED_LOG where order_mess_flag=2 and msg_status=2 and sysdate()-consumer_success_time>0 limit 100000";
    private static final String querySql = "select SERIES,MESSAGE_ID,MESSAGE_KEY,MESSAGE_TOPIC,MESSAGE_TAG,MESSAGE_BODY,MESSAGE_NAME_ADDR,PRODUCER_NAME,CREATE_DTME,LAST_UPDTME,CREATE_USER_ID,CANCELSIGN,FAIL_DETAIL,TENANT_NUM_ID,DATA_SIGN,WORKFLOW_ID,INSTANCE_ID,STEP_ID,FROM_SYSTEM,MSG_STATUS,CONSUMER_SUCCESS,RETRY_TIMES,RESPONSE_DETAIL,LAST_UPDTME,ORDER_MESS_FLAG,TASK_TARGET,CLIENT_IP from SYS_TRANSATION_FAILED_LOG where order_mess_flag=2 and msg_status=2 and sysdate()-consumer_success_time>0 limit ?,?";
    private static final String deleteSql = "delete from SYS_TRANSATION_FAILED_LOG where series = ?";
    private static final String querySql2 = "select * from SYS_TRANSATION_FAILED_LOG where cancelsign !='S' and create_dtme between ? and ? limit ?,?";
    private static final String updateSSql = "update SYS_TRANSATION_FAILED_LOG set cancelsign='S' where series=?";
    private static final String queryByTopicTagSql = "select SERIES,MESSAGE_ID,MESSAGE_KEY,MESSAGE_TOPIC,MESSAGE_TAG,MESSAGE_BODY,MESSAGE_NAME_ADDR,PRODUCER_NAME,CREATE_DTME,LAST_UPDTME,CREATE_USER_ID,CANCELSIGN,FAIL_DETAIL,TENANT_NUM_ID,DATA_SIGN,WORKFLOW_ID,INSTANCE_ID,STEP_ID,FROM_SYSTEM,MSG_STATUS,CONSUMER_SUCCESS,RETRY_TIMES,RESPONSE_DETAIL,LAST_UPDTME,CONSUMER_SUCCESS_TIME,ORDER_MESS_FLAG,TASK_TARGET,CLIENT_IP,sysdate() NOW_TIME from  SYS_TRANSATION_FAILED_LOG where MESSAGE_TOPIC=? and MESSAGE_TAG=? and TENANT_NUM_ID=? and DATA_SIGN=?";

    public void insert(SYS_ROCKET_MQ_SEND_LOG entity) {
        Object[] args = new Object[]{entity.getSERIES(), entity.getMESSAGE_ID(), entity.getMESSAGE_KEY(), entity.getMESSAGE_TOPIC(), entity.getMESSAGE_TAG(), entity.getMESSAGE_BODY(), entity.getMESSAGE_NAME_ADDR(), entity.getPRODUCER_NAME(), entity.getCREATE_USER_ID(), entity.getLAST_UPDATE_USER_ID(), entity.getCANCELSIGN(), entity.getFAIL_DETAIL(), entity.getTENANT_NUM_ID(), entity.getDATA_SIGN(), entity.getWORKFLOW_ID(), entity.getINSTANCE_ID(), entity.getSTEP_ID(), entity.getFROM_SYSTEM(), entity.getMSG_STATUS(), entity.getCREATE_DTME(), entity.getLAST_UPDTME(), entity.getCONSUMER_SUCCESS(), entity.getRETRY_TIMES(), entity.getTASK_TARGET(), entity.getORDER_MESS_FLAG(), entity.getRESPONSE_DETAIL(), entity.getCLIENT_IP()};
        if(this.jdbcTemplate.update(insertSql, args) <= 0) {
            throw new RuntimeException("插入失败!");
        }
    }

    public Integer queryByStepId(Long tenantNumId, Long dataSign, Integer stepId) {
        Integer count = this.jdbcTemplate.queryForObject(queryByStepIdSql, new Object[]{tenantNumId, dataSign, stepId}, Integer.class);
        return count;
    }

    public List<SYS_ROCKET_MQ_SEND_LOG> batchInsert(final List<SYS_ROCKET_MQ_SEND_LOG> list) {
        this.jdbcTemplate.batchUpdate(insertSql2, new BatchPreparedStatementSetter() {
            public int getBatchSize() {
                return list.size();
            }

            public void setValues(PreparedStatement ps, int i) throws SQLException {
                SYS_ROCKET_MQ_SEND_LOG entity = (SYS_ROCKET_MQ_SEND_LOG)list.get(i);
                int index = 1;
                ps.setLong(index++, entity.getSERIES().longValue());
                ps.setString(index++, entity.getMESSAGE_ID());
                ps.setString(index++, entity.getMESSAGE_KEY());
                ps.setString(index++, entity.getMESSAGE_TOPIC());
                ps.setString(index++, entity.getMESSAGE_TAG());
                ps.setString(index++, entity.getMESSAGE_BODY());
                ps.setString(index++, entity.getMESSAGE_NAME_ADDR());
                ps.setString(index++, entity.getPRODUCER_NAME());
                ps.setLong(index++, entity.getCREATE_USER_ID().longValue());
                ps.setLong(index++, entity.getLAST_UPDATE_USER_ID().longValue());
                ps.setString(index++, entity.getCANCELSIGN());
                ps.setString(index++, entity.getFAIL_DETAIL());
                ps.setLong(index++, entity.getTENANT_NUM_ID().longValue());
                ps.setLong(index++, entity.getDATA_SIGN().longValue());
                ps.setLong(index++, entity.getWORKFLOW_ID().longValue());
                ps.setLong(index++, entity.getINSTANCE_ID().longValue());
                ps.setInt(index++, entity.getSTEP_ID().intValue());
                ps.setString(index++, entity.getFROM_SYSTEM());
                ps.setInt(index++, entity.getMSG_STATUS().intValue());
                ps.setString(index++, entity.getCLIENT_IP());
                ps.setTimestamp(index++, new Timestamp(entity.getCONSUMER_SUCCESS_TIME().getTime()));
                ps.setLong(index++, entity.getORDER_MESS_FLAG().longValue());
            }
        });
        return list;
    }

    public Long getAllJobMessage() {
        Long count = (Long)this.jdbcTemplate.queryForObject(countSql, Long.class);
        return count;
    }

    public List<SYS_ROCKET_MQ_SEND_LOG> queryByPage(Long offset, Long rows) {
        List list = this.jdbcTemplate.query(querySql, new Object[]{offset, rows}, new BeanPropertyRowMapper(SYS_ROCKET_MQ_SEND_LOG.class));
        return list;
    }

    public List<SYS_ROCKET_MQ_SEND_LOG> batchDelete(final List<SYS_ROCKET_MQ_SEND_LOG> list) {
        this.jdbcTemplate.batchUpdate(deleteSql, new BatchPreparedStatementSetter() {
            public int getBatchSize() {
                return list.size();
            }

            public void setValues(PreparedStatement ps, int i) throws SQLException {
                SYS_ROCKET_MQ_SEND_LOG record = (SYS_ROCKET_MQ_SEND_LOG)list.get(i);
                ps.setLong(1, record.getSERIES().longValue());
            }
        });
        return list;
    }

    public List<SYS_ROCKET_MQ_SEND_LOG> queryS(String startTime, String endTime, Integer offset, int rows) {
        List list = this.jdbcTemplate.query(querySql2, new Object[]{startTime, endTime, offset, Integer.valueOf(rows)}, new BeanPropertyRowMapper(SYS_ROCKET_MQ_SEND_LOG.class));
        return list;
    }

    public void updateCancelsignS(Long series) {
        this.jdbcTemplate.update(updateSSql, new Object[]{series});
    }

    public List<SYS_ROCKET_MQ_SEND_LOG> queryByTopicTag(String topic, String tag, Long tenantNumId, Long dataSign) {
        List list = this.jdbcTemplate.query(queryByTopicTagSql, new Object[]{topic, tag, tenantNumId, dataSign}, new BeanPropertyRowMapper(SYS_ROCKET_MQ_SEND_LOG.class));
        return list;
    }

    public void delete(Long series) {
        this.jdbcTemplate.update(deleteSql, new Object[]{series});
    }
}


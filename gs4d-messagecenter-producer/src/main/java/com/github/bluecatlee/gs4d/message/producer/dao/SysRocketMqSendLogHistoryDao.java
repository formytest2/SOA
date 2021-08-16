package com.github.bluecatlee.gs4d.message.producer.dao;

import com.github.bluecatlee.gs4d.common.exception.DatabaseOperateException;
import com.github.bluecatlee.gs4d.common.exception.ExceptionType;
import com.github.bluecatlee.gs4d.message.api.model.MessageTopicDetailModel;
import com.github.bluecatlee.gs4d.message.producer.model.SYS_ROCKET_MQ_SEND_LOG;
import com.github.bluecatlee.gs4d.message.producer.utils.Constants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Service
public class SysRocketMqSendLogHistoryDao {

    @Resource(name = "jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Value("#{settings['mycatGoMaster']}")
    private String mycatGoMaster;

    private static final String insertSql = "INSERT INTO SYS_ROCKET_MQ_SEND_LOG_HISTORY(SERIES,MESSAGE_ID,MESSAGE_KEY,MESSAGE_TOPIC,MESSAGE_TAG,MESSAGE_BODY,MESSAGE_NAME_ADDR,PRODUCER_NAME,CREATE_USER_ID,LAST_UPDATE_USER_ID,CANCELSIGN,FAIL_DETAIL,TENANT_NUM_ID,DATA_SIGN,WORKFLOW_ID,INSTANCE_ID,STEP_ID,FROM_SYSTEM,MSG_STATUS,CREATE_DTME,LAST_UPDTME,CONSUMER_SUCCESS,RETRY_TIMES,CONSUMER_SUCCESS_TIME,TASK_TARGET,ORDER_MESS_FLAG,RESPONSE_DETAIL,CLIENT_IP) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,sysdate(),?,?,?,?)";
    private static final String insertSql2 = "INSERT INTO SYS_ROCKET_MQ_SEND_LOG_HISTORY(SERIES,MESSAGE_KEY,MESSAGE_TOPIC,MESSAGE_TAG,MESSAGE_BODY,MESSAGE_NAME_ADDR,PRODUCER_NAME,CREATE_USER_ID,LAST_UPDATE_USER_ID,CANCELSIGN,TENANT_NUM_ID,DATA_SIGN,FROM_SYSTEM,MSG_STATUS,CREATE_DTME,LAST_UPDTME,CLIENT_IP,ORDER_MESS_FLAG,STEP_ID) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,sysdate(),?,?,?)";
    private static final String querySql = "select SERIES,MESSAGE_ID,MESSAGE_KEY,MESSAGE_TOPIC,MESSAGE_TAG,MESSAGE_BODY,MESSAGE_NAME_ADDR,PRODUCER_NAME,CREATE_DTME,LAST_UPDTME,CREATE_USER_ID,CANCELSIGN,FAIL_DETAIL,TENANT_NUM_ID,DATA_SIGN,WORKFLOW_ID,INSTANCE_ID,STEP_ID,FROM_SYSTEM,MSG_STATUS,CONSUMER_SUCCESS,RETRY_TIMES,RESPONSE_DETAIL,LAST_UPDTME,CONSUMER_SUCCESS_TIME,ORDER_MESS_FLAG,TASK_TARGET,CLIENT_IP from SYS_ROCKET_MQ_SEND_LOG_HISTORY where SERIES=?";
    private static final String deleteSql = "delete from sys_rocket_mq_send_log_history  where series=?";

    public void insert(SYS_ROCKET_MQ_SEND_LOG entity) {
        Object[] args = new Object[]{entity.getSERIES(), entity.getMESSAGE_ID(), entity.getMESSAGE_KEY(), entity.getMESSAGE_TOPIC(), entity.getMESSAGE_TAG(), entity.getMESSAGE_BODY(), entity.getMESSAGE_NAME_ADDR(), entity.getPRODUCER_NAME(), entity.getCREATE_USER_ID(), entity.getLAST_UPDATE_USER_ID(), entity.getCANCELSIGN(), entity.getFAIL_DETAIL(), entity.getTENANT_NUM_ID(), entity.getDATA_SIGN(), entity.getWORKFLOW_ID(), entity.getINSTANCE_ID(), entity.getSTEP_ID(), entity.getFROM_SYSTEM(), entity.getMSG_STATUS(), entity.getCREATE_DTME(), entity.getLAST_UPDTME(), entity.getCONSUMER_SUCCESS(), entity.getRETRY_TIMES(), entity.getTASK_TARGET(), entity.getORDER_MESS_FLAG(), entity.getRESPONSE_DETAIL(), entity.getCLIENT_IP()};
        if(this.jdbcTemplate.update(insertSql, args) <= 0) {
            throw new RuntimeException("插入失败!");
        }
    }

    public List<MessageTopicDetailModel> query(Long fromSystem, String startTime, String endTime, Long tenantNumId, Long dataSign, String messageTags) {
        String sql = "select count(*)messageNum,count(case when (msg_status=1)  then msg_status end)messageCounsumerSuccessNum,count(case when (msg_status=4)  then msg_status end)messageCancelNum,  AVG(UNIX_TIMESTAMP(last_updtme) - UNIX_TIMESTAMP(create_dtme)) averageConsumerTime, message_topic as topic ,message_tag as tag  from (select msg_status,message_topic ,message_tag,last_updtme,create_dtme  from sys_rocket_mq_send_log_history where message_tag in (#) and from_system=? and create_dtme between ? and ?  and  tenant_num_id=? and data_sign=?  limit 10000000000)aa  group by message_topic,message_tag";
        sql = sql.replaceAll("#", messageTags);
        List list = this.jdbcTemplate.query(sql, new Object[]{fromSystem, startTime, endTime, tenantNumId, dataSign}, new BeanPropertyRowMapper(MessageTopicDetailModel.class));
        return list;
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
                ps.setString(index++, entity.getMESSAGE_KEY());
                ps.setString(index++, entity.getMESSAGE_TOPIC());
                ps.setString(index++, entity.getMESSAGE_TAG());
                ps.setString(index++, entity.getMESSAGE_BODY());
                ps.setString(index++, entity.getMESSAGE_NAME_ADDR());
                ps.setString(index++, entity.getPRODUCER_NAME());
                ps.setLong(index++, entity.getCREATE_USER_ID().longValue());
                ps.setLong(index++, entity.getLAST_UPDATE_USER_ID().longValue());
                ps.setString(index++, entity.getCANCELSIGN());
                ps.setLong(index++, entity.getTENANT_NUM_ID().longValue());
                ps.setLong(index++, entity.getDATA_SIGN().longValue());
                ps.setString(index++, entity.getFROM_SYSTEM());
                ps.setInt(index++, 4);
                ps.setString(index++, entity.getCREATE_DTME());
                ps.setString(index++, entity.getCLIENT_IP());
                ps.setLong(index++, entity.getORDER_MESS_FLAG().longValue());
                ps.setLong(index++, (long)entity.getSTEP_ID().intValue());
            }
        });
        return list;
    }

    public SYS_ROCKET_MQ_SEND_LOG queryBySeriesStrict(Long series) {
        List list = this.jdbcTemplate.query(this.mycatGoMaster + querySql, new Object[]{series}, new BeanPropertyRowMapper(SYS_ROCKET_MQ_SEND_LOG.class));
        if(list != null && list.size() != 0) {
            return (SYS_ROCKET_MQ_SEND_LOG)list.get(0);
        } else {
            throw new DatabaseOperateException(Constants.SUB_SYSTEM, ExceptionType.DOE30054, "查询消息中心发送日志序列号：" + series + "，获取实体失败");
        }
    }

    public List<SYS_ROCKET_MQ_SEND_LOG> queryBySeries(Long series) {
        List list = this.jdbcTemplate.query(querySql, new Object[]{series}, new BeanPropertyRowMapper(SYS_ROCKET_MQ_SEND_LOG.class));
        return list;
    }

    public void deleteBySeries(Long series) {
        if(this.jdbcTemplate.update(deleteSql, new Object[]{series}) <= 0) {
            throw new DatabaseOperateException(Constants.SUB_SYSTEM, ExceptionType.DOE30054, series + "删除消息失败!");
        }
    }
}


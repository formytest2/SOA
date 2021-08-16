package com.github.bluecatlee.gs4d.message.consumer.dao;

import com.github.bluecatlee.gs4d.common.exception.DatabaseOperateException;
import com.github.bluecatlee.gs4d.common.exception.ExceptionType;
import com.github.bluecatlee.gs4d.message.api.model.MessageConsumerFailedModel;
import com.github.bluecatlee.gs4d.message.api.model.MessageDetails;
import com.github.bluecatlee.gs4d.message.api.model.MessageTopicDetailModel;
import com.github.bluecatlee.gs4d.message.consumer.model.SysRocketMqSendLog;
import com.github.bluecatlee.gs4d.message.consumer.utils.Constants;
import com.github.bluecatlee.gs4d.message.consumer.utils.SeqUtil;
import com.github.bluecatlee.gs4d.sequence.exception.SequenceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class SysRocketMqSendLogDao {

    @Resource(name = "jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Value("#{settings['dataSign']}")
    private Long dataSign;
    @Value("#{settings['mycatGoMaster']}")
    private String mycatGoMaster;

    private static final String insertSql = "INSERT INTO SYS_ROCKET_MQ_SEND_LOG(SERIES,MESSAGE_ID,MESSAGE_KEY,MESSAGE_TOPIC,MESSAGE_TAG,MESSAGE_BODY,MESSAGE_NAME_ADDR,PRODUCER_NAME,CREATE_USER_ID,LAST_UPDATE_USER_ID,CANCELSIGN,FAIL_DETAIL,TENANT_NUM_ID,DATA_SIGN,WORKFLOW_ID,INSTANCE_ID,STEP_ID,FROM_SYSTEM,MSG_STATUS,CREATE_DTME,CLIENT_IP) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,sysdate(),?)";
    private static final String updateSendResSql = "update sys_rocket_mq_send_log set message_id=?,msg_status=?,order_mess_flag=?,step_id=? where series=?";
    private static final String updateConsumerSql = "update sys_rocket_mq_send_log set consumer_success='Y',consumer_success_time=sysdate(),task_target=?,response_detail=? where series=?";
    private static final String updateStatusSql = "update sys_rocket_mq_send_log set msg_status=? where series=?";
    private static final String updateConsumeFailSql = "update sys_rocket_mq_send_log set retry_times=(retry_times+1),task_target=?,consumer_success='N',consumer_success_time=sysdate(),response_detail=?,next_retry_interval=?,instance_id=?,step_id=? where series=?";
    private static final String queryBySeriesSql = "select SERIES,MESSAGE_ID,MESSAGE_KEY,MESSAGE_TOPIC,MESSAGE_TAG,MESSAGE_BODY,MESSAGE_NAME_ADDR,PRODUCER_NAME,CREATE_DTME,LAST_UPDTME,CREATE_USER_ID,CANCELSIGN,FAIL_DETAIL,TENANT_NUM_ID,DATA_SIGN,WORKFLOW_ID,INSTANCE_ID,STEP_ID,FROM_SYSTEM,MSG_STATUS,CONSUMER_SUCCESS,RETRY_TIMES,RESPONSE_DETAIL,LAST_UPDTME,CONSUMER_SUCCESS_TIME,ORDER_MESS_FLAG,TASK_TARGET,CLIENT_IP,sysdate() NOW_TIME from SYS_ROCKET_MQ_SEND_LOG where SERIES=?";
    private static final String querySendFailedSql = "select SERIES,MESSAGE_ID,MESSAGE_KEY,MESSAGE_TOPIC,MESSAGE_TAG,MESSAGE_BODY,MESSAGE_NAME_ADDR,PRODUCER_NAME,CREATE_DTME,LAST_UPDTME,CREATE_USER_ID,CANCELSIGN,FAIL_DETAIL,TENANT_NUM_ID,DATA_SIGN,WORKFLOW_ID,INSTANCE_ID,STEP_ID,FROM_SYSTEM,MSG_STATUS,CONSUMER_SUCCESS,RETRY_TIMES,RESPONSE_DETAIL,LAST_UPDTME,CONSUMER_SUCCESS_TIME,ORDER_MESS_FLAG,TASK_TARGET,CLIENT_IP from SYS_ROCKET_MQ_SEND_LOG where MSG_STATUS=3 and tenant_num_id=? and data_sign=? limit ?,?";
    private static final String deleteSql = "delete from sys_rocket_mq_send_log  where series=?";
    private static final String queryUnConsumedSql = "select SERIES,MESSAGE_ID,MESSAGE_KEY,MESSAGE_TOPIC,MESSAGE_TAG,MESSAGE_BODY,MESSAGE_NAME_ADDR,PRODUCER_NAME,CREATE_DTME,LAST_UPDTME,CREATE_USER_ID,CANCELSIGN,FAIL_DETAIL,TENANT_NUM_ID,DATA_SIGN,WORKFLOW_ID,INSTANCE_ID,STEP_ID,FROM_SYSTEM,MSG_STATUS,CONSUMER_SUCCESS,RETRY_TIMES,RESPONSE_DETAIL,LAST_UPDTME,CONSUMER_SUCCESS_TIME,ORDER_MESS_FLAG,TASK_TARGET,CLIENT_IP from sys_rocket_mq_send_log where  data_sign=? and message_topic=? and message_tag=? and consumer_success=''  and tenant_num_id=? and CREATE_DTME<date_add(now(), interval - 5 minute) and msg_status=2  limit 2000";
    private static final String querySql = "select a.series,a.message_id as messageId ,a.message_key as messageKey,a.create_dtme as createTime ,a.message_body as messageBody,a.response_detail as messageConsumerResult ,a.consumer_success_time as consumerSuccessTime,a.retry_times as retryTimes, (case when (consumer_success='N') then '消费失败' else case when (consumer_success='Y') then '消费成功' else  case when(consumer_success ='') then '未消费'end end end) as messageIsConsumer,a.client_ip as clientIP,a.message_topic as topic,a.message_tag as tag,a.from_system as systemName,a.step_id as sendType,instance_id as consumerTime from SYS_ROCKET_MQ_SEND_LOG a ";
    private static final String queryHisSql = "select a.series,a.message_id as messageId ,a.message_key as messageKey,a.create_dtme as createTime ,a.message_body as messageBody,a.response_detail as messageConsumerResult ,a.consumer_success_time as consumerSuccessTime,a.retry_times as retryTimes, (case when (consumer_success='N') then '消费失败' else case when (consumer_success='Y') then '消费成功' else  case when(consumer_success ='') then '未消费'end end end) as messageIsConsumer,a.client_ip as clientIP,a.message_topic as topic,a.message_tag as tag,a.from_system as systemName,a.step_id as sendType,instance_id as consumerTime from SYS_ROCKET_MQ_SEND_LOG_HISTORY a ";
    private static final String queryCountSql = "select count(*) from SYS_ROCKET_MQ_SEND_LOG a ";
    private static final String queryHisCountSql = "select count(*) from SYS_ROCKET_MQ_SEND_LOG_HISTORY a ";
    private static final String getMessageIsTryingNowSql = "select count(*)messageIsTryingNow from sys_rocket_mq_send_log where message_topic=? and message_tag=? and tenant_num_id=? and data_sign=? and retry_times<? and consumer_success='N'";
    private static final String queryCountByRetrytimesSql = "select count(*) from SYS_ROCKET_MQ_SEND_LOG a where message_topic=? and message_tag=? and tenant_num_id=? and data_sign=? and retry_times >= ? and retry_times < ? limit 1000000";

    public SysRocketMqSendLog insert(SysRocketMqSendLog entity) {
        Object[] args = new Object[]{entity.getSERIES(), entity.getMESSAGE_ID(), entity.getMESSAGE_KEY(), entity.getMESSAGE_TOPIC(), entity.getMESSAGE_TAG(), entity.getMESSAGE_BODY(), entity.getMESSAGE_NAME_ADDR(), entity.getPRODUCER_NAME(), entity.getCREATE_USER_ID(), entity.getLAST_UPDATE_USER_ID(), entity.getCANCELSIGN(), entity.getFAIL_DETAIL(), entity.getTENANT_NUM_ID(), entity.getDATA_SIGN(), entity.getWORKFLOW_ID(), entity.getINSTANCE_ID(), entity.getSTEP_ID(), entity.getFROM_SYSTEM(), entity.getMSG_STATUS(), entity.getCLIENT_IP()};
        if (this.jdbcTemplate.update(insertSql, args) <= 0) {
            throw new RuntimeException("插入失败!");
        } else {
            return entity;
        }
    }

    public List<SysRocketMqSendLog> batchInsert(final List<SysRocketMqSendLog> list) {
        this.jdbcTemplate.batchUpdate(insertSql, new BatchPreparedStatementSetter() {
            public int getBatchSize() {
                return list.size();
            }

            public void setValues(PreparedStatement ps, int i) throws SQLException {
                SysRocketMqSendLog entity = (SysRocketMqSendLog)list.get(i);
                int index = 1;
                ps.setLong(index++, entity.getSERIES());
                ps.setString(index++, entity.getMESSAGE_ID());
                ps.setString(index++, entity.getMESSAGE_KEY());
                ps.setString(index++, entity.getMESSAGE_TOPIC());
                ps.setString(index++, entity.getMESSAGE_TAG());
                ps.setString(index++, entity.getMESSAGE_BODY());
                ps.setString(index++, entity.getMESSAGE_NAME_ADDR());
                ps.setString(index++, entity.getPRODUCER_NAME());
                ps.setLong(index++, entity.getCREATE_USER_ID());
                ps.setLong(index++, entity.getLAST_UPDATE_USER_ID());
                ps.setString(index++, entity.getCANCELSIGN());
                ps.setString(index++, entity.getFAIL_DETAIL());
                ps.setLong(index++, entity.getTENANT_NUM_ID());
                ps.setLong(index++, entity.getDATA_SIGN());
                ps.setLong(index++, entity.getWORKFLOW_ID());
                ps.setLong(index++, entity.getINSTANCE_ID());
                ps.setInt(index++, entity.getSTEP_ID());
                ps.setString(index++, entity.getFROM_SYSTEM());
                ps.setInt(index++, entity.getMSG_STATUS());
                ps.setString(index++, entity.getCLIENT_IP());
            }
        });
        return list;
    }

    public void update(SysRocketMqSendLog sysRocketMqSendLog) {
        if (this.jdbcTemplate.update(this.mycatGoMaster + updateSendResSql, new Object[]{sysRocketMqSendLog.getMESSAGE_ID(), sysRocketMqSendLog.getMSG_STATUS(), sysRocketMqSendLog.getORDER_MESS_FLAG(), sysRocketMqSendLog.getSERIES(), sysRocketMqSendLog.getSTEP_ID()}) <= 0) {
            throw new RuntimeException("成功发送消息，更新发送流水SYS_ROCKET_MQ_FAIL_LOG失败");
        }
    }

    public void updateSendFail(SysRocketMqSendLog sysRocketMqSendLog) {
        String sql = this.mycatGoMaster + "update sys_rocket_mq_send_log set fail_detail=?,msg_status=?,order_mess_flag=? where series=?";
        if (this.jdbcTemplate.update(sql, new Object[]{sysRocketMqSendLog.getFAIL_DETAIL(), sysRocketMqSendLog.getMSG_STATUS(), sysRocketMqSendLog.getORDER_MESS_FLAG(), sysRocketMqSendLog.getSERIES()}) <= 0) {
            throw new RuntimeException("失败发送消息，更新发送流水SYS_ROCKET_MQ_FAIL_LOG失败");
        }
    }

    public List<SysRocketMqSendLog> batchUpdate(final List<SysRocketMqSendLog> list) {
        this.jdbcTemplate.batchUpdate(updateStatusSql, new BatchPreparedStatementSetter() {
            public int getBatchSize() {
                return list.size();
            }

            public void setValues(PreparedStatement ps, int i) throws SQLException {
                SysRocketMqSendLog entity = (SysRocketMqSendLog)list.get(i);
                int index = 1;
                ps.setInt(index++, entity.getMSG_STATUS());
                ps.setLong(index++, entity.getSERIES());
            }
        });
        return list;
    }

    public SysRocketMqSendLog setSeries(SysRocketMqSendLog sysRocketMqSendLog, Long tenantNumId) throws SequenceException {
        sysRocketMqSendLog.setSERIES(SeqUtil.getNoSubSequence(SeqUtil.SYS_ROCKET_MQ_SEND_LOG_SERIES));
        return sysRocketMqSendLog;
    }

    public void updateConsumeSuccess(String taskTarget, String responseDetail, Long series) {
        try {
            this.jdbcTemplate.update(this.mycatGoMaster + updateConsumerSql, new Object[]{taskTarget, responseDetail, series});
        } catch (Exception e) {
            throw new RuntimeException("成功消费消息，更新发送流水SYS_ROCKET_MQ_FAIL_LOG失败,序号为" + series, e);
        }
    }

    public void update(String taskTarget, String responseDetail, Long series, String nextRetryInterval, Long instanceId, Integer stepId) {
        try {
            ArrayList args = new ArrayList();
            args.add(taskTarget);
            args.add(responseDetail);
            args.add(nextRetryInterval);
            args.add(instanceId);
            args.add(stepId);
            args.add(series);
            Integer i = this.jdbcTemplate.update(this.mycatGoMaster + updateConsumeFailSql, args.toArray());
            if (i <= 0) {
                throw new Exception("消息中心消费消息，更新log失败，sys_rocket_mq_send_log没有当前序列");
            }
        } catch (Exception e) {
            throw new RuntimeException("失败消费消息，更新发送流水sys_rocket_mq_send_log失败,序号为" + series, e);
        }
    }

    public SysRocketMqSendLog queryBySeriesStrict(Long series) {
        List list = this.jdbcTemplate.query(this.mycatGoMaster + queryBySeriesSql, new Object[]{series}, new BeanPropertyRowMapper(SysRocketMqSendLog.class));
        if (list != null && list.size() != 0) {
            return (SysRocketMqSendLog)list.get(0);
        } else {
            throw new DatabaseOperateException(Constants.SUB_SYSTEM, ExceptionType.DOE30054, "查询消息中心发送日志序列号：" + series + "，获取实体失败");
        }
    }

    public List<SysRocketMqSendLog> querySendFailed(Integer offset, Integer rows, Long tenantNumId, Long dataSign) {
        List list = this.jdbcTemplate.query(querySendFailedSql, new Object[]{tenantNumId, dataSign, offset, rows}, new BeanPropertyRowMapper(SysRocketMqSendLog.class));
        return list;
    }

    public void delete(Long series) {
        if (this.jdbcTemplate.update(deleteSql, new Object[]{series}) <= 0) {
            throw new DatabaseOperateException(Constants.SUB_SYSTEM, ExceptionType.DOE30054, series + "删除消息失败!");
        }
    }

    public List<SysRocketMqSendLog> queryUnConsumed(Long dataSign, String topic, String tag, Long tenantNumId) {
        List list = this.jdbcTemplate.query(queryUnConsumedSql, new Object[]{dataSign, topic, tag, tenantNumId}, new BeanPropertyRowMapper(SysRocketMqSendLog.class));
        return list;
    }

    public SysRocketMqSendLog queryBySeries(long series) {
        String sql = "select * from sys_rocket_mq_send_log  where series = ?";
        List list = this.jdbcTemplate.query(sql, new Object[]{series}, new BeanPropertyRowMapper(SysRocketMqSendLog.class));
        return list.size() == 1 ? (SysRocketMqSendLog)list.get(0) : null;
    }

    public List<MessageDetails> query(String condition) {
        String sql = querySql + condition;
        List list = this.jdbcTemplate.query(sql, new BeanPropertyRowMapper(MessageDetails.class));
        return list;
    }

    public List<MessageDetails> queryHistory(String condition) {
        String sql = queryHisSql + condition;
        List list = this.jdbcTemplate.query(sql, new BeanPropertyRowMapper(MessageDetails.class));
        return list;
    }

    public Long queryCount(String condition) {
        String sql = queryCountSql + condition;
        Long count = (Long)this.jdbcTemplate.queryForObject(sql, Long.class);
        return count;
    }

    public Long queryHistoryCount(String condition) {
        String sql = queryHisCountSql + condition + " limit 100000000";
        Long count = (Long)this.jdbcTemplate.queryForObject(sql, Long.class);
        return count;
    }

    public List<MessageTopicDetailModel> count(String startTime, String endTime, Long tenantNumId, Long dataSign, String tags) {
        String sql = "select count(*)messageNum,count(case when (msg_status=2) then msg_status end)messageNoConfirmNum, count(case when (msg_status=1) then msg_status end)messageConfirmNum  ,count(case when (consumer_success='N' and msg_status=1) then consumer_success end)messageCounsumerFailedNum,  count(case when (consumer_success ='' and msg_status=1) then consumer_success end)messageNoCounsumerNum,message_topic as topic ,message_tag as tag  from (select msg_status,consumer_success,message_topic ,message_tag  from sys_rocket_mq_send_log where message_tag in (#) and create_dtme between ? and ?  and  tenant_num_id=? and data_sign=?  limit 10000000000)aa  group by message_topic,message_tag";
        sql = sql.replaceAll("#", tags);
        List list = this.jdbcTemplate.query(sql, new Object[]{startTime, endTime, tenantNumId, dataSign}, new BeanPropertyRowMapper(MessageTopicDetailModel.class));
        return list;
    }

    public Long getMessageIsTryingNow(String messageTopic, String messageTag, Integer retryTimes, Long tenantNumId, Long dataSign) {
        List list = this.jdbcTemplate.query(getMessageIsTryingNowSql, new Object[]{messageTopic, messageTag, tenantNumId, dataSign, retryTimes}, new BeanPropertyRowMapper(MessageTopicDetailModel.class));
        return list.isEmpty() ? 0L : ((MessageTopicDetailModel)list.get(0)).getMessageIsTryingNow();
    }

    public List<SysRocketMqSendLog> batchDelete(final List<SysRocketMqSendLog> list) {
        this.jdbcTemplate.batchUpdate("delete from sys_rocket_mq_send_log  where series=?", new BatchPreparedStatementSetter() {
            public int getBatchSize() {
                return list.size();
            }

            public void setValues(PreparedStatement ps, int i) throws SQLException {
                SysRocketMqSendLog entity = (SysRocketMqSendLog)list.get(i);
                ps.setLong(1, entity.getSERIES());
            }
        });
        return list;
    }

    public Long queryCountByRetrytimes(String topic, String tag, Long tenantNumId, Long dataSign, Integer minRetryTimes, Integer maxRetryTimes) {
        Long count = (Long)this.jdbcTemplate.queryForObject(queryCountByRetrytimesSql, new Object[]{topic, tag, tenantNumId, dataSign, minRetryTimes, maxRetryTimes}, Long.class);
        return count;
    }

    public List<SysRocketMqSendLog> queryConsumeFailedByRetrytimes(String topic, String tag, Long tenantNumId, Long dataSign, Integer minRetryTimes, Integer maxRetryTimes) {
        String sql = "select SERIES,MESSAGE_ID,MESSAGE_KEY,MESSAGE_TOPIC,MESSAGE_TAG,MESSAGE_BODY,MESSAGE_NAME_ADDR,PRODUCER_NAME,CREATE_DTME,LAST_UPDTME,CREATE_USER_ID,CANCELSIGN,FAIL_DETAIL,TENANT_NUM_ID,DATA_SIGN,WORKFLOW_ID,INSTANCE_ID,STEP_ID,FROM_SYSTEM,MSG_STATUS,CONSUMER_SUCCESS,RETRY_TIMES,RESPONSE_DETAIL,LAST_UPDTME,CONSUMER_SUCCESS_TIME,ORDER_MESS_FLAG,TASK_TARGET,CLIENT_IP from SYS_ROCKET_MQ_SEND_LOG where message_topic=? and message_tag=? and tenant_num_id=? and data_sign=? and consumer_success='N' and retry_times >= ? and retry_times< ? limit 2000";
        List list = this.jdbcTemplate.query(sql, new Object[]{topic, tag, tenantNumId, dataSign, minRetryTimes, maxRetryTimes}, new BeanPropertyRowMapper(SysRocketMqSendLog.class));
        return list;
    }

    public List<String> querySeries(String messageTopic, String messageTag, Long tenantNumId, Long dataSign, String startTime, String endTime) {
        String sql = this.mycatGoMaster + "select s.series  from sys_rocket_mq_send_log s  inner join platform_mq_topic t on s.message_topic = t.topic  and s.message_tag = t.tag  where s.message_topic = ?  and s.message_tag = ?  and s.tenant_num_id = ?  and s.data_sign = ?  and s.retry_times >=  ( CASE \tWHEN (s.data_sign = 0) THEN \t\tt.retries + 1 \tELSE \t\tCASE \tWHEN (s.data_sign = 1) THEN \t\tt.retries_test + 1 \tEND \tEND )  and s.consumer_success = 'N'  and s.create_dtme between ?  and ?  order by s.series desc  limit 5000";
        List list = this.jdbcTemplate.queryForList(sql, new Object[]{messageTopic, messageTag, tenantNumId, dataSign, startTime, endTime}, String.class);
        return list;
    }

    public List<MessageConsumerFailedModel> queryConsumeFailedCountByTopicTag(Long interval, Long tenantNumId, Long dataSign) {
        String sql = "select count(*) consumerFailedNum,message_topic topic,message_tag tag from sys_rocket_mq_send_log where tenant_num_id=? and data_sign=? and  consumer_success ='N' and CREATE_DTME between date_add(now(), interval - ? hour) and now() group by message_tag,message_topic limit 10000";
        List list = this.jdbcTemplate.query(sql, new Object[]{tenantNumId, dataSign, interval}, new BeanPropertyRowMapper(MessageConsumerFailedModel.class));
        return list;
    }

    public Long queryByTimeRange(String startTime, String endTime, Long tenantNumId, Long dataSign) {
        String sql = "select count(*) from sys_rocket_mq_send_log where create_dtme between ? and ?  and tenant_num_id=? and data_sign=? limit 100000000";
        Long list = (Long)this.jdbcTemplate.queryForObject(sql, new Object[]{startTime, endTime, tenantNumId, dataSign}, Long.class);
        return list;
    }

    public List<SysRocketMqSendLog> queryByTimeRange(String startTime, String endTime, int offset, Long rows, Long tenantNumId, Long dataSign) {
        String sql = "select SERIES,MESSAGE_ID,MESSAGE_KEY,MESSAGE_TOPIC,MESSAGE_TAG,MESSAGE_BODY,MESSAGE_NAME_ADDR,PRODUCER_NAME,CREATE_DTME,LAST_UPDTME,CREATE_USER_ID,CANCELSIGN,FAIL_DETAIL,TENANT_NUM_ID,DATA_SIGN,WORKFLOW_ID,INSTANCE_ID,STEP_ID,FROM_SYSTEM,MSG_STATUS,CONSUMER_SUCCESS,RETRY_TIMES,RESPONSE_DETAIL,LAST_UPDTME,CONSUMER_SUCCESS_TIME,ORDER_MESS_FLAG,TASK_TARGET,CLIENT_IP from SYS_ROCKET_MQ_SEND_LOG where  create_dtme  between  ?  and  ? and tenant_num_id=? and data_sign=? order by create_dtme limit ?,?";
        List list = this.jdbcTemplate.query(this.mycatGoMaster + sql, new Object[]{startTime, endTime, tenantNumId, dataSign, offset, rows}, new BeanPropertyRowMapper(SysRocketMqSendLog.class));
        return list;
    }

    public List<SysRocketMqSendLog> queryUnDealByTopicTagAndTimeRange(String startTime, String endTime, String topic, String tag, Long tenantNumId, Long dataSign) {
        String sql = "select SERIES,MESSAGE_ID,MESSAGE_KEY,MESSAGE_TOPIC,MESSAGE_TAG,MESSAGE_BODY,MESSAGE_NAME_ADDR,PRODUCER_NAME,CREATE_DTME,LAST_UPDTME,CREATE_USER_ID,CANCELSIGN,FAIL_DETAIL,TENANT_NUM_ID,DATA_SIGN,WORKFLOW_ID,INSTANCE_ID,STEP_ID,FROM_SYSTEM,MSG_STATUS,CONSUMER_SUCCESS,RETRY_TIMES,RESPONSE_DETAIL,LAST_UPDTME,CONSUMER_SUCCESS_TIME,ORDER_MESS_FLAG,TASK_TARGET,CLIENT_IP from SYS_ROCKET_MQ_SEND_LOG where  create_dtme  between  ?  and  ? and message_topic=? and message_tag=? and tenant_num_id=? and data_sign=?   and message_id!='' and  consumer_success_time is null  order by create_dtme   limit 100000000";
        List list = this.jdbcTemplate.query(sql, new Object[]{startTime, endTime, topic, tag, tenantNumId, dataSign}, new BeanPropertyRowMapper(SysRocketMqSendLog.class));
        return list;
    }

    public List<SysRocketMqSendLog> queryUnDealByTopicTag(String topic, String tag, Long tenantNumId, Long dataSign) {
        String sql = "select SERIES,MESSAGE_ID,MESSAGE_KEY,MESSAGE_TOPIC,MESSAGE_TAG,MESSAGE_BODY,MESSAGE_NAME_ADDR,PRODUCER_NAME,CREATE_DTME,LAST_UPDTME,CREATE_USER_ID,CANCELSIGN,FAIL_DETAIL,TENANT_NUM_ID,DATA_SIGN,WORKFLOW_ID,INSTANCE_ID,STEP_ID,FROM_SYSTEM,MSG_STATUS,CONSUMER_SUCCESS,RETRY_TIMES,RESPONSE_DETAIL,LAST_UPDTME,CONSUMER_SUCCESS_TIME,ORDER_MESS_FLAG,TASK_TARGET,CLIENT_IP from SYS_ROCKET_MQ_SEND_LOG where  message_topic=? and message_tag=? and tenant_num_id=? and data_sign=?   and message_id!='' and  consumer_success_time is null  and CREATE_DTME<date_add(now(), interval - 1 hour) and  cancelsign='N' order by create_dtme   limit 100000000";
        List list = this.jdbcTemplate.query(sql, new Object[]{topic, tag, tenantNumId, dataSign}, new BeanPropertyRowMapper(SysRocketMqSendLog.class));
        return list;
    }

    public void cancel(Long series) {
        Object[] args = new Object[]{series};
        String sql = "update sys_rocket_mq_send_log set cancelsign='Y' where series=? ";
        this.jdbcTemplate.update(sql, args);
    }

    public Long queryCount(String topic, String tag, String key, Long tenantNumId, Long dataSign) {
        String sql = "select count(*) from sys_rocket_mq_send_log where message_topic=? and message_tag=? and message_key=? and tenant_num_id=? and data_sign=?";
        Long count = (Long)this.jdbcTemplate.queryForObject(sql, new Object[]{topic, tag, key, tenantNumId, dataSign}, Long.class);
        return count;
    }
}


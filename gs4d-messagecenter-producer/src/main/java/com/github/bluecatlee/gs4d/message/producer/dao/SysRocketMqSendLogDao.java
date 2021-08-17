package com.github.bluecatlee.gs4d.message.producer.dao;

import com.github.bluecatlee.gs4d.common.exception.DatabaseOperateException;
import com.github.bluecatlee.gs4d.common.exception.ExceptionType;
import com.github.bluecatlee.gs4d.message.api.model.MessageConsumerFailedModel;
import com.github.bluecatlee.gs4d.message.api.model.MessageDetails;
import com.github.bluecatlee.gs4d.message.api.model.MessageTopicDetailModel;
import com.github.bluecatlee.gs4d.message.producer.model.SYS_ROCKET_MQ_SEND_LOG;
import com.github.bluecatlee.gs4d.message.producer.utils.Constants;
import com.github.bluecatlee.gs4d.message.producer.utils.JdbcUtils;
import com.github.bluecatlee.gs4d.message.producer.utils.SeqUtil;
import com.github.bluecatlee.gs4d.sequence.exception.SequenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class SysRocketMqSendLogDao {

    @Resource(name = "jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private JdbcUtils jdbcUtils;

    @Value("#{settings['dataSign']}")
    private Long dataSign;

    @Value("#{settings['mycatGoMaster']}")
    private String mycatGoMaster;

    private static final String insertSql = "INSERT INTO SYS_ROCKET_MQ_SEND_LOG(SERIES,MESSAGE_ID,MESSAGE_KEY,MESSAGE_TOPIC,MESSAGE_TAG,MESSAGE_BODY,MESSAGE_NAME_ADDR,PRODUCER_NAME,CREATE_USER_ID,LAST_UPDATE_USER_ID,CANCELSIGN,FAIL_DETAIL,TENANT_NUM_ID,DATA_SIGN,WORKFLOW_ID,INSTANCE_ID,STEP_ID,FROM_SYSTEM,MSG_STATUS,CREATE_DTME,CLIENT_IP,ORDER_MESS_FLAG) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,sysdate(),?,?)";
    private static final String updateMsgIdAndMsgStatusSqlAndMessFlag = "update sys_rocket_mq_send_log set message_id=?,msg_status=?,order_mess_flag=? where series=?";
    private static final String updateConsumeSuccessSql = "update sys_rocket_mq_send_log set consumer_success='Y',consumer_success_time=sysdate(),task_target=?,response_detail=? where series=?";
    private static final String updateMsgStatusSql = "update sys_rocket_mq_send_log set msg_status=? where series=?";
    private static final String updateConsumeFailSql = "update sys_rocket_mq_send_log set retry_times=(retry_times+1),task_target=?,consumer_success='N',consumer_success_time=sysdate(),response_detail=?,next_retry_interval=?,instance_id=? where series=?";
    private static final String querySql = "select SERIES,MESSAGE_ID,MESSAGE_KEY,MESSAGE_TOPIC,MESSAGE_TAG,MESSAGE_BODY,MESSAGE_NAME_ADDR,PRODUCER_NAME,CREATE_DTME,LAST_UPDTME,CREATE_USER_ID,CANCELSIGN,FAIL_DETAIL,TENANT_NUM_ID,DATA_SIGN,WORKFLOW_ID,INSTANCE_ID,STEP_ID,FROM_SYSTEM,MSG_STATUS,CONSUMER_SUCCESS,RETRY_TIMES,RESPONSE_DETAIL,LAST_UPDTME,CONSUMER_SUCCESS_TIME,ORDER_MESS_FLAG,TASK_TARGET,CLIENT_IP,sysdate() NOW_TIME from SYS_ROCKET_MQ_SEND_LOG where SERIES=?";
    private static final String querySendFaildLogSql = "select SERIES,MESSAGE_ID,MESSAGE_KEY,MESSAGE_TOPIC,MESSAGE_TAG,MESSAGE_BODY,MESSAGE_NAME_ADDR,PRODUCER_NAME,CREATE_DTME,LAST_UPDTME,CREATE_USER_ID,CANCELSIGN,FAIL_DETAIL,TENANT_NUM_ID,DATA_SIGN,WORKFLOW_ID,INSTANCE_ID,STEP_ID,FROM_SYSTEM,MSG_STATUS,CONSUMER_SUCCESS,RETRY_TIMES,RESPONSE_DETAIL,LAST_UPDTME,CONSUMER_SUCCESS_TIME,ORDER_MESS_FLAG,TASK_TARGET,CLIENT_IP from SYS_ROCKET_MQ_SEND_LOG where MSG_STATUS=3 and tenant_num_id=? and data_sign=? limit ?,?";
    private static final String updateInstanceIdSql = "update sys_rocket_mq_send_log set instance_id=? where series=?";
    private static final String deleteSql = "delete from sys_rocket_mq_send_log  where series=?";
    private static final String queryUnHandledSendLogsSql = "select SERIES,MESSAGE_ID,MESSAGE_KEY,MESSAGE_TOPIC,MESSAGE_TAG,MESSAGE_BODY,MESSAGE_NAME_ADDR,PRODUCER_NAME,CREATE_DTME,LAST_UPDTME,CREATE_USER_ID,CANCELSIGN,FAIL_DETAIL,TENANT_NUM_ID,DATA_SIGN,WORKFLOW_ID,INSTANCE_ID,STEP_ID,FROM_SYSTEM,MSG_STATUS,CONSUMER_SUCCESS,RETRY_TIMES,RESPONSE_DETAIL,LAST_UPDTME,CONSUMER_SUCCESS_TIME,ORDER_MESS_FLAG,TASK_TARGET,CLIENT_IP from sys_rocket_mq_send_log where  data_sign=? and message_topic=? and message_tag=? and consumer_success=''  and tenant_num_id=? and CREATE_DTME<date_add(now(), interval - 5 minute) and msg_status in (2,5) and instance_id>=0 limit 2000";
    private static final String queryMessageDetails = "select a.series,a.message_id as messageId ,a.message_key as messageKey,a.create_dtme as createTime ,a.message_body as messageBody,a.response_detail as messageConsumerResult ,a.consumer_success_time as consumerSuccessTime,a.retry_times as retryTimes, (case when (consumer_success='N') then '消费失败' else case when (consumer_success='Y') then '消费成功' else  case when(consumer_success ='') then '未消费'end end end) as messageIsConsumer,a.client_ip as clientIP,a.message_topic as topic,a.message_tag as tag,a.from_system as systemName from SYS_ROCKET_MQ_SEND_LOG a ";
    private static final String queryMessageDetailsFromHis = "select a.series,a.message_id as messageId ,a.message_key as messageKey,a.create_dtme as createTime ,a.message_body as messageBody,a.response_detail as messageConsumerResult ,a.consumer_success_time as consumerSuccessTime,a.retry_times as retryTimes, (case when (consumer_success='N') then '消费失败' else case when (consumer_success='Y') then '消费成功' else  case when(consumer_success ='') then '未消费'end end end) as messageIsConsumer,a.client_ip as clientIP,a.message_topic as topic,a.message_tag as tag,a.from_system as systemName from SYS_ROCKET_MQ_SEND_LOG_HISTORY a ";
    private static final String queryCount = "select count(*) from SYS_ROCKET_MQ_SEND_LOG a ";
    private static final String queryHistoryCount = "select count(*) from SYS_ROCKET_MQ_SEND_LOG_HISTORY a ";
    private static final String getMessageIsTryingNowSql = "select count(*)messageIsTryingNow from sys_rocket_mq_send_log where message_topic=? and message_tag=? and tenant_num_id=? and data_sign=? and retry_times<? and consumer_success='N'";
    private static final String queryCountByRetryTimesRangeSql = "select count(*) from SYS_ROCKET_MQ_SEND_LOG a where message_topic=? and message_tag=? and tenant_num_id=? and data_sign=? and retry_times >= ? and retry_times < ? limit 1000000";

    public SYS_ROCKET_MQ_SEND_LOG insert(SYS_ROCKET_MQ_SEND_LOG entity) {
        Object[] args = new Object[]{
                entity.getSERIES(),
                entity.getMESSAGE_ID(),
                entity.getMESSAGE_KEY(),
                entity.getMESSAGE_TOPIC(),
                entity.getMESSAGE_TAG(),
                entity.getMESSAGE_BODY(),
                entity.getMESSAGE_NAME_ADDR(),
                entity.getPRODUCER_NAME(),
                entity.getCREATE_USER_ID(),
                entity.getLAST_UPDATE_USER_ID(),
                entity.getCANCELSIGN(),
                entity.getFAIL_DETAIL(),
                entity.getTENANT_NUM_ID(),
                entity.getDATA_SIGN(),
                entity.getWORKFLOW_ID(),
                entity.getINSTANCE_ID(),
                entity.getSTEP_ID(),
                entity.getFROM_SYSTEM(),
                entity.getMSG_STATUS(),
                entity.getCLIENT_IP(),
                entity.getORDER_MESS_FLAG()
        };
        List jdbcTemplates = this.jdbcUtils.getJdbcTemplates();
        BigInteger series = (new BigInteger(entity.getSERIES().toString())).abs();
        Integer index = series.mod(BigInteger.valueOf((long)jdbcTemplates.size())).intValue();
        JdbcTemplate jdbcTemplate = (JdbcTemplate)jdbcTemplates.get(index);
        if (jdbcTemplate.update(insertSql, args) <= 0) {
            throw new RuntimeException("插入失败!");
        } else {
            return entity;
        }
    }

    public List<SYS_ROCKET_MQ_SEND_LOG> batchInsert(final List<SYS_ROCKET_MQ_SEND_LOG> list) {
        this.jdbcTemplate.batchUpdate(insertSql, new BatchPreparedStatementSetter() {
            public int getBatchSize() {
                return list.size();
            }

            public void setValues(PreparedStatement ps, int i) throws SQLException {
                SYS_ROCKET_MQ_SEND_LOG entity = (SYS_ROCKET_MQ_SEND_LOG)list.get(i);
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
                ps.setLong(index++, entity.getORDER_MESS_FLAG());
            }
        });
        return list;
    }

    public void updateMsgIdAndMsgStatusSqlAndMessFlag(SYS_ROCKET_MQ_SEND_LOG entity) {
        this.jdbcTemplate.update(this.mycatGoMaster + updateMsgIdAndMsgStatusSqlAndMessFlag, new Object[]{entity.getMESSAGE_ID(), entity.getMSG_STATUS(), entity.getORDER_MESS_FLAG(), entity.getSERIES()});
    }

    public void updateStatus(Long series, String failDetail, Integer msgStatus, Long orderMessFlag) {
        String sql = this.mycatGoMaster + "update sys_rocket_mq_send_log set fail_detail=?,msg_status=?,order_mess_flag=? where series=?";
        if (this.jdbcTemplate.update(sql, new Object[]{failDetail, msgStatus, orderMessFlag, series}) <= 0) {
            throw new RuntimeException("失败发送消息，更新发送流水SYS_ROCKET_MQ_FAIL_LOG失败");
        }
    }

    public List<SYS_ROCKET_MQ_SEND_LOG> batchUpdateStatus(final List<SYS_ROCKET_MQ_SEND_LOG> list) {
        this.jdbcTemplate.batchUpdate(updateMsgStatusSql, new BatchPreparedStatementSetter() {
            public int getBatchSize() {
                return list.size();
            }

            public void setValues(PreparedStatement ps, int i) throws SQLException {
                SYS_ROCKET_MQ_SEND_LOG entity = (SYS_ROCKET_MQ_SEND_LOG)list.get(i);
                ps.setInt(1, entity.getMSG_STATUS());
                ps.setLong(2, entity.getSERIES());
            }
        });
        return list;
    }

    public SYS_ROCKET_MQ_SEND_LOG setSeries(SYS_ROCKET_MQ_SEND_LOG entity, Long tenantNumId) throws SequenceException {
        entity.setSERIES(SeqUtil.getNoSubSequence(SeqUtil.SYS_ROCKET_MQ_SEND_LOG_SERIES));
        return entity;
    }

    public void updateConsumeSuccess(String taskTarget, String responseDetail, Long series) {
        try {
            this.jdbcTemplate.update(this.mycatGoMaster + updateConsumeSuccessSql,
                    new Object[]{taskTarget, responseDetail, series});
        } catch (Exception e) {
            throw new RuntimeException("成功消费消息，更新发送流水SYS_ROCKET_MQ_FAIL_LOG失败,序号为" + series, e);
        }
    }

    public void updateConsumeFail(String taskTarget, String responseDetail, Long series, String nextRetryInterval, Long instanceId) {
        try {
            ArrayList args = new ArrayList();
            args.add(taskTarget);
            args.add(responseDetail);
            args.add(nextRetryInterval);
            args.add(instanceId);
            args.add(series);
            Integer i = this.jdbcTemplate.update(this.mycatGoMaster + updateConsumeFailSql, args.toArray());
            if (i <= 0) {
                throw new Exception("消息中心消费消息，更新log失败，sys_rocket_mq_send_log没有当前序列");
            }
        } catch (Exception e) {
            throw new RuntimeException("失败消费消息，更新发送流水sys_rocket_mq_send_log失败,序号为" + series, e);
        }
    }

    public SYS_ROCKET_MQ_SEND_LOG queryBySeriesStrict(Long series) {
        List list = this.jdbcTemplate.query(this.mycatGoMaster + querySql, new Object[]{series}, new BeanPropertyRowMapper(SYS_ROCKET_MQ_SEND_LOG.class));
        if (list != null && list.size() != 0) {
            return (SYS_ROCKET_MQ_SEND_LOG)list.get(0);
        } else {
            throw new DatabaseOperateException(Constants.SUB_SYSTEM, ExceptionType.DOE30054, "查询消息中心发送日志序列号：" + series + "，获取实体失败");
        }
    }

    public List<SYS_ROCKET_MQ_SEND_LOG> querySendFaildLogByPage(Integer offset, Integer count, Long tenantNumId, Long dataSign) {
        List list = this.jdbcTemplate.query(querySendFaildLogSql, new Object[]{tenantNumId, dataSign, offset, count}, new BeanPropertyRowMapper(SYS_ROCKET_MQ_SEND_LOG.class));
        return list;
    }

    public void deleteBySeries(Long series) {
        if (this.jdbcTemplate.update(deleteSql, new Object[]{series}) <= 0) {
            throw new DatabaseOperateException(Constants.SUB_SYSTEM, ExceptionType.DOE30054, series + "删除消息失败!");
        }
    }

    public List<SYS_ROCKET_MQ_SEND_LOG> queryUnHandledSendLogsByTopicAndTag(Long dataSign, String messageTopic, String messageTag, Long tenantNumId) {
        List list = this.jdbcTemplate.query(queryUnHandledSendLogsSql, new Object[]{dataSign, messageTopic, messageTag, tenantNumId}, new BeanPropertyRowMapper(SYS_ROCKET_MQ_SEND_LOG.class));
        return list;
    }

    public SYS_ROCKET_MQ_SEND_LOG queryBySeries(long series) {
        String sql = "select * from sys_rocket_mq_send_log  where series = ?";
        List list = this.jdbcTemplate.query(sql, new Object[]{series}, new BeanPropertyRowMapper(SYS_ROCKET_MQ_SEND_LOG.class));
        return list.size() == 1 ? (SYS_ROCKET_MQ_SEND_LOG)list.get(0) : null;
    }

    public List<MessageDetails> queryMessageDetails(String condition) {
        String sql = queryMessageDetails + condition;
        List list = this.jdbcTemplate.query(sql, new BeanPropertyRowMapper(MessageDetails.class));
        return list;
    }

    public List<MessageDetails> queryMessageDetailsFromHis(String condition) {
        String sql = queryMessageDetailsFromHis + condition;
        List list = this.jdbcTemplate.query(sql, new BeanPropertyRowMapper(MessageDetails.class));
        return list;
    }

    public Long queryCount(String condition) {
        String sql = queryCount + condition + " limit 100000000";
        Long count = (Long)this.jdbcTemplate.queryForObject(sql, Long.class);
        return count;
    }

    public Long queryHistoryCount(String condition) {
        String sql = queryHistoryCount + condition + " limit 100000000";
        Long count = (Long)this.jdbcTemplate.queryForObject(sql, Long.class);
        return count;
    }

    public List<MessageTopicDetailModel> count(Long fromSystem, String startTime, String endTime, Long tenantNumId, Long dataSign, String messageTags) {
        String sql = "select count(*)messageNum,count(case when (msg_status=2) then msg_status end)messageNoConfirmNum, count(case when (msg_status=1) then msg_status end)messageConfirmNum  ,count(case when (consumer_success='N' and msg_status=1) then consumer_success end)messageCounsumerFailedNum,  count(case when (consumer_success ='' and msg_status=1) then consumer_success end)messageNoCounsumerNum,message_topic as topic ,message_tag as tag  from (select msg_status,consumer_success,message_topic ,message_tag  from sys_rocket_mq_send_log where message_tag in (#) and from_system=? and create_dtme between ? and ?  and  tenant_num_id=? and data_sign=?  limit 10000000000)aa  group by message_topic,message_tag";
        sql = sql.replaceAll("#", messageTags);
        List list = this.jdbcTemplate.query(sql, new Object[]{fromSystem, startTime, endTime, tenantNumId, dataSign}, new BeanPropertyRowMapper(MessageTopicDetailModel.class));
        return list;
    }

    public Long getMessageIsTryingNow(String messageTopic, String messageTag, Integer retryTimes, Long tenantNumId, Long dataSign) {
        List list = this.jdbcTemplate.query(getMessageIsTryingNowSql, new Object[]{messageTopic, messageTag, tenantNumId, dataSign, retryTimes}, new BeanPropertyRowMapper(MessageTopicDetailModel.class));
        return list.isEmpty() ? 0L : ((MessageTopicDetailModel)list.get(0)).getMessageIsTryingNow();
    }

    public List<SYS_ROCKET_MQ_SEND_LOG> batchDelete(final List<SYS_ROCKET_MQ_SEND_LOG> list) {
        this.jdbcTemplate.batchUpdate(deleteSql, new BatchPreparedStatementSetter() {
            public int getBatchSize() {
                return list.size();
            }

            public void setValues(PreparedStatement ps, int i) throws SQLException {
                SYS_ROCKET_MQ_SEND_LOG entity = (SYS_ROCKET_MQ_SEND_LOG)list.get(i);
                ps.setLong(1, entity.getSERIES());
            }
        });
        return list;
    }

    public Long queryCountByRetryTimesRange(String messageTopic, String messageTag, Long tenantNumId, Long dataSign, Integer minRetryTimes, Integer maxRetryTimes) {
        Long count = (Long)this.jdbcTemplate.queryForObject(this.queryCountByRetryTimesRangeSql, new Object[]{messageTopic, messageTag, tenantNumId, dataSign, minRetryTimes, maxRetryTimes}, Long.class);
        return count;
    }

    public List<SYS_ROCKET_MQ_SEND_LOG> queryListByRetryTimesRange(String messageTopic, String messageTag, Long tenantNumId, Long dataSign, Integer minRetryTimes, Integer maxRetryTimes) {
        String sql = "select SERIES,MESSAGE_ID,MESSAGE_KEY,MESSAGE_TOPIC,MESSAGE_TAG,MESSAGE_BODY,MESSAGE_NAME_ADDR,PRODUCER_NAME,CREATE_DTME,LAST_UPDTME,CREATE_USER_ID,CANCELSIGN,FAIL_DETAIL,TENANT_NUM_ID,DATA_SIGN,WORKFLOW_ID,INSTANCE_ID,STEP_ID,FROM_SYSTEM,MSG_STATUS,CONSUMER_SUCCESS,RETRY_TIMES,RESPONSE_DETAIL,LAST_UPDTME,CONSUMER_SUCCESS_TIME,ORDER_MESS_FLAG,TASK_TARGET,CLIENT_IP from SYS_ROCKET_MQ_SEND_LOG where message_topic=? and message_tag=? and tenant_num_id=? and data_sign=? and consumer_success='N' and retry_times >= ? and retry_times< ? limit 100";
        List list = this.jdbcTemplate.query(sql, new Object[]{messageTopic, messageTag, tenantNumId, dataSign, minRetryTimes, maxRetryTimes}, new BeanPropertyRowMapper(SYS_ROCKET_MQ_SEND_LOG.class));
        return list;
    }

    public List<String> querySeriesWhichExceedRetries(String messageTopic, String messageTag, Long tenantNumId, Long dataSign, String startTime, String endTime) {
        String sql = this.mycatGoMaster + "SELECT s.series FROM sys_rocket_mq_send_log s INNER JOIN platform_mq_topic t ON s.message_topic = t.topic AND s.message_tag = t.tag WHERE s.message_topic = ? AND s.message_tag = ? AND s.tenant_num_id = ? AND s.data_sign = ? AND s.retry_times >= ( CASE WHEN (s.data_sign = 0) THEN t.retries + 1 ELSE CASE WHEN (s.data_sign = 1) THEN t.retries_test + 1 END END ) AND s.consumer_success = 'N' AND s.create_dtme BETWEEN ? AND ? ORDER BY s.series DESC LIMIT 5000";
        List seriesList = this.jdbcTemplate.queryForList(sql, new Object[]{messageTopic, messageTag, tenantNumId, dataSign, startTime, endTime}, String.class);
        return seriesList;
    }

    public List<MessageConsumerFailedModel> queryFailedCountOfTopicTag(Long interval, Long tenantNumId, Long dataSign) {
        String sql = "select count(*) consumerFailedNum,message_topic topic,message_tag tag from sys_rocket_mq_send_log where tenant_num_id=? and data_sign=? and  consumer_success ='N' and CREATE_DTME between date_add(now(), interval - ? hour) and now() group by message_tag,message_topic limit 10000";
        List list = this.jdbcTemplate.query(sql, new Object[]{tenantNumId, dataSign, interval}, new BeanPropertyRowMapper(MessageConsumerFailedModel.class));
        return list;
    }

    public Long queryCountByStartEndTime(String startTime, String endTime, Long tenantNumId, Long dataSign) {
        String sql = "select count(*) from sys_rocket_mq_send_log where create_dtme between ? and ?  and tenant_num_id=? and data_sign=? limit 100000000";
        Long count = this.jdbcTemplate.queryForObject(sql, new Object[]{startTime, endTime, tenantNumId, dataSign}, Long.class);
        return count;
    }

    public List<SYS_ROCKET_MQ_SEND_LOG> queryListByStartEndTime(String startTime, String endTime, int offset, Long count, Long tenantNumId, Long dataSign) {
        String sql = "select SERIES,MESSAGE_ID,MESSAGE_KEY,MESSAGE_TOPIC,MESSAGE_TAG,MESSAGE_BODY,MESSAGE_NAME_ADDR,PRODUCER_NAME,CREATE_DTME,LAST_UPDTME,CREATE_USER_ID,CANCELSIGN,FAIL_DETAIL,TENANT_NUM_ID,DATA_SIGN,WORKFLOW_ID,INSTANCE_ID,STEP_ID,FROM_SYSTEM,MSG_STATUS,CONSUMER_SUCCESS,RETRY_TIMES,RESPONSE_DETAIL,LAST_UPDTME,CONSUMER_SUCCESS_TIME,ORDER_MESS_FLAG,TASK_TARGET,CLIENT_IP from SYS_ROCKET_MQ_SEND_LOG where  create_dtme  between  ?  and  ? and tenant_num_id=? and data_sign=? order by create_dtme limit ?,?";
        List list = this.jdbcTemplate.query(this.mycatGoMaster + sql, new Object[]{startTime, endTime, tenantNumId, dataSign, offset, count}, new BeanPropertyRowMapper(SYS_ROCKET_MQ_SEND_LOG.class));
        return list;
    }

    public List<SYS_ROCKET_MQ_SEND_LOG> queryUnConsumedSendLogs(String startTime, String endTime, String messageTopic, String messageTag, Long tenantNumId, Long dataSign) {
        String sql = "select SERIES,MESSAGE_ID,MESSAGE_KEY,MESSAGE_TOPIC,MESSAGE_TAG,MESSAGE_BODY,MESSAGE_NAME_ADDR,PRODUCER_NAME,CREATE_DTME,LAST_UPDTME,CREATE_USER_ID,CANCELSIGN,FAIL_DETAIL,TENANT_NUM_ID,DATA_SIGN,WORKFLOW_ID,INSTANCE_ID,STEP_ID,FROM_SYSTEM,MSG_STATUS,CONSUMER_SUCCESS,RETRY_TIMES,RESPONSE_DETAIL,LAST_UPDTME,CONSUMER_SUCCESS_TIME,ORDER_MESS_FLAG,TASK_TARGET,CLIENT_IP from SYS_ROCKET_MQ_SEND_LOG where  create_dtme  between  ?  and  ? and message_topic=? and message_tag=? and tenant_num_id=? and data_sign=?   and message_id!='' and  consumer_success_time is null  order by create_dtme   limit 100000000";
        List list = this.jdbcTemplate.query(sql, new Object[]{startTime, endTime, messageTopic, messageTag, tenantNumId, dataSign}, new BeanPropertyRowMapper(SYS_ROCKET_MQ_SEND_LOG.class));
        return list;
    }

    public List<SYS_ROCKET_MQ_SEND_LOG> queryUnConsumedSendLogs(String messageTopic, String messageTag, Long tenantNumId, Long dataSign) {
        String sql = "select SERIES,MESSAGE_ID,MESSAGE_KEY,MESSAGE_TOPIC,MESSAGE_TAG,MESSAGE_BODY,MESSAGE_NAME_ADDR,PRODUCER_NAME,CREATE_DTME,LAST_UPDTME,CREATE_USER_ID,CANCELSIGN,FAIL_DETAIL,TENANT_NUM_ID,DATA_SIGN,WORKFLOW_ID,INSTANCE_ID,STEP_ID,FROM_SYSTEM,MSG_STATUS,CONSUMER_SUCCESS,RETRY_TIMES,RESPONSE_DETAIL,LAST_UPDTME,CONSUMER_SUCCESS_TIME,ORDER_MESS_FLAG,TASK_TARGET,CLIENT_IP from SYS_ROCKET_MQ_SEND_LOG where  message_topic=? and message_tag=? and tenant_num_id=? and data_sign=?  and message_id!='' and  consumer_success_time is null  and CREATE_DTME<date_add(now(), interval - 1 hour) and  cancelsign='N' and instance_id >=0 order by create_dtme   limit 500";
        List list = this.jdbcTemplate.query(sql, new Object[]{messageTopic, messageTag, tenantNumId, dataSign}, new BeanPropertyRowMapper(SYS_ROCKET_MQ_SEND_LOG.class));
        return list;
    }

    public void cancelBySeries(Long series) {
        Object[] args = new Object[]{series};
        String sql = "update sys_rocket_mq_send_log set cancelsign='Y' where series=? ";
        this.jdbcTemplate.update(sql, series);
    }

    public List<SYS_ROCKET_MQ_SEND_LOG> queryByWorkflowId(Long workflowId) {
        String sql = "select SERIES,MESSAGE_ID,MESSAGE_KEY,MESSAGE_TOPIC,MESSAGE_TAG,MESSAGE_BODY,MESSAGE_NAME_ADDR,PRODUCER_NAME,CREATE_DTME,LAST_UPDTME,CREATE_USER_ID,CANCELSIGN,FAIL_DETAIL,TENANT_NUM_ID,DATA_SIGN,WORKFLOW_ID,INSTANCE_ID,STEP_ID,FROM_SYSTEM,MSG_STATUS,CONSUMER_SUCCESS,RETRY_TIMES,RESPONSE_DETAIL,LAST_UPDTME,CONSUMER_SUCCESS_TIME,ORDER_MESS_FLAG,TASK_TARGET,CLIENT_IP from SYS_ROCKET_MQ_SEND_LOG where workflow_id=? and msg_status=? limit 1000000";
        List list = this.jdbcTemplate.query(sql, new Object[]{workflowId, Constants.transaction_pre_send}, new BeanPropertyRowMapper(SYS_ROCKET_MQ_SEND_LOG.class));
        return list;
    }

    public void updateCreateUserId(Long series, Long usrNumId) {
        String sql = " UPDATE SYS_ROCKET_MQ_SEND_LOG SET  CREATE_USER_ID=? WHERE SERIES=?";
        this.jdbcTemplate.update(sql, new Object[]{usrNumId, series});
    }

    public void updateInstanceIdBySeries(Long series, Long instanceId) {
        if (this.jdbcTemplate.update(updateInstanceIdSql, new Object[]{instanceId, series}) <= 0) {
            throw new RuntimeException("事务回查消息重试，不满足重发条件的，避免反复重试更新标识数据库记录失败!消息行号:" + series);
        }
    }
}


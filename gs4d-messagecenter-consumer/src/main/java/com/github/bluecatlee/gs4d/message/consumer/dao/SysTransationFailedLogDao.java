package com.github.bluecatlee.gs4d.message.consumer.dao;

import com.github.bluecatlee.gs4d.message.api.model.MessageDetails;
import com.github.bluecatlee.gs4d.message.consumer.model.SysRocketMqSendLog;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SysTransationFailedLogDao {

    @Resource(name = "jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    private static final String querySqlPrefix = "select a.series,a.message_id as messageId ,a.message_key as messageKey,a.create_dtme as createTime ,a.message_body as messageBody,a.fail_detail as messageConsumerResult ,a.consumer_success_time as consumerSuccessTime,a.retry_times as retryTimes, (case when (consumer_success='N') then '消费失败' else case when (consumer_success='Y') then '消费成功' else  case when(consumer_success ='') then '未消费'end end end) as messageIsConsumer,a.client_ip as clientIP,a.message_topic as topic,a.message_tag as tag,a.from_system as systemName,a.step_id as sendType from SYS_TRANSATION_FAILED_LOG a ";
    private static final String queryCountSqlPrefix = "select count(*) from SYS_TRANSATION_FAILED_LOG a ";
    private static final String queryByTopicTagSql = "select SERIES,MESSAGE_ID,MESSAGE_KEY,MESSAGE_TOPIC,MESSAGE_TAG,MESSAGE_BODY,MESSAGE_NAME_ADDR,PRODUCER_NAME,CREATE_DTME,LAST_UPDTME,CREATE_USER_ID,CANCELSIGN,FAIL_DETAIL,TENANT_NUM_ID,DATA_SIGN,WORKFLOW_ID,INSTANCE_ID,STEP_ID,FROM_SYSTEM,MSG_STATUS,CONSUMER_SUCCESS,RETRY_TIMES,RESPONSE_DETAIL,LAST_UPDTME,CONSUMER_SUCCESS_TIME,ORDER_MESS_FLAG,TASK_TARGET,CLIENT_IP,sysdate() NOW_TIME from from SYS_TRANSATION_FAILED_LOG where MESSAGE_TOPIC=? and MESSAGE_TAG=? and TENANT_NUM_ID=? and DATA_SIGN=?";

    public Long queryCount(String condition) {
        String sql = queryCountSqlPrefix + condition;
        Long count = (Long)this.jdbcTemplate.queryForObject(sql, Long.class);
        return count;
    }

    public List<MessageDetails> query(String condition) {
        String sql = querySqlPrefix + condition;
        List list = this.jdbcTemplate.query(sql, new BeanPropertyRowMapper(MessageDetails.class));
        return list;
    }

    public List<SysRocketMqSendLog> queryByTopicTag(String topic, String tag, Long tenantNumId, Long dataSign) {
        List list = this.jdbcTemplate.query(queryByTopicTagSql, new Object[]{topic, tag, tenantNumId, dataSign}, new BeanPropertyRowMapper(SysRocketMqSendLog.class));
        return list;
    }
}


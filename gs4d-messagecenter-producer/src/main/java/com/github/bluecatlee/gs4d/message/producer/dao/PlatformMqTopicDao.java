package com.github.bluecatlee.gs4d.message.producer.dao;

import com.github.bluecatlee.gs4d.common.exception.DatabaseOperateException;
import com.github.bluecatlee.gs4d.common.exception.ExceptionType;
import com.github.bluecatlee.gs4d.common.utils.StringUtil;
import com.github.bluecatlee.gs4d.message.api.model.TopicConfigModel;
import com.github.bluecatlee.gs4d.message.producer.model.PLATFORM_MQ_TOPIC;
import com.github.bluecatlee.gs4d.message.producer.utils.Constants;
import com.github.bluecatlee.gs4d.message.producer.utils.SeqUtil;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class PlatformMqTopicDao {

    @Resource(name = "jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    private static final String queryAllTopicsSql = "select topic from platform_mq_topic  where system_num_id= ? and tenant_num_id=? and cancelsign='N' group by topic limit 10000";
    private static final String queryAllTagsSql = "select tag from platform_mq_topic  where system_num_id= ? and topic=? and tenant_num_id=? and cancelsign='N' group by tag limit 10000";
    private static final String cancelSql = "update platform_mq_topic set cancelsign='Y' where series=?";
    private static final String selectBySystemNumId = "select series,topic,remark from platform_mq_topic where system_num_id =? and tenant_num_id=?";
    private static final String selectDatasource = "select topic,tag,datasouce_name from platform_mq_topic where  cancelsign='N' limit 10000000";
    private static final String queryByTopicAndTagSql = "select series,remark,from_system,system_num_id from platform_mq_topic where topic=? and tag=? and tenant_num_id=?";
    private static final String queryHandleFailedmessSql = "select topic,tag,wether_handle_failedmess,consumer_series,retry_max,(case when (?=0) then retries else  case when(?=1) then retries_test end end ) as retries,consumer_type,zk_data_sign,task_target from platform_mq_topic where tenant_num_id =? and wether_handle_failedmess='Y' group by topic,tag limit 1000000";

    public List<PLATFORM_MQ_TOPIC> queryAll() {
        String sql = "select series,msg_id,topic,tag,from_system,create_date_time,create_user_name,remark,consumer_type,consumer_series,consumer_instance_count,consumer_interval,retries,retries_test,retries_develop,task_target,is_distinct,mess_batch_num,consumer_thread_min,consumer_thread_max,wether_order_mess,retry_interval,tenant_num_id,wether_insertdb,correct_codes,mq_queue,zk_data_sign,mq_namesrv,consumer_ip,system_num_id from platform_mq_topic where cancelsign = 'N'  group by topic,tag,tenant_num_id LIMIT 100000 ";
        List list = this.jdbcTemplate.query(sql, new BeanPropertyRowMapper(PLATFORM_MQ_TOPIC.class));
        return list;
    }

    public List<TopicConfigModel> queryAllSystemNames(Long tenantNumId) {
        String sql = "select distinct b.system_name fromSystem from platform_mq_topic a LEFT JOIN ex_arc_doc_system b on b.system_num_id=a.system_num_id where b.tenant_num_id=?  and  a.tenant_num_id=? and cancelsign='N' limit 100000";
        List list = this.jdbcTemplate.query(sql, new Object[]{tenantNumId, tenantNumId}, new BeanPropertyRowMapper(TopicConfigModel.class));
        return list;
    }

    public List<TopicConfigModel> query(Long systemNumId, String topic, String tag, Long tenantNumId, String condition, Long dataSign) {
        StringBuilder sb = new StringBuilder();
        sb.append("select series,topic,tag,from_system as fromSystem,create_date_time as createTime,create_user_name as createUser,is_distinct as whetherDistinct,wether_order_mess as whetherOrderMessage,consumer_thread_min as consumerThreadNum,remark,consumer_series as consumerSeries,  (case when (consumer_type=1) then 'dubbo' else  case when(consumer_type =2) then 'http' end end ) as consumerType,wether_insertdb wetherInsertdb,correct_codes correctCodes,mq_queue mqQueue,wether_handle_failedmess wetherHandleFailedmess,retry_max retryMax,zk_data_sign zkDataSign,mq_namesrv mqNamesrv,consumer_ip consumerIp,");
        if(SeqUtil.prod == Integer.parseInt(String.valueOf(dataSign))) {
            sb.append(" retries as retryTimes , ");
        } else if(SeqUtil.develop == Integer.parseInt(String.valueOf(dataSign))) {
            sb.append(" retries_develop as retryTimes , ");
        } else if(SeqUtil.test == Integer.parseInt(String.valueOf(dataSign))) {
            sb.append(" retries_test as retryTimes , ");
        }

        sb.append(" retry_interval retryInterval,task_target taskTarget from platform_mq_topic a where system_num_id=? and topic= ? and tag=? and tenant_num_id=?  and cancelsign='N'");
        sb.append(condition);
        String sql = sb.toString();
        List list = this.jdbcTemplate.query(sql, new Object[]{systemNumId, topic, tag, tenantNumId}, new BeanPropertyRowMapper(TopicConfigModel.class));
        return list;
    }

    public void update(String fromSystem, Long retries, String isDistinct, String wetherOrderMess, String consumerThread, String remark, String tag, String topic, long tenantNumId, long env, Long taskTarget, String retryInterval, String wetherInsertdb, String correctCodes, Long mqQueue, String wetherHandleFailedmess, Long retryMax, Long zkDataSign, String mqNamesrv, String consumerIp) {
        StringBuffer sb = new StringBuffer(" UPDATE platform_mq_topic SET ");
        String condition = " WHERE tag =? AND topic =? AND tenant_num_id =? ";
        ArrayList argList = new ArrayList();
        if(!StringUtil.isNullOrBlankTrim(fromSystem)) {
            sb.append("from_system =?,");
            argList.add(fromSystem);
        }

        if(!StringUtil.isNullOrBlankTrim(retries)) {
            if(SeqUtil.prod == Integer.parseInt(String.valueOf(env))) {
                sb.append("retries =?,");
            } else if(SeqUtil.develop == Integer.parseInt(String.valueOf(env))) {
                sb.append("retries_develop =?,");
            } else if(SeqUtil.test == Integer.parseInt(String.valueOf(env))) {
                sb.append("retries_test =?,");
            }

            argList.add(retries);
        }

        if(!StringUtil.isNullOrBlankTrim(retryInterval)) {
            sb.append("retry_interval =?,");
            argList.add(retryInterval);
        }

        if(!StringUtil.isNullOrBlankTrim(taskTarget)) {
            sb.append("task_target =?,");
            argList.add(taskTarget);
        }

        if(!StringUtil.isNullOrBlankTrim(isDistinct)) {
            sb.append("is_distinct =?,");
            argList.add(isDistinct);
        }

        if(!StringUtil.isNullOrBlankTrim(wetherOrderMess)) {
            sb.append("wether_order_mess =?,");
            argList.add(wetherOrderMess);
        }

        if(!StringUtil.isNullOrBlankTrim(consumerThread)) {
            sb.append("consumer_thread_min =?,");
            sb.append("consumer_thread_max =?,");
            argList.add(Integer.valueOf(Integer.parseInt(consumerThread)));
            argList.add(Integer.valueOf(Integer.parseInt(consumerThread) + 1));
        }

        if(!StringUtil.isNullOrBlankTrim(remark)) {
            sb.append("remark =?,");
            argList.add(remark);
        }

        if(!StringUtil.isNullOrBlankTrim(wetherInsertdb)) {
            sb.append("wether_insertdb =?,");
            argList.add(wetherInsertdb);
        }

        if(!StringUtil.isNullOrBlankTrim(correctCodes)) {
            sb.append("correct_codes =?,");
            argList.add(correctCodes);
        }

        if(!StringUtil.isNullOrBlankTrim(mqQueue)) {
            sb.append("mq_queue =?,");
            argList.add(mqQueue);
        }

        if(!StringUtil.isNullOrBlankTrim(wetherHandleFailedmess)) {
            sb.append("wether_handle_failedmess =?,");
            argList.add(wetherHandleFailedmess);
        }

        if(!StringUtil.isNullOrBlankTrim(retryMax)) {
            sb.append("retry_max =?,");
            argList.add(retryMax);
        }

        if(!StringUtil.isNullOrBlankTrim(zkDataSign)) {
            sb.append("zk_data_sign =?,");
            argList.add(zkDataSign);
        }

        if(!StringUtil.isNullOrBlankTrim(mqNamesrv)) {
            sb.append("mq_namesrv =?,");
            argList.add(mqNamesrv);
        }

        if(!StringUtil.isNullOrBlankTrim(consumerIp)) {
            sb.append("consumer_ip =?,");
            argList.add(consumerIp);
        }

        if(!argList.isEmpty()) {
            sb.delete(sb.length() - 1, sb.length()).append(condition);
            argList.add(tag);
            argList.add(topic);
            argList.add(Long.valueOf(tenantNumId));
            if(this.jdbcTemplate.update(sb.toString(), argList.toArray()) < 1) {
                throw new DatabaseOperateException(Constants.SUB_SYSTEM, ExceptionType.DOE30045, "topic:" + topic + ",tag:" + tag + "消息消费表编辑失败!");
            }
        }
    }

    public List<TopicConfigModel> queryAllTopicsBySystemNumId(Long systemNumId, Long tenantNumId) {
        List list = this.jdbcTemplate.query(queryAllTopicsSql, new Object[]{systemNumId, tenantNumId}, new BeanPropertyRowMapper(TopicConfigModel.class));
        return list;
    }

    public List<TopicConfigModel> queryAllTagsBySystemNumIdAndTopic(Long systemNumId, String topic, Long tenantNumId) {
        List list = this.jdbcTemplate.query(queryAllTagsSql, new Object[]{systemNumId, topic, tenantNumId}, new BeanPropertyRowMapper(TopicConfigModel.class));
        return list;
    }

    public List<TopicConfigModel> query(Long tenantNumId, Long systemNumId, String topic, Long env) {
        StringBuilder sb = new StringBuilder();
        sb.append("select from_system systemName ,topic,tag,remark, ");
        if(SeqUtil.prod == Integer.parseInt(String.valueOf(env))) {
            sb.append(" retries as retryTimes ");
        } else if(SeqUtil.develop == Integer.parseInt(String.valueOf(env))) {
            sb.append(" retries_develop as retryTimes ");
        } else if(SeqUtil.test == Integer.parseInt(String.valueOf(env))) {
            sb.append(" retries_test as retryTimes ");
        }

        sb.append("from platform_mq_topic a where a.tenant_num_id=?  and a.system_num_id=?  and cancelsign='N'");
        ArrayList args = new ArrayList();
        args.add(tenantNumId);
        args.add(systemNumId);
        if(StringUtil.isAllNotNullOrBlank(new String[]{topic})) {
            sb.append(" and topic=? ");
            args.add(topic);
        }

        String groupBy = " group by a.from_system ,a.tag,a.topic,a.remark,a.retries ,a.retries_test,a.retries_develop limit 100000";
        String sql = sb.append(groupBy).toString();
        List list = this.jdbcTemplate.query(sql, args.toArray(), new BeanPropertyRowMapper(TopicConfigModel.class));
        return list;
    }

    public void cancel(Long series) {
        if(this.jdbcTemplate.update(cancelSql, new Object[]{series}) < 1) {
            throw new DatabaseOperateException(Constants.SUB_SYSTEM, ExceptionType.DOE30045, "topic删除失败");
        }
    }

    public void insert(String topic, String tag, Long consumerInstanceCount, Long consumerInterval, Long consumerThread, String createUserName, String isDistinct, Long messBatchNum, String remark, Long retries, Long retriesDevelop, Long retriesTest, Long systemNumId, Long taskTarget, Long tenantNumId, String wetherOrderMess, String retryInterval, String fromSystem, String wetherInsertdb, String correctCodes, Long mqQueue, String wetherHandleFailedmess, Long retryMax, Long zkDataSign, String mqNamesrv, String consumerIp) {
        StringBuffer sb = new StringBuffer("insert into platform_mq_topic(series,msg_id,topic,tag,system_num_id,create_date_time,create_user_name,remark,consumer_instance_count,cancelsign,consumer_interval,retries,retries_test,retries_develop,task_target,is_distinct,mess_batch_num,consumer_thread_min,consumer_thread_max,tenant_num_id,wether_order_mess,retry_interval,from_system,wether_insertdb,correct_codes,mq_queue,wether_handle_failedmess,retry_max,zk_data_sign,mq_namesrv,consumer_ip)values(?,?,?,?,?,sysdate(),?,?,?,'N',?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
        ArrayList args = new ArrayList();
        args.add(SeqUtil.getNoSubSequence(SeqUtil.PLATFORM_MQ_TOPIC_SERIES));
        args.add(SeqUtil.getNoSubSequence(SeqUtil.PLATFORM_MQ_TOPIC_MSGID));
        args.add(topic);
        args.add(tag);
        args.add(systemNumId);
        args.add(createUserName);
        args.add(remark);
        if(!StringUtil.isNullOrBlankTrim(consumerInstanceCount)) {
            args.add(consumerInstanceCount);
        } else {
            args.add(Integer.valueOf(1));
        }

        if(!StringUtil.isNullOrBlankTrim(consumerInterval)) {
            args.add(consumerInterval);
        } else {
            args.add(Integer.valueOf(0));
        }

        if(!StringUtil.isNullOrBlankTrim(retries)) {
            args.add(retries);
        } else {
            args.add(Integer.valueOf(3));
        }

        if(!StringUtil.isNullOrBlankTrim(retriesTest)) {
            args.add(retriesTest);
        } else {
            args.add(Integer.valueOf(3));
        }

        if(!StringUtil.isNullOrBlankTrim(retriesDevelop)) {
            args.add(retriesDevelop);
        } else {
            args.add(Integer.valueOf(3));
        }

        if(!StringUtil.isNullOrBlankTrim(taskTarget)) {
            args.add(taskTarget);
        } else {
            args.add(Integer.valueOf(1800000));
        }

        if(!StringUtil.isNullOrBlankTrim(isDistinct)) {
            args.add(isDistinct);
        } else {
            args.add("N");
        }

        if(!StringUtil.isNullOrBlankTrim(messBatchNum)) {
            args.add(messBatchNum);
        } else {
            args.add(Integer.valueOf(1));
        }

        if(!StringUtil.isNullOrBlankTrim(consumerThread)) {
            args.add(consumerThread);
            args.add(Long.valueOf(consumerThread.longValue() + 1L));
        } else {
            args.add(Integer.valueOf(3));
            args.add(Integer.valueOf(4));
        }

        args.add(tenantNumId);
        if(!StringUtil.isNullOrBlankTrim(wetherOrderMess)) {
            args.add(wetherOrderMess);
        } else {
            args.add("N");
        }

        if(!StringUtil.isNullOrBlankTrim(retryInterval)) {
            args.add(retryInterval);
        } else {
            args.add("");
        }

        if(!StringUtil.isNullOrBlankTrim(fromSystem)) {
            args.add(fromSystem);
        } else {
            args.add("");
        }

        if(!StringUtil.isNullOrBlankTrim(wetherInsertdb)) {
            args.add(wetherInsertdb);
        } else {
            args.add("Y");
        }

        if(!StringUtil.isNullOrBlankTrim(correctCodes)) {
            args.add(correctCodes);
        } else {
            args.add("");
        }

        if(!StringUtil.isNullOrBlankTrim(mqQueue)) {
            args.add(mqQueue);
        } else {
            args.add(Long.valueOf(8L));
        }

        if(!StringUtil.isNullOrBlankTrim(wetherHandleFailedmess)) {
            args.add(wetherHandleFailedmess);
        } else {
            args.add("Y");
        }

        if(!StringUtil.isNullOrBlankTrim(retryMax)) {
            args.add(retryMax);
        } else {
            args.add(Integer.valueOf(20));
        }

        if(!StringUtil.isNullOrBlankTrim(zkDataSign)) {
            args.add(zkDataSign);
        } else {
            args.add(Integer.valueOf(0));
        }

        if(!StringUtil.isNullOrBlankTrim(mqNamesrv)) {
            args.add(mqNamesrv);
        } else {
            args.add("");
        }

        if(!StringUtil.isNullOrBlankTrim(consumerIp)) {
            args.add(consumerIp);
        } else {
            args.add("");
        }

        if(this.jdbcTemplate.update(sb.toString(), args.toArray()) < 1) {
            throw new DatabaseOperateException(Constants.SUB_SYSTEM, ExceptionType.DOE30045, "topic插入失败");
        }
    }

    public List<TopicConfigModel> b(Long systemNumId, Long tenantNumId) {
        List list = this.jdbcTemplate.query(selectBySystemNumId, new Object[]{systemNumId, tenantNumId}, new BeanPropertyRowMapper(TopicConfigModel.class));
        return list;
    }

    public List<PLATFORM_MQ_TOPIC> getDistinctSystemName() {
        List list = this.jdbcTemplate.query(selectDatasource, new BeanPropertyRowMapper(PLATFORM_MQ_TOPIC.class));
        return list;
    }

    public List<PLATFORM_MQ_TOPIC> queryByTopicAndTag(String topic, String tag, String tenantNumId) {
        List list = this.jdbcTemplate.query(queryByTopicAndTagSql, new Object[]{topic, tag, tenantNumId}, new BeanPropertyRowMapper(PLATFORM_MQ_TOPIC.class));
        return list;
    }

    public List<PLATFORM_MQ_TOPIC> queryHandleFailedmess(Long env, Long tenantNumId) {
        List list = this.jdbcTemplate.query(queryHandleFailedmessSql, new Object[]{env, env, tenantNumId}, new BeanPropertyRowMapper(PLATFORM_MQ_TOPIC.class));
        return list;
    }

    public List<PLATFORM_MQ_TOPIC> queryConsumerDistinct(Long tenantNumId) {
        String sql = "select topic,tag,remark from platform_mq_topic where tenant_num_id =? and consumer_distinct='Y' group by topic,tag,remark limit 1000000";
        List list = this.jdbcTemplate.query(sql, new Object[]{tenantNumId}, new BeanPropertyRowMapper(PLATFORM_MQ_TOPIC.class));
        return list;
    }

    public List<PLATFORM_MQ_TOPIC> queryBySeries(Long series) {
        String sql = "select series,msg_id,topic,tag,from_system,create_date_time,create_user_name,remark,consumer_type,consumer_series,consumer_instance_count,consumer_interval,retries,retries_test,retries_develop,task_target,is_distinct,mess_batch_num,consumer_thread_min,consumer_thread_max,wether_order_mess,retry_interval,tenant_num_id,wether_insertdb,correct_codes,mq_queue,zk_data_sign,mq_namesrv,consumer_ip,system_num_id,consumer_pull_delay from platform_mq_topic where series=? LIMIT 100000 ";
        List list = this.jdbcTemplate.query(sql, new Object[]{series}, new BeanPropertyRowMapper(PLATFORM_MQ_TOPIC.class));
        return list;
    }
}


package com.github.bluecatlee.gs4d.message.consumer.dao;

import com.github.bluecatlee.gs4d.common.exception.DatabaseOperateException;
import com.github.bluecatlee.gs4d.common.exception.ExceptionType;
import com.github.bluecatlee.gs4d.common.utils.StringUtil;
import com.github.bluecatlee.gs4d.message.api.model.SimpleConfig;
import com.github.bluecatlee.gs4d.message.api.model.TopicConfigModel;
import com.github.bluecatlee.gs4d.message.consumer.model.PlatformMqTopic;
import com.github.bluecatlee.gs4d.message.consumer.utils.Constants;
import com.github.bluecatlee.gs4d.message.consumer.utils.SeqUtil;
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
    private static final String queryAllTagsByTopicSql = "select tag from platform_mq_topic  where  topic=? and tenant_num_id=? and cancelsign='N' group by tag limit 10000";
    private static final String cancelSql = "update platform_mq_topic set cancelsign='Y' where series=?";
    private static final String queryAllBySystemNumIdSql = "select series,topic,remark from platform_mq_topic where system_num_id =? and tenant_num_id=? and cancelsign='N'";
    private static final String queryDinstinctSql = "select DISTINCT system_num_id,topic,tag from platform_mq_topic where  cancelsign='N' and system_num_id!='' and system_num_id is not null limit 10000000";
    private static final String queryByTopicTagSql = "select series,remark,from_system,system_num_id from platform_mq_topic where topic=? and tag=? and tenant_num_id=?";
    private static final String queryNeedHandleFailedmessSql = "select topic,tag,wether_handle_failedmess,consumer_series,retry_max,(case when (?=0) then retries else  case when(?=1) then retries_test end end ) as retries,consumer_type,zk_data_sign,task_target from platform_mq_topic where tenant_num_id =? and wether_handle_failedmess='Y' group by topic,tag limit 1000000";
    private static final String updateConsumerSeriesAndConsumerTypeSql = " UPDATE platform_mq_topic SET consumer_series =? ,consumer_type=? WHERE tag =? AND topic =? AND tenant_num_id =?";
    private static final String deleteConsumerSeriesSql = " UPDATE platform_mq_topic SET consumer_series =null,consumer_type=0 WHERE consumer_series=? and consumer_type=?";
    private static final String queryByTagSql = "select topic,remark from platform_mq_topic where  tag=? and cancelsign='N'";
    private static final String queryByConsumerInstanceCountSql = "select topic,tag,remark from platform_mq_topic where  consumer_instance_count=? and cancelsign='N'";
    private static final String updateCancelSignSql = "update platform_mq_topic set cancelsign=? where series=? ";
    private static final String queryCountByTopicTagSql = "select count(*) from platform_mq_topic where topic=? and tag=? ";
    private static final String getAllTopicSql = "select topic from platform_mq_topic group by topic limit 1000000";
    private static final String queryAllTenantNeedHandleFailedmessSql = "select topic,tag,wether_handle_failedmess,consumer_series,retry_max,(case when (?=0) then retries else  case when(?=1) then retries_test end end ) as retries,consumer_type,zk_data_sign,task_target from platform_mq_topic where wether_handle_failedmess='Y' group by topic,tag limit 1000000";

    public List<PlatformMqTopic> queryAll() {
        String sql = "select series,msg_id,topic,tag,from_system,create_date_time,create_user_name,remark,consumer_type,consumer_series,consumer_instance_count,consumer_interval,retries,retries_test,retries_develop,task_target,is_distinct,mess_batch_num,consumer_thread_min,consumer_thread_max,wether_order_mess,retry_interval,tenant_num_id,wether_insertdb,correct_codes,mq_queue,zk_data_sign,mq_namesrv,consumer_ip,system_num_id,consumer_pull_delay from platform_mq_topic where cancelsign = 'N'  group by topic,tag,tenant_num_id LIMIT 100000 ";
        List list = this.jdbcTemplate.query(sql, new BeanPropertyRowMapper(PlatformMqTopic.class));
        return list;
    }

    public List<TopicConfigModel> querySystemNames(Long tenantNumId) {
        String sql = "select distinct b.system_name fromSystem from  ex_arc_doc_system b  where b.tenant_num_id=?   limit 100000";
        List list = this.jdbcTemplate.query(sql, new Object[]{tenantNumId}, new BeanPropertyRowMapper(TopicConfigModel.class));
        return list;
    }

    public List<TopicConfigModel> query(Long systemNumId, String topic, String tag, Long tenantNumId, String condition, Long dataSign) {
        StringBuilder sb = new StringBuilder();
        sb.append("select series,topic,tag,from_system as fromSystem,create_date_time as createTime,create_user_name as createUser,is_distinct as whetherDistinct,wether_order_mess as whetherOrderMessage,consumer_thread_min as consumerThreadNum,remark,consumer_series as consumerSeries,  (case when (consumer_type=1) then 'dubbo' else  case when(consumer_type =2) then 'http' end end ) as consumerType,wether_insertdb wetherInsertdb,correct_codes correctCodes,mq_queue mqQueue,wether_handle_failedmess wetherHandleFailedmess,retry_max retryMax,zk_data_sign zkDataSign,mq_namesrv mqNamesrv,consumer_ip consumerIp,");
        if (SeqUtil.prod == Integer.parseInt(String.valueOf(dataSign))) {
            sb.append(" retries as retryTimes , ");
        } else if (SeqUtil.develop == Integer.parseInt(String.valueOf(dataSign))) {
            sb.append(" retries_develop as retryTimes , ");
        } else if (SeqUtil.test == Integer.parseInt(String.valueOf(dataSign))) {
            sb.append(" retries_test as retryTimes , ");
        }

        sb.append(" retry_interval retryInterval,task_target taskTarget from platform_mq_topic a where system_num_id=? and topic= ? and tag=? and tenant_num_id=?  and cancelsign='N'");
        sb.append(condition);
        String sql = sb.toString();
        List list = this.jdbcTemplate.query(sql, new Object[]{systemNumId, topic, tag, tenantNumId}, new BeanPropertyRowMapper(TopicConfigModel.class));
        return list;
    }

    public void update(String topic, String tag, String wetherListenBinLogTopic, String consumerInterval, Long consumerThreadNum, String createUserName, String isDistinct, Long messBatchNum, String remark, Long retries, Long retriesDevelop, Long retriesTest, Long systemNumId, Long taskTarget, Long tenantNumId, String wetherOrderMess, String retryInterval, String wetherInsertdb, String correctCodes, Long mqQueue, String wetherHandleFailedmess, Long retryMax, String zkDataSign, Long consumerPullDelay, String fromSystem) {
        StringBuffer sb = new StringBuffer(" UPDATE platform_mq_topic SET ");
        String condition = " WHERE tag =? AND topic =? AND tenant_num_id =? ";
        ArrayList argList = new ArrayList();
        if (!StringUtil.isNullOrBlankTrim(fromSystem)) {
            sb.append("from_system =?,");
            argList.add(fromSystem);
        }

        if (!StringUtil.isNullOrBlankTrim(retriesTest)) {
            sb.append("retries_test =?,");
            argList.add(retriesTest);
        }

        if (!StringUtil.isNullOrBlankTrim(retriesDevelop)) {
            sb.append("retries_develop =?,");
            argList.add(retriesDevelop);
        }

        if (!StringUtil.isNullOrBlankTrim(retries)) {
            sb.append("retries =?,");
            argList.add(retries);
        }

        if (!StringUtil.isNullOrBlankTrim(retryInterval)) {
            sb.append("retry_interval =?,");
            argList.add(retryInterval);
        }

        if (!StringUtil.isNullOrBlankTrim(taskTarget)) {
            sb.append("task_target =?,");
            argList.add(taskTarget);
        }

        if (!StringUtil.isNullOrBlankTrim(wetherListenBinLogTopic) && "Y".equals(wetherListenBinLogTopic)) {
            sb.append("consumer_instance_count =?,");
            argList.add("100");
        } else {
            sb.append("consumer_instance_count =?,");
            argList.add(1);
        }

        if (!StringUtil.isNullOrBlankTrim(consumerInterval)) {
            sb.append("consumer_interval =?,");
            argList.add(consumerInterval);
        }

        if (!StringUtil.isNullOrBlankTrim(consumerThreadNum)) {
            sb.append("consumer_thread_min =?,");
            sb.append("consumer_thread_max =?,");
            argList.add(consumerThreadNum.intValue());
            argList.add(consumerThreadNum.intValue() + 1);
        }

        if (!StringUtil.isNullOrBlankTrim(remark)) {
            sb.append("remark =?,");
            argList.add(remark);
        }

        if (!StringUtil.isNullOrBlankTrim(createUserName)) {
            sb.append("create_user_name =?,");
            argList.add(createUserName);
        }

        if (!StringUtil.isNullOrBlankTrim(correctCodes)) {
            sb.append("correct_codes =?,");
            argList.add(correctCodes);
        }

        if (!StringUtil.isNullOrBlankTrim(mqQueue)) {
            sb.append("mq_queue =?,");
            argList.add(mqQueue);
        }

        if (!StringUtil.isNullOrBlankTrim(wetherHandleFailedmess)) {
            sb.append("wether_handle_failedmess =?,");
            argList.add(wetherHandleFailedmess);
        }

        if (!StringUtil.isNullOrBlankTrim(retryMax)) {
            sb.append("retry_max =?,");
            argList.add(retryMax);
        }

        if (!StringUtil.isNullOrBlankTrim(zkDataSign)) {
            sb.append("zk_data_sign =?,");
            argList.add(zkDataSign);
        }

        if (!StringUtil.isNullOrBlankTrim(messBatchNum)) {
            sb.append("mess_batch_num =?,");
            argList.add(messBatchNum);
        }

        if (!StringUtil.isNullOrBlankTrim(systemNumId)) {
            sb.append("system_num_id =?,");
            argList.add(systemNumId);
        }

        if (!StringUtil.isNullOrBlankTrim(wetherOrderMess)) {
            sb.append("wether_order_mess =?,");
            argList.add(wetherOrderMess);
        }

        if (!StringUtil.isNullOrBlankTrim(wetherInsertdb)) {
            sb.append("wether_insertdb =?,");
            argList.add(wetherInsertdb);
        }

        if (!StringUtil.isNullOrBlankTrim(consumerPullDelay)) {
            sb.append("consumer_pull_delay =?,");
            argList.add(consumerPullDelay);
        }

        if (!argList.isEmpty()) {
            sb.delete(sb.length() - 1, sb.length()).append(condition);
            argList.add(tag);
            argList.add(topic);
            argList.add(tenantNumId);
            if (this.jdbcTemplate.update(sb.toString(), argList.toArray()) < 1) {
                throw new DatabaseOperateException(Constants.SUB_SYSTEM, ExceptionType.DOE30045, "topic:" + topic + ",tag:" + tag + "消息消费表编辑失败!");
            }
        }
    }

    public List<TopicConfigModel> queryAllTopicsBySystemNumId(Long systemNumId, Long tenantNumId) {
        List list = this.jdbcTemplate.query(queryAllTopicsSql, new Object[]{systemNumId, tenantNumId}, new BeanPropertyRowMapper(TopicConfigModel.class));
        return list;
    }

    public List<TopicConfigModel> queryAllTagsByTopic(String topic, Long tenantNumId) {
        List list = this.jdbcTemplate.query(queryAllTagsByTopicSql, new Object[]{topic, tenantNumId}, new BeanPropertyRowMapper(TopicConfigModel.class));
        return list;
    }

    public List<TopicConfigModel> query(Long tenantNumId, String topic, String tag, Long dataSign) {
        StringBuilder sb = new StringBuilder();
        sb.append("select system_num_id systemNumId ,topic,tag,remark, ");
        if (SeqUtil.prod == Integer.parseInt(String.valueOf(dataSign))) {
            sb.append(" retries as retryTimes ");
        } else if (SeqUtil.develop == Integer.parseInt(String.valueOf(dataSign))) {
            sb.append(" retries_develop as retryTimes ");
        } else if (SeqUtil.test == Integer.parseInt(String.valueOf(dataSign))) {
            sb.append(" retries_test as retryTimes ");
        }

        sb.append("from platform_mq_topic a where a.tenant_num_id=?  and cancelsign='N'");
        ArrayList args = new ArrayList();
        args.add(tenantNumId);
        if (StringUtil.isAllNotNullOrBlank(new String[]{topic})) {
            sb.append(" and topic=? ");
            args.add(topic);
        }

        if (StringUtil.isAllNotNullOrBlank(new String[]{tag})) {
            sb.append(" and tag=? ");
            args.add(tag);
        }

        String groupBySql = " group by a.system_num_id,a.tag,a.topic,a.remark,a.retries ,a.retries_test,a.retries_develop limit 100000";
        String sql = sb.append(groupBySql).toString();
        List list = this.jdbcTemplate.query(sql, args.toArray(), new BeanPropertyRowMapper(TopicConfigModel.class));
        return list;
    }

    public void cancel(Long series) {
        if (this.jdbcTemplate.update(cancelSql, new Object[]{series}) < 1) {
            throw new DatabaseOperateException(Constants.SUB_SYSTEM, ExceptionType.DOE30045, "topic删除失败");
        }
    }

    public Long insert(String topic, String tag, String wetherListenBinLogTopic, String consumerInterval, Long consumerThreadNums, String createUserName, String isDistinct, Long messBatchNum, String remark, Long retries, Long retriesDevelopment, Long retriesTest, Long systemNumId, Long taskTarget, Long tenantNumId, String wetherOrderMess, String retryInterval, String wetherInsertdb, String correctCodes, Long mqQueue, String wetherHandleFailedmess, Long retryMax, String zkDataSign, Long consumerPullDelay, String fromSystem) {
        StringBuffer sb = new StringBuffer("insert into platform_mq_topic(series,msg_id,topic,tag,system_num_id,create_date_time,create_user_name,remark,consumer_instance_count,cancelsign,consumer_interval,retries,retries_test,retries_develop,task_target,is_distinct,mess_batch_num,consumer_thread_min,consumer_thread_max,tenant_num_id,wether_order_mess,retry_interval,wether_insertdb,correct_codes,mq_queue,wether_handle_failedmess,retry_max,zk_data_sign,consumer_pull_delay,from_system)values(?,?,?,?,?,sysdate(),?,?,?,'Y',?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
        ArrayList args = new ArrayList();
        Long series = SeqUtil.getNoSubSequence(SeqUtil.PLATFORM_MQ_TOPIC_SERIES);
        args.add(series);
        args.add(SeqUtil.getNoSubSequence(SeqUtil.PLATFORM_MQ_TOPIC_MSGID));
        args.add(topic);
        args.add(tag);
        args.add(systemNumId);
        args.add(createUserName);
        args.add(remark);
        if (!StringUtil.isNullOrBlankTrim(wetherListenBinLogTopic) && "Y".equals(wetherListenBinLogTopic)) {
            args.add("100");
        } else {
            args.add(1);
        }

        if (!StringUtil.isNullOrBlankTrim(consumerInterval)) {
            args.add(consumerInterval);
        } else {
            args.add(0);
        }

        if (!StringUtil.isNullOrBlankTrim(retries)) {
            args.add(retries);
        } else {
            args.add(3);
        }

        if (!StringUtil.isNullOrBlankTrim(retriesTest)) {
            args.add(retriesTest);
        } else {
            args.add(3);
        }

        if (!StringUtil.isNullOrBlankTrim(retriesDevelopment)) {
            args.add(retriesDevelopment);
        } else {
            args.add(3);
        }

        if (!StringUtil.isNullOrBlankTrim(taskTarget)) {
            args.add(taskTarget);
        } else {
            args.add(1800000);
        }

        if (!StringUtil.isNullOrBlankTrim(isDistinct)) {
            args.add(isDistinct);
        } else {
            args.add("N");
        }

        if (!StringUtil.isNullOrBlankTrim(messBatchNum)) {
            args.add(messBatchNum);
        } else {
            args.add(1);
        }

        if (!StringUtil.isNullOrBlankTrim(consumerThreadNums)) {
            args.add(consumerThreadNums);
            args.add(consumerThreadNums + 1L);
        } else {
            args.add(3);
            args.add(4);
        }

        args.add(tenantNumId);
        if (!StringUtil.isNullOrBlankTrim(wetherOrderMess)) {
            args.add(wetherOrderMess);
        } else {
            args.add("N");
        }

        if (!StringUtil.isNullOrBlankTrim(retryInterval)) {
            args.add(retryInterval);
        } else {
            args.add("");
        }

        if (!StringUtil.isNullOrBlankTrim(wetherInsertdb)) {
            args.add(wetherInsertdb);
        } else {
            args.add("Y");
        }

        if (!StringUtil.isNullOrBlankTrim(correctCodes)) {
            args.add(correctCodes);
        } else {
            args.add("");
        }

        if (!StringUtil.isNullOrBlankTrim(mqQueue)) {
            args.add(mqQueue);
        } else {
            args.add(8L);
        }

        if (!StringUtil.isNullOrBlankTrim(wetherHandleFailedmess)) {
            args.add(wetherHandleFailedmess);
        } else {
            args.add("Y");
        }

        if (!StringUtil.isNullOrBlankTrim(retryMax)) {
            args.add(retryMax);
        } else {
            args.add(20);
        }

        if (!StringUtil.isNullOrBlankTrim(zkDataSign)) {
            args.add(zkDataSign);
        } else {
            args.add(0);
        }

        if (!StringUtil.isNullOrBlankTrim(consumerPullDelay)) {
            args.add(consumerPullDelay);
        } else {
            args.add(0);
        }

        args.add(fromSystem);
        if (this.jdbcTemplate.update(sb.toString(), args.toArray()) < 1) {
            throw new DatabaseOperateException(Constants.SUB_SYSTEM, ExceptionType.DOE30045, "topic插入失败");
        } else {
            return series;
        }
    }

    public List<TopicConfigModel> queryAllBySystemNumId(Long systemNumId, Long tenantNumId) {
        List list = this.jdbcTemplate.query(queryAllBySystemNumIdSql, new Object[]{systemNumId, tenantNumId}, new BeanPropertyRowMapper(TopicConfigModel.class));
        return list;
    }

    public List<PlatformMqTopic> queryDinstinct() {
        List list = this.jdbcTemplate.query(queryDinstinctSql, new BeanPropertyRowMapper(PlatformMqTopic.class));
        return list;
    }

    public List<PlatformMqTopic> queryByTopicTag(String topic, String tag, String tenantNumId) {
        List list = this.jdbcTemplate.query(queryByTopicTagSql, new Object[]{topic, tag, tenantNumId}, new BeanPropertyRowMapper(PlatformMqTopic.class));
        return list;
    }

    public List<PlatformMqTopic> queryNeedHandleFailedmess(Long dataSign, Long tenantNumId) {
        List list = this.jdbcTemplate.query(queryNeedHandleFailedmessSql, new Object[]{dataSign, dataSign, tenantNumId}, new BeanPropertyRowMapper(PlatformMqTopic.class));
        return list;
    }

    public List<PlatformMqTopic> queryConsumerDinstinctTopicTags(Long tenantNumId) {
        String sql = "select topic,tag from platform_mq_topic where tenant_num_id =? and consumer_distinct='Y' group by topic,tag limit 1000000";
        List list = this.jdbcTemplate.query(sql, new Object[]{tenantNumId}, new BeanPropertyRowMapper(PlatformMqTopic.class));
        return list;
    }

    public void updateConsumerSeriesAndConsumerType(long consumerSeries, String tag, String topic, long tenantNumId, Long consumerType) {
        if (this.jdbcTemplate.update(updateConsumerSeriesAndConsumerTypeSql, new Object[]{consumerSeries, consumerType, tag, topic, tenantNumId}) < 1) {
            throw new DatabaseOperateException(Constants.SUB_SYSTEM, ExceptionType.DOE30045, "更新消息消费表的消费者编号失败!");
        }
    }

    public void deleteConsumerSeries(Long consumerSeries, int consumerType) {
        if (this.jdbcTemplate.update(deleteConsumerSeriesSql, new Object[]{consumerSeries, consumerType}) < 1) {
            throw new DatabaseOperateException(Constants.SUB_SYSTEM, ExceptionType.DOE30045, "更新消息消费表的消费者编号失败!");
        }
    }

    public List<TopicConfigModel> queryByTag(String tag) {
        List list = this.jdbcTemplate.query(queryByTagSql, new Object[]{tag}, new BeanPropertyRowMapper(TopicConfigModel.class));
        return list;
    }

    public List<TopicConfigModel> queryByConsumerInstanceCount(Long consumerInstanceCount) {
        List list = this.jdbcTemplate.query(queryByConsumerInstanceCountSql, new Object[]{consumerInstanceCount}, new BeanPropertyRowMapper(TopicConfigModel.class));
        return list;
    }

    public List<SimpleConfig> query(String topic, String tag, Long tenantNumId, String condition, Long dataSign) {
        StringBuilder sb = new StringBuilder();
        sb.append("select series,topic,tag,system_num_id as systemName,create_user_name as createUser,remark,");
        if (SeqUtil.prod == Integer.parseInt(String.valueOf(dataSign))) {
            sb.append(" retries as retryTimes , ");
        } else if (SeqUtil.develop == Integer.parseInt(String.valueOf(dataSign))) {
            sb.append(" retries_develop as retryTimes , ");
        } else if (SeqUtil.test == Integer.parseInt(String.valueOf(dataSign))) {
            sb.append(" retries_test as retryTimes , ");
        }

        sb.append(" cancelsign onlineFlag,consumer_type consumerType from platform_mq_topic a where  tenant_num_id=? ");
        ArrayList args = new ArrayList();
        args.add(tenantNumId);
        if (!StringUtil.isNullOrBlankTrim(topic)) {
            sb.append(" and topic =? ");
            args.add(topic);
        }

        if (!StringUtil.isNullOrBlankTrim(tag)) {
            sb.append(" and tag =? ");
            args.add(tag);
        }

        sb.append(condition);
        String sql = sb.toString();
        List list = this.jdbcTemplate.query(sql, args.toArray(), new BeanPropertyRowMapper(SimpleConfig.class));
        return list;
    }

    public Long queryCount(String topic, String tag, Long tenantNumId, String condition, Long dataSign) {
        StringBuilder sb = new StringBuilder();
        sb.append("select count(*) from platform_mq_topic a where  tenant_num_id=? ");
        ArrayList args = new ArrayList();
        args.add(tenantNumId);
        if (!StringUtil.isNullOrBlankTrim(topic)) {
            sb.append(" and topic =? ");
            args.add(topic);
        }

        if (!StringUtil.isNullOrBlankTrim(tag)) {
            sb.append(" and tag =? ");
            args.add(tag);
        }

        sb.append(condition);
        Long list = (Long)this.jdbcTemplate.queryForObject(sb.toString(), args.toArray(), Long.class);
        return list;
    }

    public List<TopicConfigModel> queryTopicConfigModelsBySeries(Long series) {
        StringBuilder sb = new StringBuilder();
        sb.append("select series,topic,tag,system_num_id as systemNumId,create_date_time as createTime,create_user_name as createUser,is_distinct as wetherDistinct,wether_order_mess as wetherOrderMessage,consumer_thread_min as consumerThreadNum,remark,consumer_series as consumerSeries,  (case when (consumer_type=1) then 'dubbo' else  case when(consumer_type =2) then 'http' end end ) as consumerType,wether_insertdb wetherInsertdb,correct_codes correctCodes,mq_queue mqQueue,wether_handle_failedmess wetherHandleFailedmess,retry_max retryMax,zk_data_sign zkDataSign,mq_namesrv mqNamesrv,consumer_ip consumerIp,consumer_pull_delay consumerPullDelay,consumer_instance_count wetherListenBinLogTopic,");
        sb.append(" retries as retryTimes , ");
        sb.append(" retries_develop as retryDevelopTimes , ");
        sb.append(" retries_test as retryTestTimes , ");
        sb.append(" retry_interval retryInterval,consumer_interval consumerInterval,task_target taskTarget,mess_batch_num messBatchNum from platform_mq_topic a where series=? ");
        String sql = sb.toString();
        List list = this.jdbcTemplate.query(sql, new Object[]{series}, new BeanPropertyRowMapper(TopicConfigModel.class));
        return list;
    }

    public void updateCancelSign(Long series, String cancelSign) {
        this.jdbcTemplate.update(updateCancelSignSql, new Object[]{cancelSign, series});
    }

    public Long queryCountByTopicTag(String topic, String tag) {
        Long count = (Long)this.jdbcTemplate.queryForObject(queryCountByTopicTagSql, new Object[]{topic, tag}, Long.class);
        return count;
    }

    public List<TopicConfigModel> getAllTopic() {
        List list = this.jdbcTemplate.query(getAllTopicSql, new BeanPropertyRowMapper(TopicConfigModel.class));
        return list;
    }

    public List<PlatformMqTopic> queryBySeries(Long series) {
        String sql = "select series,msg_id,topic,tag,from_system,create_date_time,create_user_name,remark,consumer_type,consumer_series,consumer_instance_count,consumer_interval,retries,retries_test,retries_develop,task_target,is_distinct,mess_batch_num,consumer_thread_min,consumer_thread_max,wether_order_mess,retry_interval,tenant_num_id,wether_insertdb,correct_codes,mq_queue,zk_data_sign,mq_namesrv,consumer_ip,system_num_id,consumer_pull_delay from platform_mq_topic where series=? LIMIT 100000 ";
        List list = this.jdbcTemplate.query(sql, new Object[]{series}, new BeanPropertyRowMapper(PlatformMqTopic.class));
        return list;
    }

    public List<PlatformMqTopic> queryAllTenantNeedHandleFailedmess(Long dataSign) {
        return this.jdbcTemplate.query(queryAllTenantNeedHandleFailedmessSql, new Object[]{dataSign, dataSign}, new BeanPropertyRowMapper(PlatformMqTopic.class));
    }
}


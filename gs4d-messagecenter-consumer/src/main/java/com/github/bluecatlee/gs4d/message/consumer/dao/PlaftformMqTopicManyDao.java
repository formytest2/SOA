package com.github.bluecatlee.gs4d.message.consumer.dao;

import com.github.bluecatlee.gs4d.common.exception.DatabaseOperateException;
import com.github.bluecatlee.gs4d.common.exception.ExceptionType;
import com.github.bluecatlee.gs4d.message.api.model.SonTopicRelationModel;
import com.github.bluecatlee.gs4d.message.api.model.TopicConfigModel;
import com.github.bluecatlee.gs4d.message.consumer.utils.Constants;
import com.github.bluecatlee.gs4d.message.consumer.utils.SeqUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlaftformMqTopicManyDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String queryAllFatherTopicsSql = "select topic from platform_mq_topic_many where  tenant_num_id=? and cancelsign='N' group by topic limit 100000";
    private static final String queryFatherTopicRelationSonTopicsSql = "select a.topic fatherTopic,a.remark,c.topic sonTopic,c.tag sonTag ,b.series relationSeries,a.series fatherSeries,a.tenant_num_id tenantNumId from platform_mq_topic_many a left JOIN platform_mq_topic_relation b on a.series=b.topic_father_series LEFT JOIN platform_mq_topic c on b.topic_son_series=c.series where a.topic=? and  a.tenant_num_id=?  and c.cancelsign='N' limit 1000000";
    private static final String queryByTopicSql = "select series,remark from platform_mq_topic_many where  topic=? and tenant_num_id=? ";
    private static final String insertSql = "insert into platform_mq_topic_many(series,topic,remark,tenant_num_id,create_dtme,cancelsign)values(?,?,?,?,sysdate(),'N')";
    private static final String deleteSql = "update platform_mq_topic_many set cancelsign='Y' where topic=? and tenant_num_id=? ";
    private static final String queryByBizTypeSql = "select topic,remark from platform_mq_topic_many where  biz_type=? ";

    public List<SonTopicRelationModel> queryAllFatherTopicRelationSonTopics() {
        String sql = "select a.topic fatherTopic,c.topic sonTopic,c.tag sonTag,a.tenant_num_id tenantNumId from platform_mq_topic_many a left JOIN platform_mq_topic_relation b on a.series=b.topic_father_series LEFT JOIN platform_mq_topic c on b.topic_son_series=c.series where c.cancelsign='N' and a.cancelsign='N'  limit 1000000";
        List list = this.jdbcTemplate.query(sql, new BeanPropertyRowMapper(SonTopicRelationModel.class));
        return list;
    }

    public List<TopicConfigModel> queryAllFatherTopics(String key, Long tenantNumId) {
        List list = this.jdbcTemplate.query(queryAllFatherTopicsSql, new Object[]{tenantNumId}, new BeanPropertyRowMapper(TopicConfigModel.class));
        return list;
    }

    public List<SonTopicRelationModel> queryFatherTopicRelationSonTopics(String fatherTopic, Long tenantNumId) {
        List list = this.jdbcTemplate.query(queryFatherTopicRelationSonTopicsSql, new Object[]{fatherTopic, tenantNumId}, new BeanPropertyRowMapper(SonTopicRelationModel.class));
        return list;
    }

    public List<TopicConfigModel> queryByTopic(String fatherTopic, Long tenantNumId) {
        List list = this.jdbcTemplate.query(queryByTopicSql, new Object[]{fatherTopic, tenantNumId}, new BeanPropertyRowMapper(TopicConfigModel.class));
        if (list.isEmpty()) {
            throw new DatabaseOperateException(Constants.SUB_SYSTEM, ExceptionType.DOE30045, "查询父topic序列号失败");
        } else {
            return list;
        }
    }

    public void insert(String topic, String remark, Long tenantNumId) {
        if (this.jdbcTemplate.update(insertSql, new Object[]{SeqUtil.getNoSubSequence(SeqUtil.PLATFORM_MQ_TOPIC_MANY_SERIES), topic, remark, tenantNumId}) < 1) {
            throw new DatabaseOperateException(Constants.SUB_SYSTEM, ExceptionType.DOE30045, "父topic插入失败");
        }
    }

    public void delete(String topic, Long tenantNumId) {
        if (this.jdbcTemplate.update(deleteSql, new Object[]{topic, tenantNumId}) < 1) {
            throw new DatabaseOperateException(Constants.SUB_SYSTEM, ExceptionType.DOE30045, "父topic删除失败");
        }
    }

    public List<TopicConfigModel> queryByBizType(Long bizType) {
        List list = this.jdbcTemplate.query(queryByBizTypeSql, new Object[]{bizType}, new BeanPropertyRowMapper(TopicConfigModel.class));
        return list;
    }
}


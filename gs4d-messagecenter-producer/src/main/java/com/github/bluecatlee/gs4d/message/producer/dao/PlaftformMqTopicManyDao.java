package com.github.bluecatlee.gs4d.message.producer.dao;

import com.github.bluecatlee.gs4d.common.exception.DatabaseOperateException;
import com.github.bluecatlee.gs4d.common.exception.ExceptionType;
import com.github.bluecatlee.gs4d.message.api.model.SonTopicRelationModel;
import com.github.bluecatlee.gs4d.message.api.model.TopicConfigModel;
import com.github.bluecatlee.gs4d.message.producer.utils.Constants;
import com.github.bluecatlee.gs4d.message.producer.utils.SeqUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlaftformMqTopicManyDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String queryAllTopics = "select topic from platform_mq_topic_many where  tenant_num_id=? and cancelsign='N' group by topic limit 100000";
    private static final String queryOTMSql = "select a.topic fatherTopic,a.remark,c.topic sonTopic,c.tag sonTag ,b.series relationSeries,a.series fatherSeries,a.tenant_num_id tenantNumId from platform_mq_topic_many a left JOIN platform_mq_topic_relation b on a.series=b.topic_father_series LEFT JOIN platform_mq_topic c on b.topic_son_series=c.series where a.topic=? and  a.tenant_num_id=?  and c.cancelsign='N' limit 1000000";
    private static final String queryFatherTopicSeriesSql = "select series from platform_mq_topic_many where  topic=? and tenant_num_id=? ";
    private static final String insertSql = "insert into platform_mq_topic_many(series,topic,remark,tenant_num_id,create_dtme,cancelsign)values(?,?,?,?,sysdate(),'N')";
    private static final String deleteSql = "update platform_mq_topic_many set cancelsign='Y' where topic=? and tenant_num_id=? ";

    public List<SonTopicRelationModel> queryAllOneToManyTopics() {
        String sql = "select a.topic fatherTopic,c.topic sonTopic,c.tag sonTag,a.tenant_num_id tenantNumId from platform_mq_topic_many a left JOIN platform_mq_topic_relation b on a.series=b.topic_father_series LEFT JOIN platform_mq_topic c on b.topic_son_series=c.series where c.cancelsign='N' and a.cancelsign='N'  limit 1000000";
        List list = this.jdbcTemplate.query(sql, new BeanPropertyRowMapper(SonTopicRelationModel.class));
        return list;
    }

    public List<TopicConfigModel> queryAllTopics(Long tenantNumId) {
        List list = this.jdbcTemplate.query(queryAllTopics, new Object[]{tenantNumId}, new BeanPropertyRowMapper(TopicConfigModel.class));
        return list;
    }

    public List<SonTopicRelationModel> queryOTM(String topic, Long tenantNumId) {
        List list = this.jdbcTemplate.query(queryOTMSql, new Object[]{topic, tenantNumId}, new BeanPropertyRowMapper(SonTopicRelationModel.class));
        return list;
    }

    public List<TopicConfigModel> queryFatherTopicSeries(String topic, Long tenantNumId) {
        List sql = this.jdbcTemplate.query(queryFatherTopicSeriesSql, new Object[]{topic, tenantNumId}, new BeanPropertyRowMapper(TopicConfigModel.class));
        if(sql.isEmpty()) {
            throw new DatabaseOperateException(Constants.SUB_SYSTEM, ExceptionType.DOE30045, "查询父topic序列号失败");
        } else {
            return sql;
        }
    }

    public void insert(String topic, String remark, Long tenantNumId) {
        if(this.jdbcTemplate.update(insertSql, new Object[]{SeqUtil.getNoSubSequence(SeqUtil.PLATFORM_MQ_TOPIC_MANY_SERIES), topic, remark, tenantNumId}) < 1) {
            throw new DatabaseOperateException(Constants.SUB_SYSTEM, ExceptionType.DOE30045, "父topic插入失败");
        }
    }

    public void delete(String topic, Long tenantNumId) {
        if(this.jdbcTemplate.update(deleteSql, new Object[]{topic, tenantNumId}) < 1) {
            throw new DatabaseOperateException(Constants.SUB_SYSTEM, ExceptionType.DOE30045, "父topic删除失败");
        }
    }
}


package com.github.bluecatlee.gs4d.message.producer.dao;

import com.github.bluecatlee.gs4d.common.exception.DatabaseOperateException;
import com.github.bluecatlee.gs4d.common.exception.ExceptionType;
import com.github.bluecatlee.gs4d.message.producer.utils.Constants;
import com.github.bluecatlee.gs4d.message.producer.utils.SeqUtil;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class PlatformMqTopicRelationDao {

    @Resource(name = "jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    private static final String deleteSql = "delete from platform_mq_topic_relation where series=?";
    private static final String insertSql = "insert into platform_mq_topic_relation(series,topic_father_series,topic_son_series,create_dtme)values(?,?,?,sysdate())";

    public void delete(Long series) {
        if(this.jdbcTemplate.update(deleteSql, new Object[]{series}) < 1) {
            throw new DatabaseOperateException(Constants.SUB_SYSTEM, ExceptionType.DOE30045, "父topic关联关系删除失败");
        }
    }

    public void insert(String topicSonSeries, String topicFatherSeries) {
        if(this.jdbcTemplate.update(insertSql, new Object[]{SeqUtil.getNoSubSequence(SeqUtil.PLATFORM_MQ_TOPIC_RELATION_SERIES), topicFatherSeries, topicSonSeries}) < 1) {
            throw new DatabaseOperateException(Constants.SUB_SYSTEM, ExceptionType.DOE30045, "父topic关联插入失败");
        }
    }
}

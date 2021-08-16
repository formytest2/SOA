package com.github.bluecatlee.gs4d.message.producer.dao;

import com.github.bluecatlee.gs4d.message.producer.model.SYS_ROCKET_MQ_MESSAGE_ENCRYLOG;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SysRocketMqMessageEncrylogDao {

    @Resource(name = "jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    private final String querySql = "select MSG_SERIES from sys_rocket_mq_message_encrylog where msgmd  =? and topic=? and tag=? and data_sign= ? and tenant_num_id=?";
    private final String insertSql = "insert into sys_rocket_mq_message_encrylog (msgmd,msg_series,topic,tag,data_sign,tenant_num_id)values(?,?,?,?,?,?)";
    private final String deleteSql = "delete from sys_rocket_mq_message_encrylog where data_sign=? and tenant_num_id=? and create_dtme between  DATE_SUB(sysdate(), INTERVAL ? DAY) and sysdate()";

    public List<SYS_ROCKET_MQ_MESSAGE_ENCRYLOG> query(String msgmd, String topic, String tag, Long dataSign, Long tenantNumId) {
        List list = this.jdbcTemplate.query(querySql, new Object[]{msgmd, topic, tag, dataSign, tenantNumId}, new BeanPropertyRowMapper(SYS_ROCKET_MQ_MESSAGE_ENCRYLOG.class));
        return list;
    }

    public void insert(Long msgSeries, String msgmd, String topic, String tag, Long dataSign, Long tenantNumId) {
        Object[] args = new Object[]{msgmd, msgSeries, topic, tag, dataSign, tenantNumId};
        if(this.jdbcTemplate.update(insertSql, args) <= 0) {
            throw new RuntimeException("MD5消息获取序列插入失败!");
        }
    }

    public void delete(Long interval, Long tenantNumId, Long dataSign) {
        Object[] args = new Object[]{dataSign, tenantNumId, interval};
        this.jdbcTemplate.update(deleteSql, args);
    }
}


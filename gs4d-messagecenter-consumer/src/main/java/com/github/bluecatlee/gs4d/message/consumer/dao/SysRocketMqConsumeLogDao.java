package com.github.bluecatlee.gs4d.message.consumer.dao;

import com.github.bluecatlee.gs4d.message.consumer.model.SysRocketMqConsumeLog;
import com.github.bluecatlee.gs4d.message.consumer.utils.SeqUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class SysRocketMqConsumeLogDao {

    @Resource(name = "jdbcTemplate")
    private JdbcTemplate jdbcTemplate;
    
    @Value("#{settings['dataSign']}")
    private Long dataSign;

    private static final String insertSql = "INSERT INTO sys_rocket_mq_consume_log (SERIES,MESSAGE_SERIES,CONSUMER_GROUP,IS_SUCCESS,CREATE_USER_ID,FAIL_DETAIL,tenant_num_id,data_sign,instance_id,create_dtme) VALUES (? ,? ,? ,? ,? ,?,?,?,?,?)";

    public SysRocketMqConsumeLog insert(SysRocketMqConsumeLog entity) {
        entity.setSERIES(SeqUtil.getNoSubSequence(SeqUtil.SYS_ROCKET_MQ_CONSUMER_LOG_SERIES));
        Object[] args = new Object[]{entity.getSERIES(), entity.getMESSAGE_SERIES(), entity.getCONSUMER_GROUP(), entity.getIS_SUCCESS(), entity.getCREATE_USER_ID(), entity.getFAIL_DETAIL(), entity.getTENANT_NUM_ID(), entity.getDATA_SIGN(), entity.getINSTANCE_ID(), new Date()};
        if (this.jdbcTemplate.update(insertSql, args) <= 0) {
            throw new RuntimeException("插入消息消费表SYS_ROCKET_MQ_CONSUME_LOG失败");
        } else {
            return entity;
        }
    }
}


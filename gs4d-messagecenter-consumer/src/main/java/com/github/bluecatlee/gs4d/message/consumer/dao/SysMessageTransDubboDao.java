package com.github.bluecatlee.gs4d.message.consumer.dao;

import com.github.bluecatlee.gs4d.message.consumer.model.SysMessageTransDubbo;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SysMessageTransDubboDao {

    @Resource(name = "jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    private static final String queryAllSql = "select system_num_id,dubbo_group,zk_adr,zk_adr_test,zk_adr_development from sys_message_trans_dubbo";

    public List<SysMessageTransDubbo> queryAll() {
        List list = this.jdbcTemplate.query(queryAllSql, new BeanPropertyRowMapper(SysMessageTransDubbo.class));
        return list;
    }
}


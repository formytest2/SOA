package com.github.bluecatlee.gs4d.message.consumer.dao;

import com.github.bluecatlee.gs4d.message.consumer.model.SysMailSendReq;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SysMailSendReqDao {

    @Resource(name = "jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    public void insert(SysMailSendReq sysMailSendReq) {
        String sql = "insert into SYS_MAIL_SEND_REQ(SERIES,CC_MAIL,CONTENT,DATA_SIGN,SEND_MAIL,TENANT_NUM_ID,TITLE,CREATEDTME,LASTUPDTME,STATUS)values(?,?,?,?,?,?,?,sysdate(),sysdate(),0)";
        Object[] args = new Object[]{sysMailSendReq.getSERIES(), sysMailSendReq.getCC_MAIL(), sysMailSendReq.getCONTENT(), sysMailSendReq.getDATA_SIGN(), sysMailSendReq.getSEND_MAIL(), sysMailSendReq.getTENANT_NUM_ID(), sysMailSendReq.getTITLE()};
        if (this.jdbcTemplate.update(sql, args) <= 0) {
            throw new RuntimeException("插入失败!");
        }
    }
}

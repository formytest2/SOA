package com.github.bluecatlee.gs4d.message.producer.dao;

import com.github.bluecatlee.gs4d.message.producer.model.SYS_ORDER_MESSAGE_SEND_LOG;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SysOrderMessageSendLogDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Value("#{settings['mycatGoMaster']}")
    private String mycatGoMaster;

    public static final String insertSql = "INSERT INTO SYS_ORDER_MESSAGE_SEND_LOG(DATA_SIGN,FROM_SYSTEM,MESSAGE_BODY,MESSAGE_KEY,MESSAGE_TAG,MESSAGE_TOPIC,ORDER_ID,ORDER_MESSAGE_GROUP_ID,SERIES,TENANT_NUM_ID,CREATE_DTME,CLIENT_IP,CANCEL_SIGN,SEND_SIGN) values (?,?,?,?,?,?,?,?,?,?,sysdate(),?,'N','N')";

    public void insert(SYS_ORDER_MESSAGE_SEND_LOG entity) {
        Object[] args = new Object[]{entity.getDATA_SIGN(), entity.getFROM_SYSTEM(), entity.getMESSAGE_BODY(), entity.getMESSAGE_KEY(), entity.getMESSAGE_TAG(), entity.getMESSAGE_TOPIC(), entity.getORDER_ID(), entity.getORDER_MESSAGE_GROUP_ID(), entity.getSERIES(), entity.getTENANT_NUM_ID(), entity.getCLIENT_IP()};
        if(this.jdbcTemplate.update(insertSql, args) <= 0) {
            throw new RuntimeException("插入失败!");
        }
    }

    public List<SYS_ORDER_MESSAGE_SEND_LOG> queryUnSendOrderMessage(Long orderMessageGroupId) {
        String sql = this.mycatGoMaster + "SELECT DATA_SIGN,FROM_SYSTEM,MESSAGE_BODY,MESSAGE_KEY,MESSAGE_TAG,MESSAGE_TOPIC,SERIES,TENANT_NUM_ID,CLIENT_IP,ORDER_ID,CREATE_DTME FROM SYS_ORDER_MESSAGE_SEND_LOG WHERE ORDER_MESSAGE_GROUP_ID=? and SEND_SIGN='N' and CANCEL_SIGN='N' order by order_id asc limit 1000000";
        List list = this.jdbcTemplate.query(sql, new Object[]{orderMessageGroupId}, new BeanPropertyRowMapper(SYS_ORDER_MESSAGE_SEND_LOG.class));
        return list;
    }

    public void updateSendSuccess(Long series) {
        String sql = this.mycatGoMaster + "UPDATE SYS_ORDER_MESSAGE_SEND_LOG SET SEND_SIGN='Y' where SERIES=?";
        Object[] args = new Object[]{series};
        if(this.jdbcTemplate.update(sql, args) <= 0) {
            throw new RuntimeException("顺序消息更新发送状态失败!序列号为" + series);
        }
    }

    public void cancelBySeries(Long series) {
        String sql = this.mycatGoMaster + "UPDATE SYS_ORDER_MESSAGE_SEND_LOG SET CANCEL_SIGN='Y' where SERIES=?";
        Object[] args = new Object[]{series};
        if(this.jdbcTemplate.update(sql, args) <= 0) {
            throw new RuntimeException("顺序消息更新取消标识失败!序列号为" + series);
        }
    }

    public List<SYS_ORDER_MESSAGE_SEND_LOG> queryOrderMessageGroupIdBySeries(Long series) {
        String sql = this.mycatGoMaster + "SELECT order_message_group_id from sys_order_message_send_log WHERE SERIES=? ";
        List list = this.jdbcTemplate.query(sql, new Object[]{series}, new BeanPropertyRowMapper(SYS_ORDER_MESSAGE_SEND_LOG.class));
        return list;
    }

    public void cancelByOrderMessageGroupId(Long orderMessageGroupId) {
        String sql = this.mycatGoMaster + "UPDATE SYS_ORDER_MESSAGE_SEND_LOG SET CANCEL_SIGN='Y' where order_message_group_id=?";
        Object[] args = new Object[]{orderMessageGroupId};
        if(this.jdbcTemplate.update(sql, args) <= 0) {
            throw new RuntimeException("顺序消息更新取消标识失败!小组号为" + orderMessageGroupId);
        }
    }

    public List<SYS_ORDER_MESSAGE_SEND_LOG> getNodealFlowMessage() {
        String sql = this.mycatGoMaster + "SELECT DATA_SIGN,FROM_SYSTEM,MESSAGE_BODY,MESSAGE_KEY,MESSAGE_TAG,MESSAGE_TOPIC,SERIES,TENANT_NUM_ID,CLIENT_IP,ORDER_ID,CREATE_DTME,ORDER_MESSAGE_GROUP_ID FROM SYS_ORDER_MESSAGE_SEND_LOG WHERE SEND_SIGN='N' and CANCEL_SIGN='N' and order_id=0 and CREATE_DTME<date_add(now(), interval - 5 minute) and data_sign=0 limit 1000";
        List list = this.jdbcTemplate.query(sql, new BeanPropertyRowMapper(SYS_ORDER_MESSAGE_SEND_LOG.class));
        return list;
    }

    public void deleteCanceledAndExpiredOrderMessageSendLogs() {
        String sql = "DELETE FROM SYS_ORDER_MESSAGE_SEND_LOG  where CANCEL_SIGN='Y' and CREATE_DTME<date_add(now(), interval - 3 day) ";
        this.jdbcTemplate.update(sql);
    }
}

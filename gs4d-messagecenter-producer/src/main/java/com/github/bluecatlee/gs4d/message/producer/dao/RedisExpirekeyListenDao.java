package com.github.bluecatlee.gs4d.message.producer.dao;

import com.github.bluecatlee.gs4d.message.producer.model.REDIS_EXPIRED_KEY_TOPIC;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Deprecated
@Repository
public class RedisExpirekeyListenDao {

    @Resource(name = "jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    public List<REDIS_EXPIRED_KEY_TOPIC> queryAll() {
        String sql = "SELECT REDIS_KEY_HEAD,TOPIC,TAG FROM REDIS_EXPIRED_KEY_TOPIC where CANCELSIGN='N'";
        List list = this.jdbcTemplate.query(sql, new BeanPropertyRowMapper(REDIS_EXPIRED_KEY_TOPIC.class));
        return list;
    }

    public void updateByRedisKeyHead(String redisKeyHead) {
        Object[] arg = new Object[]{redisKeyHead};
        this.jdbcTemplate.update("update REDIS_EXPIRED_KEY_TOPIC set update_dtme = sysdate() where redis_key_head = ?", arg);
    }

    public List<REDIS_EXPIRED_KEY_TOPIC> getRedisKeyByDelay() {
        String sql = "select redis_key_head,email from redis_expired_key_topic s where update_dtme < date_add(now(), interval - notice_time_interval minute)";
        List list = this.jdbcTemplate.query(sql, new BeanPropertyRowMapper(REDIS_EXPIRED_KEY_TOPIC.class));
        return list;
    }

}

package com.github.bluecatlee.gs4d.message.producer.dao;

import com.github.bluecatlee.gs4d.message.producer.model.SYS_ROCKET_MQ_JOB_LOG;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SysRocketMqJobLogDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Value("#{settings['mycatGoMaster']}")
    private String mycatGoMaster;

    private static final String insertSql = "INSERT INTO SYS_ROCKET_MQ_JOB_LOG(SERIES,MESSAGE_KEY,MESSAGE_DETAIL,MESSAGE_TOPIC,MESSAGE_TAG,CRON,CREATE_DTME,TENANT_NUM_ID,DATA_SIGN,CLIENT_IP,CANCELSIGN) values (?,?,?,?,?,?,sysdate(),?,?,?,'N')";
    private static final String queryBySeriesSql = "SELECT SERIES,MESSAGE_TOPIC,MESSAGE_TAG,MESSAGE_DETAIL,DATA_SIGN,TENANT_NUM_ID,CANCELSIGN FROM SYS_ROCKET_MQ_JOB_LOG WHERE SERIES=?";
    private static final String cancelSql = "UPDATE SYS_ROCKET_MQ_JOB_LOG SET CANCELSIGN='Y' WHERE SERIES=? ";
    private static final String queryAllSql = "SELECT SERIES,MESSAGE_TOPIC,MESSAGE_TAG,MESSAGE_DETAIL,DATA_SIGN,TENANT_NUM_ID,CANCELSIGN,CRON FROM SYS_ROCKET_MQ_JOB_LOG WHERE CANCELSIGN='N' limit 10000000 ";
    private static final String updateSendLogSql = "UPDATE SYS_ROCKET_MQ_JOB_LOG SET ERROR_LOG=?,SEND_LOG_SERIES=?,LAST_UPDTME=sysdate() WHERE SERIES=? ";

    public void insert(SYS_ROCKET_MQ_JOB_LOG entity) {
        Object[] args = new Object[]{entity.getSERIES(), entity.getMESSAGE_KEY(), entity.getMESSAGE_DETAIL(), entity.getMESSAGE_TOPIC(), entity.getMESSAGE_TAG(), entity.getCRON(), Long.valueOf(entity.getTENANT_NUM_ID()), entity.getDATA_SIGN(), entity.getCLIENT_IP()};
        if(this.jdbcTemplate.update(insertSql, args) <= 0) {
            throw new RuntimeException("插入失败!");
        }
    }

    public List<SYS_ROCKET_MQ_JOB_LOG> queryBySeries(Long series) {
        List list = this.jdbcTemplate.query(this.mycatGoMaster + queryBySeriesSql, new Object[]{series}, new BeanPropertyRowMapper(SYS_ROCKET_MQ_JOB_LOG.class));
        return list;
    }

    public void cancelJobLog(Long series) {
        this.jdbcTemplate.update(cancelSql, new Object[]{series});
    }

    public List<SYS_ROCKET_MQ_JOB_LOG> getAllJob() {
        List list = this.jdbcTemplate.query(this.mycatGoMaster + queryAllSql, new BeanPropertyRowMapper(SYS_ROCKET_MQ_JOB_LOG.class));
        return list;
    }

    public void updateSendLog(Long series, Long sendLogSeries, String errorLog) {
        this.jdbcTemplate.update(updateSendLogSql, new Object[]{errorLog, sendLogSeries, series});
    }

}


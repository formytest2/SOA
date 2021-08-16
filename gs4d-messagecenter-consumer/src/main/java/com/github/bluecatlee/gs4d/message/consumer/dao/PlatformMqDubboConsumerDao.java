package com.github.bluecatlee.gs4d.message.consumer.dao;

import com.github.bluecatlee.gs4d.common.exception.DatabaseOperateException;
import com.github.bluecatlee.gs4d.common.exception.ExceptionType;
import com.github.bluecatlee.gs4d.common.utils.StringUtil;
import com.github.bluecatlee.gs4d.message.consumer.model.PlatformMqDubboConsumer;
import com.github.bluecatlee.gs4d.message.consumer.utils.Constants;
import com.github.bluecatlee.gs4d.message.consumer.utils.SeqUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class PlatformMqDubboConsumerDao {

    @Resource(name = "jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private static final String querySelectiveBySeriesSql = " SELECT series AS dubboSeries,service_name,method_name,param_entity,create_user_name,create_date_time,zk_adr_test,zk_adr,cancelsign,direct_adr,version,retries,zk_adr_develop,dubbo_group FROM platform_mq_dubbo_consumer WHERE series =? ";
    private static final String fuzzyQueryByKeySql = " SELECT series AS dubboSeries,service_name dubboService,method_name dubboMethod,param_entity dubboParam,create_user_name dubboCreatePerson,zk_adr_test zkTestAddress,zk_adr zkAddress,direct_adr dubboDirUrl,zk_adr_develop zkDevelopAddress,dubbo_group dubboGroup,remark dubboRemark FROM platform_mq_dubbo_consumer WHERE (series like ? or service_name like ? or method_name like ? or create_user_name like ? or dubbo_group like ? or remark like ? )and cancelsign='N' order by create_date_time desc limit ?,?";
    private static final String insertSql = "insert into platform_mq_dubbo_consumer(series,service_name,method_name,param_entity,create_user_name,zk_adr_test,zk_adr,zk_adr_develop,dubbo_group,remark,direct_adr,version,retries,cancelsign,create_date_time)values(?,?,?,?,?,?,?,?,?,?,?,?,0,'N',sysdate())";
    private static final String cancelSql = "update platform_mq_dubbo_consumer set cancelsign='Y' where series=?";

    public List<PlatformMqDubboConsumer> queryAllDubboConsumers() {
        String sql = "select series,service_name,method_name,param_entity,create_user_name,create_date_time,zk_adr_test,zk_adr,zk_adr_develop,dubbo_group,cancelsign,direct_adr,version,retries from platform_mq_dubbo_consumer  where cancelsign = 'N' LIMIT 100000 ";
        List list = this.jdbcTemplate.query(sql, new Object[0], new BeanPropertyRowMapper(PlatformMqDubboConsumer.class));
        return list;
    }

    public PlatformMqDubboConsumer queryBySeries(long series) {
        String sql = "select series,service_name,method_name,param_entity,create_user_name,create_date_time,zk_adr_test,zk_adr,zk_adr_develop,cancelsign,direct_adr,version,retries,remark,dubbo_group from platform_mq_dubbo_consumer  where series = ?";
        List list = this.jdbcTemplate.query(sql, new Object[]{series}, new BeanPropertyRowMapper(PlatformMqDubboConsumer.class));
        return list.size() == 1 ? (PlatformMqDubboConsumer)list.get(0) : null;
    }

    public List<com.github.bluecatlee.gs4d.message.api.model.PlatformMqDubboConsumer> querySelectiveBySeries(long series) {
        List list = this.jdbcTemplate.query(querySelectiveBySeriesSql, new Object[]{series}, new BeanPropertyRowMapper(com.github.bluecatlee.gs4d.message.api.model.PlatformMqDubboConsumer.class));
        return list;
    }

    public List<com.github.bluecatlee.gs4d.message.api.model.PlatformMqDubboConsumer> fuzzyQueryByKey(String key, Long offset, Long rows) {
        ArrayList args = new ArrayList();
        if (key == null) {
            key = "";
        }

        for(int i = 0; i < 6; ++i) {
            args.add("%" + key + "%");
        }

        args.add(offset);
        args.add(rows);
        List list = this.jdbcTemplate.query(fuzzyQueryByKeySql, args.toArray(), new BeanPropertyRowMapper(com.github.bluecatlee.gs4d.message.api.model.PlatformMqDubboConsumer.class));
        return list;
    }

    public void insert(String createUserName, String directAdr, String dubboGroup, String methodName, String paramEntity, String serviceName, String remark, String version, String zkAdr, String zkAdrDevelop, String zkAdrTest) {
        Long series = SeqUtil.getNoSubSequence(SeqUtil.PLATFORM_MQ_DUBBO_CONSUMER_SERIES);
        if (this.jdbcTemplate.update(insertSql, new Object[]{series, serviceName, methodName, paramEntity, createUserName, zkAdrTest, zkAdr, zkAdrDevelop, dubboGroup, remark, directAdr, version}) <= 0) {
            throw new RuntimeException("插入失败!");
        }
    }

    public void cancel(Long series) {
        if (this.jdbcTemplate.update(cancelSql, new Object[]{series}) <= 0) {
            throw new RuntimeException("修改dubbo配置表失败!");
        }
    }

    public void update(Long series, String createUserName, String directAdr, String dubboGroup, String methodName, String paramEntity, String serviceName, String remark, String version, String zkAdr, String zkAdrDevelop, String zkAdrTest) {
        StringBuffer sb = new StringBuffer(" UPDATE platform_mq_dubbo_consumer SET ");
        String condition = " WHERE series =? ";
        ArrayList args = new ArrayList();
        if (!StringUtil.isNullOrBlankTrim(createUserName)) {
            sb.append("create_user_name =?,");
            args.add(createUserName);
        }

        if (!StringUtil.isNullOrBlankTrim(dubboGroup)) {
            sb.append("dubbo_group =?,");
            args.add(dubboGroup);
        }

        if (!StringUtil.isNullOrBlankTrim(directAdr)) {
            sb.append("direct_adr =?,");
            args.add(directAdr);
        }

        if (!StringUtil.isNullOrBlankTrim(methodName)) {
            sb.append("method_name =?,");
            args.add(methodName);
        }

        if (!StringUtil.isNullOrBlankTrim(remark)) {
            sb.append("remark =?,");
            args.add(remark);
        }

        if (!StringUtil.isNullOrBlankTrim(paramEntity)) {
            sb.append("param_entity =?,");
            args.add(paramEntity);
        }

        if (!StringUtil.isNullOrBlankTrim(serviceName)) {
            sb.append("service_name =?,");
            args.add(serviceName);
        }

        if (!StringUtil.isNullOrBlankTrim(version)) {
            sb.append("version =?,");
            args.add(version);
        }

        if (!StringUtil.isNullOrBlankTrim(zkAdr)) {
            sb.append("zk_adr =?,");
            args.add(zkAdr);
        }

        if (!StringUtil.isNullOrBlankTrim(zkAdrDevelop)) {
            sb.append("zk_adr_develop =?,");
            args.add(zkAdrDevelop);
        }

        if (!StringUtil.isNullOrBlankTrim(zkAdrTest)) {
            sb.append("zk_adr_test =?,");
            args.add(zkAdrTest);
        }

        if (!args.isEmpty()) {
            sb.delete(sb.length() - 1, sb.length()).append(condition);
            args.add(series);
            if (this.jdbcTemplate.update(sb.toString(), args.toArray()) < 1) {
                throw new DatabaseOperateException(Constants.SUB_SYSTEM, ExceptionType.DOE30045, "series:" + series + "http消费表编辑失败!");
            }
        }
    }
}


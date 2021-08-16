package com.github.bluecatlee.gs4d.message.consumer.dao;

import com.github.bluecatlee.gs4d.common.exception.DatabaseOperateException;
import com.github.bluecatlee.gs4d.common.exception.ExceptionType;
import com.github.bluecatlee.gs4d.common.utils.StringUtil;
import com.github.bluecatlee.gs4d.message.consumer.model.PlatformMqHttpConsumer;
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
public class PlatformMqHttpConsumerDao {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Resource(name = "jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    private static final String querySelectiveBySeriesSql = " SELECT series AS httpSeries,url,url_test,param_name,create_user_name,create_date_time,cancelsign FROM platform_mq_http_consumer WHERE series =? ";
    private static final String fuzzyQueryByKeySql = " SELECT series AS httpSeries,url,url_test urlTest,url_develop urlDevelop,param_name paramName,remark httpRemark,http_head httpHead ,create_user_name  httpUserName FROM platform_mq_http_consumer WHERE (series like ?  or url like ?  or url_test like ? or  param_name like ?  or  create_user_name like ? or remark like ? )and cancelsign='N' order by create_date_time desc limit ?,?";
    private static final String insertSql = "insert into platform_mq_http_consumer(series,url,url_test,url_develop,param_name,create_user_name,http_head,create_date_time,cancelsign,remark)values(?,?,?,?,?,?,?,sysdate(),'N',?)";
    private static final String cancelSql = "update platform_mq_http_consumer set cancelsign='Y' where series=?";

    public PlatformMqHttpConsumer queryBySeries(long series) {
        String sql = "select * from platform_mq_http_consumer  where series = ?";
        List list = this.jdbcTemplate.query(sql, new Object[]{series}, new BeanPropertyRowMapper(PlatformMqHttpConsumer.class));
        return list.size() == 1 ? (PlatformMqHttpConsumer)list.get(0) : null;
    }

    public List<com.github.bluecatlee.gs4d.message.api.model.PlatformMqHttpConsumer> querySelectiveBySeries(long series) {
        List list = this.jdbcTemplate.query(querySelectiveBySeriesSql, new Object[]{series}, new BeanPropertyRowMapper(com.github.bluecatlee.gs4d.message.api.model.PlatformMqHttpConsumer.class));
        return list;
    }

    public List<com.github.bluecatlee.gs4d.message.api.model.PlatformMqHttpConsumer> fuzzyQueryByKey(String key, Long offset, Long rows) {
        ArrayList args = new ArrayList();
        if (key == null) {
            key = "";
        }

        for(int i = 0; i < 6; ++i) {
            args.add("%" + key + "%");
        }

        args.add(offset);
        args.add(rows);
        List list = this.jdbcTemplate.query(fuzzyQueryByKeySql, args.toArray(), new BeanPropertyRowMapper(com.github.bluecatlee.gs4d.message.api.model.PlatformMqHttpConsumer.class));
        return list;
    }

    public void insert(String createUserName, String remark, String paramName, String httpHead, String urlDevelop, String urlTest, String url) {
        Long series = SeqUtil.getNoSubSequence(SeqUtil.PLATFORM_MQ_HTTP_CONSUMER_SERIES);
        if (this.jdbcTemplate.update(insertSql, new Object[]{series, url, urlTest, urlDevelop, paramName, createUserName, httpHead, remark}) <= 0) {
            throw new RuntimeException("插入失败!");
        }
    }

    public void cancel(Long series) {
        if (this.jdbcTemplate.update(cancelSql, new Object[]{series}) <= 0) {
            throw new RuntimeException("删除http表失败!");
        }
    }

    public void update(Long series, String createUserName, String urlDevelop, String httpHead, String paramName, String remark, String urlTest, String url) {
        StringBuffer sb = new StringBuffer(" UPDATE platform_mq_http_consumer SET ");
        String condition = " WHERE series =? ";
        ArrayList args = new ArrayList();
        if (!StringUtil.isNullOrBlankTrim(createUserName)) {
            sb.append("create_user_name =?,");
            args.add(createUserName);
        }

        if (!StringUtil.isNullOrBlankTrim(urlDevelop)) {
            sb.append("url_develop =?,");
            args.add(urlDevelop);
        }

        if (!StringUtil.isNullOrBlankTrim(httpHead)) {
            sb.append("http_head =?,");
            args.add(httpHead);
        }

        if (!StringUtil.isNullOrBlankTrim(paramName)) {
            sb.append("param_name =?,");
            args.add(paramName);
        }

        if (!StringUtil.isNullOrBlankTrim(remark)) {
            sb.append("remark =?,");
            args.add(remark);
        }

        if (!StringUtil.isNullOrBlankTrim(urlTest)) {
            sb.append("url_test =?,");
            args.add(urlTest);
        }

        if (!StringUtil.isNullOrBlankTrim(url)) {
            sb.append("url =?,");
            args.add(url);
        }

        if (!StringUtil.isNullOrBlankTrim(remark)) {
            sb.append("remark =?,");
            args.add(remark);
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


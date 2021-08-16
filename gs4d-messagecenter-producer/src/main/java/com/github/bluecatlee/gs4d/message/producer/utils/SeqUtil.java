package com.github.bluecatlee.gs4d.message.producer.utils;

import com.github.bluecatlee.gs4d.sequence.utils.SeqGetUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Component("messageCenterOmpUtil")
@Lazy(false)
@Scope("singleton")
public class SeqUtil {

    // 环境
    public static int test = 2;
    public static int develop = 1;
    public static int prod = 0;

    @Value("#{settings['zookeeper.host.port']}")
    private String zkAddress;
    @Value("#{settings['dataSign']}")
    private Long dataSign;

    @Resource(name = "jdbcTemplate")
    private JdbcTemplate JdbcTemplate;

    @PostConstruct
    public void init() {
        SeqGetUtil.initeZkConfig(this.zkAddress);
    }

    public static Long getNoSubSequence(String seqName) {
        return SeqGetUtil.getNoSubSequence(Constants.SUB_SYSTEM, seqName);
    }

    public static final String SYS_ROCKET_MQ_SEND_LOG_SERIES = "sys_rocket_mq_send_log_series";
    public static final String SYS_ROCKET_MQ_JOB_LOG_SERIES = "sys_rocket_mq_job_log_series";
    public static final String PLATFORM_MQ_TOPIC_MANY_SERIES = "platform_mq_topic_many_series";
    public static final String PLATFORM_MQ_TOPIC_SERIES = "platform_mq_topic_series";
    public static final String PLATFORM_MQ_TOPIC_MSGID = "platform_mq_topic_msgid";
    public static final String PLATFORM_MQ_TOPIC_RELATION_SERIES = "platform_mq_topic_relation_series";
    public static final String ORDER_MESSAGE_GROUP_ID = "order_message_group_id";

}

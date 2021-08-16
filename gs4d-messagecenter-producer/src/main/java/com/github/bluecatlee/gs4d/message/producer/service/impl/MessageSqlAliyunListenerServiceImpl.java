package com.github.bluecatlee.gs4d.message.producer.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.aliyun.openservices.ons.api.*;
import com.github.bluecatlee.gs4d.message.producer.dao.SysRocketMqSendLogDao;
import com.github.bluecatlee.gs4d.message.producer.model.SYS_ROCKET_MQ_SEND_LOG;
import com.github.bluecatlee.gs4d.message.producer.service.MessageSqlListenerService;
import com.github.bluecatlee.gs4d.message.producer.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Properties;

/**
 * 阿里云消息监听处理
 */
@Service("messageSqlAliyunListenerService")
public class MessageSqlAliyunListenerServiceImpl implements MessageSqlListenerService {

    private static Logger logger = LoggerFactory.getLogger(MessageSqlListenerServiceImpl.class);
    protected static Logger messSaveFailedLogger = LoggerFactory.getLogger("messSaveFailedLog");

    @Value("${aliyunMqAccessKey}")
    private String aliyunMqAccessKey;
    @Value("${aliyunMqSecretKey}")
    private String aliyunMqSecretKey;
    @Value("${aliyunMqUrl}")
    private String aliyunMqUrl;

    @Value("#{settings['dataSign']}")
    private Long dataSign;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    @Autowired
    public SysRocketMqSendLogDao sysRocketMqSendLogDao;

    public void initMqSqlConsumer() throws MQClientException {
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.ConsumerId, Constants.getAliyunMqConsumerId(Constants.MESS_SQL, Constants.MESS_UPDATE_SEND_SQL));
        properties.put(PropertyKeyConst.AccessKey, this.aliyunMqAccessKey);
        properties.put(PropertyKeyConst.SecretKey, this.aliyunMqSecretKey);
        properties.put(PropertyKeyConst.ONSAddr, this.aliyunMqUrl);
        properties.put(PropertyKeyConst.ConsumeThreadNums, Integer.valueOf(50));   // 消费者线程数
        properties.put(PropertyKeyConst.MaxReconsumeTimes, Integer.valueOf(5));    // 最大重试次数
        properties.put(PropertyKeyConst.ConsumeTimeout, Integer.valueOf(10));      // 消费超时时间
        Consumer consumer = ONSFactory.createConsumer(properties);
        consumer.subscribe(Constants.MESS_SQL, Constants.MESS_UPDATE_SEND_SQL, new MessageListener() {
            public Action consume(Message message, ConsumeContext consumeContext) {
                Action action = Action.CommitMessage;
                String body = new String(message.getBody());
                SYS_ROCKET_MQ_SEND_LOG entity = JSONObject.parseObject(body, SYS_ROCKET_MQ_SEND_LOG.class);

                try {
                    sysRocketMqSendLogDao.updateMsgIdAndMsgStatusSqlAndMessFlag(entity);
                } catch (Exception e) {
                    action = Action.ReconsumeLater;
                    logger.error(e.getMessage());
                }

                return action;
            }
        });
        consumer.start();
        logger.info("初始化"+Constants.MESS_UPDATE_SEND_SQL+"成功");
    }
}


package com.github.bluecatlee.gs4d.message.consumer.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.aliyun.openservices.ons.api.*;
import com.github.bluecatlee.gs4d.message.api.model.MsgConsumeModel;
import com.github.bluecatlee.gs4d.message.consumer.dao.SysRocketMqConsumeFailedlogDao;
import com.github.bluecatlee.gs4d.message.consumer.dao.SysRocketMqSendLogDao;
import com.github.bluecatlee.gs4d.message.consumer.dao.SysRocketMqSendLogHistoryDao;
import com.github.bluecatlee.gs4d.message.consumer.model.SysRocketMqSendLog;
import com.github.bluecatlee.gs4d.message.consumer.service.MessageSqlListenerService;
import com.github.bluecatlee.gs4d.message.consumer.service.UpdateConsumerResultService;
import com.github.bluecatlee.gs4d.message.consumer.utils.Constants;
import com.github.bluecatlee.gs4d.message.consumer.utils.SeqUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Properties;

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
    
    @Autowired
    private PlatformTransactionManager platformTransactionManager;
    
    @Value("#{settings['initalConsumerFlag']}")
    private String initalConsumerFlag;
    @Value("#{settings['dataSign']}")
    private Long dataSign;
    
    @Autowired
    public SysRocketMqSendLogDao sysRocketMqSendLogDao;
    
    @Autowired
    private SysRocketMqConsumeFailedlogDao sysRocketMqConsumeFailedlogDao;
    
    @Autowired
    private SysRocketMqSendLogHistoryDao sysRocketMqSendLogHistoryDao;
    
    @Autowired
    private UpdateConsumerResultService updateConsumerResultService;
    
    @Value("#{settings['consumerFiledTag']}")
    private String consumerFiledTag;

    public void initMqSqlConsumer() throws MQClientException {
        Boolean flag = true;
        Properties properties;
        Consumer consumer;
        Properties properties2;
        Consumer consumer2;
        if (!"Y".equalsIgnoreCase(this.initalConsumerFlag) || this.dataSign != (long) SeqUtil.prod) {
            properties = new Properties();
            properties.put("ConsumerId", Constants.getAliyunMqConsumerId(Constants.MESS_SQL, Constants.MESS_SAVE_SQL));
            properties.put("AccessKey", this.aliyunMqAccessKey);
            properties.put("SecretKey", this.aliyunMqSecretKey);
            properties.put("ONSAddr", this.aliyunMqUrl);
            properties.put("ConsumeThreadNums", 50);
            properties.put("maxReconsumeTimes", 5);
            properties.put("consumeTimeout", 10);
            consumer = ONSFactory.createConsumer(properties);
            consumer.subscribe(Constants.MESS_SQL, Constants.MESS_SAVE_SQL, new MessageListener() {
                public Action consume(Message message, ConsumeContext consumeContext) {
                    Action action = Action.CommitMessage;
                    String body = new String(message.getBody());
                    SysRocketMqSendLog sysRocketMqSendLog = (SysRocketMqSendLog)JSONObject.parseObject(body, SysRocketMqSendLog.class);

                    try {
                        MessageSqlAliyunListenerServiceImpl.this.sysRocketMqSendLogDao.insert(sysRocketMqSendLog);
                    } catch (DuplicateKeyException e) {
                        MessageSqlAliyunListenerServiceImpl.messSaveFailedLogger.info("发现消息重复" + sysRocketMqSendLog.getSERIES());
                    } catch (Exception e) {
                        action = Action.ReconsumeLater;
                        MessageSqlAliyunListenerServiceImpl.messSaveFailedLogger.error(e.getMessage(), e);
                    }

                    return action;
                }
            });
            consumer.start();
            logger.info("初始化"+Constants.MESS_SAVE_SQL+"成功");

            properties2 = new Properties();
            properties2.put("ConsumerId", Constants.getAliyunMqConsumerId(Constants.MESS_SQL, Constants.MESS_UPDATE_SEND_SQL));
            properties2.put("AccessKey", this.aliyunMqAccessKey);
            properties2.put("SecretKey", this.aliyunMqSecretKey);
            properties2.put("ONSAddr", this.aliyunMqUrl);
            properties2.put("ConsumeThreadNums", 50);
            properties2.put("maxReconsumeTimes", 5);
            properties2.put("consumeTimeout", 10);
            consumer2 = ONSFactory.createConsumer(properties2);
            consumer2.subscribe(Constants.MESS_SQL, Constants.MESS_UPDATE_SEND_SQL, new MessageListener() {
                public Action consume(Message message, ConsumeContext consumeContext) {
                    Action action = Action.CommitMessage;
                    String body = new String(message.getBody());
                    SysRocketMqSendLog sysRocketMqSendLog = (SysRocketMqSendLog)JSONObject.parseObject(body, SysRocketMqSendLog.class);

                    try {
                        MessageSqlAliyunListenerServiceImpl.this.sysRocketMqSendLogDao.update(sysRocketMqSendLog);
                    } catch (Exception e) {
                        action = Action.CommitMessage;
                        MessageSqlAliyunListenerServiceImpl.logger.error(e.getMessage());
                    }

                    return action;
                }
            });
            consumer2.start();
            logger.info("初始化MESS_UPDATE_SEND_SQL成功");
        }

        if (flag && "Y".equals(this.initalConsumerFlag)) {
            properties = new Properties();
            properties.put("ConsumerId", Constants.getAliyunMqConsumerId(Constants.MESS_SQL, Constants.MESS_UPDATE_SUCC_SQL));
            properties.put("AccessKey", this.aliyunMqAccessKey);
            properties.put("SecretKey", this.aliyunMqSecretKey);
            properties.put("ONSAddr", this.aliyunMqUrl);
            properties.put("ConsumeThreadNums", 50);
            properties.put("maxReconsumeTimes", 5);
            properties.put("consumeTimeout", 10);
            consumer = ONSFactory.createConsumer(properties);
            consumer.subscribe(Constants.MESS_SQL, Constants.MESS_UPDATE_SUCC_SQL, new MessageListener() {
                public Action consume(Message message, ConsumeContext consumeContext) {
                    Action action = Action.CommitMessage;
                    String body = new String(message.getBody());
                    MsgConsumeModel msgConsumeModel = (MsgConsumeModel)JSONObject.parseObject(body, MsgConsumeModel.class);
                    Integer result = MessageSqlAliyunListenerServiceImpl.this.updateConsumerResultService.updateConsumerSuccessResult(msgConsumeModel.getMsgSeries(), msgConsumeModel.getTaskTarget(), msgConsumeModel.getConsumerSuccessDetail(), msgConsumeModel.getConsumeTime(), msgConsumeModel.getStepId());
                    if (result != 0) {
                        action = Action.CommitMessage;
                    }

                    return action;
                }
            });
            consumer.start();
            logger.info("初始化"+Constants.MESS_UPDATE_SUCC_SQL+"成功");

            properties2 = new Properties();
            properties2.put("ConsumerId", Constants.getAliyunMqConsumerId(Constants.MESS_SQL, Constants.MESS_UPDATE_FAIL_SQL));
            properties2.put("AccessKey", this.aliyunMqAccessKey);
            properties2.put("SecretKey", this.aliyunMqSecretKey);
            properties2.put("ONSAddr", this.aliyunMqUrl);
            properties2.put("ConsumeThreadNums", 50);
            properties2.put("maxReconsumeTimes", 5);
            properties2.put("consumeTimeout", 10);
            consumer2 = ONSFactory.createConsumer(properties2);
            consumer2.subscribe(Constants.MESS_SQL, Constants.MESS_UPDATE_FAIL_SQL, new MessageListener() {
                public Action consume(Message message, ConsumeContext consumeContext) {
                    Action action = Action.CommitMessage;
                    String body = new String(message.getBody());
                    MsgConsumeModel msgConsumeModel = (MsgConsumeModel)JSONObject.parseObject(body, MsgConsumeModel.class);

                    try {
                        if (msgConsumeModel.getRetryTimes().equals(msgConsumeModel.getRetryMax())) {
                            msgConsumeModel.setNextretryTime("0");
                        }

                        if (msgConsumeModel.getConsumeTime() == null) {
                            msgConsumeModel.setConsumeTime(0L);
                        }

                        MessageSqlAliyunListenerServiceImpl.this.sysRocketMqSendLogDao.update(msgConsumeModel.getTaskTarget(), msgConsumeModel.getConsumerSuccessDetail(), msgConsumeModel.getMsgSeries(), msgConsumeModel.getNextretryTime(), msgConsumeModel.getConsumeTime(), msgConsumeModel.getStepId());
                        if (msgConsumeModel.getRetryTimes().equals(msgConsumeModel.getRetryMax()) && msgConsumeModel.getDataSign() == (long)SeqUtil.prod) {
                            try {
                                MessageSqlAliyunListenerServiceImpl.messSaveFailedLogger.error("消费失败超过次数，series:" + msgConsumeModel.getMsgSeries());
                                MessageSqlAliyunListenerServiceImpl.this.sysRocketMqConsumeFailedlogDao.insert(msgConsumeModel.getMsgSeries(), 1L, msgConsumeModel.getTenantNumId(), msgConsumeModel.getDataSign());
                            } catch (Exception e) {
                                MessageSqlAliyunListenerServiceImpl.messSaveFailedLogger.error("消息中心消费失败更新数据库时失败,序列号" + msgConsumeModel.getMsgSeries());
                            }
                        }
                    } catch (Exception e) {
                        action = Action.CommitMessage;
                        MessageSqlAliyunListenerServiceImpl.logger.error("消息中心消费失败更新数据库时失败", e);
                    }

                    return action;
                }
            });
            consumer2.start();
            logger.info("初始化"+Constants.MESS_UPDATE_FAIL_SQL+"成功");
        }

    }
}


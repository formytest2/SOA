package com.github.bluecatlee.gs4d.message.consumer.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.*;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.consumer.ConsumeFromWhere;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.alibaba.rocketmq.common.protocol.heartbeat.MessageModel;
import com.alibaba.rocketmq.tools.admin.DefaultMQAdminExt;
import com.github.bluecatlee.gs4d.common.utils.Md5Util;
import com.github.bluecatlee.gs4d.message.api.model.MsgConsumeModel;
import com.github.bluecatlee.gs4d.message.api.model.RocketMqFlushModel;
import com.github.bluecatlee.gs4d.message.api.utils.ExceptionUtils;
import com.github.bluecatlee.gs4d.message.consumer.dao.*;
import com.github.bluecatlee.gs4d.message.consumer.model.PlatformMqDubboConsumer;
import com.github.bluecatlee.gs4d.message.consumer.model.PlatformMqTopic;
import com.github.bluecatlee.gs4d.message.consumer.service.MessageSqlListenerService;
import com.github.bluecatlee.gs4d.message.consumer.service.MqConsumerService;
import com.github.bluecatlee.gs4d.message.consumer.service.MqDubboConsumerService;
import com.github.bluecatlee.gs4d.message.consumer.service.UpdateConsumerResultService;
import com.github.bluecatlee.gs4d.message.consumer.utils.Constants;
import com.github.bluecatlee.gs4d.message.consumer.utils.TopicUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.*;

@Service("messageSqlListenerService")
public class MessageSqlListenerServiceImpl implements MessageSqlListenerService {

    private static Logger logger = LoggerFactory.getLogger(MessageSqlListenerServiceImpl.class);
    protected static Logger messSaveFailedLogger = LoggerFactory.getLogger("messSaveFailedLog");

    @Value("#{settings['mqNameServAddrMessOnly']}")
    private String MQ_NAME_SERV_ADDR_MESS_ONLY;
    
    @Autowired
    private UpdateConsumerResultService updateConsumerResultService;
    
    @Value("#{settings['consumerFiledTag']}")
    private String consumerFiledTag;
    
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
    private PlatformMqTopicDao platformMqTopicDao;
    
    @Autowired
    private MqConsumerService mqConsumerService;
    
    @Autowired
    private MqDubboConsumerService mqDubboConsumerService;
    
    @Autowired
    private PlatformMqDubboConsumerDao platformMqDubboConsumerDao;

    public void initMqSqlConsumer() throws MQClientException {
        TopicUtil.initTopic(Constants.MESS_SQL, this.MQ_NAME_SERV_ADDR_MESS_ONLY);          // 创建messonly的topic：MESS_SQL
        DefaultMQPushConsumer mqPushConsumer1 = new DefaultMQPushConsumer(Constants.MESS_SQL + "-" + Constants.MESS_UPDATE_SUCC_SQL);
        mqPushConsumer1.setNamesrvAddr(this.MQ_NAME_SERV_ADDR_MESS_ONLY);
        mqPushConsumer1.setInstanceName((new Date()).getTime() + Md5Util.md5(UUID.randomUUID().toString()));
        mqPushConsumer1.subscribe(Constants.MESS_SQL, Constants.MESS_UPDATE_SUCC_SQL);
        mqPushConsumer1.setConsumeThreadMin(50);
        mqPushConsumer1.setConsumeThreadMax(51);
        mqPushConsumer1.setPullBatchSize(1);
        mqPushConsumer1.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
        mqPushConsumer1.setMessageListener(new MessageListenerOrderly() {
            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> messageExts, ConsumeOrderlyContext consumeOrderlyContext) {
                ConsumeOrderlyStatus consumeOrderlyStatus = ConsumeOrderlyStatus.SUCCESS;
                MessageExt messageExt = (MessageExt)messageExts.get(0);
                String body = new String(messageExt.getBody());
                MsgConsumeModel msgConsumeModel = (MsgConsumeModel)JSONObject.parseObject(body, MsgConsumeModel.class);
                Integer result = MessageSqlListenerServiceImpl.this.updateConsumerResultService.updateConsumerSuccessResult(msgConsumeModel.getMsgSeries(), msgConsumeModel.getTaskTarget(), msgConsumeModel.getConsumerSuccessDetail(), msgConsumeModel.getConsumeTime(), msgConsumeModel.getStepId());
                if (result != 0 && result != -1) {
                    consumeOrderlyStatus = ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
                }

                return consumeOrderlyStatus;
            }
        });
        mqPushConsumer1.start();
        logger.info("初始化"+Constants.MESS_UPDATE_SUCC_SQL+"成功");

        DefaultMQPushConsumer mqPushConsumer2 = new DefaultMQPushConsumer(Constants.MESS_SQL + "-" + Constants.MESS_UPDATE_FAIL_SQL);
        mqPushConsumer2.setNamesrvAddr(this.MQ_NAME_SERV_ADDR_MESS_ONLY);
        mqPushConsumer2.setInstanceName((new Date()).getTime() + Md5Util.md5(UUID.randomUUID().toString()));
        mqPushConsumer2.subscribe(Constants.MESS_SQL, Constants.MESS_UPDATE_FAIL_SQL);
        mqPushConsumer2.setConsumeThreadMin(50);
        mqPushConsumer2.setConsumeThreadMax(51);
        mqPushConsumer2.setPullBatchSize(1);
        mqPushConsumer2.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
        mqPushConsumer2.setMessageListener(new MessageListenerOrderly() {
            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> messageExts, ConsumeOrderlyContext consumeOrderlyContext) {
                Integer result = 0;
                ConsumeOrderlyStatus consumeOrderlyStatus = ConsumeOrderlyStatus.SUCCESS;
                MessageExt messageExt = (MessageExt)messageExts.get(0);
                String body = new String(messageExt.getBody());
                MsgConsumeModel msgConsumeModel = (MsgConsumeModel)JSONObject.parseObject(body, MsgConsumeModel.class);
                result = MessageSqlListenerServiceImpl.this.updateConsumerResultService.updateConsumerFailedResult(msgConsumeModel.getMsgSeries(), msgConsumeModel.getRetryMax(), msgConsumeModel.getTaskTarget(), msgConsumeModel.getConsumerSuccessDetail(), msgConsumeModel.getNextretryTime(), msgConsumeModel.getRetryTimes(), "", msgConsumeModel.getConsumeTime(), msgConsumeModel.getTenantNumId(), msgConsumeModel.getDataSign(), msgConsumeModel.getStepId());
                if (result != 0) {
                    consumeOrderlyStatus = ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
                }

                return consumeOrderlyStatus;
            }
        });
        mqPushConsumer2.start();
        logger.info("初始化"+Constants.MESS_UPDATE_FAIL_SQL+"成功");

        TopicUtil.initTopic(ExceptionUtils.FLUSH_MQ_TOPIC, this.MQ_NAME_SERV_ADDR_MESS_ONLY);
        DefaultMQPushConsumer mqPushConsumer3 = new DefaultMQPushConsumer(ExceptionUtils.FLUSH_MQ_TOPIC);
        mqPushConsumer3.setNamesrvAddr(this.MQ_NAME_SERV_ADDR_MESS_ONLY);
        mqPushConsumer3.setInstanceName((new Date()).getTime() + Md5Util.md5(UUID.randomUUID().toString()));
        mqPushConsumer3.subscribe(ExceptionUtils.FLUSH_MQ_TOPIC, "*");
        mqPushConsumer3.setMessageModel(MessageModel.BROADCASTING);
        mqPushConsumer3.setConsumeThreadMin(2);
        mqPushConsumer3.setConsumeThreadMax(3);
        mqPushConsumer3.setPullBatchSize(1);
        mqPushConsumer3.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
        mqPushConsumer3.setMessageListener(new MessageListenerConcurrently() {
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> messageExts, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                ConsumeConcurrentlyStatus consumeConcurrentlyStatus = ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                MessageExt messageExt = (MessageExt)messageExts.get(0);
                String body = new String(messageExt.getBody());
                RocketMqFlushModel rocketMqFlushModel = (RocketMqFlushModel)JSONObject.parseObject(body, RocketMqFlushModel.class);
                boolean flag = false;

                ConsumeConcurrentlyStatus consumeConcurrentlyStatus2;
                Iterator mQAdminExtListIterator;
                DefaultMQAdminExt defaultMQAdminExt;
                label260: {
                    ConsumeConcurrentlyStatus consumeConcurrentlyStatus3;
                    label261: {
                        label274: {
                            Iterator mQAdminExtListIterator2;
                            DefaultMQAdminExt defaultMQAdminExt2;
                            label275: {
                                try {
                                    flag = true;
                                    List platformMqTopicList = MessageSqlListenerServiceImpl.this.platformMqTopicDao.queryBySeries(rocketMqFlushModel.getSeries());
                                    if (platformMqTopicList.size() == 0) {
                                        MessageSqlListenerServiceImpl.logger.error("刷新series为" + rocketMqFlushModel.getSeries() + "的topic时,没有找到原始topic。");
                                        consumeConcurrentlyStatus = ConsumeConcurrentlyStatus.RECONSUME_LATER;
                                        consumeConcurrentlyStatus2 = consumeConcurrentlyStatus;
                                        flag = false;
                                        break label274;
                                    }

                                    if (rocketMqFlushModel.getFlushFlag().equals("N")) {
                                        if (Constants.consumerGroupTable.get(((PlatformMqTopic)platformMqTopicList.get(0)).getTOPIC() + "-" + ((PlatformMqTopic)platformMqTopicList.get(0)).getTAG()) != null) {
                                            MessageSqlListenerServiceImpl.logger.warn("序列为" + rocketMqFlushModel.getSeries() + "的mq已经启动无需刷新");
                                            consumeConcurrentlyStatus2 = consumeConcurrentlyStatus;
                                            flag = false;
                                            break label260;
                                        }

                                        ArrayList mqPushConsumerList = new ArrayList();
                                        MessageSqlListenerServiceImpl.this.mqConsumerService.initalConsumer((PlatformMqTopic)platformMqTopicList.get(0), mqPushConsumerList);
                                        mQAdminExtListIterator = mqPushConsumerList.iterator();

                                        while(mQAdminExtListIterator.hasNext()) {
                                            DefaultMQPushConsumer mqPushConsumer = (DefaultMQPushConsumer)mQAdminExtListIterator.next();

                                            try {
                                                mqPushConsumer.start();
                                                Constants.consumerGroupTable.put(mqPushConsumer.getConsumerGroup(), mqPushConsumer);
                                                PlatformMqTopic platformMqTopic = (PlatformMqTopic)platformMqTopicList.get(0);
                                                if (platformMqTopic.getCONSUMER_TYPE() == 1 && platformMqTopic.getCONSUMER_SERIES() != null) {
                                                    PlatformMqDubboConsumer platformMqDubboConsumer = MessageSqlListenerServiceImpl.this.platformMqDubboConsumerDao.queryBySeries(platformMqTopic.getCONSUMER_SERIES());
                                                    if (platformMqDubboConsumer != null) {
                                                        MessageSqlListenerServiceImpl.this.mqDubboConsumerService.initalDubboMethodCore(platformMqDubboConsumer);
                                                    }
                                                }
                                            } catch (Exception e) {
                                                MessageSqlListenerServiceImpl.logger.error(e.getMessage(), e);
                                                throw new RuntimeException(e);
                                            }
                                        }

                                        flag = false;
                                        break label275;
                                    }

                                    PlatformMqTopic platformMqTopic = (PlatformMqTopic)platformMqTopicList.get(0);
                                    DefaultMQPushConsumer mqPushConsumer = (DefaultMQPushConsumer) Constants.consumerGroupTable.get(platformMqTopic.getTOPIC() + "-" + platformMqTopic.getTAG());
                                    if (mqPushConsumer == null) {
                                        MessageSqlListenerServiceImpl.logger.warn("topic为" + platformMqTopic.getTOPIC() + "tag为" + platformMqTopic.getTAG() + "的消息缓存中不存在，不需要重启");
                                        consumeConcurrentlyStatus3 = consumeConcurrentlyStatus;
                                        flag = false;
                                        break label261;
                                    }

                                    mqPushConsumer.shutdown();
                                    Constants.consumerGroupTable.remove(platformMqTopic.getTOPIC() + "-" + platformMqTopic.getTAG());
                                    Constants.topicTagToConsumerInterval.remove(platformMqTopic.getTOPIC() + "#" + platformMqTopic.getTAG());
                                    Constants.topicTagToRetryIntervalList.remove(platformMqTopic.getTOPIC() + "#" + platformMqTopic.getTAG());
                                    Constants.topicTagToCorrectCodesList.remove(platformMqTopic.getTOPIC() + "#" + platformMqTopic.getTAG());
                                    flag = false;
                                    break label275;
                                } catch (Exception e) {
                                    MessageSqlListenerServiceImpl.logger.error("刷新topic发生异常，行号" + rocketMqFlushModel.getSeries() + "原因" + e.getMessage());
                                    consumeConcurrentlyStatus = ConsumeConcurrentlyStatus.RECONSUME_LATER;
                                    flag = false;
                                } finally {
                                    if (flag) {
                                        Iterator iterator = MqConsumerServiceImpl.namesrvAddr2MqAdminExt.values().iterator();

                                        while(iterator.hasNext()) {
                                            DefaultMQAdminExt mqAdminExt = (DefaultMQAdminExt)iterator.next();
                                            mqAdminExt.shutdown();
                                        }

                                        MqConsumerServiceImpl.namesrvAddr2MqAdminExt.clear();
                                    }
                                }

                                mQAdminExtListIterator2 = MqConsumerServiceImpl.namesrvAddr2MqAdminExt.values().iterator();

                                while(mQAdminExtListIterator2.hasNext()) {
                                    defaultMQAdminExt2 = (DefaultMQAdminExt)mQAdminExtListIterator2.next();
                                    defaultMQAdminExt2.shutdown();
                                }

                                MqConsumerServiceImpl.namesrvAddr2MqAdminExt.clear();
                                return consumeConcurrentlyStatus;
                            }

                            mQAdminExtListIterator2 = MqConsumerServiceImpl.namesrvAddr2MqAdminExt.values().iterator();

                            while(mQAdminExtListIterator2.hasNext()) {
                                defaultMQAdminExt2 = (DefaultMQAdminExt)mQAdminExtListIterator2.next();
                                defaultMQAdminExt2.shutdown();
                            }

                            MqConsumerServiceImpl.namesrvAddr2MqAdminExt.clear();
                            return consumeConcurrentlyStatus;
                        }

                        mQAdminExtListIterator = MqConsumerServiceImpl.namesrvAddr2MqAdminExt.values().iterator();

                        while(mQAdminExtListIterator.hasNext()) {
                            defaultMQAdminExt = (DefaultMQAdminExt)mQAdminExtListIterator.next();
                            defaultMQAdminExt.shutdown();
                        }

                        MqConsumerServiceImpl.namesrvAddr2MqAdminExt.clear();
                        return consumeConcurrentlyStatus2;
                    }

                    Iterator mQAdminExtListIterator3 = MqConsumerServiceImpl.namesrvAddr2MqAdminExt.values().iterator();

                    while(mQAdminExtListIterator3.hasNext()) {
                        DefaultMQAdminExt mqAdminExt = (DefaultMQAdminExt)mQAdminExtListIterator3.next();
                        mqAdminExt.shutdown();
                    }

                    MqConsumerServiceImpl.namesrvAddr2MqAdminExt.clear();
                    return consumeConcurrentlyStatus3;
                }

                mQAdminExtListIterator = MqConsumerServiceImpl.namesrvAddr2MqAdminExt.values().iterator();

                while(mQAdminExtListIterator.hasNext()) {
                    defaultMQAdminExt = (DefaultMQAdminExt)mQAdminExtListIterator.next();
                    defaultMQAdminExt.shutdown();
                }

                MqConsumerServiceImpl.namesrvAddr2MqAdminExt.clear();
                return consumeConcurrentlyStatus2;
            }
        });
        mqPushConsumer3.start();
        logger.info("初始化" + ExceptionUtils.FLUSH_MQ_TOPIC + "成功");
    }
}

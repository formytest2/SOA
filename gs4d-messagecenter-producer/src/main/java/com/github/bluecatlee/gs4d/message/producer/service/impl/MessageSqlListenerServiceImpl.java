package com.github.bluecatlee.gs4d.message.producer.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerOrderly;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.consumer.ConsumeFromWhere;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.alibaba.rocketmq.common.protocol.heartbeat.MessageModel;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.github.bluecatlee.gs4d.common.utils.Md5Util;
import com.github.bluecatlee.gs4d.message.api.model.OrderFlowerModel;
import com.github.bluecatlee.gs4d.message.api.model.RocketMqFlushModel;
import com.github.bluecatlee.gs4d.message.api.request.OrderSendMessageConfirmRequest;
import com.github.bluecatlee.gs4d.message.api.response.OrderSendMessageConfirmResponse;
import com.github.bluecatlee.gs4d.message.api.service.MessageCenterSendService;
import com.github.bluecatlee.gs4d.message.api.utils.ExceptionUtils;
import com.github.bluecatlee.gs4d.message.producer.dao.PlatformMqTopicDao;
import com.github.bluecatlee.gs4d.message.producer.dao.SysOrderMessageSendLogDao;
import com.github.bluecatlee.gs4d.message.producer.dao.SysRocketMqSendLogDao;
import com.github.bluecatlee.gs4d.message.producer.model.PLATFORM_MQ_TOPIC;
import com.github.bluecatlee.gs4d.message.producer.model.SYS_ROCKET_MQ_SEND_LOG;
import com.github.bluecatlee.gs4d.message.producer.service.MessageSqlListenerService;
import com.github.bluecatlee.gs4d.message.producer.service.MqProductorBizInitService;
import com.github.bluecatlee.gs4d.message.producer.utils.TopicUtil;
import com.github.bluecatlee.gs4d.message.producer.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * rocketmq消息监听处理
 */
@Service("messageSqlListenerService")
public class MessageSqlListenerServiceImpl implements MessageSqlListenerService {

    private static Logger logger = LoggerFactory.getLogger(MessageSqlListenerServiceImpl.class);
    protected static Logger messSaveFailedLogger = LoggerFactory.getLogger("messSaveFailedLog");
    
    @Value("#{settings['mqNameServAddrMessOnly']}")
    private String MQ_NAME_SERV_ADDR_MESS_ONLY;
    
    @Autowired
    private PlatformTransactionManager platformTransactionManager;
    
    @Autowired
    private MessageCenterSendService messageCenterSendService;
    
    @Autowired
    public SysRocketMqSendLogDao sysRocketMqSendLogDao;
    
    @Autowired
    public SysOrderMessageSendLogDao sysOrderMessageSendLogDao;
    
    @Autowired
    private PlatformMqTopicDao platformMqTopicDao;
    
    @Autowired
    private MqProductorBizInitService mqProductorBizInitService;

    public void initMqSqlConsumer() throws MQClientException {
        TopicUtil.initTopic(Constants.MESS_SQL, this.MQ_NAME_SERV_ADDR_MESS_ONLY);
        DefaultMQPushConsumer consumer1 = new DefaultMQPushConsumer(Constants.MESS_SQL + "-" + Constants.MESS_UPDATE_SEND_SQL);
        consumer1.setNamesrvAddr(this.MQ_NAME_SERV_ADDR_MESS_ONLY);
        consumer1.setInstanceName((new Date()).getTime() + Md5Util.md5(UUID.randomUUID().toString()));
        consumer1.subscribe(Constants.MESS_SQL, Constants.MESS_UPDATE_SEND_SQL);
        consumer1.setConsumeThreadMin(50);
        consumer1.setConsumeThreadMax(51);
        consumer1.setPullBatchSize(1);
        consumer1.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
        consumer1.setMessageListener(new MessageListenerOrderly() {
            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> messageExts, ConsumeOrderlyContext consumeOrderlyContext) {
                ConsumeOrderlyStatus consumeOrderlyStatus = ConsumeOrderlyStatus.SUCCESS;
                MessageExt messageExt = (MessageExt)messageExts.get(0);
                String body = new String(messageExt.getBody());
                SYS_ROCKET_MQ_SEND_LOG sysRocketMqSendLog = (SYS_ROCKET_MQ_SEND_LOG)JSONObject.parseObject(body, SYS_ROCKET_MQ_SEND_LOG.class);

                try {
                    MessageSqlListenerServiceImpl.this.sysRocketMqSendLogDao.updateMsgIdAndMsgStatusSqlAndMessFlag(sysRocketMqSendLog);
                } catch (Exception e) {
                    consumeOrderlyStatus = ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
                    MessageSqlListenerServiceImpl.logger.error(e.getMessage());
                }

                return consumeOrderlyStatus;
            }
        });
        consumer1.start();
        logger.info("初始化"+Constants.MESS_UPDATE_SEND_SQL+"成功");

        TopicUtil.initTopic(Constants.FLOWER_TOPIC, this.MQ_NAME_SERV_ADDR_MESS_ONLY);
        DefaultMQPushConsumer consumer2 = new DefaultMQPushConsumer(Constants.FLOWER_TOPIC + "-" + Constants.ORDER_FLOWER_TAG);
        consumer2.setNamesrvAddr(this.MQ_NAME_SERV_ADDR_MESS_ONLY);
        consumer2.setInstanceName((new Date()).getTime() + Md5Util.md5(UUID.randomUUID().toString()));
        consumer2.subscribe(Constants.FLOWER_TOPIC, Constants.ORDER_FLOWER_TAG);
        consumer2.setConsumeThreadMin(50);
        consumer2.setConsumeThreadMax(51);
        consumer2.setPullBatchSize(1);
        consumer2.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
        consumer2.setMessageListener(new MessageListenerConcurrently() {
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> messageExts, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                ConsumeConcurrentlyStatus consumeConcurrentlyStatus = ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                MessageExt messageExt = (MessageExt)messageExts.get(0);
                String body = new String(messageExt.getBody());
                OrderFlowerModel orderFlowerModel = (OrderFlowerModel)JSONObject.parseObject(body, OrderFlowerModel.class);

                try {
                    Long workflowId = orderFlowerModel.getWorkflowId();
                    OrderSendMessageConfirmRequest orderSendMessageConfirmRequest = new OrderSendMessageConfirmRequest();
                    orderSendMessageConfirmRequest.setOrderMessageGroupId(workflowId);
                    OrderSendMessageConfirmResponse orderSendMessageConfirmResponse = MessageSqlListenerServiceImpl.this.messageCenterSendService.confirmOrderSendMessage(orderSendMessageConfirmRequest);
                    if (orderSendMessageConfirmResponse.getCode() != 0L) {
                        throw new RuntimeException(orderSendMessageConfirmResponse.getMessage());
                    }
                } catch (Exception e) {
                    consumeConcurrentlyContext.setDelayLevelWhenNextConsume(3);
                    consumeConcurrentlyStatus = ConsumeConcurrentlyStatus.RECONSUME_LATER;
                    MessageSqlListenerServiceImpl.logger.error(e.getMessage());
                }

                return consumeConcurrentlyStatus;
            }
        });
        consumer2.start();
        logger.info("初始化"+Constants.FLOWER_TOPIC+"成功");

        TopicUtil.initTopic(ExceptionUtils.FLUSH_MQ_TOPIC, this.MQ_NAME_SERV_ADDR_MESS_ONLY);
        DefaultMQPushConsumer consumer3 = new DefaultMQPushConsumer(ExceptionUtils.FLUSH_MQ_TOPIC);
        consumer3.setNamesrvAddr(this.MQ_NAME_SERV_ADDR_MESS_ONLY);
        consumer3.setInstanceName((new Date()).getTime() + Md5Util.md5(UUID.randomUUID().toString()));
        consumer3.subscribe(ExceptionUtils.FLUSH_MQ_TOPIC, "*");
        consumer3.setMessageModel(MessageModel.BROADCASTING);
        consumer3.setConsumeThreadMin(1);
        consumer3.setConsumeThreadMax(3);
        consumer3.setPullBatchSize(1);
        consumer3.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
        consumer3.setMessageListener(new MessageListenerConcurrently() {
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> messageExts, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                ConsumeConcurrentlyStatus consumeConcurrentlyStatus = ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                MessageExt messageExt = (MessageExt)messageExts.get(0);
                String body = new String(messageExt.getBody());
                RocketMqFlushModel rocketMqFlushModel = (RocketMqFlushModel)JSONObject.parseObject(body, RocketMqFlushModel.class);

                try {
                    List platformMqTopics = MessageSqlListenerServiceImpl.this.platformMqTopicDao.queryBySeries(rocketMqFlushModel.getSeries());
                    if (platformMqTopics.size() == 0) {
                        MessageSqlListenerServiceImpl.logger.error("刷新series为" + rocketMqFlushModel.getSeries() + "的topic时,没有找到原始topic。");
                        consumeConcurrentlyStatus = ConsumeConcurrentlyStatus.RECONSUME_LATER;
                        return consumeConcurrentlyStatus;
                    }

                    PLATFORM_MQ_TOPIC platformMqTopic = (PLATFORM_MQ_TOPIC)platformMqTopics.get(0);
                    if (rocketMqFlushModel.getFlushFlag().equals("N")) {
                        MessageSqlListenerServiceImpl.this.mqProductorBizInitService.initalProductorBiz(platformMqTopic);
                    } else {
                        Constants.notDistinctTopicTagList.remove(platformMqTopic.getTOPIC() + "#" + platformMqTopic.getTAG());
                        Constants.distinctTopicTagList.remove(platformMqTopic.getTOPIC() + "#" + platformMqTopic.getTAG());
                        Constants.orderTopicTagList.remove(platformMqTopic.getTOPIC() + "#" + platformMqTopic.getTAG());
                        Constants.notInsertDbTopicTagList.remove(platformMqTopic.getTOPIC() + "#" + platformMqTopic.getTAG());
                        Constants.topicTagToConsumerInterval.remove(platformMqTopic.getTOPIC() + "#" + platformMqTopic.getTAG());
                    }
                } catch (Exception e) {
                    MessageSqlListenerServiceImpl.logger.error("刷新topic发生异常，行号" + rocketMqFlushModel.getSeries() + "原因" + e.getMessage());
                    consumeConcurrentlyStatus = ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }

                return consumeConcurrentlyStatus;
            }
        });
        consumer3.start();
        logger.info("初始化" + ExceptionUtils.FLUSH_MQ_TOPIC + "成功");
    }
}


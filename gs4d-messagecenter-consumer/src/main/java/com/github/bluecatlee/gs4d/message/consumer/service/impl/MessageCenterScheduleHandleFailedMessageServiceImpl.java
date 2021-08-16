package com.github.bluecatlee.gs4d.message.consumer.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.alibaba.rocketmq.common.message.MessageQueue;
import com.github.bluecatlee.gs4d.common.bean.MessagePack;
import com.github.bluecatlee.gs4d.common.exception.DatabaseOperateException;
import com.github.bluecatlee.gs4d.common.utils.BatchUtil;
import com.github.bluecatlee.gs4d.common.utils.StringUtil;
import com.github.bluecatlee.gs4d.message.api.request.*;
import com.github.bluecatlee.gs4d.message.api.response.ConsumerFailedMessageRetryResponse;
import com.github.bluecatlee.gs4d.message.api.response.ConsummerFailedMessageDeleteResponse;
import com.github.bluecatlee.gs4d.message.api.response.ConsummerFailedMessageHandleResponse;
import com.github.bluecatlee.gs4d.message.api.response.FailedMessageToSuccessDbInsertResponse;
import com.github.bluecatlee.gs4d.message.api.service.MessageCenterConsumerScheduleHandleFailedMessageService;
import com.github.bluecatlee.gs4d.message.api.service.MessageCenterSendService;
import com.github.bluecatlee.gs4d.message.api.utils.ExceptionUtils;
import com.github.bluecatlee.gs4d.message.consumer.dao.*;
import com.github.bluecatlee.gs4d.message.consumer.model.*;
import com.github.bluecatlee.gs4d.message.consumer.service.UpdateConsumerResultService;
import com.github.bluecatlee.gs4d.message.consumer.utils.AbstractRocketMqUtil;
import com.github.bluecatlee.gs4d.message.consumer.utils.Constants;
import com.github.bluecatlee.gs4d.message.consumer.utils.TransactionUtil;
import com.github.bluecatlee.gs4d.monitor.api.request.MessageCenterErrorLogDealRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;

import java.text.SimpleDateFormat;
import java.util.*;

@Service("scheduleHandleFailed")
public class MessageCenterScheduleHandleFailedMessageServiceImpl implements MessageCenterConsumerScheduleHandleFailedMessageService {

    private static Logger logger = LoggerFactory.getLogger(MessageCenterScheduleHandleFailedMessageServiceImpl.class);
    
    private Integer maxRows = 5000;

    @Autowired
    private SysRocketMqConsumeFailedlogDao sysRocketMqConsumeFailedlogDao;

    @Autowired
    private SysRocketMqSendLogDao sysRocketMqSendLogDao;

    @Value("${noConsumerTag}")
    private String noConsumerTag;

    @Autowired
    private SysRocketMqSendLogHistoryDao sysRocketMqSendLogHistoryDao;

    @Autowired
    private MessageCenterSendService messageSendService;

    @Value("#{settings['dataSign']}")
    private Integer dataSign;
    @Value("#{settings['consumerFiledToopic']}")
    private String consumerFiledToopic;
    @Value("#{settings['consumerFiledTag']}")
    private String consumerFiledTag;
    @Value("#{settings['systemId']}")
    private Long systemId;
    @Value("#{settings['consumerFiledToopic']}")
    private String consumerFiledTopic;
    @Value("#{settings['deleteEncryTime']}")
    private Long deleteEncryTime;

    @Autowired
    private PlatformMqTopicDao platformMqTopicDao;

    @Autowired
    private PlatformMqDubboConsumerDao platformMqDubboConsumerDao;

    @Autowired
    private PlatformMqHttpConsumerDao platformMqHttpConsumerDao;

    @Autowired
    SysRocketMqConsumeLogDao sysRocketMqConsumeLogDao;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    @Value("#{settings['emailAddress']}")
    private String emailAddress;

    @Autowired
    private UpdateConsumerResultService updateConsumerResultService;

    @Autowired
    JdbcTemplate jdbcTemplate;

    public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static int cP = 1200;

    public ConsummerFailedMessageHandleResponse handleConsummerFailedMessage(ConsummerFailedMessageHandleRequest consummerFailedMessageHandleRequest) {
        ConsummerFailedMessageHandleResponse consummerFailedMessageHandleResponse = new ConsummerFailedMessageHandleResponse();

        try {
            List<SysRocketMqConsumeFailedlog> sysRocketMqConsumeFailedlogList = this.sysRocketMqConsumeFailedlogDao.queryUnDeal(this.maxRows, consummerFailedMessageHandleRequest.getTenantNumId(), consummerFailedMessageHandleRequest.getDataSign());
            if (sysRocketMqConsumeFailedlogList == null || sysRocketMqConsumeFailedlogList.isEmpty()) {
                logger.debug("本次处理消费失败，消费延时消息没有获取到数据，直接返回。");
                return consummerFailedMessageHandleResponse;
            }

            for(int i = 0; i < sysRocketMqConsumeFailedlogList.size(); ++i) {
                SysRocketMqConsumeFailedlog sysRocketMqConsumeFailedlog = (SysRocketMqConsumeFailedlog)sysRocketMqConsumeFailedlogList.get(i);
                Long typeId = sysRocketMqConsumeFailedlog.getTYPE_ID();
                SysRocketMqSendLog sysRocketMqSendLog = null;
                String responseBody = null;

                try {
                    if (typeId.equals(2L)) {
                        typeId = 1L;

                        try {
                            sysRocketMqSendLog = this.sysRocketMqSendLogHistoryDao.queryBySeriesStrict(sysRocketMqConsumeFailedlog.getMSG_SERIES());
                        } catch (DatabaseOperateException e) {
                            logger.warn("补偿机制获取实体失败,序列号" + sysRocketMqConsumeFailedlog.getMSG_SERIES(), e);
                            continue;
                        }

                        responseBody = "编号为:" + sysRocketMqSendLog.getMESSAGE_TAG() + "的主题消息消费超时，有问题的series" + sysRocketMqSendLog.getSERIES() + "有问题的消息业务号为" + sysRocketMqSendLog.getMESSAGE_KEY() + "错误异常" + sysRocketMqSendLog.getRESPONSE_DETAIL();
                    } else {
                        try {
                            sysRocketMqSendLog = this.sysRocketMqSendLogDao.queryBySeriesStrict(sysRocketMqConsumeFailedlog.getMSG_SERIES());
                        } catch (DatabaseOperateException e) {
                            logger.warn("补偿机制获取实体失败,序列号" + sysRocketMqConsumeFailedlog.getMSG_SERIES(), e);
                            continue;
                        }
                    }

                    if (sysRocketMqSendLog != null) {
                        if (typeId.equals(1L)) {
                            responseBody = "编号为:" + sysRocketMqSendLog.getMESSAGE_TAG() + "的主题消息重试次数过多，有问题的series" + sysRocketMqSendLog.getSERIES() + "有问题的消息业务号为" + sysRocketMqSendLog.getMESSAGE_KEY() + "错误异常" + sysRocketMqSendLog.getRESPONSE_DETAIL();
                        } else if (typeId.equals(6L)) {
                            typeId = 1L;
                            responseBody = "编号为:" + sysRocketMqSendLog.getMESSAGE_TAG() + "的主题定时器消费全部失败，请尽快处理，有问题的series" + sysRocketMqSendLog.getSERIES() + "有问题的消息业务号为" + sysRocketMqSendLog.getMESSAGE_KEY() + "错误异常" + sysRocketMqSendLog.getRESPONSE_DETAIL();
                        } else {
                            typeId = 1L;
                        }

                        String responseDetail = sysRocketMqSendLog.getRESPONSE_DETAIL();
                        this.sendMonitorLog(sysRocketMqSendLog.getMESSAGE_TOPIC(), sysRocketMqSendLog.getMESSAGE_TAG(), sysRocketMqSendLog.getTENANT_NUM_ID(), sysRocketMqSendLog.getDATA_SIGN(), typeId, responseDetail, responseBody);
                    }
                } catch (Exception e) {
                    logger.error("处理消费失败消息异常：序列" + sysRocketMqConsumeFailedlog.getMSG_SERIES() + "原因" + e.getMessage(), e);
                }
            }

            this.sysRocketMqConsumeFailedlogDao.batchUpdate(sysRocketMqConsumeFailedlogList);
        } catch (Exception e) {
            ExceptionUtils.processException(e, consummerFailedMessageHandleResponse);
            logger.error("消息中心查询消费者消费问题时失败，原因" + e.getMessage(), e);
        }

        return consummerFailedMessageHandleResponse;
    }

    public void sendMonitorLog(String topic, String tag, Long tenantNumId, Long dataSign, Long typeId, String responseDetail, String responseBody) {
        MessageCenterErrorLogDealRequest messageCenterErrorLogDealRequest = new MessageCenterErrorLogDealRequest();
        PlatformMqTopic platformMqTopic = (PlatformMqTopic) Constants.platformMqTopicTable.get(Constants.getTopicKey(topic, tag, tenantNumId.toString(), dataSign.toString()));
        if (platformMqTopic == null) {
            List<PlatformMqTopic> platformMqTopicList = this.platformMqTopicDao.queryByTopicTag(topic, tag, tenantNumId.toString());
            if (platformMqTopicList == null || platformMqTopicList.isEmpty()) {
                logger.error("发现了topic表里面没有的消息tag，请去检查" + tag);
                return;
            }

            platformMqTopic = (PlatformMqTopic)platformMqTopicList.get(0);
            Constants.platformMqTopicTable.put(Constants.getTopicKey(topic, tag, tenantNumId.toString(), dataSign.toString()), platformMqTopic);
        }

        messageCenterErrorLogDealRequest.setSystemId(platformMqTopic.getSYSTEM_NUM_ID());
        messageCenterErrorLogDealRequest.setErrorCode(Constants.getErrorCode(platformMqTopic.getSERIES().toString(), typeId.toString()));
        messageCenterErrorLogDealRequest.setDataSign(dataSign);
        messageCenterErrorLogDealRequest.setResponseBody(responseBody);
        messageCenterErrorLogDealRequest.setTopicName(topic);
        messageCenterErrorLogDealRequest.setSystemName(Constants.SUB_SYSTEM + "#" + platformMqTopic.getFROM_SYSTEM());
        messageCenterErrorLogDealRequest.setRequestParam(platformMqTopic.getREMARK() + responseDetail);
        messageCenterErrorLogDealRequest.setTenantNumId(tenantNumId);
        SendMessRightNowRequest sendMessRightNowRequest = new SendMessRightNowRequest();
        sendMessRightNowRequest.setBody(messageCenterErrorLogDealRequest);
        sendMessRightNowRequest.setDataSign(dataSign);
        sendMessRightNowRequest.setFromSystem(Constants.fromSystem);
        sendMessRightNowRequest.setMsgKey(platformMqTopic.getSERIES().toString());
        sendMessRightNowRequest.setTopic(this.consumerFiledToopic);
        sendMessRightNowRequest.setTag(this.consumerFiledTag);
        sendMessRightNowRequest.setTenantNumId(tenantNumId);
        this.messageSendService.sendMessRightNow(sendMessRightNowRequest);
    }

    public ConsumerFailedMessageRetryResponse retryConsumerFailedMessage(final ConsumerFailedMessageRetryRequest consumerFailedMessageRetryRequest) {
        ConsumerFailedMessageRetryResponse consumerFailedMessageRetryResponse = new ConsumerFailedMessageRetryResponse();
        logger.debug("消息中心定时器处理消费失败消息开始执行");

        try {
            List<PlatformMqTopic> needHandleFailedmessList = this.platformMqTopicDao.queryAllTenantNeedHandleFailedmess(consumerFailedMessageRetryRequest.getDataSign());
            Iterator iterator = needHandleFailedmessList.iterator();

            while(iterator.hasNext()) {
                final PlatformMqTopic platformMqTopic = (PlatformMqTopic)iterator.next();
                if (1 == platformMqTopic.getCONSUMER_TYPE()) {
                    final PlatformMqDubboConsumer platformMqDubboConsumer = this.platformMqDubboConsumerDao.queryBySeries(platformMqTopic.getCONSUMER_SERIES());
                    if (platformMqDubboConsumer == null) {
                        logger.error("PLATFORM_MQ_TOPIC对应的行号" + platformMqTopic.getSERIES() + "配置的dubbo消费者记录不存在");
                    } else {
                        AbstractRocketMqUtil.executorService.submit(new Runnable() {
                            public void run() {
                                MessagePack messagePack = new MessagePack();

                                try {
                                    List<SysRocketMqSendLog> consumeFailedList = MessageCenterScheduleHandleFailedMessageServiceImpl.this.sysRocketMqSendLogDao.queryConsumeFailedByRetrytimes(platformMqTopic.getTOPIC(), platformMqTopic.getTAG(), consumerFailedMessageRetryRequest.getTenantNumId(), consumerFailedMessageRetryRequest.getDataSign(), platformMqTopic.getRETRIES(), platformMqTopic.getRETRY_MAX());

                                    for(int i = 0; i < consumeFailedList.size(); ++i) {
                                        MessageListenerConcurrentlyImpl4DubboConsumer messageListenerConcurrentlyImpl4DubboConsumer = new MessageListenerConcurrentlyImpl4DubboConsumer(platformMqTopic, platformMqDubboConsumer, MessageCenterScheduleHandleFailedMessageServiceImpl.this.dataSign, platformMqTopic.getRETRIES(), platformMqTopic.getZK_DATA_SIGN(), MessageCenterScheduleHandleFailedMessageServiceImpl.this.updateConsumerResultService, MessageCenterScheduleHandleFailedMessageServiceImpl.this.noConsumerTag);
                                        ((SysRocketMqSendLog)consumeFailedList.get(i)).setJOB_SEND_EMAIL("Y");
                                        ConsumeConcurrentlyContext consumeConcurrentlyContext = new ConsumeConcurrentlyContext((MessageQueue)null);
                                        List<MessageExt> msgExtList = new ArrayList();
                                        MessageExt messageExt = new MessageExt();
                                        messageExt.setBody(JSONObject.toJSONString(consumeFailedList.get(i)).getBytes());
                                        msgExtList.add(messageExt);
                                        messageListenerConcurrentlyImpl4DubboConsumer.consumeMessage(msgExtList, consumeConcurrentlyContext);
                                        if (((SysRocketMqSendLog)consumeFailedList.get(i)).getRETRY_TIMES() == platformMqTopic.getRETRY_MAX() - 1) {
                                            MessageCenterScheduleHandleFailedMessageServiceImpl.this.sysRocketMqConsumeFailedlogDao.insert(((SysRocketMqSendLog)consumeFailedList.get(i)).getSERIES(), 6L, consumerFailedMessageRetryRequest.getTenantNumId(), consumerFailedMessageRetryRequest.getDataSign());
                                        }
                                    }
                                } catch (Exception e) {
                                    ExceptionUtils.processException(e, messagePack);
                                    MessageCenterScheduleHandleFailedMessageServiceImpl.logger.error("消息中心定时器处理消费失败消息失败：" + e.getMessage(), e);
                                }

                            }
                        });
                    }
                } else if (2 == platformMqTopic.getCONSUMER_TYPE()) {
                    final PlatformMqHttpConsumer platformMqHttpConsumer = this.platformMqHttpConsumerDao.queryBySeries(platformMqTopic.getCONSUMER_SERIES());
                    if (platformMqHttpConsumer == null) {
                        logger.error("PLATFORM_MQ_TOPIC对应的行号" + platformMqTopic.getSERIES() + "配置的http消费者记录不存在");
                    } else {
                        AbstractRocketMqUtil.executorService.execute(new Runnable() {
                            public void run() {
                                MessagePack messagePack = new MessagePack();

                                try {
                                    List<SysRocketMqSendLog> consumeFailedList = MessageCenterScheduleHandleFailedMessageServiceImpl.this.sysRocketMqSendLogDao.queryConsumeFailedByRetrytimes(platformMqTopic.getTOPIC(), platformMqTopic.getTAG(), consumerFailedMessageRetryRequest.getTenantNumId(), consumerFailedMessageRetryRequest.getDataSign(), platformMqTopic.getRETRIES(), platformMqTopic.getRETRY_MAX());

                                    for(int i = 0; i < consumeFailedList.size(); ++i) {
                                        ((SysRocketMqSendLog)consumeFailedList.get(i)).setJOB_SEND_EMAIL("Y");
                                        MessageListenerConcurrentlyImpl4HttpConsumer messageListenerConcurrentlyImpl4HttpConsumer = new MessageListenerConcurrentlyImpl4HttpConsumer(platformMqTopic, platformMqHttpConsumer, MessageCenterScheduleHandleFailedMessageServiceImpl.this.dataSign, platformMqTopic.getRETRIES(), platformMqTopic.getZK_DATA_SIGN());
                                        ConsumeConcurrentlyContext consumeConcurrentlyContext = new ConsumeConcurrentlyContext((MessageQueue)null);
                                        List<MessageExt> msgExtList = new ArrayList();
                                        MessageExt messageExt = new MessageExt();
                                        messageExt.setBody(JSONObject.toJSONString(consumeFailedList.get(i)).getBytes());
                                        msgExtList.add(messageExt);
                                        messageListenerConcurrentlyImpl4HttpConsumer.consumeMessage(msgExtList, consumeConcurrentlyContext);
                                        if (((SysRocketMqSendLog)consumeFailedList.get(i)).getRETRY_TIMES() == platformMqTopic.getRETRY_MAX() - 1) {
                                            MessageCenterScheduleHandleFailedMessageServiceImpl.this.sysRocketMqConsumeFailedlogDao.insert(((SysRocketMqSendLog)consumeFailedList.get(i)).getSERIES(), 6L, consumerFailedMessageRetryRequest.getTenantNumId(), consumerFailedMessageRetryRequest.getDataSign());
                                        }
                                    }
                                } catch (Exception e) {
                                    ExceptionUtils.processException(e, messagePack);
                                    MessageCenterScheduleHandleFailedMessageServiceImpl.logger.error("消息中心定时器处理消费失败消息失败：" + e.getMessage(), e);
                                }

                            }
                        });
                    }
                }
            }
        } catch (Exception e) {
            ExceptionUtils.processException(e, consumerFailedMessageRetryResponse);
            logger.error("消息中心定时器处理消费失败消息失败：" + e.getMessage(), e);
        }

        return consumerFailedMessageRetryResponse;
    }

    public FailedMessageToSuccessDbInsertResponse insertFailedMessageToSuccessDb(FailedMessageToSuccessDbRequest failedMessageToSuccessDbRequest) {
        FailedMessageToSuccessDbInsertResponse failedMessageToSuccessDbInsertResponse = new FailedMessageToSuccessDbInsertResponse();

        try {
            Long batchSize = 1000L;
            String startTime = null;
            String endTime = null;
            if (StringUtil.isAllNullOrBlank(new String[]{failedMessageToSuccessDbRequest.getMessageCreateDtme()})) {
                startTime = this.getStartTimeOfDay(new Date());
                endTime = this.getEndTimeOfDay(new Date());
            } else {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date messageCreateDtme = simpleDateFormat.parse(failedMessageToSuccessDbRequest.getMessageCreateDtme());
                startTime = this.getStartTimeOfDay(messageCreateDtme);
                endTime = this.getEndTimeOfDay(messageCreateDtme);
            }

            Long count = this.sysRocketMqSendLogDao.queryByTimeRange(startTime, endTime, failedMessageToSuccessDbRequest.getTenantNumId(), failedMessageToSuccessDbRequest.getDataSign());
            Long batch = BatchUtil.batch(count, batchSize); // 批数

            for(int i = 0; (long)i < batch; ++i) {
                List<SysRocketMqSendLog> sysRocketMqSendLogList = this.sysRocketMqSendLogDao.queryByTimeRange(startTime, endTime, i, batchSize, failedMessageToSuccessDbRequest.getTenantNumId(), failedMessageToSuccessDbRequest.getDataSign());

                for(int j = 0; j < sysRocketMqSendLogList.size(); ++j) {
                    List<SysRocketMqSendLog> sysRocketMqSendLogs = this.sysRocketMqSendLogHistoryDao.queryBySeries(((SysRocketMqSendLog)sysRocketMqSendLogList.get(j)).getSERIES());
                    if (sysRocketMqSendLogs != null && !sysRocketMqSendLogs.isEmpty()) {
                        TransactionStatus transactionStatus = this.platformTransactionManager.getTransaction(TransactionUtil.newTransactionDefinition(10));

                        try {
                            this.sysRocketMqSendLogHistoryDao.delete(((SysRocketMqSendLog)sysRocketMqSendLogList.get(j)).getSERIES());
                            ((SysRocketMqSendLog)sysRocketMqSendLogs.get(0)).setSTEP_ID(7);
                            this.sysRocketMqSendLogHistoryDao.insert((SysRocketMqSendLog)sysRocketMqSendLogs.get(0));
                            this.sysRocketMqSendLogDao.delete(((SysRocketMqSendLog)sysRocketMqSendLogList.get(j)).getSERIES());
                            this.platformTransactionManager.commit(transactionStatus);
                        } catch (Exception e) {
                            this.platformTransactionManager.rollback(transactionStatus);
                        }
                    }
                }
            }
        } catch (Exception e) {
            ExceptionUtils.processException(e, failedMessageToSuccessDbInsertResponse);
        }

        return failedMessageToSuccessDbInsertResponse;
    }

    private String getStartTimeOfDay(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date newDate = calendar.getTime();
        return simpleDateFormat.format(newDate);
    }

    private String getEndTimeOfDay(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        Date newDate = calendar.getTime();
        return dateFormat.format(newDate);
    }

    public ConsummerFailedMessageDeleteResponse deleteConsummerFailedMessage(ConsummerFailedMessageDeleteRequest consummerFailedMessageDeleteRequest) {
        ConsummerFailedMessageDeleteResponse consummerFailedMessageDeleteResponse = new ConsummerFailedMessageDeleteResponse();

        try {
            this.sysRocketMqConsumeFailedlogDao.delete(consummerFailedMessageDeleteRequest.getTenantNumId(), consummerFailedMessageDeleteRequest.getDataSign());
        } catch (Exception e) {
            ExceptionUtils.processException(e, consummerFailedMessageDeleteResponse);
            logger.error("消息中心删除已经处理的消息失败：" + e.getMessage(), e);
        }

        return consummerFailedMessageDeleteResponse;
    }
}


package com.github.bluecatlee.gs4d.message.producer.service.impl;

import com.alibaba.rocketmq.client.QueryResult;
import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.admin.ConsumeStats;
import com.alibaba.rocketmq.common.admin.OffsetWrapper;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.alibaba.rocketmq.common.message.MessageQueue;
import com.alibaba.rocketmq.remoting.exception.RemotingException;
import com.alibaba.rocketmq.tools.admin.DefaultMQAdminExt;
import com.github.bluecatlee.gs4d.common.bean.MessagePack;
import com.github.bluecatlee.gs4d.common.exception.ExceptionType;
import com.github.bluecatlee.gs4d.common.utils.JsonUtil;
import com.github.bluecatlee.gs4d.common.utils.StringUtil;
import com.github.bluecatlee.gs4d.export.api.request.MessageCommonRefoundRequest;
import com.github.bluecatlee.gs4d.export.api.response.MessageCommonRefoundResponse;
import com.github.bluecatlee.gs4d.export.api.service.ExportDataService;
import com.github.bluecatlee.gs4d.message.api.request.*;
import com.github.bluecatlee.gs4d.message.api.response.*;
import com.github.bluecatlee.gs4d.message.api.service.MessageCenterProductorScheduleHandleFailedMessageService;
import com.github.bluecatlee.gs4d.message.api.service.MessageCenterSendService;
import com.github.bluecatlee.gs4d.message.api.utils.ExceptionUtils;
import com.github.bluecatlee.gs4d.message.producer.dao.*;
import com.github.bluecatlee.gs4d.message.producer.model.PLATFORM_MQ_TOPIC;
import com.github.bluecatlee.gs4d.message.producer.model.REDIS_EXPIRED_KEY_TOPIC;
import com.github.bluecatlee.gs4d.message.producer.model.SYS_ORDER_MESSAGE_SEND_LOG;
import com.github.bluecatlee.gs4d.message.producer.model.SYS_ROCKET_MQ_SEND_LOG;
import com.github.bluecatlee.gs4d.message.producer.utils.AbstractRocketMqUtil;
import com.github.bluecatlee.gs4d.common.utils.BatchUtil;
import com.github.bluecatlee.gs4d.message.producer.utils.Constants;
import com.github.bluecatlee.gs4d.message.producer.utils.TransactionUtil;
import com.github.bluecatlee.gs4d.monitor.api.request.MessageCenterErrorLogDealRequest;
import com.github.bluecatlee.gs4d.transaction.api.request.MessagecenterRecallRequest;
import com.github.bluecatlee.gs4d.transaction.api.response.MessagecenterRecallResponse;
import com.github.bluecatlee.gs4d.transaction.api.service.MessagecenterRecallService;
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
import java.util.Map.Entry;

/**
 * ????????????????????????
 */
@Service("scheduleHandleFailed")
public class MessageCenterScheduleHandleFailedMessageServiceImpl implements MessageCenterProductorScheduleHandleFailedMessageService {

    private static Logger logger = LoggerFactory.getLogger(MessageCenterScheduleHandleFailedMessageServiceImpl.class);

    @Autowired
    private SysRocketMqSendLogDao sysRocketMqSendLogDao;

    @Autowired
    private SysRocketMqSendLogHistoryDao sysRocketMqSendLogHistoryDao;

    @Autowired
    private MessageCenterSendService messageCenterSendService;

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
    @Value("#{settings['monitorEmail']}")
    private String monitorEmail;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private PlatformMqTopicDao platformMqTopicDao;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    @Autowired
    private ExportDataService exportDataService;

    @Autowired
    private SysTransationFailedLogDao sysTransationFailedLogDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MessagecenterRecallService messagecenterRecallService;

    @Autowired
    private SysOrderMessageSendLogDao sysOrderMessageSendLogDao;

    @Autowired
    private RedisExpirekeyListenDao redisExpirekeyListenDao;

    @Value("${defaultNamesrv}")
    private String defaultNamesrv;

    public static int ca = 1200;

    /**
     * ?????????????????????????????????
     * @param noDealTransMessageFindRequest
     * @return
     */
    public NoDealTransMessageFindResponse findNoDealTransMessage(final NoDealTransMessageFindRequest noDealTransMessageFindRequest) {
        if (logger.isDebugEnabled()) {
            logger.debug("begin findNoDealTransMessage request:{}", JsonUtil.toJson(noDealTransMessageFindRequest));
        }

        NoDealTransMessageFindResponse noDealTransMessageFindResponse = new NoDealTransMessageFindResponse();

        try {
            List list = this.platformMqTopicDao.getDistinctSystemName();
            if (list != null && !list.isEmpty()) {
                Iterator iterator = list.iterator();

                while(iterator.hasNext()) {
                    final PLATFORM_MQ_TOPIC platformMqTopic = (PLATFORM_MQ_TOPIC)iterator.next();
                    AbstractRocketMqUtil.executorService.execute(new Runnable() {
                        public void run() {
                            try {
                                String topic = platformMqTopic.getTOPIC();
                                String tag = platformMqTopic.getTAG();
                                String datasourceName = platformMqTopic.getDATASOURCE_NAME();
                                // ???????????????????????????????????????
                                List sendLogs = MessageCenterScheduleHandleFailedMessageServiceImpl.this.sysRocketMqSendLogDao.queryUnHandledSendLogsByTopicAndTag(noDealTransMessageFindRequest.getDataSign(), topic, tag, noDealTransMessageFindRequest.getTenantNumId());
                                if (sendLogs == null || sendLogs.isEmpty()) {
                                    return;
                                }

                                logger.info("?????????????????????tag???" + tag + "??????????????????????????????" + sendLogs.size());
                                Iterator sendLogsIterator = sendLogs.iterator();

                                while(sendLogsIterator.hasNext()) {
                                    SYS_ROCKET_MQ_SEND_LOG sysRocketMqSendLog = (SYS_ROCKET_MQ_SEND_LOG)sendLogsIterator.next();

                                    try {
                                        ArrayList sysRocketMqSendLogList;
                                        if (sysRocketMqSendLog.getMSG_STATUS() == Constants.transaction_pre_send) {
                                            MessagecenterRecallRequest messagecenterRecallRequest = new MessagecenterRecallRequest();
                                            messagecenterRecallRequest.setTransactionId(sysRocketMqSendLog.getWORKFLOW_ID());
                                            MessagecenterRecallResponse messagecenterRecallResponse = MessageCenterScheduleHandleFailedMessageServiceImpl.this.messagecenterRecallService.recallMessagecenter(messagecenterRecallRequest);

                                            if (messagecenterRecallResponse.getCode() == MessagePack.OK && messagecenterRecallResponse.getState().equals(1L)) {
                                                // ??????transaction_log???transaction_state=1 ????????????????????????????????? ????????????????????????
                                                sysRocketMqSendLogList = new ArrayList();
                                                sysRocketMqSendLog.setSTEP_ID(Constants.ez);
                                                sysRocketMqSendLogList.add(sysRocketMqSendLog);
                                                AbstractRocketMqUtil.send(sysRocketMqSendLogList, MessageCenterScheduleHandleFailedMessageServiceImpl.this.stringRedisTemplate);
                                            } else if (messagecenterRecallResponse.getCode() == MessagePack.OK && messagecenterRecallResponse.getState().equals(2L)) {
                                                // ??????transaction_log???transaction_state=2 ??????????????????????????? ????????????????????????mq?????? ???????????????????????? ???????????????????????????
                                                sysRocketMqSendLogList = new ArrayList();
                                                sysRocketMqSendLogList.add(sysRocketMqSendLog);
                                                sysRocketMqSendLog.setSTEP_ID(Constants.eE);
                                                sysRocketMqSendLog.setMSG_STATUS(Constants.send_cancel);
                                                MessageCenterScheduleHandleFailedMessageServiceImpl.this.sysTransationFailedLogDao.insert(sysRocketMqSendLog);
                                                MessageCenterScheduleHandleFailedMessageServiceImpl.this.sysRocketMqSendLogDao.batchDelete(sysRocketMqSendLogList);
                                            }
                                        } else {
                                            MessageCommonRefoundRequest messageCommonRefoundRequest = new MessageCommonRefoundRequest();
                                            messageCommonRefoundRequest.setMsgSeries(sysRocketMqSendLog.getSERIES().toString());
                                            messageCommonRefoundRequest.setSysNumId(Long.valueOf(sysRocketMqSendLog.getFROM_SYSTEM()));
                                            messageCommonRefoundRequest.setDatasouceName(datasourceName);
                                            messageCommonRefoundRequest.setTenantNumId(sysRocketMqSendLog.getTENANT_NUM_ID());
                                            messageCommonRefoundRequest.setDataSign(sysRocketMqSendLog.getDATA_SIGN());
                                            // ??????series??????
                                            MessageCommonRefoundResponse messageCommonRefoundResponse = MessageCenterScheduleHandleFailedMessageServiceImpl.this.exportDataService.messageCommonRefound(messageCommonRefoundRequest);
                                            if (messageCommonRefoundResponse.getCode() == MessagePack.OK) {
                                                sysRocketMqSendLogList = new ArrayList();
                                                sysRocketMqSendLog.setSTEP_ID(Constants.eA);
                                                sysRocketMqSendLogList.add(sysRocketMqSendLog);
                                                AbstractRocketMqUtil.send(sysRocketMqSendLogList, MessageCenterScheduleHandleFailedMessageServiceImpl.this.stringRedisTemplate);    // ?????????????????????????????????????????????????????????
                                            } else if (messageCommonRefoundResponse.getCode() == ExceptionType.BE40160.getCode()) {
                                                MessageCenterScheduleHandleFailedMessageServiceImpl.this.sysRocketMqSendLogDao.updateInstanceIdBySeries(sysRocketMqSendLog.getSERIES(), -1L);
                                                logger.error("???????????????????????????????????????????????????,????????????:" + sysRocketMqSendLog.getSERIES());
                                            } else {
                                                MessageCenterScheduleHandleFailedMessageServiceImpl.this.sysRocketMqSendLogDao.updateInstanceIdBySeries(sysRocketMqSendLog.getSERIES(), -2L);
                                                logger.error("??????????????????????????????import????????????????????????:" + sysRocketMqSendLog.getFROM_SYSTEM() + "??????????????????,????????????:" + sysRocketMqSendLog.getSERIES());
                                            }
                                        }
                                    } catch (Throwable e) {
                                        logger.error("?????????????????????????????????:" + sysRocketMqSendLog.getSERIES() + ",??????:" + e.getMessage(), e);
                                    }
                                }
                            } catch (Exception e) {
                                logger.error("??????tag:" + platformMqTopic.getTAG() + "???????????????????????????:" + e.getMessage(), e);
                            }

                        }
                    });
                }
            }
        } catch (Throwable e) {
            ExceptionUtils.processException(e, noDealTransMessageFindResponse);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("end findNoDealTransMessage response:{}", noDealTransMessageFindResponse.toLowerCaseJson());
        }

        return noDealTransMessageFindResponse;
    }

    /**
     * ??????????????????????????????????????????
     * @param sendFailedMessageFindRequest
     * @return
     */
    public SendFailedMessageFindResponse findSendFailedMessage(SendFailedMessageFindRequest sendFailedMessageFindRequest) {
        if (logger.isDebugEnabled()) {
            logger.debug("begin findSendFailedMessage request:{}", JsonUtil.toJson(sendFailedMessageFindRequest));
        }

        SendFailedMessageFindResponse sendFailedMessageFindResponse = new SendFailedMessageFindResponse();

        try {
            Integer offset = 0;
            Integer count = 1000;
            List failedSendLogs = this.sysRocketMqSendLogDao.querySendFaildLogByPage(offset, count, sendFailedMessageFindRequest.getTenantNumId(), sendFailedMessageFindRequest.getDataSign());
            if (!failedSendLogs.isEmpty()) {
                Iterator iterator = failedSendLogs.iterator();

                while(iterator.hasNext()) {
                    SYS_ROCKET_MQ_SEND_LOG sysRocketMqSendLog = (SYS_ROCKET_MQ_SEND_LOG)iterator.next();
                    MsgRetrySendRequest msgRetrySendRequest = new MsgRetrySendRequest();
                    msgRetrySendRequest.setSeries(sysRocketMqSendLog.getSERIES());
                    msgRetrySendRequest.setDataSign(sysRocketMqSendLog.getDATA_SIGN());
                    msgRetrySendRequest.setTenantNumId(sysRocketMqSendLog.getTENANT_NUM_ID());
                    msgRetrySendRequest.setSendType(Constants.eC);
                    this.messageCenterSendService.retrySendMsg(msgRetrySendRequest);
                }
            }
        } catch (Throwable e) {
            ExceptionUtils.processException(e, sendFailedMessageFindResponse);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("end findSendFailedMessage response:{}", sendFailedMessageFindResponse.toLowerCaseJson());
        }

        return sendFailedMessageFindResponse;
    }

    /**
     * ???????????????(????????????)????????????????????????????????????????????????????????????
     * @param failedMessageToSuccessDbRequest
     * @return
     */
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
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = dateFormat.parse(failedMessageToSuccessDbRequest.getMessageCreateDtme());
                startTime = this.getStartTimeOfDay(date);
                endTime = this.getEndTimeOfDay(date);
            }

            Long totalCount = this.sysRocketMqSendLogDao.queryCountByStartEndTime(startTime, endTime, failedMessageToSuccessDbRequest.getTenantNumId(), failedMessageToSuccessDbRequest.getDataSign());
            Long batchCount = BatchUtil.batch(totalCount, batchSize);   // ?????????

            for(int i = 0; (long)i < batchCount; ++i) {
                List list = this.sysRocketMqSendLogDao.queryListByStartEndTime(startTime, endTime, i, batchSize, failedMessageToSuccessDbRequest.getTenantNumId(), failedMessageToSuccessDbRequest.getDataSign());

                for(int j = 0; j < list.size(); ++j) {
                    List historyList = this.sysRocketMqSendLogHistoryDao.queryBySeries(((SYS_ROCKET_MQ_SEND_LOG)list.get(j)).getSERIES());
                    if (historyList != null && !historyList.isEmpty()) {
                        TransactionStatus transactionStatus = this.platformTransactionManager.getTransaction(TransactionUtil.newTransactionDefinition(10));

                        try {
                            this.sysRocketMqSendLogHistoryDao.deleteBySeries(((SYS_ROCKET_MQ_SEND_LOG)list.get(j)).getSERIES());
                            this.sysRocketMqSendLogHistoryDao.insert((SYS_ROCKET_MQ_SEND_LOG)historyList.get(0));
                            this.sysRocketMqSendLogDao.deleteBySeries(((SYS_ROCKET_MQ_SEND_LOG)list.get(j)).getSERIES());
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
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date time = calendar.getTime();
        return dateFormat.format(time);
    }

    private String getEndTimeOfDay(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        Date time = calendar.getTime();
        return dateFormat.format(time);
    }

    /**
     * ?????????????????????????????????????????????????????????mq(??????????????????????????????????????? ?????????????????????)
     * @param unConsumerMessageRetrytRequest
     * @return
     */
    public UnConsumerMessageRetrytResponse retryUnConsumerMessage(UnConsumerMessageRetrytRequest unConsumerMessageRetrytRequest) {
        if (logger.isDebugEnabled()) {
            logger.debug("begin retryUnConsumerMessage request:{}", JsonUtil.toJson(unConsumerMessageRetrytRequest));
        }

        UnConsumerMessageRetrytResponse unConsumerMessageRetrytResponse = new UnConsumerMessageRetrytResponse();
        DefaultMQAdminExt mqAdminExt = null;

        try {
            mqAdminExt = new DefaultMQAdminExt();
            mqAdminExt.setNamesrvAddr(this.defaultNamesrv);
            mqAdminExt.setInstanceName(Long.toString(System.currentTimeMillis()));
            mqAdminExt.setHeartbeatBrokerInterval(30000);
            mqAdminExt.start();
            List topicList = this.platformMqTopicDao.getDistinctSystemName();

            for(int i = 0; i < topicList.size(); ++i) {
                List unConsumedSendLogs = this.sysRocketMqSendLogDao.queryUnConsumedSendLogs(((PLATFORM_MQ_TOPIC)topicList.get(i)).getTOPIC(), ((PLATFORM_MQ_TOPIC)topicList.get(i)).getTAG(), unConsumerMessageRetrytRequest.getTenantNumId(), unConsumerMessageRetrytRequest.getDataSign());

                for(int j = 0; j < unConsumedSendLogs.size(); ++j) {
                    String topicTag = ((SYS_ROCKET_MQ_SEND_LOG)unConsumedSendLogs.get(0)).getMESSAGE_TOPIC() + "-" + ((SYS_ROCKET_MQ_SEND_LOG)unConsumedSendLogs.get(0)).getMESSAGE_TAG();
                    if (j == 0) {
                        Boolean hasConsumer = this.hasConsumer(mqAdminExt, topicTag);
                        if (!hasConsumer) {
                            logger.error("??????tag:" + ((SYS_ROCKET_MQ_SEND_LOG)unConsumedSendLogs.get(0)).getMESSAGE_TAG() + ",??????????????????!");
                            break;
                        }

                        Long overstock = this.calcOverstock(mqAdminExt, topicTag);
                        if (overstock > 50L) {
                            logger.error("??????tag:" + ((SYS_ROCKET_MQ_SEND_LOG)unConsumedSendLogs.get(0)).getMESSAGE_TAG() + ",??????????????????!");
                            break;
                        }
                    }

                    MsgRetrySendRequest msgRetrySendRequest = new MsgRetrySendRequest();
                    msgRetrySendRequest.setDataSign(unConsumerMessageRetrytRequest.getDataSign());
                    msgRetrySendRequest.setTenantNumId(unConsumerMessageRetrytRequest.getTenantNumId());
                    msgRetrySendRequest.setSeries(((SYS_ROCKET_MQ_SEND_LOG)unConsumedSendLogs.get(j)).getSERIES());
                    msgRetrySendRequest.setSendType(Constants.eB);
                    this.messageCenterSendService.retrySendMsg(msgRetrySendRequest);
                    this.sysRocketMqSendLogDao.updateInstanceIdBySeries(((SYS_ROCKET_MQ_SEND_LOG)unConsumedSendLogs.get(j)).getSERIES(), -3L);
                }
            }
        } catch (Throwable e) {
            ExceptionUtils.processException(e, unConsumerMessageRetrytResponse);
        } finally {
            if (mqAdminExt != null) {
                mqAdminExt.shutdown();
            }

        }

        if (logger.isDebugEnabled()) {
            logger.debug("end retryUnConsumerMessage response:{}", unConsumerMessageRetrytResponse.toLowerCaseJson());
        }

        return unConsumerMessageRetrytResponse;
    }

    /**
     * ????????????????????????????????????????????????
     * @param mqAdminExt
     * @param consumerGroup
     * @return
     * @throws RemotingException
     * @throws MQClientException
     * @throws InterruptedException
     * @throws MQBrokerException
     */
    private Long calcOverstock(DefaultMQAdminExt mqAdminExt, String consumerGroup) throws RemotingException, MQClientException, InterruptedException, MQBrokerException {
        ConsumeStats consumeStats = mqAdminExt.examineConsumeStats(consumerGroup);
        LinkedList list = new LinkedList();
        list.addAll(consumeStats.getOffsetTable().keySet());
        Collections.sort(list);
        long overstock = 0L;

        long diff;
        for(Iterator iterator = list.iterator(); iterator.hasNext(); overstock += diff) {
            MessageQueue messageQueue = (MessageQueue)iterator.next();
            OffsetWrapper offsetWrapper = (OffsetWrapper)consumeStats.getOffsetTable().get(messageQueue);
            diff = offsetWrapper.getBrokerOffset() - offsetWrapper.getConsumerOffset();
        }

        return overstock;
    }

    /**
     * ??????topic????????????????????????
     * @param mqAdminExt
     * @param key
     * @param topic
     * @param group
     * @return
     * @throws MQClientException
     * @throws InterruptedException
     * @throws RemotingException
     * @throws MQBrokerException
     */
    private Boolean hasConsumed(DefaultMQAdminExt mqAdminExt, String key, String topic, String group) throws MQClientException, InterruptedException, RemotingException, MQBrokerException {
        Boolean flag = false;
        long end = System.currentTimeMillis();
        long begin = end - 259200000L;      // 259200??? = 3???

        try {
            QueryResult queryResult = mqAdminExt.queryMessage(topic, key, 32, begin, end);  // ????????????topic??????????????????
            List messageList = queryResult.getMessageList();

            for(int i = 0; i < messageList.size(); ++i) {
                Map consumerTable = mqAdminExt.getConsumeStatus(topic, group, "");
                Iterator iterator = consumerTable.entrySet().iterator();

                while(iterator.hasNext()) {
                    Entry messageQueueTableEntry = (Entry)iterator.next();
                    Iterator messageQueueTableIterator = ((Map)messageQueueTableEntry.getValue()).entrySet().iterator();

                    while(messageQueueTableIterator.hasNext()) {
                        Entry entry = (Entry)messageQueueTableIterator.next();
                        if (((MessageQueue)entry.getKey()).getQueueId() == ((MessageExt)messageList.get(i)).getQueueId() && (Long)entry.getValue() > ((MessageExt)messageList.get(i)).getQueueOffset()) {
                            flag = true;
                        }
                    }
                }
            }

            return flag;
        } catch (MQClientException e) {
            if (e.getMessage().contains("but no message")) { // todo ???code??????
                flag = true;
                return flag;
            } else {
                return flag;
            }
        }
    }

    /**
     * ????????????????????????????????????????????????????????????
     * @param mqAdminExt
     * @param consumerGroup
     * @return
     */
    private Boolean hasConsumer(DefaultMQAdminExt mqAdminExt, String consumerGroup) {
        Boolean flag = true;

        try {
            mqAdminExt.examineConsumerConnectionInfo(consumerGroup);
        } catch (Exception e) {
            flag = false;
        }

        return flag;
    }

    public void sendMonitorLog(Long tenantNumId, Long dataSign, Long var, String detailMessage, String message) {
    }

    private String getMessageSendExceptionEmailKey(Long series) {
        return "messageSendExceptionEmail_" + series;
    }

    public NoDealTransFlowerMessageFindResponse findNoDealTransFlowerMessage(NoDealTransFlowerMessageFindRequest noDealTransFlowerMessageFindRequest) {
        NoDealTransFlowerMessageFindResponse noDealTransFlowerMessageFindResponse = new NoDealTransFlowerMessageFindResponse();
        List noDealFlowMessages = this.sysOrderMessageSendLogDao.getNodealFlowMessage();
        if (noDealFlowMessages != null && !noDealFlowMessages.isEmpty()) {
            Iterator iterator = noDealFlowMessages.iterator();

            while(iterator.hasNext()) {
                SYS_ORDER_MESSAGE_SEND_LOG sysOrderMessageSendLog = (SYS_ORDER_MESSAGE_SEND_LOG)iterator.next();
                logger.info("???????????????????????????tag???" + sysOrderMessageSendLog.getMESSAGE_TAG() + "??????????????????????????????" + noDealFlowMessages.size());

                try {
                    MessageCommonRefoundRequest messageCommonRefoundRequest = new MessageCommonRefoundRequest();
                    messageCommonRefoundRequest.setMsgSeries(sysOrderMessageSendLog.getSERIES().toString());
                    messageCommonRefoundRequest.setSysNumId(Long.valueOf(sysOrderMessageSendLog.getFROM_SYSTEM()));
                    messageCommonRefoundRequest.setTenantNumId(sysOrderMessageSendLog.getTENANT_NUM_ID());
                    messageCommonRefoundRequest.setDataSign(sysOrderMessageSendLog.getDATA_SIGN());
                    MessageCommonRefoundResponse messageCommonRefoundResponse = this.exportDataService.messageCommonRefound(messageCommonRefoundRequest);// ????????????
                    if (messageCommonRefoundResponse.getCode() == MessagePack.OK) {
                        OrderSendMessageConfirmRequest orderSendMessageConfirmRequest = new OrderSendMessageConfirmRequest();
                        orderSendMessageConfirmRequest.setOrderMessageGroupId(sysOrderMessageSendLog.getORDER_MESSAGE_GROUP_ID());
                        this.messageCenterSendService.confirmOrderSendMessage(orderSendMessageConfirmRequest);
                    } else {
                        if (messageCommonRefoundResponse.getCode() != ExceptionType.BE40160.getCode()) {
                            throw new Exception("??????????????????????????????import???????????????????????????" + sysOrderMessageSendLog.getFROM_SYSTEM() + "?????????????????????");
                        }

                        OrderSendMessageCancelRequest orderSendMessageCancelRequest = new OrderSendMessageCancelRequest();
                        orderSendMessageCancelRequest.setOrderMessageGroupId(sysOrderMessageSendLog.getORDER_MESSAGE_GROUP_ID());
                        this.messageCenterSendService.cancelOrderSendMessage(orderSendMessageCancelRequest);
                    }
                } catch (Exception e) {
                    logger.error("???????????????????????????????????????ID?????????series???" + sysOrderMessageSendLog.getSERIES() + "?????????");
                    ExceptionUtils.processException(e, noDealTransFlowerMessageFindResponse);
                    String detailMessage = "??????????????????????????????????????????????????? " + sysOrderMessageSendLog.getSERIES() + "??????topic???" + sysOrderMessageSendLog.getMESSAGE_TOPIC();
                    String message = "???????????????????????????????????????";
                    this.sendMonitorLog(sysOrderMessageSendLog.getTENANT_NUM_ID(), sysOrderMessageSendLog.getDATA_SIGN(), Constants.dl, detailMessage, message);
                }
            }

            return noDealTransFlowerMessageFindResponse;
        } else {
            return noDealTransFlowerMessageFindResponse;
        }
    }

    public FlowerMessageDeleteResponse deleteFlowerMessage(FlowerMessageDeleteRequest flowerMessageDeleteRequest) {
        FlowerMessageDeleteResponse flowerMessageDeleteResponse = new FlowerMessageDeleteResponse();

        try {
            this.sysOrderMessageSendLogDao.deleteCanceledAndExpiredOrderMessageSendLogs();  // ????????????????????????????????????????????????????????????
        } catch (Exception e) {
            ExceptionUtils.processException(e, flowerMessageDeleteResponse);
        }

        return flowerMessageDeleteResponse;
    }

    public TransationFailedLogFindResponse findTransationFailedLog(TransationFailedLogFindRequest transationFailedLogFindRequest) {
        TransationFailedLogFindResponse transationFailedLogFindResponse = new TransationFailedLogFindResponse();

        try {
            Integer count = this.sysTransationFailedLogDao.queryByStepId(transationFailedLogFindRequest.getTenantNumId(), transationFailedLogFindRequest.getDataSign(), Constants.eD);
            if (count > 0) {
                this.sendMonitorLog(transationFailedLogFindRequest.getTenantNumId(), transationFailedLogFindRequest.getDataSign(), Constants.dj, "????????????????????????????????????" + count + "?????????????????????", "????????????????????????????????????" + count + "?????????????????????");
            }
        } catch (Exception e) {
            ExceptionUtils.processException(e, transationFailedLogFindResponse);
        }

        return transationFailedLogFindResponse;
    }

    public RedisDelayMessageListenCheckResponse checkRedisDelayMessageListen(RedisDelayMessageListenCheckRequest redisDelayMessageListenCheckRequest) {
        RedisDelayMessageListenCheckResponse redisDelayMessageListenCheckResponse = new RedisDelayMessageListenCheckResponse();

        try {
            List list = this.redisExpirekeyListenDao.getRedisKeyByDelay();

            REDIS_EXPIRED_KEY_TOPIC redisExpiredKeyTopic;
            for(Iterator iterator = list.iterator(); iterator.hasNext(); redisExpiredKeyTopic = (REDIS_EXPIRED_KEY_TOPIC)iterator.next()) {
            }
        } catch (Exception e) {
            ExceptionUtils.processException(e, redisDelayMessageListenCheckResponse);
        }

        return redisDelayMessageListenCheckResponse;
    }

    public void errorLogDeal(String topic, String tag, Long tenantNumId, Long dataSign, Long errorCode, String requestParam, String responseBody) {
        MessageCenterErrorLogDealRequest messageCenterErrorLogDealRequest = new MessageCenterErrorLogDealRequest();
        PLATFORM_MQ_TOPIC platformMqTopic = Constants.platformMqTopicTable.get(Constants.getTopicTagKey(topic, tag, tenantNumId.toString(), dataSign.toString()));
        if (platformMqTopic == null) {
            List platformMqTopicList = this.platformMqTopicDao.queryByTopicAndTag(topic, tag, tenantNumId.toString());
            if (platformMqTopicList == null || platformMqTopicList.isEmpty()) {
                logger.error("?????????topic????????????????????????tag???????????????" + tag);
                return;
            }

            platformMqTopic = (PLATFORM_MQ_TOPIC)platformMqTopicList.get(0);
            Constants.platformMqTopicTable.put(Constants.getTopicTagKey(topic, tag, tenantNumId.toString(), dataSign.toString()), platformMqTopic);
        }

        messageCenterErrorLogDealRequest.setSystemId(platformMqTopic.getSYSTEM_NUM_ID());
        messageCenterErrorLogDealRequest.setErrorCode(Constants.getErrorCode(platformMqTopic.getSERIES().toString(), errorCode.toString()));
        messageCenterErrorLogDealRequest.setDataSign(dataSign);
        messageCenterErrorLogDealRequest.setResponseBody(responseBody);
        messageCenterErrorLogDealRequest.setTopicName(topic);
        messageCenterErrorLogDealRequest.setSystemName(Constants.SUB_SYSTEM + "#" + platformMqTopic.getFROM_SYSTEM());
        messageCenterErrorLogDealRequest.setRequestParam(platformMqTopic.getREMARK() + requestParam);
        messageCenterErrorLogDealRequest.setTenantNumId(tenantNumId);

        SendMessRightNowRequest sendMessRightNowRequest = new SendMessRightNowRequest();
        sendMessRightNowRequest.setBody(messageCenterErrorLogDealRequest);
        sendMessRightNowRequest.setDataSign(dataSign);
        sendMessRightNowRequest.setFromSystem(Constants.systemId);
        sendMessRightNowRequest.setMsgKey(platformMqTopic.getSERIES().toString());
        sendMessRightNowRequest.setTopic(this.consumerFiledToopic);
        sendMessRightNowRequest.setTag(this.consumerFiledTag);
        sendMessRightNowRequest.setTenantNumId(tenantNumId);
        this.messageCenterSendService.sendMessRightNow(sendMessRightNowRequest);
    }
}


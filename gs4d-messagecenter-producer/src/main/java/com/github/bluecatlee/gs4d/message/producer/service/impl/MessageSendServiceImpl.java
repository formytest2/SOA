package com.github.bluecatlee.gs4d.message.producer.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.rocketmq.common.TopicConfig;
import com.alibaba.rocketmq.common.protocol.body.ClusterInfo;
import com.alibaba.rocketmq.tools.admin.DefaultMQAdminExt;
import com.alibaba.rocketmq.tools.command.CommandUtil;
import com.fasterxml.jackson.databind.PropertyNamingStrategy.LowerCaseStrategy;
import com.github.bluecatlee.gs4d.common.bean.MessagePack;
import com.github.bluecatlee.gs4d.common.exception.ExceptionType;
import com.github.bluecatlee.gs4d.common.exception.ValidateBusinessException;
import com.github.bluecatlee.gs4d.common.utils.*;
import com.github.bluecatlee.gs4d.export.api.request.MessageCommonRefoundRequest;
import com.github.bluecatlee.gs4d.export.api.response.MessageCommonRefoundResponse;
import com.github.bluecatlee.gs4d.export.api.service.ExportDataService;
import com.github.bluecatlee.gs4d.message.api.model.JobMessageModel;
import com.github.bluecatlee.gs4d.message.api.model.OrderMessageModel;
import com.github.bluecatlee.gs4d.message.api.model.SimpleMessage;
import com.github.bluecatlee.gs4d.message.api.model.SimpleObjectMessage;
import com.github.bluecatlee.gs4d.message.api.request.*;
import com.github.bluecatlee.gs4d.message.api.response.*;
import com.github.bluecatlee.gs4d.message.api.service.MessageCenterSendService;
import com.github.bluecatlee.gs4d.message.api.utils.ExceptionUtils;
import com.github.bluecatlee.gs4d.message.api.utils.MessageSendUtil;
import com.github.bluecatlee.gs4d.message.producer.dao.SysRocketMqJobLogDao;
import com.github.bluecatlee.gs4d.message.producer.dao.SysRocketMqSendLogHistoryDao;
import com.github.bluecatlee.gs4d.message.producer.model.SYS_ORDER_MESSAGE_SEND_LOG;
import com.github.bluecatlee.gs4d.message.producer.model.SYS_ROCKET_MQ_JOB_LOG;
import com.github.bluecatlee.gs4d.message.producer.model.SYS_ROCKET_MQ_MESSAGE_ENCRYLOG;
import com.github.bluecatlee.gs4d.message.producer.model.SYS_ROCKET_MQ_SEND_LOG;
import com.github.bluecatlee.gs4d.message.producer.utils.AbstractRocketMqUtil;
import com.github.bluecatlee.gs4d.message.producer.utils.Constants;
import com.github.bluecatlee.gs4d.message.producer.utils.SeqUtil;
import com.github.bluecatlee.gs4d.message.producer.utils.TransactionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;

import java.util.*;
import java.util.Map.Entry;

/**
 * 消息发送核心实现
 */
@Service("messageservice")
public class MessageSendServiceImpl extends AbstractMessageSendService implements MessageCenterSendService {

    private static Logger logger = LoggerFactory.getLogger(MessageSendServiceImpl.class);

    @Autowired
    private ExportDataService exportDataService;

    @Autowired
    private SysRocketMqSendLogHistoryDao sysRocketMqSendLogHistoryDao;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    @Autowired
    private SysRocketMqJobLogDao sysRocketMqJobLogDao;

    public static List<String> clusterNameList;

    private static MyJsonMapper jsonMapper = new MyJsonMapper();

    static {
        jsonMapper = MyJsonMapper.nonEmptyMapper();
        jsonMapper.getMapper().setPropertyNamingStrategy(LowerCaseStrategy.SNAKE_CASE);
    }

    public PrepSimpleMessageResponse sendPrepSimpleMessage(PrepSimpleMessageRequest request) {
        PrepSimpleMessageResponse prepSimpleMessageResponse = new PrepSimpleMessageResponse();
        long startTime = System.currentTimeMillis();
        SimpleMessage simpleMessage = request.getSimpleMessage();
        if (Constants.notInsertDbTopicTagList.contains(simpleMessage.getTopic() + "#" + simpleMessage.getTag())) {
            throw new ValidateBusinessException(Constants.SUB_SYSTEM, ExceptionType.VBE20031, "当前topic,tag数据库配置为不插入数据库,请调用立即发送消息接口" + simpleMessage.getTopic());
        } else {
            long judgeInsertTime = System.currentTimeMillis() - startTime;
            long initTime = 0L;
            long insertTime = 0L;
            long cacheTime = 0L;

            try {
                SYS_ROCKET_MQ_SEND_LOG sysRocketMqSendLog = new SYS_ROCKET_MQ_SEND_LOG();
                long initStartTime = System.currentTimeMillis();
                this.assemble(sysRocketMqSendLog, simpleMessage);   // sysRocketMqSendLog 组装参数
                initTime = System.currentTimeMillis() - initStartTime;

                long insertStartTime = System.currentTimeMillis();
                this.saveSysRocketMqSendLog(sysRocketMqSendLog);    // 保存sysRocketMqSendLog到发送日志表sys_rocket_mq_send_log
                insertTime = System.currentTimeMillis() - insertStartTime;
                logger.info("插入数据库时间为" + insertTime);

                long cacheStartTime = System.currentTimeMillis();
                this.addSysRocketMqSendLogToRedis(sysRocketMqSendLog); // 缓存sysRocketMqSendLog key为series
                cacheTime = System.currentTimeMillis() - cacheStartTime;

                prepSimpleMessageResponse.setSeries(sysRocketMqSendLog.getSERIES());
            } catch (Exception e) {
                logger.error("发消息错误，topic" + request.getSimpleMessage().getTopic() + "," + "tag," + request.getSimpleMessage().getTag() + ",入参" + request.getSimpleMessage().getBody());
                logger.error(e.getMessage(), e);
                ExceptionUtil.processException(e, prepSimpleMessageResponse);
            }

            Long endTime = System.currentTimeMillis() - startTime;
            if (endTime > 100L) {
                logger.info("预发送消息花费时间为" + endTime + "存入数据库时间为" + insertTime + "初始化序列时间：" + initTime + "存入redis时间" + cacheTime + "判断是否插入数据库时间" + judgeInsertTime);
            }

            if (prepSimpleMessageResponse.getSeries() == null || prepSimpleMessageResponse.getSeries() == 0L) {
                logger.info("当前序列号出现异常。");
            }

            return prepSimpleMessageResponse;
        }
    }

    private void saveSysRocketMqSendLog(SYS_ROCKET_MQ_SEND_LOG sysRocketMqSendLog) {
        this.sysRocketMqSendLogDao.insert(sysRocketMqSendLog);
    }

    public MsgConfirmSendResponse confirmSendMsg(MsgConfirmSendRequest msgConfirmSendRequest) {
        ArrayList list = new ArrayList();
        MsgConfirmSendResponse msgConfirmSendResponse = new MsgConfirmSendResponse();
        long startTime = System.currentTimeMillis();
        String db = "redis";
        long dbQueryTime = 0L;
        long cacheCostTime = 0L;
        long putIntoThreadPoolCostTime = 0L;

        try {
            Iterator iterator = msgConfirmSendRequest.getSeries().iterator();

            while(true) {
                Long series;
                while(iterator.hasNext()) {
                    series = (Long)iterator.next();
                    if (series == null) {
                        logger.error("消息中心确认消息发现序列号为null");
                    } else {
                        SYS_ROCKET_MQ_SEND_LOG sysRocketMqSendLog = null;

                        try {
                            sysRocketMqSendLog = (SYS_ROCKET_MQ_SEND_LOG) JSONObject.parseObject((String)this.stringRedisTemplate.opsForValue().get(MessageSendUtil.generateMessageKeyToRedis(series)), SYS_ROCKET_MQ_SEND_LOG.class);
                        } catch (Exception e) {
                            logger.error("消息中心确认消息插入redis异常,原因:" + e.getMessage() + ",series:" + series, e);
                        }
                        dbQueryTime = System.currentTimeMillis() - startTime;

                        if (sysRocketMqSendLog == null) {
                            db = "mysql";
                            logger.warn("消息中心确认消息发送时发现redis中没有的序列号" + series);
                            sysRocketMqSendLog = this.sysRocketMqSendLogDao.queryBySeriesStrict(series);
                            dbQueryTime = System.currentTimeMillis() - startTime;
                        }

                        Long currentTime = System.currentTimeMillis();
                        if (sysRocketMqSendLog != null && sysRocketMqSendLog.getMSG_STATUS() == Constants.eJ) {
                            this.sysRocketMqSendLogDao.updateCreateUserId(sysRocketMqSendLog.getSERIES(), Constants.dO);
                            sysRocketMqSendLog.setCREATE_USER_ID(Constants.dO);
                            this.pushSysRocketMqSendLogToWorkFlowList(sysRocketMqSendLog);  // 将sysRocketMqSendLog追加到链表右边
                        } else {
                            cacheCostTime += System.currentTimeMillis() - currentTime;
                            list.add(sysRocketMqSendLog);
                        }
                    }
                }

                if (list.size() > 0) {
                    long putIntoThreadPoolStartTime = System.currentTimeMillis();
                    AbstractRocketMqUtil.send(list, this.stringRedisTemplate);          // 使用线程池 将消息发送到消息队列
                    putIntoThreadPoolCostTime = System.currentTimeMillis() - putIntoThreadPoolStartTime;
                }
                break;
            }
        } catch (Exception e) {
            logger.error("确认消息异常，异常的序列号是：" + msgConfirmSendRequest.getMessageSeries());
            logger.error(e.getMessage(), e);
            ExceptionUtil.processException(e, msgConfirmSendResponse);
        }

        long totalCostTime = System.currentTimeMillis() - startTime;
        if (totalCostTime > 100L) {
            logger.info("确认消息花费时间" + totalCostTime + "数据库为" + db + "查询数据库时间" + dbQueryTime + "投入线程池时间为" + putIntoThreadPoolCostTime + "缓存时间为" + cacheCostTime);
        }

        return msgConfirmSendResponse;
    }

    public MsgCancelSendResponse cancelSendMsg(MsgCancelSendRequest msgCancelSendRequest) {
        MsgCancelSendResponse msgCancelSendResponse = new MsgCancelSendResponse();

        try {
            ArrayList list = new ArrayList();
            Iterator iterator = msgCancelSendRequest.getSeries().iterator();

            while(iterator.hasNext()) {
                Long series = (Long)iterator.next();
                if (series == null) {
                    logger.error("消息中心取消消息发现序列号为null");
                } else {
                    SYS_ROCKET_MQ_SEND_LOG sysRocketMqSendLog = null;

                    try {
                        sysRocketMqSendLog = (SYS_ROCKET_MQ_SEND_LOG)JSONObject.parseObject((String)this.stringRedisTemplate.opsForValue().get(MessageSendUtil.generateMessageKeyToRedis(series)), SYS_ROCKET_MQ_SEND_LOG.class);
                    } catch (Exception e) {
                        logger.error("消息中心取消消息插入redis异常,原因:" + e.getMessage() + ",series:" + series, e);
                    }

                    if (sysRocketMqSendLog == null) {
                        logger.warn("消息中心取消消息发送时发现redis中没有的序列号" + series);
                        sysRocketMqSendLog = this.sysRocketMqSendLogDao.queryBySeriesStrict(series);
                    } else {
                        this.stringRedisTemplate.delete(MessageSendUtil.generateMessageKeyToRedis(series));
                    }

                    sysRocketMqSendLog.setMSG_STATUS(Constants.send_cancel);
                    sysRocketMqSendLog.setSTEP_ID(Constants.eE);
                    list.add(sysRocketMqSendLog);
                }
            }

            if (list.size() > 0) {
                this.sysRocketMqSendLogHistoryDao.batchInsert(list);
                this.sysRocketMqSendLogDao.batchDelete(list);
            }
        } catch (Exception e) {
            logger.error("消息取消失败，入参序列号是" + msgCancelSendRequest.getMessageSeries());
            logger.error(e.getMessage(), e);
            ExceptionUtil.processException(e, msgCancelSendResponse);
        }

        return msgCancelSendResponse;
    }

    public SimpleMessageRightNowSendResponse sendSimpleMessageRightNow(SimpleMessageRightNowSendRequest simpleMessageRightNowSendRequest) {
        SimpleMessage simpleMessage = simpleMessageRightNowSendRequest.getSimpleMessage();
        SimpleMessageRightNowSendResponse simpleMessageRightNowSendResponse = new SimpleMessageRightNowSendResponse();
        if (Constants.notInsertDbTopicTagList.contains(simpleMessage.getTopic() + "#" + simpleMessage.getTag())) {
            SYS_ROCKET_MQ_SEND_LOG sysRocketMqSendLog = new SYS_ROCKET_MQ_SEND_LOG();
            sysRocketMqSendLog.setMESSAGE_BODY(simpleMessage.getBody());
            sysRocketMqSendLog.setINSTANCE_ID(0L);
            sysRocketMqSendLog.setHAS_LOG("N");
            sysRocketMqSendLog.setSERIES(1L);
            sysRocketMqSendLog.setMESSAGE_TOPIC(simpleMessage.getTopic());
            sysRocketMqSendLog.setMESSAGE_TAG(simpleMessage.getTag());
            sysRocketMqSendLog.setMESSAGE_KEY(simpleMessage.getMsgKey());
            sysRocketMqSendLog.setTENANT_NUM_ID(simpleMessage.getTenantNumId());
            sysRocketMqSendLog.setDATA_SIGN(simpleMessageRightNowSendRequest.getDataSign());
            AbstractRocketMqUtil.send(simpleMessage.getTopic(), simpleMessage.getTag(), simpleMessage.getMsgKey(), sysRocketMqSendLog, simpleMessage.getTenantNumId());
            return simpleMessageRightNowSendResponse;
        } else {
            PrepSimpleMessageRequest prepSimpleMessageRequest = new PrepSimpleMessageRequest();
            prepSimpleMessageRequest.setSimpleMessage(simpleMessageRightNowSendRequest.getSimpleMessage());

            try {
                SYS_ROCKET_MQ_SEND_LOG sysRocketMqSendLog = new SYS_ROCKET_MQ_SEND_LOG();
                this.assemble(sysRocketMqSendLog, simpleMessage);
                if (sysRocketMqSendLog != null && Constants.distinctTopicTagList.contains(sysRocketMqSendLog.getMESSAGE_TOPIC() + "#" + sysRocketMqSendLog.getMESSAGE_TAG())) {
                    String msgmd = Md5Util.md5(sysRocketMqSendLog.getMESSAGE_BODY());
                    List mqMessageEncrylogs = this.sysRocketMqMessageEncrylogDao.query(msgmd, simpleMessage.getTopic(), simpleMessage.getTag(), simpleMessage.getDataSign(), simpleMessage.getTenantNumId());
                    if (mqMessageEncrylogs != null && !mqMessageEncrylogs.isEmpty()) {
                        simpleMessageRightNowSendResponse.setSeries(((SYS_ROCKET_MQ_MESSAGE_ENCRYLOG)mqMessageEncrylogs.get(0)).getMsgSeries());
                        return simpleMessageRightNowSendResponse;
                    }

                    this.sysRocketMqMessageEncrylogDao.insert(sysRocketMqSendLog.getSERIES(), msgmd, simpleMessage.getTopic(), simpleMessage.getTag(), simpleMessage.getDataSign(), simpleMessage.getTenantNumId());
                } else if (sysRocketMqSendLog != null && Constants.notDistinctTopicTagList.contains(sysRocketMqSendLog.getMESSAGE_TOPIC() + "#" + sysRocketMqSendLog.getMESSAGE_TAG())) {
                    List mqMessageEncrylogs = this.sysRocketMqMessageEncrylogDao.query(sysRocketMqSendLog.getMESSAGE_KEY(), simpleMessage.getTopic(), simpleMessage.getTag(), simpleMessage.getDataSign(), simpleMessage.getTenantNumId());
                    if (mqMessageEncrylogs != null && !mqMessageEncrylogs.isEmpty()) {
                        simpleMessageRightNowSendResponse.setSeries(((SYS_ROCKET_MQ_MESSAGE_ENCRYLOG)mqMessageEncrylogs.get(0)).getMsgSeries());
                        return simpleMessageRightNowSendResponse;
                    }

                    this.sysRocketMqMessageEncrylogDao.insert(sysRocketMqSendLog.getSERIES(), sysRocketMqSendLog.getMESSAGE_KEY(), simpleMessage.getTopic(), simpleMessage.getTag(), simpleMessage.getDataSign(), simpleMessage.getTenantNumId());
                }

                long insertStartTime = System.currentTimeMillis();
                this.saveSysRocketMqSendLog(sysRocketMqSendLog);
                long insertCostTime = System.currentTimeMillis() - insertStartTime;
                logger.info("插入数据库时间为" + insertCostTime);

                long cacheStartTime = System.currentTimeMillis();
                this.addSysRocketMqSendLogToRedis(sysRocketMqSendLog);
                long cacheCostTime = System.currentTimeMillis() - cacheStartTime;
                logger.info("插入redis时间为" + cacheCostTime);

                simpleMessageRightNowSendResponse.setSeries(sysRocketMqSendLog.getSERIES());
                ArrayList list = new ArrayList();
                list.add(simpleMessageRightNowSendResponse.getSeries());
                MsgConfirmSendRequest msgConfirmSendRequest = new MsgConfirmSendRequest();
                msgConfirmSendRequest.setSeries(list);
                this.confirmSendMsg(msgConfirmSendRequest);
            } catch (Exception e) {
                ExceptionUtil.processException(e, simpleMessageRightNowSendResponse);
            }

            return simpleMessageRightNowSendResponse;
        }
    }

    public MsgRetrySendResponse retrySendMsg(MsgRetrySendRequest msgRetrySendRequest) {
        if (logger.isDebugEnabled()) {
            logger.debug("begin retrySendMsg request:{}", JsonUtil.toJson(msgRetrySendRequest));
        }

        MsgRetrySendResponse msgRetrySendResponse = new MsgRetrySendResponse();

        try {
            ArrayList sysRocketMqSendLogList = new ArrayList();
            SYS_ROCKET_MQ_SEND_LOG sysRocketMqSendLog = (SYS_ROCKET_MQ_SEND_LOG)JSONObject.parseObject((String)this.stringRedisTemplate.opsForValue().get(MessageSendUtil.generateMessageKeyToRedis(msgRetrySendRequest.getSeries())), SYS_ROCKET_MQ_SEND_LOG.class);
            if (sysRocketMqSendLog == null) {
                logger.warn("消息中心重发消息时发现redis中没有的序列号" + msgRetrySendRequest.getSeries());
                sysRocketMqSendLog = this.sysRocketMqSendLogDao.queryBySeriesStrict(msgRetrySendRequest.getSeries());
            }

            if (msgRetrySendRequest.getSendType() == null) {
                sysRocketMqSendLog.setSTEP_ID(Constants.ey);
            } else {
                sysRocketMqSendLog.setSTEP_ID(msgRetrySendRequest.getSendType());
            }

            if (this.expressTag.contains(sysRocketMqSendLog.getMESSAGE_TAG()) && !sysRocketMqSendLog.getMESSAGE_BODY().contains("{")) {
                sysRocketMqSendLog.setEXPRESS_FLAG("Y");
            }

            sysRocketMqSendLogList.add(sysRocketMqSendLog);
            if (sysRocketMqSendLogList.size() > 0) {
                AbstractRocketMqUtil.send(sysRocketMqSendLogList, this.stringRedisTemplate);
            }
        } catch (Throwable throwable) {
            ExceptionUtils.processException(throwable, msgRetrySendResponse);
            if (msgRetrySendResponse.getMessage().contains("获取实体失败")) {
                msgRetrySendResponse.setMessage("此消息不存在与sendLog表，请去sendLogHistory表查看详情");
            }
        }

        if (logger.isDebugEnabled()) {
            logger.debug("end retrySendMsg response:{}", msgRetrySendResponse.toLowerCaseJson());
        }

        return msgRetrySendResponse;
    }

    public TopicCreateResponse createTopic(TopicCreateRequest topicCreateRequest) {
        TopicCreateResponse topicCreateResponse = new TopicCreateResponse();

        try {
            if (StringUtil.isAllNullOrBlank(new String[]{topicCreateRequest.getTopic()})) {
                topicCreateResponse.setCode(-1L);
                topicCreateResponse.setMessage("创建topic,topic不能为空");
                return topicCreateResponse;
            }

            DefaultMQAdminExt mqAdminExt = new DefaultMQAdminExt();

            try {
                if (clusterNameList == null) {
                    this.initClusterNameList(topicCreateRequest.getNameSrv());
                }

                mqAdminExt.setInstanceName(Long.toString(System.currentTimeMillis()));
                mqAdminExt.setNamesrvAddr(topicCreateRequest.getNameSrv());
                mqAdminExt.start();
                String clusterName = "";

                for(int i = 0; i < clusterNameList.size(); ++i) {
                    if (i == 0) {
                        clusterName = clusterName + clusterNameList.get(i);
                    } else {
                        clusterName = clusterName + ";" + clusterNameList.get(i);
                    }
                }

                TopicConfig topicConfig = new TopicConfig();
                if (topicCreateRequest.getRowNum() != null && topicCreateRequest.getRowNum() != 0L) {
                    topicConfig.setReadQueueNums(topicCreateRequest.getRowNum().intValue());
                    topicConfig.setWriteQueueNums(topicCreateRequest.getRowNum().intValue());
                } else {
                    topicConfig.setReadQueueNums(8);
                    topicConfig.setWriteQueueNums(8);
                }

                topicConfig.setTopicName(topicCreateRequest.getTopic());
                Set masterAddrs = CommandUtil.fetchMasterAddrByClusterName(mqAdminExt, clusterName);
                Iterator iterator = masterAddrs.iterator();

                while(iterator.hasNext()) {
                    String masterAddr = (String)iterator.next();
                    mqAdminExt.createAndUpdateTopicConfig(masterAddr, topicConfig);
                }
            } catch (Exception e) {
                throw new ValidateBusinessException(Constants.SUB_SYSTEM, ExceptionType.VBE20031, "创建topic产生" + e.getMessage() + "的问题");
            } finally {
                mqAdminExt.shutdown();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            ExceptionUtil.processException(e, topicCreateResponse);
        }

        return topicCreateResponse;
    }

    private List<String> initClusterNameList(String namesrv) throws Exception {
        DefaultMQAdminExt mqAdminExt = new DefaultMQAdminExt();

        try {
            mqAdminExt.setNamesrvAddr(namesrv);
            mqAdminExt.setInstanceName(Long.toString(System.currentTimeMillis()));
            mqAdminExt.start();
            ArrayList list = new ArrayList();
            ClusterInfo clusterInfo = mqAdminExt.examineBrokerClusterInfo();
            Iterator iterator = clusterInfo.getClusterAddrTable().entrySet().iterator();

            while(iterator.hasNext()) {
                Entry entry = (Entry)iterator.next();
                String key = (String)entry.getKey();
                list.add(key);
            }

            clusterNameList = list;
            return list;
        } catch (Exception e) {
            throw new Exception("消息中心web项目获取客户端信息时出现错误。");
        } finally {
            mqAdminExt.shutdown();
        }
    }

    public SendMessRightNowResponse sendMessRightNow(SendMessRightNowRequest sendMessRightNowRequest) {
        SendMessRightNowResponse sendMessRightNowResponse = new SendMessRightNowResponse();
        long startTime = System.currentTimeMillis();

        try {
            SimpleMessageRightNowSendRequest simpleMessageRightNowSendRequest = new SimpleMessageRightNowSendRequest();
            simpleMessageRightNowSendRequest.setSimpleMessage(new SimpleMessage(sendMessRightNowRequest.getTopic(), sendMessRightNowRequest.getTag(), sendMessRightNowRequest.getMsgKey(), jsonMapper.toJson(sendMessRightNowRequest.getBody()), Long.valueOf(sendMessRightNowRequest.getFromSystem()), sendMessRightNowRequest.getDataSign(), sendMessRightNowRequest.getTenantNumId()));
            SimpleMessageRightNowSendResponse simpleMessageRightNowSendResponse = this.sendSimpleMessageRightNow(simpleMessageRightNowSendRequest);
            sendMessRightNowResponse.setCode(simpleMessageRightNowSendResponse.getCode());
            sendMessRightNowResponse.setMessage(simpleMessageRightNowSendResponse.getMessage());
            sendMessRightNowResponse.setSeries(simpleMessageRightNowSendResponse.getSeries());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            ExceptionUtil.processException(e, sendMessRightNowResponse);
        }

        long duration = System.currentTimeMillis() - startTime;
        logger.info("发送消息总共时间为" + duration);
        return sendMessRightNowResponse;
    }

    public PrepOTMSimpleMessageSendResponse sendPrepOTMSimpleMessage(PrepOTMSimpleMessageSendRequest prepOTMSimpleMessageSendRequest) {
        PrepOTMSimpleMessageSendResponse prepOTMSimpleMessageSendResponse = new PrepOTMSimpleMessageSendResponse();

        try {
            SimpleMessage simpleMessage = prepOTMSimpleMessageSendRequest.getSimpleMessage();
            if (simpleMessage.getDataSign() == null) {
                if (this.dataSign == 2L) {
                    simpleMessage.setDataSign(1L);
                } else {
                    simpleMessage.setDataSign(this.dataSign);
                }
            }

            if (simpleMessage.getTenantNumId() == null) {
                simpleMessage.setTenantNumId(1L);
            }

            if (Constants.oneToManyTopicsMap.isEmpty() || Constants.oneToManyTopicsMap.get(simpleMessage.getTopic() + "#" + simpleMessage.getTenantNumId()) == null) {
                throw new ValidateBusinessException(Constants.SUB_SYSTEM, ExceptionType.VBE20031, "一对多发送消息异常，消息中心初始化时没有初始化到父类Topic" + simpleMessage.getTopic());
            }

            ArrayList seriesList = new ArrayList();
            Map sonTopicTagMap = (Map) Constants.oneToManyTopicsMap.get(simpleMessage.getTopic() + "#" + simpleMessage.getTenantNumId());
            Iterator iterator = sonTopicTagMap.entrySet().iterator();

            while(iterator.hasNext()) {
                Entry entry = (Entry)iterator.next();
                PrepSimpleMessageRequest prepSimpleMessageRequest = new PrepSimpleMessageRequest();
                prepSimpleMessageRequest.setSimpleMessage(new SimpleMessage((String)entry.getValue(), (String)entry.getKey(), simpleMessage.getMsgKey(), simpleMessage.getBody(), simpleMessage.getFromSystem(), simpleMessage.getDataSign(), simpleMessage.getTenantNumId(), simpleMessage.getClientIp()));
                PrepSimpleMessageResponse prepSimpleMessageResponse = this.sendPrepSimpleMessage(prepSimpleMessageRequest);
                seriesList.add(prepSimpleMessageResponse.getSeries());
            }

            prepOTMSimpleMessageSendResponse.setSeries(seriesList);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            ExceptionUtil.processException(e, prepOTMSimpleMessageSendResponse);
        }

        return prepOTMSimpleMessageSendResponse;
    }

    public OTMSimpleMessageRightNowSendResponse sendOTMSimpleMessageRightNow(OTMSimpleMessageRightNowSendRequest otmSimpleMessageRightNowSendRequest) {
        OTMSimpleMessageRightNowSendResponse otmSimpleMessageRightNowSendResponse = new OTMSimpleMessageRightNowSendResponse();

        try {
            PrepOTMSimpleMessageSendRequest prepOTMSimpleMessageSendRequest = new PrepOTMSimpleMessageSendRequest();
            prepOTMSimpleMessageSendRequest.setSimpleMessage(otmSimpleMessageRightNowSendRequest.getSimpleMessage());
            PrepOTMSimpleMessageSendResponse prepOTMSimpleMessageSendResponse = this.sendPrepOTMSimpleMessage(prepOTMSimpleMessageSendRequest);
            MsgConfirmSendRequest msgConfirmSendRequest = new MsgConfirmSendRequest();
            msgConfirmSendRequest.setSeries(prepOTMSimpleMessageSendResponse.getSeries());
            this.confirmSendMsg(msgConfirmSendRequest);
            otmSimpleMessageRightNowSendResponse.setSeries(prepOTMSimpleMessageSendResponse.getSeries());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            ExceptionUtil.processException(e, otmSimpleMessageRightNowSendResponse);
        }

        return otmSimpleMessageRightNowSendResponse;
    }

    public PrepMessSendResponse sendPrepMess(SendMessRightNowRequest sendMessRightNowRequest) {
        PrepMessSendResponse prepMessSendResponse = new PrepMessSendResponse();

        try {
            PrepSimpleMessageRequest prepSimpleMessageRequest = new PrepSimpleMessageRequest();
            SimpleMessage simpleMessage = new SimpleMessage();
            simpleMessage.setBody(jsonMapper.toJson(sendMessRightNowRequest.getBody()));
            simpleMessage.setFromSystem(Long.valueOf(sendMessRightNowRequest.getFromSystem()));
            simpleMessage.setTopic(sendMessRightNowRequest.getTopic());
            simpleMessage.setTag(sendMessRightNowRequest.getTag());
            simpleMessage.setMsgKey(sendMessRightNowRequest.getMsgKey());
            simpleMessage.setDataSign(sendMessRightNowRequest.getDataSign());
            simpleMessage.setTenantNumId(sendMessRightNowRequest.getTenantNumId());
            prepSimpleMessageRequest.setSimpleMessage(simpleMessage);
            PrepSimpleMessageResponse prepSimpleMessageResponse = this.sendPrepSimpleMessage(prepSimpleMessageRequest);
            prepMessSendResponse.setSeries(prepSimpleMessageResponse.getSeries());
            prepMessSendResponse.setCode(prepSimpleMessageResponse.getCode());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            ExceptionUtil.processException(e, prepMessSendResponse);
        }

        return prepMessSendResponse;
    }

    public TransactionMessageConfirmResponse confirmTransactionMessage(TransactionMessageConfirmRequest transactionMessageConfirmRequest) {
        TransactionMessageConfirmResponse transactionMessageConfirmResponse = new TransactionMessageConfirmResponse();

        try {
            List sysRocketMqSendLogList = new ArrayList();
            List list = this.stringRedisTemplate.opsForList().range(MessageSendUtil.generateTranstionMessageKeyToRedis(transactionMessageConfirmRequest.getTransactionId()), 0L, -1L);
            if (list.isEmpty()) {
                sysRocketMqSendLogList = this.sysRocketMqSendLogDao.queryByWorkflowId(transactionMessageConfirmRequest.getTransactionId());
            } else {
                Iterator iterator = list.iterator();

                while(iterator.hasNext()) {
                    String item = (String)iterator.next();
                    SYS_ROCKET_MQ_SEND_LOG sysRocketMqSendLog = (SYS_ROCKET_MQ_SEND_LOG)JSONObject.parseObject(item, SYS_ROCKET_MQ_SEND_LOG.class);
                    (sysRocketMqSendLogList).add(sysRocketMqSendLog);
                }
            }

            if ((sysRocketMqSendLogList).size() > 0) {
                AbstractRocketMqUtil.send(sysRocketMqSendLogList, this.stringRedisTemplate);
            }
        } catch (Exception e) {
            transactionMessageConfirmResponse.setCode(-1L);
            transactionMessageConfirmResponse.setMessage(e.getMessage());
            logger.error(e.getMessage(), e);
        } finally {
            try {
                this.stringRedisTemplate.delete(MessageSendUtil.generateTranstionMessageKeyToRedis(transactionMessageConfirmRequest.getTransactionId()));
            } catch (Exception e) {
                logger.error("分布式事务删除键失败", e);
            }

        }

        return transactionMessageConfirmResponse;
    }

    public TransactionMessageCancelResponse cancelTransactionMessage(TransactionMessageCancelRequest transactionMessageCancelRequest) {
        TransactionMessageCancelResponse transactionMessageCancelResponse = new TransactionMessageCancelResponse();

        try {
            List sysRocketMqSendLogList = new ArrayList();
            List list = this.stringRedisTemplate.opsForList().range(MessageSendUtil.generateTranstionMessageKeyToRedis(transactionMessageCancelRequest.getTransactionId()), 0L, -1L);
            Iterator iterator;
            if (list.isEmpty()) {
                sysRocketMqSendLogList = this.sysRocketMqSendLogDao.queryByWorkflowId(transactionMessageCancelRequest.getTransactionId());
            } else {
                iterator = list.iterator();

                while(iterator.hasNext()) {
                    String item = (String)iterator.next();
                    SYS_ROCKET_MQ_SEND_LOG sysRocketMqSendLog = (SYS_ROCKET_MQ_SEND_LOG)JSONObject.parseObject(item, SYS_ROCKET_MQ_SEND_LOG.class);
                    (sysRocketMqSendLogList).add(sysRocketMqSendLog);
                }
            }

            iterator = (sysRocketMqSendLogList).iterator();

            while(iterator.hasNext()) {
                SYS_ROCKET_MQ_SEND_LOG sysRocketMqSendLog = (SYS_ROCKET_MQ_SEND_LOG)iterator.next();
                sysRocketMqSendLog.setMSG_STATUS(Constants.eK);
                sysRocketMqSendLog.setWORKFLOW_ID(transactionMessageCancelRequest.getTransactionId());
            }

            if ((sysRocketMqSendLogList).size() > 0) {
                this.sysRocketMqSendLogHistoryDao.batchInsert(sysRocketMqSendLogList);
                this.sysRocketMqSendLogDao.batchDelete(sysRocketMqSendLogList);
            }
        } catch (Exception e) {
            transactionMessageCancelResponse.setCode(-1L);
            transactionMessageCancelResponse.setMessage(e.getMessage());
            logger.error(e.getMessage(), e);
        }

        return transactionMessageCancelResponse;
    }

    public PrepOrderSimpleMessageResponse sendPrepOrderSimpleMessage(PrepOrderSimpleMessageRequest prepOrderSimpleMessageRequest) {
        PrepOrderSimpleMessageResponse prepOrderSimpleMessageResponse = new PrepOrderSimpleMessageResponse();
        TransactionStatus transactionStatus = this.platformTransactionManager.getTransaction(TransactionUtil.newTransactionDefinition(30));

        try {
            Long orderMessageGroupId = null;
            ArrayList orderMessageModelList = new ArrayList();

            for(int i = 0; i < prepOrderSimpleMessageRequest.getSimpleMessage().size(); ++i) {
                OrderMessageModel orderMessageModel = new OrderMessageModel();
                SimpleMessage simpleMessage = (SimpleMessage)prepOrderSimpleMessageRequest.getSimpleMessage().get(i);
                SYS_ORDER_MESSAGE_SEND_LOG sysOrderMessageSendLog = new SYS_ORDER_MESSAGE_SEND_LOG();
                sysOrderMessageSendLog.setSERIES(SeqUtil.getNoSubSequence(SeqUtil.SYS_ROCKET_MQ_SEND_LOG_SERIES));
                sysOrderMessageSendLog.setTENANT_NUM_ID(prepOrderSimpleMessageRequest.getTenantNumId());
                sysOrderMessageSendLog.setDATA_SIGN(prepOrderSimpleMessageRequest.getDataSign());
                sysOrderMessageSendLog.setCLIENT_IP(simpleMessage.getClientIp());
                sysOrderMessageSendLog.setFROM_SYSTEM(simpleMessage.getFromSystem().toString());
                sysOrderMessageSendLog.setMESSAGE_BODY(simpleMessage.getBody());
                sysOrderMessageSendLog.setMESSAGE_KEY(simpleMessage.getMsgKey());
                sysOrderMessageSendLog.setMESSAGE_TAG(simpleMessage.getTag());
                sysOrderMessageSendLog.setMESSAGE_TOPIC(simpleMessage.getTopic());
                sysOrderMessageSendLog.setORDER_ID(i);
                if (orderMessageGroupId == null) {
                    sysOrderMessageSendLog.setORDER_MESSAGE_GROUP_ID(SeqUtil.getNoSubSequence(SeqUtil.ORDER_MESSAGE_GROUP_ID));
                    orderMessageGroupId = sysOrderMessageSendLog.getORDER_MESSAGE_GROUP_ID();
                } else {
                    sysOrderMessageSendLog.setORDER_MESSAGE_GROUP_ID(orderMessageGroupId);
                }

                this.sysOrderMessageSendLogDao.insert(sysOrderMessageSendLog);
                orderMessageModel.setTopic(simpleMessage.getTopic());
                orderMessageModel.setTag(simpleMessage.getTag());
                orderMessageModel.setTenantNumId(simpleMessage.getTenantNumId());
                orderMessageModel.setDataSign(simpleMessage.getDataSign());
                orderMessageModel.setSystemId(simpleMessage.getFromSystem());
                orderMessageModel.setSeries(sysOrderMessageSendLog.getSERIES());
                orderMessageModelList.add(orderMessageModel);
            }

            prepOrderSimpleMessageResponse.setOrderMessageGroupId(orderMessageGroupId);
            prepOrderSimpleMessageResponse.setOrderMessageModel(orderMessageModelList);
            this.platformTransactionManager.commit(transactionStatus);
        } catch (Exception e) {
            this.platformTransactionManager.rollback(transactionStatus);
            prepOrderSimpleMessageResponse.setCode(-1L);
            prepOrderSimpleMessageResponse.setMessage(e.getMessage());
            logger.error(e.getMessage(), e);
        }

        return prepOrderSimpleMessageResponse;
    }

    public OrderSendMessageConfirmResponse confirmOrderSendMessage(OrderSendMessageConfirmRequest orderSendMessageConfirmRequest) {
        OrderSendMessageConfirmResponse orderSendMessageConfirmResponse = new OrderSendMessageConfirmResponse();

        try {
            List sysOrderMessageSendLogs = this.sysOrderMessageSendLogDao.queryUnSendOrderMessage(orderSendMessageConfirmRequest.getOrderMessageGroupId());
            if (sysOrderMessageSendLogs.isEmpty()) {
                return orderSendMessageConfirmResponse;
            }

            SimpleMessage simpleMessage = new SimpleMessage();
            simpleMessage.setBody(((SYS_ORDER_MESSAGE_SEND_LOG)sysOrderMessageSendLogs.get(0)).getMESSAGE_BODY());
            simpleMessage.setClientIp(((SYS_ORDER_MESSAGE_SEND_LOG)sysOrderMessageSendLogs.get(0)).getCLIENT_IP());
            simpleMessage.setDataSign(((SYS_ORDER_MESSAGE_SEND_LOG)sysOrderMessageSendLogs.get(0)).getDATA_SIGN());
            simpleMessage.setTenantNumId(((SYS_ORDER_MESSAGE_SEND_LOG)sysOrderMessageSendLogs.get(0)).getTENANT_NUM_ID());
            simpleMessage.setTopic(((SYS_ORDER_MESSAGE_SEND_LOG)sysOrderMessageSendLogs.get(0)).getMESSAGE_TOPIC());
            simpleMessage.setTag(((SYS_ORDER_MESSAGE_SEND_LOG)sysOrderMessageSendLogs.get(0)).getMESSAGE_TAG());
            simpleMessage.setFromSystem(Long.valueOf(((SYS_ORDER_MESSAGE_SEND_LOG)sysOrderMessageSendLogs.get(0)).getFROM_SYSTEM()));
            simpleMessage.setMsgKey(((SYS_ORDER_MESSAGE_SEND_LOG)sysOrderMessageSendLogs.get(0)).getMESSAGE_KEY());
            simpleMessage.setWorkFlowId(orderSendMessageConfirmRequest.getOrderMessageGroupId());
            SimpleMessageRightNowSendResponse simpleMessageRightNowSendResponse = MessageSendUtil.sendSimpleMessageRightNow(simpleMessage);
            if (simpleMessageRightNowSendResponse.getCode() != 0L) {
                throw new RuntimeException("流程消息确认发送消息失败。");
            }

            this.sysOrderMessageSendLogDao.updateSendSuccess(((SYS_ORDER_MESSAGE_SEND_LOG)sysOrderMessageSendLogs.get(0)).getSERIES());
        } catch (Exception e) {
            ExceptionUtil.processException(e, orderSendMessageConfirmResponse);
        }

        return orderSendMessageConfirmResponse;
    }

    public OrderSendMessageCancelResponse cancelOrderSendMessage(OrderSendMessageCancelRequest orderSendMessageCancelRequest) {
        OrderSendMessageCancelResponse orderSendMessageCancelResponse = new OrderSendMessageCancelResponse();

        try {
            this.sysOrderMessageSendLogDao.cancelByOrderMessageGroupId(orderSendMessageCancelRequest.getOrderMessageGroupId());
        } catch (Exception e) {
            ExceptionUtil.processException(e, orderSendMessageCancelResponse);
        }

        return orderSendMessageCancelResponse;
    }

    public OrderMessageRightNowResponse sendOrderMessageRightNow(OrderMessageRightNowRequest orderMessageRightNowRequest) {
        OrderMessageRightNowResponse orderMessageRightNowResponse = new OrderMessageRightNowResponse();

        try {
            PrepOrderSimpleMessageRequest prepOrderSimpleMessageRequest = new PrepOrderSimpleMessageRequest();
            prepOrderSimpleMessageRequest.setSimpleMessage(orderMessageRightNowRequest.getSimpleMessage());
            prepOrderSimpleMessageRequest.setDataSign(orderMessageRightNowRequest.getDataSign());
            prepOrderSimpleMessageRequest.setTenantNumId(orderMessageRightNowRequest.getTenantNumId());
            PrepOrderSimpleMessageResponse prepOrderSimpleMessageResponse = this.sendPrepOrderSimpleMessage(prepOrderSimpleMessageRequest);
            OrderSendMessageConfirmRequest orderSendMessageConfirmRequest = new OrderSendMessageConfirmRequest();
            orderSendMessageConfirmRequest.setOrderMessageGroupId(prepOrderSimpleMessageResponse.getOrderMessageGroupId());
            OrderSendMessageConfirmResponse orderSendMessageConfirmResponse = this.confirmOrderSendMessage(orderSendMessageConfirmRequest);
            orderMessageRightNowResponse.setCode(orderSendMessageConfirmResponse.getCode());
            orderMessageRightNowResponse.setMessage(orderSendMessageConfirmResponse.getMessage());
        } catch (Exception e) {
            ExceptionUtil.processException(e, orderMessageRightNowResponse);
        }

        return orderMessageRightNowResponse;
    }

    public JobCronMessageRightNowSendResponse sendJobCronMessageRightNow(JobCronMessageRightNowSendRequest jobCronMessageRightNowSendRequest) {
        JobCronMessageRightNowSendResponse jobCronMessageRightNowSendResponse = new JobCronMessageRightNowSendResponse();

        try {
            JobMessageModel jobMessageModel = jobCronMessageRightNowSendRequest.getJobMessageModel();
            Boolean flag = SchedulerUtil.canTrigger(jobMessageModel.getCron());
            if (!flag) {
                throw new RuntimeException("输入的cron表达式时间不合法或者执行时间小于当前时间,请检查!cron表达式为" + jobCronMessageRightNowSendRequest.getJobMessageModel().getCron());
            }

            SYS_ROCKET_MQ_JOB_LOG sysRocketMqJobLog = new SYS_ROCKET_MQ_JOB_LOG();
            sysRocketMqJobLog.setCLIENT_IP(jobMessageModel.getClientIp());
            sysRocketMqJobLog.setCRON(jobMessageModel.getCron());
            sysRocketMqJobLog.setMESSAGE_KEY(jobMessageModel.getMessageKey());
            sysRocketMqJobLog.setMESSAGE_TAG(jobMessageModel.getTag());
            sysRocketMqJobLog.setMESSAGE_TOPIC(jobMessageModel.getTopic());
            sysRocketMqJobLog.setTENANT_NUM_ID(jobCronMessageRightNowSendRequest.getTenantNumId());
            sysRocketMqJobLog.setDATA_SIGN(jobCronMessageRightNowSendRequest.getDataSign());
            Long series = SeqUtil.getNoSubSequence(SeqUtil.SYS_ROCKET_MQ_JOB_LOG_SERIES);
            sysRocketMqJobLog.setSERIES(series);
            SimpleObjectMessage simpleObjectMessage = new SimpleObjectMessage();
            simpleObjectMessage.setTopic(jobMessageModel.getTopic());
            simpleObjectMessage.setTag(jobMessageModel.getTag());
            simpleObjectMessage.setClientIp(jobMessageModel.getClientIp());
            simpleObjectMessage.setMsgKey(jobMessageModel.getMessageKey());
            simpleObjectMessage.setBody(jobMessageModel.getMessageBody());
            simpleObjectMessage.setFromSystem(jobMessageModel.getSystemId().toString());
            simpleObjectMessage.setTenantNumId(jobCronMessageRightNowSendRequest.getTenantNumId());
            simpleObjectMessage.setDataSign(jobCronMessageRightNowSendRequest.getDataSign());
            sysRocketMqJobLog.setMESSAGE_DETAIL(jsonMapper.toJson(simpleObjectMessage));
            Constants.jobLogSeriesToUuid.put(series, UUID.randomUUID().toString());
            ScheduledSendMsgJob job = new ScheduledSendMsgJob();
            SchedulerUtil.scheduleJob(series.toString(), series.toString(), series.toString(), series.toString(), job.getClass(), jobMessageModel.getCron());
            this.sysRocketMqJobLogDao.insert(sysRocketMqJobLog);
            jobCronMessageRightNowSendResponse.setSeries(series);
        } catch (Exception e) {
            ExceptionUtil.processException(e, jobCronMessageRightNowSendResponse);
        }

        return jobCronMessageRightNowSendResponse;
    }

    public JobCronMessageCancelResponse cancelJobCronMessage(JobCronMessageCancelRequest jobCronMessageCancelRequest) {
        JobCronMessageCancelResponse jobCronMessageCancelResponse = new JobCronMessageCancelResponse();

        try {
            this.sysRocketMqJobLogDao.cancelJobLog(jobCronMessageCancelRequest.getSeries());
        } catch (Exception e) {
            ExceptionUtil.processException(e, jobCronMessageCancelResponse);
        }

        return jobCronMessageCancelResponse;
    }

    public TransMessageRetryResponse retryTransMessage(TransMessageRetryRequest transMessageRetryRequest) {
        TransMessageRetryResponse transMessageRetryResponse = new TransMessageRetryResponse();

        try {
            List list = this.sysTransationFailedLogDao.queryByTopicTag(transMessageRetryRequest.getTopic(), transMessageRetryRequest.getTag(), transMessageRetryRequest.getTenantNumId(), transMessageRetryRequest.getDataSign());
            Iterator iterator = list.iterator();

            while(iterator.hasNext()) {
                SYS_ROCKET_MQ_SEND_LOG sysRocketMqSendLog = (SYS_ROCKET_MQ_SEND_LOG)iterator.next();
                MessageCommonRefoundRequest messageCommonRefoundRequest = new MessageCommonRefoundRequest();
                messageCommonRefoundRequest.setMsgSeries(sysRocketMqSendLog.getSERIES().toString());
                messageCommonRefoundRequest.setSysNumId(Long.valueOf(sysRocketMqSendLog.getFROM_SYSTEM()));
                messageCommonRefoundRequest.setTenantNumId(transMessageRetryRequest.getTenantNumId());
                messageCommonRefoundRequest.setDataSign(transMessageRetryRequest.getDataSign());
                MessageCommonRefoundResponse messageCommonRefoundResponse = this.exportDataService.messageCommonRefound(messageCommonRefoundRequest);
                ArrayList sysRocketMqSendLogList;
                if (messageCommonRefoundResponse.getCode() == MessagePack.OK) {
                    sysRocketMqSendLogList = new ArrayList();
                    sysRocketMqSendLog.setSTEP_ID(Constants.ez);
                    sysRocketMqSendLogList.add(sysRocketMqSendLog);
                    AbstractRocketMqUtil.send(sysRocketMqSendLogList, this.stringRedisTemplate);
                    this.sysTransationFailedLogDao.delete(sysRocketMqSendLog.getSERIES());
                } else {
                    if (messageCommonRefoundResponse.getCode() != ExceptionType.BE40160.getCode()) {
                        throw new RuntimeException("事务回查导入项目失败，原因" + messageCommonRefoundResponse.getMessage());
                    }

                    sysRocketMqSendLogList = new ArrayList();
                    sysRocketMqSendLogList.add(sysRocketMqSendLog);
                    sysRocketMqSendLog.setSTEP_ID(Constants.eE);
                    sysRocketMqSendLog.setMSG_STATUS(Constants.send_cancel);
                    this.sysRocketMqSendLogDao.batchDelete(sysRocketMqSendLogList);
                    this.sysTransationFailedLogDao.delete(sysRocketMqSendLog.getSERIES());
                }
            }
        } catch (Exception e) {
            ExceptionUtil.processException(e, transMessageRetryResponse);
        }

        return transMessageRetryResponse;
    }
}


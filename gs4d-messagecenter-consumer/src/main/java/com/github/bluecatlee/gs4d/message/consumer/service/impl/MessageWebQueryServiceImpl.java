package com.github.bluecatlee.gs4d.message.consumer.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.admin.ConsumeStats;
import com.alibaba.rocketmq.common.admin.OffsetWrapper;
import com.alibaba.rocketmq.common.message.MessageQueue;
import com.alibaba.rocketmq.common.protocol.body.Connection;
import com.alibaba.rocketmq.common.protocol.body.ConsumerConnection;
import com.alibaba.rocketmq.remoting.exception.RemotingException;
import com.alibaba.rocketmq.tools.admin.DefaultMQAdminExt;
import com.fasterxml.jackson.databind.PropertyNamingStrategy.LowerCaseStrategy;
import com.github.bluecatlee.gs4d.common.exception.BusinessException;
import com.github.bluecatlee.gs4d.common.exception.ExceptionType;
import com.github.bluecatlee.gs4d.common.exception.ValidateBusinessException;
import com.github.bluecatlee.gs4d.common.utils.ExceptionUtil;
import com.github.bluecatlee.gs4d.common.utils.JsonUtil;
import com.github.bluecatlee.gs4d.common.utils.MyJsonMapper;
import com.github.bluecatlee.gs4d.common.utils.StringUtil;
import com.github.bluecatlee.gs4d.common.utils.lock.DistributedLock;
import com.github.bluecatlee.gs4d.export.api.request.CommonExcuteBySqlIdRequest;
import com.github.bluecatlee.gs4d.export.api.response.CommonExcuteBySqlIdResponse;
import com.github.bluecatlee.gs4d.export.api.service.ExportDataService;
import com.github.bluecatlee.gs4d.message.api.exception.CodeMessagcenterExceptionType;
import com.github.bluecatlee.gs4d.message.api.model.*;
import com.github.bluecatlee.gs4d.message.api.request.*;
import com.github.bluecatlee.gs4d.message.api.response.*;
import com.github.bluecatlee.gs4d.message.api.service.MessageCenterSendService;
import com.github.bluecatlee.gs4d.message.api.service.MessageCenterWebQueryService;
import com.github.bluecatlee.gs4d.message.api.utils.ExceptionUtils;
import com.github.bluecatlee.gs4d.message.consumer.dao.*;
import com.github.bluecatlee.gs4d.message.consumer.model.PlatformMqDubboConsumer;
import com.github.bluecatlee.gs4d.message.consumer.model.PlatformMqHttpConsumer;
import com.github.bluecatlee.gs4d.message.consumer.model.PlatformMqTopic;
import com.github.bluecatlee.gs4d.message.consumer.utils.*;
import com.github.bluecatlee.gs4d.monitor.api.model.NoticeUserName;
import com.github.bluecatlee.gs4d.monitor.api.request.CodeWatchUserAddRequest;
import com.github.bluecatlee.gs4d.monitor.api.request.MessageCodeUserFindRequest;
import com.github.bluecatlee.gs4d.monitor.api.response.MessageCodeUserFindResponse;
import com.github.bluecatlee.gs4d.monitor.api.service.MonitorMessageCodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

@Service("messageWebQueryService")
public class MessageWebQueryServiceImpl implements MessageCenterWebQueryService {
    
    private static Logger logger = LoggerFactory.getLogger(MessageWebQueryServiceImpl.class);
    
    @Value("#{settings['dataSign']}")
    private Long dataSign;
    
    @Autowired
    private PlatformTransactionManager platformTransactionManager;
    
    @Autowired
    private PlatformMqTopicDao platformMqTopicDao;
    
    @Resource
    private MessageCenterSendService messageSendService;
    
    @Autowired
    private SysRocketMqSendLogDao sysRocketMqSendLogDao;
    
    @Autowired
    private ExArcDocSystemDao exArcDocSystemDao;
    
    @Autowired
    private PlatformMqDubboConsumerDao platformMqDubboConsumerDao;
    
    @Autowired
    private PlatformMqHttpConsumerDao platformMqHttpConsumerDao;
    
    @Autowired
    private SysRocketMqSendLogHistoryDao sysRocketMqSendLogHistoryDao;
    
    @Autowired
    private PlaftformMqTopicManyDao plaftformMqTopicManyDao;
    
    @Autowired
    private PlatformMqTopicRelationDao platformMqTopicRelationDao;
    
    @Autowired
    private RocketMqConfigUtil rocketMqConfigUtil;
    
    @Value("#{settings['nsRemark']}")
    private String nsRemark;
    @Value("#{settings['cipRemark']}")
    private String cipRemark;
    @Value("#{settings['zookeeper.host.port']}")
    private String zookeeperHostPort;
    @Value("#{settings['defaultNamesrv']}")
    private String defaultName;
    
    private static MyJsonMapper jsonMapper = MyJsonMapper.nonEmptyMapper();
    static {
        jsonMapper.getMapper().setPropertyNamingStrategy(LowerCaseStrategy.SNAKE_CASE);
    }
    
    @Autowired
    private ExportDataService exportDataService;
    
    @Autowired(required = false)
    private MonitorMessageCodeService monitorMessageCodeService;
    
    @Autowired
    private SysTransationFailedLogDao sysTransationFailedLogDao;
    
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * ???????????????????????? (ex_arc_doc_system???)
     * @param systemNameFindRequest
     * @return
     */
    public SystemNameFindResponse findSystemName(SystemNameFindRequest systemNameFindRequest) {
        SystemNameFindResponse systemNameFindResponse = new SystemNameFindResponse();

        try {
            HashMap map = new HashMap();
            map.put("TENANT_NUM_ID", systemNameFindRequest.getTenantNumId());
            map.put("DATA_SIGN", systemNameFindRequest.getDataSign());
            CommonExcuteBySqlIdRequest commonExcuteBySqlIdRequest = new CommonExcuteBySqlIdRequest();
            commonExcuteBySqlIdRequest.setDataSign(systemNameFindRequest.getDataSign());
            commonExcuteBySqlIdRequest.setTenantNumId(systemNameFindRequest.getTenantNumId());
            commonExcuteBySqlIdRequest.setSqlId(Constants.queryAllSystemNameSqlId);
            commonExcuteBySqlIdRequest.setInputParam(map);
            CommonExcuteBySqlIdResponse commonExcuteBySqlIdResponse = this.exportDataService.commonExcuteBySqlId(commonExcuteBySqlIdRequest);
            ExceptionUtil.checkDubboException(commonExcuteBySqlIdResponse);
            List list = (List) JsonUtil.fromJson(JsonUtil.toJson(commonExcuteBySqlIdResponse.getResults()), List.class);
            if (list == null || list.size() <= 0) {
                logger.error("??????????????????????????????????????????");
                throw new BusinessException(Constants.SUB_SYSTEM, CodeMessagcenterExceptionType.BSE119401, "???????????????????????????????????????");
            }

            systemNameFindResponse.setSystemNames(list);
        } catch (Exception e) {
            ExceptionUtil.processException(e, systemNameFindResponse);
        }

        return systemNameFindResponse;
    }

    /**
     * ??????????????????
     * @param messageDetailRequest
     * @return
     */
    public MessageDetailResponse findMessageDetail(MessageDetailRequest messageDetailRequest) {
        MessageDetailResponse messageDetailResponse = new MessageDetailResponse();
        DefaultMQAdminExt mqAdminExt = null;

        try {
            mqAdminExt = new DefaultMQAdminExt();
            mqAdminExt.setNamesrvAddr(this.defaultName);
            mqAdminExt.setInstanceName(Long.toString(System.currentTimeMillis()));
            mqAdminExt.setHeartbeatBrokerInterval(30000);
            mqAdminExt.start();
            if (messageDetailRequest.getConsumerSuccess() != null && messageDetailRequest.getConsumerSuccess().equals("S")) {
                messageDetailRequest.setConsumerSuccess("");
            }

            String condition = this.assembleCondition(messageDetailRequest);
            Long count = this.sysRocketMqSendLogDao.queryCount(condition);
            condition = condition + " order by create_dtme desc limit " + (messageDetailRequest.getPageNo() - 1L) * messageDetailRequest.getPageCount() + " , " + messageDetailRequest.getPageCount();
            List<MessageDetails> messageDetailsList = this.sysRocketMqSendLogDao.query(condition);

            for(int i = 0; i < messageDetailsList.size(); ++i) {
                MessageDetails messageDetails = (MessageDetails)messageDetailsList.get(i);
                if (StringUtil.isAllNotNullOrBlank(new String[]{messageDetails.getSendType()})) {
                    String sendType = (String) Constants.sendTypeDetails.get(Integer.valueOf(messageDetails.getSendType()));
                    messageDetails.setSendType(sendType);
                }

                List platformMqTopicList = this.platformMqTopicDao.queryByTopicTag(((MessageDetails)messageDetailsList.get(i)).getTopic(), ((MessageDetails)messageDetailsList.get(i)).getTag(), messageDetailRequest.getTenantNumId().toString());
                if (platformMqTopicList != null && !platformMqTopicList.isEmpty()) {
                    messageDetails.setRemark(((PlatformMqTopic)platformMqTopicList.get(0)).getREMARK());
                }

                if ("?????????".equals(messageDetails.getMessageIsConsumer())) {
                    String topicTag = messageDetails.getTopic() + "-" + messageDetails.getTag();
                    Boolean hasConsumer = this.hasConsumer(mqAdminExt, topicTag);
                    if (!hasConsumer) {
                        messageDetails.setUnConsumeReason("tag:" + messageDetails.getTag() + "???????????????mq???,??????platform_mq_topic????????????tag,???????????????????????????");
                    } else {
                        Long overstock = this.calcOverstock(mqAdminExt, topicTag);
                        if (overstock > 10L) {
                            messageDetails.setUnConsumeReason("tag:" + messageDetails.getTag() + "???????????????,??????????????????" + overstock + ",??????????????????????????????????????????");
                        } else if (StringUtil.isAllNotNullOrBlank(new String[]{messageDetails.getMessageId()})) {
                            messageDetails.setUnConsumeReason("tag:" + messageDetails.getTag() + "???????????????,???????????????????????????????????????");
                        }
                    }
                }
            }

            messageDetailResponse.setMessageList(messageDetailsList);
            messageDetailResponse.setTotal(count);
            logger.info("???????????????????????????:" + messageDetailResponse.toJson());
        } catch (Exception e) {
            ExceptionUtil.processException(e, messageDetailResponse);
            logger.error("???????????????????????????" + e.getMessage(), e);
        } finally {
            if (mqAdminExt != null) {
                mqAdminExt.shutdown();
            }

        }

        return messageDetailResponse;
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

    private String assembleCondition(MessageDetailRequest messageDetailRequest) {
        String condition = "where tenant_num_id=" + messageDetailRequest.getTenantNumId() + " and data_sign=" + messageDetailRequest.getDataSign() + " ";
        if (StringUtil.isAllNotNullOrBlank(new String[]{messageDetailRequest.getTopic()})) {
            condition = condition + " and message_topic ='" + messageDetailRequest.getTopic() + "'";
        }

        if (StringUtil.isAllNotNullOrBlank(new String[]{messageDetailRequest.getTag()})) {
            condition = condition + " and message_tag ='" + messageDetailRequest.getTag() + "'";
        }

        if (StringUtil.isAllNotNullOrBlank(new Object[]{messageDetailRequest.getSeries()})) {
            condition = condition + " and series =" + messageDetailRequest.getSeries();
        }

        if (StringUtil.isAllNotNullOrBlank(new String[]{messageDetailRequest.getMessageKey()})) {
            condition = condition + " and message_key ='" + messageDetailRequest.getMessageKey() + "'";
        }

        if (messageDetailRequest.getConsumerSuccess() != null) {
            condition = condition + " and consumer_success ='" + messageDetailRequest.getConsumerSuccess() + "'";
        }

        if (StringUtil.isAllNotNullOrBlank(new Object[]{messageDetailRequest.getRetryTimes()})) {
            condition = condition + " and retry_times >='" + messageDetailRequest.getRetryTimes() + "'";
        }

        String startTime = "";
        String endTime = "";
        if (messageDetailRequest.getCreateTime() != null && messageDetailRequest.getEndTime() != null) {
            try {
                startTime = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(messageDetailRequest.getCreateTime());
                endTime = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(messageDetailRequest.getEndTime());
            } catch (Exception e) {
                throw new ValidateBusinessException(Constants.SUB_SYSTEM, ExceptionType.VBE20031, "??????????????????????????????yyyy-MM-dd HH-mm-ss???????????????");
            }

            condition = condition + " and create_dtme between '" + startTime + "' and '" + endTime + "'";
        }

        return condition;
    }

    /**
     * ????????????????????????
     * @param messageDetailRequest
     * @return
     */
    public MessageDetailResponse findHistoryMessageDetail(MessageDetailRequest messageDetailRequest) {
        MessageDetailResponse messageDetailResponse = new MessageDetailResponse();

        try {
            messageDetailRequest.setConsumerSuccess("Y");
            String condition = this.assembleCondition(messageDetailRequest);
            Long count = this.sysRocketMqSendLogDao.queryHistoryCount(condition);
            condition = condition + " order by create_dtme desc limit " + (messageDetailRequest.getPageNo() - 1L) * messageDetailRequest.getPageCount() + " , " + messageDetailRequest.getPageCount();
            List<MessageDetails> messageDetailsList = this.sysRocketMqSendLogDao.queryHistory(condition);

            for(int i = 0; i < messageDetailsList.size(); ++i) {
                MessageDetails messageDetails = (MessageDetails)messageDetailsList.get(i);
                if (StringUtil.isAllNotNullOrBlank(new String[]{messageDetails.getSendType()})) {
                    String sendType = (String) Constants.sendTypeDetails.get(Integer.valueOf(messageDetails.getSendType()));
                    messageDetails.setSendType(sendType);
                }

                List platformMqTopicList = this.platformMqTopicDao.queryByTopicTag(((MessageDetails)messageDetailsList.get(i)).getTopic(), ((MessageDetails)messageDetailsList.get(i)).getTag(), messageDetailRequest.getTenantNumId().toString());
                if (platformMqTopicList != null && !platformMqTopicList.isEmpty()) {
                    messageDetails.setRemark(((PlatformMqTopic)platformMqTopicList.get(0)).getREMARK());
                }
            }

            messageDetailResponse.setMessageList(messageDetailsList);
            messageDetailResponse.setTotal(count);
        } catch (Exception e) {
            ExceptionUtil.processException(e, messageDetailResponse);
            logger.error("???????????????????????????" + e.getMessage(), e);
        }

        return messageDetailResponse;
    }

    /**
     * ??????topic????????????
     * @param topicConfigRequest
     * @return
     */
    public TopicConfigResponse getTopicConfig(TopicConfigRequest topicConfigRequest) {
        TopicConfigResponse topicConfigResponse = new TopicConfigResponse();
        DefaultMQAdminExt mqAdminExt = null;
        String message = "??????";

        try {
            mqAdminExt = new DefaultMQAdminExt();
            mqAdminExt.setNamesrvAddr(this.defaultName);
            mqAdminExt.setInstanceName(Long.toString(System.currentTimeMillis()));
            mqAdminExt.setHeartbeatBrokerInterval(30000);
            mqAdminExt.start();
            List<TopicConfigModel> topicConfigModels = this.platformMqTopicDao.queryTopicConfigModelsBySeries(topicConfigRequest.getSeries());
            TopicConfigModel topicConfigModel = (TopicConfigModel)topicConfigModels.get(0);
            if (!StringUtil.isAllNotNullOrBlank(new String[]{topicConfigModel.getConsumerType()})) {
                topicConfigModel.setConsumerType("");
                message = "??????topic???????????????????????????????????????";
            } else if (topicConfigModel.getConsumerType().equals(Constants.CONSUMER_TYPE_DUBBO)) {      // dubbo?????????
                PlatformMqDubboConsumer platformMqDubboConsumer = this.platformMqDubboConsumerDao.queryBySeries(topicConfigModel.getConsumerSeries());
                if (platformMqDubboConsumer != null) {
                    topicConfigModel.setDubboCreatePerson(platformMqDubboConsumer.getCREATE_USER_NAME());
                }

                topicConfigModel.setDubboGroup(platformMqDubboConsumer.getDUBBO_GROUP());
                topicConfigModel.setDubboMethod(platformMqDubboConsumer.getMETHOD_NAME());
                topicConfigModel.setDubboParam(platformMqDubboConsumer.getPARAM_ENTITY());
                topicConfigModel.setDubboRemark(platformMqDubboConsumer.getREMARK());
                topicConfigModel.setDubboSeries(platformMqDubboConsumer.getSERIES());
                topicConfigModel.setDubboService(platformMqDubboConsumer.getSERVICE_NAME());
                topicConfigModel.setDubboDirUrl(platformMqDubboConsumer.getDIRECT_ADR());
                topicConfigModel.setZkAddress(this.zookeeperHostPort);
                topicConfigModel.setZkDevelopAddress(this.zookeeperHostPort);
                topicConfigModel.setZkTestAddress(this.zookeeperHostPort);
            } else {    // http?????????
                PlatformMqHttpConsumer platformMqHttpConsumer = this.platformMqHttpConsumerDao.queryBySeries(topicConfigModel.getConsumerSeries());
                if (platformMqHttpConsumer != null) {
                    topicConfigModel.setHttpHead(platformMqHttpConsumer.getHTTP_HEAD());
                }

                topicConfigModel.setHttpRemark(platformMqHttpConsumer.getRemark());
                topicConfigModel.setHttpSeries(platformMqHttpConsumer.getSERIES());
                topicConfigModel.setUrl(platformMqHttpConsumer.getURL());
                topicConfigModel.setUrlDevelop(platformMqHttpConsumer.getURL_DEVELOP());
                topicConfigModel.setUrlTest(platformMqHttpConsumer.getURL_TEST());
                topicConfigModel.setParamName(platformMqHttpConsumer.getPARAM_NAME());
                topicConfigModel.setHttpUserName(platformMqHttpConsumer.getCREATE_USER_NAME());
            }

            if (StringUtil.isAllNotNullOrBlank(new String[]{topicConfigModel.getWetherListenBinLogTopic()}) && "100".equals(topicConfigModel.getWetherListenBinLogTopic())) {
                topicConfigModel.setWetherListenBinLogTopic("Y");
            } else {
                topicConfigModel.setWetherListenBinLogTopic("N");
            }

            topicConfigModel.setRetryInterval(Constants.y(topicConfigModel.getRetryInterval()));
            topicConfigModel.setConsumerInterval(Constants.y(topicConfigModel.getConsumerInterval()));
            HashMap map = new HashMap();
            map.put("TENANT_NUM_ID", topicConfigRequest.getTenantNumId());
            map.put("DATA_SIGN", topicConfigRequest.getDataSign());
            map.put("SYSTEM_NUM_ID", topicConfigModel.getSystemNumId());
            CommonExcuteBySqlIdRequest commonExcuteBySqlIdRequest = new CommonExcuteBySqlIdRequest();
            commonExcuteBySqlIdRequest.setDataSign(topicConfigRequest.getDataSign());
            commonExcuteBySqlIdRequest.setTenantNumId(topicConfigRequest.getTenantNumId());
            commonExcuteBySqlIdRequest.setSqlId(Constants.querySystemNameBySystemNumIdSqlId);
            commonExcuteBySqlIdRequest.setInputParam(map);
            CommonExcuteBySqlIdResponse commonExcuteBySqlIdResponse = this.exportDataService.commonExcuteBySqlId(commonExcuteBySqlIdRequest);
            ExceptionUtil.checkDubboException(commonExcuteBySqlIdResponse);
            List results = commonExcuteBySqlIdResponse.getResults();
            if (results != null && results.size() > 0) {
                topicConfigModel.setSystemName(((Map)results.get(0)).get("system_name").toString());
            } else {
                logger.error("??????????????????????????????????????????");
                topicConfigModel.setSystemName("??????????????????topic");
            }

            String topicTag = topicConfigModel.getTopic() + "-" + topicConfigModel.getTag();
            Boolean hasConsumer = this.hasConsumer(mqAdminExt, topicTag);
            if (!hasConsumer) {
                message = "tag:" + topicConfigModel.getTag() + "???????????????mq???,??????platform_mq_topic????????????tag,???????????????????????????";
            }

            topicConfigModel.setConsumerInfo(message);
            if ("0".equals(topicConfigModel.getZkDataSign())) {
                topicConfigModel.setZkDataSign("N");
            } else {
                topicConfigModel.setZkDataSign("Y");
            }

            MessageCodeUserFindRequest messageCodeUserFindRequest = new MessageCodeUserFindRequest();
            messageCodeUserFindRequest.setErrorCodeId(Constants.getErrorCode(topicConfigModel.getSeries(), Constants.eB));
            messageCodeUserFindRequest.setTopicId(Long.valueOf(topicConfigModel.getSeries()));
            messageCodeUserFindRequest.setDataSign(topicConfigRequest.getDataSign());
            messageCodeUserFindRequest.setTenantNumId(topicConfigRequest.getTenantNumId());
            MessageCodeUserFindResponse messageCodeUserFindResponse = this.monitorMessageCodeService.findMessageCodeUser(messageCodeUserFindRequest);
            if (messageCodeUserFindResponse.getWatchUser() != null) {
                List watchUsers = messageCodeUserFindResponse.getWatchUser();
                ArrayList userIdList = new ArrayList();
                Iterator iterator = watchUsers.iterator();

                while(iterator.hasNext()) {
                    NoticeUserName noticeUserName = (NoticeUserName)iterator.next();
                    userIdList.add(noticeUserName.getUserId());
                }

                topicConfigModel.setUsers(userIdList);
            }

            topicConfigResponse.setTopicConfigModel(topicConfigModel);
        } catch (Exception e) {
            ExceptionUtil.processException(e, topicConfigResponse);
            logger.error("??????topic????????????" + e.getMessage(), e);
        } finally {
            if (mqAdminExt != null) {
                mqAdminExt.shutdown();
            }

        }

        return topicConfigResponse;
    }

    /**
     * ????????????topic?????????????????????
     * @param allTopicMessageRequest
     * @return
     */
    public AllTopicMessageResponse findAllTopicMessage(AllTopicMessageRequest allTopicMessageRequest) {
        AllTopicMessageResponse allTopicMessageResponse = new AllTopicMessageResponse();
        String tags = "";
        String startTime = "";
        String endTime = "";

        try {
            if (allTopicMessageRequest.getCreateTime() != null && allTopicMessageRequest.getEndTime() != null) {
                startTime = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(allTopicMessageRequest.getCreateTime());
                endTime = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(allTopicMessageRequest.getEndTime());
            }

            List<TopicConfigModel> topicConfigModelList = this.platformMqTopicDao.query(allTopicMessageRequest.getTenantNumId(), allTopicMessageRequest.getTopic(), allTopicMessageRequest.getTag(), allTopicMessageRequest.getDataSign());
            Integer offset = (allTopicMessageRequest.getPageNo() - 1) * allTopicMessageRequest.getPageCount();
            Integer rows = allTopicMessageRequest.getPageNo() * allTopicMessageRequest.getPageCount();
            if (offset > topicConfigModelList.size()) {
                allTopicMessageResponse.setMessageLists(new ArrayList());
                return allTopicMessageResponse;
            }

            HashMap tag2TopicConfigModelMap = new HashMap();

            for(int i = offset; i < rows; ++i) {
                try {
                    TopicConfigModel topicConfigModel = (TopicConfigModel)topicConfigModelList.get(i);
                    tag2TopicConfigModelMap.put(topicConfigModel.getTag(), topicConfigModel);
                    tags = tags + "'" + topicConfigModel.getTag() + "',";
                } catch (Exception e) {
                    break;
                }
            }

            tags = tags.substring(0, tags.length() - 1);
            List<MessageTopicDetailModel> messageTopicDetailModelList = this.sysRocketMqSendLogDao.count(startTime, endTime, allTopicMessageRequest.getTenantNumId(), allTopicMessageRequest.getDataSign(), tags);
            List<MessageTopicDetailModel> messageTopicDetailModelHistoryList = this.sysRocketMqSendLogHistoryDao.query(startTime, endTime, allTopicMessageRequest.getTenantNumId(), allTopicMessageRequest.getDataSign(), tags);
            Iterator messageTopicDetailModelListIteraor = messageTopicDetailModelList.iterator();

            MessageTopicDetailModel messageTopicDetailModel;
            int index;
            while(messageTopicDetailModelListIteraor.hasNext()) {
                messageTopicDetailModel = (MessageTopicDetailModel)messageTopicDetailModelListIteraor.next();
                int k = 0;

                for(index = 0; index < messageTopicDetailModelHistoryList.size(); ++index) {
                    if ((messageTopicDetailModelHistoryList.get(index)).getTag().equals(messageTopicDetailModel.getTag())) {
                        messageTopicDetailModel.setMessageNum(messageTopicDetailModel.getMessageNum() + (messageTopicDetailModelHistoryList.get(index)).getMessageNum());
                        messageTopicDetailModel.setMessageConfirmNum(messageTopicDetailModel.getMessageConfirmNum() + (messageTopicDetailModelHistoryList.get(index)).getMessageNum());
                        messageTopicDetailModel.setMessageCounsumerSuccessNum((messageTopicDetailModelHistoryList.get(index)).getMessageCounsumerSuccessNum());
                        messageTopicDetailModel.setMessageCancelNum((messageTopicDetailModelHistoryList.get(index)).getMessageCancelNum());
                        if ((messageTopicDetailModelHistoryList.get(index)).getAverageConsumerTime() == null) {
                            messageTopicDetailModel.setAverageConsumerTime("0");
                        } else {
                            messageTopicDetailModel.setAverageConsumerTime((messageTopicDetailModelHistoryList.get(index)).getAverageConsumerTime());
                        }
                        break;
                    }

                    ++k;
                }

                if (k == messageTopicDetailModelHistoryList.size()) {
                    messageTopicDetailModel.setMessageCounsumerSuccessNum(0L);
                    messageTopicDetailModel.setMessageCancelNum(0L);
                    messageTopicDetailModel.setAverageConsumerTime("0");
                }

                messageTopicDetailModel.setBusinessRemark(((TopicConfigModel)tag2TopicConfigModelMap.get(messageTopicDetailModel.getTag())).getRemark());
                messageTopicDetailModel.setRetryTime(((TopicConfigModel)tag2TopicConfigModelMap.get(messageTopicDetailModel.getTag())).getRetryTimes());
                messageTopicDetailModel.setCreateTime(startTime);
                messageTopicDetailModel.setUpdateTime(endTime);
                Long messageIsTryingNow = this.sysRocketMqSendLogDao.getMessageIsTryingNow(messageTopicDetailModel.getTopic(), messageTopicDetailModel.getTag(), messageTopicDetailModel.getRetryTime(), allTopicMessageRequest.getTenantNumId(), allTopicMessageRequest.getDataSign());
                messageTopicDetailModel.setMessageIsTryingNow(messageIsTryingNow);
            }

            Iterator messageTopicDetailModelHistoryListIteraor = messageTopicDetailModelHistoryList.iterator();

            MessageTopicDetailModel messageTopicDetailModelH;
            while(messageTopicDetailModelHistoryListIteraor.hasNext()) {
                messageTopicDetailModel = (MessageTopicDetailModel)messageTopicDetailModelHistoryListIteraor.next();
                Boolean flag = true;

                for(index = 0; index < messageTopicDetailModelList.size(); ++index) {
                    if (messageTopicDetailModel.getTag().equals(((MessageTopicDetailModel)messageTopicDetailModelList.get(index)).getTag())) {
                        flag = false;
                        break;
                    }
                }

                if (flag) {
                    messageTopicDetailModelH = new MessageTopicDetailModel();
                    messageTopicDetailModelH.setMessageNum(messageTopicDetailModel.getMessageNum());
                    messageTopicDetailModelH.setMessageConfirmNum(messageTopicDetailModel.getMessageNum());
                    messageTopicDetailModelH.setMessageCounsumerSuccessNum(messageTopicDetailModel.getMessageCounsumerSuccessNum());
                    messageTopicDetailModelH.setMessageCancelNum(messageTopicDetailModel.getMessageCancelNum());
                    if (messageTopicDetailModel.getAverageConsumerTime() == null) {
                        messageTopicDetailModelH.setAverageConsumerTime("0");
                    } else {
                        messageTopicDetailModelH.setAverageConsumerTime(messageTopicDetailModel.getAverageConsumerTime());
                    }

                    messageTopicDetailModelH.setBusinessRemark(((TopicConfigModel)tag2TopicConfigModelMap.get(messageTopicDetailModel.getTag())).getRemark());
                    messageTopicDetailModelH.setRetryTime(((TopicConfigModel)tag2TopicConfigModelMap.get(messageTopicDetailModel.getTag())).getRetryTimes());
                    messageTopicDetailModelH.setCreateTime(startTime);
                    messageTopicDetailModelH.setUpdateTime(endTime);
                    messageTopicDetailModelH.setMessageIsTryingNow(0L);
                    messageTopicDetailModelH.setMessageCounsumerFailedNum(0L);
                    messageTopicDetailModelH.setMessageNoConfirmNum(0L);
                    messageTopicDetailModelH.setMessageNoCounsumerNum(0L);
                    messageTopicDetailModelH.setTopic(messageTopicDetailModel.getTopic());
                    messageTopicDetailModelH.setTag(messageTopicDetailModel.getTag());

                    messageTopicDetailModelList.add(messageTopicDetailModelH);
                }
            }

            if (messageTopicDetailModelList.size() < tag2TopicConfigModelMap.size()) {
                ArrayList tagList = new ArrayList();

                for(int i = 0; i < messageTopicDetailModelList.size(); ++i) {
                    tagList.add(((MessageTopicDetailModel)messageTopicDetailModelList.get(i)).getTag());
                }

                Iterator tag2TopicConfigModelMapIterator = tag2TopicConfigModelMap.entrySet().iterator();

                while(tag2TopicConfigModelMapIterator.hasNext()) {
                    Entry entry = (Entry)tag2TopicConfigModelMapIterator.next();
                    if (!tagList.contains(entry.getKey())) {
                        messageTopicDetailModelH = new MessageTopicDetailModel();
                        messageTopicDetailModelH.setTopic(((TopicConfigModel)entry.getValue()).getTopic());
                        messageTopicDetailModelH.setTag(((TopicConfigModel)entry.getValue()).getTag());
                        messageTopicDetailModelH.setAverageConsumerTime("0");
                        messageTopicDetailModelH.setMessageConfirmNum(0L);
                        messageTopicDetailModelH.setMessageCounsumerFailedNum(0L);
                        messageTopicDetailModelH.setMessageCounsumerSuccessNum(0L);
                        messageTopicDetailModelH.setMessageNoConfirmNum(0L);
                        messageTopicDetailModelH.setMessageNoCounsumerNum(0L);
                        messageTopicDetailModelH.setMessageNum(0L);
                        messageTopicDetailModelH.setBusinessRemark(((TopicConfigModel)tag2TopicConfigModelMap.get(messageTopicDetailModelH.getTag())).getRemark());
                        messageTopicDetailModelH.setRetryTime(((TopicConfigModel)tag2TopicConfigModelMap.get(messageTopicDetailModelH.getTag())).getRetryTimes());
                        messageTopicDetailModelH.setMessageIsTryingNow(0L);
                        messageTopicDetailModelH.setCreateTime(startTime);
                        messageTopicDetailModelH.setUpdateTime(endTime);
                        messageTopicDetailModelH.setMessageCancelNum(0L);

                        messageTopicDetailModelList.add(messageTopicDetailModelH);
                    }
                }
            }

            allTopicMessageResponse.setMessageLists(messageTopicDetailModelList);
            allTopicMessageResponse.setTotal(topicConfigModelList.size());
        } catch (Exception e) {
            ExceptionUtil.processException(e, allTopicMessageResponse);
        }

        return allTopicMessageResponse;
    }

    /**
     * ????????????
     * @param pageCount
     * @param totalCount
     * @return
     */
    private Integer countPage(Integer pageCount, Integer totalCount) {
        Integer page = totalCount / pageCount;
        if (totalCount % pageCount > 0 && totalCount > pageCount) {
            page = page + 1;
        } else if (totalCount < pageCount) {
            page = 1;
        }

        return page;
    }

    /**
     * ?????????????????????????????????????????????????????????
     * @param consumerSeriesUpdateRequest
     * @return
     */
    public ConsumerSeriesUpdateResponse updateConsumerSeries(ConsumerSeriesUpdateRequest consumerSeriesUpdateRequest) {
        ConsumerSeriesUpdateResponse consumerSeriesUpdateResponse = new ConsumerSeriesUpdateResponse();

        try {
            logger.info("??????mq_topic????????????????????????:" + jsonMapper.toJson(consumerSeriesUpdateRequest));
            Long consumerType = 0L;
            if (consumerSeriesUpdateRequest.getConsumerType().equals(Constants.CONSUMER_TYPE_DUBBO)) {
                consumerType = 1L;
            } else {
                if (!consumerSeriesUpdateRequest.getConsumerType().equals(Constants.CONSUMER_TYPE_HTTP)) {
                    throw new ValidateBusinessException(Constants.SUB_SYSTEM, ExceptionType.VBE20031, "????????????????????????dubbo??????http");
                }
                consumerType = 2L;
            }

            this.platformMqTopicDao.updateConsumerSeriesAndConsumerType(consumerSeriesUpdateRequest.getConsumerSeries(), consumerSeriesUpdateRequest.getTag(), consumerSeriesUpdateRequest.getTopic(), consumerSeriesUpdateRequest.getTenantNumId(), consumerType);
        } catch (Exception e) {
            ExceptionUtil.processException(e, consumerSeriesUpdateResponse);
            logger.error("??????dubbo?????????????????????" + e.getMessage(), e);
        }

        logger.info("??????mq_topic????????????????????????:" + jsonMapper.toJson(consumerSeriesUpdateResponse));
        return consumerSeriesUpdateResponse;
    }

    /**
     * ??????http?????????
     * @param httpConsumerMessageGetRequest
     * @return
     */
    public HttpConsumerMessageGetResponse getHttpConsumerMessage(HttpConsumerMessageGetRequest httpConsumerMessageGetRequest) {
        HttpConsumerMessageGetResponse httpConsumerMessageGetResponse = new HttpConsumerMessageGetResponse();

        try {
            logger.info("??????Http??????????????????????????????:" + jsonMapper.toJson(httpConsumerMessageGetRequest));
            List httpConsumerList = this.platformMqHttpConsumerDao.querySelectiveBySeries(httpConsumerMessageGetRequest.getConsumerSeries());
            httpConsumerMessageGetResponse.setHttpList(httpConsumerList);
        } catch (Exception e) {
            ExceptionUtil.processException(e, httpConsumerMessageGetResponse);
            logger.error("??????http?????????????????????" + e.getMessage(), e);
        }

        logger.info("??????Http??????????????????????????????:" + jsonMapper.toJson(httpConsumerMessageGetResponse));
        return httpConsumerMessageGetResponse;
    }

    /**
     * ??????dubbo?????????
     * @param dubboConsumerMessageGetRequest
     * @return
     */
    public DubboConsumerMessageGetResponse getDubboConsumerMessage(DubboConsumerMessageGetRequest dubboConsumerMessageGetRequest) {
        DubboConsumerMessageGetResponse dubboConsumerMessageGetResponse = new DubboConsumerMessageGetResponse();

        try {
            logger.info("??????dubboconsumer???????????????:" + jsonMapper.toJson(dubboConsumerMessageGetRequest));
            List dubboConsumerList = this.platformMqDubboConsumerDao.querySelectiveBySeries(dubboConsumerMessageGetRequest.getConsumerSeries());
            dubboConsumerMessageGetResponse.setDubboList(dubboConsumerList);
        } catch (Exception e) {
            ExceptionUtil.processException(e, dubboConsumerMessageGetResponse);
            logger.error("??????dubbo?????????????????????" + e.getMessage(), e);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("end getDubboConsumerMessage response:{}", dubboConsumerMessageGetResponse.toLowerCaseJson());
        }

        logger.info("??????dubboconsumer???????????????:" + jsonMapper.toJson(dubboConsumerMessageGetResponse));
        return dubboConsumerMessageGetResponse;
    }

    /**
     * ??????topic??????
     * @param platformMqTopicMessageUpdateRequest
     * @return
     */
    public PlatformMqTopicMessageUpdateResponse updatePlatformMqTopicMessage(PlatformMqTopicMessageUpdateRequest platformMqTopicMessageUpdateRequest) {
        PlatformMqTopicMessageUpdateResponse platformMqTopicMessageUpdateResponse = new PlatformMqTopicMessageUpdateResponse();

        try {
            logger.info("???????????????????????????:" + jsonMapper.toJson(platformMqTopicMessageUpdateRequest));
            String retryTime = null;
            if (StringUtil.isAllNotNullOrBlank(new String[]{platformMqTopicMessageUpdateRequest.getCorrectCodes()}) && platformMqTopicMessageUpdateRequest.getCorrectCodes().contains("???")) {
                throw new ValidateBusinessException(Constants.SUB_SYSTEM, ExceptionType.VBE20031, "??????code????????????????????????,??????");
            }

            if (platformMqTopicMessageUpdateRequest.getUsers() == null || platformMqTopicMessageUpdateRequest.getUsers().isEmpty()) {
                throw new ValidateBusinessException(Constants.SUB_SYSTEM, ExceptionType.VBE20031, "????????????????????????");
            }

            if (StringUtil.isAllNotNullOrBlank(new String[]{platformMqTopicMessageUpdateRequest.getRetryInterval()})) {
                retryTime = Constants.index2RetryTime(platformMqTopicMessageUpdateRequest.getRetryInterval());
            }

            String consumerTime = null;
            if (StringUtil.isAllNotNullOrBlank(new String[]{platformMqTopicMessageUpdateRequest.getConsumerInterval()})) {
                consumerTime = Constants.index2RetryTime(platformMqTopicMessageUpdateRequest.getConsumerInterval().toString());
            }

            String systemName = "";
            if (platformMqTopicMessageUpdateRequest.getZkDataSign() != null && !platformMqTopicMessageUpdateRequest.getZkDataSign().equals("N")) {
                platformMqTopicMessageUpdateRequest.setZkDataSign("1");
            } else {
                platformMqTopicMessageUpdateRequest.setZkDataSign("0");
            }

            HashMap map = new HashMap();
            map.put("TENANT_NUM_ID", platformMqTopicMessageUpdateRequest.getTenantNumId());
            map.put("DATA_SIGN", platformMqTopicMessageUpdateRequest.getDataSign());
            map.put("SYSTEM_NUM_ID", platformMqTopicMessageUpdateRequest.getSystemNumId());
            CommonExcuteBySqlIdRequest commonExcuteBySqlIdRequest = new CommonExcuteBySqlIdRequest();
            commonExcuteBySqlIdRequest.setDataSign(platformMqTopicMessageUpdateRequest.getDataSign());
            commonExcuteBySqlIdRequest.setTenantNumId(platformMqTopicMessageUpdateRequest.getTenantNumId());
            commonExcuteBySqlIdRequest.setSqlId(Constants.querySystemNameBySystemNumIdSqlId);
            commonExcuteBySqlIdRequest.setInputParam(map);
            CommonExcuteBySqlIdResponse commonExcuteBySqlIdResponse = this.exportDataService.commonExcuteBySqlId(commonExcuteBySqlIdRequest);
            ExceptionUtil.checkDubboException(commonExcuteBySqlIdResponse);
            List results = commonExcuteBySqlIdResponse.getResults();
            if (results == null || results.size() <= 0) {
                logger.error("??????????????????????????????????????????");
                throw new BusinessException(Constants.SUB_SYSTEM, CodeMessagcenterExceptionType.BSE119401, "???????????????????????????????????????");
            }

            systemName = ((Map)results.get(0)).get("system_name").toString();
            this.platformMqTopicDao.update(platformMqTopicMessageUpdateRequest.getTopic(), platformMqTopicMessageUpdateRequest.getTag(), platformMqTopicMessageUpdateRequest.getWetherListenBinLogTopic(), consumerTime, platformMqTopicMessageUpdateRequest.getConsumerThreadNum(), platformMqTopicMessageUpdateRequest.getCreateUser(), platformMqTopicMessageUpdateRequest.getIsDistinct(), platformMqTopicMessageUpdateRequest.getMessBatchNum(), platformMqTopicMessageUpdateRequest.getRemark(), platformMqTopicMessageUpdateRequest.getRetryTimes(), platformMqTopicMessageUpdateRequest.getRetryDevelopTimes(), platformMqTopicMessageUpdateRequest.getRetryTestTimes(), platformMqTopicMessageUpdateRequest.getSystemNumId(), platformMqTopicMessageUpdateRequest.getTaskTarget(), platformMqTopicMessageUpdateRequest.getTenantNumId(), platformMqTopicMessageUpdateRequest.getWetherOrderMessage(), retryTime, platformMqTopicMessageUpdateRequest.getWetherInsertdb(), platformMqTopicMessageUpdateRequest.getCorrectCodes(), platformMqTopicMessageUpdateRequest.getMqQueue(), platformMqTopicMessageUpdateRequest.getWetherHandleFailedmess(), platformMqTopicMessageUpdateRequest.getRetryMax(), platformMqTopicMessageUpdateRequest.getZkDataSign(), platformMqTopicMessageUpdateRequest.getConsumerPullDelay(), systemName);
            CodeWatchUserAddRequest codeWatchUserAddRequest = new CodeWatchUserAddRequest();
            ArrayList users = new ArrayList();
            Iterator usersIterator = platformMqTopicMessageUpdateRequest.getUsers().iterator();

            while(usersIterator.hasNext()) {
                Long user = (Long)usersIterator.next();
                users.add(user.toString());
            }

            codeWatchUserAddRequest.setUsers(users);
            codeWatchUserAddRequest.setSystemId(platformMqTopicMessageUpdateRequest.getSystemNumId());
            codeWatchUserAddRequest.setSmsWatch("Y");
            codeWatchUserAddRequest.setEmailWatch("Y");
            codeWatchUserAddRequest.setDataSign(platformMqTopicMessageUpdateRequest.getDataSign());
            codeWatchUserAddRequest.setTenantNumId(platformMqTopicMessageUpdateRequest.getTenantNumId());
            codeWatchUserAddRequest.setErrorCodeId(Constants.getErrorCode(platformMqTopicMessageUpdateRequest.getSeries().toString(), Constants.eB));
            codeWatchUserAddRequest.setTopicName(platformMqTopicMessageUpdateRequest.getTopic());
            codeWatchUserAddRequest.setTopicSeries(platformMqTopicMessageUpdateRequest.getSeries());
            codeWatchUserAddRequest.setSystemName(systemName);
            codeWatchUserAddRequest.setErrorName("????????????!");
            codeWatchUserAddRequest.setNoticeTimeInterval(5);
            this.monitorMessageCodeService.addMessageCodeUser(codeWatchUserAddRequest);
        } catch (Exception e) {
            ExceptionUtil.processException(e, platformMqTopicMessageUpdateResponse);
            logger.error("??????topic????????????" + e.getMessage(), e);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("end updatePlatformMqTopicMessage response:{}", platformMqTopicMessageUpdateResponse.toLowerCaseJson());
        }

        logger.info("???????????????????????????:" + jsonMapper.toJson(platformMqTopicMessageUpdateResponse));
        return platformMqTopicMessageUpdateResponse;
    }

    /**
     * ????????????
     * @param messsageRetrySendRequest
     * @return
     */
    public MesssageRetrySendResponse retrySendMessage(MesssageRetrySendRequest messsageRetrySendRequest) {
        MesssageRetrySendResponse messsageRetrySendResponse = new MesssageRetrySendResponse();

        try {
            String[] seriesArr = messsageRetrySendRequest.getSeriesList().split(",");

            for(int i = 0; i < seriesArr.length; ++i) {
                MsgRetrySendRequest msgRetrySendRequest = new MsgRetrySendRequest();
                msgRetrySendRequest.setSeries(Long.valueOf(seriesArr[i]));
                this.messageSendService.retrySendMsg(msgRetrySendRequest);
            }
        } catch (NullPointerException e) {
            logger.error("retrySendMess????????????????????????????????????????????????", e);
            throw new ValidateBusinessException(Constants.SUB_SYSTEM, ExceptionType.VBE20031, "retrySendMess????????????????????????????????????????????????" + e.getMessage());
        } catch (Exception e) {
            ExceptionUtil.processException(e, messsageRetrySendResponse);
            logger.error("????????????????????????" + e.getMessage(), e);
        }

        return messsageRetrySendResponse;
    }

    /**
     * ???????????????????????????topics
     * @param topicBySystemNameFindRequest
     * @return
     */
    public TopicBySystemNameFindResponse findTopicBySystemName(TopicBySystemNameFindRequest topicBySystemNameFindRequest) {
        TopicBySystemNameFindResponse topicBySystemNameFindResponse = new TopicBySystemNameFindResponse();

        try {
            if (!StringUtil.isAllNotNullOrBlank(new Object[]{topicBySystemNameFindRequest.getSystemNumId()})) {
                throw new ValidateBusinessException(Constants.SUB_SYSTEM, CodeMessagcenterExceptionType.VCE119101, "??????????????????");
            }

            List<TopicConfigModel> topicConfigModels = this.platformMqTopicDao.queryAllTopicsBySystemNumId(topicBySystemNameFindRequest.getSystemNumId(), topicBySystemNameFindRequest.getTenantNumId());
            ArrayList topics = new ArrayList();

            for(int i = 0; i < topicConfigModels.size(); ++i) {
                topics.add(((TopicConfigModel)topicConfigModels.get(i)).getTopic());
            }

            topicBySystemNameFindResponse.setTopic(topics);
        } catch (Exception e) {
            ExceptionUtil.processException(e, topicBySystemNameFindResponse);
            logger.error("??????topic????????????" + e.getMessage(), e);
        }

        return topicBySystemNameFindResponse;
    }

    /**
     * ??????topic?????????tag
     * @param tagBySystemNameAndTopicFindRequest
     * @return
     */
    public TagBySystemNameAndTopicFindResponse findTagBySystemNameAndTopic(TagBySystemNameAndTopicFindRequest tagBySystemNameAndTopicFindRequest) {
        TagBySystemNameAndTopicFindResponse tagBySystemNameAndTopicFindResponse = new TagBySystemNameAndTopicFindResponse();

        try {
            List<TopicConfigModel> topicConfigModels = this.platformMqTopicDao.queryAllTagsByTopic(tagBySystemNameAndTopicFindRequest.getTopic(), tagBySystemNameAndTopicFindRequest.getTenantNumId());
            ArrayList tags = new ArrayList();

            for(int i = 0; i < topicConfigModels.size(); ++i) {
                tags.add(((TopicConfigModel)topicConfigModels.get(i)).getTag());
            }

            tagBySystemNameAndTopicFindResponse.setTag(tags);
        } catch (Exception e) {
            ExceptionUtil.processException(e, tagBySystemNameAndTopicFindResponse);
            logger.error("??????tag????????????" + e.getMessage(), e);
        }

        return tagBySystemNameAndTopicFindResponse;
    }

    /**
     * ????????????http?????????
     * @param httpConfigByKeyRequest
     * @return
     */
    public HttpConfigByKeyResponse findHttpConfigByKey(HttpConfigByKeyRequest httpConfigByKeyRequest) {
        HttpConfigByKeyResponse httpConfigByKeyResponse = new HttpConfigByKeyResponse();

        try {
            logger.info("??????Http??????????????????????????????:" + jsonMapper.toJson(httpConfigByKeyRequest));
            Long offset = (httpConfigByKeyRequest.getPageNo() - 1L) * httpConfigByKeyRequest.getPageCount();
            List list = this.platformMqHttpConsumerDao.fuzzyQueryByKey(httpConfigByKeyRequest.getConsumerKey(), offset, httpConfigByKeyRequest.getPageCount());
            httpConfigByKeyResponse.setHttpList(list);
        } catch (Exception e) {
            ExceptionUtil.processException(e, httpConfigByKeyResponse);
            logger.error("??????httpKey????????????????????????" + e.getMessage(), e);
        }

        logger.info("??????Http??????????????????????????????:" + jsonMapper.toJson(httpConfigByKeyResponse));
        return httpConfigByKeyResponse;
    }

    /**
     * ????????????dubbo?????????
     * @param dubboConfigByKeyRequest
     * @return
     */
    public DubboConfigByKeyResponse findDubboConfigByKey(DubboConfigByKeyRequest dubboConfigByKeyRequest) {
        DubboConfigByKeyResponse dubboConfigByKeyResponse = new DubboConfigByKeyResponse();

        try {
            logger.info("??????dubboconsumer???????????????:" + jsonMapper.toJson(dubboConfigByKeyRequest));
            Long offset = (dubboConfigByKeyRequest.getPageNo() - 1L) * dubboConfigByKeyRequest.getPageCount();
            List list = this.platformMqDubboConsumerDao.fuzzyQueryByKey(dubboConfigByKeyRequest.getConsumerKey(), offset, dubboConfigByKeyRequest.getPageCount());
            dubboConfigByKeyResponse.setDubboList(list);
        } catch (Exception e) {
            ExceptionUtil.processException(e, dubboConfigByKeyResponse);
            logger.error("??????dubboKey????????????????????????" + e.getMessage(), e);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("end getDubboConsumerMessage response:{}", dubboConfigByKeyResponse.toLowerCaseJson());
        }

        logger.info("??????dubboconsumer???????????????:" + jsonMapper.toJson(dubboConfigByKeyResponse));
        return dubboConfigByKeyResponse;
    }

    /**
     * ??????topic??????
     * @param topicConfigDeleteRequest
     * @return
     */
    public TopicConfigDeleteResponse deleteTopicConfig(TopicConfigDeleteRequest topicConfigDeleteRequest) {
        TopicConfigDeleteResponse topicConfigDeleteResponse = new TopicConfigDeleteResponse();

        try {
            logger.info("??????topic????????????:" + jsonMapper.toJson(topicConfigDeleteRequest));
            this.platformMqTopicDao.cancel(topicConfigDeleteRequest.getSeries());
        } catch (Exception e) {
            ExceptionUtil.processException(e, topicConfigDeleteResponse);
            logger.error("??????topic????????????" + e.getMessage(), e);
        }

        logger.info("??????topic????????????:" + jsonMapper.toJson(topicConfigDeleteResponse));
        return topicConfigDeleteResponse;
    }

    /**
     * ??????topic
     * @param topicConfigAddRequest
     * @return
     */
    public TopicConfigAddResponse addTopicConfig(TopicConfigAddRequest topicConfigAddRequest) {
        TopicConfigAddResponse topicConfigAddResponse = new TopicConfigAddResponse();
        TransactionStatus transactionStatus = this.platformTransactionManager.getTransaction(TransactionUtil.newTransactionDefinition(-1));

        try {
            if (topicConfigAddRequest.getUsers() == null || topicConfigAddRequest.getUsers().isEmpty()) {
                throw new ValidateBusinessException(Constants.SUB_SYSTEM, ExceptionType.VBE20031, "??????topic??????????????????");
            }

            String retryTime = null;
            if (StringUtil.isAllNotNullOrBlank(new String[]{topicConfigAddRequest.getCorrectCodes()}) && topicConfigAddRequest.getCorrectCodes().contains("???")) {
                throw new ValidateBusinessException(Constants.SUB_SYSTEM, ExceptionType.VBE20031, "??????code????????????????????????,??????");
            }

            if (StringUtil.isAllNotNullOrBlank(new String[]{topicConfigAddRequest.getRetryInterval()})) {
                retryTime = Constants.index2RetryTime(topicConfigAddRequest.getRetryInterval());
            }

            String systemName = "";
            if (topicConfigAddRequest.getZkDataSign() != null && !topicConfigAddRequest.getZkDataSign().equals("N")) {
                topicConfigAddRequest.setZkDataSign("1");
            } else {
                topicConfigAddRequest.setZkDataSign("0");
            }

            String consumerInterval = null;
            if (StringUtil.isAllNotNullOrBlank(new String[]{topicConfigAddRequest.getConsumerInterval()})) {
                consumerInterval = Constants.index2RetryTime(topicConfigAddRequest.getConsumerInterval().toString());
            }

            TopicCreateRequest topicCreateRequest = new TopicCreateRequest();
            Long count = this.platformMqTopicDao.queryCountByTopicTag(topicConfigAddRequest.getTopic(), topicConfigAddRequest.getTag());
            if (count > 0L) {
                throw new BusinessException(Constants.SUB_SYSTEM, CodeMessagcenterExceptionType.BSE119402, "??????topic???tag?????????????????????");
            }

            HashMap map = new HashMap();
            map.put("TENANT_NUM_ID", topicConfigAddRequest.getTenantNumId());
            map.put("DATA_SIGN", topicConfigAddRequest.getDataSign());
            map.put("SYSTEM_NUM_ID", topicConfigAddRequest.getSystemNumId());
            CommonExcuteBySqlIdRequest commonExcuteBySqlIdRequest = new CommonExcuteBySqlIdRequest();
            commonExcuteBySqlIdRequest.setDataSign(topicConfigAddRequest.getDataSign());
            commonExcuteBySqlIdRequest.setTenantNumId(topicConfigAddRequest.getTenantNumId());
            commonExcuteBySqlIdRequest.setSqlId(Constants.querySystemNameBySystemNumIdSqlId);
            commonExcuteBySqlIdRequest.setInputParam(map);
            CommonExcuteBySqlIdResponse commonExcuteBySqlIdResponse = this.exportDataService.commonExcuteBySqlId(commonExcuteBySqlIdRequest);
            ExceptionUtil.checkDubboException(commonExcuteBySqlIdResponse);
            List results = commonExcuteBySqlIdResponse.getResults();
            if (results == null || results.size() <= 0) {
                logger.error("??????????????????????????????????????????");
                throw new BusinessException(Constants.SUB_SYSTEM, CodeMessagcenterExceptionType.BSE119401, "???????????????????????????????????????");
            }

            systemName = ((Map)results.get(0)).get("system_name").toString();
            Long series = this.platformMqTopicDao.insert(topicConfigAddRequest.getTopic(), topicConfigAddRequest.getTag(), topicConfigAddRequest.getWetherListenBinLogTopic(), consumerInterval, topicConfigAddRequest.getConsumerNum(), topicConfigAddRequest.getCreateUserName(), topicConfigAddRequest.getIsDistinct(), topicConfigAddRequest.getMessBatchNum(), topicConfigAddRequest.getRemark(), topicConfigAddRequest.getRetries(), topicConfigAddRequest.getRetriesDevelopment(), topicConfigAddRequest.getRetriesTest(), topicConfigAddRequest.getSystemNumId(), topicConfigAddRequest.getTaskTarget(), topicConfigAddRequest.getTenantNumId(), topicConfigAddRequest.getWetherOrderMess(), retryTime, topicConfigAddRequest.getWetherInsertdb(), topicConfigAddRequest.getCorrectCodes(), topicConfigAddRequest.getMqQueue(), topicConfigAddRequest.getWetherHandleFailedmess(), topicConfigAddRequest.getRetryMax(), topicConfigAddRequest.getZkDataSign(), topicConfigAddRequest.getConsumerPullDelay(), systemName);
            CodeWatchUserAddRequest codeWatchUserAddRequest = new CodeWatchUserAddRequest();
            ArrayList users = new ArrayList();
            Iterator usersIterator = topicConfigAddRequest.getUsers().iterator();

            while(usersIterator.hasNext()) {
                Long user = (Long)usersIterator.next();
                users.add(user.toString());
            }

            codeWatchUserAddRequest.setUsers(users);
            codeWatchUserAddRequest.setSystemId(topicConfigAddRequest.getSystemNumId());
            codeWatchUserAddRequest.setSmsWatch("Y");
            codeWatchUserAddRequest.setEmailWatch("Y");
            codeWatchUserAddRequest.setDataSign(topicConfigAddRequest.getDataSign());
            codeWatchUserAddRequest.setTenantNumId(topicConfigAddRequest.getTenantNumId());
            codeWatchUserAddRequest.setErrorCodeId(Constants.getErrorCode(series.toString(), Constants.eB));
            codeWatchUserAddRequest.setTopicName(topicConfigAddRequest.getTopic());
            codeWatchUserAddRequest.setTopicSeries(series);
            codeWatchUserAddRequest.setSystemName(systemName);
            codeWatchUserAddRequest.setErrorName("????????????!");
            codeWatchUserAddRequest.setNoticeTimeInterval(5);
            this.monitorMessageCodeService.addMessageCodeUser(codeWatchUserAddRequest);
            topicCreateRequest.setTopic(topicConfigAddRequest.getTopic());
            topicCreateRequest.setNameSrv(this.defaultName);
            this.messageSendService.createTopic(topicCreateRequest);
            this.platformTransactionManager.commit(transactionStatus);
        } catch (Exception e) {
            this.platformTransactionManager.rollback(transactionStatus);
            ExceptionUtil.processException(e, topicConfigAddResponse);
        }

        return topicConfigAddResponse;
    }

    /**
     * ?????????????????????
     * @param allSystemNameFindRequest
     * @return
     */
    public AllSystemNameFindResponse findAllSystemName(AllSystemNameFindRequest allSystemNameFindRequest) {
        AllSystemNameFindResponse allSystemNameFindResponse = new AllSystemNameFindResponse();

        try {
            List<TopicConfigModel> topicConfigModelList = this.exArcDocSystemDao.queryAllSystemNames(allSystemNameFindRequest.getTenantNumId());
            ArrayList systemNames = new ArrayList();

            for(int i = 0; i < topicConfigModelList.size(); ++i) {
                if (StringUtil.isAllNotNullOrBlank(new String[]{((TopicConfigModel)topicConfigModelList.get(i)).getSystemName()})) {
                    systemNames.add(((TopicConfigModel)topicConfigModelList.get(i)).getSystemName());
                }
            }

            allSystemNameFindResponse.setSystemNames(systemNames);
        } catch (Exception e) {
            ExceptionUtil.processException(e, allSystemNameFindResponse);
            logger.error("??????????????????????????? ?????????" + e.getMessage(), e);
        }

        return allSystemNameFindResponse;
    }

    /**
     * ???????????????topic
     * @param allFatherTopicFindRequest
     * @return
     */
    public AllFatherTopicFindResponse findAllFatherTopic(AllFatherTopicFindRequest allFatherTopicFindRequest) {
        AllFatherTopicFindResponse allFatherTopicFindResponse = new AllFatherTopicFindResponse();

        try {
            List<TopicConfigModel> list = this.plaftformMqTopicManyDao.queryAllFatherTopics(allFatherTopicFindRequest.getFatherTopicKey(), allFatherTopicFindRequest.getTenantNumId());
            ArrayList topics = new ArrayList();

            for(int i = 0; i < list.size(); ++i) {
                if (StringUtil.isAllNotNullOrBlank(new String[]{((TopicConfigModel)list.get(i)).getTopic()})) {
                    topics.add(((TopicConfigModel)list.get(i)).getTopic());
                }
            }

            allFatherTopicFindResponse.setFatherTopics(topics);
        } catch (Exception e) {
            ExceptionUtil.processException(e, allFatherTopicFindResponse);
            logger.error("??????topic???????????? ?????????" + e.getMessage(), e);
        }

        return allFatherTopicFindResponse;
    }

    /**
     * ?????????topic????????????topic
     * @param relationTopicFindRequest
     * @return
     */
    public RelationTopicFindResponse findRelationTopic(RelationTopicFindRequest relationTopicFindRequest) {
        RelationTopicFindResponse relationTopicFindResponse = new RelationTopicFindResponse();

        try {
            List<SonTopicRelationModel> list = this.plaftformMqTopicManyDao.queryFatherTopicRelationSonTopics(relationTopicFindRequest.getFatherTopic(), relationTopicFindRequest.getTenantNumId());
            relationTopicFindResponse.setTopicRelations(list);
        } catch (Exception e) {
            ExceptionUtil.processException(e, relationTopicFindResponse);
            logger.error("??????topic?????????????????? ?????????" + e.getMessage(), e);
        }

        return relationTopicFindResponse;
    }

    /**
     * ????????????topic???????????????
     * @param relationTopicDeleteRequest
     * @return
     */
    public RelationTopicDeleteResponse deleteRelationTopic(RelationTopicDeleteRequest relationTopicDeleteRequest) {
        RelationTopicDeleteResponse relationTopicDeleteResponse = new RelationTopicDeleteResponse();

        try {
            this.platformMqTopicRelationDao.delete(relationTopicDeleteRequest.getRelationSeries());
        } catch (Exception e) {
            ExceptionUtil.processException(e, relationTopicDeleteResponse);
            logger.error("??????topic??????????????? ?????????" + e.getMessage(), e);
        }

        return relationTopicDeleteResponse;
    }

    /**
     * ????????????topic????????????
     * @param relationTopicAddRequest
     * @return
     */
    public RelationTopicAddResponse addRelationTopic(RelationTopicAddRequest relationTopicAddRequest) {
        RelationTopicAddResponse relationTopicAddResponse = new RelationTopicAddResponse();

        try {
            Long systemNumId = this.exArcDocSystemDao.querySystemNumIdBySystemName(relationTopicAddRequest.getSystemName(), relationTopicAddRequest.getTenantNumId());
            if (!StringUtil.isAllNotNullOrBlank(new Object[]{systemNumId})) {
                throw new ValidateBusinessException(Constants.SUB_SYSTEM, ExceptionType.VBE20031, "??????????????????");
            }

            List<TopicConfigModel> topicConfigModels = this.platformMqTopicDao.query(systemNumId, relationTopicAddRequest.getTopic(), relationTopicAddRequest.getTag(), relationTopicAddRequest.getTenantNumId(), "", relationTopicAddRequest.getDataSign());
            if (topicConfigModels.isEmpty()) {
                throw new ValidateBusinessException(Constants.SUB_SYSTEM, ExceptionType.VBE20031, "??????topic,tag,system?????????????????????");
            }

            String series = ((TopicConfigModel)topicConfigModels.get(0)).getSeries();
            List<TopicConfigModel> topicConfigModels2 = this.plaftformMqTopicManyDao.queryByTopic(relationTopicAddRequest.getFatherTopic(), relationTopicAddRequest.getTenantNumId());
            String fatherSeries = ((TopicConfigModel)topicConfigModels2.get(0)).getSeries();
            this.platformMqTopicRelationDao.insert(series, fatherSeries);
        } catch (Exception e) {
            ExceptionUtil.processException(e, relationTopicAddResponse);
            logger.error("??????topic??????????????????" + e.getMessage(), e);
        }

        return relationTopicAddResponse;
    }

    /**
     * ?????????topic
     * @param fatherTopicAddRequest
     * @return
     */
    public FatherTopicAddResponse addFatherTopic(FatherTopicAddRequest fatherTopicAddRequest) {
        FatherTopicAddResponse fatherTopicAddResponse = new FatherTopicAddResponse();

        try {
            this.plaftformMqTopicManyDao.insert(fatherTopicAddRequest.getFatherTopic(), fatherTopicAddRequest.getRemark(), fatherTopicAddRequest.getTenantNumId());
        } catch (Exception e) {
            ExceptionUtil.processException(e, fatherTopicAddResponse);
            logger.error("?????????topic??????" + e.getMessage(), e);
        }

        return fatherTopicAddResponse;
    }

    /**
     * ?????????topic
     * @param fatherTopicDeleteRequest
     * @return
     */
    public FatherTopicDeleteResponse deleteFatherTopic(FatherTopicDeleteRequest fatherTopicDeleteRequest) {
        FatherTopicDeleteResponse fatherTopicDeleteResponse = new FatherTopicDeleteResponse();

        try {
            this.plaftformMqTopicManyDao.delete(fatherTopicDeleteRequest.getFatherTopic(), fatherTopicDeleteRequest.getTenantNumId());
        } catch (Exception e) {
            ExceptionUtil.processException(e, fatherTopicDeleteResponse);
            logger.error("?????????topic??????" + e.getMessage(), e);
        }

        return fatherTopicDeleteResponse;
    }

    /**
     * ?????????????????????????????????
     * @param allSystemNameAndIdFindRequest
     * @return
     */
    public AllSystemNameAndIdFindResponse findAllSystemNameAndId(AllSystemNameAndIdFindRequest allSystemNameAndIdFindRequest) {
        AllSystemNameAndIdFindResponse allSystemNameAndIdFindResponse = new AllSystemNameAndIdFindResponse();

        try {
            List list = this.exArcDocSystemDao.querySystemNameAndId(allSystemNameAndIdFindRequest.getTenantNumId());
            allSystemNameAndIdFindResponse.setSystemInfo(list);
        } catch (Exception e) {
            ExceptionUtil.processException(e, allSystemNameAndIdFindResponse);
            logger.error("????????????????????????ID?????????" + e.getMessage(), e);
        }

        return allSystemNameAndIdFindResponse;
    }

    /**
     * ????????????id??????topic
     * @param topicConfigBySystemIdFindRequest
     * @return
     */
    public TopicConfigBySystemIdFindResponse findTopicConfigBySystemId(TopicConfigBySystemIdFindRequest topicConfigBySystemIdFindRequest) {
        TopicConfigBySystemIdFindResponse topicConfigBySystemIdFindResponse = new TopicConfigBySystemIdFindResponse();

        try {
            List<TopicConfigModel> list = this.platformMqTopicDao.queryAllBySystemNumId(topicConfigBySystemIdFindRequest.getSystemId(), topicConfigBySystemIdFindRequest.getTenantNumId());
            topicConfigBySystemIdFindResponse.setTopics(list);
        } catch (Exception e) {
            ExceptionUtil.processException(e, topicConfigBySystemIdFindResponse);
            logger.error("????????????ID??????topic?????????" + e.getMessage(), e);
        }

        return topicConfigBySystemIdFindResponse;
    }

    /**
     * ???????????????????????????series??????
     * @param topicConfigBySystemIdAndTopicFindRequest
     * @return
     */
    public TopicConfigBySystemIdFindAndTopicResponse findTopicConfigBySystemIdAndTopic(TopicConfigBySystemIdAndTopicFindRequest topicConfigBySystemIdAndTopicFindRequest) {
        TopicConfigBySystemIdFindAndTopicResponse topicConfigBySystemIdFindAndTopicResponse = new TopicConfigBySystemIdFindAndTopicResponse();

        try {
            List<MonitorModel> list = this.exArcDocSystemDao.query(topicConfigBySystemIdAndTopicFindRequest.getSystemId(), topicConfigBySystemIdAndTopicFindRequest.getTopicSeries(), topicConfigBySystemIdAndTopicFindRequest.getTenantNumId());
            topicConfigBySystemIdFindAndTopicResponse.setMonitorList(list);
        } catch (Exception e) {
            ExceptionUtil.processException(e, topicConfigBySystemIdFindAndTopicResponse);
            logger.error("????????????ID,tpic??????topic???????????????" + e.getMessage(), e);
        }

        return topicConfigBySystemIdFindAndTopicResponse;
    }

    public RetryIntervalResponse getRetryInterval(RetryIntervalRequest retryIntervalRequest) {
        RetryIntervalResponse retryIntervalResponse = new RetryIntervalResponse();
        retryIntervalResponse.setRetryInterval(retryIntervalRequest.getRetryInterval());
        return retryIntervalResponse;
    }

    /**
     * ??????????????????
     * @param messageByBodySendRequest
     * @return
     */
    public MessageByBodySendResponse sendMessageByBody(MessageByBodySendRequest messageByBodySendRequest) {
        MessageByBodySendResponse messageByBodySendResponse = new MessageByBodySendResponse();

        try {
            SimpleMessageRightNowSendRequest simpleMessageRightNowSendRequest = new SimpleMessageRightNowSendRequest();
            simpleMessageRightNowSendRequest.setSimpleMessage(new SimpleMessage(messageByBodySendRequest.getMessage().getTopic(), messageByBodySendRequest.getMessage().getTag(), messageByBodySendRequest.getMessage().getMsgKey(), jsonMapper.toJson(messageByBodySendRequest.getMessage().getBody()), Long.valueOf(messageByBodySendRequest.getMessage().getFromSystem()), messageByBodySendRequest.getDataSign(), messageByBodySendRequest.getTenantNumId()));
            SimpleMessageRightNowSendResponse simpleMessageRightNowSendResponse = this.messageSendService.sendSimpleMessageRightNow(simpleMessageRightNowSendRequest);
            messageByBodySendResponse.setMessage("??????????????????" + simpleMessageRightNowSendResponse.getSeries());
        } catch (Exception e) {
            ExceptionUtil.processException(e, messageByBodySendResponse);
            logger.error("?????????????????????" + e.getMessage(), e);
        }

        return messageByBodySendResponse;
    }

    /**
     * ??????????????????????????????
     * @param messageDetailRequest
     * @return
     */
    public MessageDetailResponse findUnconsumeMessageDetail(MessageDetailRequest messageDetailRequest) {
        MessageDetailResponse messageDetailResponse = new MessageDetailResponse();

        try {
            messageDetailRequest.setConsumerSuccess("");
            String condition = this.assembleCondition(messageDetailRequest);
            Long count = this.sysRocketMqSendLogDao.queryCount(condition);
            condition = condition + " order by create_dtme desc limit " + (messageDetailRequest.getPageNo() - 1L) * messageDetailRequest.getPageCount() + " , " + messageDetailRequest.getPageCount();
            List messageDetailsList = this.sysRocketMqSendLogDao.query(condition);
            Integer page = this.countPage(Integer.valueOf(messageDetailRequest.getPageCount().toString()), Integer.valueOf(count.toString()));  // ??????

            for(int i = 0; i < messageDetailsList.size(); ++i) {
                List platformMqTopicList = this.platformMqTopicDao.queryByTopicTag(((MessageDetails)messageDetailsList.get(i)).getTopic(), ((MessageDetails)messageDetailsList.get(i)).getTag(), messageDetailRequest.getTenantNumId().toString());
                if (platformMqTopicList != null && !platformMqTopicList.isEmpty()) {
                    ((MessageDetails)messageDetailsList.get(i)).setRemark(((PlatformMqTopic)platformMqTopicList.get(0)).getREMARK());
                }
            }

            messageDetailResponse.setMessageList(messageDetailsList);
            messageDetailResponse.setTotal(Long.valueOf(page.toString()));
        } catch (Exception e) {
            ExceptionUtil.processException(e, messageDetailResponse);
            logger.error("???????????????????????????" + e.getMessage(), e);
        }

        return messageDetailResponse;
    }

    /**
     * ????????????topic tag??????
     * @param messageRetrySendByTopicAndTagRequest
     * @return
     */
    public MessageRetrySendByTopicAndTagResponse retrySendMessageByTopicAndTag(MessageRetrySendByTopicAndTagRequest messageRetrySendByTopicAndTagRequest) {
        MessageRetrySendByTopicAndTagResponse messageRetrySendByTopicAndTagResponse = new MessageRetrySendByTopicAndTagResponse();
        DistributedLock distributedLock = null;

        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            simpleDateFormat.parse(messageRetrySendByTopicAndTagRequest.getMessageDtme());
            StringBuffer sb = new StringBuffer(messageRetrySendByTopicAndTagRequest.getMessageDtme().trim());
            boolean locked = true;
            distributedLock = new DistributedLock(this.zookeeperHostPort, messageRetrySendByTopicAndTagRequest.getMessageTopic() + "_" + messageRetrySendByTopicAndTagRequest.getMessageTag());
            if (!distributedLock.tryLock()) {
                locked = false;
                messageRetrySendByTopicAndTagResponse.setMessage("????????????,???????????????~");
            }

            if (locked) {
                List seriesList = this.sysRocketMqSendLogDao.querySeries(messageRetrySendByTopicAndTagRequest.getMessageTopic(), messageRetrySendByTopicAndTagRequest.getMessageTag(), messageRetrySendByTopicAndTagRequest.getTenantNumId(), messageRetrySendByTopicAndTagRequest.getDataSign(), sb.append(" 00:00:00").toString(), sb.replace(10, 18, " 23:59:59").substring(0, 19).toString());
                if (seriesList == null || seriesList.isEmpty()) {
                    messageRetrySendByTopicAndTagResponse.setMessage("??????????????????????????????");
                    MessageRetrySendByTopicAndTagResponse messageRetrySendByTopicAndTagResponse1 = messageRetrySendByTopicAndTagResponse;
                    return messageRetrySendByTopicAndTagResponse1;
                }

                MesssageRetrySendRequest messsageRetrySendRequest = new MesssageRetrySendRequest();
                messsageRetrySendRequest.setTenantNumId(messageRetrySendByTopicAndTagRequest.getTenantNumId());
                messsageRetrySendRequest.setDataSign(messageRetrySendByTopicAndTagRequest.getDataSign());
                messsageRetrySendRequest.setSeriesList(String.join(",", seriesList));
                this.retrySendMessage(messsageRetrySendRequest);
            }
        } catch (ParseException e) {
            throw new ValidateBusinessException(Constants.SUB_SYSTEM, ExceptionType.VBE20031, "??????????????????????????????????????????????????????:2017-01-01");
        } catch (Exception e) {
            ExceptionUtil.processException(e, messageRetrySendByTopicAndTagResponse);
            logger.error("??????topic,tag??????:??????????????????????????? error", e.getMessage());
        } finally {
            if (distributedLock != null) {
                distributedLock.unlock();
            }

        }

        return messageRetrySendByTopicAndTagResponse;
    }

    /**
     * ??????namesrv??????(???????????????)
     * @param mqNameSrvGetRequest
     * @return
     */
    public MqNameSrvGetRsponse getMqNameSrv(MqNameSrvGetRequest mqNameSrvGetRequest) {
        MqNameSrvGetRsponse mqNameSrvGetRsponse = new MqNameSrvGetRsponse();

        try {
            Set nameserverKeySet = this.rocketMqConfigUtil.getPropertyKey();
            ArrayList list = new ArrayList();
            Iterator nameserverKeySetIterator = nameserverKeySet.iterator();

            while(nameserverKeySetIterator.hasNext()) {
                Object key = nameserverKeySetIterator.next();
                list.add((String)key);
            }

            mqNameSrvGetRsponse.setNameSrvRemark(this.nsRemark);
            mqNameSrvGetRsponse.setNameSrv(list);
        } catch (Exception e) {
            ExceptionUtil.processException(e, mqNameSrvGetRsponse);
            logger.error("??????namesrv?????????" + e.getMessage(), e);
        }

        return mqNameSrvGetRsponse;
    }

    /**
     * ??????consumerIp??????
     * @param consumerIpsGetRequest
     * @return
     */
    public ConsumerIpsGetRsponse getConsumerIps(ConsumerIpsGetRequest consumerIpsGetRequest) {
        ConsumerIpsGetRsponse consumerIpsGetRsponse = new ConsumerIpsGetRsponse();

        try {
            Set cipKeySet = this.rocketMqConfigUtil.getConsumerIpsKey();
            ArrayList list = new ArrayList();
            Iterator cipKeySetIterator = cipKeySet.iterator();

            while(cipKeySetIterator.hasNext()) {
                Object cipKey = cipKeySetIterator.next();
                list.add((String)cipKey);
            }

            consumerIpsGetRsponse.setConsumerRemark(this.cipRemark);
            consumerIpsGetRsponse.setConsumerIps(list);
        } catch (Exception e) {
            ExceptionUtil.processException(e, consumerIpsGetRsponse);
            logger.error("??????consumerIp?????????" + e.getMessage(), e);
        }

        return consumerIpsGetRsponse;
    }

    /**
     * ??????http?????????
     * @param httpConsumerAddRequest
     * @return
     */
    public HttpConsumerAddRsponse addHttpConsumer(HttpConsumerAddRequest httpConsumerAddRequest) {
        HttpConsumerAddRsponse httpConsumerAddRsponse = new HttpConsumerAddRsponse();

        try {
            this.platformMqHttpConsumerDao.insert(httpConsumerAddRequest.getHttpUserName(), httpConsumerAddRequest.getHttpRemark(), httpConsumerAddRequest.getParamName(), httpConsumerAddRequest.getHttpHead(), httpConsumerAddRequest.getUrlDevelop(), httpConsumerAddRequest.getUrlTest(), httpConsumerAddRequest.getUrl());
        } catch (Exception e) {
            ExceptionUtil.processException(e, httpConsumerAddRsponse);
        }

        return httpConsumerAddRsponse;
    }

    /**
     * ??????dubbo?????????
     * @param dubboConsumerAddRequest
     * @return
     */
    public DubboConsumerAddRsponse addDubboConsumer(DubboConsumerAddRequest dubboConsumerAddRequest) {
        DubboConsumerAddRsponse dubboConsumerAddRsponse = new DubboConsumerAddRsponse();

        try {
            this.platformMqDubboConsumerDao.insert(dubboConsumerAddRequest.getDubboCreatePerson(), dubboConsumerAddRequest.getDubboDirUrl(), dubboConsumerAddRequest.getDubboGroup(), dubboConsumerAddRequest.getDubboMethod(), dubboConsumerAddRequest.getDubboParam(), dubboConsumerAddRequest.getDubboService(), dubboConsumerAddRequest.getDubboRemark(), dubboConsumerAddRequest.getVersion(), dubboConsumerAddRequest.getZkAddress(), dubboConsumerAddRequest.getZkDevelopAddress(), dubboConsumerAddRequest.getZkTestAddress());
        } catch (Exception e) {
            ExceptionUtil.processException(e, dubboConsumerAddRsponse);
        }

        return dubboConsumerAddRsponse;
    }

    /**
     * ??????dubbo?????????
     * @param dubboConsumerDeleteRequest
     * @return
     */
    public DubboConsumerDeleteRsponse deleteDubboConsumer(DubboConsumerDeleteRequest dubboConsumerDeleteRequest) {
        DubboConsumerDeleteRsponse dubboConsumerDeleteRsponse = new DubboConsumerDeleteRsponse();
        TransactionStatus transactionStatus = this.platformTransactionManager.getTransaction(TransactionUtil.newTransactionDefinition(-1));

        try {
            this.platformMqDubboConsumerDao.cancel(dubboConsumerDeleteRequest.getSeries());
            this.platformMqTopicDao.deleteConsumerSeries(dubboConsumerDeleteRequest.getSeries(), 1);
            this.platformTransactionManager.commit(transactionStatus);
        } catch (Exception e) {
            ExceptionUtil.processException(e, dubboConsumerDeleteRsponse);
            this.platformTransactionManager.rollback(transactionStatus);
        }

        return dubboConsumerDeleteRsponse;
    }

    /**
     * ??????http?????????
     * @param httpConsumerDeleteRequest
     * @return
     */
    public HttpConsumerDeleteRsponse deleteHttpConsumer(HttpConsumerDeleteRequest httpConsumerDeleteRequest) {
        HttpConsumerDeleteRsponse httpConsumerDeleteRsponse = new HttpConsumerDeleteRsponse();
        TransactionStatus transactionStatus = this.platformTransactionManager.getTransaction(TransactionUtil.newTransactionDefinition(-1));

        try {
            this.platformMqHttpConsumerDao.cancel(httpConsumerDeleteRequest.getSeries());
            this.platformMqTopicDao.deleteConsumerSeries(httpConsumerDeleteRequest.getSeries(), 2);
            this.platformTransactionManager.commit(transactionStatus);
        } catch (Exception e) {
            ExceptionUtil.processException(e, httpConsumerDeleteRsponse);
            this.platformTransactionManager.rollback(transactionStatus);
        }

        return httpConsumerDeleteRsponse;
    }

    /**
     * ??????http?????????
     * @param httpConsumerUpdateRequest
     * @return
     */
    public HttpConsumerUpdateRsponse updateHttpConsumer(HttpConsumerUpdateRequest httpConsumerUpdateRequest) {
        HttpConsumerUpdateRsponse httpConsumerUpdateRsponse = new HttpConsumerUpdateRsponse();

        try {
            this.platformMqHttpConsumerDao.update(httpConsumerUpdateRequest.getHttpSeries(), httpConsumerUpdateRequest.getHttpUserName(), httpConsumerUpdateRequest.getUrlDevelop(), httpConsumerUpdateRequest.getHttpHead(), httpConsumerUpdateRequest.getParamName(), httpConsumerUpdateRequest.getHttpRemark(), httpConsumerUpdateRequest.getUrlTest(), httpConsumerUpdateRequest.getUrl());
        } catch (Exception e) {
            ExceptionUtil.processException(e, httpConsumerUpdateRsponse);
        }

        return httpConsumerUpdateRsponse;
    }

    /**
     * ??????dubbo?????????
     * @param dubboConsumerUpdateRequest
     * @return
     */
    public DubboConsumerUpdateRsponse updateDubboConsumer(DubboConsumerUpdateRequest dubboConsumerUpdateRequest) {
        DubboConsumerUpdateRsponse dubboConsumerUpdateRsponse = new DubboConsumerUpdateRsponse();

        try {
            this.platformMqDubboConsumerDao.update(dubboConsumerUpdateRequest.getDubboSeries(), dubboConsumerUpdateRequest.getDubboCreatePerson(), dubboConsumerUpdateRequest.getDubboDirUrl(), dubboConsumerUpdateRequest.getDubboGroup(), dubboConsumerUpdateRequest.getDubboMethod(), dubboConsumerUpdateRequest.getDubboParam(), dubboConsumerUpdateRequest.getDubboService(), dubboConsumerUpdateRequest.getDubboRemark(), dubboConsumerUpdateRequest.getVersion(), dubboConsumerUpdateRequest.getZkAddress(), dubboConsumerUpdateRequest.getZkDevelopAddress(), dubboConsumerUpdateRequest.getZkTestAddress());
        } catch (Exception e) {
            ExceptionUtil.processException(e, dubboConsumerUpdateRsponse);
        }

        return dubboConsumerUpdateRsponse;
    }

    /**
     * ??????series??????consumer?????????
     * @param httpConsumerGetBySeriesRequest
     * @return
     */
    public HttpConsumerGetBySeriesResponse getHttpConsumerBySeries(HttpConsumerGetBySeriesRequest httpConsumerGetBySeriesRequest) {
        HttpConsumerGetBySeriesResponse httpConsumerGetBySeriesResponse = new HttpConsumerGetBySeriesResponse();

        try {
            PlatformMqHttpConsumer platformMqHttpConsumer = this.platformMqHttpConsumerDao.queryBySeries(httpConsumerGetBySeriesRequest.getSeries());
            if (platformMqHttpConsumer == null) {
                throw new RuntimeException("????????????????????????http??????");
            }

            httpConsumerGetBySeriesResponse.setCreateUserName(platformMqHttpConsumer.getCREATE_USER_NAME());
            httpConsumerGetBySeriesResponse.setDevelopmentUrl(platformMqHttpConsumer.getURL_DEVELOP());
            httpConsumerGetBySeriesResponse.setHttpHead(platformMqHttpConsumer.getHTTP_HEAD());
            httpConsumerGetBySeriesResponse.setParamName(platformMqHttpConsumer.getPARAM_NAME());
            httpConsumerGetBySeriesResponse.setRemark(platformMqHttpConsumer.getRemark());
            httpConsumerGetBySeriesResponse.setSeries(platformMqHttpConsumer.getSERIES());
            httpConsumerGetBySeriesResponse.setTestUrl(platformMqHttpConsumer.getURL_TEST());
            httpConsumerGetBySeriesResponse.setUrl(platformMqHttpConsumer.getURL());
        } catch (Exception e) {
            ExceptionUtil.processException(e, httpConsumerGetBySeriesResponse);
        }

        return httpConsumerGetBySeriesResponse;
    }

    /**
     * ??????series??????dubbo?????????
     * @param dubboConsumerGetBySeriesRequest
     * @return
     */
    public DubboConsumerGetBySeriesResponse getDubboConsumerBySeries(DubboConsumerGetBySeriesRequest dubboConsumerGetBySeriesRequest) {
        DubboConsumerGetBySeriesResponse dubboConsumerGetBySeriesResponse = new DubboConsumerGetBySeriesResponse();

        try {
            PlatformMqDubboConsumer platformMqDubboConsumer = this.platformMqDubboConsumerDao.queryBySeries(dubboConsumerGetBySeriesRequest.getSeries());
            if (platformMqDubboConsumer == null) {
                throw new RuntimeException("????????????????????????dubbo??????");
            }

            dubboConsumerGetBySeriesResponse.setCreateUserName(platformMqDubboConsumer.getCREATE_USER_NAME());
            dubboConsumerGetBySeriesResponse.setDirectAdr(platformMqDubboConsumer.getDIRECT_ADR());
            dubboConsumerGetBySeriesResponse.setDubboGroup(platformMqDubboConsumer.getDUBBO_GROUP());
            dubboConsumerGetBySeriesResponse.setDubboMethodName(platformMqDubboConsumer.getMETHOD_NAME());
            dubboConsumerGetBySeriesResponse.setDubboParam(platformMqDubboConsumer.getPARAM_ENTITY());
            dubboConsumerGetBySeriesResponse.setDubboServiceName(platformMqDubboConsumer.getSERVICE_NAME());
            dubboConsumerGetBySeriesResponse.setRemark(platformMqDubboConsumer.getREMARK());
            dubboConsumerGetBySeriesResponse.setSeries(platformMqDubboConsumer.getSERIES());
            dubboConsumerGetBySeriesResponse.setVersion(platformMqDubboConsumer.getVERSION());
            dubboConsumerGetBySeriesResponse.setZkAddress(this.zookeeperHostPort);
            dubboConsumerGetBySeriesResponse.setZkDevelopmentAddress(this.zookeeperHostPort);
            dubboConsumerGetBySeriesResponse.setZkTestAddress(this.zookeeperHostPort);
        } catch (Exception e) {
            ExceptionUtil.processException(e, dubboConsumerGetBySeriesResponse);
        }

        return dubboConsumerGetBySeriesResponse;
    }

    /**
     * ??????????????????
     * @param exArcDocSystemAddRequest
     * @return
     */
    public ExArcDocSystemAddRsponse addExArcDocSystem(ExArcDocSystemAddRequest exArcDocSystemAddRequest) {
        ExArcDocSystemAddRsponse exArcDocSystemAddRsponse = new ExArcDocSystemAddRsponse();

        try {
            this.exArcDocSystemDao.querySystemNumIdBySystemNameStrict(exArcDocSystemAddRequest.getSystemName(), exArcDocSystemAddRequest.getTenantNumId());
            this.exArcDocSystemDao.insert(exArcDocSystemAddRequest.getSystemName(), exArcDocSystemAddRequest.getTenantNumId());
        } catch (Exception e) {
            ExceptionUtil.processException(e, exArcDocSystemAddRsponse);
        }

        return exArcDocSystemAddRsponse;
    }

    /**
     * ?????????topic
     * @param fatherTopicFindRequest
     * @return
     */
    public FatherTopicFindResponse findFatherTopicByTopic(FatherTopicFindRequest fatherTopicFindRequest) {
        FatherTopicFindResponse fatherTopicFindResponse = new FatherTopicFindResponse();

        try {
            List<TopicConfigModel> list = this.plaftformMqTopicManyDao.queryByTopic(fatherTopicFindRequest.getFatherTopic(), fatherTopicFindRequest.getTenantNumId());
            if (!list.isEmpty() && list.size() > 0) {
                fatherTopicFindResponse.setRemark(((TopicConfigModel)list.get(0)).getRemark());
            } else {
                fatherTopicFindResponse.setRemark("??????topic????????????????????????????????????????????????");
            }

            fatherTopicFindResponse.setFatherTopic(fatherTopicFindRequest.getFatherTopic());
        } catch (Exception e) {
            ExceptionUtil.processException(e, fatherTopicFindResponse);
        }

        return fatherTopicFindResponse;
    }

    /**
     * ??????????????????topic
     * @param topicByTopicTypeFindRequest
     * @return
     */
    public TopicByTopicTypeFindResponse findTopicByTopicType(TopicByTopicTypeFindRequest topicByTopicTypeFindRequest) {
        TopicByTopicTypeFindResponse topicByTopicTypeFindResponse = new TopicByTopicTypeFindResponse();

        try {
            List<TopicConfigModel> list = this.plaftformMqTopicManyDao.queryByBizType(topicByTopicTypeFindRequest.getTopicTopicType());
            topicByTopicTypeFindResponse.setModels(list);
        } catch (Exception e) {
            ExceptionUtil.processException(e, topicByTopicTypeFindResponse);
        }

        return topicByTopicTypeFindResponse;
    }

    public BeatHeartResponse heartBeat(BeatHeartRequest beatHeartRequest) {
        return new BeatHeartResponse();
    }

    /**
     * ??????tag???topic
     * @param remarkByTagGetRequest
     * @return
     */
    public RemarkByTagGetResponse getRemarkByTag(RemarkByTagGetRequest remarkByTagGetRequest) {
        RemarkByTagGetResponse remarkByTagGetResponse = new RemarkByTagGetResponse();

        try {
            List<TopicConfigModel> list = this.platformMqTopicDao.queryByTag(remarkByTagGetRequest.getTag());
            if (!list.isEmpty()) {
                remarkByTagGetResponse.setRemark(((TopicConfigModel)list.get(0)).getRemark());
                remarkByTagGetResponse.setTopic(((TopicConfigModel)list.get(0)).getTopic());
            } else {
                remarkByTagGetResponse.setRemark("???????????????????????????");
                remarkByTagGetResponse.setTopic("????????????topic");
            }
        } catch (Exception e) {
            ExceptionUtil.processException(e, remarkByTagGetResponse);
        }

        return remarkByTagGetResponse;
    }

    public TopicTagByTypeGetResponse getTopicTagByType(TopicTagByTypeGetRequest topicTagByTypeGetRequest) {
        TopicTagByTypeGetResponse topicTagByTypeGetResponse = new TopicTagByTypeGetResponse();

        try {
            List<TopicConfigModel> list = this.platformMqTopicDao.queryByConsumerInstanceCount(topicTagByTypeGetRequest.getTopicType()); // ?
            topicTagByTypeGetResponse.setTopicList(list);
        } catch (Exception e) {
            ExceptionUtil.processException(e, topicTagByTypeGetResponse);
        }

        return topicTagByTypeGetResponse;
    }

    private Boolean hasConsumer(DefaultMQAdminExt mqAdminExt, String consumerGroup) {
        Boolean flag = true;

        try {
            mqAdminExt.examineConsumerConnectionInfo(consumerGroup);
        } catch (Exception e) {
            flag = false;
        }

        return flag;
    }

    /**
     * ??????topic tag?????????
     * @param simpleConfigByTopicAndTagGetRequest
     * @return
     */
    public SimpleConfigByTopicAndTagGetResponse getSimpleConfigByTopicAndTag(SimpleConfigByTopicAndTagGetRequest simpleConfigByTopicAndTagGetRequest) {
        SimpleConfigByTopicAndTagGetResponse simpleConfigByTopicAndTagGetResponse = new SimpleConfigByTopicAndTagGetResponse();

        try {
            String condition = "";
            if (StringUtil.isAllNotNullOrBlank(new String[]{simpleConfigByTopicAndTagGetRequest.getRemark()})) {
                condition = condition + " and remark like '%" + simpleConfigByTopicAndTagGetRequest.getRemark() + "%'";
            }

            Long count = this.platformMqTopicDao.queryCount(simpleConfigByTopicAndTagGetRequest.getTopic(), simpleConfigByTopicAndTagGetRequest.getTag(), simpleConfigByTopicAndTagGetRequest.getTenantNumId(), condition, simpleConfigByTopicAndTagGetRequest.getDataSign());
            condition = condition + "order by create_date_time desc limit " + (simpleConfigByTopicAndTagGetRequest.getPageNo() - 1L) * simpleConfigByTopicAndTagGetRequest.getPageCount() + " , " + simpleConfigByTopicAndTagGetRequest.getPageCount();
            List<SimpleConfig> topicList = this.platformMqTopicDao.query(simpleConfigByTopicAndTagGetRequest.getTopic(), simpleConfigByTopicAndTagGetRequest.getTag(), simpleConfigByTopicAndTagGetRequest.getTenantNumId(), condition, simpleConfigByTopicAndTagGetRequest.getDataSign());
            Iterator iterator = topicList.iterator();

            while(true) {
                while(iterator.hasNext()) {
                    SimpleConfig simpleConfig = (SimpleConfig)iterator.next();
                    if ("N".equals(simpleConfig.getOnlineFlag())) {
                        simpleConfig.setOnlineFlag("????????????");
                    } else {
                        simpleConfig.setOnlineFlag("????????????");
                    }

                    if (simpleConfig.getConsumerType() == 0L) {
                        simpleConfig.setOnlineFlag("????????????dubbo??????http,????????????");
                    }

                    HashMap map = new HashMap();
                    map.put("TENANT_NUM_ID", simpleConfigByTopicAndTagGetRequest.getTenantNumId());
                    map.put("DATA_SIGN", simpleConfigByTopicAndTagGetRequest.getDataSign());
                    map.put("SYSTEM_NUM_ID", simpleConfig.getSystemName());
                    CommonExcuteBySqlIdRequest commonExcuteBySqlIdRequest = new CommonExcuteBySqlIdRequest();
                    commonExcuteBySqlIdRequest.setDataSign(simpleConfigByTopicAndTagGetRequest.getDataSign());
                    commonExcuteBySqlIdRequest.setTenantNumId(simpleConfigByTopicAndTagGetRequest.getTenantNumId());
                    commonExcuteBySqlIdRequest.setSqlId(Constants.querySystemNameBySystemNumIdSqlId);
                    commonExcuteBySqlIdRequest.setInputParam(map);
                    CommonExcuteBySqlIdResponse commonExcuteBySqlIdResponse = this.exportDataService.commonExcuteBySqlId(commonExcuteBySqlIdRequest);
                    ExceptionUtil.checkDubboException(commonExcuteBySqlIdResponse);
                    List results = commonExcuteBySqlIdResponse.getResults();
                    if (results != null && results.size() > 0) {
                        simpleConfig.setSystemName(((Map)results.get(0)).get("system_name").toString());
                    } else {
                        simpleConfig.setSystemName("??????????????????,???????????????????????????id???" + simpleConfig.getSystemName());
                    }
                }

                simpleConfigByTopicAndTagGetResponse.setTotal(count);
                simpleConfigByTopicAndTagGetResponse.setSimpleConfig(topicList);
                break;
            }
        } catch (Exception e) {
            ExceptionUtil.processException(e, simpleConfigByTopicAndTagGetResponse);
        }

        return simpleConfigByTopicAndTagGetResponse;
    }

    public MessageOnlineFlagUpdateResponse updateMessageOnlineFlag(MessageOnlineFlagUpdateRequest messageOnlineFlagUpdateRequest) {
        MessageOnlineFlagUpdateResponse messageOnlineFlagUpdateResponse = new MessageOnlineFlagUpdateResponse();

        try {
            this.platformMqTopicDao.updateCancelSign(messageOnlineFlagUpdateRequest.getSeries(), messageOnlineFlagUpdateRequest.getOnlineFlag());
            RocketMqFlushModel rocketMqFlushModel = new RocketMqFlushModel();
            rocketMqFlushModel.setFlushFlag(messageOnlineFlagUpdateRequest.getOnlineFlag());
            rocketMqFlushModel.setSeries(messageOnlineFlagUpdateRequest.getSeries());
            AbstractRocketMqUtil.send(ExceptionUtils.FLUSH_MQ_TOPIC, ExceptionUtils.FLUSH_MQ_TAG, messageOnlineFlagUpdateRequest.getSeries().toString(), JSONObject.toJSONString(rocketMqFlushModel));
            if (this.dataSign == (long)SeqUtil.test) {
                AbstractRocketMqUtil.d(ExceptionUtils.FLUSH_MQ_TOPIC, ExceptionUtils.FLUSH_MQ_TAG, messageOnlineFlagUpdateRequest.getSeries().toString(), JSONObject.toJSONString(rocketMqFlushModel));
            }
        } catch (Exception e) {
            ExceptionUtil.processException(e, messageOnlineFlagUpdateResponse);
        }

        return messageOnlineFlagUpdateResponse;
    }

    /**
     * ?????????????????????????????????
     * @param messageDetailRequest
     * @return
     */
    public MessageDetailResponse findTransFailedMessageDetail(MessageDetailRequest messageDetailRequest) {
        MessageDetailResponse messageDetailResponse = new MessageDetailResponse();

        try {
            messageDetailRequest.setConsumerSuccess("");
            String condition = this.assembleCondition(messageDetailRequest);
            condition = condition + " and step_id=8";
            Long count = this.sysTransationFailedLogDao.queryCount(condition);
            condition = condition + " order by create_dtme desc limit " + (messageDetailRequest.getPageNo() - 1L) * messageDetailRequest.getPageCount() + " , " + messageDetailRequest.getPageCount();
            List<MessageDetails> list = this.sysTransationFailedLogDao.query(condition);

            for(int i = 0; i < list.size(); ++i) {
                MessageDetails messageDetails = (MessageDetails)list.get(i);
                if (StringUtil.isAllNotNullOrBlank(new String[]{messageDetails.getSendType()})) {
                    String sendType = (String) Constants.sendTypeDetails.get(Integer.valueOf(messageDetails.getSendType()));
                    messageDetails.setSendType(sendType);
                }

                List<PlatformMqTopic> topicList = this.platformMqTopicDao.queryByTopicTag(((MessageDetails)list.get(i)).getTopic(), ((MessageDetails)list.get(i)).getTag(), messageDetailRequest.getTenantNumId().toString());
                if (topicList != null && !topicList.isEmpty()) {
                    messageDetails.setRemark(((PlatformMqTopic)topicList.get(0)).getREMARK());
                }
            }

            messageDetailResponse.setMessageList(list);
            messageDetailResponse.setTotal(count);
            logger.info("???????????????????????????:" + messageDetailResponse.toJson());
        } catch (Exception e) {
            ExceptionUtil.processException(e, messageDetailResponse);
        }

        return messageDetailResponse;
    }

    public TransFailedMessageRetryResponse retryTransFailedMessage(TransFailedMessageRetryRequest transFailedMessageRetryRequest) {
        TransFailedMessageRetryResponse transFailedMessageRetryResponse = new TransFailedMessageRetryResponse();

        try {
            TransMessageRetryRequest transMessageRetryRequest = new TransMessageRetryRequest();
            transMessageRetryRequest.setTopic(transFailedMessageRetryRequest.getTopic());
            transMessageRetryRequest.setTag(transFailedMessageRetryRequest.getTag());
            transMessageRetryRequest.setTenantNumId(transFailedMessageRetryRequest.getTenantNumId());
            transMessageRetryRequest.setDataSign(transFailedMessageRetryRequest.getDataSign());
            TransMessageRetryResponse transMessageRetryResponse = this.messageSendService.retryTransMessage(transMessageRetryRequest);
            ExceptionUtil.checkDubboException(transMessageRetryResponse);
        } catch (Exception e) {
            ExceptionUtil.processException(e, transFailedMessageRetryResponse);
        }

        return transFailedMessageRetryResponse;
    }

    private MessageWebQueryServiceImpl.ConsumerInfo getConsumerInfo(DefaultMQAdminExt mqAdminExt, String consumerGroup) {
        MessageWebQueryServiceImpl.ConsumerInfo consumerInfo = new MessageWebQueryServiceImpl.ConsumerInfo();
        consumerInfo.setHasConsumer(true);

        try {
            ConsumerConnection consumerConnection = mqAdminExt.examineConsumerConnectionInfo(consumerGroup);
            ArrayList clientIdList = new ArrayList();
            Iterator connectionSetIterator = consumerConnection.getConnectionSet().iterator();

            while(connectionSetIterator.hasNext()) {
                Connection connection = (Connection)connectionSetIterator.next();
                clientIdList.add(connection.getClientId());
            }

            consumerInfo.setClientIdList(clientIdList);
        } catch (Exception e) {
            consumerInfo.setHasConsumer(false);
        }

        return consumerInfo;
    }

    /**
     * ???????????????????????????
     * @param messageDiffGetRequest
     * @return
     */
    public MessageDiffGetResponse getMessageDiff(MessageDiffGetRequest messageDiffGetRequest) {
        MessageDiffGetResponse messageDiffGetResponse = new MessageDiffGetResponse();
        DefaultMQAdminExt mqAdminExt = null;
        String consumerGroup = messageDiffGetRequest.getTopic() + "-" + messageDiffGetRequest.getTag();
        ArrayList messageDiffModelList = new ArrayList();

        try {
            mqAdminExt = new DefaultMQAdminExt();
            mqAdminExt.setNamesrvAddr(this.defaultName);
            mqAdminExt.setInstanceName(Long.toString(System.currentTimeMillis()));
            mqAdminExt.setHeartbeatBrokerInterval(30000);
            mqAdminExt.start();
            ConsumeStats consumeStats = mqAdminExt.examineConsumeStats(consumerGroup);
            LinkedList messageQueueList = new LinkedList();
            messageQueueList.addAll(consumeStats.getOffsetTable().keySet());
            Collections.sort(messageQueueList);
            long totalDiff = 0L;

            long diff;
            for(Iterator i = messageQueueList.iterator(); i.hasNext(); totalDiff += diff) {
                MessageQueue messageQueue = (MessageQueue)i.next();
                MessageDiffModel messageDiffModel = new MessageDiffModel();
                OffsetWrapper offsetWrapper = (OffsetWrapper)consumeStats.getOffsetTable().get(messageQueue);
                diff = offsetWrapper.getBrokerOffset() - offsetWrapper.getConsumerOffset();
                messageDiffModel.setBrokerName(messageQueue.getBrokerName());
                messageDiffModel.setQueueId(messageQueue.getQueueId());
                messageDiffModel.setTag(messageDiffGetRequest.getTag());
                messageDiffModel.setTopic(messageQueue.getTopic());
                String lastTime = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(offsetWrapper.getLastTimestamp());
                if ("1970-01-01 08:00:00".equals(lastTime)) {
                    messageDiffModel.setLastTime("");
                } else {
                    messageDiffModel.setLastTime(lastTime);
                }

                messageDiffModel.setDiff(diff);
                messageDiffModelList.add(messageDiffModel);
            }

            MessageWebQueryServiceImpl.ConsumerInfo consumerInfo = this.getConsumerInfo(mqAdminExt, consumerGroup);
            if (!consumerInfo.getHasConsumer()) {
                messageDiffGetResponse.setConsumerInfo("?????????????????????????????????!");
            } else {
                String consumerInfoDetail = "?????????????????????????????????~ ????????????IP???:";

                String url;
                for(Iterator iterator = consumerInfo.getClientIdList().iterator(); iterator.hasNext(); consumerInfoDetail = consumerInfoDetail + url) {
                    url = (String)iterator.next();
                    if (url.contains("@")) {
                        url = url.substring(0, url.indexOf("@")) + ";";
                    }
                }

                messageDiffGetResponse.setConsumerInfo(consumerInfoDetail);
            }

            messageDiffGetResponse.setMessageDiffModel(messageDiffModelList);
            messageDiffGetResponse.setTotalDiff(totalDiff);
        } catch (Exception e) {
            ExceptionUtil.processException(e, messageDiffGetResponse);
        } finally {
            if (mqAdminExt != null) {
                mqAdminExt.shutdown();
            }

        }

        return messageDiffGetResponse;
    }

    /**
     * ??????topic
     * @param topicRegistRequest
     * @return
     */
    public TopicRegistResponse registTopic(TopicRegistRequest topicRegistRequest) {
        TopicRegistResponse topicRegistResponse = new TopicRegistResponse();

        try {
            List<TopicConfigModel> topicList = this.platformMqTopicDao.getAllTopic();
            Iterator iterator = topicList.iterator();

            while(iterator.hasNext()) {
                TopicConfigModel topicConfigModel = (TopicConfigModel)iterator.next();
                TopicCreateRequest topicCreateRequest = new TopicCreateRequest();
                topicCreateRequest.setTopic(topicConfigModel.getTopic());
                topicCreateRequest.setNameSrv(this.defaultName);
                this.messageSendService.createTopic(topicCreateRequest);
            }
        } catch (Exception e) {
            ExceptionUtil.processException(e, topicRegistResponse);
        }

        return topicRegistResponse;
    }

    /**
     * ???????????????
     * @param messageCountGetRequest
     * @return
     */
    public MessageCountGetResponse getMessageCount(MessageCountGetRequest messageCountGetRequest) {
        MessageCountGetResponse messageCountGetResponse = new MessageCountGetResponse();
        Long count = 0L;
        Long hisCount = 0L;

        try {
            count = this.sysRocketMqSendLogDao.queryCount(messageCountGetRequest.getTopic(), messageCountGetRequest.getTag(), messageCountGetRequest.getMessageKey(), messageCountGetRequest.getTenantNumId(), messageCountGetRequest.getDataSign());
            hisCount = this.sysRocketMqSendLogHistoryDao.queryCount(messageCountGetRequest.getTopic(), messageCountGetRequest.getTag(), messageCountGetRequest.getMessageKey(), messageCountGetRequest.getTenantNumId(), messageCountGetRequest.getDataSign());
            messageCountGetResponse.setTotal(count + hisCount);
        } catch (Exception e) {
            ExceptionUtil.processException(e, messageCountGetResponse);
        }

        return messageCountGetResponse;
    }

    class ConsumerInfo {
        private List<String> clientIdList;
        private Boolean hasConsumer;

        public List<String> getClientIdList() {
            return this.clientIdList;
        }

        public void setClientIdList(List<String> clientIdList) {
            this.clientIdList = clientIdList;
        }

        public Boolean getHasConsumer() {
            return this.hasConsumer;
        }

        public void setHasConsumer(Boolean hasConsumer) {
            this.hasConsumer = hasConsumer;
        }
    }
}


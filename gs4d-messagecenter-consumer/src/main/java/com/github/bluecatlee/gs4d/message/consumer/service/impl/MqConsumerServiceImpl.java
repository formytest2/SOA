package com.github.bluecatlee.gs4d.message.consumer.service.impl;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.TopicConfig;
import com.alibaba.rocketmq.common.protocol.route.BrokerData;
import com.alibaba.rocketmq.common.protocol.route.QueueData;
import com.alibaba.rocketmq.common.protocol.route.TopicRouteData;
import com.alibaba.rocketmq.common.subscription.SubscriptionGroupConfig;
import com.alibaba.rocketmq.remoting.exception.RemotingException;
import com.alibaba.rocketmq.tools.admin.DefaultMQAdminExt;
import com.alibaba.rocketmq.tools.admin.MQAdminExt;
import com.alibaba.rocketmq.tools.command.CommandUtil;
import com.github.bluecatlee.gs4d.common.utils.Md5Util;
import com.github.bluecatlee.gs4d.common.utils.StringUtil;
import com.github.bluecatlee.gs4d.message.api.service.MessageCenterSendService;
import com.github.bluecatlee.gs4d.message.api.utils.IPUtil;
import com.github.bluecatlee.gs4d.message.consumer.dao.*;
import com.github.bluecatlee.gs4d.message.consumer.model.PlatformMqDubboConsumer;
import com.github.bluecatlee.gs4d.message.consumer.model.PlatformMqHttpConsumer;
import com.github.bluecatlee.gs4d.message.consumer.model.PlatformMqTopic;
import com.github.bluecatlee.gs4d.message.consumer.service.MqConsumerService;
import com.github.bluecatlee.gs4d.message.consumer.service.MqDubboConsumerService;
import com.github.bluecatlee.gs4d.message.consumer.service.UpdateConsumerResultService;
import com.github.bluecatlee.gs4d.message.consumer.utils.AbstractRocketMqUtil;
import com.github.bluecatlee.gs4d.message.consumer.utils.Constants;
import com.github.bluecatlee.gs4d.message.consumer.utils.RocketMqConfigUtil;
import com.github.bluecatlee.gs4d.message.consumer.utils.SeqUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.*;
import java.util.Map.Entry;

@Service("mqConsumerService")
public class MqConsumerServiceImpl implements MqConsumerService {
    
    private static Logger logger = LoggerFactory.getLogger(MqConsumerServiceImpl.class);
    
    @Autowired
    SysRocketMqConsumeLogDao sysRocketMqConsumeLogDao;

    @Value("#{settings['initalConsumerFlag']}")
    private String initalConsumerFlag;
    @Value("#{settings['dataSign']}")
    private int dataSign;
    @Value("#{settings['consumerFiledToopic']}")
    private String consumerFiledToopic;
    @Value("#{settings['consumerFiledTag']}")
    private String consumerFiledTag;
    @Value("#{settings['messagecenter.count']}")
    private Integer messageCenterCount;
    @Value("${aliyunMqAccessKey}")
    private String aliyunMqAccessKey;
    @Value("${aliyunMqSecretKey}")
    private String aliyunMqSecretKey;
    @Value("${aliyunMqUrl}")
    private String aliyunMqUrl;
    @Value("${useAliyunMq}")
    private String useAliyunMq;
    @Value("${noConsumerTag}")
    private String noConsumerTag;
    @Value("#{settings['zookeeper.host.port']}")
    private String zookeeperHostPort;

    public static Map<String, DefaultMQAdminExt> namesrvAddr2MqAdminExt = new HashMap();

    @Autowired
    private MessageCenterSendService messageSendService;

    @Autowired
    private SysRocketMqSendLogDao sysRocketMqSendLogDao;

    @Autowired
    private SysRocketMqConsumeFailedlogDao sysRocketMqConsumeFailedlogDao;

    @Autowired
    private SysRocketMqSendLogHistoryDao sysRocketMqSendLogHistoryDao;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    @Autowired
    private PlaftformMqTopicManyDao plaftformMqTopicManyDao;

    @Autowired
    private PlatformMqTopicDao platformMqTopicDao;

    @Autowired
    private PlatformMqDubboConsumerDao platformMqDubboConsumerDao;

    @Autowired
    private MqDubboConsumerService mqDubboConsumerService;

    @Autowired
    private PlatformMqHttpConsumerDao platformMqHttpConsumerDao;

    @Autowired
    private RocketMqConfigUtil rocketMqConfigUtil;

    @Autowired
    private UpdateConsumerResultService updateConsumerResultService;

    private static String ip = IPUtil.getIP();

    public void initalAllConsumer() {
        boolean flag = false;

        try {
            flag = true;
            logger.info("?????????rocketmq???????????????");
            List<DefaultMQPushConsumer> mqPushConsumerList = new ArrayList();
            List<PlatformMqTopic> platformMqTopicList = this.platformMqTopicDao.queryAll();
            logger.info("????????????topic????????????topic????????????" + platformMqTopicList.size());
            Iterator platformMqTopicListIterator = platformMqTopicList.iterator();

            while(true) {
                if (!platformMqTopicListIterator.hasNext()) {
                    Iterator mqPushConsumerListIterator = mqPushConsumerList.iterator();

                    while(mqPushConsumerListIterator.hasNext()) {
                        DefaultMQPushConsumer mqPushConsumer = (DefaultMQPushConsumer)mqPushConsumerListIterator.next();

                        try {
                            mqPushConsumer.start();     // ??????mq?????????
                            Constants.consumerGroupTable.put(mqPushConsumer.getConsumerGroup(), mqPushConsumer);
                        } catch (Exception e) {
                            logger.error(e.getMessage(), e);
                            throw new RuntimeException(e);
                        }
                    }

                    logger.info("?????????rocketmq???????????????");
                    flag = false;
                    break;
                }

                PlatformMqTopic platformMqTopic = (PlatformMqTopic)platformMqTopicListIterator.next();
                this.initalConsumer(platformMqTopic, mqPushConsumerList);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (flag) {
                Iterator iterator = namesrvAddr2MqAdminExt.values().iterator();

                while(iterator.hasNext()) {
                    DefaultMQAdminExt mqAdminExt = (DefaultMQAdminExt)iterator.next();
                    mqAdminExt.shutdown();
                }

                namesrvAddr2MqAdminExt.clear();
            }
        }

        Iterator iterator = namesrvAddr2MqAdminExt.values().iterator();

        while(iterator.hasNext()) {
            DefaultMQAdminExt mqAdminExt = (DefaultMQAdminExt)iterator.next();
            mqAdminExt.shutdown();
        }

        namesrvAddr2MqAdminExt.clear();
    }

    private void initalSpecialDubboMethod(int consumerType, Long consumerSeries) {
        if ((long)consumerType == 1L) { // ??????????????? 1dubbo 2http
            this.mqDubboConsumerService.initalSpecialDubboMethod(consumerSeries);
        }

    }

    /**
     * ?????????MqAdminExt?????????
     * @param namesrvAddr
     * @throws MQClientException
     */
    private void initMqAdminExt(String namesrvAddr) throws MQClientException {
        if (namesrvAddr2MqAdminExt.get(namesrvAddr) == null) {
            DefaultMQAdminExt mqAdminExt = new DefaultMQAdminExt();
            mqAdminExt.setNamesrvAddr(namesrvAddr);
            mqAdminExt.setInstanceName(Long.toString(System.currentTimeMillis()));
            mqAdminExt.setHeartbeatBrokerInterval(30000);
            mqAdminExt.start();
            namesrvAddr2MqAdminExt.put(namesrvAddr, mqAdminExt);
            logger.info("?????????defaultMQAdminExt??????");
        }
    }

    /**
     * ????????? DefaultMQPushConsumer ????????????????????????
     * @param platformMqTopic
     * @param platformMqDubboConsumer   dubbo?????????
     * @param flag
     * @return
     */
    private DefaultMQPushConsumer initalMqPushConsumerWithMessageListener(PlatformMqTopic platformMqTopic, PlatformMqDubboConsumer platformMqDubboConsumer, boolean flag) {
        DefaultMQPushConsumer mqPushConsumer = null;
        Integer retries = 0;
        if (SeqUtil.test == this.dataSign && StringUtil.isAllNotNullOrBlank(new Object[]{platformMqTopic.getRETRIES_TEST()})) {
            retries = platformMqTopic.getRETRIES_TEST();
        } else if (SeqUtil.prod == this.dataSign && StringUtil.isAllNotNullOrBlank(new Object[]{platformMqTopic.getRETRIES()})) {
            retries = platformMqTopic.getRETRIES();
        } else if (SeqUtil.develop == this.dataSign && StringUtil.isAllNotNullOrBlank(new Object[]{platformMqTopic.getRETRIES_DEVELOP()})) {
            retries = platformMqTopic.getRETRIES_DEVELOP();
        }

        try {
            if (StringUtil.isAllNotNullOrBlank(new String[]{platformMqTopic.getWETHER_ORDER_MESS()}) && platformMqTopic.getWETHER_ORDER_MESS().equals("Y")) {
                mqPushConsumer = this.initalMqPushConsumerCore(platformMqTopic, flag);
                MessageListenerOrderlyImpl4DubboConsumer messageListenerOrderly = new MessageListenerOrderlyImpl4DubboConsumer(platformMqTopic, platformMqDubboConsumer, this.dataSign, retries, platformMqTopic.getZK_DATA_SIGN(), this.updateConsumerResultService);
                mqPushConsumer.setMessageListener(messageListenerOrderly);
            } else {
                mqPushConsumer = this.initalMqPushConsumerCore(platformMqTopic, flag);
                MessageListenerConcurrentlyImpl4DubboConsumer messageListenerConcurrently = new MessageListenerConcurrentlyImpl4DubboConsumer(platformMqTopic, platformMqDubboConsumer, this.dataSign, retries, platformMqTopic.getZK_DATA_SIGN(), this.updateConsumerResultService, this.noConsumerTag);
                mqPushConsumer.setMessageListener(messageListenerConcurrently);
            }

            return mqPushConsumer;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    /**
     * ????????? DefaultMQPushConsumer ????????????????????????
     * @param platformMqTopic
     * @param platformMqHttpConsumer    http?????????
     * @param flag
     * @return
     */
    private DefaultMQPushConsumer initalMqPushConsumerWithMessageListener(PlatformMqTopic platformMqTopic, PlatformMqHttpConsumer platformMqHttpConsumer, boolean flag) {
        DefaultMQPushConsumer mqPushConsumer = null;
        Integer retries = 0;
        if (SeqUtil.test == this.dataSign && StringUtil.isAllNotNullOrBlank(new Object[]{platformMqTopic.getRETRIES_TEST()})) {
            retries = platformMqTopic.getRETRIES_TEST();
        } else if (SeqUtil.prod == this.dataSign && StringUtil.isAllNotNullOrBlank(new Object[]{platformMqTopic.getRETRIES()})) {
            retries = platformMqTopic.getRETRIES();
        } else if (SeqUtil.develop == this.dataSign && StringUtil.isAllNotNullOrBlank(new Object[]{platformMqTopic.getRETRIES_DEVELOP()})) {
            retries = platformMqTopic.getRETRIES_DEVELOP();
        }

        try {
            mqPushConsumer = this.initalMqPushConsumerCore(platformMqTopic, flag);
            MessageListenerConcurrentlyImpl4HttpConsumer httpMessageListener = new MessageListenerConcurrentlyImpl4HttpConsumer(platformMqTopic, platformMqHttpConsumer, this.dataSign, retries, platformMqTopic.getZK_DATA_SIGN());
            mqPushConsumer.setMessageListener(httpMessageListener);
            return mqPushConsumer;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    /**
     * ????????? DefaultMQPushConsumer ??????
     * @param platformMqTopic
     * @param flag
     * @return
     * @throws Exception
     */
    public DefaultMQPushConsumer initalMqPushConsumerCore(PlatformMqTopic platformMqTopic, boolean flag) throws Exception {
        String consumerGroup = platformMqTopic.getTOPIC() + "-" + platformMqTopic.getTAG();
        DefaultMQPushConsumer mqPushConsumer = new DefaultMQPushConsumer(consumerGroup);
        mqPushConsumer.setNamesrvAddr(platformMqTopic.getMQ_NAMESRV());
        mqPushConsumer.setInstanceName((new Date()).getTime() + Md5Util.md5(UUID.randomUUID().toString()));
        mqPushConsumer.subscribe(platformMqTopic.getTOPIC(), platformMqTopic.getTAG());     // ??????tag??????
        mqPushConsumer.setPullBatchSize(platformMqTopic.getMESS_BATCH_NUM());               // ????????????????????????
        mqPushConsumer.setConsumeMessageBatchMaxSize(platformMqTopic.getMESS_BATCH_NUM());  // ??????????????????????????????
        mqPushConsumer.setHeartbeatBrokerInterval(30000);
        if (platformMqTopic.getCONSUMER_PULL_DELAY() != null && platformMqTopic.getCONSUMER_PULL_DELAY() != 0L) {
            mqPushConsumer.setPullInterval(platformMqTopic.getCONSUMER_PULL_DELAY());
        }

        Integer queueNums = this.getQueueNums(platformMqTopic.getMQ_NAMESRV(), platformMqTopic.getTOPIC(), platformMqTopic.getMQ_QUEUE());
        Integer n = queueNums / this.messageCenterCount;
        if (n < platformMqTopic.getCONSUMER_THREAD_MIN()) {
            mqPushConsumer.setConsumeThreadMin(n);
            mqPushConsumer.setConsumeThreadMax(n + 10);
        } else {
            mqPushConsumer.setConsumeThreadMin(platformMqTopic.getCONSUMER_THREAD_MIN());
            mqPushConsumer.setConsumeThreadMax(platformMqTopic.getCONSUMER_THREAD_MAX());
        }

        if (SeqUtil.test == this.dataSign && StringUtil.isAllNotNullOrBlank(new Object[]{platformMqTopic.getRETRIES_TEST()})) {
            this.updateRetriesOfConsumerGroup(platformMqTopic.getMQ_NAMESRV(), consumerGroup, platformMqTopic.getRETRIES_TEST());
        } else if (SeqUtil.prod == this.dataSign && StringUtil.isAllNotNullOrBlank(new Object[]{platformMqTopic.getRETRIES()})) {
            this.updateRetriesOfConsumerGroup(platformMqTopic.getMQ_NAMESRV(), consumerGroup, platformMqTopic.getRETRIES());
        } else if (SeqUtil.develop == this.dataSign && StringUtil.isAllNotNullOrBlank(new Object[]{platformMqTopic.getRETRIES_DEVELOP()})) {
            this.updateRetriesOfConsumerGroup(platformMqTopic.getMQ_NAMESRV(), consumerGroup, platformMqTopic.getRETRIES_DEVELOP());
        }

        return mqPushConsumer;
    }

    private Integer getQueueNums(String namesrvAddr, String topic, Integer queueNums) throws Exception {
        try {
            TopicRouteData topicRouteData = ((DefaultMQAdminExt)namesrvAddr2MqAdminExt.get(namesrvAddr)).examineTopicRouteInfo(topic);

            for(int i = 0; i < topicRouteData.getQueueDatas().size(); ++i) {
                Integer writeQueueNums = ((QueueData)topicRouteData.getQueueDatas().get(i)).getWriteQueueNums();
                if (queueNums >= writeQueueNums && writeQueueNums != queueNums) {
                    for(int j = 0; j < topicRouteData.getBrokerDatas().size(); ++j) {
                        if (((QueueData)topicRouteData.getQueueDatas().get(i)).getBrokerName().equals(((BrokerData)topicRouteData.getBrokerDatas().get(j)).getBrokerName())) {
                            TopicConfig topicConfig = new TopicConfig();
                            topicConfig.setWriteQueueNums(queueNums);
                            topicConfig.setReadQueueNums(queueNums);
                            topicConfig.setTopicName(topic);
                            Iterator brokerAddrsIterator = ((BrokerData)topicRouteData.getBrokerDatas().get(j)).getBrokerAddrs().entrySet().iterator();

                            while(brokerAddrsIterator.hasNext()) {
                                Entry entry = (Entry)brokerAddrsIterator.next();
                                ((DefaultMQAdminExt)namesrvAddr2MqAdminExt.get(namesrvAddr)).createAndUpdateTopicConfig((String)entry.getValue(), topicConfig);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            if (e.getMessage().contains("???????????????????????????")) {
                throw e;
            }

            logger.debug("??????topic???" + topic + "???????????????");
        }

        return queueNums;
    }

    /**
     * ??????????????????????????????
     * @param mqNamesrv
     * @param consumerGroup
     * @param retries
     * @throws InterruptedException
     * @throws MQBrokerException
     * @throws RemotingException
     * @throws MQClientException
     */
    private void updateRetriesOfConsumerGroup(String mqNamesrv, String consumerGroup, Integer retries) throws InterruptedException, MQBrokerException, RemotingException, MQClientException {
        String clusterName = "";
        List brokerAddrList = RocketMqConfigUtil.namesrvToBrokerAddrs.get(mqNamesrv);

        for(int i = 0; i < brokerAddrList.size(); ++i) {
            if (i == 0) {
                clusterName = clusterName + (String)brokerAddrList.get(i);
            } else {
                clusterName = clusterName + ";" + (String)brokerAddrList.get(i);
            }
        }

        SubscriptionGroupConfig subscriptionGroupConfig = new SubscriptionGroupConfig();
        subscriptionGroupConfig.setConsumeBroadcastEnable(false);       // ????????????
        subscriptionGroupConfig.setConsumeFromMinEnable(false);
        subscriptionGroupConfig.setRetryMaxTimes(retries);
        subscriptionGroupConfig.setRetryQueueNums(1);
        subscriptionGroupConfig.setGroupName(consumerGroup);
        Set masterAddrSet = CommandUtil.fetchMasterAddrByClusterName((MQAdminExt)namesrvAddr2MqAdminExt.get(mqNamesrv), clusterName);
        Iterator iterator = masterAddrSet.iterator();

        while(iterator.hasNext()) {
            String masterAddr = (String)iterator.next();
            ((DefaultMQAdminExt)namesrvAddr2MqAdminExt.get(mqNamesrv)).createAndUpdateSubscriptionGroupConfig(masterAddr, subscriptionGroupConfig);
        }

    }

    public void initalConsumer(PlatformMqTopic platformMqTopic, List<DefaultMQPushConsumer> mqPushConsumerList) throws Exception {
        String namesrvAddr;
        if (StringUtil.isAllNotNullOrBlank(new String[]{platformMqTopic.getMQ_NAMESRV()})) {
            String topicTag = platformMqTopic.getTOPIC() + "#" + platformMqTopic.getTAG() + "#" + platformMqTopic.getTENANT_NUM_ID();
            namesrvAddr = this.rocketMqConfigUtil.getNsRealAddress(platformMqTopic.getMQ_NAMESRV());
            if (namesrvAddr == null) {
                throw new Exception("?????????????????????????????????namesrv?????????tag:" + platformMqTopic.getTAG() + "??????????????????" + platformMqTopic.getMQ_NAMESRV());
            }

            platformMqTopic.setMQ_NAMESRV(namesrvAddr);                 // ???platformMqTopic??????MQ_NAMESRV(?????????)????????????????????? ????????????????????????
            Constants.topicTagToNamesrv.put(topicTag, namesrvAddr);
        } else {
            platformMqTopic.setMQ_NAMESRV(AbstractRocketMqUtil.defaultNamesrv);
        }

        if (StringUtil.isAllNotNullOrBlank(new Object[]{platformMqTopic.getCONSUMER_INTERVAL()}) && platformMqTopic.getCONSUMER_INTERVAL() > 0L) {
            Constants.topicTagToConsumerInterval.put(platformMqTopic.getTOPIC() + "#" + platformMqTopic.getTAG(), platformMqTopic.getCONSUMER_INTERVAL().intValue());
        }

        String[] retryIntervalArr;
        List retryIntervalList;
        try {
            if (StringUtil.isAllNotNullOrBlank(new String[]{platformMqTopic.getRETRY_INTERVAL()})) {
                retryIntervalArr = platformMqTopic.getRETRY_INTERVAL().split(",");
                retryIntervalList = Arrays.asList(retryIntervalArr);
                Constants.topicTagToRetryIntervalList.put(platformMqTopic.getTOPIC() + "#" + platformMqTopic.getTAG(), retryIntervalList);
            }
        } catch (Exception e) {
            logger.error(platformMqTopic.getTOPIC() + "#" + platformMqTopic.getTAG() + "???retryInterval???????????????????????????????????????????????????topic??????", e);
        }

        String[] correctCodesArr;
        List correctCodesList;
        try {
            if (StringUtil.isAllNotNullOrBlank(new String[]{platformMqTopic.getCORRECT_CODES()})) {
                correctCodesArr = platformMqTopic.getCORRECT_CODES().split(",");
                correctCodesList = Arrays.asList(correctCodesArr);
                Constants.topicTagToCorrectCodesList.put(platformMqTopic.getTOPIC() + "#" + platformMqTopic.getTAG(), correctCodesList);
            }
        } catch (Exception e) {
            logger.error(platformMqTopic.getTOPIC() + "#" + platformMqTopic.getTAG() + "???error???????????????????????????????????????????????????topic??????", e);
        }

        try {
            if (StringUtil.isAllNotNullOrBlank(new Object[]{platformMqTopic.getZK_DATA_SIGN()}) && platformMqTopic.getZK_DATA_SIGN() != SeqUtil.prod) {
                this.initalSpecialDubboMethod(platformMqTopic.getCONSUMER_TYPE(), platformMqTopic.getCONSUMER_SERIES());
            }
        } catch (Exception e) {
            logger.error(platformMqTopic.getTOPIC() + "#" + platformMqTopic.getTAG() + "????????????zk???????????????", e);
        }

        logger.info("?????????????????????");
        this.initMqAdminExt(platformMqTopic.getMQ_NAMESRV());

        if (1 == platformMqTopic.getCONSUMER_TYPE()) {  // dubbo?????????
            PlatformMqDubboConsumer platformMqDubboConsumer = this.platformMqDubboConsumerDao.queryBySeries(platformMqTopic.getCONSUMER_SERIES());
            if (platformMqDubboConsumer == null) {
                logger.error("PLATFORM_MQ_TOPIC???????????????" + platformMqTopic.getSERIES() + "?????????dubbo????????????????????????");
                return;
            }

            String zkAddr = this.zookeeperHostPort;
            if (SeqUtil.test == this.dataSign && StringUtil.isAllNotNullOrBlank(new String[]{zkAddr})) {
                mqPushConsumerList.add(this.initalMqPushConsumerWithMessageListener(platformMqTopic, platformMqDubboConsumer, true));
            } else if (SeqUtil.prod == this.dataSign && StringUtil.isAllNotNullOrBlank(new String[]{zkAddr})) {
                mqPushConsumerList.add(this.initalMqPushConsumerWithMessageListener(platformMqTopic, platformMqDubboConsumer, false));
            } else if (SeqUtil.develop == this.dataSign && StringUtil.isAllNotNullOrBlank(new String[]{zkAddr})) {
                mqPushConsumerList.add(this.initalMqPushConsumerWithMessageListener(platformMqTopic, platformMqDubboConsumer, false));
            }
        } else if (2 == platformMqTopic.getCONSUMER_TYPE()) {   // http?????????
            PlatformMqHttpConsumer platformMqHttpConsumer = this.platformMqHttpConsumerDao.queryBySeries(platformMqTopic.getCONSUMER_SERIES());
            if (platformMqHttpConsumer == null) {
                logger.error("PLATFORM_MQ_TOPIC???????????????" + platformMqTopic.getSERIES() + "?????????http????????????????????????");
                return;
            }

            if (SeqUtil.test == this.dataSign && StringUtil.isAllNotNullOrBlank(new String[]{platformMqHttpConsumer.getURL_TEST()})) {
                mqPushConsumerList.add(this.initalMqPushConsumerWithMessageListener(platformMqTopic, platformMqHttpConsumer, true));
            } else if (SeqUtil.develop == this.dataSign && StringUtil.isAllNotNullOrBlank(new String[]{platformMqHttpConsumer.getURL_DEVELOP()})) {
                mqPushConsumerList.add(this.initalMqPushConsumerWithMessageListener(platformMqTopic, platformMqHttpConsumer, false));
            } else if (SeqUtil.prod == this.dataSign && StringUtil.isAllNotNullOrBlank(new String[]{platformMqHttpConsumer.getURL()})) {
                mqPushConsumerList.add(this.initalMqPushConsumerWithMessageListener(platformMqTopic, platformMqHttpConsumer, false));
            }
        }

    }
}

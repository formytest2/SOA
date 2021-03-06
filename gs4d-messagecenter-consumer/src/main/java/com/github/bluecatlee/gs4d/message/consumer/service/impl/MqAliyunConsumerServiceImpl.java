package com.github.bluecatlee.gs4d.message.consumer.service.impl;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.aliyun.openservices.ons.api.Consumer;
import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.batch.BatchConsumer;
import com.aliyun.openservices.ons.api.order.OrderConsumer;
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
import com.github.bluecatlee.gs4d.message.consumer.utils.Constants;
import com.github.bluecatlee.gs4d.message.consumer.utils.RocketMqConfigUtil;
import com.github.bluecatlee.gs4d.message.consumer.utils.SeqUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

@Service("mqAliyunConsumerService")
public class MqAliyunConsumerServiceImpl implements MqConsumerService {
    
    private static Logger logger = LoggerFactory.getLogger(MqAliyunConsumerServiceImpl.class);
    
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
    @Value("#{settings['zookeeper.host.port']}")
    private String zookeeperHostPort;

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
        try {
            logger.info("?????????rocketmq???????????????");
            List platformMqTopicList = this.platformMqTopicDao.queryAll();
            logger.info("????????????topic????????????topic????????????" + platformMqTopicList.size());
            Iterator platformMqTopicListIterator = platformMqTopicList.iterator();

            while(true) {
                while(true) {
                    while(platformMqTopicListIterator.hasNext()) {
                        PlatformMqTopic platformMqTopic = (PlatformMqTopic)platformMqTopicListIterator.next();
                        if (StringUtil.isAllNotNullOrBlank(new Object[]{platformMqTopic.getCONSUMER_INTERVAL()}) && platformMqTopic.getCONSUMER_INTERVAL() > 0L) {
                            Constants.topicTagToConsumerInterval.put(platformMqTopic.getTOPIC() + "#" + platformMqTopic.getTAG(), platformMqTopic.getCONSUMER_INTERVAL().intValue());
                        }

                        try {
                            if (StringUtil.isAllNotNullOrBlank(new String[]{platformMqTopic.getCORRECT_CODES()})) {
                                String[] correctCodesArr = platformMqTopic.getCORRECT_CODES().split(",");
                                List correctCodesList = Arrays.asList(correctCodesArr);
                                Constants.topicTagToCorrectCodesList.put(platformMqTopic.getTOPIC() + "#" + platformMqTopic.getTAG(), correctCodesList);
                            }
                        } catch (Exception e) {
                            logger.error(platformMqTopic.getTOPIC() + "#" + platformMqTopic.getTAG() + "???error???????????????????????????????????????????????????topic??????", e);
                        }

                        logger.info("?????????????????????");
                        if (1 == platformMqTopic.getCONSUMER_TYPE()) {      // dubbo?????????
                            PlatformMqDubboConsumer platformMqDubboConsumer = this.platformMqDubboConsumerDao.queryBySeries(platformMqTopic.getCONSUMER_SERIES());
                            if (platformMqDubboConsumer == null) {
                                logger.error("PLATFORM_MQ_TOPIC???????????????" + platformMqTopic.getSERIES() + "?????????dubbo????????????????????????");
                            } else {
                                String zkAddr = this.zookeeperHostPort;
                                if (SeqUtil.test == this.dataSign && StringUtil.isAllNotNullOrBlank(new String[]{zkAddr})) {
                                    this.initalAliMqConsumerWithMessageListener(platformMqTopic, platformMqDubboConsumer, true);
                                } else if (SeqUtil.prod == this.dataSign && StringUtil.isAllNotNullOrBlank(new String[]{zkAddr})) {
                                    this.initalAliMqConsumerWithMessageListener(platformMqTopic, platformMqDubboConsumer, false);
                                } else if (SeqUtil.develop == this.dataSign && StringUtil.isAllNotNullOrBlank(new String[]{zkAddr})) {
                                    this.initalAliMqConsumerWithMessageListener(platformMqTopic, platformMqDubboConsumer, false);
                                }
                            }
                        } else if (2 == platformMqTopic.getCONSUMER_TYPE()) {   // http?????????
                            PlatformMqHttpConsumer platformMqHttpConsumer = this.platformMqHttpConsumerDao.queryBySeries(platformMqTopic.getCONSUMER_SERIES());
                            if (platformMqHttpConsumer == null) {
                                logger.error("PLATFORM_MQ_TOPIC???????????????" + platformMqTopic.getSERIES() + "?????????http????????????????????????");
                            } else if (SeqUtil.test == this.dataSign && StringUtil.isAllNotNullOrBlank(new String[]{platformMqHttpConsumer.getURL_TEST()})) {
                                this.initalMqPushConsumerWithMessageListener(platformMqTopic, platformMqHttpConsumer, true);
                            } else if (SeqUtil.develop == this.dataSign && StringUtil.isAllNotNullOrBlank(new String[]{platformMqHttpConsumer.getURL_DEVELOP()})) {
                                this.initalMqPushConsumerWithMessageListener(platformMqTopic, platformMqHttpConsumer, false);
                            } else if (SeqUtil.prod == this.dataSign && StringUtil.isAllNotNullOrBlank(new String[]{platformMqHttpConsumer.getURL()})) {
                                this.initalMqPushConsumerWithMessageListener(platformMqTopic, platformMqHttpConsumer, false);
                            }
                        }
                    }

                    logger.info("?????????rocketmq???????????????");
                    return;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public DefaultMQPushConsumer initalMqPushConsumerCore(PlatformMqTopic platformMqTopic, boolean flag) throws Exception {
        throw new Exception("????????????????????????aliyunMq?????????,???????????????????????????rocketmq????????????");
    }

    /**
     * ???????????????????????????????????????????????????????????????
     * @param platformMqTopic
     * @param platformMqDubboConsumer
     * @param flag
     */
    private void initalAliMqConsumerWithMessageListener(PlatformMqTopic platformMqTopic, PlatformMqDubboConsumer platformMqDubboConsumer, boolean flag) {
        Integer retries = 0;
        if (SeqUtil.test == this.dataSign && StringUtil.isAllNotNullOrBlank(new Object[]{platformMqTopic.getRETRIES_TEST()})) {
            retries = platformMqTopic.getRETRIES_TEST();
            platformMqTopic.setRETRIES(retries);
        } else if (SeqUtil.prod == this.dataSign && StringUtil.isAllNotNullOrBlank(new Object[]{platformMqTopic.getRETRIES()})) {
            retries = platformMqTopic.getRETRIES();
            platformMqTopic.setRETRIES(retries);
        } else if (SeqUtil.develop == this.dataSign && StringUtil.isAllNotNullOrBlank(new Object[]{platformMqTopic.getRETRIES_DEVELOP()})) {
            retries = platformMqTopic.getRETRIES_DEVELOP();
            platformMqTopic.setRETRIES(retries);
        }

        try {
            Properties properties = new Properties();
            properties.put("ConsumerId", Constants.getAliyunMqConsumerId(platformMqTopic.getTOPIC(), platformMqTopic.getTAG()));
            properties.put("AccessKey", this.aliyunMqAccessKey);
            properties.put("SecretKey", this.aliyunMqSecretKey);
            properties.put("ONSAddr", this.aliyunMqUrl);
            properties.put("ConsumeThreadNums", platformMqTopic.getCONSUMER_THREAD_MIN());
            properties.put("maxReconsumeTimes", platformMqTopic.getRETRIES());
            properties.put("consumeTimeout", 100);
            if (StringUtil.isAllNotNullOrBlank(new String[]{platformMqTopic.getWETHER_ORDER_MESS()}) && platformMqTopic.getWETHER_ORDER_MESS().equals("Y")) {
                OrderConsumer orderConsumer = ONSFactory.createOrderedConsumer(properties);
                AliyunMessageOrderListenerImpl4DubboConsumer aliyunMessageOrderListener = new AliyunMessageOrderListenerImpl4DubboConsumer(platformMqTopic, platformMqDubboConsumer, this.dataSign, retries, platformMqTopic.getZK_DATA_SIGN(), this.updateConsumerResultService);
                orderConsumer.subscribe(platformMqTopic.getTOPIC(), platformMqTopic.getTAG(), aliyunMessageOrderListener);
                orderConsumer.start();
            } else if (StringUtil.isAllNotNullOrBlank(new Object[]{platformMqTopic.getMESS_BATCH_NUM()}) && platformMqTopic.getMESS_BATCH_NUM() > 1) {
                properties.put("ConsumeMessageBatchMaxSize", platformMqTopic.getMESS_BATCH_NUM());
                BatchConsumer batchConsumer = ONSFactory.createBatchConsumer(properties);
                AliyunBatchMessageListenerImpl4DubboConsumer aliyunBatchMessageListener = new AliyunBatchMessageListenerImpl4DubboConsumer(platformMqTopic, platformMqDubboConsumer, this.dataSign, retries, platformMqTopic.getZK_DATA_SIGN(), this.updateConsumerResultService);
                batchConsumer.subscribe(platformMqTopic.getTOPIC(), platformMqTopic.getTAG(), aliyunBatchMessageListener);
                batchConsumer.start();
            } else {
                Consumer consumer = ONSFactory.createConsumer(properties);
                AliyunMessageListenerImpl4DubboConsumer aliyunMessageListener = new AliyunMessageListenerImpl4DubboConsumer(platformMqTopic, platformMqDubboConsumer, this.dataSign, retries, platformMqTopic.getZK_DATA_SIGN(), this.updateConsumerResultService);
                consumer.subscribe(platformMqTopic.getTOPIC(), platformMqTopic.getTAG(), aliyunMessageListener);
                consumer.start();
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    /**
     * ???????????????????????????????????????????????????????????????
     * @param platformMqTopic
     * @param platformMqHttpConsumer
     * @param flag
     */
    private void initalMqPushConsumerWithMessageListener(PlatformMqTopic platformMqTopic, PlatformMqHttpConsumer platformMqHttpConsumer, boolean flag) {
        Integer retries = 0;
        if (SeqUtil.test == this.dataSign && StringUtil.isAllNotNullOrBlank(new Object[]{platformMqTopic.getRETRIES_TEST()})) {
            retries = platformMqTopic.getRETRIES_TEST();
            platformMqTopic.setRETRIES(retries);
        } else if (SeqUtil.prod == this.dataSign && StringUtil.isAllNotNullOrBlank(new Object[]{platformMqTopic.getRETRIES()})) {
            retries = platformMqTopic.getRETRIES();
            platformMqTopic.setRETRIES(retries);
        } else if (SeqUtil.develop == this.dataSign && StringUtil.isAllNotNullOrBlank(new Object[]{platformMqTopic.getRETRIES_DEVELOP()})) {
            retries = platformMqTopic.getRETRIES_DEVELOP();
            platformMqTopic.setRETRIES(retries);
        }

        try {
            Properties properties = new Properties();
            properties.put("ConsumerId", Constants.getAliyunMqConsumerId(platformMqTopic.getTOPIC(), platformMqTopic.getTAG()));
            properties.put("AccessKey", this.aliyunMqAccessKey);
            properties.put("SecretKey", this.aliyunMqSecretKey);
            properties.put("ONSAddr", this.aliyunMqUrl);
            properties.put("ConsumeThreadNums", platformMqTopic.getCONSUMER_THREAD_MIN());
            properties.put("maxReconsumeTimes", platformMqTopic.getRETRIES());
            properties.put("consumeTimeout", 10);
            if (StringUtil.isAllNotNullOrBlank(new String[]{platformMqTopic.getWETHER_ORDER_MESS()}) && platformMqTopic.getWETHER_ORDER_MESS().equals("Y")) {
                OrderConsumer orderConsumer = ONSFactory.createOrderedConsumer(properties);
                AliyunMessageOrderListenerImpl4HttpConsumer aliyunMessageOrderListener = new AliyunMessageOrderListenerImpl4HttpConsumer(platformMqTopic, platformMqHttpConsumer, this.messageSendService, this.sysRocketMqConsumeLogDao, this.dataSign, this.sysRocketMqSendLogDao, retries, this.sysRocketMqConsumeFailedlogDao, this.consumerFiledToopic, this.consumerFiledTag, this.sysRocketMqSendLogHistoryDao, this.platformTransactionManager, platformMqTopic.getZK_DATA_SIGN());
                orderConsumer.subscribe(platformMqTopic.getTOPIC(), platformMqTopic.getTAG(), aliyunMessageOrderListener);
                orderConsumer.start();
            } else {
                Consumer consumer = ONSFactory.createConsumer(properties);
                AliyunMessageListenerImpl4HttpConsumer aliyunMessageListener = new AliyunMessageListenerImpl4HttpConsumer(platformMqTopic, platformMqHttpConsumer, this.messageSendService, this.sysRocketMqConsumeLogDao, this.dataSign, this.sysRocketMqSendLogDao, retries, this.sysRocketMqConsumeFailedlogDao, this.consumerFiledToopic, this.consumerFiledTag, this.sysRocketMqSendLogHistoryDao, this.platformTransactionManager, platformMqTopic.getZK_DATA_SIGN());
                consumer.subscribe(platformMqTopic.getTOPIC(), platformMqTopic.getTAG(), aliyunMessageListener);
                consumer.start();
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public void initalConsumer(PlatformMqTopic platformMqTopic, List<DefaultMQPushConsumer> mqPushConsumerList) {
    }
}


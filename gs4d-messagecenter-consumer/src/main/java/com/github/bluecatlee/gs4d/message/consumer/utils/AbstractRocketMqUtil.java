package com.github.bluecatlee.gs4d.message.consumer.utils;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.MessageQueueSelector;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.client.producer.SendStatus;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.common.message.MessageQueue;
import com.alibaba.rocketmq.common.protocol.body.ClusterInfo;
import com.alibaba.rocketmq.remoting.exception.RemotingException;
import com.alibaba.rocketmq.tools.admin.DefaultMQAdminExt;
import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.Producer;
import com.aliyun.openservices.ons.api.order.OrderProducer;
import com.github.bluecatlee.gs4d.common.exception.ExceptionType;
import com.github.bluecatlee.gs4d.common.exception.ValidateBusinessException;
import com.github.bluecatlee.gs4d.common.utils.StringUtil;
import com.github.bluecatlee.gs4d.message.api.utils.IPUtil;
import com.github.bluecatlee.gs4d.message.api.utils.MessageSendUtil;
import com.github.bluecatlee.gs4d.message.consumer.dao.SysRocketMqConsumeLogDao;
import com.github.bluecatlee.gs4d.message.consumer.dao.SysRocketMqSendLogDao;
import com.github.bluecatlee.gs4d.message.consumer.model.SysRocketMqSendLog;
import com.github.bluecatlee.gs4d.message.consumer.service.MessageSqlListenerService;
import com.github.bluecatlee.gs4d.message.consumer.service.MqConsumerService;
import com.github.bluecatlee.gs4d.message.consumer.service.MqDubboConsumerService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.*;

public abstract class AbstractRocketMqUtil implements ApplicationListener<ContextRefreshedEvent> {

    private static Logger logger = LoggerFactory.getLogger(AbstractRocketMqUtil.class);
    protected static Logger messSaveFailedLogger = LoggerFactory.getLogger("messSaveFailedLog");

    @Resource(name = "mqConsumerService")
    private MqConsumerService mqConsumerService;

    @Resource(name = "mqAliyunConsumerService")
    private MqConsumerService mqAliyunConsumerService;

    public static ExecutorService executorService;

    public static Map<String, List<String>> namesrvToBrokerAddrs = new HashMap();

    @Autowired
    SysRocketMqSendLogDao sysRocketMqSendLogDao;
    
    @Autowired
    SysRocketMqConsumeLogDao sysRocketMqConsumeLogDao;
    
    @Autowired
    private MqDubboConsumerService mqDubboConsumerService;

    @Resource(name = "messageSqlListenerService")
    private MessageSqlListenerService messageSqlListenerService;

    @Resource(name = "messageSqlAliyunListenerService")
    private MessageSqlListenerService messageSqlAliyunListenerService;

    private static SysRocketMqSendLogDao sysRocketMqSendLogDaoE;

    @Value("#{settings['dataSign']}")
    private Long dataSign;
    @Value("#{settings['useAliyunMq']}")
    private String useAliyunMq;
    @Value("#{settings}")
    private Properties settings;
    @Value("#{settings['cipKey']}")
    private String cipKey;
    @Value("#{settings['nsKey']}")
    private String nsKey;

    static ThreadLocal<List<SysRocketMqSendLog>> sysRocketMqSendLogsThreadLocal = new ThreadLocal<List<SysRocketMqSendLog>>() {
        public List<SysRocketMqSendLog> u() {
            return new ArrayList();
        }
    };
    public static int shutdown_timeout = 1200;  // 线程池关闭时的超时时间，关闭线程池时，不会立即关闭，而是等待任务执行完才关闭，最多等待shutdown_timeout秒
    public static List<String> nameSrvList = new ArrayList();
    public static String mqNameServAddrMessOnly;
    public static String producerGroup;
    public static String producerGroupOnly;
    public static int topicQueueNums = 8;
    public static String defaultNamesrv;
    public static Map<String, List<DefaultMQProducer>> mqProducers = new HashMap(); // 注册到nameSrvList的rocketmq生产者列表 map的key为nameSrvList中的元素
    public static List<DefaultMQProducer> messOnlyMqProducers = new ArrayList();
    public static List<Producer> aliyunProducers = new ArrayList();
    public static List<OrderProducer> aliyunOrderProducers = new ArrayList();
    public static List<DefaultMQProducer> defaultMqProducers = new ArrayList();
    public static String aliyunMqAccessKey;
    public static String aliyunMqSecretKey;
    public static String aliyunMqUrl;
    private static Random random = new Random();
    private static String useAliyunMqFlag;
    public static DefaultMQProducer flushGroupProducer;
    private int queueCapacity = 10000;
    
    private Properties nsProperties = new Properties();         // nameserver的虚拟地址（即路由routeKey）->真实地址
    private Properties cipProperties = new Properties();        // consumer_ip的路由key -> 真实地址

    protected abstract void loadRocketMqConfig() throws Exception;

    protected abstract void loadAliyunMqConfig() throws Exception;

    /**
     * 加载入口
     * @param event
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null) {
            try {
                logger.info("初始化线程池");
                this.initThreadPool();
                
                this.loadNsProperties();
                logger.info("初始化mq集群结束");
                this.nsProperties.remove("mqNameServAddrMessOnly");
                this.nsProperties.remove("defaultNamesrv");
                
                this.loadCipProperties();
                logger.info("初始化消费IP结束");
                
                sysRocketMqSendLogDaoE = this.sysRocketMqSendLogDao;
                useAliyunMqFlag = this.useAliyunMq;
                int index;
                if (this.useAliyunMq.equals("N")) {
                    this.loadRocketMqConfig();

                    String instanceName;
                    for(index = 0; index < nameSrvList.size(); ++index) {
                        this.loadBrokerAddrs(nameSrvList.get(index));       // 依次查询每个名称服务器下的broker集群并保存到namesrvToBrokerAddrs

                        for(int i = 0; i < 10; ++i) {
                            instanceName = this.getInstanceName();
                            logger.info("初始化 生产者:{},消息服务器:{},实例:{}的RocketMq producer开始", new Object[]{producerGroup, nameSrvList, instanceName});
                            this.initRocketMqProducer(instanceName, nameSrvList.get(index));    // 依次初始化每个名称服务器的生产者mqProducer 并保存到mqProducers中
                        }
                    }

                    logger.info("初始化生产者结束");

                    for(index = 0; index < 10; ++index) {
                        this.loadBrokerAddrs(defaultNamesrv);       // 查询默认nameserver下的broker集群并保存到namesrvToBrokerAddrs
                        instanceName = this.getInstanceName();
                        logger.info("初始化 生产者:{},消息服务器:{},实例:{}的RocketMq producer开始", new Object[]{producerGroup, nameSrvList, instanceName});
                        this.initDefaultRocketMqProducer(instanceName, defaultNamesrv);     // 初始化使用默认名称服务器的生产者mqProducer 并保存到defaultMqProducers中
                        instanceName = this.getInstanceName();
                        this.initMessOnlyRocketMqProducer(instanceName);                    // 初始化使用mqNameServAddrMessOnly的生产者mqProducer 并保存到messOnlyMqProducers
                    }

                    if (this.dataSign == (long)SeqUtil.test) {
                        this.initFlushGroupProducer("flushGroup");  // 初始化flushGroup生产者组并保存到flushGroupProducer
                    }

                    this.messageSqlListenerService.initMqSqlConsumer();     // 创建messOnly下的topic(MESS_SQL) 并初始化对应的消费者(订阅MESS_SQL)
                    this.mqDubboConsumerService.initalAllDubboMethod();     // 【核心】从platform_mq_dubbo_consumer表 初始化dubbo消费者代理对象并保存到serviceNameDubboGroup2consumerServiceProxy
                    this.mqConsumerService.initalAllConsumer();             // 【核心】从platform_mq_topic表 依次初始化topic及其consumer(以及消息监听器) 并启动
                } else {
                    this.loadAliyunMqConfig();
                    this.loadCipProperties();

                    for(index = 0; index < 10; ++index) {
                        logger.info("初始化 生产者:{},消息服务器:{},实例:{}的aliyun producer开始", producerGroup, aliyunMqUrl);
                        this.initAliyunProducer();
                    }

                    this.messageSqlAliyunListenerService.initMqSqlConsumer();   // 创建messOnly下的topic(MESS_SQL) 并初始化对应的消费者(订阅MESS_SQL)
                    logger.info("初始化mqsql监听结束");
                    this.mqDubboConsumerService.initalAllDubboMethod();     // 初始化dubbo消费者代理对象并保存到serviceNameDubboGroup2consumerServiceProxy
                    logger.info("初始化dubbo监听结束");
                    this.mqAliyunConsumerService.initalAllConsumer();       // 从platform_mq_topic表 依次初始化topic及其consumer(以及消息监听器) 并启动
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                throw new ValidateBusinessException(Constants.SUB_SYSTEM, ExceptionType.VBE20031, "AbstractRocketMqUtil类初始化发生异常,异常信息为:" + e.getMessage());
            }
        }

    }

    private void initDefaultRocketMqProducer(String instanceName, String namesrvAddr) throws MQClientException {
        DefaultMQProducer mqProducer = new DefaultMQProducer(producerGroupOnly);
        mqProducer.setNamesrvAddr(namesrvAddr);
        mqProducer.setInstanceName(instanceName);
        mqProducer.setSendMsgTimeout(20000);
        mqProducer.setDefaultTopicQueueNums(topicQueueNums);
        mqProducer.start();
        logger.info("初始化 项目{},消息服务器:{},实例:{}的RocketMq producer结束", new Object[]{producerGroup, nameSrvList, namesrvAddr});
        defaultMqProducers.add(mqProducer);
    }

    private void initMessOnlyRocketMqProducer(String instanceName) throws MQClientException {
        DefaultMQProducer mqProducer = new DefaultMQProducer(producerGroupOnly);
        mqProducer.setInstanceName(instanceName);
        mqProducer.setNamesrvAddr(mqNameServAddrMessOnly);
        mqProducer.setSendMsgTimeout(20000);
        mqProducer.setDefaultTopicQueueNums(topicQueueNums);
        mqProducer.start();
        logger.info("初始化 项目{},消息服务器:{},实例:{}的RocketMq producer结束", new Object[]{producerGroup, nameSrvList, instanceName});
        messOnlyMqProducers.add(mqProducer);
    }

    private void initFlushGroupProducer(String producerGroup) throws MQClientException {
        DefaultMQProducer mqProducer = new DefaultMQProducer(producerGroup);
        mqProducer.setNamesrvAddr("mq.dev.gb246.com:9876");
        mqProducer.setInstanceName(UUID.randomUUID().toString());
        mqProducer.setSendMsgTimeout(20000);
        mqProducer.setDefaultTopicQueueNums(topicQueueNums);
        mqProducer.start();
        flushGroupProducer = mqProducer;
    }

    private void initAliyunProducer() {
        Properties properties = new Properties();
        properties.put("ProducerId", producerGroup);
        properties.put("AccessKey", aliyunMqAccessKey);
        properties.put("SecretKey", aliyunMqSecretKey);
        properties.setProperty("SendMsgTimeoutMillis", "5000");
        properties.put("ONSAddr", aliyunMqUrl);
        Producer producer = ONSFactory.createProducer(properties);
        producer.start();
        aliyunProducers.add(producer);
        OrderProducer orderProducer = ONSFactory.createOrderProducer(properties);
        orderProducer.start();
        aliyunOrderProducers.add(orderProducer);
    }

    private void initRocketMqProducer(String instanceName, String namesrvAddr) throws MQClientException {
        DefaultMQProducer mqProducer = new DefaultMQProducer(producerGroup);
        mqProducer.setNamesrvAddr(namesrvAddr);
        mqProducer.setInstanceName(instanceName);
        mqProducer.setSendMsgTimeout(20000);
        mqProducer.setDefaultTopicQueueNums(topicQueueNums);
        mqProducer.start();
        logger.info("初始化 项目{},消息服务器:{},实例:{}的RocketMq producer结束", new Object[]{producerGroup, nameSrvList, instanceName});
        List<DefaultMQProducer> list = mqProducers.get(namesrvAddr);
        if (list == null) {
            list = new ArrayList();
            list.add(mqProducer);
            mqProducers.put(namesrvAddr, list);
        } else {
            list.add(mqProducer);
        }
    }

    private void initThreadPool() {
        ArrayBlockingQueue queue = new ArrayBlockingQueue(this.queueCapacity);
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(150, 150, 0L, TimeUnit.MILLISECONDS, queue);
        threadPoolExecutor.setRejectedExecutionHandler(new RejectedExecutionHandler() {
            public void rejectedExecution(Runnable runnable, ThreadPoolExecutor threadPoolExecutor) {
                try {
                    AbstractRocketMqUtil.logger.info("当前线程池发现线程池阻塞");
                    threadPoolExecutor.getQueue().put(runnable);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });
        executorService = threadPoolExecutor;
    }

    private String getInstanceName() {
        if (nameSrvList != null && !nameSrvList.isEmpty()) {
            logger.debug("MQ_NAME_SERV_ADDR:{}", nameSrvList);
            if (StringUtils.isEmpty(mqNameServAddrMessOnly)) {
                throw new ValidateBusinessException(Constants.SUB_SYSTEM, ExceptionType.VBE20031, "没指定消息中心本身的命名服务器地址");
            } else {
                logger.debug("MQ_NAME_SERV_ADDR_MESSONLY:{}", mqNameServAddrMessOnly);
                if (StringUtils.isEmpty(producerGroup)) {
                    throw new ValidateBusinessException(Constants.SUB_SYSTEM, ExceptionType.VBE20031, "没指定生产者名称");
                } else {
                    logger.debug("PRODUCER_NAME:{}", producerGroup);
                    String instanceName = IPUtil.getServerIpAddr() + UUID.randomUUID();
                    return instanceName;
                }
            }
        } else {
            throw new ValidateBusinessException(Constants.SUB_SYSTEM, ExceptionType.VBE20031, "没指定命名服务器地址");
        }
    }

    @PreDestroy
    public void preDestroy() {
        preDestroy(executorService, shutdown_timeout, TimeUnit.SECONDS);
    }

    private static void preDestroy(ExecutorService executorService, int timeout, TimeUnit timeUnit) {
        try {
            executorService.shutdownNow();
            if (!executorService.awaitTermination((long)timeout, timeUnit)) {
                logger.error("thread poool did not terminated");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // 捕获到中断异常 继续保持中断
        }

    }

    public static void send(SysRocketMqSendLog sysRocketMqSendLog, JdbcTemplate jdbcTemplate, StringRedisTemplate stringRedisTemplate) {
        send(sysRocketMqSendLog, (MessageQueueSelector)null, (Object)null, jdbcTemplate, stringRedisTemplate, false);
    }

    public static void a(List<SysRocketMqSendLog> var0, JdbcTemplate var1, StringRedisTemplate var2, boolean var3) {
        Iterator var4 = var0.iterator();

        while(var4.hasNext()) {
            final SysRocketMqSendLog var5 = (SysRocketMqSendLog)var4.next();
            if (Constants.orderTopicTagList.contains(var5.getMESSAGE_TOPIC() + "#" + var5.getMESSAGE_TAG() + "#" + var5.getTENANT_NUM_ID())) {
                var5.setORDER_MESS_FLAG(Constants.ez);
                MessageQueueSelector var6 = new MessageQueueSelector() {
                    public MessageQueue select(List<MessageQueue> var1, Message var2, Object var3) {
                        Integer var4 = var5.getMESSAGE_KEY().hashCode();
                        int var5x = Math.abs(var4) % var1.size();
                        return (MessageQueue)var1.get(var5x);
                    }
                };
                send(var5, var6, var5.getMESSAGE_KEY(), var1, var2, var3);
            } else {
                var5.setORDER_MESS_FLAG(Constants.eO);
                send(var5, (MessageQueueSelector)null, (Object)null, var1, var2, var3);
            }
        }

    }

    public static void a(SysRocketMqSendLog sysRocketMqSendLog, MessageQueueSelector selector, Object arg, JdbcTemplate jdbcTemplate, StringRedisTemplate stringRedisTemplate, boolean var5) {
        if (sysRocketMqSendLog != null) {
            sysRocketMqSendLog.setORDER_MESS_FLAG(Constants.ez);
        }

        send(sysRocketMqSendLog, selector, arg, jdbcTemplate, stringRedisTemplate, var5);
    }

    private static void send(final SysRocketMqSendLog sysRocketMqSendLog, final MessageQueueSelector selector, final Object arg, JdbcTemplate jdbcTemplate, final StringRedisTemplate stringRedisTemplate, boolean var5) {
        
        executorService.submit(new Runnable() {

            public void run() {
                if (AbstractRocketMqUtil.useAliyunMqFlag.equals("N")) {
                    this.sendToRocketMq();
                } else {
                    this.sendToAliyunMq();
                }

            }
            
            private void sendToRocketMq() {
                boolean messUpdateFlag = false;
                String body = JSONObject.toJSONString(sysRocketMqSendLog);

                try {
                    Message message = new Message(sysRocketMqSendLog.getMESSAGE_TOPIC(), sysRocketMqSendLog.getMESSAGE_TAG(), body.getBytes());
                    message.setKeys(sysRocketMqSendLog.getMESSAGE_KEY());
                    if (Constants.topicTagToConsumerInterval.get(sysRocketMqSendLog.getMESSAGE_TOPIC() + "#" + sysRocketMqSendLog.getMESSAGE_TAG()) != null) {
                        message.setDelayTimeLevel((Integer) Constants.topicTagToConsumerInterval.get(sysRocketMqSendLog.getMESSAGE_TOPIC() + "#" + sysRocketMqSendLog.getMESSAGE_TAG()));
                    }

                    String topicTag = sysRocketMqSendLog.getMESSAGE_TOPIC() + "#" + sysRocketMqSendLog.getMESSAGE_TAG() + "#" + sysRocketMqSendLog.getTENANT_NUM_ID();
                    String namesrv = (String) Constants.topicTagToNamesrv.get(topicTag);
                    List producerList = null;
                    if (StringUtil.isAllNullOrBlank(new String[]{namesrv})) {
                        AbstractRocketMqUtil.logger.error("发现未配置namesrv的tag" + sysRocketMqSendLog.getMESSAGE_TAG());
                        producerList = AbstractRocketMqUtil.defaultMqProducers;
                    } else {
                        producerList = (List) AbstractRocketMqUtil.mqProducers.get(namesrv);
                    }

                    if (producerList == null || producerList.isEmpty()) {
                        AbstractRocketMqUtil.messSaveFailedLogger.error("发现和消息中心不匹配的nameSrv:" + namesrv + ",tag:" + sysRocketMqSendLog.getMESSAGE_TAG());
                        return;
                    }

                    SendResult sendResult;
                    if (selector != null) {
                        sendResult = ((DefaultMQProducer)producerList.get(AbstractRocketMqUtil.random.nextInt(producerList.size()))).send(message, selector, arg);
                    } else {
                        sendResult = ((DefaultMQProducer)producerList.get(AbstractRocketMqUtil.random.nextInt(producerList.size()))).send(message);
                    }

                    AbstractRocketMqUtil.logger.debug("消息发送结果:messageSeries:{},messageId:{},sendResult:{}", new Object[]{sysRocketMqSendLog.getSERIES(), sendResult.getMsgId(), sendResult.getSendStatus()});
                    if (sendResult.getSendStatus() == SendStatus.SEND_OK) {
                        sysRocketMqSendLog.setMESSAGE_ID(sendResult.getMsgId());
                        sysRocketMqSendLog.setMSG_STATUS(1);    // todo 常量
                        messUpdateFlag = true;
                    } else {
                        AbstractRocketMqUtil.logger.error("消息发送失败,topic:{},tag:{},messageKey:{},body:{}", new Object[]{sysRocketMqSendLog.getMESSAGE_TOPIC(), sysRocketMqSendLog.getMESSAGE_TAG(), sysRocketMqSendLog.getMESSAGE_KEY(), body});
                        sysRocketMqSendLog.setMSG_STATUS(3);
                        sysRocketMqSendLog.setFAIL_DETAIL("发送失败");
                    }
                } catch (Exception e) {
                    AbstractRocketMqUtil.logger.error("消息发送发生异常,topic:{},tag:{},messageKey:{},body:{},原因:{}", new Object[]{sysRocketMqSendLog.getMESSAGE_TOPIC(), sysRocketMqSendLog.getMESSAGE_TAG(), sysRocketMqSendLog.getMESSAGE_KEY(), body, e.getMessage()});
                    AbstractRocketMqUtil.logger.error(e.getMessage(), e);
                    String failDetail = "发送发生异常," + e.getMessage();
                    if (failDetail.length() > 1000) {
                        failDetail = failDetail.substring(0, 1000);
                    }

                    sysRocketMqSendLog.setFAIL_DETAIL(failDetail);
                    sysRocketMqSendLog.setMSG_STATUS(3);
                }

                String newBody = JSONObject.toJSONString(sysRocketMqSendLog);
                AbstractRocketMqUtil.deleteSendLogCache(sysRocketMqSendLog, stringRedisTemplate);
                if (messUpdateFlag) {
                    AbstractRocketMqUtil.a(Constants.MESS_SQL, Constants.MESS_UPDATE_SEND_SQL, sysRocketMqSendLog.getSERIES().toString(), newBody);
                } else {
                    AbstractRocketMqUtil.sysRocketMqSendLogDaoE.updateSendFail(sysRocketMqSendLog);
                }

            }

            private void sendToAliyunMq() {
                String body = JSONObject.toJSONString(sysRocketMqSendLog);
                boolean messUpdateFlag = false;

                try {
                    com.aliyun.openservices.ons.api.Message message = new com.aliyun.openservices.ons.api.Message(sysRocketMqSendLog.getMESSAGE_TOPIC(), sysRocketMqSendLog.getMESSAGE_TAG(), body.getBytes());
                    message.setKey(sysRocketMqSendLog.getMESSAGE_KEY());
                    if (Constants.topicTagToConsumerInterval.get(sysRocketMqSendLog.getMESSAGE_TOPIC() + "#" + sysRocketMqSendLog.getMESSAGE_TAG()) != null) {
                        Long startDeliverTime = System.currentTimeMillis() + (Long) Constants.fx.get((Integer) Constants.topicTagToConsumerInterval.get(sysRocketMqSendLog.getMESSAGE_TOPIC() + "#" + sysRocketMqSendLog.getMESSAGE_TAG()));
                        message.setStartDeliverTime(startDeliverTime);
                    }

                    com.aliyun.openservices.ons.api.SendResult sendResult;
                    if (selector != null) {
                        sendResult = ((OrderProducer) AbstractRocketMqUtil.aliyunOrderProducers.get(AbstractRocketMqUtil.random.nextInt(AbstractRocketMqUtil.aliyunOrderProducers.size()))).send(message, sysRocketMqSendLog.getMESSAGE_KEY());
                    } else {
                        sendResult = ((Producer) AbstractRocketMqUtil.aliyunProducers.get(AbstractRocketMqUtil.random.nextInt(AbstractRocketMqUtil.aliyunProducers.size()))).send(message);
                    }

                    AbstractRocketMqUtil.logger.debug("消息发送结果:messageSeries:" + sysRocketMqSendLog.getSERIES() + ",messageId:" + sendResult.getMessageId());
                    sysRocketMqSendLog.setMESSAGE_ID(sendResult.getMessageId());
                    sysRocketMqSendLog.setMSG_STATUS(1);
                    messUpdateFlag = true;
                } catch (Exception e) {
                    AbstractRocketMqUtil.logger.error("消息发送发生异常,topic:{},tag:{},messageKey:{},body:{},原因:{}", new Object[]{sysRocketMqSendLog.getMESSAGE_TOPIC(), sysRocketMqSendLog.getMESSAGE_TAG(), sysRocketMqSendLog.getMESSAGE_KEY(), body, e.getMessage()});
                    AbstractRocketMqUtil.logger.error(e.getMessage(), e);
                    String failDetail = "发送发生异常," + e.getMessage();
                    if (failDetail.length() > 1000) {
                        failDetail = failDetail.substring(0, 1000);
                    }

                    sysRocketMqSendLog.setFAIL_DETAIL(failDetail);
                    sysRocketMqSendLog.setMSG_STATUS(3);
                }

                String newBody = JSONObject.toJSONString(sysRocketMqSendLog);
                AbstractRocketMqUtil.deleteSendLogCache(sysRocketMqSendLog, stringRedisTemplate);
                if (messUpdateFlag) {
                    AbstractRocketMqUtil.a(Constants.MESS_SQL, Constants.MESS_UPDATE_SEND_SQL, sysRocketMqSendLog.getSERIES().toString(), newBody);
                } else {
                    AbstractRocketMqUtil.sysRocketMqSendLogDaoE.updateSendFail(sysRocketMqSendLog);
                }

            }

        });
    }

    private static void deleteSendLogCache(SysRocketMqSendLog sysRocketMqSendLog, StringRedisTemplate stringRedisTemplate) {
        stringRedisTemplate.delete(MessageSendUtil.generateMessageKeyToRedis(sysRocketMqSendLog.getSERIES()));
    }

    public static void send(final String topic, final String tags, final String keys, final SysRocketMqSendLog sysRocketMqSendLog, final Long tenantNumId) {

        executorService.submit(new Runnable() {

            public void run() {
                if (AbstractRocketMqUtil.useAliyunMqFlag.equals("N")) {
                    this.sendToRocketMq(topic, tags, keys, sysRocketMqSendLog);
                } else {
                    this.sendToAliyunMq(topic, tags, keys, sysRocketMqSendLog);
                }

            }

            private void sendToRocketMq(String topic, String tags, String keys, SysRocketMqSendLog sysRocketMqSendLog) {
                String body = JSONObject.toJSONString(sysRocketMqSendLog);
                Message message = new Message(topic, tags, body.getBytes());
                message.setKeys(keys);
                if (Constants.topicTagToConsumerInterval.get(sysRocketMqSendLog.getMESSAGE_TOPIC() + "#" + sysRocketMqSendLog.getMESSAGE_TAG()) != null) {
                    message.setDelayTimeLevel((Integer) Constants.topicTagToConsumerInterval.get(sysRocketMqSendLog.getMESSAGE_TOPIC() + "#" + sysRocketMqSendLog.getMESSAGE_TAG()));
                }

                String topicTag = topic + "#" + tags + "#" + tenantNumId;
                String namesrv = (String) Constants.topicTagToNamesrv.get(topicTag);
                List producerList = null;
                if (StringUtil.isAllNullOrBlank(new String[]{namesrv})) {
                    AbstractRocketMqUtil.logger.error("发现未配置namesrv的tag" + sysRocketMqSendLog.getMESSAGE_TAG());
                    producerList = AbstractRocketMqUtil.defaultMqProducers;
                } else {
                    producerList = (List) AbstractRocketMqUtil.mqProducers.get(namesrv);
                }

                if (producerList != null && !producerList.isEmpty()) {
                    try {
                        SendResult sendResult = ((DefaultMQProducer)producerList.get(AbstractRocketMqUtil.random.nextInt(producerList.size()))).send(message);
                    } catch (Exception e) {
                        AbstractRocketMqUtil.logger.error("监控发送消息异常：" + e.getMessage(), e);
                    }

                } else {
                    AbstractRocketMqUtil.messSaveFailedLogger.error("发现和消息中心不匹配的nameSrv:" + namesrv + ",tag:" + tags);
                }
            }

            private void sendToAliyunMq(String topic, String tags, String keys, SysRocketMqSendLog sysRocketMqSendLog) {
                String body = JSONObject.toJSONString(sysRocketMqSendLog);

                try {
                    com.aliyun.openservices.ons.api.Message message = new com.aliyun.openservices.ons.api.Message(topic, tags, body.getBytes());
                    message.setKey(keys);
                    if (Constants.topicTagToConsumerInterval.get(sysRocketMqSendLog.getMESSAGE_TOPIC() + "#" + sysRocketMqSendLog.getMESSAGE_TAG()) != null) {
                        message.setStartDeliverTime(System.currentTimeMillis() + (Long) Constants.fx.get((Integer) Constants.topicTagToConsumerInterval.get(sysRocketMqSendLog.getMESSAGE_TOPIC() + "#" + sysRocketMqSendLog.getMESSAGE_TAG())));
                    }

                    if (Constants.orderTopicTagList.contains(sysRocketMqSendLog.getMESSAGE_TOPIC() + "#" + sysRocketMqSendLog.getMESSAGE_TAG() + "#" + sysRocketMqSendLog.getTENANT_NUM_ID())) {
                        ((OrderProducer) AbstractRocketMqUtil.aliyunOrderProducers.get(AbstractRocketMqUtil.random.nextInt(AbstractRocketMqUtil.aliyunOrderProducers.size()))).send(message, sysRocketMqSendLog.getMESSAGE_KEY());
                    } else {
                        ((Producer) AbstractRocketMqUtil.aliyunProducers.get(AbstractRocketMqUtil.random.nextInt(AbstractRocketMqUtil.aliyunProducers.size()))).send(message);
                    }
                } catch (Exception e) {
                    AbstractRocketMqUtil.logger.error(e.getMessage(), e);
                }

            }
            
        });
    }

    public static void a(String topic, String tags, String keys, String body) {
        if (useAliyunMqFlag.equals("N")) {
            send(topic, tags, keys, body);
        } else {
            sendAliyun(topic, tags, keys, body);
        }

    }

    private static void sendAliyun(String topic, String tags, String keys, String body) {
        try {
            com.aliyun.openservices.ons.api.Message message = new com.aliyun.openservices.ons.api.Message(topic, tags, body.getBytes());
            message.setKey(keys);
            ((Producer)aliyunProducers.get(random.nextInt(aliyunProducers.size()))).send(message);
        } catch (Exception e) {
            logger.error("写入数据库发消息失败，原因" + e.getMessage(), e);
        }

    }

    public static void send(String topic, String tags, final String keys, String body) {
        Message message = new Message(topic, tags, body.getBytes());
        message.setKeys(keys);
        MessageQueueSelector selector = new MessageQueueSelector() {
            public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                Integer keyHash = keys.hashCode();
                int mod = Math.abs(keyHash) % mqs.size();
                return (MessageQueue)mqs.get(mod);
            }
        };
        SendResult sendResult = null;

        try {
            if (tags.equals(Constants.ORDER_FLOWER_TAG)) {
                message.setDelayTimeLevel(2);
            }

            sendResult = ((DefaultMQProducer)messOnlyMqProducers.get(random.nextInt(messOnlyMqProducers.size()))).send(message, selector, keys);
            if (sendResult.getSendStatus() != SendStatus.SEND_OK) {
                messSaveFailedLogger.error("发送消息失败，TOPIC:" + message.getTopic() + " TAG:" + message.getTags() + " message_key:" + keys);
                throw new ValidateBusinessException(Constants.SUB_SYSTEM, ExceptionType.VBE20031, "发送消息失败，TOPIC:" + message.getTopic() + " TAG:" + message.getTags() + " message_key:" + keys);
            }
        } catch (RemotingException | MQBrokerException | InterruptedException | MQClientException e) {
            messSaveFailedLogger.error("发送消息异常，TOPIC:" + message.getTopic() + " TAG:" + message.getTags() + " message_key:" + keys, e.getMessage());
            throw new ValidateBusinessException(Constants.SUB_SYSTEM, ExceptionType.VBE20031, "发送消息异常，TOPIC:" + message.getTopic() + " TAG:" + message.getTags() + " message_key:" + keys);
        } catch (Throwable e) {
            messSaveFailedLogger.error("发送消息出现错误，TOPIC:" + message.getTopic() + " TAG:" + message.getTags() + " message_key:" + keys, e.getMessage());
            throw new ValidateBusinessException(Constants.SUB_SYSTEM, ExceptionType.VBE20031, "发送消息出现错误，TOPIC:" + message.getTopic() + " TAG:" + message.getTags() + " message_key:" + keys);
        }
    }

    public static void d(String topic, String tags, final String keys, String body) {
        Message message = new Message(topic, tags, body.getBytes());
        message.setKeys(keys);
        MessageQueueSelector selector = new MessageQueueSelector() {
            public MessageQueue select(List<MessageQueue> mqs, Message msg, Object body) {
                Integer keyHash = keys.hashCode();
                int mod = Math.abs(keyHash) % mqs.size();
                return (MessageQueue)mqs.get(mod);
            }
        };
        SendResult sendResult = null;

        try {
            sendResult = flushGroupProducer.send(message, selector, keys);
            if (sendResult.getSendStatus() != SendStatus.SEND_OK) {
                messSaveFailedLogger.error("发送消息异常message_key" + keys);
            }
        } catch (RemotingException | MQBrokerException | InterruptedException | MQClientException e) {
            messSaveFailedLogger.error("消息发送发生异常key" + keys, e.getMessage());
        }

    }

    private void loadBrokerAddrs(String namesrvAddr) {
        DefaultMQAdminExt mqAdminExt = new DefaultMQAdminExt();
        if (namesrvToBrokerAddrs.get(namesrvAddr) == null) {
            try {
                mqAdminExt.setNamesrvAddr(namesrvAddr);
                mqAdminExt.setInstanceName(Long.toString(System.currentTimeMillis()));
                mqAdminExt.start();
                ArrayList list = new ArrayList();
                ClusterInfo clusterInfo = mqAdminExt.examineBrokerClusterInfo();
                Iterator iterator = clusterInfo.getClusterAddrTable().entrySet().iterator();

                while(iterator.hasNext()) {
                    Entry entry = (Entry)iterator.next();
                    String entryKey = (String)entry.getKey();
                    list.add(entryKey);
                }

                namesrvToBrokerAddrs.put(namesrvAddr, list);
            } catch (Exception e) {
                throw new ValidateBusinessException(Constants.SUB_SYSTEM, ExceptionType.VBE20031, "消息中心获取客户端信息时出现错误。" + e.getMessage());
            } finally {
                mqAdminExt.shutdown();
            }
        }
    }

    private void loadNsProperties() {
        if (StringUtil.isAllNotNullOrBlank(new String[]{this.nsKey})) {
            String[] keys = this.nsKey.split(",");

            for(int i = 0; i < keys.length; ++i) {
                String routeKey = keys[i];
                String value = this.settings.getProperty(routeKey);
                if (StringUtil.isAllNotNullOrBlank(new String[]{value})) {
                    this.nsProperties.put(routeKey, value);
                }
            }
        }

    }

    public String getNsRealAddress(String routeKey) {
        if (this.nsProperties.isEmpty()) {
            this.loadNsProperties();
        }

        String nsRealAddress = this.nsProperties.getProperty(routeKey);
        return nsRealAddress;
    }

    public Set<Object> getPropertyKey() {
        if (this.nsProperties.isEmpty()) {
            this.loadNsProperties();
        }

        Set set = this.nsProperties.keySet();
        return set;
    }

    public Collection getNameSrvs() {
        if (this.nsProperties.isEmpty()) {
            this.loadNsProperties();
        }

        Collection collection = this.nsProperties.values();
        return collection;
    }

    public Boolean nsKeyExists(String routeKey) {
        if (this.nsProperties.isEmpty()) {
            this.loadNsProperties();
        }

        Boolean exists = this.nsProperties.containsKey(routeKey);
        return exists;
    }

    public Collection getconsumerIps() {
        if (this.cipProperties.isEmpty()) {
            this.loadCipProperties();
        }

        Collection collection = this.cipProperties.values();
        return collection;
    }

    public Set<Object> getConsumerIpsKey() {
        if (this.cipProperties.isEmpty()) {
            this.loadCipProperties();
        }

        Set cipKeys = this.cipProperties.keySet();
        return cipKeys;
    }

    public String getConsumerIp(String cipRouter) {
        if (this.cipProperties.isEmpty()) {
            this.loadCipProperties();
        }

        String consumerIp = this.cipProperties.getProperty(cipRouter);
        return consumerIp;
    }

    private void loadCipProperties() {
        if (StringUtil.isAllNotNullOrBlank(new String[]{this.cipKey})) {
            String[] cipKeys = this.cipKey.split(",");

            for(int i = 0; i < cipKeys.length; ++i) {
                String key = cipKeys[i];
                String value = this.settings.getProperty(key);
                if (StringUtil.isAllNotNullOrBlank(new String[]{value})) {
                    this.cipProperties.put(key, value);
                }
            }
        }

    }
}


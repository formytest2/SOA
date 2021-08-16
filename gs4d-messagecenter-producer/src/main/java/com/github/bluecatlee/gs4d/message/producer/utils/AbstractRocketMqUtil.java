package com.github.bluecatlee.gs4d.message.producer.utils;

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
import com.github.bluecatlee.gs4d.message.producer.dao.SysRocketMqJobLogDao;
import com.github.bluecatlee.gs4d.message.producer.dao.SysRocketMqSendLogDao;
import com.github.bluecatlee.gs4d.message.producer.dao.SysTransationFailedLogDao;
import com.github.bluecatlee.gs4d.message.producer.model.SYS_ROCKET_MQ_SEND_LOG;
import com.github.bluecatlee.gs4d.message.producer.service.JobmessageService;
import com.github.bluecatlee.gs4d.message.producer.service.MessageSqlListenerService;
import com.github.bluecatlee.gs4d.message.producer.service.MqProductorBizInitService;
import com.github.bluecatlee.gs4d.message.producer.service.RedisInitLitenerService;
import com.github.bluecatlee.gs4d.message.producer.service.impl.MessageSendListServiceImpl;
import com.github.bluecatlee.gs4d.message.producer.service.impl.TransationFailedLogReHandleTask;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.*;

public abstract class AbstractRocketMqUtil implements ApplicationListener<ContextRefreshedEvent> {

    private static Logger logger = LoggerFactory.getLogger(AbstractRocketMqUtil.class);
    protected static Logger messSaveFailedLogger = LoggerFactory.getLogger("messSaveFailedLog");

    @Autowired
    SysRocketMqSendLogDao sysRocketMqSendLogDao;

    @Autowired
    public StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisInitLitenerService redisInitLitenerService;

    @Autowired
    private SysTransationFailedLogDao sysTransationFailedLogDao;

    @Autowired
    private MessageSendListServiceImpl messageSendListService;

    @Autowired
    private MqProductorBizInitService mqProductorBizInitService;

    @Resource(name = "messageSqlListenerService")
    private MessageSqlListenerService messageSqlListenerService;

    @Resource(name = "messageSqlAliyunListenerService")
    private MessageSqlListenerService messageSqlAliyunListenerService;

    @Autowired
    private SysRocketMqJobLogDao sysRocketMqJobLogDao;

    @Autowired
    private JobmessageService jobmessageService;

    @Value("#{settings['dataSign']}")
    private Long dataSign;
    @Value("#{settings['useAliyunMq']}")
    private String useAliyunMq;
    @Value("#{settings['zookeeper.host.port']}")
    private String zookeeperAddress;
    @Value("#{settings}")
    private Properties settings;
    @Value("#{settings['cipKey']}")
    private String cipKey;
    @Value("#{settings['nsKey']}")
    private String nsKey;

    @Autowired
    private JdbcUtils jdbcUtils;

    public static String zkAddress;
    public static SysRocketMqSendLogDao sysRocketMqSendLogDaoE;
    public static SysRocketMqJobLogDao sysRocketMqJobLogDaoE;
    public static JobmessageService jobmessageServiceE;
    private static String useAliyunMqFlag;

    static ThreadLocal<List<SYS_ROCKET_MQ_SEND_LOG>> cC = new ThreadLocal<List<SYS_ROCKET_MQ_SEND_LOG>>() {
        public List<SYS_ROCKET_MQ_SEND_LOG> s() {
            return new ArrayList();
        }
    };

    public static ExecutorService executorService;
    public static int shutdown_timeout = 1200;   // 线程池关闭时的超时时间，关闭线程池时，不会立即关闭，而是等待任务执行完才关闭，最多等待shutdown_timeout秒

    private Properties nsProperties = new Properties();     // nameserver的虚拟地址（即路由routeKey）->真实地址
    private Properties cipProperties = new Properties();    // consumer_ip的路由key -> 真实地址

    public static String producerGroup;
    public static String producerGroupOnly;
    public static int topicQueueNums = 8;
    private int queueCapacity = 10000;

    public static Map<String, List<String>> namesrvToBrokerAddrs = new HashMap();     // nameserver真实地址 -> broker集群地址集合

    public static List<String> nameSrvList = new ArrayList();                           // nameserver列表
    public static Map<String, List<DefaultMQProducer>> mqProducers = new HashMap();     // 注册到nameSrvList的rocketmq生产者列表 map的key为nameSrvList中的元素

    public static String defaultNamesrv;                                                // 默认nameserver
    public static List<DefaultMQProducer> defaultMqProducers = new ArrayList();         // 注册到默认名称服务器的rocketmq生产者列表

    public static String mqNameServAddrMessOnly;                                        // messagecenter本身的名称服务器地址   messOnly
    public static List<DefaultMQProducer> messOnlyMqProducers = new ArrayList();        // 注册到messOnly名称服务器的rocketmq生产者列表

    public static String aliyunMqUrl;
    public static String aliyunMqAccessKey;
    public static String aliyunMqSecretKey;

    public static List<Producer> aliyunProducers = new ArrayList();                     // 阿里云生产者列表
    public static List<OrderProducer> aliyunOrderProducers = new ArrayList();           // 阿里云顺序生产者列表

    private static Random random = new Random();

    protected abstract void loadRocketMqConfig() throws Exception;

    protected abstract void loadAliyunMqConfig() throws Exception;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {           // Spring上下文刷新事件 会在所有的bean都初始化(InitializingBean)之后触发
        if (event.getApplicationContext().getParent() == null) {
            try {
                this.loadNsProperties();                                 // 加载配置到nsProperties
                this.nsProperties.remove("mqNameServAddrMessOnly");
                this.nsProperties.remove("defaultNamesrv");
                this.initThreadPool();                                     // 初始化线程池

                sysRocketMqSendLogDaoE = this.sysRocketMqSendLogDao;        // 静态字段赋值  这几个静态字段会暴露到类外部
                sysRocketMqJobLogDaoE = this.sysRocketMqJobLogDao;
                jobmessageServiceE = this.jobmessageService;
                zkAddress = this.zookeeperAddress;
                useAliyunMqFlag = this.useAliyunMq;

                if (this.useAliyunMq.equals("N")) {     // 不使用阿里云队列 aliyun openservices, 使用本地rocketmq
                    this.loadRocketMqConfig();          // 加载rocketmq配置

                    int index = 0;
                    while(true) {
                        String instanceName;
                        if (index >= nameSrvList.size()) {
                            for(index = 0; index < 10; ++index) {
                                this.loadBrokerAddrs(defaultNamesrv);                       // 缓存默认名称服务器下的brokers地址
                                instanceName = this.getInstanceName();
                                logger.info("初始化 生产者:{},消息服务器:{},实例:{}的RocketMq producer开始", new Object[]{producerGroup, nameSrvList, instanceName});
                                this.initDefaultRocketMqProducer(instanceName, defaultNamesrv);    // 初始化生产者producer并注册到默认的名称服务器defaultNamesrv
                                instanceName = this.getInstanceName();
                                this.initMessOnlyRocketMqProducer(instanceName);                    // 初始化生产者producer并注册到名称服务器mqNameServAddrMessOnly
                            }

                            this.messageSqlListenerService.initMqSqlConsumer();         // 初始化消费者
                            this.mqProductorBizInitService.initalAllProductorBiz();     // 初始化业务数据 topic相关的
                            this.redisInitLitenerService.initalRedisListener();         // 初始化redis监听器 (使用了redis的发布订阅功能)
                            break;
                        }

                        this.loadBrokerAddrs(nameSrvList.get(index));        // 循环nameSrvList 根据nameserver加载brokers的地址

                        for(int i = 0; i < 10; ++i) {
                            instanceName = this.getInstanceName();
                            logger.info("初始化 生产者:{},消息服务器:{},实例:{}的RocketMq producer开始", producerGroup, nameSrvList, instanceName);
                            this.initRocketMqProducer(instanceName, nameSrvList.get(index));   // 初始化生产者producer并注册到nameSrvList下的名称服务器
                        }

                        ++index;
                    }
                } else {                // 使用阿里云队列 aliyun openservices
                    this.loadAliyunMqConfig();  // 加载aliyun openservices配置

                    for(int i = 0; i < 10; ++i) {
                        logger.info("初始化 生产者:{},消息服务器:{},实例:{}的aliyun producer开始", producerGroup, aliyunMqUrl, "");
                        this.initAliyunProducer();      // 初始化阿里云生产者和顺序生产者
                    }

                    this.messageSqlAliyunListenerService.initMqSqlConsumer();
                    this.mqProductorBizInitService.initalAllProductorBiz();
                    this.redisInitLitenerService.initalRedisListener();
                }

                Timer timer = new Timer(true);
                timer.schedule(new TransationFailedLogReHandleTask(this.sysTransationFailedLogDao, this.messageSendListService, this.zookeeperAddress, this.sysRocketMqSendLogDao, this.stringRedisTemplate), 1000L, 20000L);
                this.jobmessageService.initJobMessageToSpringJob(); // 初始化定时任务 即根据SYS_ROCKET_MQ_JOB_LOG表的配置一次创建其触发器、任务、调度器
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                throw new ValidateBusinessException(Constants.SUB_SYSTEM, ExceptionType.VBE20031, "AbstractRocketMqUtil类初始化发生异常,异常信息为:" + e.getMessage());
            }
        }

    }

    private void initDefaultRocketMqProducer(String instanceName, String namesrvAddr) throws MQClientException {
        DefaultMQProducer mqProducer = new DefaultMQProducer(producerGroupOnly);
        mqProducer.setInstanceName(instanceName);
        mqProducer.setNamesrvAddr(namesrvAddr);
        mqProducer.setSendMsgTimeout(20000);
        mqProducer.setDefaultTopicQueueNums(topicQueueNums);
        mqProducer.start();
        logger.info("初始化 项目{},消息服务器:{},实例:{}的RocketMq producer结束", new Object[]{producerGroup, nameSrvList, instanceName});
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

    private void initRocketMqProducer(String instanceName, String namesrvAddr) throws MQClientException {
        DefaultMQProducer mqProducer = new DefaultMQProducer(producerGroup);
        mqProducer.setInstanceName(instanceName);
        mqProducer.setNamesrvAddr(namesrvAddr);
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

    // 创建阿里云消息服务生产者和顺序生产者并启动
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

    private void initThreadPool() {
        ArrayBlockingQueue queue = new ArrayBlockingQueue(this.queueCapacity);
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(150, 150, 0L, TimeUnit.MILLISECONDS, queue);
        threadPoolExecutor.setRejectedExecutionHandler(new RejectedExecutionHandler() {
            public void rejectedExecution(Runnable runnable, ThreadPoolExecutor threadPoolExecutor) {
                try {
                    logger.info("当前线程池发现线程池阻塞");
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

    public static void send(SYS_ROCKET_MQ_SEND_LOG sysRocketMqSendLog, StringRedisTemplate stringRedisTemplate) {
        send(sysRocketMqSendLog, (MessageQueueSelector)null, (Object)null, stringRedisTemplate);
    }

    public static void send(List<SYS_ROCKET_MQ_SEND_LOG> sysRocketMqSendLogs, StringRedisTemplate stringRedisTemplate) {
        Iterator iterator = sysRocketMqSendLogs.iterator();

        while(iterator.hasNext()) {
            final SYS_ROCKET_MQ_SEND_LOG sysRocketMqSendLog = (SYS_ROCKET_MQ_SEND_LOG)iterator.next();
            if (Constants.orderTopicTagList.contains(sysRocketMqSendLog.getMESSAGE_TOPIC() + "#" + sysRocketMqSendLog.getMESSAGE_TAG())) {
                sysRocketMqSendLog.setORDER_MESS_FLAG(Constants.mess_flag_order);
                MessageQueueSelector selector = new MessageQueueSelector() {
                    public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                        Integer messagekeyHash = sysRocketMqSendLog.getMESSAGE_KEY().hashCode();
                        int mod = Math.abs(messagekeyHash) % mqs.size();
                        logger.info("发现顺序消息tag:" + sysRocketMqSendLog.getMESSAGE_TAG() + ",key:" + sysRocketMqSendLog.getMESSAGE_KEY() + ",本次所属队列" + mod + ",总队列mq" + mqs.size());
                        return (MessageQueue)mqs.get(mod);
                    }
                };
                send(sysRocketMqSendLog, selector, sysRocketMqSendLog.getMESSAGE_KEY(), stringRedisTemplate);
            } else {
                if (sysRocketMqSendLog.getORDER_MESS_FLAG() == null) {
                    sysRocketMqSendLog.setORDER_MESS_FLAG(Constants.mess_flag_common);
                }

                send(sysRocketMqSendLog, (MessageQueueSelector)null, (Object)null, stringRedisTemplate);
            }
        }

    }

    private static void send(final SYS_ROCKET_MQ_SEND_LOG sysRocketMqSendLog, final MessageQueueSelector selector, final Object arg, final StringRedisTemplate stringRedisTemplate) {
        executorService.submit(new Runnable() {

            public void run() {
                if (AbstractRocketMqUtil.useAliyunMqFlag.equals("N")) {
                    this.sendToRocketMq();       // 使用本地rocketmq
                } else {
                    this.sendToAliyunMq();       // 使用阿里云mq
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

                    String topicTag = sysRocketMqSendLog.getMESSAGE_TOPIC() + "#" + sysRocketMqSendLog.getMESSAGE_TAG();
                    String namesrv = (String) Constants.topicTagToNamesrv.get(topicTag);
                    List producerList = null;
                    if (StringUtil.isAllNullOrBlank(new String[]{namesrv})) {
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

                    logger.debug("消息发送结果:messageSeries:{},messageId:{},sendResult:{}", new Object[]{sysRocketMqSendLog.getSERIES(), sendResult.getMsgId(), sendResult.getSendStatus()});
                    if (sendResult.getSendStatus() == SendStatus.SEND_OK) {
                        sysRocketMqSendLog.setMESSAGE_ID(sendResult.getMsgId());
                        sysRocketMqSendLog.setMSG_STATUS(Constants.send_success);
                        messUpdateFlag = true;
                    } else {
                        logger.error("消息发送失败,topic:{},tag:{},messageKey:{},body:{}", new Object[]{sysRocketMqSendLog.getMESSAGE_TOPIC(), sysRocketMqSendLog.getMESSAGE_TAG(), sysRocketMqSendLog.getMESSAGE_KEY(), body});
                        sysRocketMqSendLog.setMSG_STATUS(Constants.send_fail);
                        sysRocketMqSendLog.setFAIL_DETAIL("发送失败");
                    }
                } catch (Exception e) {
                    logger.error("消息发送发生异常,topic:{},tag:{},messageKey:{},body:{},原因:{}", new Object[]{sysRocketMqSendLog.getMESSAGE_TOPIC(), sysRocketMqSendLog.getMESSAGE_TAG(), sysRocketMqSendLog.getMESSAGE_KEY(), body, e.getMessage()});
                    logger.error(e.getMessage(), e);
                    String failDetail = "发送发生异常," + e.getMessage();
                    if (failDetail.length() > 1000) {
                        failDetail = failDetail.substring(0, 1000);
                    }

                    sysRocketMqSendLog.setFAIL_DETAIL(failDetail);
                    sysRocketMqSendLog.setMSG_STATUS(Constants.send_fail);
                }

                String newBody = JSONObject.toJSONString(sysRocketMqSendLog);
                AbstractRocketMqUtil.deleteSendLogCache(sysRocketMqSendLog, stringRedisTemplate);
                if (messUpdateFlag) {
                    AbstractRocketMqUtil.b(Constants.MESS_SQL, Constants.MESS_UPDATE_SEND_SQL, sysRocketMqSendLog.getSERIES().toString(), newBody);
                } else {
                    AbstractRocketMqUtil.sysRocketMqSendLogDaoE.updateStatus(sysRocketMqSendLog.getSERIES(), sysRocketMqSendLog.getFAIL_DETAIL(), sysRocketMqSendLog.getMSG_STATUS(), sysRocketMqSendLog.getORDER_MESS_FLAG());
                }

            }

            private void sendToAliyunMq() {
                String body = JSONObject.toJSONString(sysRocketMqSendLog);
                boolean messUpdateFlag = false;

                try {
                    com.aliyun.openservices.ons.api.Message message = new com.aliyun.openservices.ons.api.Message(sysRocketMqSendLog.getMESSAGE_TOPIC(), sysRocketMqSendLog.getMESSAGE_TAG(), body.getBytes());
                    message.setKey(sysRocketMqSendLog.getMESSAGE_KEY());
                    if (Constants.topicTagToConsumerInterval.get(sysRocketMqSendLog.getMESSAGE_TOPIC() + "#" + sysRocketMqSendLog.getMESSAGE_TAG()) != null) {
                        Long startDeliverTime = System.currentTimeMillis() + (Long) Constants.ew.get((Integer) Constants.topicTagToConsumerInterval.get(sysRocketMqSendLog.getMESSAGE_TOPIC() + "#" + sysRocketMqSendLog.getMESSAGE_TAG()));
                        message.setStartDeliverTime(startDeliverTime);
                    }

                    com.aliyun.openservices.ons.api.SendResult sendResult;
                    if (selector != null) {
                        sendResult = ((OrderProducer) AbstractRocketMqUtil.aliyunOrderProducers.get(AbstractRocketMqUtil.random.nextInt(AbstractRocketMqUtil.aliyunOrderProducers.size()))).send(message, sysRocketMqSendLog.getMESSAGE_KEY());
                    } else {
                        sendResult = ((Producer) AbstractRocketMqUtil.aliyunProducers.get(AbstractRocketMqUtil.random.nextInt(AbstractRocketMqUtil.aliyunProducers.size()))).send(message);
                    }

                    logger.debug("消息发送结果:messageSeries:" + sysRocketMqSendLog.getSERIES() + ",messageId:" + sendResult.getMessageId());
                    sysRocketMqSendLog.setMESSAGE_ID(sendResult.getMessageId());
                    sysRocketMqSendLog.setMSG_STATUS(Constants.send_success);
                    messUpdateFlag = true;
                } catch (Exception e) {
                    logger.error("消息发送发生异常,topic:{},tag:{},messageKey:{},body:{},原因:{}", new Object[]{sysRocketMqSendLog.getMESSAGE_TOPIC(), sysRocketMqSendLog.getMESSAGE_TAG(), sysRocketMqSendLog.getMESSAGE_KEY(), body, e.getMessage()});
                    logger.error(e.getMessage(), e);
                    String failDetail = "发送发生异常," + e.getMessage();
                    if (failDetail.length() > 1000) {
                        failDetail = failDetail.substring(0, 1000);
                    }

                    sysRocketMqSendLog.setFAIL_DETAIL(failDetail);
                    sysRocketMqSendLog.setMSG_STATUS(Constants.send_fail);
                }

                String newBody = JSONObject.toJSONString(sysRocketMqSendLog);
                AbstractRocketMqUtil.deleteSendLogCache(sysRocketMqSendLog, stringRedisTemplate);
                if (messUpdateFlag) {
                    AbstractRocketMqUtil.b(Constants.MESS_SQL, Constants.MESS_UPDATE_SEND_SQL, sysRocketMqSendLog.getSERIES().toString(), newBody);
                } else {
                    AbstractRocketMqUtil.sysRocketMqSendLogDaoE.updateStatus(sysRocketMqSendLog.getSERIES(), sysRocketMqSendLog.getFAIL_DETAIL(), sysRocketMqSendLog.getMSG_STATUS(), sysRocketMqSendLog.getORDER_MESS_FLAG());
                }

            }

        });
    }

    private static void deleteSendLogCache(SYS_ROCKET_MQ_SEND_LOG sysRocketMqSendLog, StringRedisTemplate stringRedisTemplate) {
        stringRedisTemplate.delete(MessageSendUtil.generateMessageKeyToRedis(sysRocketMqSendLog.getSERIES()));
    }

    public static void send(final String topic, final String tags, final String keys, final SYS_ROCKET_MQ_SEND_LOG sysRocketMqSendLog, Long tenantNumId) {
        executorService.submit(new Runnable() {

            public void run() {
                if (AbstractRocketMqUtil.useAliyunMqFlag.equals("N")) {
                    this.sendToRocketMq(topic, tags, keys, sysRocketMqSendLog);
                } else {
                    this.sendToAliyunMq(topic, tags, keys, sysRocketMqSendLog);
                }
            }

            private void sendToRocketMq(String topic, String tags, String keys, SYS_ROCKET_MQ_SEND_LOG sysRocketMqSendLog) {
                String body = JSONObject.toJSONString(sysRocketMqSendLog);
                Message message = new Message(topic, tags, body.getBytes());
                message.setKeys(keys);
                if (Constants.topicTagToConsumerInterval.get(sysRocketMqSendLog.getMESSAGE_TOPIC() + "#" + sysRocketMqSendLog.getMESSAGE_TAG()) != null) {
                    message.setDelayTimeLevel((Integer) Constants.topicTagToConsumerInterval.get(sysRocketMqSendLog.getMESSAGE_TOPIC() + "#" + sysRocketMqSendLog.getMESSAGE_TAG()));
                }

                String topicTag = topic + "#" + tags;
                String namesrv = (String) Constants.topicTagToNamesrv.get(topicTag);
                List producerList = null;
                if (StringUtil.isAllNullOrBlank(new String[]{namesrv})) {
                    producerList = AbstractRocketMqUtil.defaultMqProducers;
                } else {
                    producerList = (List) AbstractRocketMqUtil.mqProducers.get(namesrv);
                }

                if (producerList != null && !producerList.isEmpty()) {
                    try {
                        SendResult sendResult = ((DefaultMQProducer)producerList.get(AbstractRocketMqUtil.random.nextInt(producerList.size()))).send(message);
                    } catch (Exception e) {
                        logger.error("监控发送消息异常：" + e.getMessage(), e);
                    }

                } else {
                    AbstractRocketMqUtil.messSaveFailedLogger.error("发现和消息中心不匹配的nameSrv:" + namesrv + ",tag:" + tags);
                }
            }

            private void sendToAliyunMq(String topic, String tags, String keys, SYS_ROCKET_MQ_SEND_LOG sysRocketMqSendLog) {
                String body = JSONObject.toJSONString(sysRocketMqSendLog);

                try {
                    com.aliyun.openservices.ons.api.Message message = new com.aliyun.openservices.ons.api.Message(topic, tags, body.getBytes());
                    message.setKey(keys);
                    if (Constants.topicTagToConsumerInterval.get(sysRocketMqSendLog.getMESSAGE_TOPIC() + "#" + sysRocketMqSendLog.getMESSAGE_TAG()) != null) {
                        message.setStartDeliverTime(System.currentTimeMillis() + (Long) Constants.ew.get((Integer) Constants.topicTagToConsumerInterval.get(sysRocketMqSendLog.getMESSAGE_TOPIC() + "#" + sysRocketMqSendLog.getMESSAGE_TAG())));
                    }

                    if (Constants.orderTopicTagList.contains(sysRocketMqSendLog.getMESSAGE_TOPIC() + "#" + sysRocketMqSendLog.getMESSAGE_TAG())) {
                        ((OrderProducer) AbstractRocketMqUtil.aliyunOrderProducers.get(AbstractRocketMqUtil.random.nextInt(AbstractRocketMqUtil.aliyunOrderProducers.size()))).send(message, sysRocketMqSendLog.getMESSAGE_KEY());
                    } else {
                        ((Producer) AbstractRocketMqUtil.aliyunProducers.get(AbstractRocketMqUtil.random.nextInt(AbstractRocketMqUtil.aliyunProducers.size()))).send(message);
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }

            }

        });
    }

    public static void b(String topic, String tags, String keys, String body) {
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

    private static void send(String topic, String tags, final String keys, String body) {
        Message message = new Message(topic, tags, body.getBytes());
        message.setKeys(keys);
        MessageQueueSelector selector = new MessageQueueSelector() {
            public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                Integer keyHash = keys.hashCode();
                int mod = Math.abs(keyHash) % mqs.size();
                return mqs.get(mod);
            }
        };
        SendResult sendResult = null;

        try {
            sendResult = ((DefaultMQProducer)messOnlyMqProducers.get(random.nextInt(messOnlyMqProducers.size()))).send(message, selector, keys);
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
                ClusterInfo clusterInfo = mqAdminExt.examineBrokerClusterInfo();                // 通过rocketmq管理工具获取消息代理的集群信息
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

    public Set<Object> getNsKeys() {
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

    public Collection getConsumerIps() {
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


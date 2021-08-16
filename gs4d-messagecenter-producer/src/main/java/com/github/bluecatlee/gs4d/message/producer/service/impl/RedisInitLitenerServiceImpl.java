package com.github.bluecatlee.gs4d.message.producer.service.impl;

import com.github.bluecatlee.gs4d.common.utils.StringUtil;
import com.github.bluecatlee.gs4d.export.api.service.ExportDataService;
import com.github.bluecatlee.gs4d.message.api.service.MessageCenterProductorScheduleHandleFailedMessageService;
import com.github.bluecatlee.gs4d.message.producer.dao.RedisExpirekeyListenDao;
import com.github.bluecatlee.gs4d.message.producer.dao.SysRocketMqSendLogDao;
import com.github.bluecatlee.gs4d.message.producer.dao.SysTransationFailedLogDao;
import com.github.bluecatlee.gs4d.message.producer.model.REDIS_EXPIRED_KEY_TOPIC;
import com.github.bluecatlee.gs4d.message.producer.service.RedisInitLitenerService;
import com.github.bluecatlee.gs4d.transaction.service.MessagecenterRecallService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.PreDestroy;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.*;

/**
 * redis监听器初始化服务
 */
@Service
public class RedisInitLitenerServiceImpl implements RedisInitLitenerService {

    private static Logger logger = LoggerFactory.getLogger(RedisInitLitenerServiceImpl.class);

    @Value("#{settings['dataSign']}")
    private Long dataSign;
    @Value("#{settings['consumerFiledToopic']}")
    private String consumerFiledTopic;
    @Value("#{settings['initalMessTrans']}")
    private String initalMessTrans;

    @Autowired
    private RedisClusterConfiguration redisClusterConfiguration;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private JedisPoolConfig jedisPoolConfig;

    @Autowired
    private JedisConnectionFactory jedisConnectionFactory;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private SysRocketMqSendLogDao sysRocketMqSendLogDao;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    @Autowired
    private MessageCenterProductorScheduleHandleFailedMessageService messageCenterProductorScheduleHandleFailedMessageService;

    @Autowired
    private SysTransationFailedLogDao sysTransationFailedLogDao;

    @Autowired
    private RedisExpirekeyListenDao redisExpirekeyListenDao;

    @Autowired
    private MessagecenterRecallService messagecenterRecallService;

    private ExportDataService exportDataService;

    public static ExecutorService executorService;
    public static int shutdown_timeout = 1200;                  // 线程池关闭的等待超时时间 秒

    public void initalRedisListener() {
        logger.info("初始化redis消费者开始");
        if("Y".equalsIgnoreCase(initalMessTrans)) {
            HashSet set = new HashSet();
            Iterator iterator = this.redisClusterConfiguration.getClusterNodes().iterator();

            while(iterator.hasNext()) {
                RedisNode redisNode = (RedisNode)iterator.next();
                set.add(new HostAndPort(redisNode.getHost(), redisNode.getPort().intValue()));
            }

            final JedisCluster jedisCluster = StringUtil.isAllNotNullOrBlank(new String[]{this.jedisConnectionFactory.getPassword()}) ?
                    new JedisCluster(set, 5000, 5000, 5, this.jedisConnectionFactory.getPassword(), this.jedisPoolConfig) :
                    new JedisCluster(set, 5000, 5, this.jedisPoolConfig);

            final HashMap redisKeyHeadToTopicMap = new HashMap();
            List redisExpiredKeyTopicList = this.redisExpirekeyListenDao.queryAll();

            for(int i = 0; i < redisExpiredKeyTopicList.size(); ++i) {
                HashMap topicToTagMap = new HashMap();  // topic下有多个tag时 有点问题
                topicToTagMap.put(((REDIS_EXPIRED_KEY_TOPIC)redisExpiredKeyTopicList.get(i)).getTOPIC(), ((REDIS_EXPIRED_KEY_TOPIC)redisExpiredKeyTopicList.get(i)).getTAG());
                redisKeyHeadToTopicMap.put(((REDIS_EXPIRED_KEY_TOPIC)redisExpiredKeyTopicList.get(i)).getREDIS_KEY_HEAD(), topicToTagMap);
            }

            if(executorService == null) {
                this.initThreadPool();              // 初始化线程池
            }

            for(int i = 0; i < set.size() * 3; ++i) {
                executorService.execute(new Runnable() {
                    public void run() {
                        RedisMsgPubSubListener redisMsgPubSubListener = new RedisMsgPubSubListener(
                                RedisInitLitenerServiceImpl.this.stringRedisTemplate,
                                RedisInitLitenerServiceImpl.this.sysRocketMqSendLogDao,
                                RedisInitLitenerServiceImpl.this.jdbcTemplate,
                                RedisInitLitenerServiceImpl.this.consumerFiledTopic,
                                RedisInitLitenerServiceImpl.this.dataSign,
                                RedisInitLitenerServiceImpl.this.messageCenterProductorScheduleHandleFailedMessageService,
                                RedisInitLitenerServiceImpl.this.platformTransactionManager,
                                RedisInitLitenerServiceImpl.this.exportDataService,
                                RedisInitLitenerServiceImpl.this.sysTransationFailedLogDao,
                                redisKeyHeadToTopicMap,
                                RedisInitLitenerServiceImpl.this.messagecenterRecallService,
                                RedisInitLitenerServiceImpl.this.redisExpirekeyListenDao
                        );
                        jedisCluster.subscribe(redisMsgPubSubListener, new String[]{"__keyevent@0__:expired"});     // 监听rediskey失效的通道，通道名为__keyevent@0__:expired，0表示index=0的db
                    }
                });
            }

            logger.info("初始化redis消费者结束");
        }
    }

    private void initThreadPool() {
        ArrayBlockingQueue queue = new ArrayBlockingQueue(20);
        ThreadPoolExecutor executor = new ThreadPoolExecutor(20, 20, 0L, TimeUnit.MILLISECONDS, queue);
        executor.setRejectedExecutionHandler(new RejectedExecutionHandler() {
            public void rejectedExecution(Runnable runnable, ThreadPoolExecutor executor) {
                try {
                    executor.getQueue().put(runnable);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });
        executorService = executor;
    }

    @PreDestroy
    public void preDestroy() {
        preDestroy(executorService, shutdown_timeout, TimeUnit.SECONDS);
    }

    private static void preDestroy(ExecutorService executorService, int timeout, TimeUnit timeUnit) {
        try {
            executorService.shutdownNow();
            if(!executorService.awaitTermination(timeout, timeUnit)) {
                logger.error("thread poool did not terminated");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

    }

}


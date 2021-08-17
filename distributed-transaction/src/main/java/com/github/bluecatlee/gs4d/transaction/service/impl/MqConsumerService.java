package com.github.bluecatlee.gs4d.transaction.service.impl;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.remoting.exception.RemotingException;
import com.github.bluecatlee.gs4d.common.utils.Md5Util;
import com.github.bluecatlee.gs4d.sequence.utils.SeqGetUtil;
import com.github.bluecatlee.gs4d.transaction.api.utils.TransactionApiUtil;
import com.github.bluecatlee.gs4d.transaction.dao.TransactionLogDao;
import com.github.bluecatlee.gs4d.transaction.dao.TransactionSqlLogDao;
import com.github.bluecatlee.gs4d.transaction.service.RollBackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.Resource;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
public class MqConsumerService implements ApplicationListener<ContextRefreshedEvent> {
    
    protected static Logger logger = LoggerFactory.getLogger(MqConsumerService.class);

    @Value("#{settings['defaultNamesrv']}")
    private String defaultNamesrv;
    @Value("#{settings['zookeeper.host.port']}")
    private String zkAddress;

    @Autowired
    public StringRedisTemplate stringRedisTemplate;

    @Autowired
    private TransactionLogDao transactionLogDao;

    @Autowired
    private TransactionSqlLogDao transactionSqlLogDao;

    @Resource(name = "transactionManager")
    private PlatformTransactionManager transactionManager;

    @Resource(name = "dynamicTransactionManager")
    private PlatformTransactionManager dynamicTransactionManager;

    public static ThreadPoolExecutor threadPoolExecutor;

    @Resource
    private RollBackService rollBackService;

    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            if (event.getApplicationContext().getParent() == null) {
                SeqGetUtil.initeZkConfig(this.zkAddress);
                initThreadPool();
                this.d(TransactionApiUtil.mqTopic, TransactionApiUtil.mqTag);
                this.c(TransactionApiUtil.mqUpdateSqlTopic, TransactionApiUtil.mqUpdateSqlTag);
            }
        } catch (Exception e) {
            logger.error("初始化消息错误，原因" + e.getMessage(), e);
        }

    }

    public static void initThreadPool() {
        ArrayBlockingQueue queue = new ArrayBlockingQueue(100);
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(50, 50, 0L, TimeUnit.MILLISECONDS, queue);
        threadPoolExecutor.setRejectedExecutionHandler(new RejectedExecutionHandler() {
            public void rejectedExecution(Runnable runnable, ThreadPoolExecutor executor) {
                try {
                    executor.getQueue().put(runnable);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });
        MqConsumerService.threadPoolExecutor = threadPoolExecutor;
    }

    private void c(String topic, String tag) throws InterruptedException, MQBrokerException, RemotingException, MQClientException {
        DefaultMQPushConsumer mqPushConsumer = this.initDefaultMQPushConsumer(topic, tag);
        mqPushConsumer.registerMessageListener(new BI(this.transactionLogDao));
        mqPushConsumer.start();
    }

    private void d(String topic, String tag) throws InterruptedException, MQBrokerException, RemotingException, MQClientException {
        DefaultMQPushConsumer mqPushConsumer = this.initDefaultMQPushConsumer(topic, tag);
        mqPushConsumer.registerMessageListener(new MessageListenerConcurrentlyImpl(this.stringRedisTemplate, this.transactionManager, this.transactionSqlLogDao, this.rollBackService, this.transactionLogDao));
        mqPushConsumer.start();
    }

    DefaultMQPushConsumer initDefaultMQPushConsumer(String topic, String tag) throws InterruptedException, MQBrokerException, RemotingException, MQClientException {
        String consumerGroup = this.getConsumerGroupName(topic, tag);
        DefaultMQPushConsumer mqPushConsumer = new DefaultMQPushConsumer(consumerGroup);
        mqPushConsumer.setNamesrvAddr(this.defaultNamesrv);
        mqPushConsumer.setInstanceName((new Date()).getTime() + Md5Util.md5(UUID.randomUUID().toString()));
        mqPushConsumer.subscribe(topic, tag);
        return mqPushConsumer;
    }

    private String getConsumerGroupName(String topic, String tag) {
        return topic + "-" + tag;
    }
}


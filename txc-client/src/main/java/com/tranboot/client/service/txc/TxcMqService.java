package com.tranboot.client.service.txc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.MessageQueueSelector;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.common.message.MessageQueue;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.github.bluecatlee.gs4d.transaction.api.model.MqTransactionModel;
import com.github.bluecatlee.gs4d.transaction.api.utils.TransactionApiUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

// todo 仅实现了InitializingBean 没有使用@Component注解的类会被作为spring bean加载到容器吗？
public class TxcMqService implements InitializingBean {
    protected Logger logger = LoggerFactory.getLogger(TxcMqService.class);
    private DefaultMQProducer producer;
    private String nameServerAddr;

    public void setNameServerAddr(String nameServerAddr) {
        this.nameServerAddr = nameServerAddr;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // 初始化mq生产者
        this.producer = new DefaultMQProducer("productor_group");
        this.producer.setNamesrvAddr(this.nameServerAddr);
        this.producer.setInstanceName(UUID.randomUUID().toString());
        this.producer.setSendMsgTimeout(20000);
        this.producer.setDefaultTopicQueueNums(30);

        try {
            this.producer.start();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("DbsyncMqService Producer start error");
        }
    }

    public void sendCommit(final long xid, Date transactionCommitDate) throws Exception {
        this.logger.debug("开始发送commit消息 xid:{},name server address:{}", xid, this.nameServerAddr);
        MqTransactionModel model = new MqTransactionModel();
        model.setTransactionId(xid);
        model.setTransactionCommmitDate(transactionCommitDate);
        MessageQueueSelector mqs = new MessageQueueSelector() {
            public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                int index = (int)(Math.abs(xid) % (long)mqs.size());
                return (MessageQueue)mqs.get(index);
            }
        };
        Message message = new Message();
        message.setBody(JSON.toJSONBytes(model, new SerializerFeature[0]));
        message.setTopic(TransactionApiUtil.mqUpdateSqlTopic);
        message.setTags(TransactionApiUtil.mqUpdateSqlTag);
        message.setKeys(String.valueOf(xid));
        message.setDelayTimeLevel(2);
        this.producer.send(message, mqs, (Object)null);
        this.logger.debug("发送commit消息完成{}", message.toString());
    }

    public void sendRollback(final long xid, Date transactionCommitDate) throws Exception {
        this.logger.debug("开始发送rollback消息 xid:{}, name server address:{}", xid, this.nameServerAddr);
        MqTransactionModel model = new MqTransactionModel();
        model.setTransactionId(xid);
        model.setTransactionCommmitDate(transactionCommitDate);
        MessageQueueSelector mqs = new MessageQueueSelector() {
            public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                int index = (int)(Math.abs(xid) % (long)mqs.size());
                return (MessageQueue)mqs.get(index);
            }
        };
        Message message = new Message();
        message.setBody(JSON.toJSONBytes(model, new SerializerFeature[0]));
        message.setTopic(TransactionApiUtil.mqTopic);
        message.setTags(TransactionApiUtil.mqTag);
        message.setKeys(String.valueOf(xid));
        message.setDelayTimeLevel(2);
        this.producer.send(message, mqs, (Object)null);
        this.logger.debug("开始发送rollback消息完成{}", message.toString());
    }

}


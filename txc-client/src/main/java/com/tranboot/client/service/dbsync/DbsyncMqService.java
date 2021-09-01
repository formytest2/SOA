//package com.tranboot.client.service.dbsync;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.serializer.SerializerFeature;
//import com.alibaba.rocketmq.client.exception.MQBrokerException;
//import com.alibaba.rocketmq.client.exception.MQClientException;
//import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
//import com.alibaba.rocketmq.client.producer.MessageQueueSelector;
//import com.alibaba.rocketmq.common.message.Message;
//import com.alibaba.rocketmq.common.message.MessageQueue;
//import com.alibaba.rocketmq.remoting.exception.RemotingException;
//import com.gb.soa.omp.dbsync.model.MqSyncModel;
//import com.gb.soa.omp.dbsync.model.RdisSyncModel;
//import java.util.List;
//import java.util.UUID;
//
//public class DbsyncMqService {
//    private DefaultMQProducer producer = new DefaultMQProducer("productor_group");
//
//    public DbsyncMqService(String nameServerAddr) {
//        this.producer.setNamesrvAddr(nameServerAddr);
//        this.producer.setInstanceName(UUID.randomUUID().toString());
//        this.producer.setSendMsgTimeout(20000);
//        this.producer.setDefaultTopicQueueNums(30);
//
//        try {
//            this.producer.start();
//        } catch (MQClientException var3) {
//            var3.printStackTrace();
//            throw new RuntimeException("DbsyncMqService Producer start error");
//        }
//    }
//
//    public void send(MqSyncModel model, final RdisSyncModel _model) throws MQClientException, RemotingException, MQBrokerException, InterruptedException {
//        MessageQueueSelector mqs = new MessageQueueSelector() {
//            public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
//                Integer id = _model.getMessageKey().hashCode();
//                int index = Math.abs(id) % mqs.size();
//                return (MessageQueue)mqs.get(index);
//            }
//        };
//        Message message = new Message();
//        message.setBody(JSON.toJSONBytes(model, new SerializerFeature[0]));
//        message.setTags("DBSYNC_0001_1");
//        message.setTopic("DBSYNC_0001");
//        message.setKeys(_model.getMessageKey());
//        this.producer.send(message, mqs, (Object)null);
//    }
//
//    public static String messageKey(String bizKey, String sourceDb, String sourceTable, String targetDb) {
//        return UUID.randomUUID().toString() + "_" + bizKey + "_" + sourceDb + "_" + sourceTable + "_" + targetDb;
//    }
//}

package com.github.bluecatlee.gs4d.message.consumer.utils;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.github.bluecatlee.gs4d.common.utils.StringUtil;
import com.github.bluecatlee.gs4d.message.consumer.model.PlatformMqTopic;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Constants {

    public static final String SUB_SYSTEM = "messagecenter";

    public static final String er = "businessMethodInfo_";
    public static final String CONSUMER_TYPE_DUBBO = "dubbo";
    public static final String CONSUMER_TYPE_HTTP = "http";
    public static final String eu = "topicInfo_";
    public static final String ev = "workflowStep_";
    public static final String ew = "workflow_back_Step_";
    public static final String ex = "workflowInstance_";
    public static final String ey = "step";

    public static final String queryAllSystemNameSqlId = "SYSTEM-NAME-001";
    public static final String querySystemNameBySystemNumIdSqlId = "SYSTEM-NAME-002";

    public static final Long ez = 1L;   // order_mess_flag
    public static final Long eA = 3L;
    public static String eB = "1";

    public static Integer eC = 1;
    public static Integer eD = 2;
    public static Integer eE = 3;
    public static Integer eF = 4;
    public static Integer eG = 5;
    public static Integer eH = 6;
    public static Integer eI = 7;       // step_id
    public static Integer eJ = 8;
    public static Integer eK = 9;
    public static Map<Integer, String> sendTypeDetails = new HashMap() {
        {
            this.put(0, "正常发送的消息");
            this.put(Constants.eC, "正常发送的消息");
            this.put(Constants.eD, "重试发送的消息");
            this.put(Constants.eE, "redis回查发送的消息");
            this.put(Constants.eF, "定时器回查发送的消息");
            this.put(Constants.eG, "消息丢失后补充发送的消息");
            this.put(Constants.eH, "mq挂了后补充发送的消息");
            this.put(Constants.eI, "大补偿发送的消息");
            this.put(Constants.eJ, "没有系统编号的消息");
            this.put(Constants.eK, "取消后的消息");
        }
    };

    public static final Long eO = 0L;
    public static final String fromSystem = "19";
    public static final String eR = "gbMonitorClient";
    public static final String eS = "monitor_series";
    public static final String MESS_SQL = "MESS_SQL";
    public static final String MESS_SAVE_SQL = "MESS_SAVE_SQL";
    public static final String MESS_UPDATE_SEND_SQL = "MESS_UPDATE_SEND_SQL";
    public static final String MESS_UPDATE_SUCC_SQL = "MESS_UPDATE_SUCC_SQL";
    public static final String MESS_UPDATE_FAIL_SQL = "MESS_UPDATE_FAIL_SQL";
    public static final String FLOWER_TOPIC = "FLOWER_TOPIC";
    public static final String ORDER_FLOWER_TAG = "ORDER_FLOWER_TAG";
    public static final String fa = "消息中心消费失败消息定时告警";

    public static Map<String, DefaultMQPushConsumer> consumerGroupTable = new HashMap();    // consumerGroup -> DefaultMQPushConsumer

    public static Map<String, Map<String, String>> fc = new HashMap();
    public static List<String> fd = new ArrayList();
    public static List<String> fe = new ArrayList();
    public static List<String> ff = new ArrayList();
    public static List<String> orderTopicTagList = new ArrayList();
    public static Map<String, List<String>> topicTagToRetryIntervalList = new HashMap();
    public static Map<String, List<String>> topicTagToCorrectCodesList = new HashMap();
    public static Map<String, PlatformMqTopic> platformMqTopicTable = new HashMap();      // topic#tag#tenantNumId#dataSign  ->   PlatformMqTopic
    public static Map<String, String> fj = new HashMap();
    public static Map<String, Long> fk = new HashMap();
    public static List<String> fl = new ArrayList();
    public static Map<String, String> topicTagToNamesrv = new HashMap();
    public static List<String> consumerIpList = new ArrayList();
    public static List<String> consumerIpKeyList = new ArrayList();
    public static Map<String, Integer> topicTagToConsumerInterval = new HashMap();

    public static List<String> retryIntervalList = new ArrayList<String>() {
        {
            this.add("0");
            this.add("1s");
            this.add("5s");
            this.add("10s");
            this.add("30s");
            this.add("1m");
            this.add("2m");
            this.add("3m");
            this.add("4m");
            this.add("5m");
            this.add("6m");
            this.add("7m");
            this.add("8m");
            this.add("9m");
            this.add("10m");
            this.add("20m");
            this.add("30m");
            this.add("1h");
            this.add("2h");
        }
    };

    public static String fs = "MESS_MON";
    public static String ft = "521";
    static String fu = "%RETRY%";
    static String fv = "%DLQ%";

    public static List<String> fw = new ArrayList<String>() {
        {
            this.add("10s");
            this.add("30s");
            this.add("1m");
            this.add("2m");
            this.add("3m");
            this.add("4m");
            this.add("5m");
            this.add("6m");
            this.add("7m");
            this.add("8m");
            this.add("9m");
            this.add("10m");
            this.add("20m");
            this.add("1h");
            this.add("2h");
        }
    };

    public static List<Long> fx = new ArrayList<Long>() {
        {
            this.add(0L);
            this.add(1000L);
            this.add(5000L);
            this.add(10000L);
            this.add(30000L);
            this.add(60000L);
            this.add(120000L);
            this.add(180000L);
            this.add(240000L);
            this.add(300000L);
            this.add(360000L);
            this.add(420000L);
            this.add(480000L);
            this.add(540000L);
            this.add(600000L);
            this.add(1200000L);
            this.add(1800000L);
            this.add(3600000L);
            this.add(7200000L);
        }
    };

    public static Cache<String, String> cache;
    public static final int fz = 1;
    public static final int fA = 2;
    public static final String fB = "100";

    /*
     * serviceName_dubboGroup  -> reference(消费者服务类的代理对象)
     */
    public static Map<String, Object> serviceNameDubboGroup2consumerServiceProxy = new HashMap();            // 生产
    public static Map<String, Object> serviceNameDubboGroup2consumerServiceProxy_Test = new HashMap();       // 测试
    public static Map<String, Object> serviceNameDubboGroup2consumerServiceProxy_Devepop = new HashMap();    // 开发

    public static String index2RetryTime(String retryInterval) {
        if (StringUtil.isAllNullOrBlank(new String[]{retryInterval})) {
            return "";
        } else {
            String[] retryIntervalArr = retryInterval.split(",");
            StringBuilder sb = new StringBuilder();

            for(int i = 0; i < retryIntervalArr.length; ++i) {
                sb.append(retryIntervalList.indexOf(retryIntervalArr[i]) + ",");
            }

            sb.delete(sb.length() - 1, sb.length());
            return sb.toString();
        }
    }

    public static String y(String var0) {
        if (StringUtil.isAllNullOrBlank(new String[]{var0})) {
            return "";
        } else {
            String[] var1 = var0.split(",");
            StringBuilder var2 = new StringBuilder();

            for(int var3 = 0; var3 < var1.length; ++var3) {
                var2.append((String)retryIntervalList.get(Integer.valueOf(var1[var3])) + ",");
            }

            var2.delete(var2.length() - 1, var2.length());
            return var2.toString();
        }
    }

    public static String getErrorCode(String series, String var1) {
        return series + "010" + var1;
    }

    public static String getTopicKey(String topic, String tag, String tenantNumId, String dataSign) {
        return topic + "#" + tag + "#" + tenantNumId + "#" + dataSign;
    }

    public static String getAliyunMqConsumerId(String topic, String subExpression) {
        return "CID-" + topic + "-" + subExpression;
    }

    public static String i(String var0, String var1) {
        return fu + var0 + "-" + var1;
    }

    public static String j(String var0, String var1) {
        return fv + var0 + "-" + var1;
    }

    public static String k(String var0, String var1) {
        return var0 + "#" + var1;
    }

    static {
        cache = CacheBuilder.newBuilder().concurrencyLevel(16).expireAfterWrite(5L, TimeUnit.MINUTES).build();
    }
}

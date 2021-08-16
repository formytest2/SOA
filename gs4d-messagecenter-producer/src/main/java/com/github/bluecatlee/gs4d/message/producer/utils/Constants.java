package com.github.bluecatlee.gs4d.message.producer.utils;

import com.github.bluecatlee.gs4d.common.utils.StringUtil;
import com.github.bluecatlee.gs4d.message.producer.model.PLATFORM_MQ_TOPIC;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Constants {

    public static final String SUB_SYSTEM = "messagecenter";

    public static final String dA = "businessMethodInfo_";
    public static final String dB = "dubbo";
    public static final String dC = "http";
    public static final String dD = "topicInfo_";
    public static final String dE = "workflowStep_";
    public static final String dF = "workflow_back_Step_";
    public static final String dG = "workflowInstance_";
    public static final String dH = "step";
    public static final String TransationFailedLogReHandle_LockKey = "messagecenter_jobmessage_handle";

    public static final Long mess_flag_common = 0L;   // 普通消息
    public static final Long mess_flag_order = 1L;   // 顺序消息
    public static final Long dJ = 2L;
    public static final Long dM = 3L;
    public static final Integer dN = 5;

    public static final Long dO = 100L;

    public static final String systemId = "19";
    public static final String dR = "gbMonitorClient";
    public static final String dS = "monitor_series";

    public static final String MESS_SQL = "MESS_SQL";
    public static final String MESS_SAVE_SQL = "MESS_SAVE_SQL";
    public static final String MESS_UPDATE_SEND_SQL = "MESS_UPDATE_SEND_SQL";
    public static final String MESS_UPDATE_SUCC_SQL = "MESS_UPDATE_SUCC_SQL";
    public static final String MESS_UPDATE_FAIL_SQL = "MESS_UPDATE_FAIL_SQL";

    public static final String FLOWER_TOPIC = "FLOWER_TOPIC";
    public static final String ORDER_FLOWER_TAG = "ORDER_FLOWER_TAG";
    public static final String ea = "消息中心消费失败消息定时告警";

    /*
    * 里层innerMap是 sonTag -> sonTopic 的映射
    * 外层map是 fatherTopic#tenantNumId -> innerMap 的映射
    * */
    public static Map<String, Map<String, String>> oneToManyTopicsMap = new HashMap();

    public static List<String> notDistinctTopicTagList = new ArrayList();       // 消息内容不需要去重的topic#tag集合 但是实际上还是会通过message_key进行去重
    public static List<String> distinctTopicTagList = new ArrayList();          // 消息内容需要去重的topic#tag集合
    public static List<String> ee = new ArrayList();
    public static List<String> orderTopicTagList = new ArrayList();                // 顺序消费(wether_order_mess=Y)的topic#tag集合
    public static List<String> notInsertDbTopicTagList = new ArrayList();          // 无需插入本地数据库(wether_insertdb=N)的topic#tag集合

    public static Map<String, String> topicTagToNamesrv = new HashMap();               // topic#tag  -> nameServerAddress(真实地址) 的映射
    public static Map<String, Integer> topicTagToConsumerInterval = new HashMap();      // topic#tag -> consumer_interval

    public static Map<String, List<String>> eg = new HashMap();
    public static Map<String, PLATFORM_MQ_TOPIC> platformMqTopicTable = new HashMap();        // getTopicTagKey(topic#tag#tenantNumId#dataSign) -> PLATFORM_MQ_TOPIC
    public static Map<String, String> ei = new HashMap();
    public static Map<String, Long> ej = new HashMap();
    public static Map<String, List<String>> el = new HashMap();


    public static List<String> consumerIpKeyList = new ArrayList();     // 消费者ip路由键列表
    public static List<String> consumerIpList = new ArrayList();        // 消费者ip列表

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

    public static String er = "MESS_MON";
    public static String es = "521";
    static String et = "%RETRY%";
    static String dlqPrefix = "%DLQ%";  // 延迟队列主题前缀

    public static List<String> ev = new ArrayList<String>() {
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

    public static List<Long> ew = new ArrayList<Long>() {
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

    public static Integer ex = 1;   // 步骤 todo step_id
    public static Integer ey = 2;
    public static Integer ez = 3;
    public static Integer eA = 4;
    public static Integer eB = 5;
    public static Integer eC = 6;   // send_type  重发
    public static Integer eD = 8;   // 事务失败?
    public static Integer eE = 9;

    public static Integer send_success = 1;       // 发送成功
    public static Integer eF = 2;               // 预发送
    public static Integer send_fail = 3;       // 发送失败
    public static Integer send_cancel = 4;     // 取消发送
    public static Integer eJ = 5;       // todo msg_status
    public static Integer eK = 6;

    public static Long de = 1L;
    public static Long df = 2L;
    public static Long dg = 3L;
    public static Long dh = 6L;
    public static Long di = 4L;
    public static Long dj = 5L;
    public static Long dk = 7L;
    public static Long dl = 8L;
    public static Long dm = 9L;

    public static Map<Long, String> jobLogSeriesToUuid = new HashMap(); // SysRocketMqJobLog.series -> uuid  即定时消息日志的series到uuid的映射

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

    public static String z(String var0) {
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

    public static String getTopicTagKey(String topic, String tag, String tenantNumId, String dataSign) {
        return topic + "#" + tag + "#" + tenantNumId + "#" + dataSign;
    }

    public static String getAliyunMqConsumerId(String topic, String subExpression) {
        return "CID-" + topic + "-" + subExpression;
    }

    public static String g(String var0, String var1) {
        return et + var0 + "-" + var1;
    }

    public static String h(String var0, String var1) {
        return dlqPrefix + var0 + "-" + var1;
    }

    public static String getJobMessageLockName(Long series) {
        return "JobMessage_" + series;
    }
}


package com.github.bluecatlee.gs4d.transaction.utils;

import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.protocol.body.ClusterInfo;
import com.alibaba.rocketmq.common.subscription.SubscriptionGroupConfig;
import com.alibaba.rocketmq.remoting.exception.RemotingException;
import com.alibaba.rocketmq.tools.admin.DefaultMQAdminExt;
import com.alibaba.rocketmq.tools.command.CommandUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.Map.Entry;

public class CreateAndUpdateSubscriptionGroupConfigUtil {

    protected static Logger logger = LoggerFactory.getLogger(CreateAndUpdateSubscriptionGroupConfigUtil.class);

    public static Map<String, List<String>> clusters = new HashMap();

    /**
     * 初始化消费者的重试次数
     * @param namesrvAddr
     * @param groupName
     * @throws InterruptedException
     * @throws MQBrokerException
     * @throws RemotingException
     * @throws MQClientException
     */
    public static void initRetriesOfConsumerGroup(String namesrvAddr, String groupName) throws InterruptedException, MQBrokerException, RemotingException, MQClientException {
        DefaultMQAdminExt mqAdminExt = initMQAdminExt(namesrvAddr);

        try {
            initClusters(namesrvAddr, mqAdminExt);
            String clusterName = "";
            List clusterList = (List)clusters.get(namesrvAddr);

            for(int i = 0; i < clusterList.size(); ++i) {
                if (i == 0) {
                    clusterName = clusterName + (String)clusterList.get(i);
                } else {
                    clusterName = clusterName + ";" + (String)clusterList.get(i);
                }
            }

            SubscriptionGroupConfig subscriptionGroupConfig = new SubscriptionGroupConfig();
            subscriptionGroupConfig.setConsumeBroadcastEnable(false);
            subscriptionGroupConfig.setConsumeFromMinEnable(false);
            subscriptionGroupConfig.setRetryMaxTimes(0);
            subscriptionGroupConfig.setRetryQueueNums(1);
            subscriptionGroupConfig.setGroupName(groupName);
            Set masterAddrs = CommandUtil.fetchMasterAddrByClusterName(mqAdminExt, clusterName);
            Iterator iterator = masterAddrs.iterator();

            while(iterator.hasNext()) {
                String masterAddr = (String)iterator.next();
                mqAdminExt.createAndUpdateSubscriptionGroupConfig(masterAddr, subscriptionGroupConfig);
            }
        } catch (Exception e) {
            logger.error("初始化消费失败后重试次数失败" + e.getMessage(), e);
        } finally {
            mqAdminExt.shutdown();
        }

    }

    private static DefaultMQAdminExt initMQAdminExt(String namesrvAddr) throws MQClientException {
        DefaultMQAdminExt mqAdminExt = new DefaultMQAdminExt();
        mqAdminExt.setNamesrvAddr(namesrvAddr);
        mqAdminExt.setInstanceName(Long.toString(System.currentTimeMillis()));
        mqAdminExt.setHeartbeatBrokerInterval(30000);
        mqAdminExt.start();
        logger.info("初始化defaultMQAdminExt完成");
        return mqAdminExt;
    }

    private static void initClusters(String namesrvAddr, DefaultMQAdminExt mqAdminExt) {
        try {
            if (clusters.get(namesrvAddr) != null) {
                return;
            }

            ArrayList list = new ArrayList();
            ClusterInfo clusterInfo = mqAdminExt.examineBrokerClusterInfo();
            Iterator iterator = clusterInfo.getClusterAddrTable().entrySet().iterator();

            while(iterator.hasNext()) {
                Entry entry = (Entry)iterator.next();
                String clusterName = (String)entry.getKey();
                list.add(clusterName);
            }

            clusters.put(namesrvAddr, list);
        } catch (Exception e) {
            logger.error("初始化clusters失败" + e.getMessage(), e);
        }

    }

    public static Map<String, List<String>> getClusters() {
        return clusters;
    }

    public static void setClusters(Map<String, List<String>> clusters) {
        clusters = clusters;
    }

}


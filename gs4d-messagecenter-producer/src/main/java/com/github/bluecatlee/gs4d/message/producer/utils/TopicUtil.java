package com.github.bluecatlee.gs4d.message.producer.utils;

import com.alibaba.rocketmq.common.TopicConfig;
import com.alibaba.rocketmq.common.protocol.body.ClusterInfo;
import com.alibaba.rocketmq.tools.admin.DefaultMQAdminExt;
import com.alibaba.rocketmq.tools.command.CommandUtil;
import com.github.bluecatlee.gs4d.common.exception.ExceptionType;
import com.github.bluecatlee.gs4d.common.exception.ValidateBusinessException;
import com.github.bluecatlee.gs4d.common.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

public class TopicUtil {

    private static Logger logger = LoggerFactory.getLogger(TopicUtil.class);

    public static List<String> clusterNameList;

    public static void initTopic(String topicName, String namesrv) {
        try {
            if (StringUtil.isAllNullOrBlank(new String[]{topicName})) {
                throw new RuntimeException("初始化topic不能为空");
            }

            DefaultMQAdminExt mqAdminExt = new DefaultMQAdminExt();

            try {
                if (clusterNameList == null) {
                    initClusterNameList(namesrv);
                }

                mqAdminExt.setInstanceName(Long.toString(System.currentTimeMillis()));
                mqAdminExt.setNamesrvAddr(namesrv);
                mqAdminExt.start();
                String clusterName = "";

                for(int i = 0; i < clusterNameList.size(); ++i) {
                    if (i == 0) {
                        clusterName = clusterName + clusterNameList.get(i);
                    } else {
                        clusterName = clusterName + ";" + clusterNameList.get(i);
                    }
                }

                TopicConfig topicConfig = new TopicConfig();
                topicConfig.setReadQueueNums(50);
                topicConfig.setWriteQueueNums(50);
                topicConfig.setTopicName(topicName);
                Set masterAddrs = CommandUtil.fetchMasterAddrByClusterName(mqAdminExt, clusterName);
                Iterator iterator = masterAddrs.iterator();

                while(iterator.hasNext()) {
                    String masterAddr = (String)iterator.next();
                    mqAdminExt.createAndUpdateTopicConfig(masterAddr, topicConfig); // 创建topic
                }
            } catch (Exception e) {
                throw new ValidateBusinessException(Constants.SUB_SYSTEM, ExceptionType.VBE20031, "创建topic产生" + e.getMessage() + "的问题");
            } finally {
                mqAdminExt.shutdown();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

    }

    private static List<String> initClusterNameList(String namesrv) throws Exception {
        DefaultMQAdminExt mqAdminExt = new DefaultMQAdminExt();

        try {
            mqAdminExt.setNamesrvAddr(namesrv);
            mqAdminExt.setInstanceName(Long.toString(System.currentTimeMillis()));
            mqAdminExt.start();
            ArrayList list = new ArrayList();
            ClusterInfo clusterInfo = mqAdminExt.examineBrokerClusterInfo();
            Iterator iterator = clusterInfo.getClusterAddrTable().entrySet().iterator();

            while(iterator.hasNext()) {
                Entry entry = (Entry)iterator.next();
                String key = (String)entry.getKey();
                list.add(key);
            }

            clusterNameList = list;
            return list;
        } catch (Exception e) {
            throw new Exception("消息中心web项目获取客户端信息时出现错误。", e);
        } finally {
            mqAdminExt.shutdown();
        }
    }
}


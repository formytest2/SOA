package com.github.bluecatlee.gs4d.message.producer.service.impl;

import com.github.bluecatlee.gs4d.common.utils.StringUtil;
import com.github.bluecatlee.gs4d.message.api.model.SonTopicRelationModel;
import com.github.bluecatlee.gs4d.message.producer.dao.PlaftformMqTopicManyDao;
import com.github.bluecatlee.gs4d.message.producer.dao.PlatformMqTopicDao;
import com.github.bluecatlee.gs4d.message.producer.model.PLATFORM_MQ_TOPIC;
import com.github.bluecatlee.gs4d.message.producer.service.MqProductorBizInitService;
import com.github.bluecatlee.gs4d.message.producer.utils.AbstractRocketMqUtil;
import com.github.bluecatlee.gs4d.message.producer.utils.Constants;
import com.github.bluecatlee.gs4d.message.producer.utils.RocketMqConfigUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 生产者初始化服务
 */
@Service("mqProductorBizInitService")
public class MqProductorBizInitServiceImpl implements MqProductorBizInitService {

    private static Logger logger = LoggerFactory.getLogger(MqProductorBizInitServiceImpl.class);

    @Autowired
    private PlaftformMqTopicManyDao plaftformMqTopicManyDao;

    @Autowired
    private PlatformMqTopicDao platformMqTopicDao;

    @Autowired
    private RocketMqConfigUtil rocketMqConfigUtil;

    public void initalAllProductorBiz() {
        try {
            logger.info("初始化rocketmq生产者业务开始");
            List<SonTopicRelationModel> list = this.plaftformMqTopicManyDao.queryAllOneToManyTopics();
            logger.info("初始化topic子表完成");
            if (list != null) {                             // 加载一对多topic的关系到oneToManyTopicsMap中
                for(int i = 0; i < list.size(); ++i) {
                    if (Constants.oneToManyTopicsMap.containsKey((list.get(i)).getFatherTopic() + "#" + (list.get(i)).getTenantNumId())) {
                        Map map = (Map) Constants.oneToManyTopicsMap.get((list.get(i)).getFatherTopic() + "#" + (list.get(i)).getTenantNumId());
                        map.put((list.get(i)).getSonTag(), (list.get(i)).getSonTopic());
                        Constants.oneToManyTopicsMap.put((list.get(i)).getFatherTopic() + "#" + (list.get(i)).getTenantNumId(), map);
                    } else {
                        Map map = new HashMap();
                        map.put((list.get(i)).getSonTag(), (list.get(i)).getSonTopic());
                        Constants.oneToManyTopicsMap.put((list.get(i)).getFatherTopic() + "#" + (list.get(i)).getTenantNumId(), map);
                    }
                }
            }

            List<PLATFORM_MQ_TOPIC> topics = this.platformMqTopicDao.queryAll();
            logger.info("成功获取topic表数据，topic数量为：" + topics.size());
            Iterator iterator = topics.iterator();

            while(iterator.hasNext()) {
                PLATFORM_MQ_TOPIC platformMqTopic = (PLATFORM_MQ_TOPIC)iterator.next();
                this.initalProductorBiz(platformMqTopic);   // 遍历初始化所有topic的业务数据
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void initalProductorBiz(PLATFORM_MQ_TOPIC platformMqTopic) throws Exception {
        if (StringUtil.isAllNotNullOrBlank(new String[]{platformMqTopic.getMQ_NAMESRV()})) {
            String topicTagKey = platformMqTopic.getTOPIC() + "#" + platformMqTopic.getTAG();
            String nameServerAddress = this.rocketMqConfigUtil.getNsRealAddress(platformMqTopic.getMQ_NAMESRV());
            if (nameServerAddress == null) {
                throw new Exception("消息中心发现没有配置的namesrv路由，tag:" + platformMqTopic.getTAG() + "错误的路由名" + platformMqTopic.getMQ_NAMESRV());
            }

            platformMqTopic.setMQ_NAMESRV(nameServerAddress);    // 更新nameserver路由(虚拟的nameserver名称)到真实的nameserver地址
            Constants.topicTagToNamesrv.put(topicTagKey, nameServerAddress);
        } else {
            platformMqTopic.setMQ_NAMESRV(AbstractRocketMqUtil.defaultNamesrv);
        }

        if (StringUtil.isAllNotNullOrBlank(new String[]{platformMqTopic.getIS_DISTINCT()}) && "K".equals(platformMqTopic.getIS_DISTINCT())) {
            Constants.notDistinctTopicTagList.add(platformMqTopic.getTOPIC() + "#" + platformMqTopic.getTAG());
        }

        if (StringUtil.isAllNotNullOrBlank(new String[]{platformMqTopic.getIS_DISTINCT()}) && "M".equals(platformMqTopic.getIS_DISTINCT())) {
            Constants.distinctTopicTagList.add(platformMqTopic.getTOPIC() + "#" + platformMqTopic.getTAG());
        }

        if (StringUtil.isAllNotNullOrBlank(new String[]{platformMqTopic.getWETHER_ORDER_MESS()}) && "Y".equals(platformMqTopic.getWETHER_ORDER_MESS())) {
            Constants.orderTopicTagList.add(platformMqTopic.getTOPIC() + "#" + platformMqTopic.getTAG());
        }

        if (StringUtil.isAllNotNullOrBlank(new String[]{platformMqTopic.getWETHER_INSERTDB()}) && "N".equals(platformMqTopic.getWETHER_INSERTDB())) {
            Constants.notInsertDbTopicTagList.add(platformMqTopic.getTOPIC() + "#" + platformMqTopic.getTAG());
        }

        if (StringUtil.isAllNotNullOrBlank(new Object[]{platformMqTopic.getCONSUMER_INTERVAL()}) && platformMqTopic.getCONSUMER_INTERVAL() > 0L) {
            Constants.topicTagToConsumerInterval.put(platformMqTopic.getTOPIC() + "#" + platformMqTopic.getTAG(), platformMqTopic.getCONSUMER_INTERVAL().intValue());
        }

    }
}


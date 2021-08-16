package com.github.bluecatlee.gs4d.message.consumer.service.impl;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.github.bluecatlee.gs4d.common.utils.StringUtil;
import com.github.bluecatlee.gs4d.message.consumer.dao.PlatformMqDubboConsumerDao;
import com.github.bluecatlee.gs4d.message.consumer.model.PlatformMqDubboConsumer;
import com.github.bluecatlee.gs4d.message.consumer.service.MqDubboConsumerService;
import com.github.bluecatlee.gs4d.message.consumer.utils.Constants;
import com.github.bluecatlee.gs4d.message.consumer.utils.SeqUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service("mqDubboConsumerService")
public class MqDubboConsumerServiceImpl implements MqDubboConsumerService {
    
    private static Logger logger = LoggerFactory.getLogger(MqDubboConsumerServiceImpl.class);
    
    @Value("#{settings['initalConsumerFlag']}")
    private String initalConsumerFlag;
    @Value("#{settings['dataSign']}")
    private Integer dataSign;
    @Value("#{settings['zookeeper.host.port']}")
    private String zookeeperHostPort;
    
    @Autowired
    private PlatformMqDubboConsumerDao platformMqDubboConsumerDao;

    public void initalAllDubboMethod() {
        try {
            logger.info("初始化dubbo消费者开始");
            List dubboConsumers = this.platformMqDubboConsumerDao.queryAllDubboConsumers();
            Iterator iterator = dubboConsumers.iterator();

            while(iterator.hasNext()) {
                PlatformMqDubboConsumer platformMqDubboConsumer = (PlatformMqDubboConsumer)iterator.next();
                this.initalDubboMethodCore(platformMqDubboConsumer);
            }

            logger.info("初始化dubbo消费者成功");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public void initalDubboMethodCore(PlatformMqDubboConsumer platformMqDubboConsumer) throws Exception {
        logger.info("初始化dubbo开始dubbo序号" + platformMqDubboConsumer.getSERIES() + "方法" + platformMqDubboConsumer.getSERVICE_NAME() + "方法" + platformMqDubboConsumer.getMETHOD_NAME());
        ReferenceConfig referenceConfig = new ReferenceConfig();
        if (this.getDubboConsumerServiceClass(platformMqDubboConsumer) != null) {
            logger.info("初始化dubbo反射");
            referenceConfig.setInterface(this.getDubboConsumerServiceClass(platformMqDubboConsumer));
            if (SeqUtil.test == this.dataSign && StringUtil.isAllNotNullOrBlank(new String[]{platformMqDubboConsumer.getSERVICE_NAME()})) {
                if (Constants.serviceNameDubboGroup2consumerServiceProxy_Test.get(platformMqDubboConsumer.getSERVICE_NAME() + "_" + platformMqDubboConsumer.getDUBBO_GROUP()) != null && !StringUtil.isAllNotNullOrBlank(new String[]{platformMqDubboConsumer.getDUBBO_GROUP()})) {
                    return;
                }
            } else if (SeqUtil.develop == this.dataSign && StringUtil.isAllNotNullOrBlank(new String[]{platformMqDubboConsumer.getSERVICE_NAME()})) {
                if (Constants.serviceNameDubboGroup2consumerServiceProxy_Devepop.get(platformMqDubboConsumer.getSERVICE_NAME() + "_" + platformMqDubboConsumer.getDUBBO_GROUP()) != null && !StringUtil.isAllNotNullOrBlank(new String[]{platformMqDubboConsumer.getDUBBO_GROUP()})) {
                    return;
                }
            } else {
                if (SeqUtil.prod != this.dataSign || !StringUtil.isAllNotNullOrBlank(new String[]{platformMqDubboConsumer.getSERVICE_NAME()})) {
                    return;
                }

                if (Constants.serviceNameDubboGroup2consumerServiceProxy.get(platformMqDubboConsumer.getSERVICE_NAME() + "_" + platformMqDubboConsumer.getDUBBO_GROUP()) != null && !StringUtil.isAllNotNullOrBlank(new String[]{platformMqDubboConsumer.getSERVICE_NAME()}) && !StringUtil.isAllNotNullOrBlank(new String[]{platformMqDubboConsumer.getDUBBO_GROUP()})) {
                    return;
                }
            }

            logger.info("初始化dubbomap");
            ApplicationConfig applicationConfig = new ApplicationConfig();
            applicationConfig.setName(Constants.SUB_SYSTEM);
            ArrayList registries = new ArrayList();
            String[] zkAddrs = this.zookeeperHostPort.split(",");

            for(int i = 0; i < zkAddrs.length; ++i) {
                RegistryConfig registryConfig = new RegistryConfig();
                registryConfig.setAddress("zookeeper://" + zkAddrs[i]);
                registries.add(registryConfig);
            }

            logger.info("初始化dubbozookeeper");
            referenceConfig.setApplication(applicationConfig);
            referenceConfig.setRegistries(registries);
            if (StringUtil.isAllNotNullOrBlank(new String[]{platformMqDubboConsumer.getDUBBO_GROUP()})) {
                referenceConfig.setGroup(platformMqDubboConsumer.getDUBBO_GROUP());
            }

            if (StringUtil.isAllNotNullOrBlank(new String[]{platformMqDubboConsumer.getDIRECT_ADR()})) {
                referenceConfig.setUrl("dubbo://" + platformMqDubboConsumer.getDIRECT_ADR());
            }

            referenceConfig.setCheck(false);
            if (StringUtils.isNotBlank(platformMqDubboConsumer.getVERSION())) {
                referenceConfig.setVersion(platformMqDubboConsumer.getVERSION());
            }

            referenceConfig.setRetries(0);
            logger.info("初始化dubbo实例前");

            try {
                Object reference = referenceConfig.get();
                logger.info("初始化dubbo实例后");
                if (SeqUtil.test == this.dataSign) {
                    Constants.serviceNameDubboGroup2consumerServiceProxy_Test.put(platformMqDubboConsumer.getSERVICE_NAME() + "_" + platformMqDubboConsumer.getDUBBO_GROUP(), reference);
                } else if (SeqUtil.develop == this.dataSign) {
                    Constants.serviceNameDubboGroup2consumerServiceProxy_Devepop.put(platformMqDubboConsumer.getSERVICE_NAME() + "_" + platformMqDubboConsumer.getDUBBO_GROUP(), reference);
                } else if (SeqUtil.prod == this.dataSign) {
                    Constants.serviceNameDubboGroup2consumerServiceProxy.put(platformMqDubboConsumer.getSERVICE_NAME() + "_" + platformMqDubboConsumer.getDUBBO_GROUP(), reference);
                }
            } catch (NoClassDefFoundError e) {
                logger.error(e.getMessage(), e);
                logger.error("初始化dubbo失败,原因:" + e.getMessage());
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                logger.error("初始化dubbo出现错误,原因:" + e.getMessage());
            }

            logger.info("初始化dubbo结束");
        }
    }

    private Class<?> getDubboConsumerServiceClass(PlatformMqDubboConsumer platformMqDubboConsumer) {
        Class clazz = null;

        try {
            clazz = Class.forName(platformMqDubboConsumer.getSERVICE_NAME());
        } catch (ClassNotFoundException e) {
            logger.error(e.getMessage());
        }

        return clazz;
    }

    public void initalSpecialDubboMethod(Long series) {
        PlatformMqDubboConsumer platformMqDubboConsumer = this.platformMqDubboConsumerDao.queryBySeries(series);
        ReferenceConfig referenceConfig = new ReferenceConfig();
        if (this.getDubboConsumerServiceClass(platformMqDubboConsumer) != null) {
            referenceConfig.setInterface(this.getDubboConsumerServiceClass(platformMqDubboConsumer));
            if (StringUtil.isAllNotNullOrBlank(new String[]{platformMqDubboConsumer.getSERVICE_NAME()})) {
                if (Constants.serviceNameDubboGroup2consumerServiceProxy_Test.get(platformMqDubboConsumer.getSERVICE_NAME() + "_" + platformMqDubboConsumer.getDUBBO_GROUP() + "_test") != null && !StringUtil.isAllNotNullOrBlank(new String[]{platformMqDubboConsumer.getDUBBO_GROUP()})) {
                    return;
                }

                ApplicationConfig applicationConfig = new ApplicationConfig();
                applicationConfig.setName(Constants.SUB_SYSTEM);
                ArrayList registries = new ArrayList();
                String[] zkAddrs = this.zookeeperHostPort.split(",");

                for(int i = 0; i < zkAddrs.length; ++i) {
                    RegistryConfig registryConfig = new RegistryConfig();
                    registryConfig.setAddress("zookeeper://" + zkAddrs[i]);
                    registries.add(referenceConfig);
                }

                referenceConfig.setApplication(applicationConfig);
                referenceConfig.setRegistries(registries);
                if (StringUtil.isAllNotNullOrBlank(new String[]{platformMqDubboConsumer.getDUBBO_GROUP()})) {
                    referenceConfig.setGroup(platformMqDubboConsumer.getDUBBO_GROUP());
                }

                if (StringUtil.isAllNotNullOrBlank(new String[]{platformMqDubboConsumer.getDIRECT_ADR()})) {
                    referenceConfig.setUrl("dubbo://" + platformMqDubboConsumer.getDIRECT_ADR());
                }

                referenceConfig.setCheck(false);
                if (StringUtils.isNotBlank(platformMqDubboConsumer.getVERSION())) {
                    referenceConfig.setVersion(platformMqDubboConsumer.getVERSION());
                }

                referenceConfig.setRetries(0);
                Object reference = referenceConfig.get();
                Constants.serviceNameDubboGroup2consumerServiceProxy_Test.put(platformMqDubboConsumer.getSERVICE_NAME() + "_" + platformMqDubboConsumer.getDUBBO_GROUP() + "_test", reference);
            }

        }
    }
}


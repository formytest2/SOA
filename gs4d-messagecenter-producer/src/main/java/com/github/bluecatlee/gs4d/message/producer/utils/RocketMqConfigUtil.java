package com.github.bluecatlee.gs4d.message.producer.utils;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@Lazy(false)
public class RocketMqConfigUtil extends AbstractRocketMqUtil {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Value("${mqNameServAddrMessOnly}")
    private String mqNameServAddrMessOnly;
    private DefaultMQPushConsumer dw;

    @Value("${aliyunMqAccessKey}")
    private String aliyunMqAccessKey;
    @Value("${aliyunMqSecretKey}")
    private String aliyunMqSecretKey;
    @Value("${aliyunMqUrl}")
    private String aliyunMqUrl;
    @Value("${defaultNamesrv}")
    private String defaultNamesrv;

    protected void loadRocketMqConfig() throws Exception {
        Object[] nameSrvs = this.getNameSrvs().toArray();
        for(int i = 0; i < nameSrvs.length; ++i) {
            nameSrvList.add((String)nameSrvs[i]);
        }

        Object[] consumerIps = this.getConsumerIps().toArray();
        for(int i = 0; i < consumerIps.length; ++i) {
            Constants.consumerIpList.add((String)consumerIps[i]);
        }

        Object[] cipKeys = this.getConsumerIpsKey().toArray();
        for(int i = 0; i < cipKeys.length; ++i) {
            Constants.consumerIpKeyList.add((String)cipKeys[i]);
        }

        AbstractRocketMqUtil.mqNameServAddrMessOnly = this.mqNameServAddrMessOnly;
        AbstractRocketMqUtil.defaultNamesrv = this.defaultNamesrv;
        AbstractRocketMqUtil.producerGroupOnly = "message_center_only";
        AbstractRocketMqUtil.producerGroup = "message_center";
    }

    protected void loadAliyunMqConfig() {
        AbstractRocketMqUtil.aliyunMqUrl = this.aliyunMqUrl;
        AbstractRocketMqUtil.aliyunMqAccessKey = this.aliyunMqAccessKey;
        AbstractRocketMqUtil.aliyunMqSecretKey = this.aliyunMqSecretKey;
        AbstractRocketMqUtil.producerGroup = "PID_GB_002";
    }

}


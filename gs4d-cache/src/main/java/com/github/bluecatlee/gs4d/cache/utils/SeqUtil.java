package com.github.bluecatlee.gs4d.cache.utils;

import com.github.bluecatlee.gs4d.cache.constant.Constants;
import com.github.bluecatlee.gs4d.common.utils.MyJdbcTemplate;
import com.github.bluecatlee.gs4d.monitor.utils.UniversalLiveDetectUtil;
import com.github.bluecatlee.gs4d.sequence.utils.SeqGetUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Component
@Lazy(false)
@Scope("singleton")
public class SeqUtil {

    @Value("${dubbo.zookeeper.host.port}")
    private String dubboZookeeperHostPort;
    @Value("${dubbo.service.port}")
    private Integer dubboServicePort;
    @Value("${rpc.invoke-type}")
    private String rpcInvokeType;

    @Resource(name = "commonJdbcTemplate")
    private MyJdbcTemplate jdbcTemplate;

    public SeqUtil() {
    }

    @PostConstruct
    public void post() {
        if (!"hsf".equals(this.rpcInvokeType)) {
            // 注册一个检测服务(这个检测服务本身是用来检测数据库连接是否正常的)
            // 把当前服务所需的jdbcTemplate传入 可以通过该检测服务的响应判断这个jdbcTemplate是否正常 从而可以判断当前服务是否正常 (当然这种检测只是数据库层面的 理应有其他检测点)
            // 可以用spring actuactor
            UniversalLiveDetectUtil.initSystemLiveDetect(this.dubboZookeeperHostPort, Constants.SUB_SYSTEM, this.jdbcTemplate, this.dubboServicePort, "mysql");
        }
        SeqGetUtil.initeZkConfig(this.dubboZookeeperHostPort);
    }

    public static String getSequence(String seqName, String routeId) {
        return SeqGetUtil.getSequence(seqName, routeId);
    }

    public static String getSequence(String seqName) {
        return String.valueOf(SeqGetUtil.getNoSubSequence(Constants.SUB_SYSTEM, seqName));
    }

}


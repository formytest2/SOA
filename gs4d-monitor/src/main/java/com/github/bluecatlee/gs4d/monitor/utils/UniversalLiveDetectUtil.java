package com.github.bluecatlee.gs4d.monitor.utils;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.ServiceConfig;
import com.github.bluecatlee.gs4d.common.exception.ExceptionType;
import com.github.bluecatlee.gs4d.common.exception.ValidateBusinessException;
import com.github.bluecatlee.gs4d.common.utils.StringUtil;
import com.github.bluecatlee.gs4d.monitor.api.service.UniversalLiveDetectService;
import com.github.bluecatlee.gs4d.monitor.service.impl.UniversalLiveDetectServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Lazy(false)
public class UniversalLiveDetectUtil {

    public static JdbcTemplate jdbcTemplate;

    public static String dbType;

    public static void initSystemLiveDetect(String zkAddress, String subSystem, Integer dubboPort) {
        initSystemLiveDetect(zkAddress, subSystem, null, dubboPort, "none");
    }

    public static void initSystemLiveDetect(String zkAddress, String subSystem, JdbcTemplate jdbcTemplateClient, Integer dubboPort, String dbTypeParam) {
        if (StringUtil.isAllNullOrBlank(new String[] {zkAddress})) {
            throw new ValidateBusinessException("monitor", ExceptionType.VBE20007, "zk地址不能为空");
        }
        if (StringUtil.isAllNullOrBlank(new String[] {subSystem})) {
            throw new ValidateBusinessException("monitor", ExceptionType.VBE20007, "subSystem不能为空");
        }
        if (StringUtil.isAllNullOrBlank(new String[] {dbTypeParam})) {
            throw new ValidateBusinessException("monitor", ExceptionType.VBE20007, "dbType不能为空");
        }
        if (!"none".equals(dbTypeParam) && jdbcTemplateClient == null) {
            throw new ValidateBusinessException("monitor", ExceptionType.VBE20007, "dbType不为none,jdbcTemplate实例不能为空");
        }
        if (!"mysql".equals(dbTypeParam) && !"oracle".equals(dbTypeParam) && !"none".equals(dbTypeParam)) {
            throw new ValidateBusinessException("monitor", ExceptionType.VBE20007, "不支持的数据库类别:"+ dbTypeParam);
        }

        jdbcTemplate = jdbcTemplateClient;
        dbType = dbTypeParam;
        UniversalLiveDetectServiceImpl universalLiveDetectService = new UniversalLiveDetectServiceImpl();
        ApplicationConfig application = new ApplicationConfig();
        application.setName(subSystem.substring(1));
        List<RegistryConfig> config = new ArrayList<>();
        String[] zkAdd = zkAddress.split(",");
        for (int i = 0; i < zkAdd.length; i++) {
            RegistryConfig registry = new RegistryConfig();
            registry.setAddress("zookeeper://" + zkAdd[i]);
            config.add(registry);
        }
        ProtocolConfig protocol = new ProtocolConfig();
        protocol.setName("dubbo");
        protocol.setPort(dubboPort);
        ServiceConfig<UniversalLiveDetectService> service = new ServiceConfig();
        service.setApplication(application);
        service.setRegistries(config);
        service.setProtocol(protocol);
        service.setGroup(subSystem + "LiveDetectGroup");
        service.setInterface(UniversalLiveDetectService.class);
        service.setRef(universalLiveDetectService);
        service.setTimeout(Integer.valueOf(2000));
        service.setRetries(Integer.valueOf(0));
        service.export();
    }
}


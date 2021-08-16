package com.github.bluecatlee.gs4d.sequence.utils;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.github.bluecatlee.gs4d.sequence.service.SequenceActionService;

import java.util.ArrayList;
import java.util.List;

public class DubboImpl {

    public DubboImpl() {
    }

    public static Object initDubbo(String zkaddress) {
        ReferenceConfig reference = new ReferenceConfig();
        reference.setInterface(SequenceActionService.class);
        ApplicationConfig application = new ApplicationConfig();
        application.setName("sequenceClient");
        List<RegistryConfig> config = new ArrayList();
        String[] zkAdd = zkaddress.split(",");

        for(int i = 0; i < zkAdd.length; ++i) {
            RegistryConfig registry = new RegistryConfig();
            registry.setAddress("zookeeper://" + zkAdd[i]);
            config.add(registry);
        }

        reference.setApplication(application);
        reference.setRegistries(config);
        reference.setCheck(false);
        reference.setRetries(0);
        SequenceActionService serviceAction = (SequenceActionService)reference.get();
        return serviceAction;
    }
}


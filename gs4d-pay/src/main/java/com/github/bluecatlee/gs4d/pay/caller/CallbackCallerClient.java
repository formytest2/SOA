package com.github.bluecatlee.gs4d.pay.caller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CallbackCallerClient implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public static CallbackCaller getCaller(String businessCallback, String channel) {
        log.info("callback method={}", businessCallback);
        // TODO 业务回调调用控制
        CallbackCaller callbackCaller = null;
        if(businessCallback.startsWith("http")){ // 回调走http
            callbackCaller =  context.getBean(GateWayCaller.class);
        } else {
            callbackCaller = context.getBean(DubboCaller.class);
        }

        return callbackCaller;
    }

}

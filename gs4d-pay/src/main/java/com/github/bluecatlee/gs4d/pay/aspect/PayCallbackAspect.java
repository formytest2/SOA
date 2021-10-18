package com.github.bluecatlee.gs4d.pay.aspect;

import com.github.bluecatlee.gs4d.pay.annotation.Callback;
import com.github.bluecatlee.gs4d.pay.constant.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.Method;
import java.net.URI;

/**
 * 异步回调拦截
 *
 *      注意 针对回调controller接口的切面 必须设置成非懒加载的bean 否则项目重启后 第一次回调进来后才会创建该切面 导致回调处理时间延长 以至于第二次回调又进来了
 *      当然业务代码必须保证能够幂等处理重复回调
 *
 * @Author Bluecatlee
 * @Date 2021/10/12 16:53
 */
@ControllerAdvice
@Slf4j
@Lazy(false)
public class PayCallbackAspect implements ResponseBodyAdvice {


    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        Method method = methodParameter.getMethod();
//        Class<?> declaringClass = method.getDeclaringClass();
//        if (declaringClass.equals(PayController.class)) {
//            String name = method.getName();
//            if ("afterpay".equals(name)) {
//                return true;
//            }
//        }
        Callback annotation = method.getAnnotation(Callback.class); // 使用自定义注解@Callback在PayController的afterpay方法上 避免使用类名和方法名来筛选
        if (annotation != null) {
            return true;
        }

        return false;
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        String platType = "";
        try {
            URI uri = serverHttpRequest.getURI();
            String path = uri.getPath(); //  /paymentplatform/pay/api/wxcallback/{platType}/{tradeType}
            String substring = path.substring(0, path.lastIndexOf("/"));
            platType = substring.substring(substring.lastIndexOf("/") + 1);
        } catch (Exception e) {
            log.error("PayCallbackAspect获取通用回调入口的platType参数失败...");
            return o;
        }
        if (Constants.CCB_PAY_TYPE.equals(platType) || Constants.DCEP_PAY_TYPE.equals(platType)) {
            if (o instanceof String) {
                String result = (String)o;
                if (!result.equals("success")) {
                    serverHttpResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
        }

        return o;
    }

}

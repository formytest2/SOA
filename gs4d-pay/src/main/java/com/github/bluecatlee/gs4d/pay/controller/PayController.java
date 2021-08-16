package com.github.bluecatlee.gs4d.pay.controller;

import com.alibaba.fastjson.JSON;
import com.github.bluecatlee.gs4d.common.utils.id.IdGenerator;
import com.github.bluecatlee.gs4d.pay.bean.BaseRequest;
import com.github.bluecatlee.gs4d.pay.bean.BaseResponse;
import com.github.bluecatlee.gs4d.pay.constant.RequestMethodEnum;
import com.github.bluecatlee.gs4d.pay.service.BusinessCallbackService;
import com.github.bluecatlee.gs4d.pay.service.impl.PayServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/pay/api")
public class PayController {

    @Autowired
    private PayServiceImpl payService;

    @Autowired
    BusinessCallbackService businessCallbackService;

    /**
     * 通用的异步回调入口
     * @param platType
     * @param tradeType
     * @param body
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/callback/{platType}/{tradeType}", method = {RequestMethod.POST, RequestMethod.GET})
    public String callback(@PathVariable String platType, @PathVariable String tradeType, @RequestBody(required = false) String body, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.info("支付后服务器回调 wxcallback platType = {}, tradeType = {}, body = {}", platType, tradeType ,body);
        Map<String, String[]> parameterMap = request.getParameterMap();
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String s = parameterNames.nextElement();
            String[] strings = parameterMap.get(s);
            log.info("{}:{}", s, strings);
        }

        BaseRequest baseRequest = new BaseRequest();
        baseRequest.setBody(body);
        baseRequest.setTradeType(tradeType);
        baseRequest.setRequestParam(parameterMap);
        baseRequest.setPlatType(platType);
        String PLACEHOLDER = Integer.MIN_VALUE + "";
        baseRequest.setSubUnitNumId(PLACEHOLDER);//第三方平台不知道我们的门店 ，在具体通知方法里修正
        baseRequest.setOutTradeNo(PLACEHOLDER);  //支付订单需要解密 ，在具体通知方法里修正
        baseRequest.setChannel(platType);  //第三方平台不知道我们的渠道 ，在具体通知方法里修正
        baseRequest.setRequestNo(IdGenerator.nextId() + "");
        String pay = payService.pay(JSON.toJSONString(baseRequest), RequestMethodEnum.NOTIFY.getMethod());
        String resBody = JSON.parseObject(pay, BaseResponse.class).getResBody();

        return resBody;
    }

    @RequestMapping("/recall")
    public Object recall(String outTradeNo){
        return businessCallbackService.queryNeedCallBackTask(outTradeNo);
    }

}




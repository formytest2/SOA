package com.github.bluecatlee.gs4d.pay.service.impl;

import com.github.bluecatlee.gs4d.pay.bean.BaseResponse;
import com.github.bluecatlee.gs4d.pay.caller.CallbackCaller;
import com.github.bluecatlee.gs4d.pay.caller.CallbackCallerClient;
import com.github.bluecatlee.gs4d.pay.constant.CallbackStatusEnum;
import com.github.bluecatlee.gs4d.pay.constant.RespEnum;
import com.github.bluecatlee.gs4d.pay.constant.TradeStatusEnum;
import com.github.bluecatlee.gs4d.pay.entity.PayOrderInfo;
import com.github.bluecatlee.gs4d.pay.mapper.PayOrderInfoMapper;
import com.github.bluecatlee.gs4d.pay.service.BusinessCallbackService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 业务回调服务实现
 */
@Service
@Slf4j
public class BusinessCallbackServiceImpl implements BusinessCallbackService {

    @Autowired
    private PayOrderInfoMapper payOrderInfoMapper;
    
//    private static final Byte cancel_sign = 0;
    
    private static final Byte BUSINESS_CALLBACK_DONE = 1;

    /**
     * 重试间隔时间
     */
    private final static int [] intervals = {50, 15*1000, 15*1000,
            30*1000, 3*60*1000, 10*60*1000, 20*60*1000, 30*60*1000, 30*60*1000, 30*60*1000,
            60*60*1000, 3*60*60*1000, 3*60*60*1000, 3*60*60*1000, 6*60*60*1000, 6*60*60*1000};

    @Override
    @Async
    public void businessCallBack(String outTradeNo) {
        log.info("business callback");
        PayOrderInfo param = new PayOrderInfo().setOutTradeNo(outTradeNo);
        List<PayOrderInfo> select = payOrderInfoMapper.select(param);
        assert !select.isEmpty() && select.size() == 1;
        PayOrderInfo payOrderInfo = select.get(0);

        if (payOrderInfo.getBusinessCallbackStatus() == null) {
            return;
        }

        // 方法调用者必须确保支付已经成功

        int callBackStatus = Integer.parseInt(String.valueOf(payOrderInfo.getBusinessCallbackStatus()));
        if (callBackStatus > 0) {
            return;
        }

        String businessCallback = payOrderInfo.getBusinessCallback();
        try {
            CallbackCaller caller = CallbackCallerClient.getCaller(businessCallback, payOrderInfo.getChannel());
        	
        	for(int i = 0 ; i < intervals.length ; i++) {
                Thread.sleep(intervals[i]);
            	BaseResponse callResponse = caller.call(payOrderInfo, businessCallback);
                log.info(" Thread id = {} , 第 {} 次执行, outTradeNo = {}  , time = {} call back result:{}", Thread.currentThread().getId(), i + 1 ,outTradeNo, LocalDateTime.now(), callResponse);
                if (RespEnum.SUCCESS.getStatus().equals(callResponse.getCode())) {
                    PayOrderInfo result = new PayOrderInfo();
                    result.setSeries(payOrderInfo.getSeries());
                    result.setBusinessCallbackStatus(BUSINESS_CALLBACK_DONE);   // CallbackStatusEnum.SUCCESS
                    payOrderInfoMapper.updateByPrimaryKeySelective(result);
                    break;
                }
        	}
        	            		
        } catch (Exception e) {
            log.error("businessCallBack outTradeNo = {} ", e);
        }
    }
    

    @Override
    public Object queryNeedCallBackTask(String outTradeNos) {
        log.info("business callback task list query");
        List<String> collect;
        if (StringUtils.isEmpty(outTradeNos)) {
            collect = queryOrderNeedCallback();
        } else {
            collect = Arrays.asList(outTradeNos);
        }

        for (String payOrderInfo : collect) {
            businessCallBack(payOrderInfo);
        }
        return null;
    }

    private List<String> queryOrderNeedCallback() {
        Example example = Example.builder(PayOrderInfo.class)
                .select(PayOrderInfo.OUT_TRADE_NO)
                .andWhere(Sqls.custom()
                        .andEqualTo(PayOrderInfo.TRADE_STATUS, TradeStatusEnum.SUCCESS.getStatus())
                        .andLessThanOrEqualTo(PayOrderInfo.BUSINESS_CALLBACK_STATUS, CallbackStatusEnum.NEW.getStatus()))
                .build();
        List<PayOrderInfo> payOrderInfos = payOrderInfoMapper.selectByExampleAndRowBounds(example, new RowBounds(0, 100));
        return payOrderInfos.stream().map(x -> x.getOutTradeNo()).collect(Collectors.toList());
    }
}

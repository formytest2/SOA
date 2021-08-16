package com.github.bluecatlee.gs4d.pay.service.impl;

import com.github.bluecatlee.gs4d.pay.bean.BaseResponse;
import com.github.bluecatlee.gs4d.pay.constant.RespEnum;
import com.github.bluecatlee.gs4d.pay.constant.TradeStatusEnum;
import com.github.bluecatlee.gs4d.pay.entity.PayOrderInfo;
import com.github.bluecatlee.gs4d.pay.mapper.PayOrderInfoMapper;
import com.github.bluecatlee.gs4d.pay.service.BusinessCallbackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

@Service
@Slf4j
public class CallBackService {

    @Autowired
    PayOrderInfoMapper payOrderInfoMapper;

    @Autowired
    BusinessCallbackService businessCallbackService;

    public void doAfterNotify(BaseResponse res) {
        if (res.getCode().equals(RespEnum.SUCCESS.getStatus())) {
            markOrderAsPaySuccess(res.getOutTradeNo(), res.getTransactionId(), res.getTotalFee());
        }
    }

    private void markOrderAsPaySuccess(String outTradeNo, String trans, Double totalFee) {
        log.info("markOrderAsPaySuccess ,outTradeNo = [" + outTradeNo + "], trans = [" + trans + "], totalFee = [" + totalFee + "]");
        PayOrderInfo t = new PayOrderInfo();
        t.setTradeStatus(TradeStatusEnum.SUCCESS.getStatus());
        t.setTotalFee(totalFee);
        t.setTransactionId(trans);
        t.setOutTradeNo(outTradeNo);
        Example example = Example.builder(PayOrderInfo.class).andWhere(Sqls.custom().andEqualTo(PayOrderInfo.OUT_TRADE_NO, outTradeNo)).build();
        int i = payOrderInfoMapper.updateByExampleSelective(t, example);
        if (i != 1) {
            log.error("update trade status error {}", outTradeNo, TradeStatusEnum.SUCCESS);
            return;
        }
        businessCallbackService.businessCallBack(outTradeNo);
    }

}

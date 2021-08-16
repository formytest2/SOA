package com.github.bluecatlee.gs4d.pay.mapper;

import com.github.bluecatlee.gs4d.pay.entity.PayOrderInfo;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface PayOrderInfoMapper extends Mapper<PayOrderInfo> {

    PayOrderInfo selectByPlatTypeAndOutTradeNo(PayOrderInfo record);

    List<PayOrderInfo> selectProcessingData();

    int updateTradeStatusByPrimaryKey(PayOrderInfo record);

    PayOrderInfo selectPayInfoByOutTradeNo(PayOrderInfo record);

}
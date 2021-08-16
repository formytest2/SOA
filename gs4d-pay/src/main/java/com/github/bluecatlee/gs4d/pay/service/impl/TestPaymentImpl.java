package com.github.bluecatlee.gs4d.pay.service.impl;

import com.github.bluecatlee.gs4d.common.utils.id.IdGenerator;
import com.github.bluecatlee.gs4d.pay.bean.BaseRequest;
import com.github.bluecatlee.gs4d.pay.bean.BaseResponse;
import com.github.bluecatlee.gs4d.pay.bean.PrepaidPayRequest;
import com.github.bluecatlee.gs4d.pay.bean.PrepaidPayResponse;
import com.github.bluecatlee.gs4d.pay.constant.RespEnum;
import com.github.bluecatlee.gs4d.pay.constant.TradeStatusEnum;
import com.github.bluecatlee.gs4d.pay.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Random;

@Component
@Slf4j
public class TestPaymentImpl implements PaymentService<PrepaidPayRequest, PrepaidPayResponse> {

	private Random r =  new Random();

    @Override
    public void checkInputParams(PrepaidPayRequest request, PrepaidPayResponse res, String requestMethod) {
        log.info("----测试入参:" + request.toString());
    }

    @Override
    public PrepaidPayResponse pay(PrepaidPayRequest request, PrepaidPayResponse res) throws Exception {
        log.info("----测试入参:" + request.toString());


        res.setPrepaidPayId("xxxxx");

//        SUCCESS("1", "成功"),
//                TIMEOUT("2","超时"),
//                PROCESSING("11","处理中"),
//                FAIL("80","处理失败"),
//                ERROR("99","系统错误");
//        res.setTradeStatus("1");

        res.setCode(RespEnum.SUCCESS.getStatus());
        res.setMessage(RespEnum.SUCCESS.getMsg());
        res.setTradeStatus(TradeStatusEnum.PROCESSING.getStatus());


        LocalDateTime currentTime = LocalDateTime.now();

        res.setMerchantId(99999l); // 商户号
        res.setSubUnitNumId(Long.valueOf(request.getSubUnitNumId())); // 门店号
        res.setPlatType(Integer.valueOf(request.getPlatType())); // 支付方式
        res.setTransactionId(String.valueOf(IdGenerator.nextId())); // 第三方支付订单流水号
        res.setTotalFee(Double.valueOf(request.getTotalFee()));
        res.setTxndate(currentTime.format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        res.setTxntime(currentTime.format(DateTimeFormatter.ofPattern("HHmmss")));
        res.setTradeType(Byte.valueOf(request.getTradeType()));
        res.setOutTradeNo(request.getOutTradeNo());
        
        return res;
    }

    @Override
    public Map afterPay(PrepaidPayRequest req, String res, long id) throws Exception {
        return null;
    }


    @Override
    public PrepaidPayResponse refund(PrepaidPayRequest request, PrepaidPayResponse res) throws Exception {
        res.setPrepaidPayId("yyyyyy");

        res.setTradeStatus("1");

        res.setCode(RespEnum.SUCCESS.getStatus());
        res.setMessage(RespEnum.SUCCESS.getMsg());
        res.setTradeStatus(TradeStatusEnum.SUCCESS.getStatus());


        LocalDateTime currentTime = LocalDateTime.now();

        res.setMerchantId(99999l); // 商户号
        res.setSubUnitNumId(100042l); // 门店号
        res.setPlatType(0); // 支付方式
        res.setTransactionId(String.valueOf(IdGenerator.nextId())); // 第三方支付订单流水号
        res.setTotalFee((double)r.nextInt(100));
        res.setTxndate(currentTime.format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        res.setTxntime(currentTime.format(DateTimeFormatter.ofPattern("HHmmss")));
        return res;
    }

    @Override
    public Map afterRefund(PrepaidPayRequest req, String res, long id) throws Exception {
        return null;
    }


    @Override
    public PrepaidPayResponse queryPayResult(PrepaidPayRequest request, PrepaidPayResponse res) {

        res.setPrepaidPayId("uuuuuuuuuuuu");

        res.setCode(RespEnum.SUCCESS.getStatus());
        res.setMessage(RespEnum.SUCCESS.getMsg());

        return res;
    }

    @Override
    public BaseResponse queryRefundResult(PrepaidPayRequest req, PrepaidPayResponse res) throws Exception {
        res.setPrepaidPayId("rtereeee");

        res.setCode(RespEnum.SUCCESS.getStatus());
        res.setMessage(RespEnum.SUCCESS.getMsg());

        return res;
    }
    
    @Override
	public BaseResponse callbackNotify(BaseRequest request, BaseResponse response) throws Exception {
		response.setCode(RespEnum.SUCCESS.getStatus());
		response.setMessage(RespEnum.SUCCESS.getMsg());
		response.setOutTradeNo(request.getOutTradeNo());
		response.setTradeStatus(TradeStatusEnum.SUCCESS.getStatus());
		response.setTransactionId(request.getTransactionId());
		response.setTotalFee(Double.valueOf(request.getTotalFee()));
    	response.setResBody("success");
		return response;
	}
}

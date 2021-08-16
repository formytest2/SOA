package com.github.bluecatlee.gs4d.pay.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.github.bluecatlee.gs4d.common.bean.AbstractRequest;
import lombok.Data;

@Data
@Deprecated
public class SendVo extends AbstractRequest {

    @JSONField(name = "transaction_id")
    private String transactionId;//订单流水号:

    @JSONField(name = "tml_num_id")
    private String tmlNumId;//订单号

    @JSONField(name = "total_fee")
    private Double totalFee;//支付金额
    
    private String tradeType;
    
    private String outTradeNo; // 支付流水号
    
    private String code;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Double getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Double totalFee) {
        this.totalFee = totalFee;
    }

    public String getTmlNumId() {
        return tmlNumId;
    }

    public void setTmlNumId(String tmlNumId) {
        this.tmlNumId = tmlNumId;
    }

}

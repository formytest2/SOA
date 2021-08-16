package com.github.bluecatlee.gs4d.pay.exception;

import com.github.bluecatlee.gs4d.pay.constant.RespEnum;
import lombok.Getter;

@Getter
public class BizException extends BaseServiceException {

    private String status;
    private String statusCode;
    private String outTradeNo;
    private String transactionId;


    public BizException(RespEnum resp) {
        super(resp.getMsg());
        this.errorCode = resp.getStatus();
        this.status = resp.getMsg();
        this.statusCode = resp.getStatus();
    }

    public BizException(String statusCode, String status) {
        super(status);
        this.status = status;
        this.statusCode = statusCode;
    }

    public BizException(String status, String statusCode, String outTradeNo, String transactionId) {
        this.status = status;
        this.statusCode = statusCode;
        this.outTradeNo = outTradeNo;
        this.transactionId = transactionId;
    }

    @Override
    public String toString() {
        return "BussinessException{" +
                "status='" + status + '\'' +
                ", statusCode='" + statusCode + '\'' +
                ", outTradeNo='" + outTradeNo + '\'' +
                ", transactionId='" + transactionId + '\'' +
                '}';
    }
}

package com.github.bluecatlee.gs4d.pay.bean;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@ToString(callSuper = true)
public class PrepaidPayRequest extends BaseRequest {

	private String method;
    private String authCode;
    private String subject;
    private String body;
    // 金额
    private String notifyUrl;
    private Long ztTenantNumId;
    private String deviceInfo = null;
    private List<GoodsDetailModel> goodsDetail;
    private String zimmetainfo;
    private String scene;
    private String terminalParams;
    private String auth_confirm_mode;
    private String terminalId;

    @Data
    public class GoodsDetailModel {
        private long goods_id;
        private String goods_name;
        private long quantity;
        private double price;
        private String goods_category;
        private String body;
        private String show_url;
    }


}

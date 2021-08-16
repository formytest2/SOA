package com.github.bluecatlee.gs4d.pay.bean;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@ToString(callSuper = true)
@Accessors(chain = true)
public class PrepaidPayResponse extends BaseResponse {

    private String prepaidPayId;

}

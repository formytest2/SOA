package com.github.bluecatlee.gs4d.message.api.request;

import com.github.bluecatlee.gs4d.common.bean.AbstractRequest;

import javax.validation.constraints.NotEmpty;

public class DubboConfigByKeyRequest extends AbstractRequest {
    private static final long serialVersionUID = 1L;
    @NotEmpty(message = "consumerKey不能为空")
    private String consumerKey;
    private Long pageNo;
    private Long pageCount;

    public String getConsumerKey() {
        return this.consumerKey;
    }

    public void setConsumerKey(String consumerKey) {
        this.consumerKey = consumerKey;
    }

    public Long getPageNo() {
        return this.pageNo;
    }

    public void setPageNo(Long pageNo) {
        this.pageNo = pageNo;
    }

    public Long getPageCount() {
        return this.pageCount;
    }

    public void setPageCount(Long pageCount) {
        this.pageCount = pageCount;
    }
}

package com.github.bluecatlee.gs4d.transaction.request;

import com.github.bluecatlee.gs4d.common.bean.AbstractRequest;

public class SharedGetRequest extends AbstractRequest {
    private static final long serialVersionUID = 3462742216011297515L;

    private Long pageNo;

    private Long pageCount;

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

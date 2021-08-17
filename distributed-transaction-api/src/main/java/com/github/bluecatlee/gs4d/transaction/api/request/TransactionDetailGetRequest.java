package com.github.bluecatlee.gs4d.transaction.api.request;

import com.github.bluecatlee.gs4d.common.bean.AbstractRequest;

import java.util.Date;

public class TransactionDetailGetRequest extends AbstractRequest {
    private static final long serialVersionUID = 6110641534721491852L;

    private Long transactionId;

    private Date startDtme;

    private Date endDtme;

    private Long dbId;

    private String table;

    private String sqlSearch;

    private Long pageNo;

    private Long pageCount;

    public Long getTransactionId() {
        return this.transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public Date getStartDtme() {
        return this.startDtme;
    }

    public void setStartDtme(Date startDtme) {
        this.startDtme = startDtme;
    }

    public Date getEndDtme() {
        return this.endDtme;
    }

    public void setEndDtme(Date endDtme) {
        this.endDtme = endDtme;
    }

    public Long getDbId() {
        return this.dbId;
    }

    public void setDbId(Long dbId) {
        this.dbId = dbId;
    }

    public String getTable() {
        return this.table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getSqlSearch() {
        return this.sqlSearch;
    }

    public void setSqlSearch(String sqlSearch) {
        this.sqlSearch = sqlSearch;
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

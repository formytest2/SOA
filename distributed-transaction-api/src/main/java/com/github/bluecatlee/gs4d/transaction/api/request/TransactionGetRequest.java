package com.github.bluecatlee.gs4d.transaction.api.request;

import com.github.bluecatlee.gs4d.common.bean.AbstractRequest;

import java.util.Date;

public class TransactionGetRequest extends AbstractRequest {
    private static final long serialVersionUID = -253010702943679130L;

    private Long transactionId;

    private Date startDtme;

    private Date endDtme;

    private String ipAddress;

    private Integer transactionState;

    private String transactionSign;

    private String fromSystem;

    private String methodName;

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

    public String getIpAddress() {
        return this.ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Integer getTransactionState() {
        return this.transactionState;
    }

    public void setTransactionState(Integer transactionState) {
        this.transactionState = transactionState;
    }

    public String getTransactionSign() {
        return this.transactionSign;
    }

    public void setTransactionSign(String transactionSign) {
        this.transactionSign = transactionSign;
    }

    public String getFromSystem() {
        return this.fromSystem;
    }

    public void setFromSystem(String fromSystem) {
        this.fromSystem = fromSystem;
    }

    public String getMethodName() {
        return this.methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
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

    public String getSqlSearch() {
        return this.sqlSearch;
    }

    public void setSqlSearch(String sqlSearch) {
        this.sqlSearch = sqlSearch;
    }
}

package com.github.bluecatlee.gs4d.message.api.request;

import com.github.bluecatlee.gs4d.common.bean.AbstractRequest;

import javax.validation.constraints.NotNull;

public class DubboConsumerUpdateRequest extends AbstractRequest {
    private static final long serialVersionUID = -878364252666440672L;
//    @ApiField(description = "消费者序号")
    @NotNull(message = "消费者序号不能为空!")
    private Long dubboSeries;
    private String dubboService;
    private String dubboMethod;
    private String dubboParam;
    private String dubboCreatePerson;
    private String zkAddress;
    private String zkDevelopAddress;
    private String zkTestAddress;
    private String dubboDirUrl;
    private String version;
    private String dubboGroup;
    private String dubboRemark;

    public Long getDubboSeries() {
        return this.dubboSeries;
    }

    public void setDubboSeries(Long dubboSeries) {
        this.dubboSeries = dubboSeries;
    }

    public String getDubboService() {
        return this.dubboService;
    }

    public void setDubboService(String dubboService) {
        this.dubboService = dubboService;
    }

    public String getDubboMethod() {
        return this.dubboMethod;
    }

    public void setDubboMethod(String dubboMethod) {
        this.dubboMethod = dubboMethod;
    }

    public String getDubboParam() {
        return this.dubboParam;
    }

    public void setDubboParam(String dubboParam) {
        this.dubboParam = dubboParam;
    }

    public String getDubboCreatePerson() {
        return this.dubboCreatePerson;
    }

    public void setDubboCreatePerson(String dubboCreatePerson) {
        this.dubboCreatePerson = dubboCreatePerson;
    }

    public String getZkAddress() {
        return this.zkAddress;
    }

    public void setZkAddress(String zkAddress) {
        this.zkAddress = zkAddress;
    }

    public String getZkDevelopAddress() {
        return this.zkDevelopAddress;
    }

    public void setZkDevelopAddress(String zkDevelopAddress) {
        this.zkDevelopAddress = zkDevelopAddress;
    }

    public String getZkTestAddress() {
        return this.zkTestAddress;
    }

    public void setZkTestAddress(String zkTestAddress) {
        this.zkTestAddress = zkTestAddress;
    }

    public String getDubboDirUrl() {
        return this.dubboDirUrl;
    }

    public void setDubboDirUrl(String dubboDirUrl) {
        this.dubboDirUrl = dubboDirUrl;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDubboGroup() {
        return this.dubboGroup;
    }

    public void setDubboGroup(String dubboGroup) {
        this.dubboGroup = dubboGroup;
    }

    public String getDubboRemark() {
        return this.dubboRemark;
    }

    public void setDubboRemark(String dubboRemark) {
        this.dubboRemark = dubboRemark;
    }
}


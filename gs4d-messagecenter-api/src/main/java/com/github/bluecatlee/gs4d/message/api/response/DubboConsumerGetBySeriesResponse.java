package com.github.bluecatlee.gs4d.message.api.response;

import com.github.bluecatlee.gs4d.common.bean.MessagePack;

import javax.validation.constraints.NotNull;

public class DubboConsumerGetBySeriesResponse extends MessagePack {
    private static final long serialVersionUID = -7107970677836467517L;
//    @ApiField(description = "消费者序号")
    @NotNull(message = "消费者序号不能为空!")
    private Long series;
    private String dubboServiceName;
    private String dubboMethodName;
    private String dubboParam;
    private String createUserName;
    private String zkAddress;
    private String zkDevelopmentAddress;
    private String zkTestAddress;
    private String directAdr;
    private String version;
    private String dubboGroup;
    private String remark;

    public Long getSeries() {
        return this.series;
    }

    public void setSeries(Long series) {
        this.series = series;
    }

    public String getDubboServiceName() {
        return this.dubboServiceName;
    }

    public void setDubboServiceName(String dubboServiceName) {
        this.dubboServiceName = dubboServiceName;
    }

    public String getDubboMethodName() {
        return this.dubboMethodName;
    }

    public void setDubboMethodName(String dubboMethodName) {
        this.dubboMethodName = dubboMethodName;
    }

    public String getDubboParam() {
        return this.dubboParam;
    }

    public void setDubboParam(String dubboParam) {
        this.dubboParam = dubboParam;
    }

    public String getCreateUserName() {
        return this.createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public String getZkAddress() {
        return this.zkAddress;
    }

    public void setZkAddress(String zkAddress) {
        this.zkAddress = zkAddress;
    }

    public String getZkDevelopmentAddress() {
        return this.zkDevelopmentAddress;
    }

    public void setZkDevelopmentAddress(String zkDevelopmentAddress) {
        this.zkDevelopmentAddress = zkDevelopmentAddress;
    }

    public String getZkTestAddress() {
        return this.zkTestAddress;
    }

    public void setZkTestAddress(String zkTestAddress) {
        this.zkTestAddress = zkTestAddress;
    }

    public String getDirectAdr() {
        return this.directAdr;
    }

    public void setDirectAdr(String directAdr) {
        this.directAdr = directAdr;
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

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}

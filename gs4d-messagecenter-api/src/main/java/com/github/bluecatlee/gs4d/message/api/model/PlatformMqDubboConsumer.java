package com.github.bluecatlee.gs4d.message.api.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;

public class PlatformMqDubboConsumer implements Serializable {

    private static final long serialVersionUID = 340161632127857876L;
    static SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

//    @ApiField(description = "dubbo序列号")
    private Long dubboSeries = 0L;
//    @ApiField(description = "服务名")
    private String dubboService;
//    @ApiField(description = "dubbo方法名")
    private String dubboMethod;
//    @ApiField(description = "参数类型")
    private String dubboParam;
//    @ApiField(description = "创建人名字")
    private String dubboCreatePerson;
//    @ApiField(description = "zk测试环境地址")
    private String zkTestAddress;
//    @ApiField(description = "zk正式环境地址")
    private String zkAddress;
//    @ApiField(description = "直连地址")
    private String dubboDirUrl;
//    @ApiField(description = "zk开发环境地址")
    private String zkDevelopAddress;
//    @ApiField(description = "dubbo分组")
    private String dubboGroup;
//    @ApiField(description = "dubbo备注")
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

    public String getZkTestAddress() {
        return this.zkTestAddress;
    }

    public void setZkTestAddress(String zkTestAddress) {
        this.zkTestAddress = zkTestAddress;
    }

    public String getZkAddress() {
        return this.zkAddress;
    }

    public void setZkAddress(String zkAddress) {
        this.zkAddress = zkAddress;
    }

    public String getDubboDirUrl() {
        return this.dubboDirUrl;
    }

    public void setDubboDirUrl(String dubboDirUrl) {
        this.dubboDirUrl = dubboDirUrl;
    }

    public String getZkDevelopAddress() {
        return this.zkDevelopAddress;
    }

    public void setZkDevelopAddress(String zkDevelopAddress) {
        this.zkDevelopAddress = zkDevelopAddress;
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


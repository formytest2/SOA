package com.github.bluecatlee.gs4d.message.api.request;

import com.github.bluecatlee.gs4d.common.bean.AbstractRequest;

import javax.validation.constraints.NotBlank;

public class DubboConsumerAddRequest extends AbstractRequest {
    private static final long serialVersionUID = -613111055551981629L;
    @NotBlank(message = "dubbo类名全路径不能为空")
    private String dubboService;
    @NotBlank(message = "dubbo方法不能为空")
    private String dubboMethod;
    @NotBlank(message = "dubbo入参不能为空")
    private String dubboParam;
    @NotBlank(message = "创建人不能为空")
    private String dubboCreatePerson;
    @NotBlank(message = "正式环境zookeeper地址不能为空")
    private String zkAddress;
    @NotBlank(message = "开发环境zookeeper地址不能为空")
    private String zkDevelopAddress;
    @NotBlank(message = "测试环境zookeeper地址不能为空")
    private String zkTestAddress;
    private String dubboDirUrl;
    private String version;
    private String dubboGroup;
    @NotBlank(message = "备注不能为空")
    private String dubboRemark;

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

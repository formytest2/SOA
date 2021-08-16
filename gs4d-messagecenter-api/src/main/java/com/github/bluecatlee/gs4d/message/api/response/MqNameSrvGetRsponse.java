package com.github.bluecatlee.gs4d.message.api.response;

import com.github.bluecatlee.gs4d.common.bean.MessagePack;

import java.util.List;

public class MqNameSrvGetRsponse extends MessagePack {
    private static final long serialVersionUID = 6109800555506884113L;
    private List<String> nameSrv;
    private String nameSrvRemark;

    public List<String> getNameSrv() {
        return this.nameSrv;
    }

    public void setNameSrv(List<String> nameSrv) {
        this.nameSrv = nameSrv;
    }

    public String getNameSrvRemark() {
        return this.nameSrvRemark;
    }

    public void setNameSrvRemark(String nameSrvRemark) {
        this.nameSrvRemark = nameSrvRemark;
    }
}


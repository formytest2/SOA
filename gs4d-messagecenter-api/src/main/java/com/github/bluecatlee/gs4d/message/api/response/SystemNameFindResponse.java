package com.github.bluecatlee.gs4d.message.api.response;

import com.github.bluecatlee.gs4d.common.bean.MessagePack;
import com.github.bluecatlee.gs4d.message.api.model.SystemModel;

import java.util.List;

public class SystemNameFindResponse extends MessagePack {
    private static final long serialVersionUID = -2050992679599789362L;
    private List<SystemModel> systemNames;

    public List<SystemModel> getSystemNames() {
        return this.systemNames;
    }

    public void setSystemNames(List<SystemModel> systemNames) {
        this.systemNames = systemNames;
    }
}


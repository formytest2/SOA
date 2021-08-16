package com.github.bluecatlee.gs4d.message.api.response;

import com.github.bluecatlee.gs4d.common.bean.MessagePack;

import java.util.List;

public class AllSystemNameFindResponse extends MessagePack {
    private static final long serialVersionUID = 2676443343453796031L;
    private List<String> systemNames;

    public List<String> getSystemNames() {
        return this.systemNames;
    }

    public void setSystemNames(List<String> systemNames) {
        this.systemNames = systemNames;
    }
}

package com.github.bluecatlee.gs4d.message.api.response;

import com.github.bluecatlee.gs4d.common.bean.MessagePack;
import com.github.bluecatlee.gs4d.message.api.model.MonitorModel;

import java.util.List;

public class AllSystemNameAndIdFindResponse extends MessagePack {
    private static final long serialVersionUID = 3551202257523939553L;
    private List<MonitorModel> systemInfo;

    public List<MonitorModel> getSystemInfo() {
        return this.systemInfo;
    }

    public void setSystemInfo(List<MonitorModel> systemInfo) {
        this.systemInfo = systemInfo;
    }
}


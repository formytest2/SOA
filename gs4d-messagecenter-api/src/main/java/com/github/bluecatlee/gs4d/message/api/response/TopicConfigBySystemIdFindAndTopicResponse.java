package com.github.bluecatlee.gs4d.message.api.response;

import com.github.bluecatlee.gs4d.common.bean.MessagePack;
import com.github.bluecatlee.gs4d.message.api.model.MonitorModel;

import java.util.List;

public class TopicConfigBySystemIdFindAndTopicResponse extends MessagePack {
    private static final long serialVersionUID = -8204783947907450722L;
    private List<MonitorModel> monitorList;

    public List<MonitorModel> getMonitorList() {
        return this.monitorList;
    }

    public void setMonitorList(List<MonitorModel> monitorList) {
        this.monitorList = monitorList;
    }
}


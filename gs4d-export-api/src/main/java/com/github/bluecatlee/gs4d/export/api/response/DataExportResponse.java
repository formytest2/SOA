package com.github.bluecatlee.gs4d.export.api.response;

import com.github.bluecatlee.gs4d.common.bean.MessagePack;
import lombok.Data;

@Data
public class DataExportResponse extends MessagePack {
    private static final long serialVersionUID = -242867612503454296L;
    private String data;
}


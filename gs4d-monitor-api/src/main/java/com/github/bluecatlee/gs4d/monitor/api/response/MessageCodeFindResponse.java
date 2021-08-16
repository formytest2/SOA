package com.github.bluecatlee.gs4d.monitor.api.response;

import com.github.bluecatlee.gs4d.common.bean.MessagePack;
import com.github.bluecatlee.gs4d.monitor.api.model.PlatformErrorCode;

import java.util.List;

public class MessageCodeFindResponse extends MessagePack {
    private static final long serialVersionUID = 1L;

    private List<PlatformErrorCode> errorCodes;

    public List<PlatformErrorCode> getErrorCodes() {
        return this.errorCodes;
    }

    public void setErrorCodes(List<PlatformErrorCode> errorCodes) {
        this.errorCodes = errorCodes;
    }
}


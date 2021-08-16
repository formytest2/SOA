package com.github.bluecatlee.gs4d.monitor.api.service;

import com.github.bluecatlee.gs4d.monitor.api.request.CodeWatchUserAddRequest;
import com.github.bluecatlee.gs4d.monitor.api.request.MessageCodeFindRequest;
import com.github.bluecatlee.gs4d.monitor.api.request.MessageCodeUserFindRequest;
import com.github.bluecatlee.gs4d.monitor.api.response.CodeWatchUserAddResponse;
import com.github.bluecatlee.gs4d.monitor.api.response.MessageCodeFindResponse;
import com.github.bluecatlee.gs4d.monitor.api.response.MessageCodeUserFindResponse;

public interface MonitorMessageCodeService {

    MessageCodeFindResponse findMessageCode(MessageCodeFindRequest paramMessageCodeFindRequest);

    MessageCodeUserFindResponse findMessageCodeUser(MessageCodeUserFindRequest paramMessageCodeUserFindRequest);

    CodeWatchUserAddResponse addMessageCodeUser(CodeWatchUserAddRequest paramCodeWatchUserAddRequest);

}


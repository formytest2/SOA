package com.github.bluecatlee.gs4d.monitor.api.service;

import com.github.bluecatlee.gs4d.monitor.api.request.SystemLiveDetectRequest;
import com.github.bluecatlee.gs4d.monitor.api.response.SystemLiveDetectResponse;

public interface UniversalLiveDetectService {

    SystemLiveDetectResponse detectSystemLive(SystemLiveDetectRequest paramSystemLiveDetectRequest);

}


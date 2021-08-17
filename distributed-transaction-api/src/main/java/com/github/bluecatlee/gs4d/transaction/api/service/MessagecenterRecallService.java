package com.github.bluecatlee.gs4d.transaction.api.service;

import com.github.bluecatlee.gs4d.transaction.api.request.MessagecenterRecallRequest;
import com.github.bluecatlee.gs4d.transaction.api.response.MessagecenterRecallResponse;

public interface MessagecenterRecallService {

    MessagecenterRecallResponse recallMessagecenter(MessagecenterRecallRequest paramMessagecenterRecallRequest);
}


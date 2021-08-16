package com.github.bluecatlee.gs4d.transaction.service;

import com.github.bluecatlee.gs4d.transaction.request.MessagecenterRecallRequest;
import com.github.bluecatlee.gs4d.transaction.response.MessagecenterRecallResponse;

public interface MessagecenterRecallService {

    MessagecenterRecallResponse recallMessagecenter(MessagecenterRecallRequest paramMessagecenterRecallRequest);
}


package com.tranboot.client.service.txc.impl;

import com.tranboot.client.service.txc.TxcManualRollbackSqlService;

public class TxcManualRollbackSqlServiceDubboImpl extends TxcManualRollbackSqlService {
    int systemId;

    public TxcManualRollbackSqlServiceDubboImpl(int systemId) {
        this.systemId = systemId;
        this.init();
    }

    public void init() {
    }
}

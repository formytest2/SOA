package com.tranboot.client.service.txc.impl;

import com.github.bluecatlee.gs4d.transaction.api.request.SharedColumnGetRequest;
import com.github.bluecatlee.gs4d.transaction.api.response.SharedColumnGetResponse;
import com.github.bluecatlee.gs4d.transaction.api.service.TransactionInitService;
import com.tranboot.client.service.txc.TxcShardSettingReader;
import com.tranboot.client.spring.ContextUtils;

public class TxcShardSettingReaderDubboImpl implements TxcShardSettingReader {
    public TxcShardSettingReaderDubboImpl() {
    }

    public String shardFiled(String datasource, String table) {
        SharedColumnGetRequest request = new SharedColumnGetRequest();
        request.setSchema(datasource.toLowerCase());
        request.setTable(table.toLowerCase());
        SharedColumnGetResponse response = ((TransactionInitService)ContextUtils.getBean(TransactionInitService.class)).getSharedColumn(request);
        return response.getCode() != 0L ? null : response.getSharedColumnName().toLowerCase();
    }
}


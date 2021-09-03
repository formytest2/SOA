package com.tranboot.client.service.txc;

/**
 * txc shared-setting 读取器
 */
public interface TxcShardSettingReader {

    String shardFiled(String datasource, String table);

}


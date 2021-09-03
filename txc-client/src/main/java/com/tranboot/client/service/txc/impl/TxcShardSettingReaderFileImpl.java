package com.tranboot.client.service.txc.impl;

import com.tranboot.client.exception.TxcTransactionException;
import com.tranboot.client.service.txc.TxcShardSettingReader;
import java.io.IOException;
import java.util.Properties;

/**
 * txc shared-setting 读取器  文件方式
 */
public class TxcShardSettingReaderFileImpl implements TxcShardSettingReader {

    Properties setting = new Properties();

    public TxcShardSettingReaderFileImpl() {
        try {
            this.setting.load(this.getClass().getClassLoader().getResourceAsStream("txc-shard-setting.properties"));
        } catch (IOException e) {
            throw new TxcTransactionException(e, "读取txc-shard-setting.properties出错");
        }
    }

    public String shardFiled(String datasource, String table) {
        return this.setting.getProperty(String.format("%s-%s", datasource.toLowerCase(), table.toLowerCase()));
    }
}

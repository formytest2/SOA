package com.tranboot.client.service.txc.impl;

import com.tranboot.client.model.txc.CustomSetting;
import com.tranboot.client.service.txc.TxcCustomSettingService;
import java.util.ArrayList;
import java.util.List;

public class FileCustomSettingService implements TxcCustomSettingService {
    private List<CustomSetting> settings = new ArrayList();

    public FileCustomSettingService(String xmlFile) {
    }

    public boolean customField(String table, String field, Integer type) {
        return this.settings.contains(new CustomSetting(field, table, type));
    }
}

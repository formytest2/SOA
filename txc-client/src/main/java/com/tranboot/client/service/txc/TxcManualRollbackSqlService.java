package com.tranboot.client.service.txc;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * txc事务手动回滚sql配置
 *      将配置保存到rollbackCache中
 */
public abstract class TxcManualRollbackSqlService {
    protected Logger logger = LoggerFactory.getLogger(TxcManualRollbackSqlService.class);
    protected Map<String, Map<String, Object>> rollbackCache = new HashMap();   // 原始sql -> 回滚sql信息

    public abstract void init();

    public Map<String, Object> map(String originalSql) {
        return (Map)this.rollbackCache.get(originalSql);
    }
}


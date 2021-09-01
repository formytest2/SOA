package com.tranboot.client.druid.wall;

import com.tranboot.client.druid.util.JdbcSqlStatUtils;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;

public class WallFunctionStat {
    private volatile long invokeCount;
    static final AtomicLongFieldUpdater<WallFunctionStat> invokeCountUpdater = AtomicLongFieldUpdater.newUpdater(WallFunctionStat.class, "invokeCount");

    public WallFunctionStat() {
    }

    public long getInvokeCount() {
        return this.invokeCount;
    }

    public void incrementInvokeCount() {
        invokeCountUpdater.incrementAndGet(this);
    }

    public void addSqlFunctionStat(WallSqlFunctionStat sqlFunctionStat) {
        this.invokeCount += (long)sqlFunctionStat.getInvokeCount();
    }

    public String toString() {
        return "{\"invokeCount\":" + this.invokeCount + "}";
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap();
        map.put("invokeCount", this.invokeCount);
        return map;
    }

    public WallFunctionStatValue getStatValue(boolean reset) {
        WallFunctionStatValue statValue = new WallFunctionStatValue();
        statValue.setInvokeCount(JdbcSqlStatUtils.get(this, invokeCountUpdater, reset));
        return statValue;
    }
}

package com.tranboot.client.druid.wall;

import com.tranboot.client.druid.support.monitor.annotation.AggregateType;
import com.tranboot.client.druid.support.monitor.annotation.MField;
import com.tranboot.client.druid.support.monitor.annotation.MTable;
import java.util.LinkedHashMap;
import java.util.Map;

@MTable(name = "druid_wall_function")
public class WallFunctionStatValue {
    @MField(
            groupBy = true,
            aggregate = AggregateType.None
    )
    private String name;
    @MField(
            aggregate = AggregateType.Sum
    )
    private long invokeCount;

    public WallFunctionStatValue() {
    }

    public WallFunctionStatValue(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getInvokeCount() {
        return this.invokeCount;
    }

    public void setInvokeCount(long invokeCount) {
        this.invokeCount = invokeCount;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new LinkedHashMap(2);
        map.put("name", this.name);
        map.put("invokeCount", this.invokeCount);
        return map;
    }
}

package com.tranboot.client.druid.wall;

import com.tranboot.client.druid.support.monitor.annotation.AggregateType;
import com.tranboot.client.druid.support.monitor.annotation.MField;
import com.tranboot.client.druid.support.monitor.annotation.MTable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@MTable(
        name = "druid_wall"
)
public class WallProviderStatValue {
    @MField(
            aggregate = AggregateType.None
    )
    private String name;
    @MField(
            aggregate = AggregateType.Sum
    )
    private long checkCount;
    @MField(
            aggregate = AggregateType.Sum
    )
    private long hardCheckCount;
    @MField(
            aggregate = AggregateType.Sum
    )
    private long violationCount;
    @MField(
            aggregate = AggregateType.Sum
    )
    private long whiteListHitCount;
    @MField(
            aggregate = AggregateType.Sum
    )
    private long blackListHitCount;
    @MField(
            aggregate = AggregateType.Sum
    )
    private long syntaxErrorCount;
    @MField(
            aggregate = AggregateType.Sum
    )
    private long violationEffectRowCount;
    private final List<WallTableStatValue> tables = new ArrayList();
    private final List<WallFunctionStatValue> functions = new ArrayList();
    private final List<WallSqlStatValue> whiteList = new ArrayList();
    private final List<WallSqlStatValue> blackList = new ArrayList();

    public WallProviderStatValue() {
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCheckCount() {
        return this.checkCount;
    }

    public void setCheckCount(long checkCount) {
        this.checkCount = checkCount;
    }

    public long getHardCheckCount() {
        return this.hardCheckCount;
    }

    public void setHardCheckCount(long hardCheckCount) {
        this.hardCheckCount = hardCheckCount;
    }

    public long getViolationCount() {
        return this.violationCount;
    }

    public void setViolationCount(long violationCount) {
        this.violationCount = violationCount;
    }

    public long getWhiteListHitCount() {
        return this.whiteListHitCount;
    }

    public void setWhiteListHitCount(long whiteListHitCount) {
        this.whiteListHitCount = whiteListHitCount;
    }

    public long getBlackListHitCount() {
        return this.blackListHitCount;
    }

    public void setBlackListHitCount(long blackListHitCount) {
        this.blackListHitCount = blackListHitCount;
    }

    public long getSyntaxErrorCount() {
        return this.syntaxErrorCount;
    }

    public void setSyntaxErrorCount(long syntaxErrorCount) {
        this.syntaxErrorCount = syntaxErrorCount;
    }

    public long getViolationEffectRowCount() {
        return this.violationEffectRowCount;
    }

    public void setViolationEffectRowCount(long violationEffectRowCount) {
        this.violationEffectRowCount = violationEffectRowCount;
    }

    public List<WallTableStatValue> getTables() {
        return this.tables;
    }

    public List<WallFunctionStatValue> getFunctions() {
        return this.functions;
    }

    public List<WallSqlStatValue> getWhiteList() {
        return this.whiteList;
    }

    public List<WallSqlStatValue> getBlackList() {
        return this.blackList;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> info = new LinkedHashMap();
        info.put("checkCount", this.getCheckCount());
        info.put("hardCheckCount", this.getHardCheckCount());
        info.put("violationCount", this.getViolationCount());
        info.put("violationEffectRowCount", this.getViolationEffectRowCount());
        info.put("blackListHitCount", this.getBlackListHitCount());
        info.put("blackListSize", this.getBlackList().size());
        info.put("whiteListHitCount", this.getWhiteListHitCount());
        info.put("whiteListSize", this.getWhiteList().size());
        info.put("syntaxErrorCount", this.getSyntaxErrorCount());
        List<Map<String, Object>> whiteList = new ArrayList(this.tables.size());
        Iterator var3 = this.tables.iterator();

        Map statMap;
        while(var3.hasNext()) {
            WallTableStatValue tableStatValue = (WallTableStatValue)var3.next();
            statMap = tableStatValue.toMap();
            whiteList.add(statMap);
        }

        info.put("tables", whiteList);
        whiteList = new ArrayList();
        var3 = this.functions.iterator();

        while(var3.hasNext()) {
            WallFunctionStatValue funStatValue = (WallFunctionStatValue)var3.next();
            statMap = funStatValue.toMap();
            whiteList.add(statMap);
        }

        info.put("functions", whiteList);
        whiteList = new ArrayList(this.blackList.size());
        var3 = this.blackList.iterator();

        WallSqlStatValue sqlStatValue;
        while(var3.hasNext()) {
            sqlStatValue = (WallSqlStatValue)var3.next();
            whiteList.add(sqlStatValue.toMap());
        }

        info.put("blackList", whiteList);
        whiteList = new ArrayList(this.whiteList.size());
        var3 = this.whiteList.iterator();

        while(var3.hasNext()) {
            sqlStatValue = (WallSqlStatValue)var3.next();
            whiteList.add(sqlStatValue.toMap());
        }

        info.put("whiteList", whiteList);
        return info;
    }
}

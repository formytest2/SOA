package com.tranboot.client.druid.wall;

import com.tranboot.client.druid.support.monitor.annotation.AggregateType;
import com.tranboot.client.druid.support.monitor.annotation.MField;
import com.tranboot.client.druid.support.monitor.annotation.MTable;
import java.util.LinkedHashMap;
import java.util.Map;

@MTable(
        name = "druid_wall_table"
)
public class WallTableStatValue {
    @MField(
            aggregate = AggregateType.None
    )
    private String name;
    @MField(
            aggregate = AggregateType.Sum
    )
    private long selectCount;
    @MField(
            aggregate = AggregateType.Sum
    )
    private long selectIntoCount;
    @MField(
            aggregate = AggregateType.Sum
    )
    private long insertCount;
    @MField(
            aggregate = AggregateType.Sum
    )
    private long updateCount;
    @MField(
            aggregate = AggregateType.Sum
    )
    private long deleteCount;
    @MField(
            aggregate = AggregateType.Sum
    )
    private long truncateCount;
    @MField(
            aggregate = AggregateType.Sum
    )
    private long createCount;
    @MField(
            aggregate = AggregateType.Sum
    )
    private long alterCount;
    @MField(
            aggregate = AggregateType.Sum
    )
    private long dropCount;
    @MField(
            aggregate = AggregateType.Sum
    )
    private long replaceCount;
    @MField(
            aggregate = AggregateType.Sum
    )
    private long deleteDataCount;
    @MField(
            aggregate = AggregateType.Sum
    )
    private long updateDataCount;
    @MField(
            aggregate = AggregateType.Sum
    )
    private long insertDataCount;
    @MField(
            aggregate = AggregateType.Sum
    )
    private long fetchRowCount;
    @MField(
            name = "f1",
            aggregate = AggregateType.Sum
    )
    protected long fetchRowCount_0_1;
    @MField(
            name = "f10",
            aggregate = AggregateType.Sum
    )
    protected long fetchRowCount_1_10;
    @MField(
            name = "f100",
            aggregate = AggregateType.Sum
    )
    protected long fetchRowCount_10_100;
    @MField(
            name = "f1000",
            aggregate = AggregateType.Sum
    )
    protected int fetchRowCount_100_1000;
    @MField(
            name = "f10000",
            aggregate = AggregateType.Sum
    )
    protected int fetchRowCount_1000_10000;
    @MField(
            name = "fmore",
            aggregate = AggregateType.Sum
    )
    protected int fetchRowCount_10000_more;
    @MField(
            name = "u1",
            aggregate = AggregateType.Sum
    )
    protected long updateDataCount_0_1;
    @MField(
            name = "u10",
            aggregate = AggregateType.Sum
    )
    protected long updateDataCount_1_10;
    @MField(
            name = "u100",
            aggregate = AggregateType.Sum
    )
    protected long updateDataCount_10_100;
    @MField(
            name = "u1000",
            aggregate = AggregateType.Sum
    )
    protected int updateDataCount_100_1000;
    @MField(
            name = "u10000",
            aggregate = AggregateType.Sum
    )
    protected int updateDataCount_1000_10000;
    @MField(
            name = "umore",
            aggregate = AggregateType.Sum
    )
    protected int updateDataCount_10000_more;
    @MField(
            name = "del_1",
            aggregate = AggregateType.Sum
    )
    protected long deleteDataCount_0_1;
    @MField(
            name = "del_10",
            aggregate = AggregateType.Sum
    )
    protected long deleteDataCount_1_10;
    @MField(
            name = "del_100",
            aggregate = AggregateType.Sum
    )
    protected long deleteDataCount_10_100;
    @MField(
            name = "del_1000",
            aggregate = AggregateType.Sum
    )
    protected int deleteDataCount_100_1000;
    @MField(
            name = "del_10000",
            aggregate = AggregateType.Sum
    )
    protected int deleteDataCount_1000_10000;
    @MField(
            name = "del_more",
            aggregate = AggregateType.Sum
    )
    protected int deleteDataCount_10000_more;

    public long[] getDeleteDataHistogram() {
        return new long[]{this.deleteDataCount_0_1, this.deleteDataCount_1_10, this.deleteDataCount_10_100, (long)this.deleteDataCount_100_1000, (long)this.deleteDataCount_1000_10000, (long)this.deleteDataCount_10000_more};
    }

    public WallTableStatValue() {
        this((String)null);
    }

    public WallTableStatValue(String name) {
        this.name = name;
    }

    public long[] getFetchRowHistogram() {
        return new long[]{this.fetchRowCount_0_1, this.fetchRowCount_1_10, this.fetchRowCount_10_100, (long)this.fetchRowCount_100_1000, (long)this.fetchRowCount_1000_10000, (long)this.fetchRowCount_10000_more};
    }

    public long getTotalExecuteCount() {
        return this.selectCount + this.selectIntoCount + this.insertCount + this.updateCount + this.deleteCount + this.truncateCount + this.createCount + this.dropCount + this.replaceCount;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSelectCount() {
        return this.selectCount;
    }

    public void setSelectCount(long selectCount) {
        this.selectCount = selectCount;
    }

    public long getSelectIntoCount() {
        return this.selectIntoCount;
    }

    public void setSelectIntoCount(long selectIntoCount) {
        this.selectIntoCount = selectIntoCount;
    }

    public long getInsertCount() {
        return this.insertCount;
    }

    public void setInsertCount(long insertCount) {
        this.insertCount = insertCount;
    }

    public long getUpdateCount() {
        return this.updateCount;
    }

    public void setUpdateCount(long updateCount) {
        this.updateCount = updateCount;
    }

    public long getDeleteCount() {
        return this.deleteCount;
    }

    public void setDeleteCount(long deleteCount) {
        this.deleteCount = deleteCount;
    }

    public long getTruncateCount() {
        return this.truncateCount;
    }

    public void setTruncateCount(long truncateCount) {
        this.truncateCount = truncateCount;
    }

    public long getCreateCount() {
        return this.createCount;
    }

    public void setCreateCount(long createCount) {
        this.createCount = createCount;
    }

    public long getAlterCount() {
        return this.alterCount;
    }

    public void setAlterCount(long alterCount) {
        this.alterCount = alterCount;
    }

    public long getDropCount() {
        return this.dropCount;
    }

    public void setDropCount(long dropCount) {
        this.dropCount = dropCount;
    }

    public long getReplaceCount() {
        return this.replaceCount;
    }

    public void setReplaceCount(long replaceCount) {
        this.replaceCount = replaceCount;
    }

    public long getDeleteDataCount() {
        return this.deleteDataCount;
    }

    public void setDeleteDataCount(long deleteDataCount) {
        this.deleteDataCount = deleteDataCount;
    }

    public long getUpdateDataCount() {
        return this.updateDataCount;
    }

    public long[] getUpdateDataHistogram() {
        return new long[]{this.updateDataCount_0_1, this.updateDataCount_1_10, this.updateDataCount_10_100, (long)this.updateDataCount_100_1000, (long)this.updateDataCount_1000_10000, (long)this.updateDataCount_10000_more};
    }

    public void setUpdateDataCount(long updateDataCount) {
        this.updateDataCount = updateDataCount;
    }

    public long getInsertDataCount() {
        return this.insertDataCount;
    }

    public void setInsertDataCount(long insertDataCount) {
        this.insertDataCount = insertDataCount;
    }

    public long getFetchRowCount() {
        return this.fetchRowCount;
    }

    public void setFetchRowCount(long fetchRowCount) {
        this.fetchRowCount = fetchRowCount;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new LinkedHashMap();
        this.toMap(map);
        return map;
    }

    public Map<String, Object> toMap(Map<String, Object> map) {
        map.put("name", this.name);
        if (this.selectCount > 0L) {
            map.put("selectCount", this.selectCount);
        }

        if (this.deleteCount > 0L) {
            map.put("deleteCount", this.deleteCount);
        }

        if (this.insertCount > 0L) {
            map.put("insertCount", this.insertCount);
        }

        if (this.updateCount > 0L) {
            map.put("updateCount", this.updateCount);
        }

        if (this.alterCount > 0L) {
            map.put("alterCount", this.alterCount);
        }

        if (this.dropCount > 0L) {
            map.put("dropCount", this.dropCount);
        }

        if (this.createCount > 0L) {
            map.put("createCount", this.createCount);
        }

        if (this.truncateCount > 0L) {
            map.put("truncateCount", this.truncateCount);
        }

        if (this.replaceCount > 0L) {
            map.put("replaceCount", this.replaceCount);
        }

        if (this.deleteDataCount > 0L) {
            map.put("deleteDataCount", this.deleteDataCount);
            map.put("deleteDataCountHistogram", this.getDeleteDataHistogram());
        }

        if (this.fetchRowCount > 0L) {
            map.put("fetchRowCount", this.fetchRowCount);
            map.put("fetchRowCountHistogram", this.getFetchRowHistogram());
        }

        if (this.updateDataCount > 0L) {
            map.put("updateDataCount", this.updateDataCount);
            map.put("updateDataCountHistogram", this.getUpdateDataHistogram());
        }

        return map;
    }
}

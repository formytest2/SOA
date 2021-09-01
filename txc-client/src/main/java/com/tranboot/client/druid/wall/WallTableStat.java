package com.tranboot.client.druid.wall;

import com.tranboot.client.druid.util.JdbcSqlStatUtils;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;

public class WallTableStat {
    private volatile long selectCount;
    private volatile long selectIntoCount;
    private volatile long insertCount;
    private volatile long updateCount;
    private volatile long deleteCount;
    private volatile long truncateCount;
    private volatile long createCount;
    private volatile long alterCount;
    private volatile long dropCount;
    private volatile long replaceCount;
    private volatile long deleteDataCount;
    private volatile long updateDataCount;
    private volatile long insertDataCount;
    private volatile long fetchRowCount;
    static final AtomicLongFieldUpdater<WallTableStat> selectCountUpdater = AtomicLongFieldUpdater.newUpdater(WallTableStat.class, "selectCount");
    static final AtomicLongFieldUpdater<WallTableStat> selectIntoCountUpdater = AtomicLongFieldUpdater.newUpdater(WallTableStat.class, "selectIntoCount");
    static final AtomicLongFieldUpdater<WallTableStat> insertCountUpdater = AtomicLongFieldUpdater.newUpdater(WallTableStat.class, "insertCount");
    static final AtomicLongFieldUpdater<WallTableStat> updateCountUpdater = AtomicLongFieldUpdater.newUpdater(WallTableStat.class, "updateCount");
    static final AtomicLongFieldUpdater<WallTableStat> deleteCountUpdater = AtomicLongFieldUpdater.newUpdater(WallTableStat.class, "deleteCount");
    static final AtomicLongFieldUpdater<WallTableStat> truncateCountUpdater = AtomicLongFieldUpdater.newUpdater(WallTableStat.class, "truncateCount");
    static final AtomicLongFieldUpdater<WallTableStat> createCountUpdater = AtomicLongFieldUpdater.newUpdater(WallTableStat.class, "createCount");
    static final AtomicLongFieldUpdater<WallTableStat> alterCountUpdater = AtomicLongFieldUpdater.newUpdater(WallTableStat.class, "alterCount");
    static final AtomicLongFieldUpdater<WallTableStat> dropCountUpdater = AtomicLongFieldUpdater.newUpdater(WallTableStat.class, "dropCount");
    static final AtomicLongFieldUpdater<WallTableStat> replaceCountUpdater = AtomicLongFieldUpdater.newUpdater(WallTableStat.class, "replaceCount");
    static final AtomicLongFieldUpdater<WallTableStat> deleteDataCountUpdater = AtomicLongFieldUpdater.newUpdater(WallTableStat.class, "deleteDataCount");
    static final AtomicLongFieldUpdater<WallTableStat> insertDataCountUpdater = AtomicLongFieldUpdater.newUpdater(WallTableStat.class, "insertDataCount");
    static final AtomicLongFieldUpdater<WallTableStat> updateDataCountUpdater = AtomicLongFieldUpdater.newUpdater(WallTableStat.class, "updateDataCount");
    static final AtomicLongFieldUpdater<WallTableStat> fetchRowCountUpdater = AtomicLongFieldUpdater.newUpdater(WallTableStat.class, "fetchRowCount");
    private volatile long fetchRowCount_0_1;
    private volatile long fetchRowCount_1_10;
    private volatile long fetchRowCount_10_100;
    private volatile int fetchRowCount_100_1000;
    private volatile int fetchRowCount_1000_10000;
    private volatile int fetchRowCount_10000_more;
    static final AtomicLongFieldUpdater<WallTableStat> fetchRowCount_0_1_Updater = AtomicLongFieldUpdater.newUpdater(WallTableStat.class, "fetchRowCount_0_1");
    static final AtomicLongFieldUpdater<WallTableStat> fetchRowCount_1_10_Updater = AtomicLongFieldUpdater.newUpdater(WallTableStat.class, "fetchRowCount_1_10");
    static final AtomicLongFieldUpdater<WallTableStat> fetchRowCount_10_100_Updater = AtomicLongFieldUpdater.newUpdater(WallTableStat.class, "fetchRowCount_10_100");
    static final AtomicIntegerFieldUpdater<WallTableStat> fetchRowCount_100_1000_Updater = AtomicIntegerFieldUpdater.newUpdater(WallTableStat.class, "fetchRowCount_100_1000");
    static final AtomicIntegerFieldUpdater<WallTableStat> fetchRowCount_1000_10000_Updater = AtomicIntegerFieldUpdater.newUpdater(WallTableStat.class, "fetchRowCount_1000_10000");
    static final AtomicIntegerFieldUpdater<WallTableStat> fetchRowCount_10000_more_Updater = AtomicIntegerFieldUpdater.newUpdater(WallTableStat.class, "fetchRowCount_10000_more");
    private volatile long updateDataCount_0_1;
    private volatile long updateDataCount_1_10;
    private volatile long updateDataCount_10_100;
    private volatile int updateDataCount_100_1000;
    private volatile int updateDataCount_1000_10000;
    private volatile int updateDataCount_10000_more;
    static final AtomicLongFieldUpdater<WallTableStat> updateDataCount_0_1_Updater = AtomicLongFieldUpdater.newUpdater(WallTableStat.class, "updateDataCount_0_1");
    static final AtomicLongFieldUpdater<WallTableStat> updateDataCount_1_10_Updater = AtomicLongFieldUpdater.newUpdater(WallTableStat.class, "updateDataCount_1_10");
    static final AtomicLongFieldUpdater<WallTableStat> updateDataCount_10_100_Updater = AtomicLongFieldUpdater.newUpdater(WallTableStat.class, "updateDataCount_10_100");
    static final AtomicIntegerFieldUpdater<WallTableStat> updateDataCount_100_1000_Updater = AtomicIntegerFieldUpdater.newUpdater(WallTableStat.class, "updateDataCount_100_1000");
    static final AtomicIntegerFieldUpdater<WallTableStat> updateDataCount_1000_10000_Updater = AtomicIntegerFieldUpdater.newUpdater(WallTableStat.class, "updateDataCount_1000_10000");
    static final AtomicIntegerFieldUpdater<WallTableStat> updateDataCount_10000_more_Updater = AtomicIntegerFieldUpdater.newUpdater(WallTableStat.class, "updateDataCount_10000_more");
    private volatile long deleteDataCount_0_1;
    private volatile long deleteDataCount_1_10;
    private volatile long deleteDataCount_10_100;
    private volatile int deleteDataCount_100_1000;
    private volatile int deleteDataCount_1000_10000;
    private volatile int deleteDataCount_10000_more;
    static final AtomicLongFieldUpdater<WallTableStat> deleteDataCount_0_1_Updater = AtomicLongFieldUpdater.newUpdater(WallTableStat.class, "deleteDataCount_0_1");
    static final AtomicLongFieldUpdater<WallTableStat> deleteDataCount_1_10_Updater = AtomicLongFieldUpdater.newUpdater(WallTableStat.class, "deleteDataCount_1_10");
    static final AtomicLongFieldUpdater<WallTableStat> deleteDataCount_10_100_Updater = AtomicLongFieldUpdater.newUpdater(WallTableStat.class, "deleteDataCount_10_100");
    static final AtomicIntegerFieldUpdater<WallTableStat> deleteDataCount_100_1000_Updater = AtomicIntegerFieldUpdater.newUpdater(WallTableStat.class, "deleteDataCount_100_1000");
    static final AtomicIntegerFieldUpdater<WallTableStat> deleteDataCount_1000_10000_Updater = AtomicIntegerFieldUpdater.newUpdater(WallTableStat.class, "deleteDataCount_1000_10000");
    static final AtomicIntegerFieldUpdater<WallTableStat> deleteDataCount_10000_more_Updater = AtomicIntegerFieldUpdater.newUpdater(WallTableStat.class, "deleteDataCount_10000_more");

    public WallTableStat() {
    }

    public long getSelectCount() {
        return this.selectCount;
    }

    public long getSelectIntoCount() {
        return this.selectIntoCount;
    }

    public long getInsertCount() {
        return this.insertCount;
    }

    public long getUpdateCount() {
        return this.updateCount;
    }

    public long getDeleteCount() {
        return this.deleteCount;
    }

    public long getTruncateCount() {
        return this.truncateCount;
    }

    public long getCreateCount() {
        return this.createCount;
    }

    public long getAlterCount() {
        return this.alterCount;
    }

    public long getDropCount() {
        return this.dropCount;
    }

    public long getReplaceCount() {
        return this.replaceCount;
    }

    public long getDeleteDataCount() {
        return this.deleteDataCount;
    }

    public long[] getDeleteDataCountHistogramValues() {
        return new long[]{this.deleteDataCount_0_1, this.deleteDataCount_1_10, this.deleteDataCount_10_100, (long)this.deleteDataCount_100_1000, (long)this.deleteDataCount_1000_10000, (long)this.deleteDataCount_10000_more};
    }

    public void addDeleteDataCount(long delta) {
        deleteDataCountUpdater.addAndGet(this, delta);
        if (delta < 1L) {
            deleteDataCount_0_1_Updater.incrementAndGet(this);
        } else if (delta < 10L) {
            deleteDataCount_1_10_Updater.incrementAndGet(this);
        } else if (delta < 100L) {
            deleteDataCount_10_100_Updater.incrementAndGet(this);
        } else if (delta < 1000L) {
            deleteDataCount_100_1000_Updater.incrementAndGet(this);
        } else if (delta < 10000L) {
            deleteDataCount_1000_10000_Updater.incrementAndGet(this);
        } else {
            deleteDataCount_10000_more_Updater.incrementAndGet(this);
        }

    }

    public long getUpdateDataCount() {
        return this.updateDataCount;
    }

    public long[] getUpdateDataCountHistogramValues() {
        return new long[]{this.updateDataCount_0_1, this.updateDataCount_1_10, this.updateDataCount_10_100, (long)this.updateDataCount_100_1000, (long)this.updateDataCount_1000_10000, (long)this.updateDataCount_10000_more};
    }

    public long getInsertDataCount() {
        return this.insertDataCount;
    }

    public void addInsertDataCount(long delta) {
        insertDataCountUpdater.addAndGet(this, delta);
    }

    public void addUpdateDataCount(long delta) {
        updateDataCountUpdater.addAndGet(this, delta);
        if (delta < 1L) {
            updateDataCount_0_1_Updater.incrementAndGet(this);
        } else if (delta < 10L) {
            updateDataCount_1_10_Updater.incrementAndGet(this);
        } else if (delta < 100L) {
            updateDataCount_10_100_Updater.incrementAndGet(this);
        } else if (delta < 1000L) {
            updateDataCount_100_1000_Updater.incrementAndGet(this);
        } else if (delta < 10000L) {
            updateDataCount_1000_10000_Updater.incrementAndGet(this);
        } else {
            updateDataCount_10000_more_Updater.incrementAndGet(this);
        }

    }

    public long getFetchRowCount() {
        return this.fetchRowCount;
    }

    public long[] getFetchRowCountHistogramValues() {
        return new long[]{this.fetchRowCount_0_1, this.fetchRowCount_1_10, this.fetchRowCount_10_100, (long)this.fetchRowCount_100_1000, (long)this.fetchRowCount_1000_10000, (long)this.fetchRowCount_10000_more};
    }

    public void addFetchRowCount(long delta) {
        fetchRowCountUpdater.addAndGet(this, delta);
        if (delta < 1L) {
            fetchRowCount_0_1_Updater.incrementAndGet(this);
        } else if (delta < 10L) {
            fetchRowCount_1_10_Updater.incrementAndGet(this);
        } else if (delta < 100L) {
            fetchRowCount_10_100_Updater.incrementAndGet(this);
        } else if (delta < 1000L) {
            fetchRowCount_100_1000_Updater.incrementAndGet(this);
        } else if (delta < 10000L) {
            fetchRowCount_1000_10000_Updater.incrementAndGet(this);
        } else {
            fetchRowCount_10000_more_Updater.incrementAndGet(this);
        }

    }

    public void addSqlTableStat(WallSqlTableStat stat) {
        long val = (long)stat.getSelectCount();
        if (val > 0L) {
            selectCountUpdater.addAndGet(this, val);
        }

        val = (long)stat.getSelectIntoCount();
        if (val > 0L) {
            selectIntoCountUpdater.addAndGet(this, val);
        }

        val = (long)stat.getInsertCount();
        if (val > 0L) {
            insertCountUpdater.addAndGet(this, val);
        }

        val = (long)stat.getUpdateCount();
        if (val > 0L) {
            updateCountUpdater.addAndGet(this, val);
        }

        val = (long)stat.getDeleteCount();
        if (val > 0L) {
            deleteCountUpdater.addAndGet(this, val);
        }

        val = (long)stat.getAlterCount();
        if (val > 0L) {
            alterCountUpdater.addAndGet(this, val);
        }

        val = (long)stat.getTruncateCount();
        if (val > 0L) {
            truncateCountUpdater.addAndGet(this, val);
        }

        val = (long)stat.getCreateCount();
        if (val > 0L) {
            createCountUpdater.addAndGet(this, val);
        }

        val = (long)stat.getDropCount();
        if (val > 0L) {
            dropCountUpdater.addAndGet(this, val);
        }

        val = (long)stat.getReplaceCount();
        if (val > 0L) {
            replaceCountUpdater.addAndGet(this, val);
        }

    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new LinkedHashMap();
        return this.toMap(map);
    }

    public WallTableStatValue getStatValue(boolean reset) {
        WallTableStatValue statValue = new WallTableStatValue();
        statValue.setSelectCount(JdbcSqlStatUtils.get(this, selectCountUpdater, reset));
        statValue.setDeleteCount(JdbcSqlStatUtils.get(this, deleteCountUpdater, reset));
        statValue.setInsertCount(JdbcSqlStatUtils.get(this, insertCountUpdater, reset));
        statValue.setUpdateCount(JdbcSqlStatUtils.get(this, updateCountUpdater, reset));
        statValue.setAlterCount(JdbcSqlStatUtils.get(this, alterCountUpdater, reset));
        statValue.setDropCount(JdbcSqlStatUtils.get(this, dropCountUpdater, reset));
        statValue.setCreateCount(JdbcSqlStatUtils.get(this, createCountUpdater, reset));
        statValue.setTruncateCount(JdbcSqlStatUtils.get(this, truncateCountUpdater, reset));
        statValue.setReplaceCount(JdbcSqlStatUtils.get(this, replaceCountUpdater, reset));
        statValue.setDeleteDataCount(JdbcSqlStatUtils.get(this, deleteDataCountUpdater, reset));
        statValue.setFetchRowCount(JdbcSqlStatUtils.get(this, fetchRowCountUpdater, reset));
        statValue.setUpdateDataCount(JdbcSqlStatUtils.get(this, updateDataCountUpdater, reset));
        statValue.fetchRowCount_0_1 = JdbcSqlStatUtils.get(this, fetchRowCount_0_1_Updater, reset);
        statValue.fetchRowCount_1_10 = JdbcSqlStatUtils.get(this, fetchRowCount_1_10_Updater, reset);
        statValue.fetchRowCount_10_100 = JdbcSqlStatUtils.get(this, fetchRowCount_10_100_Updater, reset);
        statValue.fetchRowCount_100_1000 = JdbcSqlStatUtils.get(this, fetchRowCount_100_1000_Updater, reset);
        statValue.fetchRowCount_1000_10000 = JdbcSqlStatUtils.get(this, fetchRowCount_1000_10000_Updater, reset);
        statValue.fetchRowCount_10000_more = JdbcSqlStatUtils.get(this, fetchRowCount_10000_more_Updater, reset);
        statValue.updateDataCount_0_1 = JdbcSqlStatUtils.get(this, updateDataCount_0_1_Updater, reset);
        statValue.updateDataCount_1_10 = JdbcSqlStatUtils.get(this, updateDataCount_1_10_Updater, reset);
        statValue.updateDataCount_10_100 = JdbcSqlStatUtils.get(this, updateDataCount_10_100_Updater, reset);
        statValue.updateDataCount_100_1000 = JdbcSqlStatUtils.get(this, updateDataCount_100_1000_Updater, reset);
        statValue.updateDataCount_1000_10000 = JdbcSqlStatUtils.get(this, updateDataCount_1000_10000_Updater, reset);
        statValue.updateDataCount_10000_more = JdbcSqlStatUtils.get(this, updateDataCount_10000_more_Updater, reset);
        statValue.deleteDataCount_0_1 = JdbcSqlStatUtils.get(this, deleteDataCount_0_1_Updater, reset);
        statValue.deleteDataCount_1_10 = JdbcSqlStatUtils.get(this, deleteDataCount_1_10_Updater, reset);
        statValue.deleteDataCount_10_100 = JdbcSqlStatUtils.get(this, deleteDataCount_10_100_Updater, reset);
        statValue.deleteDataCount_100_1000 = JdbcSqlStatUtils.get(this, deleteDataCount_100_1000_Updater, reset);
        statValue.deleteDataCount_1000_10000 = JdbcSqlStatUtils.get(this, deleteDataCount_1000_10000_Updater, reset);
        statValue.deleteDataCount_10000_more = JdbcSqlStatUtils.get(this, deleteDataCount_10000_more_Updater, reset);
        return statValue;
    }

    public Map<String, Object> toMap(Map<String, Object> map) {
        return this.getStatValue(false).toMap(map);
    }
}

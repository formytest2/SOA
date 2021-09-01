package com.tranboot.client.druid.wall;

import java.util.Date;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;

public class WallDenyStat {
    private volatile long denyCount;
    private volatile long lastDenyTimeMillis;
    private volatile long resetCount;
    static final AtomicLongFieldUpdater<WallDenyStat> denyCountUpdater = AtomicLongFieldUpdater.newUpdater(WallDenyStat.class, "denyCount");
    static final AtomicLongFieldUpdater<WallDenyStat> resetCountUpdater = AtomicLongFieldUpdater.newUpdater(WallDenyStat.class, "resetCount");

    public WallDenyStat() {
    }

    public long incrementAndGetDenyCount() {
        this.lastDenyTimeMillis = System.currentTimeMillis();
        return denyCountUpdater.incrementAndGet(this);
    }

    public long getDenyCount() {
        return this.denyCount;
    }

    public Date getLastDenyTime() {
        return this.lastDenyTimeMillis <= 0L ? null : new Date(this.lastDenyTimeMillis);
    }

    public void reset() {
        this.lastDenyTimeMillis = 0L;
        this.denyCount = 0L;
        resetCountUpdater.incrementAndGet(this);
    }

    public long getResetCount() {
        return this.resetCount;
    }
}

package com.tranboot.client.druid.wall;

public class WallSqlFunctionStat {
    private int invokeCount;

    public WallSqlFunctionStat() {
    }

    public int getInvokeCount() {
        return this.invokeCount;
    }

    public void incrementInvokeCount() {
        ++this.invokeCount;
    }

    public void addInvokeCount(int value) {
        this.invokeCount += value;
    }
}

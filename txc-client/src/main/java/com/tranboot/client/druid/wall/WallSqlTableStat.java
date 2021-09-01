package com.tranboot.client.druid.wall;

public class WallSqlTableStat {
    private int selectCount;
    private int selectIntoCount;
    private int insertCount;
    private int updateCount;
    private int deleteCount;
    private int truncateCount;
    private int createCount;
    private int alterCount;
    private int dropCount;
    private int replaceCount;
    private int showCount;
    private String sample;

    public WallSqlTableStat() {
    }

    public String getSample() {
        return this.sample;
    }

    public void setSample(String sample) {
        this.sample = sample;
    }

    public int getReplaceCount() {
        return this.replaceCount;
    }

    public int incrementReplaceCount() {
        return this.replaceCount++;
    }

    public void addReplaceCount(int value) {
        this.replaceCount += value;
    }

    public int getSelectCount() {
        return this.selectCount;
    }

    public void incrementSelectCount() {
        ++this.selectCount;
    }

    public void addSelectCount(int value) {
        this.selectCount += value;
    }

    public int getSelectIntoCount() {
        return this.selectIntoCount;
    }

    public void incrementSelectIntoCount() {
        ++this.selectIntoCount;
    }

    public void addSelectIntoCount(int value) {
        this.selectIntoCount += value;
    }

    public int getInsertCount() {
        return this.insertCount;
    }

    public void incrementInsertCount() {
        ++this.insertCount;
    }

    public void addInsertCount(int value) {
        this.insertCount += value;
    }

    public int getUpdateCount() {
        return this.updateCount;
    }

    public void incrementUpdateCount() {
        ++this.updateCount;
    }

    public void addUpdateCount(int value) {
        this.deleteCount += value;
    }

    public int getDeleteCount() {
        return this.deleteCount;
    }

    public void incrementDeleteCount() {
        ++this.deleteCount;
    }

    public void addDeleteCount(int value) {
        this.deleteCount += value;
    }

    public int getTruncateCount() {
        return this.truncateCount;
    }

    public void incrementTruncateCount() {
        ++this.truncateCount;
    }

    public void addTruncateCount(int value) {
        this.truncateCount += value;
    }

    public int getCreateCount() {
        return this.createCount;
    }

    public void incrementCreateCount() {
        ++this.createCount;
    }

    public void addCreateCount(int value) {
        this.createCount += value;
    }

    public int getAlterCount() {
        return this.alterCount;
    }

    public void incrementAlterCount() {
        ++this.alterCount;
    }

    public void addAlterCount(int value) {
        this.alterCount += value;
    }

    public int getDropCount() {
        return this.dropCount;
    }

    public void incrementDropCount() {
        ++this.dropCount;
    }

    public void addDropCount(int value) {
        this.dropCount += value;
    }

    public int getShowCount() {
        return this.showCount;
    }

    public void incrementShowCount() {
        ++this.showCount;
    }
}

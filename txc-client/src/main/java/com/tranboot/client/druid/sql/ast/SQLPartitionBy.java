package com.tranboot.client.druid.sql.ast;

import java.util.ArrayList;
import java.util.List;

public abstract class SQLPartitionBy extends SQLObjectImpl {
    protected SQLSubPartitionBy subPartitionBy;
    protected SQLExpr partitionsCount;
    protected boolean linear;
    protected List<SQLPartition> partitions = new ArrayList();
    protected List<SQLName> storeIn = new ArrayList();

    public SQLPartitionBy() {
    }

    public List<SQLPartition> getPartitions() {
        return this.partitions;
    }

    public void addPartition(SQLPartition partition) {
        if (partition != null) {
            partition.setParent(this);
        }

        this.partitions.add(partition);
    }

    public SQLSubPartitionBy getSubPartitionBy() {
        return this.subPartitionBy;
    }

    public void setSubPartitionBy(SQLSubPartitionBy subPartitionBy) {
        if (subPartitionBy != null) {
            subPartitionBy.setParent(this);
        }

        this.subPartitionBy = subPartitionBy;
    }

    public SQLExpr getPartitionsCount() {
        return this.partitionsCount;
    }

    public void setPartitionsCount(SQLExpr partitionsCount) {
        if (partitionsCount != null) {
            partitionsCount.setParent(this);
        }

        this.partitionsCount = partitionsCount;
    }

    public boolean isLinear() {
        return this.linear;
    }

    public void setLinear(boolean linear) {
        this.linear = linear;
    }

    public List<SQLName> getStoreIn() {
        return this.storeIn;
    }
}

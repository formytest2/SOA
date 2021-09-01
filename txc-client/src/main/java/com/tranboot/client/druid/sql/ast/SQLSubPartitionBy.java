package com.tranboot.client.druid.sql.ast;

import com.tranboot.client.druid.sql.ast.statement.SQLAssignItem;
import java.util.ArrayList;
import java.util.List;

public abstract class SQLSubPartitionBy extends SQLObjectImpl {
    protected SQLExpr subPartitionsCount;
    protected boolean linear;
    protected List<SQLAssignItem> options = new ArrayList();
    protected List<SQLSubPartition> subPartitionTemplate = new ArrayList();

    public SQLSubPartitionBy() {
    }

    public SQLExpr getSubPartitionsCount() {
        return this.subPartitionsCount;
    }

    public void setSubPartitionsCount(SQLExpr subPartitionsCount) {
        if (subPartitionsCount != null) {
            subPartitionsCount.setParent(this);
        }

        this.subPartitionsCount = subPartitionsCount;
    }

    public boolean isLinear() {
        return this.linear;
    }

    public void setLinear(boolean linear) {
        this.linear = linear;
    }

    public List<SQLAssignItem> getOptions() {
        return this.options;
    }

    public List<SQLSubPartition> getSubPartitionTemplate() {
        return this.subPartitionTemplate;
    }
}

package com.tranboot.client.druid.sql.ast;

import com.tranboot.client.druid.sql.visitor.SQLASTVisitor;
import java.util.ArrayList;
import java.util.List;

public class SQLPartition extends SQLObjectImpl {
    protected SQLName name;
    protected SQLExpr subPartitionsCount;
    protected List<SQLSubPartition> subPartitions = new ArrayList();
    protected SQLPartitionValue values;
    protected SQLExpr dataDirectory;
    protected SQLExpr indexDirectory;
    protected SQLName tableSpace;
    protected SQLExpr maxRows;
    protected SQLExpr minRows;
    protected SQLExpr engine;
    protected SQLExpr comment;

    public SQLPartition() {
    }

    public SQLName getName() {
        return this.name;
    }

    public void setName(SQLName name) {
        if (name != null) {
            name.setParent(this);
        }

        this.name = name;
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

    public SQLPartitionValue getValues() {
        return this.values;
    }

    public void setValues(SQLPartitionValue values) {
        if (values != null) {
            values.setParent(this);
        }

        this.values = values;
    }

    public List<SQLSubPartition> getSubPartitions() {
        return this.subPartitions;
    }

    public void addSubPartition(SQLSubPartition partition) {
        if (partition != null) {
            partition.setParent(this);
        }

        this.subPartitions.add(partition);
    }

    public SQLExpr getIndexDirectory() {
        return this.indexDirectory;
    }

    public void setIndexDirectory(SQLExpr indexDirectory) {
        if (indexDirectory != null) {
            indexDirectory.setParent(this);
        }

        this.indexDirectory = indexDirectory;
    }

    public SQLExpr getDataDirectory() {
        return this.dataDirectory;
    }

    public void setDataDirectory(SQLExpr dataDirectory) {
        if (dataDirectory != null) {
            dataDirectory.setParent(this);
        }

        this.dataDirectory = dataDirectory;
    }

    public SQLName getTableSpace() {
        return this.tableSpace;
    }

    public void setTableSpace(SQLName tableSpace) {
        if (tableSpace != null) {
            tableSpace.setParent(this);
        }

        this.tableSpace = tableSpace;
    }

    public SQLExpr getMaxRows() {
        return this.maxRows;
    }

    public void setMaxRows(SQLExpr maxRows) {
        if (maxRows != null) {
            maxRows.setParent(this);
        }

        this.maxRows = maxRows;
    }

    public SQLExpr getMinRows() {
        return this.minRows;
    }

    public void setMinRows(SQLExpr minRows) {
        if (minRows != null) {
            minRows.setParent(this);
        }

        this.minRows = minRows;
    }

    public SQLExpr getEngine() {
        return this.engine;
    }

    public void setEngine(SQLExpr engine) {
        if (engine != null) {
            engine.setParent(this);
        }

        this.engine = engine;
    }

    public SQLExpr getComment() {
        return this.comment;
    }

    public void setComment(SQLExpr comment) {
        if (comment != null) {
            comment.setParent(this);
        }

        this.comment = comment;
    }

    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.name);
            this.acceptChild(visitor, this.values);
            this.acceptChild(visitor, this.dataDirectory);
            this.acceptChild(visitor, this.indexDirectory);
            this.acceptChild(visitor, this.tableSpace);
            this.acceptChild(visitor, this.maxRows);
            this.acceptChild(visitor, this.minRows);
            this.acceptChild(visitor, this.engine);
            this.acceptChild(visitor, this.comment);
        }

        visitor.endVisit(this);
    }
}

package com.tranboot.client.druid.sql.ast;

import com.tranboot.client.druid.sql.visitor.SQLASTVisitor;
import java.util.ArrayList;
import java.util.List;

public class SQLSubPartitionByRange extends SQLSubPartitionBy {
    private List<SQLName> columns = new ArrayList();

    public SQLSubPartitionByRange() {
    }

    public List<SQLName> getColumns() {
        return this.columns;
    }

    protected void accept0(SQLASTVisitor visitor) {
    }
}

package com.tranboot.client.sqlast;

import com.tranboot.client.druid.sql.builder.impl.SQLSelectBuilderImpl;
import java.util.HashSet;
import java.util.Set;

public class UniqColumnSQLSelectBuilderImpl extends SQLSelectBuilderImpl {
    Set<String> selectColumns;

    public UniqColumnSQLSelectBuilderImpl(String dbType) {
        super(dbType);
    }

    public SQLSelectBuilderImpl select(String... columns) {
        if (this.selectColumns == null) {
            this.selectColumns = new HashSet();
        }

        String[] var2 = columns;
        int var3 = columns.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            String column = var2[var4];
            if (column != null && !this.selectColumns.contains(column)) {
                super.select(new String[]{column});
            }
        }

        return this;
    }
}

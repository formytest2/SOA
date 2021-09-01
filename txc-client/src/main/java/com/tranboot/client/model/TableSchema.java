package com.tranboot.client.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

public class TableSchema {
    private String tableName;
    private String dbName;
    private List<String> primaryKeyStrs = new ArrayList();
    private Collection<List<String>> uniqKeyStrs;
    private String shardField;
    private List<String> columns;

    public TableSchema() {
    }

    public String getShardField() {
        return this.shardField;
    }

    public void setShardField(String shardField) {
        this.shardField = shardField;
    }

    public String getTableName() {
        return this.tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getDbName() {
        return this.dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public List<String> getColumns() {
        return this.columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    public void setUniqKeyStrs(Collection<List<String>> uniqkeys) {
        this.uniqKeyStrs = uniqkeys;
    }

    public Collection<List<String>> getUniqKeyStrs() {
        return this.uniqKeyStrs;
    }

    public void primaryKey(String column) {
        this.primaryKeyStrs.add(column);
    }

    public void setPrimaryKeyStrs(List<String> primaryKeyStrs) {
        this.primaryKeyStrs = primaryKeyStrs;
    }

    public List<String> getPrimaryKeyStrs() {
        return this.primaryKeyStrs;
    }

    public List<String> getPK() {
        return this.primaryKeyStrs.size() <= 0 ? (List)this.uniqKeyStrs.iterator().next() : this.primaryKeyStrs;
    }

    public List<String> matchPK(Set<String> columns) {
        boolean chosePrimary = true;
        Iterator var3 = this.primaryKeyStrs.iterator();

        while(var3.hasNext()) {
            String col = (String)var3.next();
            if (!columns.contains(col)) {
                chosePrimary = false;
                break;
            }
        }

        if (chosePrimary) {
            return this.primaryKeyStrs;
        } else {
            var3 = this.uniqKeyStrs.iterator();

            label30:
            while(var3.hasNext()) {
                List<String> uniqkeys = (List)var3.next();
                Iterator var5 = uniqkeys.iterator();

                while(var5.hasNext()) {
                    String col = (String)var5.next();
                    if (!columns.contains(col)) {
                        continue label30;
                    }
                }

                return uniqkeys;
            }

            return null;
        }
    }

    public String toString() {
        StringBuilder sbuilder = new StringBuilder();
        sbuilder.append("【数据库名】:").append(this.dbName).append(System.lineSeparator());
        sbuilder.append("【表名】:").append(this.tableName).append(System.lineSeparator());
        sbuilder.append("【主键字段】:");
        Iterator var2;
        if (this.primaryKeyStrs != null && this.primaryKeyStrs.size() > 0) {
            sbuilder.append(System.lineSeparator());
            var2 = this.primaryKeyStrs.iterator();

            while(var2.hasNext()) {
                String primaryKeyStr = (String)var2.next();
                sbuilder.append(primaryKeyStr).append("\t");
            }
        } else {
            sbuilder.append("无");
        }

        sbuilder.append(System.lineSeparator());
        sbuilder.append("【唯一索引】:");
        if (this.uniqKeyStrs != null && this.uniqKeyStrs.size() > 0) {
            sbuilder.append(System.lineSeparator());
            var2 = this.uniqKeyStrs.iterator();

            while(var2.hasNext()) {
                List<String> index = (List)var2.next();
                sbuilder.append(StringUtils.join(index, ","));
                sbuilder.append(System.lineSeparator());
            }
        } else {
            sbuilder.append("无");
        }

        sbuilder.append(System.lineSeparator());
        sbuilder.append("【表字段】:").append(System.lineSeparator());
        sbuilder.append(StringUtils.join(this.columns, ","));
        return sbuilder.toString();
    }
}


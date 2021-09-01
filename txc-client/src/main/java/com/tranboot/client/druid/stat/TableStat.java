package com.tranboot.client.druid.stat;

import com.tranboot.client.druid.util.StringUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableStat {
    int selectCount = 0;
    int updateCount = 0;
    int deleteCount = 0;
    int insertCount = 0;
    int dropCount = 0;
    int mergeCount = 0;
    int createCount = 0;
    int alterCount = 0;
    int createIndexCount = 0;
    int dropIndexCount = 0;
    int referencedCount = 0;

    public TableStat() {
    }

    public int getReferencedCount() {
        return this.referencedCount;
    }

    public void incrementReferencedCount() {
        ++this.referencedCount;
    }

    public int getDropIndexCount() {
        return this.dropIndexCount;
    }

    public void incrementDropIndexCount() {
        ++this.dropIndexCount;
    }

    public int getCreateIndexCount() {
        return this.createIndexCount;
    }

    public void incrementCreateIndexCount() {
        ++this.createIndexCount;
    }

    public int getAlterCount() {
        return this.alterCount;
    }

    public void incrementAlterCount() {
        ++this.alterCount;
    }

    public int getCreateCount() {
        return this.createCount;
    }

    public void incrementCreateCount() {
        ++this.createCount;
    }

    public int getMergeCount() {
        return this.mergeCount;
    }

    public void incrementMergeCount() {
        ++this.mergeCount;
    }

    public int getDropCount() {
        return this.dropCount;
    }

    public void incrementDropCount() {
        ++this.dropCount;
    }

    public void setDropCount(int dropCount) {
        this.dropCount = dropCount;
    }

    public int getSelectCount() {
        return this.selectCount;
    }

    public void incrementSelectCount() {
        ++this.selectCount;
    }

    public void setSelectCount(int selectCount) {
        this.selectCount = selectCount;
    }

    public int getUpdateCount() {
        return this.updateCount;
    }

    public void incrementUpdateCount() {
        ++this.updateCount;
    }

    public void setUpdateCount(int updateCount) {
        this.updateCount = updateCount;
    }

    public int getDeleteCount() {
        return this.deleteCount;
    }

    public void incrementDeleteCount() {
        ++this.deleteCount;
    }

    public void setDeleteCount(int deleteCount) {
        this.deleteCount = deleteCount;
    }

    public void incrementInsertCount() {
        ++this.insertCount;
    }

    public int getInsertCount() {
        return this.insertCount;
    }

    public void setInsertCount(int insertCount) {
        this.insertCount = insertCount;
    }

    public String toString() {
        StringBuilder buf = new StringBuilder(4);
        if (this.mergeCount > 0) {
            buf.append("Merge");
        }

        if (this.insertCount > 0) {
            buf.append("Insert");
        }

        if (this.updateCount > 0) {
            buf.append("Update");
        }

        if (this.selectCount > 0) {
            buf.append("Select");
        }

        if (this.deleteCount > 0) {
            buf.append("Delete");
        }

        if (this.dropCount > 0) {
            buf.append("Drop");
        }

        if (this.createCount > 0) {
            buf.append("Create");
        }

        if (this.alterCount > 0) {
            buf.append("Alter");
        }

        if (this.createIndexCount > 0) {
            buf.append("CreateIndex");
        }

        if (this.dropIndexCount > 0) {
            buf.append("DropIndex");
        }

        return buf.toString();
    }

    public static enum Mode {
        Insert(1),
        Update(2),
        Delete(4),
        Select(8),
        Merge(16),
        Truncate(32),
        Alter(64),
        Drop(128),
        DropIndex(256),
        CreateIndex(512),
        Replace(1024);

        public final int mark;

        private Mode(int mark) {
            this.mark = mark;
        }
    }

    public static class Column {
        private String table;
        private String name;
        private boolean where;
        private boolean select;
        private boolean groupBy;
        private boolean having;
        private boolean join;
        private boolean primaryKey;
        private boolean unique;
        private Map<String, Object> attributes = new HashMap();
        private transient String fullName;
        private String dataType;

        public Column() {
        }

        public Column(String table, String name) {
            this.table = table;
            this.name = name;
        }

        public String getTable() {
            return this.table;
        }

        public void setTable(String table) {
            this.table = table;
            this.fullName = null;
        }

        public String getFullName() {
            if (this.fullName == null) {
                if (this.table != null) {
                    this.fullName = this.name;
                } else {
                    this.fullName = this.table + '.' + this.name;
                }
            }

            return this.fullName;
        }

        public boolean isWhere() {
            return this.where;
        }

        public void setWhere(boolean where) {
            this.where = where;
        }

        public boolean isSelect() {
            return this.select;
        }

        public void setSelec(boolean select) {
            this.select = select;
        }

        public boolean isGroupBy() {
            return this.groupBy;
        }

        public void setGroupBy(boolean groupBy) {
            this.groupBy = groupBy;
        }

        public boolean isHaving() {
            return this.having;
        }

        public boolean isJoin() {
            return this.join;
        }

        public void setJoin(boolean join) {
            this.join = join;
        }

        public void setHaving(boolean having) {
            this.having = having;
        }

        public boolean isPrimaryKey() {
            return this.primaryKey;
        }

        public void setPrimaryKey(boolean primaryKey) {
            this.primaryKey = primaryKey;
        }

        public boolean isUnique() {
            return this.unique;
        }

        public void setUnique(boolean unique) {
            this.unique = unique;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
            this.fullName = null;
        }

        public String getDataType() {
            return this.dataType;
        }

        public void setDataType(String dataType) {
            this.dataType = dataType;
        }

        public Map<String, Object> getAttributes() {
            return this.attributes;
        }

        public void setAttributes(Map<String, Object> attributes) {
            this.attributes = attributes;
        }

        public int hashCode() {
            int tableHashCode = this.table != null ? StringUtils.lowerHashCode(this.table) : 0;
            int nameHashCode = this.name != null ? StringUtils.lowerHashCode(this.name) : 0;
            return tableHashCode + nameHashCode;
        }

        public String toString() {
            return this.table != null ? this.table + "." + this.name : this.name;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof TableStat.Column)) {
                return false;
            } else {
                TableStat.Column column = (TableStat.Column)obj;
                if (this.table == null) {
                    if (column.getTable() != null) {
                        return false;
                    }
                } else if (!this.table.equalsIgnoreCase(column.getTable())) {
                    return false;
                }

                if (this.name == null) {
                    if (column.getName() != null) {
                        return false;
                    }
                } else if (!this.name.equalsIgnoreCase(column.getName())) {
                    return false;
                }

                return true;
            }
        }
    }

    public static class Condition {
        private TableStat.Column column;
        private String operator;
        private List<Object> values = new ArrayList();

        public Condition() {
        }

        public TableStat.Column getColumn() {
            return this.column;
        }

        public void setColumn(TableStat.Column column) {
            this.column = column;
        }

        public String getOperator() {
            return this.operator;
        }

        public void setOperator(String operator) {
            this.operator = operator;
        }

        public List<Object> getValues() {
            return this.values;
        }

        public int hashCode() {
            int prime = 31;
            int result = 1;
            result = 31 * result + (this.column == null ? 0 : this.column.hashCode());
            result = 31 * result + (this.operator == null ? 0 : this.operator.hashCode());
            return result;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            } else if (obj == null) {
                return false;
            } else if (this.getClass() != obj.getClass()) {
                return false;
            } else {
                TableStat.Condition other = (TableStat.Condition)obj;
                if (this.column == null) {
                    if (other.column != null) {
                        return false;
                    }
                } else if (!this.column.equals(other.column)) {
                    return false;
                }

                if (this.operator == null) {
                    if (other.operator != null) {
                        return false;
                    }
                } else if (!this.operator.equals(other.operator)) {
                    return false;
                }

                return true;
            }
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(this.column.toString());
            stringBuilder.append(' ');
            stringBuilder.append(this.operator);
            if (this.values.size() == 1) {
                stringBuilder.append(' ');
                stringBuilder.append(String.valueOf(this.values.get(0)));
            } else if (this.values.size() > 0) {
                stringBuilder.append(" (");

                for(int i = 0; i < this.values.size(); ++i) {
                    if (i != 0) {
                        stringBuilder.append(", ");
                    }

                    stringBuilder.append(String.valueOf(this.values.get(i)));
                }

                stringBuilder.append(")");
            }

            return stringBuilder.toString();
        }
    }

    public static class Relationship {
        private TableStat.Column left;
        private TableStat.Column right;
        private String operator;

        public Relationship() {
        }

        public TableStat.Column getLeft() {
            return this.left;
        }

        public void setLeft(TableStat.Column left) {
            this.left = left;
        }

        public TableStat.Column getRight() {
            return this.right;
        }

        public void setRight(TableStat.Column right) {
            this.right = right;
        }

        public String getOperator() {
            return this.operator;
        }

        public void setOperator(String operator) {
            this.operator = operator;
        }

        public int hashCode() {
            int prime = 31;
            int result = 1;
            result = 31 * result + (this.left == null ? 0 : this.left.hashCode());
            result = 31 * result + (this.operator == null ? 0 : this.operator.hashCode());
            result = 31 * result + (this.right == null ? 0 : this.right.hashCode());
            return result;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            } else if (obj == null) {
                return false;
            } else if (this.getClass() != obj.getClass()) {
                return false;
            } else {
                TableStat.Relationship other = (TableStat.Relationship)obj;
                if (this.left == null) {
                    if (other.left != null) {
                        return false;
                    }
                } else if (!this.left.equals(other.left)) {
                    return false;
                }

                if (this.operator == null) {
                    if (other.operator != null) {
                        return false;
                    }
                } else if (!this.operator.equals(other.operator)) {
                    return false;
                }

                if (this.right == null) {
                    if (other.right != null) {
                        return false;
                    }
                } else if (!this.right.equals(other.right)) {
                    return false;
                }

                return true;
            }
        }

        public String toString() {
            return this.left + " " + this.operator + " " + this.right;
        }
    }

    public static class Name {
        private String name;

        public Name(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        public int hashCode() {
            return StringUtils.lowerHashCode(this.name);
        }

        public boolean equals(Object o) {
            if (!(o instanceof TableStat.Name)) {
                return false;
            } else {
                TableStat.Name other = (TableStat.Name)o;
                if (this.name == other.name) {
                    return true;
                } else {
                    return this.name == null | other.name == null ? false : this.name.equalsIgnoreCase(other.name);
                }
            }
        }

        public String toString() {
            return this.name;
        }
    }
}

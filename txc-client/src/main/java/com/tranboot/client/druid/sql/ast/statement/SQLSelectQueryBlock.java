package com.tranboot.client.druid.sql.ast.statement;

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLLimit;
import com.tranboot.client.druid.sql.ast.SQLObjectImpl;
import com.tranboot.client.druid.sql.ast.SQLOrderBy;
import com.tranboot.client.druid.sql.visitor.SQLASTVisitor;
import java.util.ArrayList;
import java.util.List;

public class SQLSelectQueryBlock extends SQLObjectImpl implements SQLSelectQuery {
    protected int distionOption;
    protected final List<SQLSelectItem> selectList = new ArrayList();
    protected SQLTableSource from;
    protected SQLExprTableSource into;
    protected SQLExpr where;
    protected SQLSelectGroupByClause groupBy;
    protected SQLOrderBy orderBy;
    protected boolean parenthesized = false;
    protected boolean forUpdate = false;
    protected boolean noWait = false;
    protected SQLExpr waitTime;
    protected SQLLimit limit;

    public SQLSelectQueryBlock() {
    }

    public SQLExprTableSource getInto() {
        return this.into;
    }

    public void setInto(SQLExpr into) {
        this.setInto(new SQLExprTableSource(into));
    }

    public void setInto(SQLExprTableSource into) {
        if (into != null) {
            into.setParent(this);
        }

        this.into = into;
    }

    public SQLSelectGroupByClause getGroupBy() {
        return this.groupBy;
    }

    public void setGroupBy(SQLSelectGroupByClause groupBy) {
        if (groupBy != null) {
            groupBy.setParent(this);
        }

        this.groupBy = groupBy;
    }

    public SQLExpr getWhere() {
        return this.where;
    }

    public void setWhere(SQLExpr where) {
        if (where != null) {
            where.setParent(this);
        }

        this.where = where;
    }

    public SQLOrderBy getOrderBy() {
        return this.orderBy;
    }

    public void setOrderBy(SQLOrderBy orderBy) {
        if (orderBy != null) {
            orderBy.setParent(this);
        }

        this.orderBy = orderBy;
    }

    public int getDistionOption() {
        return this.distionOption;
    }

    public void setDistionOption(int distionOption) {
        this.distionOption = distionOption;
    }

    public List<SQLSelectItem> getSelectList() {
        return this.selectList;
    }

    public void addSelectItem(SQLSelectItem item) {
        this.selectList.add(item);
        item.setParent(this);
    }

    public SQLTableSource getFrom() {
        return this.from;
    }

    public void setFrom(SQLTableSource from) {
        if (from != null) {
            from.setParent(this);
        }

        this.from = from;
    }

    public boolean isParenthesized() {
        return this.parenthesized;
    }

    public void setParenthesized(boolean parenthesized) {
        this.parenthesized = parenthesized;
    }

    public boolean isForUpdate() {
        return this.forUpdate;
    }

    public void setForUpdate(boolean forUpdate) {
        this.forUpdate = forUpdate;
    }

    public boolean isNoWait() {
        return this.noWait;
    }

    public void setNoWait(boolean noWait) {
        this.noWait = noWait;
    }

    public SQLExpr getWaitTime() {
        return this.waitTime;
    }

    public void setWaitTime(SQLExpr waitTime) {
        if (waitTime != null) {
            waitTime.setParent(this);
        }

        this.waitTime = waitTime;
    }

    public SQLLimit getLimit() {
        return this.limit;
    }

    public void setLimit(SQLLimit limit) {
        if (limit != null) {
            limit.setParent(this);
        }

        this.limit = limit;
    }

    public SQLExpr getFirst() {
        return this.limit == null ? null : this.limit.getRowCount();
    }

    public void setFirst(SQLExpr first) {
        if (this.limit == null) {
            this.limit = new SQLLimit();
        }

        this.limit.setRowCount(first);
    }

    public SQLExpr getOffset() {
        return this.limit == null ? null : this.limit.getOffset();
    }

    public void setOffset(SQLExpr offset) {
        if (this.limit == null) {
            this.limit = new SQLLimit();
        }

        this.limit.setOffset(offset);
    }

    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.selectList);
            this.acceptChild(visitor, this.from);
            this.acceptChild(visitor, this.where);
            this.acceptChild(visitor, this.groupBy);
        }

        visitor.endVisit(this);
    }

    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = 31 * result + Boolean.valueOf(this.parenthesized).hashCode();
        result = 31 * result + this.distionOption;
        result = 31 * result + (this.from == null ? 0 : this.from.hashCode());
        result = 31 * result + (this.groupBy == null ? 0 : this.groupBy.hashCode());
        result = 31 * result + (this.into == null ? 0 : this.into.hashCode());
        result = 31 * result + (this.selectList == null ? 0 : this.selectList.hashCode());
        result = 31 * result + (this.where == null ? 0 : this.where.hashCode());
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
            SQLSelectQueryBlock other = (SQLSelectQueryBlock)obj;
            if (this.parenthesized ^ other.parenthesized) {
                return false;
            } else if (this.distionOption != other.distionOption) {
                return false;
            } else {
                if (this.from == null) {
                    if (other.from != null) {
                        return false;
                    }
                } else if (!this.from.equals(other.from)) {
                    return false;
                }

                if (this.groupBy == null) {
                    if (other.groupBy != null) {
                        return false;
                    }
                } else if (!this.groupBy.equals(other.groupBy)) {
                    return false;
                }

                if (this.into == null) {
                    if (other.into != null) {
                        return false;
                    }
                } else if (!this.into.equals(other.into)) {
                    return false;
                }

                if (this.selectList == null) {
                    if (other.selectList != null) {
                        return false;
                    }
                } else if (!this.selectList.equals(other.selectList)) {
                    return false;
                }

                if (this.where == null) {
                    if (other.where != null) {
                        return false;
                    }
                } else if (!this.where.equals(other.where)) {
                    return false;
                }

                return true;
            }
        }
    }
}

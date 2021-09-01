package com.tranboot.client.druid.sql.ast;

import com.tranboot.client.druid.sql.visitor.SQLASTVisitor;
import java.util.ArrayList;
import java.util.List;

public class SQLOver extends SQLObjectImpl {
    protected final List<SQLExpr> partitionBy = new ArrayList();
    protected SQLOrderBy orderBy;
    protected SQLExpr of;
    protected SQLExpr windowing;
    protected SQLOver.WindowingType windowingType;
    protected boolean windowingPreceding;
    protected boolean windowingFollowing;
    protected SQLExpr windowingBetweenBegin;
    protected boolean windowingBetweenBeginPreceding;
    protected boolean windowingBetweenBeginFollowing;
    protected SQLExpr windowingBetweenEnd;
    protected boolean windowingBetweenEndPreceding;
    protected boolean windowingBetweenEndFollowing;

    public SQLOver() {
        this.windowingType = SQLOver.WindowingType.ROWS;
    }

    public SQLOver(SQLOrderBy orderBy) {
        this.windowingType = SQLOver.WindowingType.ROWS;
        this.setOrderBy(orderBy);
    }

    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.partitionBy);
            this.acceptChild(visitor, this.orderBy);
            this.acceptChild(visitor, this.of);
        }

        visitor.endVisit(this);
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

    public SQLExpr getOf() {
        return this.of;
    }

    public void setOf(SQLExpr of) {
        if (of != null) {
            of.setParent(this);
        }

        this.of = of;
    }

    public List<SQLExpr> getPartitionBy() {
        return this.partitionBy;
    }

    public SQLExpr getWindowing() {
        return this.windowing;
    }

    public void setWindowing(SQLExpr windowing) {
        this.windowing = windowing;
    }

    public SQLOver.WindowingType getWindowingType() {
        return this.windowingType;
    }

    public void setWindowingType(SQLOver.WindowingType windowingType) {
        this.windowingType = windowingType;
    }

    public boolean isWindowingPreceding() {
        return this.windowingPreceding;
    }

    public void setWindowingPreceding(boolean windowingPreceding) {
        this.windowingPreceding = windowingPreceding;
    }

    public boolean isWindowingFollowing() {
        return this.windowingFollowing;
    }

    public void setWindowingFollowing(boolean windowingFollowing) {
        this.windowingFollowing = windowingFollowing;
    }

    public SQLExpr getWindowingBetweenBegin() {
        return this.windowingBetweenBegin;
    }

    public void setWindowingBetweenBegin(SQLExpr windowingBetweenBegin) {
        this.windowingBetweenBegin = windowingBetweenBegin;
    }

    public boolean isWindowingBetweenBeginPreceding() {
        return this.windowingBetweenBeginPreceding;
    }

    public void setWindowingBetweenBeginPreceding(boolean windowingBetweenBeginPreceding) {
        this.windowingBetweenBeginPreceding = windowingBetweenBeginPreceding;
    }

    public boolean isWindowingBetweenBeginFollowing() {
        return this.windowingBetweenBeginFollowing;
    }

    public void setWindowingBetweenBeginFollowing(boolean windowingBetweenBeginFollowing) {
        this.windowingBetweenBeginFollowing = windowingBetweenBeginFollowing;
    }

    public SQLExpr getWindowingBetweenEnd() {
        return this.windowingBetweenEnd;
    }

    public void setWindowingBetweenEnd(SQLExpr windowingBetweenEnd) {
        this.windowingBetweenEnd = windowingBetweenEnd;
    }

    public boolean isWindowingBetweenEndPreceding() {
        return this.windowingBetweenEndPreceding;
    }

    public void setWindowingBetweenEndPreceding(boolean windowingBetweenEndPreceding) {
        this.windowingBetweenEndPreceding = windowingBetweenEndPreceding;
    }

    public boolean isWindowingBetweenEndFollowing() {
        return this.windowingBetweenEndFollowing;
    }

    public void setWindowingBetweenEndFollowing(boolean windowingBetweenEndFollowing) {
        this.windowingBetweenEndFollowing = windowingBetweenEndFollowing;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            SQLOver sqlOver = (SQLOver)o;
            if (this.windowingPreceding != sqlOver.windowingPreceding) {
                return false;
            } else if (this.windowingFollowing != sqlOver.windowingFollowing) {
                return false;
            } else if (this.windowingBetweenBeginPreceding != sqlOver.windowingBetweenBeginPreceding) {
                return false;
            } else if (this.windowingBetweenBeginFollowing != sqlOver.windowingBetweenBeginFollowing) {
                return false;
            } else if (this.windowingBetweenEndPreceding != sqlOver.windowingBetweenEndPreceding) {
                return false;
            } else if (this.windowingBetweenEndFollowing != sqlOver.windowingBetweenEndFollowing) {
                return false;
            } else {
                if (this.partitionBy != null) {
                    if (!this.partitionBy.equals(sqlOver.partitionBy)) {
                        return false;
                    }
                } else if (sqlOver.partitionBy != null) {
                    return false;
                }

                label89: {
                    if (this.orderBy != null) {
                        if (this.orderBy.equals(sqlOver.orderBy)) {
                            break label89;
                        }
                    } else if (sqlOver.orderBy == null) {
                        break label89;
                    }

                    return false;
                }

                if (this.of != null) {
                    if (!this.of.equals(sqlOver.of)) {
                        return false;
                    }
                } else if (sqlOver.of != null) {
                    return false;
                }

                if (this.windowing != null) {
                    if (!this.windowing.equals(sqlOver.windowing)) {
                        return false;
                    }
                } else if (sqlOver.windowing != null) {
                    return false;
                }

                if (this.windowingType != sqlOver.windowingType) {
                    return false;
                } else {
                    if (this.windowingBetweenBegin != null) {
                        if (this.windowingBetweenBegin.equals(sqlOver.windowingBetweenBegin)) {
                            return this.windowingBetweenEnd != null ? this.windowingBetweenEnd.equals(sqlOver.windowingBetweenEnd) : sqlOver.windowingBetweenEnd == null;
                        }
                    } else if (sqlOver.windowingBetweenBegin == null) {
                        return this.windowingBetweenEnd != null ? this.windowingBetweenEnd.equals(sqlOver.windowingBetweenEnd) : sqlOver.windowingBetweenEnd == null;
                    }

                    return false;
                }
            }
        } else {
            return false;
        }
    }

    public int hashCode() {
        int result = this.partitionBy != null ? this.partitionBy.hashCode() : 0;
        result = 31 * result + (this.orderBy != null ? this.orderBy.hashCode() : 0);
        result = 31 * result + (this.of != null ? this.of.hashCode() : 0);
        result = 31 * result + (this.windowing != null ? this.windowing.hashCode() : 0);
        result = 31 * result + (this.windowingType != null ? this.windowingType.hashCode() : 0);
        result = 31 * result + (this.windowingPreceding ? 1 : 0);
        result = 31 * result + (this.windowingFollowing ? 1 : 0);
        result = 31 * result + (this.windowingBetweenBegin != null ? this.windowingBetweenBegin.hashCode() : 0);
        result = 31 * result + (this.windowingBetweenBeginPreceding ? 1 : 0);
        result = 31 * result + (this.windowingBetweenBeginFollowing ? 1 : 0);
        result = 31 * result + (this.windowingBetweenEnd != null ? this.windowingBetweenEnd.hashCode() : 0);
        result = 31 * result + (this.windowingBetweenEndPreceding ? 1 : 0);
        result = 31 * result + (this.windowingBetweenEndFollowing ? 1 : 0);
        return result;
    }

    public static enum WindowingType {
        ROWS,
        RANGE;

        private WindowingType() {
        }
    }
}

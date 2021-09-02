package com.tranboot.client.sqlast;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.*;
import com.alibaba.druid.sql.ast.statement.*;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlExportParameterVisitor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MySQLRewriteVistorAop extends MySqlExportParameterVisitor {
    private SQLASTVisitorAspect aspect;
    private int point = 0;

    public MySQLRewriteVistorAop(List<Object> args, StringBuilder sbuilder, SQLASTVisitorAspect aspect) {
        super(args, sbuilder, true);
        this.setParameterized(false);
        this.setPrettyFormat(false);
        this.aspect = aspect;
    }

    public MySQLRewriteVistorAop(SQLASTVisitorAspect aspect) {
        super(new ArrayList(), (Appendable)null, true);
        this.aspect = aspect;
    }

    public boolean visit(SQLBinaryOpExpr x) {
        if (!(x.getParent() instanceof SQLUpdateStatement) && !(x.getParent() instanceof SQLDeleteStatement)) {
            return super.visit(x);
        } else {
            this.aspect.whereEnterPoint(x);
            this.point = 1;
            boolean r = super.visit(x);
            this.point = 0;
            this.aspect.whereExitPoint(x);
            return r;
        }
    }

    public boolean visit(SQLIdentifierExpr x) {
        String targetColumn = x.getName();
        if (x.getParent() instanceof SQLInsertStatement) {
            SQLInsertStatement parent = (SQLInsertStatement)x.getParent();
            targetColumn = this.aspect.insertColumnAspect(x.getName(), parent);
        } else if (x.getParent() instanceof SQLBinaryOpExpr && this.point == 1) {
            SQLExpr right = ((SQLBinaryOpExpr)x.getParent()).getRight();
            if (right instanceof SQLVariantRefExpr) {
                this.aspect.whereBinaryOpAspect((SQLVariantRefExpr)right);
            }

            if (((SQLBinaryOpExpr)x.getParent()).getOperator() == SQLBinaryOperator.Equality) {
                if (right instanceof SQLVariantRefExpr) {
                    targetColumn = this.aspect.whereColumnAspect(x.getName(), (SQLVariantRefExpr)right);
                } else if (right instanceof SQLLiteralExpr) {
                    targetColumn = this.aspect.whereColumnAspect(x.getName(), (SQLLiteralExpr)right);
                }
            }
        } else {
            if (!(x.getParent() instanceof SQLInListExpr)) {
                return super.visit(x);
            }

            targetColumn = this.aspect.whereColumnAspect(x.getName(), (SQLInListExpr)x.getParent());
        }

        x.setName(targetColumn);
        return super.visit(x);
    }

    public boolean visit(SQLPropertyExpr x) {
        String targetColumn = x.getName();
        if (x.getParent() instanceof SQLBinaryOpExpr && this.point == 1) {
            SQLExpr right = ((SQLBinaryOpExpr)x.getParent()).getRight();
            if (right instanceof SQLVariantRefExpr) {
                this.aspect.whereBinaryOpAspect((SQLVariantRefExpr)right);
            }

            if (((SQLBinaryOpExpr)x.getParent()).getOperator() == SQLBinaryOperator.Equality) {
                if (right instanceof SQLVariantRefExpr) {
                    targetColumn = this.aspect.whereColumnAspect(x.getName(), (SQLVariantRefExpr)right);
                } else if (right instanceof SQLLiteralExpr) {
                    targetColumn = this.aspect.whereColumnAspect(x.getName(), (SQLLiteralExpr)right);
                }
            }
        } else {
            if (!(x.getParent() instanceof SQLInListExpr)) {
                return super.visit(x);
            }

            targetColumn = this.aspect.whereColumnAspect(x.getName(), (SQLInListExpr)x.getParent());
        }

        x.setName(targetColumn);
        return super.visit(x);
    }

    public void print(Date date) {
        if (this.appender != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            this.print0("'" + dateFormat.format(date) + "'");
        }
    }

    public boolean visit(SQLCharExpr x) {
        this.print('\'');
        String text = x.getText();
        StringBuilder buf = new StringBuilder(text.length());

        for(int i = 0; i < text.length(); ++i) {
            char ch = text.charAt(i);
            if (ch == '\'') {
                buf.append('\'');
                buf.append('\'');
            } else if (ch == '\\') {
                buf.append('\\');
                buf.append('\\');
            } else if (ch == 0) {
                buf.append('\\');
                buf.append('0');
            } else {
                buf.append(ch);
            }
        }

        if (buf.length() != text.length()) {
            text = buf.toString();
        }

        this.print0(text);
        this.print('\'');
        return false;
    }

    public boolean visit(SQLExprTableSource x) {
        x = this.aspect.tableAspect(x);
        if (x.getParent() instanceof SQLInsertStatement) {
            this.aspect.insertEnterPoint((SQLInsertStatement)x.getParent());
        } else if (x.getParent() instanceof SQLUpdateStatement) {
            this.aspect.updateEnterPoint((SQLUpdateStatement)x.getParent());
        } else if (x.getParent() instanceof SQLDeleteStatement) {
            this.aspect.deleteEnterPoint((SQLDeleteStatement)x.getParent());
        }

        return super.visit(x);
    }

    public boolean visit(SQLUpdateSetItem x) {
        x = this.aspect.updateItemAspect(x);
        if (x == null) {
            return false;
        } else {
            SQLExpr left = x.getColumn();
            String targetName;
            if (left instanceof SQLPropertyExpr) {
                targetName = this.aspect.updateColumnAspect(((SQLPropertyExpr)left).getName());
                ((SQLPropertyExpr)left).setName(targetName);
            } else if (left instanceof SQLIdentifierExpr) {
                targetName = this.aspect.updateColumnAspect(((SQLIdentifierExpr)left).getName());
                ((SQLIdentifierExpr)left).setName(targetName);
            }

            left.accept(this);
            this.print0(" = ");
            x.getValue().accept(this);
            return false;
        }
    }

    public boolean visit(SQLVariantRefExpr x) {
        this.aspect.variantRefAspect(x);
        return super.visit(x);
    }
}


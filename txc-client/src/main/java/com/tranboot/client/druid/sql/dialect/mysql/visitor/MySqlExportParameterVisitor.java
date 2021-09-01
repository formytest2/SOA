package com.tranboot.client.druid.sql.dialect.mysql.visitor;

import com.tranboot.client.druid.sql.ast.SQLLimit;
import com.tranboot.client.druid.sql.ast.SQLOrderBy;
import com.tranboot.client.druid.sql.ast.expr.SQLBetweenExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLInListExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLMethodInvokeExpr;
import com.tranboot.client.druid.sql.ast.statement.SQLSelectGroupByClause;
import com.tranboot.client.druid.sql.ast.statement.SQLSelectItem;
import com.tranboot.client.druid.sql.visitor.ExportParameterVisitor;
import com.tranboot.client.druid.sql.visitor.ExportParameterVisitorUtils;
import java.util.ArrayList;
import java.util.List;

public class MySqlExportParameterVisitor extends MySqlOutputVisitor implements ExportParameterVisitor {
    private boolean requireParameterizedOutput;

    public MySqlExportParameterVisitor(List<Object> parameters, Appendable appender, boolean wantParameterizedOutput) {
        super(appender, true);
        this.parameters = parameters;
        this.requireParameterizedOutput = wantParameterizedOutput;
    }

    public MySqlExportParameterVisitor() {
        this((List)(new ArrayList()));
    }

    public MySqlExportParameterVisitor(List<Object> parameters) {
        this(parameters, (Appendable)null, false);
    }

    public MySqlExportParameterVisitor(Appendable appender) {
        this(new ArrayList(), appender, true);
    }

    public List<Object> getParameters() {
        return this.parameters;
    }

    public boolean visit(SQLSelectItem x) {
        return this.requireParameterizedOutput ? super.visit(x) : true;
    }

    public boolean visit(SQLLimit x) {
        return this.requireParameterizedOutput ? super.visit(x) : true;
    }

    public boolean visit(SQLOrderBy x) {
        return this.requireParameterizedOutput ? super.visit(x) : false;
    }

    public boolean visit(SQLSelectGroupByClause x) {
        return this.requireParameterizedOutput ? super.visit(x) : false;
    }

    public boolean visit(SQLMethodInvokeExpr x) {
        if (this.requireParameterizedOutput) {
            return super.visit(x);
        } else {
            ExportParameterVisitorUtils.exportParamterAndAccept(this.parameters, x.getParameters());
            return true;
        }
    }

    public boolean visit(SQLInListExpr x) {
        if (this.requireParameterizedOutput) {
            return super.visit(x);
        } else {
            ExportParameterVisitorUtils.exportParamterAndAccept(this.parameters, x.getTargetList());
            return true;
        }
    }

    public boolean visit(SQLBetweenExpr x) {
        if (this.requireParameterizedOutput) {
            return super.visit(x);
        } else {
            ExportParameterVisitorUtils.exportParameter(this.parameters, x);
            return true;
        }
    }

    public boolean visit(SQLBinaryOpExpr x) {
        if (this.requireParameterizedOutput) {
            return super.visit(x);
        } else {
            ExportParameterVisitorUtils.exportParameter(this.parameters, x);
            return true;
        }
    }
}


package com.tranboot.client.druid.sql.dialect.oracle.visitor;

import com.tranboot.client.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLNumberExpr;
import com.tranboot.client.druid.sql.visitor.ParameterizedOutputVisitorUtils;
import com.tranboot.client.druid.sql.visitor.ParameterizedVisitor;

public class OracleParameterizedOutputVisitor extends OracleOutputVisitor implements ParameterizedVisitor {
    public OracleParameterizedOutputVisitor() {
        this(new StringBuilder());
        this.parameterized = true;
    }

    public OracleParameterizedOutputVisitor(Appendable appender) {
        super(appender);
        this.parameterized = true;
    }

    public OracleParameterizedOutputVisitor(Appendable appender, boolean printPostSemi) {
        super(appender, printPostSemi);
    }

    public boolean visit(SQLBinaryOpExpr x) {
        x = ParameterizedOutputVisitorUtils.merge(this, x);
        return super.visit(x);
    }

    public boolean visit(SQLNumberExpr x) {
        if (!ParameterizedOutputVisitorUtils.checkParameterize(x)) {
            return super.visit(x);
        } else {
            this.print('?');
            this.incrementReplaceCunt();
            return false;
        }
    }
}


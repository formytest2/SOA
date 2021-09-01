package com.tranboot.client.druid.sql.visitor;

import com.tranboot.client.druid.sql.ast.expr.SQLBinaryExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLBooleanExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLCaseExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLCharExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLHexExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLIdentifierExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLInListExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLIntegerExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLMethodInvokeExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLNullExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLNumberExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLQueryExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLVariantRefExpr;
import com.tranboot.client.druid.sql.visitor.functions.Function;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SQLEvalVisitorImpl extends SQLASTVisitorAdapter implements SQLEvalVisitor {
    private List<Object> parameters;
    private Map<String, Function> functions;
    private int variantIndex;
    private boolean markVariantIndex;

    public SQLEvalVisitorImpl() {
        this(new ArrayList(1));
    }

    public SQLEvalVisitorImpl(List<Object> parameters) {
        this.parameters = new ArrayList();
        this.functions = new HashMap();
        this.variantIndex = -1;
        this.markVariantIndex = true;
        this.parameters = parameters;
    }

    public List<Object> getParameters() {
        return this.parameters;
    }

    public void setParameters(List<Object> parameters) {
        this.parameters = parameters;
    }

    public boolean visit(SQLCharExpr x) {
        return SQLEvalVisitorUtils.visit(this, x);
    }

    public int incrementAndGetVariantIndex() {
        return ++this.variantIndex;
    }

    public int getVariantIndex() {
        return this.variantIndex;
    }

    public boolean visit(SQLVariantRefExpr x) {
        return SQLEvalVisitorUtils.visit(this, x);
    }

    public boolean visit(SQLBinaryOpExpr x) {
        return SQLEvalVisitorUtils.visit(this, x);
    }

    public boolean visit(SQLIntegerExpr x) {
        return SQLEvalVisitorUtils.visit(this, x);
    }

    public boolean visit(SQLNumberExpr x) {
        return SQLEvalVisitorUtils.visit(this, x);
    }

    public boolean visit(SQLHexExpr x) {
        return SQLEvalVisitorUtils.visit(this, x);
    }

    public boolean visit(SQLCaseExpr x) {
        return SQLEvalVisitorUtils.visit(this, x);
    }

    public boolean visit(SQLInListExpr x) {
        return SQLEvalVisitorUtils.visit(this, x);
    }

    public boolean visit(SQLNullExpr x) {
        return SQLEvalVisitorUtils.visit(this, x);
    }

    public boolean visit(SQLMethodInvokeExpr x) {
        return SQLEvalVisitorUtils.visit(this, x);
    }

    public boolean visit(SQLQueryExpr x) {
        return SQLEvalVisitorUtils.visit(this, x);
    }

    public boolean isMarkVariantIndex() {
        return this.markVariantIndex;
    }

    public void setMarkVariantIndex(boolean markVariantIndex) {
        this.markVariantIndex = markVariantIndex;
    }

    public Function getFunction(String funcName) {
        return (Function)this.functions.get(funcName);
    }

    public void registerFunction(String funcName, Function function) {
        this.functions.put(funcName, function);
    }

    public boolean visit(SQLIdentifierExpr x) {
        return SQLEvalVisitorUtils.visit(this, x);
    }

    public void unregisterFunction(String funcName) {
        this.functions.remove(funcName);
    }

    public boolean visit(SQLBooleanExpr x) {
        x.getAttributes().put("eval.value", x.getValue());
        return false;
    }

    public boolean visit(SQLBinaryExpr x) {
        return SQLEvalVisitorUtils.visit(this, x);
    }
}

package com.tranboot.client.druid.sql.visitor;

import com.tranboot.client.druid.sql.visitor.functions.Function;
import java.util.List;

public interface SQLEvalVisitor extends SQLASTVisitor {
    String EVAL_VALUE = "eval.value";
    String EVAL_EXPR = "eval.expr";
    Object EVAL_ERROR = new Object();
    Object EVAL_VALUE_COUNT = new Object();
    Object EVAL_VALUE_NULL = new Object();

    Function getFunction(String var1);

    void registerFunction(String var1, Function var2);

    void unregisterFunction(String var1);

    List<Object> getParameters();

    void setParameters(List<Object> var1);

    int incrementAndGetVariantIndex();

    boolean isMarkVariantIndex();

    void setMarkVariantIndex(boolean var1);
}

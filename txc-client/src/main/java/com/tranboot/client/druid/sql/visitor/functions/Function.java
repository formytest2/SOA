package com.tranboot.client.druid.sql.visitor.functions;

import com.tranboot.client.druid.sql.ast.expr.SQLMethodInvokeExpr;
import com.tranboot.client.druid.sql.visitor.SQLEvalVisitor;

public interface Function {
  Object eval(SQLEvalVisitor paramSQLEvalVisitor, SQLMethodInvokeExpr paramSQLMethodInvokeExpr);
}


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\visitor\functions\Function.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
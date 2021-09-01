package com.tranboot.client.druid.sql.ast.expr;

import com.tranboot.client.druid.sql.ast.SQLExprImpl;

public abstract class SQLNumericLiteralExpr extends SQLExprImpl implements SQLLiteralExpr {
  public abstract Number getNumber();
  
  public abstract void setNumber(Number paramNumber);
}


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\expr\SQLNumericLiteralExpr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
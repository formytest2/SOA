package com.tranboot.client.druid.sql.builder;

import com.tranboot.client.druid.sql.ast.statement.SQLSelectStatement;

public interface SQLSelectBuilder {
  SQLSelectStatement getSQLSelectStatement();
  
  SQLSelectBuilder select(String... paramVarArgs);
  
  SQLSelectBuilder selectWithAlias(String paramString1, String paramString2);
  
  SQLSelectBuilder from(String paramString);
  
  SQLSelectBuilder from(String paramString1, String paramString2);
  
  SQLSelectBuilder orderBy(String... paramVarArgs);
  
  SQLSelectBuilder groupBy(String paramString);
  
  SQLSelectBuilder having(String paramString);
  
  SQLSelectBuilder into(String paramString);
  
  SQLSelectBuilder limit(int paramInt);
  
  SQLSelectBuilder limit(int paramInt1, int paramInt2);
  
  SQLSelectBuilder where(String paramString);
  
  SQLSelectBuilder whereAnd(String paramString);
  
  SQLSelectBuilder whereOr(String paramString);
  
  String toString();
}


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\builder\SQLSelectBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
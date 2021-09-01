package com.tranboot.client.druid.sql.builder;

public interface SQLUpdateBuilder {
  SQLUpdateBuilder from(String paramString);
  
  SQLUpdateBuilder from(String paramString1, String paramString2);
  
  SQLUpdateBuilder limit(int paramInt);
  
  SQLUpdateBuilder limit(int paramInt1, int paramInt2);
  
  SQLUpdateBuilder where(String paramString);
  
  SQLUpdateBuilder whereAnd(String paramString);
  
  SQLUpdateBuilder whereOr(String paramString);
  
  SQLUpdateBuilder set(String... paramVarArgs);
}


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\builder\SQLUpdateBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
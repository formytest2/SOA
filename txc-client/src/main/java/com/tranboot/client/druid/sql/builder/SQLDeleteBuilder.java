package com.tranboot.client.druid.sql.builder;

public interface SQLDeleteBuilder {
  SQLDeleteBuilder from(String paramString);
  
  SQLDeleteBuilder from(String paramString1, String paramString2);
  
  SQLDeleteBuilder limit(int paramInt);
  
  SQLDeleteBuilder limit(int paramInt1, int paramInt2);
  
  SQLDeleteBuilder where(String paramString);
  
  SQLDeleteBuilder whereAnd(String paramString);
  
  SQLDeleteBuilder whereOr(String paramString);
}


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\builder\SQLDeleteBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
package com.tranboot.client.druid.sql.builder;

public interface UpdateBuilder {
  UpdateBuilder from(String paramString);
  
  UpdateBuilder from(String paramString1, String paramString2);
  
  UpdateBuilder limit(int paramInt);
  
  UpdateBuilder limit(int paramInt1, int paramInt2);
  
  UpdateBuilder where(String paramString);
  
  UpdateBuilder whereAnd(String paramString);
  
  UpdateBuilder whereOr(String paramString);
}


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\builder\UpdateBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
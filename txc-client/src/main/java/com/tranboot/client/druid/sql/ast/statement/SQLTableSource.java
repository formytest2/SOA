package com.tranboot.client.druid.sql.ast.statement;

import com.tranboot.client.druid.sql.ast.SQLHint;
import com.tranboot.client.druid.sql.ast.SQLObject;

import java.util.List;

public interface SQLTableSource extends SQLObject {
  String getAlias();
  
  void setAlias(String paramString);
  
  List<SQLHint> getHints();
}


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\statement\SQLTableSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
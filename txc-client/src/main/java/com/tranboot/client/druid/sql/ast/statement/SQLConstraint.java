package com.tranboot.client.druid.sql.ast.statement;

import com.tranboot.client.druid.sql.ast.SQLName;
import com.tranboot.client.druid.sql.ast.SQLObject;

public interface SQLConstraint extends SQLObject {
  SQLName getName();
  
  void setName(SQLName paramSQLName);
}


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\statement\SQLConstraint.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
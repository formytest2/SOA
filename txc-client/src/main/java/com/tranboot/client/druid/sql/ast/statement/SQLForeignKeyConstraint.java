package com.tranboot.client.druid.sql.ast.statement;

import com.tranboot.client.druid.sql.ast.SQLName;

import java.util.List;

public interface SQLForeignKeyConstraint extends SQLConstraint, SQLTableElement, SQLTableConstraint {
  List<SQLName> getReferencingColumns();
  
  SQLName getReferencedTableName();
  
  void setReferencedTableName(SQLName paramSQLName);
  
  List<SQLName> getReferencedColumns();
}


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\statement\SQLForeignKeyConstraint.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
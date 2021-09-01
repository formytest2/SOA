package com.tranboot.client.druid.sql.ast.statement;

import com.tranboot.client.druid.sql.ast.SQLExpr;

import java.util.List;

public interface SQLUniqueConstraint extends SQLConstraint {
  List<SQLExpr> getColumns();
}


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\statement\SQLUniqueConstraint.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
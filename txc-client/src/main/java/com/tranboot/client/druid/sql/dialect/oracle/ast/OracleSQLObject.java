package com.tranboot.client.druid.sql.dialect.oracle.ast;

import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.dialect.oracle.visitor.OracleASTVisitor;

public interface OracleSQLObject extends SQLObject {
  void accept0(OracleASTVisitor paramOracleASTVisitor);
}


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\oracle\ast\OracleSQLObject.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
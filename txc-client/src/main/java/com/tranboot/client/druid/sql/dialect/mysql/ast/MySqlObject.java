package com.tranboot.client.druid.sql.dialect.mysql.ast;

import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;

public interface MySqlObject extends SQLObject {
  void accept0(MySqlASTVisitor paramMySqlASTVisitor);
}


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\ast\MySqlObject.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
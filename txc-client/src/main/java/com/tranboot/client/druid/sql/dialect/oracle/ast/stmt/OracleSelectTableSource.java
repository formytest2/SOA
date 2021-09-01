package com.tranboot.client.druid.sql.dialect.oracle.ast.stmt;

import com.tranboot.client.druid.sql.ast.statement.SQLTableSource;
import com.tranboot.client.druid.sql.dialect.oracle.ast.clause.FlashbackQueryClause;

public interface OracleSelectTableSource extends SQLTableSource {
  OracleSelectPivotBase getPivot();
  
  void setPivot(OracleSelectPivotBase paramOracleSelectPivotBase);
  
  FlashbackQueryClause getFlashback();
  
  void setFlashback(FlashbackQueryClause paramFlashbackQueryClause);
}


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\oracle\ast\stmt\OracleSelectTableSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
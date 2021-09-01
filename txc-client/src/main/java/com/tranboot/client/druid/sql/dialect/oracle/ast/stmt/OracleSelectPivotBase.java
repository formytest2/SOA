/*    */ package com.tranboot.client.druid.sql.dialect.oracle.ast.stmt;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.dialect.oracle.ast.OracleSQLObjectImpl;

import java.util.ArrayList;
import java.util.List;

/*    */
/*    */
/*    */

/*    */
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class OracleSelectPivotBase
/*    */   extends OracleSQLObjectImpl
/*    */ {
/* 26 */   protected final List<SQLExpr> pivotFor = new ArrayList<>();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public List<SQLExpr> getPivotFor() {
/* 33 */     return this.pivotFor;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\oracle\ast\stmt\OracleSelectPivotBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
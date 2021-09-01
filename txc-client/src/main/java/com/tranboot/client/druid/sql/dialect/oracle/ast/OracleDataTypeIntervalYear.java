/*    */ package com.tranboot.client.druid.sql.dialect.oracle.ast;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLDataTypeImpl;
import com.tranboot.client.druid.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.tranboot.client.druid.sql.visitor.SQLASTVisitor;

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
/*    */ public class OracleDataTypeIntervalYear
/*    */   extends SQLDataTypeImpl
/*    */   implements OracleSQLObject
/*    */ {
/*    */   public OracleDataTypeIntervalYear() {
/* 25 */     setName("INTERVAL YEAR");
/*    */   }
/*    */ 
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 30 */     accept0((OracleASTVisitor)visitor);
/*    */   }
/*    */ 
/*    */   
/*    */   public void accept0(OracleASTVisitor visitor) {
/* 35 */     if (visitor.visit(this)) {
/* 36 */       acceptChild((SQLASTVisitor)visitor, getArguments());
/*    */     }
/* 38 */     visitor.endVisit(this);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\oracle\ast\OracleDataTypeIntervalYear.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
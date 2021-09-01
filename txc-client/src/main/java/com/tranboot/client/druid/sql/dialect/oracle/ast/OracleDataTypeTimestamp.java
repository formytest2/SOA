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
/*    */ public class OracleDataTypeTimestamp
/*    */   extends SQLDataTypeImpl
/*    */   implements OracleSQLObject
/*    */ {
/*    */   private boolean withTimeZone = false;
/*    */   private boolean withLocalTimeZone = false;
/*    */   
/*    */   public OracleDataTypeTimestamp() {
/* 28 */     setName("TIMESTAMP");
/*    */   }
/*    */ 
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 33 */     accept0((OracleASTVisitor)visitor);
/*    */   }
/*    */ 
/*    */   
/*    */   public void accept0(OracleASTVisitor visitor) {
/* 38 */     if (visitor.visit(this)) {
/* 39 */       acceptChild((SQLASTVisitor)visitor, getArguments());
/*    */     }
/* 41 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public boolean isWithTimeZone() {
/* 45 */     return this.withTimeZone;
/*    */   }
/*    */   
/*    */   public void setWithTimeZone(boolean withTimeZone) {
/* 49 */     this.withTimeZone = withTimeZone;
/*    */   }
/*    */   
/*    */   public boolean isWithLocalTimeZone() {
/* 53 */     return this.withLocalTimeZone;
/*    */   }
/*    */   
/*    */   public void setWithLocalTimeZone(boolean withLocalTimeZone) {
/* 57 */     this.withLocalTimeZone = withLocalTimeZone;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\oracle\ast\OracleDataTypeTimestamp.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
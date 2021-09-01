/*    */ package com.tranboot.client.druid.sql.dialect.oracle.ast.stmt;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLName;
import com.tranboot.client.druid.sql.ast.SQLObject;
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
/*    */ 
/*    */ public class OracleSavePointStatement
/*    */   extends OracleStatementImpl
/*    */ {
/*    */   private SQLName to;
/*    */   
/*    */   public void accept0(OracleASTVisitor visitor) {
/* 27 */     if (visitor.visit(this)) {
/* 28 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.to);
/*    */     }
/* 30 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public SQLName getTo() {
/* 34 */     return this.to;
/*    */   }
/*    */   
/*    */   public void setTo(SQLName to) {
/* 38 */     this.to = to;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\oracle\ast\stmt\OracleSavePointStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
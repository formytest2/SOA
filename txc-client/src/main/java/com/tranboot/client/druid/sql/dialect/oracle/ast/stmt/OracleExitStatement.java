/*    */ package com.tranboot.client.druid.sql.dialect.oracle.ast.stmt;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLExpr;
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
/*    */ public class OracleExitStatement
/*    */   extends OracleStatementImpl
/*    */ {
/*    */   private SQLExpr when;
/*    */   
/*    */   public SQLExpr getWhen() {
/* 26 */     return this.when;
/*    */   }
/*    */   
/*    */   public void setWhen(SQLExpr when) {
/* 30 */     this.when = when;
/*    */   }
/*    */ 
/*    */   
/*    */   public void accept0(OracleASTVisitor visitor) {
/* 35 */     if (visitor.visit(this)) {
/* 36 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.when);
/*    */     }
/* 38 */     visitor.endVisit(this);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\oracle\ast\stmt\OracleExitStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
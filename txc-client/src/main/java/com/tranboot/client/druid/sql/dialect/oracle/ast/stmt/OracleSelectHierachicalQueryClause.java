/*    */ package com.tranboot.client.druid.sql.dialect.oracle.ast.stmt;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.dialect.oracle.ast.OracleSQLObjectImpl;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class OracleSelectHierachicalQueryClause
/*    */   extends OracleSQLObjectImpl
/*    */ {
/*    */   private SQLExpr startWith;
/*    */   private SQLExpr connectBy;
/*    */   private boolean prior = false;
/*    */   private boolean noCycle = false;
/*    */   
/*    */   public boolean isPrior() {
/* 34 */     return this.prior;
/*    */   }
/*    */   
/*    */   public void setPrior(boolean prior) {
/* 38 */     this.prior = prior;
/*    */   }
/*    */   
/*    */   public void accept0(OracleASTVisitor visitor) {
/* 42 */     if (visitor.visit(this)) {
/* 43 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.startWith);
/* 44 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.connectBy);
/*    */     } 
/*    */     
/* 47 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public SQLExpr getStartWith() {
/* 51 */     return this.startWith;
/*    */   }
/*    */   
/*    */   public void setStartWith(SQLExpr startWith) {
/* 55 */     this.startWith = startWith;
/*    */   }
/*    */   
/*    */   public SQLExpr getConnectBy() {
/* 59 */     return this.connectBy;
/*    */   }
/*    */   
/*    */   public void setConnectBy(SQLExpr connectBy) {
/* 63 */     this.connectBy = connectBy;
/*    */   }
/*    */   
/*    */   public boolean isNoCycle() {
/* 67 */     return this.noCycle;
/*    */   }
/*    */   
/*    */   public void setNoCycle(boolean noCycle) {
/* 71 */     this.noCycle = noCycle;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\oracle\ast\stmt\OracleSelectHierachicalQueryClause.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
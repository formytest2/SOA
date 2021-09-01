/*    */ package com.tranboot.client.druid.sql.dialect.oracle.ast.expr;
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
/*    */ public class OracleRangeExpr
/*    */   extends OracleSQLObjectImpl
/*    */   implements SQLExpr
/*    */ {
/*    */   private SQLExpr lowBound;
/*    */   private SQLExpr upBound;
/*    */   
/*    */   public OracleRangeExpr() {}
/*    */   
/*    */   public OracleRangeExpr(SQLExpr lowBound, SQLExpr upBound) {
/* 32 */     this.lowBound = lowBound;
/* 33 */     this.upBound = upBound;
/*    */   }
/*    */ 
/*    */   
/*    */   public void accept0(OracleASTVisitor visitor) {
/* 38 */     if (visitor.visit(this)) {
/* 39 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.lowBound);
/* 40 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.upBound);
/*    */     } 
/* 42 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public SQLExpr getLowBound() {
/* 46 */     return this.lowBound;
/*    */   }
/*    */   
/*    */   public void setLowBound(SQLExpr lowBound) {
/* 50 */     this.lowBound = lowBound;
/*    */   }
/*    */   
/*    */   public SQLExpr getUpBound() {
/* 54 */     return this.upBound;
/*    */   }
/*    */   
/*    */   public void setUpBound(SQLExpr upBound) {
/* 58 */     this.upBound = upBound;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\oracle\ast\expr\OracleRangeExpr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
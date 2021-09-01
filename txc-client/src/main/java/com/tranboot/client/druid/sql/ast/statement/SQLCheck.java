/*    */ package com.tranboot.client.druid.sql.ast.statement;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLObject;
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
/*    */ public class SQLCheck
/*    */   extends SQLConstraintImpl
/*    */   implements SQLTableElement
/*    */ {
/*    */   private SQLExpr expr;
/*    */   
/*    */   public SQLCheck() {}
/*    */   
/*    */   public SQLCheck(SQLExpr expr) {
/* 30 */     setExpr(expr);
/*    */   }
/*    */   
/*    */   public SQLExpr getExpr() {
/* 34 */     return this.expr;
/*    */   }
/*    */   
/*    */   public void setExpr(SQLExpr expr) {
/* 38 */     if (expr != null) {
/* 39 */       expr.setParent(this);
/*    */     }
/* 41 */     this.expr = expr;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 46 */     if (visitor.visit(this)) {
/* 47 */       acceptChild(visitor, (SQLObject)getName());
/* 48 */       acceptChild(visitor, (SQLObject)getExpr());
/*    */     } 
/* 50 */     visitor.endVisit(this);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\statement\SQLCheck.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
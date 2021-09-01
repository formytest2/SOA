/*    */ package com.tranboot.client.druid.sql.ast.statement;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLHint;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.SQLObjectImpl;
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
/*    */ public class SQLExprHint
/*    */   extends SQLObjectImpl
/*    */   implements SQLHint
/*    */ {
/*    */   private SQLExpr expr;
/*    */   
/*    */   public SQLExprHint() {}
/*    */   
/*    */   public SQLExprHint(SQLExpr expr) {
/* 32 */     setExpr(expr);
/*    */   }
/*    */   
/*    */   public SQLExpr getExpr() {
/* 36 */     return this.expr;
/*    */   }
/*    */   
/*    */   public void setExpr(SQLExpr expr) {
/* 40 */     if (expr != null) {
/* 41 */       expr.setParent((SQLObject)this);
/*    */     }
/*    */     
/* 44 */     this.expr = expr;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 49 */     if (visitor.visit(this)) {
/* 50 */       acceptChild(visitor, (SQLObject)this.expr);
/*    */     }
/* 52 */     visitor.endVisit(this);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\statement\SQLExprHint.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
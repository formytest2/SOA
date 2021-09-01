/*    */ package com.tranboot.client.druid.sql.dialect.oracle.ast.expr;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.SQLObjectImpl;
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
/*    */ 
/*    */ public class OracleAnalyticWindowing
/*    */   extends SQLObjectImpl
/*    */   implements OracleExpr
/*    */ {
/*    */   private Type type;
/*    */   private SQLExpr expr;
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 34 */     accept0((OracleASTVisitor)visitor);
/*    */   }
/*    */   
/*    */   public void accept0(OracleASTVisitor visitor) {
/* 38 */     if (visitor.visit(this)) {
/* 39 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.expr);
/*    */     }
/* 41 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public SQLExpr getExpr() {
/* 45 */     return this.expr;
/*    */   }
/*    */   
/*    */   public void setExpr(SQLExpr expr) {
/* 49 */     this.expr = expr;
/*    */   }
/*    */   
/*    */   public Type getType() {
/* 53 */     return this.type;
/*    */   }
/*    */   
/*    */   public void setType(Type type) {
/* 57 */     this.type = type;
/*    */   }
/*    */   
/*    */   public enum Type {
/* 61 */     ROWS, RANGE;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\oracle\ast\expr\OracleAnalyticWindowing.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
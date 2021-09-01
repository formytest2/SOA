/*    */ package com.tranboot.client.druid.sql.ast.expr;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLExprImpl;
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
/*    */ public class SQLBooleanExpr
/*    */   extends SQLExprImpl
/*    */   implements SQLExpr, SQLLiteralExpr
/*    */ {
/*    */   private boolean value;
/*    */   
/*    */   public SQLBooleanExpr() {}
/*    */   
/*    */   public SQLBooleanExpr(boolean value) {
/* 31 */     this.value = value;
/*    */   }
/*    */   
/*    */   public boolean getValue() {
/* 35 */     return this.value;
/*    */   }
/*    */   
/*    */   public void setValue(boolean value) {
/* 39 */     this.value = value;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 44 */     visitor.visit(this);
/*    */     
/* 46 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public void output(StringBuffer buf) {
/* 50 */     buf.append("x");
/* 51 */     buf.append(this.value ? "TRUE" : "FALSE");
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 56 */     int prime = 31;
/* 57 */     int result = 1;
/* 58 */     result = 31 * result + (this.value ? 1231 : 1237);
/* 59 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 64 */     if (this == obj) {
/* 65 */       return true;
/*    */     }
/* 67 */     if (obj == null) {
/* 68 */       return false;
/*    */     }
/* 70 */     if (getClass() != obj.getClass()) {
/* 71 */       return false;
/*    */     }
/* 73 */     SQLBooleanExpr other = (SQLBooleanExpr)obj;
/* 74 */     if (this.value != other.value) {
/* 75 */       return false;
/*    */     }
/* 77 */     return true;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\expr\SQLBooleanExpr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
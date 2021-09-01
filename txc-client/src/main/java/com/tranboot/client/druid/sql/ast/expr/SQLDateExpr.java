/*    */ package com.tranboot.client.druid.sql.ast.expr;
/*    */ 
/*    */

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
/*    */ 
/*    */ public class SQLDateExpr
/*    */   extends SQLExprImpl
/*    */   implements SQLLiteralExpr
/*    */ {
/*    */   private String literal;
/*    */   
/*    */   public String getLiteral() {
/* 30 */     return this.literal;
/*    */   }
/*    */   
/*    */   public void setLiteral(String literal) {
/* 34 */     this.literal = literal;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 39 */     visitor.visit(this);
/* 40 */     visitor.endVisit(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 45 */     int prime = 31;
/* 46 */     int result = 1;
/* 47 */     result = 31 * result + ((this.literal == null) ? 0 : this.literal.hashCode());
/* 48 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 53 */     if (this == obj) {
/* 54 */       return true;
/*    */     }
/* 56 */     if (obj == null) {
/* 57 */       return false;
/*    */     }
/* 59 */     if (getClass() != obj.getClass()) {
/* 60 */       return false;
/*    */     }
/* 62 */     SQLDateExpr other = (SQLDateExpr)obj;
/* 63 */     if (this.literal == null) {
/* 64 */       if (other.literal != null) {
/* 65 */         return false;
/*    */       }
/* 67 */     } else if (!this.literal.equals(other.literal)) {
/* 68 */       return false;
/*    */     } 
/* 70 */     return true;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\expr\SQLDateExpr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
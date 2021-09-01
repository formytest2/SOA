/*    */ package com.tranboot.client.druid.sql.ast.expr;
/*    */ 
/*    */ import com.tranboot.client.druid.sql.visitor.SQLASTVisitor;

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
/*    */ public class SQLNumberExpr
/*    */   extends SQLNumericLiteralExpr
/*    */ {
/*    */   private Number number;
/*    */   
/*    */   public SQLNumberExpr() {}
/*    */   
/*    */   public SQLNumberExpr(Number number) {
/* 30 */     this.number = number;
/*    */   }
/*    */   
/*    */   public Number getNumber() {
/* 34 */     return this.number;
/*    */   }
/*    */   
/*    */   public void setNumber(Number number) {
/* 38 */     this.number = number;
/*    */   }
/*    */   
/*    */   public void output(StringBuffer buf) {
/* 42 */     buf.append(this.number.toString());
/*    */   }
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 46 */     visitor.visit(this);
/* 47 */     visitor.endVisit(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 52 */     int prime = 31;
/* 53 */     int result = 1;
/* 54 */     result = 31 * result + ((this.number == null) ? 0 : this.number.hashCode());
/* 55 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 60 */     if (this == obj) {
/* 61 */       return true;
/*    */     }
/* 63 */     if (obj == null) {
/* 64 */       return false;
/*    */     }
/* 66 */     if (getClass() != obj.getClass()) {
/* 67 */       return false;
/*    */     }
/* 69 */     SQLNumberExpr other = (SQLNumberExpr)obj;
/* 70 */     if (this.number == null) {
/* 71 */       if (other.number != null) {
/* 72 */         return false;
/*    */       }
/* 74 */     } else if (!this.number.equals(other.number)) {
/* 75 */       return false;
/*    */     } 
/* 77 */     return true;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\expr\SQLNumberExpr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
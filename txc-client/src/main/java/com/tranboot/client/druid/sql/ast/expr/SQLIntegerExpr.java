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
/*    */ public class SQLIntegerExpr
/*    */   extends SQLNumericLiteralExpr
/*    */   implements SQLValuableExpr
/*    */ {
/*    */   private Number number;
/*    */   
/*    */   public SQLIntegerExpr(Number number) {
/* 26 */     this.number = number;
/*    */   }
/*    */ 
/*    */   
/*    */   public SQLIntegerExpr() {}
/*    */ 
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
/* 42 */     buf.append(this.number);
/*    */   }
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 46 */     visitor.visit(this);
/*    */     
/* 48 */     visitor.endVisit(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 53 */     int prime = 31;
/* 54 */     int result = 1;
/* 55 */     result = 31 * result + ((this.number == null) ? 0 : this.number.hashCode());
/* 56 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 61 */     if (this == obj) {
/* 62 */       return true;
/*    */     }
/* 64 */     if (obj == null) {
/* 65 */       return false;
/*    */     }
/* 67 */     if (getClass() != obj.getClass()) {
/* 68 */       return false;
/*    */     }
/* 70 */     SQLIntegerExpr other = (SQLIntegerExpr)obj;
/* 71 */     if (this.number == null) {
/* 72 */       if (other.number != null) {
/* 73 */         return false;
/*    */       }
/* 75 */     } else if (!this.number.equals(other.number)) {
/* 76 */       return false;
/*    */     } 
/* 78 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public Object getValue() {
/* 83 */     return this.number;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\expr\SQLIntegerExpr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
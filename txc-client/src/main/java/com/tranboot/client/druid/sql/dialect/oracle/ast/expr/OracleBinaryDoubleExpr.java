/*    */ package com.tranboot.client.druid.sql.dialect.oracle.ast.expr;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.expr.SQLNumericLiteralExpr;
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
/*    */ public class OracleBinaryDoubleExpr
/*    */   extends SQLNumericLiteralExpr
/*    */   implements OracleExpr
/*    */ {
/*    */   private Double value;
/*    */   
/*    */   public OracleBinaryDoubleExpr() {}
/*    */   
/*    */   public OracleBinaryDoubleExpr(Double value) {
/* 32 */     this.value = value;
/*    */   }
/*    */ 
/*    */   
/*    */   public Number getNumber() {
/* 37 */     return this.value;
/*    */   }
/*    */   
/*    */   public Double getValue() {
/* 41 */     return this.value;
/*    */   }
/*    */   
/*    */   public void setValue(Double value) {
/* 45 */     this.value = value;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 50 */     accept0((OracleASTVisitor)visitor);
/*    */   }
/*    */   
/*    */   public void accept0(OracleASTVisitor visitor) {
/* 54 */     visitor.visit(this);
/* 55 */     visitor.endVisit(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 60 */     int prime = 31;
/* 61 */     int result = 1;
/* 62 */     result = 31 * result + ((this.value == null) ? 0 : this.value.hashCode());
/* 63 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 68 */     if (this == obj) {
/* 69 */       return true;
/*    */     }
/* 71 */     if (obj == null) {
/* 72 */       return false;
/*    */     }
/* 74 */     if (getClass() != obj.getClass()) {
/* 75 */       return false;
/*    */     }
/* 77 */     OracleBinaryDoubleExpr other = (OracleBinaryDoubleExpr)obj;
/* 78 */     if (this.value == null) {
/* 79 */       if (other.value != null) {
/* 80 */         return false;
/*    */       }
/* 82 */     } else if (!this.value.equals(other.value)) {
/* 83 */       return false;
/*    */     } 
/* 85 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setNumber(Number number) {
/* 90 */     if (number == null) {
/* 91 */       setValue(null);
/*    */       
/*    */       return;
/*    */     } 
/* 95 */     setValue(Double.valueOf(number.doubleValue()));
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\oracle\ast\expr\OracleBinaryDoubleExpr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
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
/*    */ public class SQLBinaryExpr
/*    */   extends SQLExprImpl
/*    */   implements SQLLiteralExpr
/*    */ {
/*    */   private String value;
/*    */   
/*    */   public SQLBinaryExpr() {}
/*    */   
/*    */   public SQLBinaryExpr(String value) {
/* 31 */     this.value = value;
/*    */   }
/*    */   
/*    */   public String getValue() {
/* 35 */     return this.value;
/*    */   }
/*    */   
/*    */   public void setValue(String value) {
/* 39 */     this.value = value;
/*    */   }
/*    */   
/*    */   public void accept0(SQLASTVisitor visitor) {
/* 43 */     visitor.visit(this);
/*    */     
/* 45 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public void output(StringBuffer buf) {
/* 49 */     buf.append("b'");
/* 50 */     buf.append(this.value);
/* 51 */     buf.append('\'');
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 56 */     int prime = 31;
/* 57 */     int result = 1;
/* 58 */     result = 31 * result + ((this.value == null) ? 0 : this.value.hashCode());
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
/* 73 */     SQLBinaryExpr other = (SQLBinaryExpr)obj;
/* 74 */     if (this.value == null) {
/* 75 */       if (other.value != null) {
/* 76 */         return false;
/*    */       }
/* 78 */     } else if (!this.value.equals(other.value)) {
/* 79 */       return false;
/*    */     } 
/* 81 */     return true;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\expr\SQLBinaryExpr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
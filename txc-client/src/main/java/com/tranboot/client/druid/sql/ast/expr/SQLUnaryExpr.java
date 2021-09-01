/*    */ package com.tranboot.client.druid.sql.ast.expr;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLExprImpl;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.visitor.SQLASTVisitor;

import java.io.Serializable;

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
/*    */ public class SQLUnaryExpr
/*    */   extends SQLExprImpl
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private SQLExpr expr;
/*    */   private SQLUnaryOperator operator;
/*    */   
/*    */   public SQLUnaryExpr() {}
/*    */   
/*    */   public SQLUnaryExpr(SQLUnaryOperator operator, SQLExpr expr) {
/* 35 */     this.operator = operator;
/* 36 */     setExpr(expr);
/*    */   }
/*    */   
/*    */   public SQLUnaryOperator getOperator() {
/* 40 */     return this.operator;
/*    */   }
/*    */   
/*    */   public void setOperator(SQLUnaryOperator operator) {
/* 44 */     this.operator = operator;
/*    */   }
/*    */   
/*    */   public SQLExpr getExpr() {
/* 48 */     return this.expr;
/*    */   }
/*    */   
/*    */   public void setExpr(SQLExpr expr) {
/* 52 */     if (expr != null) {
/* 53 */       expr.setParent((SQLObject)this);
/*    */     }
/* 55 */     this.expr = expr;
/*    */   }
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 59 */     if (visitor.visit(this)) {
/* 60 */       acceptChild(visitor, (SQLObject)this.expr);
/*    */     }
/*    */     
/* 63 */     visitor.endVisit(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 68 */     int prime = 31;
/* 69 */     int result = 1;
/* 70 */     result = 31 * result + ((this.expr == null) ? 0 : this.expr.hashCode());
/* 71 */     result = 31 * result + ((this.operator == null) ? 0 : this.operator.hashCode());
/* 72 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 77 */     if (this == obj) {
/* 78 */       return true;
/*    */     }
/* 80 */     if (obj == null) {
/* 81 */       return false;
/*    */     }
/* 83 */     if (getClass() != obj.getClass()) {
/* 84 */       return false;
/*    */     }
/* 86 */     SQLUnaryExpr other = (SQLUnaryExpr)obj;
/* 87 */     if (this.expr == null) {
/* 88 */       if (other.expr != null) {
/* 89 */         return false;
/*    */       }
/* 91 */     } else if (!this.expr.equals(other.expr)) {
/* 92 */       return false;
/*    */     } 
/* 94 */     if (this.operator != other.operator) {
/* 95 */       return false;
/*    */     }
/* 97 */     return true;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\expr\SQLUnaryExpr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
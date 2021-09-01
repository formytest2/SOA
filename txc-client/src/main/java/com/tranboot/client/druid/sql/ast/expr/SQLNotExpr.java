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
/*    */ 
/*    */ public class SQLNotExpr
/*    */   extends SQLExprImpl
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   public SQLExpr expr;
/*    */   
/*    */   public SQLNotExpr() {}
/*    */   
/*    */   public SQLNotExpr(SQLExpr expr) {
/* 35 */     this.expr = expr;
/*    */   }
/*    */   
/*    */   public SQLExpr getExpr() {
/* 39 */     return this.expr;
/*    */   }
/*    */   
/*    */   public void setExpr(SQLExpr expr) {
/* 43 */     this.expr = expr;
/*    */   }
/*    */ 
/*    */   
/*    */   public void output(StringBuffer buf) {
/* 48 */     buf.append(" NOT ");
/* 49 */     this.expr.output(buf);
/*    */   }
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 53 */     if (visitor.visit(this)) {
/* 54 */       acceptChild(visitor, (SQLObject)this.expr);
/*    */     }
/*    */     
/* 57 */     visitor.endVisit(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 62 */     int prime = 31;
/* 63 */     int result = 1;
/* 64 */     result = 31 * result + ((this.expr == null) ? 0 : this.expr.hashCode());
/* 65 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 70 */     if (this == obj) {
/* 71 */       return true;
/*    */     }
/* 73 */     if (obj == null) {
/* 74 */       return false;
/*    */     }
/* 76 */     if (getClass() != obj.getClass()) {
/* 77 */       return false;
/*    */     }
/* 79 */     SQLNotExpr other = (SQLNotExpr)obj;
/* 80 */     if (this.expr == null) {
/* 81 */       if (other.expr != null) {
/* 82 */         return false;
/*    */       }
/* 84 */     } else if (!this.expr.equals(other.expr)) {
/* 85 */       return false;
/*    */     } 
/* 87 */     return true;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\expr\SQLNotExpr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
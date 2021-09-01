/*    */ package com.tranboot.client.druid.sql.ast.expr;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLDataType;
import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLExprImpl;
import com.tranboot.client.druid.sql.ast.SQLObject;
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
/*    */ public class SQLCastExpr
/*    */   extends SQLExprImpl
/*    */ {
/*    */   protected SQLExpr expr;
/*    */   protected SQLDataType dataType;
/*    */   
/*    */   public SQLExpr getExpr() {
/* 33 */     return this.expr;
/*    */   }
/*    */   
/*    */   public void setExpr(SQLExpr expr) {
/* 37 */     if (expr != null) {
/* 38 */       expr.setParent((SQLObject)this);
/*    */     }
/* 40 */     this.expr = expr;
/*    */   }
/*    */   
/*    */   public SQLDataType getDataType() {
/* 44 */     return this.dataType;
/*    */   }
/*    */   
/*    */   public void setDataType(SQLDataType dataType) {
/* 48 */     this.dataType = dataType;
/*    */   }
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 52 */     if (visitor.visit(this)) {
/* 53 */       acceptChild(visitor, (SQLObject)this.expr);
/* 54 */       acceptChild(visitor, (SQLObject)this.dataType);
/*    */     } 
/* 56 */     visitor.endVisit(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 61 */     int prime = 31;
/* 62 */     int result = 1;
/* 63 */     result = 31 * result + ((this.dataType == null) ? 0 : this.dataType.hashCode());
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
/* 79 */     SQLCastExpr other = (SQLCastExpr)obj;
/* 80 */     if (this.dataType == null) {
/* 81 */       if (other.dataType != null) {
/* 82 */         return false;
/*    */       }
/* 84 */     } else if (!this.dataType.equals(other.dataType)) {
/* 85 */       return false;
/*    */     } 
/* 87 */     if (this.expr == null) {
/* 88 */       if (other.expr != null) {
/* 89 */         return false;
/*    */       }
/* 91 */     } else if (!this.expr.equals(other.expr)) {
/* 92 */       return false;
/*    */     } 
/* 94 */     return true;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\expr\SQLCastExpr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
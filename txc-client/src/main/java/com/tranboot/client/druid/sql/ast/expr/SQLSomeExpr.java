/*    */ package com.tranboot.client.druid.sql.ast.expr;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLExprImpl;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.statement.SQLSelect;
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
/*    */ public class SQLSomeExpr
/*    */   extends SQLExprImpl
/*    */ {
/*    */   public SQLSelect subQuery;
/*    */   
/*    */   public SQLSomeExpr() {}
/*    */   
/*    */   public SQLSomeExpr(SQLSelect select) {
/* 32 */     this.subQuery = select;
/*    */   }
/*    */   
/*    */   public SQLSelect getSubQuery() {
/* 36 */     return this.subQuery;
/*    */   }
/*    */   
/*    */   public void setSubQuery(SQLSelect subQuery) {
/* 40 */     this.subQuery = subQuery;
/*    */   }
/*    */   
/*    */   public void output(StringBuffer buf) {
/* 44 */     this.subQuery.output(buf);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 49 */     if (visitor.visit(this)) {
/* 50 */       acceptChild(visitor, (SQLObject)this.subQuery);
/*    */     }
/*    */     
/* 53 */     visitor.endVisit(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 58 */     int prime = 31;
/* 59 */     int result = 1;
/* 60 */     result = 31 * result + ((this.subQuery == null) ? 0 : this.subQuery.hashCode());
/* 61 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 66 */     if (this == obj) {
/* 67 */       return true;
/*    */     }
/* 69 */     if (obj == null) {
/* 70 */       return false;
/*    */     }
/* 72 */     if (getClass() != obj.getClass()) {
/* 73 */       return false;
/*    */     }
/* 75 */     SQLSomeExpr other = (SQLSomeExpr)obj;
/* 76 */     if (this.subQuery == null) {
/* 77 */       if (other.subQuery != null) {
/* 78 */         return false;
/*    */       }
/* 80 */     } else if (!this.subQuery.equals(other.subQuery)) {
/* 81 */       return false;
/*    */     } 
/* 83 */     return true;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\expr\SQLSomeExpr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
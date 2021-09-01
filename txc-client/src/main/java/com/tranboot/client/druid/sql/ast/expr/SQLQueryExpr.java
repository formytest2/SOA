/*    */ package com.tranboot.client.druid.sql.ast.expr;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLExprImpl;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.statement.SQLSelect;
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
/*    */ public class SQLQueryExpr
/*    */   extends SQLExprImpl
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   public SQLSelect subQuery;
/*    */   
/*    */   public SQLQueryExpr() {}
/*    */   
/*    */   public SQLQueryExpr(SQLSelect select) {
/* 34 */     setSubQuery(select);
/*    */   }
/*    */   
/*    */   public SQLSelect getSubQuery() {
/* 38 */     return this.subQuery;
/*    */   }
/*    */   
/*    */   public void setSubQuery(SQLSelect subQuery) {
/* 42 */     if (subQuery != null) {
/* 43 */       subQuery.setParent((SQLObject)this);
/*    */     }
/* 45 */     this.subQuery = subQuery;
/*    */   }
/*    */   
/*    */   public void output(StringBuffer buf) {
/* 49 */     this.subQuery.output(buf);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 54 */     if (visitor.visit(this)) {
/* 55 */       acceptChild(visitor, (SQLObject)this.subQuery);
/*    */     }
/*    */     
/* 58 */     visitor.endVisit(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 63 */     int prime = 31;
/* 64 */     int result = 1;
/* 65 */     result = 31 * result + ((this.subQuery == null) ? 0 : this.subQuery.hashCode());
/* 66 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 71 */     if (this == obj) {
/* 72 */       return true;
/*    */     }
/* 74 */     if (obj == null) {
/* 75 */       return false;
/*    */     }
/* 77 */     if (getClass() != obj.getClass()) {
/* 78 */       return false;
/*    */     }
/* 80 */     SQLQueryExpr other = (SQLQueryExpr)obj;
/* 81 */     if (this.subQuery == null) {
/* 82 */       if (other.subQuery != null) {
/* 83 */         return false;
/*    */       }
/* 85 */     } else if (!this.subQuery.equals(other.subQuery)) {
/* 86 */       return false;
/*    */     } 
/* 88 */     return true;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\expr\SQLQueryExpr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
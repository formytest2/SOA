/*    */ package com.tranboot.client.druid.sql.dialect.oracle.ast.expr;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLExprImpl;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.statement.SQLSelect;
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
/*    */ 
/*    */ public class OracleCursorExpr
/*    */   extends SQLExprImpl
/*    */   implements OracleExpr
/*    */ {
/*    */   private SQLSelect query;
/*    */   
/*    */   public OracleCursorExpr() {}
/*    */   
/*    */   public OracleCursorExpr(SQLSelect query) {
/* 33 */     this.query = query;
/*    */   }
/*    */   
/*    */   public SQLSelect getQuery() {
/* 37 */     return this.query;
/*    */   }
/*    */   
/*    */   public void setQuery(SQLSelect query) {
/* 41 */     this.query = query;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 46 */     accept0((OracleASTVisitor)visitor);
/*    */   }
/*    */   
/*    */   public void accept0(OracleASTVisitor visitor) {
/* 50 */     if (visitor.visit(this)) {
/* 51 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.query);
/*    */     }
/* 53 */     visitor.endVisit(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 58 */     int prime = 31;
/* 59 */     int result = 1;
/* 60 */     result = 31 * result + ((this.query == null) ? 0 : this.query.hashCode());
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
/* 75 */     OracleCursorExpr other = (OracleCursorExpr)obj;
/* 76 */     if (this.query == null) {
/* 77 */       if (other.query != null) {
/* 78 */         return false;
/*    */       }
/* 80 */     } else if (!this.query.equals(other.query)) {
/* 81 */       return false;
/*    */     } 
/* 83 */     return true;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\oracle\ast\expr\OracleCursorExpr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
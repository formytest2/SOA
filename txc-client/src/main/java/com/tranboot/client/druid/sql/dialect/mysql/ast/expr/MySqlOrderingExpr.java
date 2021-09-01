/*    */ package com.tranboot.client.druid.sql.dialect.mysql.ast.expr;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLExprImpl;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.SQLOrderingSpecification;
import com.tranboot.client.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
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
/*    */ public class MySqlOrderingExpr
/*    */   extends SQLExprImpl
/*    */   implements MySqlExpr
/*    */ {
/*    */   protected SQLExpr expr;
/*    */   protected SQLOrderingSpecification type;
/*    */   
/*    */   public MySqlOrderingExpr() {}
/*    */   
/*    */   public MySqlOrderingExpr(SQLExpr expr, SQLOrderingSpecification type) {
/* 35 */     this.expr = expr;
/* 36 */     this.type = type;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 41 */     MySqlASTVisitor mysqlVisitor = (MySqlASTVisitor)visitor;
/* 42 */     if (mysqlVisitor.visit(this)) {
/* 43 */       acceptChild(visitor, (SQLObject)this.expr);
/*    */     }
/*    */     
/* 46 */     mysqlVisitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public SQLExpr getExpr() {
/* 50 */     return this.expr;
/*    */   }
/*    */   
/*    */   public void setExpr(SQLExpr expr) {
/* 54 */     expr.setParent((SQLObject)this);
/* 55 */     this.expr = expr;
/*    */   }
/*    */   
/*    */   public SQLOrderingSpecification getType() {
/* 59 */     return this.type;
/*    */   }
/*    */   
/*    */   public void setType(SQLOrderingSpecification type) {
/* 63 */     this.type = type;
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
/* 77 */     MySqlOrderingExpr other = (MySqlOrderingExpr)obj;
/* 78 */     if (this.expr != other.expr) {
/* 79 */       return false;
/*    */     }
/* 81 */     if (this.type == null) {
/* 82 */       if (other.type != null) {
/* 83 */         return false;
/*    */       }
/* 85 */     } else if (!this.type.equals(other.type)) {
/* 86 */       return false;
/*    */     } 
/* 88 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 93 */     int prime = 31;
/* 94 */     int result = 1;
/* 95 */     result = 31 * result + ((this.expr == null) ? 0 : this.expr.hashCode());
/* 96 */     result = 31 * result + ((this.type == null) ? 0 : this.type.hashCode());
/* 97 */     return result;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\ast\expr\MySqlOrderingExpr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
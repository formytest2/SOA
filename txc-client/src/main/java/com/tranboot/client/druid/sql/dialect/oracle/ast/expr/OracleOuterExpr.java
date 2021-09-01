/*    */ package com.tranboot.client.druid.sql.dialect.oracle.ast.expr;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLExprImpl;
import com.tranboot.client.druid.sql.ast.SQLObject;
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
/*    */ public class OracleOuterExpr
/*    */   extends SQLExprImpl
/*    */   implements OracleExpr
/*    */ {
/*    */   private SQLExpr expr;
/*    */   
/*    */   public OracleOuterExpr() {}
/*    */   
/*    */   public OracleOuterExpr(SQLExpr expr) {
/* 33 */     this.expr = expr;
/*    */   }
/*    */   
/*    */   public SQLExpr getExpr() {
/* 37 */     return this.expr;
/*    */   }
/*    */   
/*    */   public void setExpr(SQLExpr expr) {
/* 41 */     this.expr = expr;
/*    */   }
/*    */   
/*    */   public void output(StringBuffer buf) {
/* 45 */     this.expr.output(buf);
/* 46 */     buf.append("(+)");
/*    */   }
/*    */ 
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 51 */     accept0((OracleASTVisitor)visitor);
/*    */   }
/*    */   
/*    */   public void accept0(OracleASTVisitor visitor) {
/* 55 */     if (visitor.visit(this)) {
/* 56 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.expr);
/*    */     }
/*    */     
/* 59 */     visitor.endVisit(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 64 */     int prime = 31;
/* 65 */     int result = 1;
/* 66 */     result = 31 * result + ((this.expr == null) ? 0 : this.expr.hashCode());
/* 67 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 72 */     if (this == obj) {
/* 73 */       return true;
/*    */     }
/* 75 */     if (obj == null) {
/* 76 */       return false;
/*    */     }
/* 78 */     if (getClass() != obj.getClass()) {
/* 79 */       return false;
/*    */     }
/* 81 */     OracleOuterExpr other = (OracleOuterExpr)obj;
/* 82 */     if (this.expr == null) {
/* 83 */       if (other.expr != null) {
/* 84 */         return false;
/*    */       }
/* 86 */     } else if (!this.expr.equals(other.expr)) {
/* 87 */       return false;
/*    */     } 
/* 89 */     return true;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\oracle\ast\expr\OracleOuterExpr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
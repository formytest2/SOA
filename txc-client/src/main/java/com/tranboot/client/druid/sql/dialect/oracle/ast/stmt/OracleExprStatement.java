/*    */ package com.tranboot.client.druid.sql.dialect.oracle.ast.stmt;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLExpr;
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
/*    */ public class OracleExprStatement
/*    */   extends OracleStatementImpl
/*    */ {
/*    */   private SQLExpr expr;
/*    */   
/*    */   public OracleExprStatement() {}
/*    */   
/*    */   public OracleExprStatement(SQLExpr expr) {
/* 30 */     this.expr = expr;
/*    */   }
/*    */ 
/*    */   
/*    */   public void accept0(OracleASTVisitor visitor) {
/* 35 */     if (visitor.visit(this)) {
/* 36 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.expr);
/*    */     }
/* 38 */     visitor.endVisit(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public SQLExpr getExpr() {
/* 43 */     return this.expr;
/*    */   }
/*    */   
/*    */   public void setExpr(SQLExpr expr) {
/* 47 */     this.expr = expr;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\oracle\ast\stmt\OracleExprStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
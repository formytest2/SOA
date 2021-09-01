/*    */ package com.tranboot.client.druid.sql.dialect.oracle.ast.stmt;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.SQLUtils;
import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.expr.SQLCharExpr;
import com.tranboot.client.druid.sql.ast.statement.SQLExplainStatement;
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
/*    */ public class OracleExplainStatement
/*    */   extends SQLExplainStatement
/*    */   implements OracleStatement
/*    */ {
/*    */   private SQLCharExpr statementId;
/*    */   private SQLExpr into;
/*    */   
/*    */   public OracleExplainStatement() {
/* 32 */     super("oracle");
/*    */   }
/*    */ 
/*    */   
/*    */   public void accept0(OracleASTVisitor visitor) {
/* 37 */     if (visitor.visit(this)) {
/* 38 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.statementId);
/* 39 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.into);
/* 40 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.statement);
/*    */     } 
/* 42 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 46 */     accept0((OracleASTVisitor)visitor);
/*    */   }
/*    */   
/*    */   public String toString() {
/* 50 */     return SQLUtils.toOracleString((SQLObject)this);
/*    */   }
/*    */   
/*    */   public SQLCharExpr getStatementId() {
/* 54 */     return this.statementId;
/*    */   }
/*    */   
/*    */   public void setStatementId(SQLCharExpr statementId) {
/* 58 */     this.statementId = statementId;
/*    */   }
/*    */   
/*    */   public SQLExpr getInto() {
/* 62 */     return this.into;
/*    */   }
/*    */   
/*    */   public void setInto(SQLExpr into) {
/* 66 */     this.into = into;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\oracle\ast\stmt\OracleExplainStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
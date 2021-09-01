/*    */ package com.tranboot.client.druid.sql.dialect.oracle.ast.stmt;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLName;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.SQLStatement;
import com.tranboot.client.druid.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.tranboot.client.druid.sql.visitor.SQLASTVisitor;

import java.util.ArrayList;
import java.util.List;

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
/*    */ 
/*    */ public class OracleForStatement
/*    */   extends OracleStatementImpl
/*    */ {
/*    */   private SQLName index;
/*    */   private SQLExpr range;
/* 32 */   private List<SQLStatement> statements = new ArrayList<>();
/*    */ 
/*    */   
/*    */   public void accept0(OracleASTVisitor visitor) {
/* 36 */     if (visitor.visit(this)) {
/* 37 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.index);
/* 38 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.range);
/* 39 */       acceptChild((SQLASTVisitor)visitor, this.statements);
/*    */     } 
/* 41 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public SQLName getIndex() {
/* 45 */     return this.index;
/*    */   }
/*    */   
/*    */   public void setIndex(SQLName index) {
/* 49 */     this.index = index;
/*    */   }
/*    */   
/*    */   public SQLExpr getRange() {
/* 53 */     return this.range;
/*    */   }
/*    */   
/*    */   public void setRange(SQLExpr range) {
/* 57 */     this.range = range;
/*    */   }
/*    */   
/*    */   public List<SQLStatement> getStatements() {
/* 61 */     return this.statements;
/*    */   }
/*    */   
/*    */   public void setStatements(List<SQLStatement> statements) {
/* 65 */     this.statements = statements;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\oracle\ast\stmt\OracleForStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
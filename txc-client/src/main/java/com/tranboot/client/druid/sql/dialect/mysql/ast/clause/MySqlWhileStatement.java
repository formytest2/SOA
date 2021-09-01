/*    */ package com.tranboot.client.druid.sql.dialect.mysql.ast.clause;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.SQLStatement;
import com.tranboot.client.druid.sql.dialect.mysql.ast.statement.MySqlStatementImpl;
import com.tranboot.client.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MySqlWhileStatement
/*    */   extends MySqlStatementImpl
/*    */ {
/*    */   private SQLExpr condition;
/* 34 */   private List<SQLStatement> statements = new ArrayList<>();
/*    */   
/*    */   private String labelName;
/*    */ 
/*    */   
/*    */   public String getLabelName() {
/* 40 */     return this.labelName;
/*    */   }
/*    */   
/*    */   public void setLabelName(String labelName) {
/* 44 */     this.labelName = labelName;
/*    */   }
/*    */ 
/*    */   
/*    */   public void accept0(MySqlASTVisitor visitor) {
/* 49 */     if (visitor.visit(this)) {
/* 50 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.condition);
/* 51 */       acceptChild((SQLASTVisitor)visitor, this.statements);
/*    */     } 
/* 53 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public List<SQLStatement> getStatements() {
/* 57 */     return this.statements;
/*    */   }
/*    */   
/*    */   public void setStatements(List<SQLStatement> statements) {
/* 61 */     this.statements = statements;
/*    */   }
/*    */   public SQLExpr getCondition() {
/* 64 */     return this.condition;
/*    */   }
/*    */   
/*    */   public void setCondition(SQLExpr condition) {
/* 68 */     this.condition = condition;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\ast\clause\MySqlWhileStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
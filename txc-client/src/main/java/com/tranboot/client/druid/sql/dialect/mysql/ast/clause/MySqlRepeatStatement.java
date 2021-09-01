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
/*    */ public class MySqlRepeatStatement
/*    */   extends MySqlStatementImpl
/*    */ {
/*    */   private String labelName;
/* 34 */   private List<SQLStatement> statements = new ArrayList<>();
/*    */   
/*    */   private SQLExpr condition;
/*    */ 
/*    */   
/*    */   public void accept0(MySqlASTVisitor visitor) {
/* 40 */     if (visitor.visit(this)) {
/* 41 */       acceptChild((SQLASTVisitor)visitor, this.statements);
/* 42 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.condition);
/*    */     } 
/* 44 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public List<SQLStatement> getStatements() {
/* 48 */     return this.statements;
/*    */   }
/*    */   
/*    */   public void setStatements(List<SQLStatement> statements) {
/* 52 */     this.statements = statements;
/*    */   }
/*    */   
/*    */   public String getLabelName() {
/* 56 */     return this.labelName;
/*    */   }
/*    */   
/*    */   public void setLabelName(String labelName) {
/* 60 */     this.labelName = labelName;
/*    */   }
/*    */   
/*    */   public SQLExpr getCondition() {
/* 64 */     return this.condition;
/*    */   }
/*    */   
/*    */   public void setCondition(SQLExpr condition) {
/* 68 */     this.condition = condition;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\ast\clause\MySqlRepeatStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
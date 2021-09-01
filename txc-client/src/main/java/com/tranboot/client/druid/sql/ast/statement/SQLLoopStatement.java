/*    */ package com.tranboot.client.druid.sql.ast.statement;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLStatement;
import com.tranboot.client.druid.sql.ast.SQLStatementImpl;
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
/*    */ public class SQLLoopStatement
/*    */   extends SQLStatementImpl
/*    */ {
/*    */   private String labelName;
/* 29 */   private List<SQLStatement> statements = new ArrayList<>();
/*    */ 
/*    */   
/*    */   public void accept0(SQLASTVisitor visitor) {
/* 33 */     if (visitor.visit(this)) {
/* 34 */       acceptChild(visitor, this.statements);
/*    */     }
/* 36 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public List<SQLStatement> getStatements() {
/* 40 */     return this.statements;
/*    */   }
/*    */   
/*    */   public void setStatements(List<SQLStatement> statements) {
/* 44 */     this.statements = statements;
/*    */   }
/*    */   
/*    */   public String getLabelName() {
/* 48 */     return this.labelName;
/*    */   }
/*    */   
/*    */   public void setLabelName(String labelName) {
/* 52 */     this.labelName = labelName;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\statement\SQLLoopStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package com.tranboot.client.druid.sql.ast.statement;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLCommentHint;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.SQLStatement;
import com.tranboot.client.druid.sql.ast.SQLStatementImpl;
import com.tranboot.client.druid.sql.visitor.SQLASTVisitor;

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
/*    */ public class SQLExplainStatement
/*    */   extends SQLStatementImpl
/*    */ {
/*    */   protected SQLStatement statement;
/*    */   private List<SQLCommentHint> hints;
/*    */   
/*    */   public SQLExplainStatement() {}
/*    */   
/*    */   public SQLExplainStatement(String dbType) {
/* 36 */     super(dbType);
/*    */   }
/*    */   
/*    */   public SQLStatement getStatement() {
/* 40 */     return this.statement;
/*    */   }
/*    */   
/*    */   public void setStatement(SQLStatement statement) {
/* 44 */     if (statement != null) {
/* 45 */       statement.setParent((SQLObject)this);
/*    */     }
/* 47 */     this.statement = statement;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 52 */     if (visitor.visit(this)) {
/* 53 */       acceptChild(visitor, (SQLObject)this.statement);
/*    */     }
/* 55 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public List<SQLCommentHint> getHints() {
/* 59 */     return this.hints;
/*    */   }
/*    */   
/*    */   public void setHints(List<SQLCommentHint> hints) {
/* 63 */     this.hints = hints;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\statement\SQLExplainStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
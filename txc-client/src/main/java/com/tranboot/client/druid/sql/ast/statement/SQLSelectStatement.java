/*    */ package com.tranboot.client.druid.sql.ast.statement;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLCommentHint;
import com.tranboot.client.druid.sql.ast.SQLObject;
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
/*    */ public class SQLSelectStatement
/*    */   extends SQLStatementImpl
/*    */ {
/*    */   protected SQLSelect select;
/*    */   private List<SQLCommentHint> headHints;
/*    */   
/*    */   public SQLSelectStatement() {}
/*    */   
/*    */   public SQLSelectStatement(String dbType) {
/* 35 */     super(dbType);
/*    */   }
/*    */   
/*    */   public SQLSelectStatement(SQLSelect select) {
/* 39 */     setSelect(select);
/*    */   }
/*    */   
/*    */   public SQLSelectStatement(SQLSelect select, String dbType) {
/* 43 */     this(dbType);
/* 44 */     setSelect(select);
/*    */   }
/*    */   
/*    */   public SQLSelect getSelect() {
/* 48 */     return this.select;
/*    */   }
/*    */   
/*    */   public void setSelect(SQLSelect select) {
/* 52 */     if (select != null) {
/* 53 */       select.setParent((SQLObject)this);
/*    */     }
/* 55 */     this.select = select;
/*    */   }
/*    */   
/*    */   public void output(StringBuffer buf) {
/* 59 */     this.select.output(buf);
/*    */   }
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 63 */     if (visitor.visit(this)) {
/* 64 */       acceptChild(visitor, (SQLObject)this.select);
/*    */     }
/* 66 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public List<SQLCommentHint> getHeadHintsDirect() {
/* 70 */     return this.headHints;
/*    */   }
/*    */   
/*    */   public void setHeadHints(List<SQLCommentHint> headHints) {
/* 74 */     this.headHints = headHints;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\statement\SQLSelectStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
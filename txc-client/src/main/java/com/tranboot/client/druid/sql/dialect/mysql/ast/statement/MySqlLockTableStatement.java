/*    */ package com.tranboot.client.druid.sql.dialect.mysql.ast.statement;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLCommentHint;
import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLName;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.statement.SQLExprTableSource;
import com.tranboot.client.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
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
/*    */ public class MySqlLockTableStatement
/*    */   extends MySqlStatementImpl
/*    */ {
/*    */   private SQLExprTableSource tableSource;
/*    */   private LockType lockType;
/*    */   private List<SQLCommentHint> hints;
/*    */   
/*    */   public SQLExprTableSource getTableSource() {
/* 34 */     return this.tableSource;
/*    */   }
/*    */   
/*    */   public void setTableSource(SQLExprTableSource tableSource) {
/* 38 */     if (tableSource != null) {
/* 39 */       tableSource.setParent((SQLObject)this);
/*    */     }
/* 41 */     this.tableSource = tableSource;
/*    */   }
/*    */   
/*    */   public void setTableSource(SQLName name) {
/* 45 */     setTableSource(new SQLExprTableSource((SQLExpr)name));
/*    */   }
/*    */   
/*    */   public LockType getLockType() {
/* 49 */     return this.lockType;
/*    */   }
/*    */   
/*    */   public void setLockType(LockType lockType) {
/* 53 */     this.lockType = lockType;
/*    */   }
/*    */   
/*    */   public void accept0(MySqlASTVisitor visitor) {
/* 57 */     if (visitor.visit(this)) {
/* 58 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.tableSource);
/*    */     }
/* 60 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public enum LockType {
/* 64 */     READ("READ"), READ_LOCAL("READ LOCAL"), WRITE("WRITE"), LOW_PRIORITY_WRITE("LOW_PRIORITY WRITE");
/*    */     
/*    */     public final String name;
/*    */     
/*    */     LockType(String name) {
/* 69 */       this.name = name;
/*    */     }
/*    */   }
/*    */   
/*    */   public List<SQLCommentHint> getHints() {
/* 74 */     return this.hints;
/*    */   }
/*    */   
/*    */   public void setHints(List<SQLCommentHint> hints) {
/* 78 */     this.hints = hints;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\ast\statement\MySqlLockTableStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
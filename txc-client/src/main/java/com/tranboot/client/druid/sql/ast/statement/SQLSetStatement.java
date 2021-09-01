/*    */ package com.tranboot.client.druid.sql.ast.statement;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLCommentHint;
import com.tranboot.client.druid.sql.ast.SQLExpr;
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
/*    */ public class SQLSetStatement
/*    */   extends SQLStatementImpl
/*    */ {
/* 28 */   private List<SQLAssignItem> items = new ArrayList<>();
/*    */   
/*    */   private List<SQLCommentHint> hints;
/*    */ 
/*    */   
/*    */   public SQLSetStatement() {}
/*    */   
/*    */   public SQLSetStatement(String dbType) {
/* 36 */     super(dbType);
/*    */   }
/*    */   
/*    */   public SQLSetStatement(SQLExpr target, SQLExpr value) {
/* 40 */     this(target, value, null);
/*    */   }
/*    */   
/*    */   public SQLSetStatement(SQLExpr target, SQLExpr value, String dbType) {
/* 44 */     super(dbType);
/* 45 */     this.items.add(new SQLAssignItem(target, value));
/*    */   }
/*    */   
/*    */   public List<SQLAssignItem> getItems() {
/* 49 */     return this.items;
/*    */   }
/*    */   
/*    */   public void setItems(List<SQLAssignItem> items) {
/* 53 */     this.items = items;
/*    */   }
/*    */   
/*    */   public List<SQLCommentHint> getHints() {
/* 57 */     return this.hints;
/*    */   }
/*    */   
/*    */   public void setHints(List<SQLCommentHint> hints) {
/* 61 */     this.hints = hints;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 66 */     if (visitor.visit(this)) {
/* 67 */       acceptChild(visitor, this.items);
/* 68 */       acceptChild(visitor, this.hints);
/*    */     } 
/* 70 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public void output(StringBuffer buf) {
/* 74 */     buf.append("SET ");
/*    */     
/* 76 */     for (int i = 0; i < this.items.size(); i++) {
/* 77 */       if (i != 0) {
/* 78 */         buf.append(", ");
/*    */       }
/*    */       
/* 81 */       SQLAssignItem item = this.items.get(i);
/* 82 */       item.output(buf);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\statement\SQLSetStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
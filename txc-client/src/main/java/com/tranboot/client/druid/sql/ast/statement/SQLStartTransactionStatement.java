/*    */ package com.tranboot.client.druid.sql.ast.statement;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLCommentHint;
import com.tranboot.client.druid.sql.ast.SQLExpr;
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
/*    */ public class SQLStartTransactionStatement
/*    */   extends SQLStatementImpl
/*    */ {
/*    */   private boolean consistentSnapshot = false;
/*    */   private boolean begin = false;
/*    */   private boolean work = false;
/*    */   private SQLExpr name;
/*    */   private List<SQLCommentHint> hints;
/*    */   
/*    */   public boolean isConsistentSnapshot() {
/* 36 */     return this.consistentSnapshot;
/*    */   }
/*    */   
/*    */   public void setConsistentSnapshot(boolean consistentSnapshot) {
/* 40 */     this.consistentSnapshot = consistentSnapshot;
/*    */   }
/*    */   
/*    */   public boolean isBegin() {
/* 44 */     return this.begin;
/*    */   }
/*    */   
/*    */   public void setBegin(boolean begin) {
/* 48 */     this.begin = begin;
/*    */   }
/*    */   
/*    */   public boolean isWork() {
/* 52 */     return this.work;
/*    */   }
/*    */   
/*    */   public void setWork(boolean work) {
/* 56 */     this.work = work;
/*    */   }
/*    */   
/*    */   public void accept0(SQLASTVisitor visitor) {
/* 60 */     visitor.visit(this);
/*    */     
/* 62 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public List<SQLCommentHint> getHints() {
/* 66 */     return this.hints;
/*    */   }
/*    */   
/*    */   public void setHints(List<SQLCommentHint> hints) {
/* 70 */     this.hints = hints;
/*    */   }
/*    */   
/*    */   public SQLExpr getName() {
/* 74 */     return this.name;
/*    */   }
/*    */   
/*    */   public void setName(SQLExpr name) {
/* 78 */     if (name != null) {
/* 79 */       name.setParent((SQLObject)this);
/*    */     }
/* 81 */     this.name = name;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\statement\SQLStartTransactionStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package com.tranboot.client.druid.sql.ast.statement;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLName;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.SQLStatementImpl;
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
/*    */ public class SQLShowTablesStatement
/*    */   extends SQLStatementImpl
/*    */ {
/*    */   protected SQLName database;
/*    */   protected SQLExpr like;
/*    */   protected boolean full;
/*    */   protected SQLExpr where;
/*    */   
/*    */   public SQLName getDatabase() {
/* 33 */     return this.database;
/*    */   }
/*    */   
/*    */   public void setDatabase(SQLName database) {
/* 37 */     if (database != null) {
/* 38 */       database.setParent((SQLObject)this);
/*    */     }
/*    */     
/* 41 */     this.database = database;
/*    */   }
/*    */   
/*    */   public SQLExpr getLike() {
/* 45 */     return this.like;
/*    */   }
/*    */   
/*    */   public void setLike(SQLExpr like) {
/* 49 */     if (like != null) {
/* 50 */       like.setParent((SQLObject)this);
/*    */     }
/*    */     
/* 53 */     this.like = like;
/*    */   }
/*    */   
/*    */   public boolean isFull() {
/* 57 */     return this.full;
/*    */   }
/*    */   
/*    */   public void setFull(boolean full) {
/* 61 */     this.full = full;
/*    */   }
/*    */   
/*    */   public SQLExpr getWhere() {
/* 65 */     return this.where;
/*    */   }
/*    */   
/*    */   public void setWhere(SQLExpr where) {
/* 69 */     this.where = where;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 74 */     if (visitor.visit(this)) {
/* 75 */       acceptChild(visitor, (SQLObject)this.database);
/* 76 */       acceptChild(visitor, (SQLObject)this.like);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\statement\SQLShowTablesStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
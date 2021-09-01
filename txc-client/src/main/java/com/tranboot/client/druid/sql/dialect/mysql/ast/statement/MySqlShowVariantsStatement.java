/*    */ package com.tranboot.client.druid.sql.dialect.mysql.ast.statement;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
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
/*    */ public class MySqlShowVariantsStatement
/*    */   extends MySqlStatementImpl
/*    */   implements MySqlShowStatement
/*    */ {
/*    */   private boolean global = false;
/*    */   private boolean session = false;
/*    */   private SQLExpr like;
/*    */   private SQLExpr where;
/*    */   
/*    */   public boolean isGlobal() {
/* 30 */     return this.global;
/*    */   }
/*    */   
/*    */   public void setGlobal(boolean global) {
/* 34 */     this.global = global;
/*    */   }
/*    */   
/*    */   public boolean isSession() {
/* 38 */     return this.session;
/*    */   }
/*    */   
/*    */   public void setSession(boolean session) {
/* 42 */     this.session = session;
/*    */   }
/*    */   
/*    */   public SQLExpr getLike() {
/* 46 */     return this.like;
/*    */   }
/*    */   
/*    */   public void setLike(SQLExpr like) {
/* 50 */     this.like = like;
/*    */   }
/*    */   
/*    */   public SQLExpr getWhere() {
/* 54 */     return this.where;
/*    */   }
/*    */   
/*    */   public void setWhere(SQLExpr where) {
/* 58 */     this.where = where;
/*    */   }
/*    */   
/*    */   public void accept0(MySqlASTVisitor visitor) {
/* 62 */     if (visitor.visit(this)) {
/* 63 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.like);
/* 64 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.where);
/*    */     } 
/* 66 */     visitor.endVisit(this);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\ast\statement\MySqlShowVariantsStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
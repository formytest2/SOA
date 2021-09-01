/*    */ package com.tranboot.client.druid.sql.dialect.mysql.ast.statement;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLName;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.expr.SQLIdentifierExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLPropertyExpr;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MySqlShowColumnsStatement
/*    */   extends MySqlStatementImpl
/*    */   implements MySqlShowStatement
/*    */ {
/*    */   private boolean full;
/*    */   private SQLName table;
/*    */   private SQLName database;
/*    */   private SQLExpr like;
/*    */   private SQLExpr where;
/*    */   
/*    */   public boolean isFull() {
/* 34 */     return this.full;
/*    */   }
/*    */   
/*    */   public void setFull(boolean full) {
/* 38 */     this.full = full;
/*    */   }
/*    */   
/*    */   public SQLName getTable() {
/* 42 */     return this.table;
/*    */   }
/*    */   
/*    */   public void setTable(SQLName table) {
/* 46 */     if (table instanceof SQLPropertyExpr) {
/* 47 */       SQLPropertyExpr propExpr = (SQLPropertyExpr)table;
/* 48 */       setDatabase((SQLName)propExpr.getOwner());
/* 49 */       this.table = (SQLName)new SQLIdentifierExpr(propExpr.getName());
/*    */       return;
/*    */     } 
/* 52 */     this.table = table;
/*    */   }
/*    */   
/*    */   public SQLName getDatabase() {
/* 56 */     return this.database;
/*    */   }
/*    */   
/*    */   public void setDatabase(SQLName database) {
/* 60 */     this.database = database;
/*    */   }
/*    */   
/*    */   public SQLExpr getLike() {
/* 64 */     return this.like;
/*    */   }
/*    */   
/*    */   public void setLike(SQLExpr like) {
/* 68 */     this.like = like;
/*    */   }
/*    */   
/*    */   public SQLExpr getWhere() {
/* 72 */     return this.where;
/*    */   }
/*    */   
/*    */   public void setWhere(SQLExpr where) {
/* 76 */     this.where = where;
/*    */   }
/*    */   
/*    */   public void accept0(MySqlASTVisitor visitor) {
/* 80 */     if (visitor.visit(this)) {
/* 81 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.table);
/* 82 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.database);
/* 83 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.like);
/* 84 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.where);
/*    */     } 
/* 86 */     visitor.endVisit(this);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\ast\statement\MySqlShowColumnsStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
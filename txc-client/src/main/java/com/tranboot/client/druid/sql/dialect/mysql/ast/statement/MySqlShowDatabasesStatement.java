/*    */ package com.tranboot.client.druid.sql.dialect.mysql.ast.statement;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLName;
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
/*    */ public class MySqlShowDatabasesStatement
/*    */   extends MySqlStatementImpl
/*    */   implements MySqlShowStatement
/*    */ {
/*    */   private SQLName database;
/*    */   private SQLExpr like;
/*    */   private SQLExpr where;
/*    */   
/*    */   public SQLName getDatabase() {
/* 29 */     return this.database;
/*    */   }
/*    */   
/*    */   public void setDatabase(SQLName database) {
/* 33 */     this.database = database;
/*    */   }
/*    */   
/*    */   public SQLExpr getLike() {
/* 37 */     return this.like;
/*    */   }
/*    */   
/*    */   public void setLike(SQLExpr like) {
/* 41 */     this.like = like;
/*    */   }
/*    */   
/*    */   public SQLExpr getWhere() {
/* 45 */     return this.where;
/*    */   }
/*    */   
/*    */   public void setWhere(SQLExpr where) {
/* 49 */     this.where = where;
/*    */   }
/*    */   
/*    */   public void accept0(MySqlASTVisitor visitor) {
/* 53 */     if (visitor.visit(this)) {
/* 54 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.database);
/* 55 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.like);
/* 56 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.where);
/*    */     } 
/* 58 */     visitor.endVisit(this);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\ast\statement\MySqlShowDatabasesStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
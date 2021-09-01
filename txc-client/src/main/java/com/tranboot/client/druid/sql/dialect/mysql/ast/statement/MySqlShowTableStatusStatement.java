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
/*    */ public class MySqlShowTableStatusStatement
/*    */   extends MySqlStatementImpl
/*    */   implements MySqlShowStatement
/*    */ {
/*    */   private SQLExpr database;
/*    */   private SQLExpr like;
/*    */   private SQLExpr where;
/*    */   
/*    */   public SQLExpr getDatabase() {
/* 28 */     return this.database;
/*    */   }
/*    */   
/*    */   public void setDatabase(SQLExpr database) {
/* 32 */     this.database = database;
/*    */   }
/*    */   
/*    */   public SQLExpr getLike() {
/* 36 */     return this.like;
/*    */   }
/*    */   
/*    */   public void setLike(SQLExpr like) {
/* 40 */     this.like = like;
/*    */   }
/*    */   
/*    */   public SQLExpr getWhere() {
/* 44 */     return this.where;
/*    */   }
/*    */   
/*    */   public void setWhere(SQLExpr where) {
/* 48 */     this.where = where;
/*    */   }
/*    */   
/*    */   public void accept0(MySqlASTVisitor visitor) {
/* 52 */     if (visitor.visit(this)) {
/* 53 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.database);
/* 54 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.like);
/* 55 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.where);
/*    */     } 
/* 57 */     visitor.endVisit(this);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\ast\statement\MySqlShowTableStatusStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
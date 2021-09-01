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
/*    */ public class MySqlShowOpenTablesStatement
/*    */   extends MySqlStatementImpl
/*    */   implements MySqlShowStatement
/*    */ {
/*    */   private SQLExpr database;
/*    */   private SQLExpr like;
/*    */   private SQLExpr where;
/*    */   
/*    */   public SQLExpr getLike() {
/* 28 */     return this.like;
/*    */   }
/*    */   
/*    */   public void setLike(SQLExpr like) {
/* 32 */     this.like = like;
/*    */   }
/*    */   
/*    */   public SQLExpr getWhere() {
/* 36 */     return this.where;
/*    */   }
/*    */   
/*    */   public void setWhere(SQLExpr where) {
/* 40 */     this.where = where;
/*    */   }
/*    */   
/*    */   public void accept0(MySqlASTVisitor visitor) {
/* 44 */     if (visitor.visit(this)) {
/* 45 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.database);
/* 46 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.like);
/* 47 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.where);
/*    */     } 
/* 49 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public SQLExpr getDatabase() {
/* 53 */     return this.database;
/*    */   }
/*    */   
/*    */   public void setDatabase(SQLExpr database) {
/* 57 */     this.database = database;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\ast\statement\MySqlShowOpenTablesStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
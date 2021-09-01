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
/*    */ public class MySqlShowCreateDatabaseStatement
/*    */   extends MySqlStatementImpl
/*    */   implements MySqlShowStatement
/*    */ {
/*    */   private SQLExpr database;
/*    */   
/*    */   public void accept0(MySqlASTVisitor visitor) {
/* 26 */     if (visitor.visit(this)) {
/* 27 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.database);
/*    */     }
/* 29 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public SQLExpr getDatabase() {
/* 33 */     return this.database;
/*    */   }
/*    */   
/*    */   public void setDatabase(SQLExpr database) {
/* 37 */     this.database = database;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\ast\statement\MySqlShowCreateDatabaseStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package com.tranboot.client.druid.sql.ast.statement;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLExpr;
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
/*    */ public class SQLDropDatabaseStatement
/*    */   extends SQLStatementImpl
/*    */   implements SQLDDLStatement
/*    */ {
/*    */   private SQLExpr database;
/*    */   private boolean ifExists;
/*    */   
/*    */   public SQLDropDatabaseStatement() {}
/*    */   
/*    */   public SQLDropDatabaseStatement(String dbType) {
/* 32 */     super(dbType);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 37 */     if (visitor.visit(this)) {
/* 38 */       acceptChild(visitor, (SQLObject)this.database);
/*    */     }
/* 40 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public SQLExpr getDatabase() {
/* 44 */     return this.database;
/*    */   }
/*    */   
/*    */   public void setDatabase(SQLExpr database) {
/* 48 */     if (database != null) {
/* 49 */       database.setParent((SQLObject)this);
/*    */     }
/* 51 */     this.database = database;
/*    */   }
/*    */   
/*    */   public boolean isIfExists() {
/* 55 */     return this.ifExists;
/*    */   }
/*    */   
/*    */   public void setIfExists(boolean ifExists) {
/* 59 */     this.ifExists = ifExists;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\statement\SQLDropDatabaseStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
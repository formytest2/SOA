/*    */ package com.tranboot.client.druid.sql.ast.statement;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLName;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.SQLStatement;
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
/*    */ public class SQLUseStatement
/*    */   extends SQLStatementImpl
/*    */   implements SQLStatement
/*    */ {
/*    */   private SQLName database;
/*    */   
/*    */   public SQLUseStatement() {}
/*    */   
/*    */   public SQLUseStatement(String dbType) {
/* 32 */     super(dbType);
/*    */   }
/*    */   
/*    */   public SQLName getDatabase() {
/* 36 */     return this.database;
/*    */   }
/*    */   
/*    */   public void setDatabase(SQLName database) {
/* 40 */     this.database = database;
/*    */   }
/*    */ 
/*    */   
/*    */   public void accept0(SQLASTVisitor visitor) {
/* 45 */     if (visitor.visit(this)) {
/* 46 */       acceptChild(visitor, (SQLObject)this.database);
/*    */     }
/* 48 */     visitor.endVisit(this);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\statement\SQLUseStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package com.tranboot.client.druid.sql.ast.statement;
/*    */ 
/*    */

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
/*    */ public class SQLAlterDatabaseStatement
/*    */   extends SQLStatementImpl
/*    */ {
/*    */   private SQLName name;
/*    */   private boolean upgradeDataDirectoryName;
/*    */   
/*    */   public SQLAlterDatabaseStatement() {}
/*    */   
/*    */   public SQLAlterDatabaseStatement(String dbType) {
/* 33 */     setDbType(dbType);
/*    */   }
/*    */   
/*    */   public SQLName getName() {
/* 37 */     return this.name;
/*    */   }
/*    */   
/*    */   public void setName(SQLName name) {
/* 41 */     if (name != null) {
/* 42 */       name.setParent((SQLObject)this);
/*    */     }
/* 44 */     this.name = name;
/*    */   }
/*    */   
/*    */   public boolean isUpgradeDataDirectoryName() {
/* 48 */     return this.upgradeDataDirectoryName;
/*    */   }
/*    */   
/*    */   public void setUpgradeDataDirectoryName(boolean upgradeDataDirectoryName) {
/* 52 */     this.upgradeDataDirectoryName = upgradeDataDirectoryName;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 57 */     if (visitor.visit(this)) {
/* 58 */       acceptChild(visitor, (SQLObject)this.name);
/*    */     }
/* 60 */     visitor.endVisit(this);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\statement\SQLAlterDatabaseStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
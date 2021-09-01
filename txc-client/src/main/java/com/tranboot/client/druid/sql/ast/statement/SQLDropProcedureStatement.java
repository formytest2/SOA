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
/*    */ public class SQLDropProcedureStatement
/*    */   extends SQLStatementImpl
/*    */   implements SQLDDLStatement
/*    */ {
/*    */   private SQLName name;
/*    */   private boolean ifExists;
/*    */   
/*    */   public SQLDropProcedureStatement() {}
/*    */   
/*    */   public SQLDropProcedureStatement(String dbType) {
/* 32 */     super(dbType);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 37 */     if (visitor.visit(this)) {
/* 38 */       acceptChild(visitor, (SQLObject)this.name);
/*    */     }
/* 40 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public SQLName getName() {
/* 44 */     return this.name;
/*    */   }
/*    */   
/*    */   public void setName(SQLName name) {
/* 48 */     if (name != null) {
/* 49 */       name.setParent((SQLObject)this);
/*    */     }
/* 51 */     this.name = name;
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


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\statement\SQLDropProcedureStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
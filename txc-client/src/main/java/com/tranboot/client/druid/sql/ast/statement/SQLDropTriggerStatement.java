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
/*    */ public class SQLDropTriggerStatement
/*    */   extends SQLStatementImpl
/*    */   implements SQLDDLStatement {
/*    */   private SQLName name;
/*    */   
/*    */   public SQLDropTriggerStatement() {}
/*    */   
/*    */   public SQLDropTriggerStatement(String dbType) {
/* 16 */     super(dbType);
/*    */   }
/*    */   
/*    */   public SQLName getName() {
/* 20 */     return this.name;
/*    */   }
/*    */   
/*    */   public void setName(SQLName name) {
/* 24 */     this.name = name;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 29 */     if (visitor.visit(this)) {
/* 30 */       acceptChild(visitor, (SQLObject)this.name);
/*    */     }
/* 32 */     visitor.endVisit(this);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\statement\SQLDropTriggerStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
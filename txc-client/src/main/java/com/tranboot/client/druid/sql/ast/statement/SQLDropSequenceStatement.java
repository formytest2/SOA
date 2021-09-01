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
/*    */ public class SQLDropSequenceStatement
/*    */   extends SQLStatementImpl
/*    */   implements SQLDDLStatement
/*    */ {
/*    */   private SQLName name;
/*    */   
/*    */   public SQLDropSequenceStatement() {}
/*    */   
/*    */   public SQLDropSequenceStatement(String dbType) {
/* 31 */     super(dbType);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 36 */     if (visitor.visit(this)) {
/* 37 */       acceptChild(visitor, (SQLObject)this.name);
/*    */     }
/* 39 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public SQLName getName() {
/* 43 */     return this.name;
/*    */   }
/*    */   
/*    */   public void setName(SQLName name) {
/* 47 */     this.name = name;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\statement\SQLDropSequenceStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
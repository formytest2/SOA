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
/*    */ public class SQLRollbackStatement
/*    */   extends SQLStatementImpl
/*    */ {
/*    */   private SQLName to;
/*    */   
/*    */   public SQLRollbackStatement() {}
/*    */   
/*    */   public SQLRollbackStatement(String dbType) {
/* 31 */     super(dbType);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 36 */     if (visitor.visit(this)) {
/* 37 */       acceptChild(visitor, (SQLObject)this.to);
/*    */     }
/* 39 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public SQLName getTo() {
/* 43 */     return this.to;
/*    */   }
/*    */   
/*    */   public void setTo(SQLName to) {
/* 47 */     this.to = to;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\statement\SQLRollbackStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
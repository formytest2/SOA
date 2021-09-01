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
/*    */ 
/*    */ public class SQLReleaseSavePointStatement
/*    */   extends SQLStatementImpl
/*    */ {
/*    */   private SQLExpr name;
/*    */   
/*    */   public SQLReleaseSavePointStatement() {}
/*    */   
/*    */   public SQLReleaseSavePointStatement(String dbType) {
/* 31 */     super(dbType);
/*    */   }
/*    */   
/*    */   public SQLExpr getName() {
/* 35 */     return this.name;
/*    */   }
/*    */   
/*    */   public void setName(SQLExpr name) {
/* 39 */     this.name = name;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void accept0(SQLASTVisitor visitor) {
/* 44 */     if (visitor.visit(this)) {
/* 45 */       acceptChild(visitor, (SQLObject)this.name);
/*    */     }
/* 47 */     visitor.endVisit(this);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\statement\SQLReleaseSavePointStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
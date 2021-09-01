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
/*    */ public class MySqlShowCreateTriggerStatement
/*    */   extends MySqlStatementImpl
/*    */   implements MySqlShowStatement
/*    */ {
/*    */   private SQLExpr name;
/*    */   
/*    */   public void accept0(MySqlASTVisitor visitor) {
/* 26 */     if (visitor.visit(this)) {
/* 27 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.name);
/*    */     }
/* 29 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public SQLExpr getName() {
/* 33 */     return this.name;
/*    */   }
/*    */   
/*    */   public void setName(SQLExpr functionName) {
/* 37 */     this.name = functionName;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\ast\statement\MySqlShowCreateTriggerStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
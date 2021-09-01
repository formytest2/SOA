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
/*    */ public class MySqlShowCreateEventStatement
/*    */   extends MySqlStatementImpl
/*    */   implements MySqlShowStatement
/*    */ {
/*    */   private SQLExpr eventName;
/*    */   
/*    */   public void accept0(MySqlASTVisitor visitor) {
/* 26 */     if (visitor.visit(this)) {
/* 27 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.eventName);
/*    */     }
/* 29 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public SQLExpr getEventName() {
/* 33 */     return this.eventName;
/*    */   }
/*    */   
/*    */   public void setEventName(SQLExpr eventName) {
/* 37 */     this.eventName = eventName;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\ast\statement\MySqlShowCreateEventStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package com.tranboot.client.druid.sql.dialect.mysql.ast.statement;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLName;
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
/*    */ 
/*    */ public class MysqlDeallocatePrepareStatement
/*    */   extends MySqlStatementImpl
/*    */ {
/*    */   private SQLName statementName;
/*    */   
/*    */   public SQLName getStatementName() {
/* 26 */     return this.statementName;
/*    */   }
/*    */   
/*    */   public void setStatementName(SQLName statementName) {
/* 30 */     this.statementName = statementName;
/*    */   }
/*    */   
/*    */   public void accept0(MySqlASTVisitor visitor) {
/* 34 */     if (visitor.visit(this)) {
/* 35 */       acceptChild((SQLASTVisitor)visitor, (SQLObject)this.statementName);
/*    */     }
/* 37 */     visitor.endVisit(this);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\ast\statement\MysqlDeallocatePrepareStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
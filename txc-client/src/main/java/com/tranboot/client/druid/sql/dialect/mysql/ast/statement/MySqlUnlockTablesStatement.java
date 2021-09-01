/*    */ package com.tranboot.client.druid.sql.dialect.mysql.ast.statement;
/*    */ 
/*    */ import com.tranboot.client.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;

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
/*    */ public class MySqlUnlockTablesStatement
/*    */   extends MySqlStatementImpl
/*    */ {
/*    */   public void accept0(MySqlASTVisitor visitor) {
/* 23 */     visitor.visit(this);
/* 24 */     visitor.endVisit(this);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\ast\statement\MySqlUnlockTablesStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
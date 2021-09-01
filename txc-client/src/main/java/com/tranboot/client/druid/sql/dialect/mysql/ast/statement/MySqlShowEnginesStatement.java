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
/*    */ public class MySqlShowEnginesStatement
/*    */   extends MySqlStatementImpl
/*    */   implements MySqlShowStatement
/*    */ {
/*    */   private boolean storage = false;
/*    */   
/*    */   public void accept0(MySqlASTVisitor visitor) {
/* 25 */     visitor.visit(this);
/* 26 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public boolean isStorage() {
/* 30 */     return this.storage;
/*    */   }
/*    */   
/*    */   public void setStorage(boolean storage) {
/* 34 */     this.storage = storage;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\ast\statement\MySqlShowEnginesStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
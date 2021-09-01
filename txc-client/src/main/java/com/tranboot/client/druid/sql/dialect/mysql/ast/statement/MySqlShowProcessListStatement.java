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
/*    */ public class MySqlShowProcessListStatement
/*    */   extends MySqlStatementImpl
/*    */   implements MySqlShowStatement
/*    */ {
/*    */   private boolean full = false;
/*    */   
/*    */   public void accept0(MySqlASTVisitor visitor) {
/* 25 */     visitor.visit(this);
/* 26 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public boolean isFull() {
/* 30 */     return this.full;
/*    */   }
/*    */   
/*    */   public void setFull(boolean full) {
/* 34 */     this.full = full;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\ast\statement\MySqlShowProcessListStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
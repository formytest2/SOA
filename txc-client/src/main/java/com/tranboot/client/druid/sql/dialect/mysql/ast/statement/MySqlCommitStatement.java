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
/*    */ 
/*    */ public class MySqlCommitStatement
/*    */   extends MySqlStatementImpl
/*    */ {
/*    */   private boolean work = false;
/*    */   private Boolean chain;
/*    */   private Boolean release;
/*    */   
/*    */   public Boolean getChain() {
/* 28 */     return this.chain;
/*    */   }
/*    */   
/*    */   public void setChain(Boolean chain) {
/* 32 */     this.chain = chain;
/*    */   }
/*    */   
/*    */   public Boolean getRelease() {
/* 36 */     return this.release;
/*    */   }
/*    */   
/*    */   public void setRelease(Boolean release) {
/* 40 */     this.release = release;
/*    */   }
/*    */   
/*    */   public boolean isWork() {
/* 44 */     return this.work;
/*    */   }
/*    */   
/*    */   public void setWork(boolean work) {
/* 48 */     this.work = work;
/*    */   }
/*    */   
/*    */   public void accept0(MySqlASTVisitor visitor) {
/* 52 */     visitor.visit(this);
/*    */     
/* 54 */     visitor.endVisit(this);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\ast\statement\MySqlCommitStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
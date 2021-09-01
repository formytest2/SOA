/*    */ package com.tranboot.client.druid.sql.dialect.oracle.ast.stmt;
/*    */ 
/*    */ import com.tranboot.client.druid.sql.dialect.oracle.visitor.OracleASTVisitor;

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
/*    */ public class OracleCommitStatement
/*    */   extends OracleStatementImpl
/*    */ {
/*    */   private boolean write;
/*    */   private Boolean wait;
/*    */   private Boolean immediate;
/*    */   
/*    */   public void accept0(OracleASTVisitor visitor) {
/* 28 */     visitor.visit(this);
/* 29 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public boolean isWrite() {
/* 33 */     return this.write;
/*    */   }
/*    */   
/*    */   public void setWrite(boolean write) {
/* 37 */     this.write = write;
/*    */   }
/*    */   
/*    */   public Boolean getWait() {
/* 41 */     return this.wait;
/*    */   }
/*    */   
/*    */   public void setWait(Boolean wait) {
/* 45 */     this.wait = wait;
/*    */   }
/*    */   
/*    */   public Boolean getImmediate() {
/* 49 */     return this.immediate;
/*    */   }
/*    */   
/*    */   public void setImmediate(Boolean immediate) {
/* 53 */     this.immediate = immediate;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\oracle\ast\stmt\OracleCommitStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
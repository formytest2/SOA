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
/*    */ 
/*    */ public class MySqlSetTransactionStatement
/*    */   extends MySqlStatementImpl
/*    */ {
/*    */   private Boolean global;
/*    */   private String isolationLevel;
/*    */   private String accessModel;
/*    */   
/*    */   public void accept0(MySqlASTVisitor visitor) {
/* 29 */     visitor.visit(this);
/* 30 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public Boolean getGlobal() {
/* 34 */     return this.global;
/*    */   }
/*    */   
/*    */   public void setGlobal(Boolean global) {
/* 38 */     this.global = global;
/*    */   }
/*    */   
/*    */   public String getIsolationLevel() {
/* 42 */     return this.isolationLevel;
/*    */   }
/*    */   
/*    */   public void setIsolationLevel(String isolationLevel) {
/* 46 */     this.isolationLevel = isolationLevel;
/*    */   }
/*    */   
/*    */   public String getAccessModel() {
/* 50 */     return this.accessModel;
/*    */   }
/*    */   
/*    */   public void setAccessModel(String accessModel) {
/* 54 */     this.accessModel = accessModel;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\ast\statement\MySqlSetTransactionStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
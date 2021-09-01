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
/*    */ public class MySqlSetNamesStatement
/*    */   extends MySqlStatementImpl
/*    */ {
/*    */   private boolean isDefault = false;
/*    */   private String charSet;
/*    */   private String collate;
/*    */   
/*    */   public void accept0(MySqlASTVisitor visitor) {
/* 27 */     visitor.visit(this);
/* 28 */     visitor.endVisit(this);
/*    */   }
/*    */   
/*    */   public String getCharSet() {
/* 32 */     return this.charSet;
/*    */   }
/*    */   
/*    */   public void setCharSet(String charSet) {
/* 36 */     this.charSet = charSet;
/*    */   }
/*    */   
/*    */   public String getCollate() {
/* 40 */     return this.collate;
/*    */   }
/*    */   
/*    */   public void setCollate(String collate) {
/* 44 */     this.collate = collate;
/*    */   }
/*    */   
/*    */   public boolean isDefault() {
/* 48 */     return this.isDefault;
/*    */   }
/*    */   
/*    */   public void setDefault(boolean isDefault) {
/* 52 */     this.isDefault = isDefault;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\ast\statement\MySqlSetNamesStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package com.tranboot.client.druid.sql.dialect.mysql.ast.clause;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ConditionValue
/*    */ {
/*    */   private ConditionType type;
/*    */   private String value;
/*    */   
/*    */   public ConditionType getType() {
/* 31 */     return this.type;
/*    */   }
/*    */   public void setType(ConditionType type) {
/* 34 */     this.type = type;
/*    */   }
/*    */   public String getValue() {
/* 37 */     return this.value;
/*    */   }
/*    */   public void setValue(String value) {
/* 40 */     this.value = value;
/*    */   }
/*    */   
/*    */   public enum ConditionType {
/* 44 */     SQLSTATE,
/* 45 */     SELF,
/* 46 */     SYSTEM,
/* 47 */     MYSQL_ERROR_CODE;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\ast\clause\ConditionValue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
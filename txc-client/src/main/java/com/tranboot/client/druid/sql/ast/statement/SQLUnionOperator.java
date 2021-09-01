/*    */ package com.tranboot.client.druid.sql.ast.statement;
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
/*    */ public enum SQLUnionOperator
/*    */ {
/* 19 */   UNION("UNION"), UNION_ALL("UNION ALL"), MINUS("MINUS"), EXCEPT("EXCEPT"), INTERSECT("INTERSECT"),
/* 20 */   DISTINCT("UNION DISTINCT");
/*    */   
/*    */   public final String name;
/*    */   public final String name_lcase;
/*    */   
/*    */   SQLUnionOperator(String name) {
/* 26 */     this.name = name;
/* 27 */     this.name_lcase = name.toLowerCase();
/*    */   }
/*    */   
/*    */   public String toString() {
/* 31 */     return this.name;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\statement\SQLUnionOperator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
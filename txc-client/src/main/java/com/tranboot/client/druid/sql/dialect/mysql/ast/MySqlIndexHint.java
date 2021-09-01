/*    */ package com.tranboot.client.druid.sql.dialect.mysql.ast;
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
/*    */ public interface MySqlIndexHint
/*    */   extends MySqlHint
/*    */ {
/*    */   public enum Option
/*    */   {
/* 20 */     JOIN("JOIN"),
/* 21 */     ORDER_BY("ORDER BY"),
/* 22 */     GROUP_BY("GROUP BY");
/*    */     
/*    */     public final String name;
/*    */     
/*    */     public final String name_lcase;
/*    */     
/*    */     Option(String name) {
/* 29 */       this.name = name;
/* 30 */       this.name_lcase = name.toLowerCase();
/*    */     }
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\ast\MySqlIndexHint.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
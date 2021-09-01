/*    */ package com.tranboot.client.druid.sql.dialect.mysql.ast.expr;
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
/*    */ public enum MySqlIntervalUnit
/*    */ {
/* 19 */   YEAR, YEAR_MONTH,
/*    */   
/* 21 */   QUARTER,
/*    */   
/* 23 */   MONTH, WEEK, DAY, DAY_HOUR, DAY_MINUTE, DAY_SECOND, DAY_MICROSECOND,
/*    */   
/* 25 */   HOUR, HOUR_MINUTE, HOUR_SECOND, HOUR_MICROSECOND,
/*    */   
/* 27 */   MINUTE, MINUTE_SECOND, MINUTE_MICROSECOND,
/*    */   
/* 29 */   SECOND, SECOND_MICROSECOND,
/*    */   
/* 31 */   MICROSECOND;
/*    */   
/*    */   public final String name_lcase;
/*    */   
/*    */   MySqlIntervalUnit() {
/* 36 */     this.name_lcase = name().toLowerCase();
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\dialect\mysql\ast\expr\MySqlIntervalUnit.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
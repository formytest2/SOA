/*    */ package com.tranboot.client.druid.sql.builder;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.builder.impl.SQLDeleteBuilderImpl;
import com.tranboot.client.druid.sql.builder.impl.SQLSelectBuilderImpl;
import com.tranboot.client.druid.sql.builder.impl.SQLUpdateBuilderImpl;

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
/*    */ public class SQLBuilderFactory
/*    */ {
/*    */   public static SQLSelectBuilder createSelectSQLBuilder(String dbType) {
/* 25 */     return (SQLSelectBuilder)new SQLSelectBuilderImpl(dbType);
/*    */   }
/*    */   
/*    */   public static SQLSelectBuilder createSelectSQLBuilder(String sql, String dbType) {
/* 29 */     return (SQLSelectBuilder)new SQLSelectBuilderImpl(sql, dbType);
/*    */   }
/*    */   
/*    */   public static SQLDeleteBuilder createDeleteBuilder(String dbType) {
/* 33 */     return (SQLDeleteBuilder)new SQLDeleteBuilderImpl(dbType);
/*    */   }
/*    */   
/*    */   public static SQLDeleteBuilder createDeleteBuilder(String sql, String dbType) {
/* 37 */     return (SQLDeleteBuilder)new SQLDeleteBuilderImpl(sql, dbType);
/*    */   }
/*    */   
/*    */   public static SQLUpdateBuilder createUpdateBuilder(String dbType) {
/* 41 */     return (SQLUpdateBuilder)new SQLUpdateBuilderImpl(dbType);
/*    */   }
/*    */   
/*    */   public static SQLUpdateBuilder createUpdateBuilder(String sql, String dbType) {
/* 45 */     return (SQLUpdateBuilder)new SQLUpdateBuilderImpl(sql, dbType);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\builder\SQLBuilderFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
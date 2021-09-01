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
/*    */ public enum SQLObjectType
/*    */ {
/* 19 */   TABLE("TABLE"),
/* 20 */   FUNCTION("FUNCTION"),
/* 21 */   PROCEDURE("PROCEDURE"),
/* 22 */   USER("USER"),
/* 23 */   DATABASE("DATABASE"),
/* 24 */   ROLE("ROLE"),
/* 25 */   PROJECT("PROJECT"),
/* 26 */   PACKAGE("PACKAGE"),
/* 27 */   RESOURCE("RESOURCE"),
/* 28 */   INSTANCE("INSTANCE"),
/* 29 */   JOB("JOB"),
/* 30 */   VOLUME("VOLUME"),
/* 31 */   OfflineModel("OFFLINEMODEL"),
/* 32 */   XFLOW("XFLOW");
/*    */   
/*    */   public final String name;
/*    */   
/*    */   public final String name_lcase;
/*    */   
/*    */   SQLObjectType(String name) {
/* 39 */     this.name = name;
/* 40 */     this.name_lcase = name.toLowerCase();
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\statement\SQLObjectType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
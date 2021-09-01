/*    */ package com.tranboot.client.druid.sql.ast.expr;
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
/*    */ public enum SQLUnaryOperator
/*    */ {
/* 19 */   Plus("+"),
/* 20 */   Negative("-"),
/* 21 */   Not("!"),
/* 22 */   Compl("~"),
/* 23 */   Prior("PRIOR"),
/* 24 */   ConnectByRoot("CONNECT BY"),
/* 25 */   BINARY("BINARY"),
/* 26 */   RAW("RAW"),
/* 27 */   NOT("NOT"),
/* 28 */   Pound("#");
/*    */   
/*    */   public final String name;
/*    */ 
/*    */   
/*    */   SQLUnaryOperator(String name) {
/* 34 */     this.name = name;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\expr\SQLUnaryOperator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
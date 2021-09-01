/*    */ package com.tranboot.client.druid.sql.parser;
/*    */ 
/*    */ import java.io.Serializable;

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
/*    */ public class ParserException
/*    */   extends RuntimeException
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public ParserException() {}
/*    */   
/*    */   public ParserException(String message) {
/* 28 */     super(message);
/*    */   }
/*    */   
/*    */   public ParserException(String message, Throwable e) {
/* 32 */     super(message, e);
/*    */   }
/*    */   
/*    */   public ParserException(String message, int line, int col) {
/* 36 */     super(message);
/*    */   }
/*    */   
/*    */   public ParserException(Throwable ex, String ksql) {
/* 40 */     super("parse error. detail message is :\n" + ex.getMessage() + "\nsource sql is : \n" + ksql, ex);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\parser\ParserException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
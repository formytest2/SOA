/*    */ package com.tranboot.client.druid.sql.parser;
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
/*    */ public class NotAllowCommentException
/*    */   extends ParserException
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public NotAllowCommentException() {
/* 23 */     this("comment not allow");
/*    */   }
/*    */   
/*    */   public NotAllowCommentException(String message, Throwable e) {
/* 27 */     super(message, e);
/*    */   }
/*    */   
/*    */   public NotAllowCommentException(String message) {
/* 31 */     super(message);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\parser\NotAllowCommentException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
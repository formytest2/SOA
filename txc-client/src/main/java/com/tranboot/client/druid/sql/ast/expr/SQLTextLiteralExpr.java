/*    */ package com.tranboot.client.druid.sql.ast.expr;
/*    */ 
/*    */ import com.tranboot.client.druid.sql.ast.SQLExprImpl;

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
/*    */ public abstract class SQLTextLiteralExpr
/*    */   extends SQLExprImpl
/*    */   implements SQLLiteralExpr
/*    */ {
/*    */   protected String text;
/*    */   
/*    */   public SQLTextLiteralExpr() {}
/*    */   
/*    */   public SQLTextLiteralExpr(String text) {
/* 30 */     this.text = text;
/*    */   }
/*    */   
/*    */   public String getText() {
/* 34 */     return this.text;
/*    */   }
/*    */   
/*    */   public void setText(String text) {
/* 38 */     this.text = text;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 43 */     int prime = 31;
/* 44 */     int result = 1;
/* 45 */     result = 31 * result + ((this.text == null) ? 0 : this.text.hashCode());
/* 46 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 51 */     if (this == obj) {
/* 52 */       return true;
/*    */     }
/* 54 */     if (obj == null) {
/* 55 */       return false;
/*    */     }
/* 57 */     if (getClass() != obj.getClass()) {
/* 58 */       return false;
/*    */     }
/* 60 */     SQLTextLiteralExpr other = (SQLTextLiteralExpr)obj;
/* 61 */     if (this.text == null) {
/* 62 */       if (other.text != null) {
/* 63 */         return false;
/*    */       }
/* 65 */     } else if (!this.text.equals(other.text)) {
/* 66 */       return false;
/*    */     } 
/* 68 */     return true;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\ast\expr\SQLTextLiteralExpr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
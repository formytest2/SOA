/*    */ package com.tranboot.client.druid.sql.visitor.functions;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLMethodInvokeExpr;
import com.tranboot.client.druid.sql.visitor.SQLASTVisitor;
import com.tranboot.client.druid.sql.visitor.SQLEvalVisitor;

import java.math.BigDecimal;

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
/*    */ public class Char
/*    */   implements Function
/*    */ {
/* 28 */   public static final Char instance = new Char();
/*    */   
/*    */   public Object eval(SQLEvalVisitor visitor, SQLMethodInvokeExpr x) {
/* 31 */     if (x.getParameters().size() == 0) {
/* 32 */       return SQLEvalVisitor.EVAL_ERROR;
/*    */     }
/*    */     
/* 35 */     StringBuffer buf = new StringBuffer(x.getParameters().size());
/* 36 */     for (SQLExpr param : x.getParameters()) {
/* 37 */       param.accept((SQLASTVisitor)visitor);
/*    */       
/* 39 */       Object paramValue = param.getAttributes().get("eval.value");
/*    */       
/* 41 */       if (paramValue instanceof Number) {
/* 42 */         int charCode = ((Number)paramValue).intValue();
/* 43 */         buf.append((char)charCode); continue;
/* 44 */       }  if (paramValue instanceof String) {
/*    */         try {
/* 46 */           int charCode = (new BigDecimal((String)paramValue)).intValue();
/* 47 */           buf.append((char)charCode);
/* 48 */         } catch (NumberFormatException numberFormatException) {}
/*    */         continue;
/*    */       } 
/* 51 */       return SQLEvalVisitor.EVAL_ERROR;
/*    */     } 
/*    */ 
/*    */     
/* 55 */     return buf.toString();
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\visitor\functions\Char.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
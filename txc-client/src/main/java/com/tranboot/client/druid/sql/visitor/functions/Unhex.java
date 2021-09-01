/*    */ package com.tranboot.client.druid.sql.visitor.functions;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLMethodInvokeExpr;
import com.tranboot.client.druid.sql.visitor.SQLASTVisitor;
import com.tranboot.client.druid.sql.visitor.SQLEvalVisitor;
import com.tranboot.client.druid.util.HexBin;

import java.io.UnsupportedEncodingException;

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
/*    */ 
/*    */ 
/*    */ public class Unhex
/*    */   implements Function
/*    */ {
/* 30 */   public static final Unhex instance = new Unhex();
/*    */   
/*    */   public Object eval(SQLEvalVisitor visitor, SQLMethodInvokeExpr x) {
/* 33 */     if (x.getParameters().size() != 1) {
/* 34 */       return SQLEvalVisitor.EVAL_ERROR;
/*    */     }
/*    */     
/* 37 */     SQLExpr param0 = x.getParameters().get(0);
/*    */     
/* 39 */     if (param0 instanceof SQLMethodInvokeExpr) {
/* 40 */       SQLMethodInvokeExpr paramMethodExpr = (SQLMethodInvokeExpr)param0;
/* 41 */       if (paramMethodExpr.getMethodName().equalsIgnoreCase("hex")) {
/* 42 */         SQLExpr subParamExpr = paramMethodExpr.getParameters().get(0);
/* 43 */         subParamExpr.accept((SQLASTVisitor)visitor);
/*    */         
/* 45 */         Object object = subParamExpr.getAttributes().get("eval.value");
/* 46 */         if (object == null) {
/* 47 */           x.putAttribute("eval.expr", subParamExpr);
/* 48 */           return SQLEvalVisitor.EVAL_ERROR;
/*    */         } 
/*    */         
/* 51 */         return object;
/*    */       } 
/*    */     } 
/*    */     
/* 55 */     param0.accept((SQLASTVisitor)visitor);
/*    */     
/* 57 */     Object param0Value = param0.getAttributes().get("eval.value");
/* 58 */     if (param0Value == null) {
/* 59 */       return SQLEvalVisitor.EVAL_ERROR;
/*    */     }
/*    */     
/* 62 */     if (param0Value instanceof String) {
/* 63 */       String result; byte[] bytes = HexBin.decode((String)param0Value);
/* 64 */       if (bytes == null) {
/* 65 */         return SQLEvalVisitor.EVAL_VALUE_NULL;
/*    */       }
/*    */ 
/*    */       
/*    */       try {
/* 70 */         result = new String(bytes, "UTF-8");
/* 71 */       } catch (UnsupportedEncodingException e) {
/* 72 */         throw new IllegalStateException(e.getMessage(), e);
/*    */       } 
/* 74 */       return result;
/*    */     } 
/*    */     
/* 77 */     return SQLEvalVisitor.EVAL_ERROR;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\visitor\functions\Unhex.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
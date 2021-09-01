/*    */ package com.tranboot.client.druid.sql.visitor.functions;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLMethodInvokeExpr;
import com.tranboot.client.druid.sql.visitor.SQLASTVisitor;
import com.tranboot.client.druid.sql.visitor.SQLEvalVisitor;

import java.util.List;

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
/*    */ public class Substring
/*    */   implements Function
/*    */ {
/* 28 */   public static final Substring instance = new Substring();
/*    */   public Object eval(SQLEvalVisitor visitor, SQLMethodInvokeExpr x) {
/*    */     String result;
/* 31 */     List<SQLExpr> params = x.getParameters();
/* 32 */     int paramSize = params.size();
/* 33 */     if (paramSize != 2 && paramSize != 3) {
/* 34 */       return SQLEvalVisitor.EVAL_ERROR;
/*    */     }
/*    */     
/* 37 */     SQLExpr param0 = params.get(0);
/* 38 */     SQLExpr param1 = params.get(1);
/* 39 */     param0.accept((SQLASTVisitor)visitor);
/* 40 */     param1.accept((SQLASTVisitor)visitor);
/*    */     
/* 42 */     Object param0Value = param0.getAttributes().get("eval.value");
/* 43 */     Object param1Value = param1.getAttributes().get("eval.value");
/* 44 */     if (param0Value == null || param1Value == null) {
/* 45 */       return SQLEvalVisitor.EVAL_ERROR;
/*    */     }
/*    */     
/* 48 */     String str = param0Value.toString();
/* 49 */     int index = ((Number)param1Value).intValue();
/*    */     
/* 51 */     if (paramSize == 2) {
/*    */       
/* 53 */       if (index <= 0) {
/* 54 */         int lastIndex = str.length() + index;
/* 55 */         return str.substring(lastIndex);
/*    */       } 
/*    */       
/* 58 */       return str.substring(index - 1);
/*    */     } 
/*    */     
/* 61 */     SQLExpr param2 = params.get(2);
/* 62 */     param2.accept((SQLASTVisitor)visitor);
/* 63 */     Object param2Value = param2.getAttributes().get("eval.value");
/* 64 */     if (param2Value == null) {
/* 65 */       return SQLEvalVisitor.EVAL_ERROR;
/*    */     }
/*    */     
/* 68 */     int len = ((Number)param2Value).intValue();
/*    */ 
/*    */     
/* 71 */     if (index <= 0) {
/* 72 */       int lastIndex = str.length() + index;
/* 73 */       result = str.substring(lastIndex);
/*    */     } else {
/* 75 */       result = str.substring(index - 1);
/*    */     } 
/*    */     
/* 78 */     if (len > result.length()) {
/* 79 */       return result;
/*    */     }
/* 81 */     return result.substring(0, len);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\visitor\functions\Substring.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
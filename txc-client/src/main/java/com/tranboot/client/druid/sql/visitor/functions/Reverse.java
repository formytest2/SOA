/*    */ package com.tranboot.client.druid.sql.visitor.functions;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLMethodInvokeExpr;
import com.tranboot.client.druid.sql.visitor.SQLASTVisitor;
import com.tranboot.client.druid.sql.visitor.SQLEvalVisitor;

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
/*    */ public class Reverse
/*    */   implements Function
/*    */ {
/* 26 */   public static final Reverse instance = new Reverse();
/*    */   
/*    */   public Object eval(SQLEvalVisitor visitor, SQLMethodInvokeExpr x) {
/* 29 */     if (x.getParameters().size() != 1) {
/* 30 */       return SQLEvalVisitor.EVAL_ERROR;
/*    */     }
/*    */     
/* 33 */     SQLExpr param0 = x.getParameters().get(0);
/* 34 */     param0.accept((SQLASTVisitor)visitor);
/*    */     
/* 36 */     Object param0Value = param0.getAttributes().get("eval.value");
/* 37 */     if (param0Value == null) {
/* 38 */       return SQLEvalVisitor.EVAL_ERROR;
/*    */     }
/*    */     
/* 41 */     String strValue = param0Value.toString();
/*    */     
/* 43 */     StringBuilder buf = new StringBuilder();
/* 44 */     for (int i = strValue.length() - 1; i >= 0; i--) {
/* 45 */       buf.append(strValue.charAt(i));
/*    */     }
/* 47 */     String result = buf.toString();
/* 48 */     return result;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\visitor\functions\Reverse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
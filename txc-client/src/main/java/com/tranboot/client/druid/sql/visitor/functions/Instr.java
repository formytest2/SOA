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
/*    */ public class Instr
/*    */   implements Function
/*    */ {
/* 26 */   public static final Instr instance = new Instr();
/*    */   
/*    */   public Object eval(SQLEvalVisitor visitor, SQLMethodInvokeExpr x) {
/* 29 */     if (x.getParameters().size() != 2) {
/* 30 */       return SQLEvalVisitor.EVAL_ERROR;
/*    */     }
/*    */     
/* 33 */     SQLExpr param0 = x.getParameters().get(0);
/* 34 */     SQLExpr param1 = x.getParameters().get(1);
/* 35 */     param0.accept((SQLASTVisitor)visitor);
/* 36 */     param1.accept((SQLASTVisitor)visitor);
/*    */     
/* 38 */     Object param0Value = param0.getAttributes().get("eval.value");
/* 39 */     Object param1Value = param1.getAttributes().get("eval.value");
/* 40 */     if (param0Value == null || param1Value == null) {
/* 41 */       return SQLEvalVisitor.EVAL_ERROR;
/*    */     }
/*    */     
/* 44 */     String strValue0 = param0Value.toString();
/* 45 */     String strValue1 = param1Value.toString();
/*    */     
/* 47 */     int result = strValue0.indexOf(strValue1) + 1;
/* 48 */     return Integer.valueOf(result);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\visitor\functions\Instr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
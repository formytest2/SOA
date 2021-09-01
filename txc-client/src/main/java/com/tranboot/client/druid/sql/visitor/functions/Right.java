/*    */ package com.tranboot.client.druid.sql.visitor.functions;
/*    */ 
/*    */

import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLMethodInvokeExpr;
import com.tranboot.client.druid.sql.visitor.SQLASTVisitor;
import com.tranboot.client.druid.sql.visitor.SQLEvalVisitor;
import com.tranboot.client.druid.sql.visitor.SQLEvalVisitorUtils;

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
/*    */ public class Right
/*    */   implements Function
/*    */ {
/* 27 */   public static final Right instance = new Right();
/*    */   
/*    */   public Object eval(SQLEvalVisitor visitor, SQLMethodInvokeExpr x) {
/* 30 */     if (x.getParameters().size() != 2) {
/* 31 */       return SQLEvalVisitor.EVAL_ERROR;
/*    */     }
/*    */     
/* 34 */     SQLExpr param0 = x.getParameters().get(0);
/* 35 */     SQLExpr param1 = x.getParameters().get(1);
/* 36 */     param0.accept((SQLASTVisitor)visitor);
/* 37 */     param1.accept((SQLASTVisitor)visitor);
/*    */     
/* 39 */     Object param0Value = param0.getAttributes().get("eval.value");
/* 40 */     Object param1Value = param1.getAttributes().get("eval.value");
/* 41 */     if (param0Value == null || param1Value == null) {
/* 42 */       return SQLEvalVisitor.EVAL_ERROR;
/*    */     }
/*    */     
/* 45 */     String strValue = param0Value.toString();
/* 46 */     int intValue = SQLEvalVisitorUtils.castToInteger(param1Value).intValue();
/*    */     
/* 48 */     int start = strValue.length() - intValue;
/* 49 */     if (start < 0) {
/* 50 */       start = 0;
/*    */     }
/* 52 */     String result = strValue.substring(start, strValue.length());
/* 53 */     return result;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\visitor\functions\Right.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
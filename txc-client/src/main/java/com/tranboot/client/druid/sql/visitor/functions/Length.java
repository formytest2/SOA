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
/*    */ public class Length
/*    */   implements Function
/*    */ {
/* 26 */   public static final Length instance = new Length();
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
/* 43 */     int result = strValue.length();
/* 44 */     return Integer.valueOf(result);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\visitor\functions\Length.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
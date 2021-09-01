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
/*    */ 
/*    */ public class Ascii
/*    */   implements Function
/*    */ {
/* 27 */   public static final Ascii instance = new Ascii();
/*    */   
/*    */   public Object eval(SQLEvalVisitor visitor, SQLMethodInvokeExpr x) {
/* 30 */     if (x.getParameters().size() == 0) {
/* 31 */       return SQLEvalVisitor.EVAL_ERROR;
/*    */     }
/* 33 */     SQLExpr param = x.getParameters().get(0);
/* 34 */     param.accept((SQLASTVisitor)visitor);
/*    */     
/* 36 */     Object paramValue = param.getAttributes().get("eval.value");
/* 37 */     if (paramValue == null) {
/* 38 */       return SQLEvalVisitor.EVAL_ERROR;
/*    */     }
/*    */     
/* 41 */     if (paramValue == SQLEvalVisitor.EVAL_VALUE_NULL) {
/* 42 */       return SQLEvalVisitor.EVAL_VALUE_NULL;
/*    */     }
/*    */     
/* 45 */     String strValue = paramValue.toString();
/* 46 */     if (strValue.length() == 0) {
/* 47 */       return Integer.valueOf(0);
/*    */     }
/*    */     
/* 50 */     int ascii = strValue.charAt(0);
/* 51 */     return Integer.valueOf(ascii);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\visitor\functions\Ascii.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
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
/*    */ public class Lpad
/*    */   implements Function
/*    */ {
/* 28 */   public static final Lpad instance = new Lpad();
/*    */   
/*    */   public Object eval(SQLEvalVisitor visitor, SQLMethodInvokeExpr x) {
/* 31 */     List<SQLExpr> params = x.getParameters();
/* 32 */     int paramSize = params.size();
/* 33 */     if (paramSize != 3) {
/* 34 */       return SQLEvalVisitor.EVAL_ERROR;
/*    */     }
/*    */     
/* 37 */     SQLExpr param0 = params.get(0);
/* 38 */     SQLExpr param1 = params.get(1);
/* 39 */     SQLExpr param2 = params.get(2);
/*    */     
/* 41 */     param0.accept((SQLASTVisitor)visitor);
/* 42 */     param1.accept((SQLASTVisitor)visitor);
/* 43 */     param2.accept((SQLASTVisitor)visitor);
/*    */     
/* 45 */     Object param0Value = param0.getAttributes().get("eval.value");
/* 46 */     Object param1Value = param1.getAttributes().get("eval.value");
/* 47 */     Object param2Value = param2.getAttributes().get("eval.value");
/* 48 */     if (param0Value == null || param1Value == null || param2Value == null) {
/* 49 */       return SQLEvalVisitor.EVAL_ERROR;
/*    */     }
/*    */     
/* 52 */     String strValue0 = param0Value.toString();
/* 53 */     int len = ((Number)param1Value).intValue();
/* 54 */     String strValue1 = param2Value.toString();
/*    */     
/* 56 */     String result = strValue0;
/* 57 */     if (result.length() > len) {
/* 58 */       return result.substring(0, len);
/*    */     }
/*    */     
/* 61 */     while (result.length() < len) {
/* 62 */       result = strValue1 + result;
/*    */     }
/*    */     
/* 65 */     return result;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\visitor\functions\Lpad.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
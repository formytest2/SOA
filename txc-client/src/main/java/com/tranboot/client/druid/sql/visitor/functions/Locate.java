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
/*    */ public class Locate
/*    */   implements Function
/*    */ {
/* 28 */   public static final Locate instance = new Locate();
/*    */   
/*    */   public Object eval(SQLEvalVisitor visitor, SQLMethodInvokeExpr x) {
/* 31 */     List<SQLExpr> params = x.getParameters();
/* 32 */     int paramSize = params.size();
/* 33 */     if (paramSize != 2 && paramSize != 3) {
/* 34 */       return SQLEvalVisitor.EVAL_ERROR;
/*    */     }
/*    */     
/* 37 */     SQLExpr param0 = params.get(0);
/* 38 */     SQLExpr param1 = params.get(1);
/* 39 */     SQLExpr param2 = null;
/*    */     
/* 41 */     param0.accept((SQLASTVisitor)visitor);
/* 42 */     param1.accept((SQLASTVisitor)visitor);
/* 43 */     if (paramSize == 3) {
/* 44 */       param2 = params.get(2);
/* 45 */       param2.accept((SQLASTVisitor)visitor);
/*    */     } 
/*    */     
/* 48 */     Object param0Value = param0.getAttributes().get("eval.value");
/* 49 */     Object param1Value = param1.getAttributes().get("eval.value");
/* 50 */     if (param0Value == null || param1Value == null) {
/* 51 */       return SQLEvalVisitor.EVAL_ERROR;
/*    */     }
/*    */     
/* 54 */     String strValue0 = param0Value.toString();
/* 55 */     String strValue1 = param1Value.toString();
/*    */     
/* 57 */     if (paramSize == 2) {
/* 58 */       int i = strValue1.indexOf(strValue0) + 1;
/* 59 */       return Integer.valueOf(i);
/*    */     } 
/*    */     
/* 62 */     Object param2Value = param2.getAttributes().get("eval.value");
/* 63 */     int start = ((Number)param2Value).intValue();
/*    */     
/* 65 */     int result = strValue1.indexOf(strValue0, start + 1) + 1;
/* 66 */     return Integer.valueOf(result);
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\visitor\functions\Locate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
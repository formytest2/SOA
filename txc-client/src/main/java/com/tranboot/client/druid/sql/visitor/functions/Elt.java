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
/*    */ public class Elt
/*    */   implements Function
/*    */ {
/* 26 */   public static final Elt instance = new Elt();
/*    */   
/*    */   public Object eval(SQLEvalVisitor visitor, SQLMethodInvokeExpr x) {
/* 29 */     if (x.getParameters().size() <= 1) {
/* 30 */       return SQLEvalVisitor.EVAL_ERROR;
/*    */     }
/*    */     
/* 33 */     SQLExpr param0 = x.getParameters().get(0);
/* 34 */     param0.accept((SQLASTVisitor)visitor);
/*    */     
/* 36 */     Object param0Value = param0.getAttributes().get("eval.value");
/*    */     
/* 38 */     if (!(param0Value instanceof Number)) {
/* 39 */       return SQLEvalVisitor.EVAL_ERROR;
/*    */     }
/* 41 */     int param0IntValue = ((Number)param0Value).intValue();
/*    */     
/* 43 */     if (param0IntValue >= x.getParameters().size()) {
/* 44 */       return null;
/*    */     }
/*    */     
/* 47 */     SQLExpr item = x.getParameters().get(param0IntValue);
/* 48 */     item.accept((SQLASTVisitor)visitor);
/*    */     
/* 50 */     Object itemValue = item.getAttributes().get("eval.value");
/* 51 */     return itemValue;
/*    */   }
/*    */ }


/* Location:              D:\projects\decompile\txc-client-4.0.5-release\!\com\tranboot\client\druid\sql\visitor\functions\Elt.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */